package edu.uci.ics.sdcl.firefly.report.predictive.montecarlo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileSessionDTO;
import edu.uci.ics.sdcl.firefly.report.predictive.AnswerData;
import edu.uci.ics.sdcl.firefly.report.predictive.DataPoint;
import edu.uci.ics.sdcl.firefly.report.predictive.MajorityVoting;
import edu.uci.ics.sdcl.firefly.report.predictive.Outcome;
import edu.uci.ics.sdcl.firefly.report.predictive.PositiveVoting;
import edu.uci.ics.sdcl.firefly.report.predictive.Predictor;
import edu.uci.ics.sdcl.firefly.report.predictive.AnswerConfidenceCounter.Output;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

public class MonteCarloSimulator {

	/**  list of precision values calculated for each sample */
	private ArrayList<DataPoint> outcomes_PositiveVoting = new ArrayList<DataPoint>();

	/**  list of precision values calculated for each sample */
	private ArrayList<DataPoint> outcomes_MajorityVoting = new ArrayList<DataPoint>();

	private String[] fileNameList = {"HIT01_8", "HIT02_24", "HIT03_6", "HIT04_7",
			"HIT05_35","HIT06_51","HIT07_33","HIT08_54"};

	private HashMap<String,String> bugCoveringMap;




	public MonteCarloSimulator(){

		//Obtain bug covering question list
		PropertyManager manager = PropertyManager.initializeSingleton();
		bugCoveringMap = new HashMap<String,String>();
		String[] listOfBugPointingQuestions = manager.bugCoveringList.split(";");
		for(String questionID:listOfBugPointingQuestions){
			bugCoveringMap.put(questionID,questionID);
		}
	}

	public void computeVoting(ArrayList<HashMap<String,Microtask>> listOfMicrotaskMaps){

		for(int i=0; i<listOfMicrotaskMaps.size();i++){

			HashMap<String, Microtask> microtaskMap = listOfMicrotaskMaps.get(i);

			Integer totalDifferentWorkersAmongHITs = countWorkers(microtaskMap, null);

			DataPoint positiveVDataPoint = new DataPoint();
			DataPoint majorityVDataPoint = new DataPoint();

			for(String fileName: fileNameList){//each fileName is a Java method
				HashMap<String, ArrayList<String>> answerMap = extractAnswersForFileName(microtaskMap,fileName);
				Integer workerCountPerHIT = countWorkers(microtaskMap,fileName);
				AnswerData data = new AnswerData(fileName,answerMap,bugCoveringMap,workerCountPerHIT,totalDifferentWorkersAmongHITs);
				Predictor predictor = new PositiveVoting();
				Outcome outcome = computeDataPoint(data,predictor);
				positiveVDataPoint.fileNameOutcomeMap.put(fileName, outcome);

				predictor = new MajorityVoting();
				outcome = computeDataPoint(data,predictor);
				majorityVDataPoint.fileNameOutcomeMap.put(fileName, outcome);
			}
			positiveVDataPoint.computeAverages();//Compute the average precision and recall for all Java methods
			majorityVDataPoint.computeAverages();

			this.outcomes_PositiveVoting.add(positiveVDataPoint);
			this.outcomes_MajorityVoting.add(majorityVDataPoint);
		}
	}

	private Outcome computeDataPoint(AnswerData answerData, Predictor predictor) {

		Outcome outcome = new Outcome(null,
				answerData.getHitFileName(),
				predictor.getName(),
				predictor.computeSignal(answerData),
				predictor.computeSignalStrength(answerData),
				predictor.computeNumberOfWorkers(answerData),
				answerData.getTotalAnswers(),
				predictor.getThreshold(),
				predictor.getTruePositives(),
				predictor.getTrueNegatives(),
				predictor.getFalsePositives(),
				predictor.getFalseNegatives(),
				answerData.getWorkerCount(),
				answerData.getDifferentWorkersAmongHITs());

		return outcome;
	}



	//-------------------------------------------------------------------------------------------------------
	private static Integer countWorkers(
			HashMap<String, Microtask> filteredMicrotaskMap, String fileName) {

		HashMap<String,String> workerMap = new HashMap<String, String>();
		for(Microtask task: filteredMicrotaskMap.values()){
			if(fileName==null || task.getFileName().compareTo(fileName)==0){
				for(Answer answer:task.getAnswerList()){
					String workerID = answer.getWorkerId();
					workerMap.put(workerID, workerID);
				}
			}
		}
		return workerMap.size();
	}

	private static HashMap<String, ArrayList<String>>  extractAnswersForFileName(
			HashMap<String, Microtask> microtaskMap,String fileName){

		int answerCount = 0;
		HashMap<String, ArrayList<String>> resultMap = new HashMap<String, ArrayList<String>>();

		for(Microtask task:microtaskMap.values() ){
			//System.out.println("fileName: "+fileName+":"+task.getFileName());
			if(task.getFileName().compareTo(fileName)==0){
				resultMap.put(task.getID().toString(),task.getAnswerOptions());
				answerCount = answerCount+task.getAnswerOptions().size();
			}
		}
		//System.out.println(fileName+" has "+answerCount+" answers");
		return resultMap;
	}
	//----------------------------------------------------------------------------------------------------------

	private String getHeader(){
		return "Sample,Average Precision_PV,Average Recall_PV,Average Precision_MV,Average Recall_MV";
	}

	public void printDataPointsToFile(int name){

		String nameStr = new Integer(name).toString();
		String destination = "C://firefly//SamplingPredictor//"+ nameStr+".csv";
		BufferedWriter log;

		try {
			log = new BufferedWriter(new FileWriter(destination));
			//Print file header

			log.write(getHeader()+"\n");

			for(int i=0; i<this.outcomes_MajorityVoting.size();i++){
				DataPoint datapointPV = this.outcomes_PositiveVoting.get(i);
				DataPoint datapointMV = this.outcomes_MajorityVoting.get(i);

				String line= new Integer(i).toString() +","+
						datapointPV.averagePrecision.toString()+","+
						datapointPV.averageRecall.toString()+","+
						datapointMV.averagePrecision.toString()+","+
						datapointMV.averageRecall.toString();

				log.write(line+"\n");
			}

			log.close();
			System.out.println("file written at: "+destination);
		} 
		catch (Exception e) {
			System.out.println("ERROR while processing file:" + destination);
			e.printStackTrace();
		}
	}


	public static void main(String args[]){

		int populationSize = 20; //total answers per question
		int numberOfSamples = 10000; //how many simulated crowds

		for(int i=1;i<=19;i++){
			int sampleSize = i; //how many answers per question
			
			RandomSampler sampling = new RandomSampler(sampleSize, numberOfSamples, populationSize);
			FileSessionDTO dto =  new FileSessionDTO();

			HashMap<String, Microtask> microtaskMap = (HashMap<String, Microtask>) dto.getMicrotasks();

			ArrayList<HashMap<String, Microtask>> listOfMicrotaskMaps =sampling.generateMicrotaskMap(microtaskMap);
			MonteCarloSimulator sim = new MonteCarloSimulator();

			sim.computeVoting(listOfMicrotaskMaps);

			sim.printDataPointsToFile(sampleSize);
		}
	}

}


