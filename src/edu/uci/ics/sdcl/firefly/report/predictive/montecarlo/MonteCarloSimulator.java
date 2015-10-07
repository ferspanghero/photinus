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
import edu.uci.ics.sdcl.firefly.util.MicrotaskMapUtil;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;
import edu.uci.ics.sdcl.firefly.util.RoundDouble;

public class MonteCarloSimulator {

	/**  list of precision values calculated for each sample using Positive voting predictor*/
	private ArrayList<DataPoint> outcomes_PositiveVoting = new ArrayList<DataPoint>();

	/**  list of precision values calculated for each sample using Majority voting predictor*/
	private ArrayList<DataPoint> outcomes_MajorityVoting = new ArrayList<DataPoint>();

	private String[] fileNameList = {"HIT01_8", "HIT02_24", "HIT03_6", "HIT04_7", "HIT05_35","HIT06_51","HIT07_33","HIT08_54"};

	private HashMap<String,String> bugCoveringMap;

	private HashMap<String, DataPoint> positiveVoting_AverageDataPointByAnswerLevel = new HashMap<String,DataPoint>();

	private HashMap<String, DataPoint> majorityVoting_AverageDataPointByAnswerLevel = new HashMap<String,DataPoint>();

	private String outputFolder = "";


	public MonteCarloSimulator(String outputFolder){

		this.outputFolder = outputFolder;

		//Obtain bug covering question list
		PropertyManager manager = PropertyManager.initializeSingleton();
		bugCoveringMap = new HashMap<String,String>();
		String[] listOfBugPointingQuestions = manager.bugCoveringList.split(";");
		for(String questionID:listOfBugPointingQuestions){
			bugCoveringMap.put(questionID,questionID);
		}
	}


	private void computeVoting(ArrayList<HashMap<String,Microtask>> listOfMicrotaskMaps){

		for(int i=0; i<listOfMicrotaskMaps.size();i++){

			HashMap<String, Microtask> microtaskMap = listOfMicrotaskMaps.get(i);

			Integer totalDifferentWorkersAmongHITs = MicrotaskMapUtil.countWorkers(microtaskMap, null);

			DataPoint positiveVDataPoint = new DataPoint();
			DataPoint majorityVDataPoint = new DataPoint();

			for(String fileName: fileNameList){//each fileName is a Java method
				HashMap<String, ArrayList<String>> answerMap = MicrotaskMapUtil.extractAnswersForFileName(microtaskMap,fileName);
				Integer workerCountPerHIT = MicrotaskMapUtil.countWorkers(microtaskMap,fileName);
				AnswerData data = new AnswerData(fileName,answerMap,bugCoveringMap,workerCountPerHIT,totalDifferentWorkersAmongHITs);
				Predictor predictor = new PositiveVoting();
				Outcome outcome = computeDataPoint(data,predictor);
				positiveVDataPoint.fileNameOutcomeMap.put(fileName, outcome);

				predictor = new MajorityVoting();
				outcome = computeDataPoint(data,predictor);
				majorityVDataPoint.fileNameOutcomeMap.put(fileName, outcome);
			}

			positiveVDataPoint.totalAnswers = MicrotaskMapUtil.countAnswers(microtaskMap);
			majorityVDataPoint.totalAnswers = positiveVDataPoint.totalAnswers;

			positiveVDataPoint.computeAverages();//Compute the average precision and recall for all Java methods
			majorityVDataPoint.computeAverages();

			positiveVDataPoint.elapsedTime = MicrotaskMapUtil.computeElapsedTimeForAnswerLevels(microtaskMap);
			majorityVDataPoint.elapsedTime = positiveVDataPoint.elapsedTime;

			positiveVDataPoint.totalWorkers = new Double(totalDifferentWorkersAmongHITs);
			majorityVDataPoint.totalWorkers = new Double(totalDifferentWorkersAmongHITs);

			this.outcomes_PositiveVoting.add(positiveVDataPoint);
			this.outcomes_MajorityVoting.add(majorityVDataPoint);
		}
	}


	private void computeAverages(int sampleSize){

		String key = new Integer(sampleSize).toString();

		DataPoint point = this.computeAveragesForList(outcomes_PositiveVoting);
		this.positiveVoting_AverageDataPointByAnswerLevel.put(key, point);

		point = this.computeAveragesForList(outcomes_MajorityVoting);
		this.majorityVoting_AverageDataPointByAnswerLevel.put(key, point);	
	}

	private DataPoint computeAveragesForList(ArrayList<DataPoint> dataPointList){

		int size = dataPointList.size();

		Double sumAnswers=0.0;
		Double sumWorkers=0.0;

		Double sumTP=0.0;
		Double sumTN=0.0;
		Double sumFP=0.0;
		Double sumFN=0.0;
		Double sumElapsedTime=0.0;

		Double sumPrecision=0.0;
		Double sumRecall=0.0;

		Double sumFaultsLocated =0.0;
		Double sumNumberOfOutcomes = 0.0;
	
		for(DataPoint data: dataPointList){
			sumAnswers = sumAnswers + data.totalAnswers;
			sumWorkers = sumWorkers + data.totalWorkers;

			sumTP = sumTP + data.truePositives;
			sumFP = sumFP + data.falsePositives;
			sumTN = sumTN + data.trueNegatives;
			sumFN = sumFN + data.falseNegatives;

			sumPrecision = sumPrecision + data.averagePrecision;
			sumRecall = sumRecall + data.averageRecall;

			sumElapsedTime = sumElapsedTime + data.elapsedTime;
			
			sumFaultsLocated = sumFaultsLocated + data.faultsLocated;
			
			sumNumberOfOutcomes = sumNumberOfOutcomes + data.numberOfOutcomes;
		}

		//Averages
		DataPoint averageDataPoint = new DataPoint();

		averageDataPoint.totalAnswers = sumAnswers / size;
		averageDataPoint.totalWorkers = sumWorkers / size;

		averageDataPoint.falseNegatives = sumFN /size;
		averageDataPoint.falsePositives = sumFP / size;
		averageDataPoint.trueNegatives = sumTN / size;
		averageDataPoint.truePositives = sumTP / size;

		averageDataPoint.averagePrecision = sumPrecision / size;
		averageDataPoint.averageRecall = sumRecall / size;

		averageDataPoint.elapsedTime = sumElapsedTime /size;
		
		averageDataPoint.faultsLocated = sumFaultsLocated / size;
		
		averageDataPoint.numberOfOutcomes = sumNumberOfOutcomes;

		return averageDataPoint;
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

	//------------------------------------------------------------------------------------------------
	//PRINT FUNCTIONS
	
	public void printDataPointsToFile(int name){

		String nameStr = new Integer(name).toString();
		String destination = "C://firefly//MonteCarloSimulation//"+this.outputFolder+"//"+ nameStr+".csv";
		BufferedWriter log;

		try {
			log = new BufferedWriter(new FileWriter(destination));
			//Print file header

			log.write("#,"+DataPoint.getHeader("_PV")+","+DataPoint.getHeader("_MV")+"\n");

			for(int i=0; i<this.outcomes_MajorityVoting.size();i++){
				String index = new Integer(i).toString();
				String positiveVote_Outcomes = this.outcomes_PositiveVoting.get(i).toString();
				String majorytVote_Outcomes = this.outcomes_MajorityVoting.get(i).toString();

				log.write(index+","+positiveVote_Outcomes+","+majorytVote_Outcomes+"\n");
			}

			log.close();
			System.out.println("file written at: "+destination);
		} 
		catch (Exception e) {
			System.out.println("ERROR while processing file:" + destination);
			e.printStackTrace();
		}
	}

	private void printToFile(HashMap<String, DataPoint> positiveVotingAverageMap,
			HashMap<String, DataPoint> majorityVotingAverageMap,String name) {

		String nameStr = name+"_datapoint";
		String destination = "C://firefly//MonteCarloSimulation//DataPoints//"+ nameStr+".csv";
		BufferedWriter log;

		try {
			log = new BufferedWriter(new FileWriter(destination));
			//Print file header

			log.write("#,"+DataPoint.getHeader(" Positive Voting")+DataPoint.getHeader(" Majority Voting")+"\n");
			for(String key: positiveVotingAverageMap.keySet()){
				
				String line= key+","+positiveVotingAverageMap.get(key).toString();
				line = line +","+ majorityVotingAverageMap.get(key).toString(); 
				
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
	
	//------------------------------------------------------------------------------------------------------
	
	public void generateSiumalations(int populationSize, int numberOfSamples, 
			HashMap<String, Microtask> microtaskMap, String crowdName){

		for(int i=1;i<=populationSize-1;i++){

			//how many answers per question
			int sampleSize = i; 

			//Generate the samples
			RandomSampler sampling = new RandomSampler(sampleSize, numberOfSamples, populationSize);
			ArrayList<HashMap<String, Microtask>> listOfMicrotaskMaps =sampling.generateMicrotaskMap(microtaskMap);

			//Compute statistics for each sample
			computeVoting(listOfMicrotaskMaps);

			//Save samples with statistics to files
			printDataPointsToFile(sampleSize);

			//Takes the average of all samples		
			computeAverages(sampleSize);	
			
		}
		//print the averages samples from Majority voting and Positive voting
		printToFile(this.positiveVoting_AverageDataPointByAnswerLevel,this.majorityVoting_AverageDataPointByAnswerLevel,crowdName);
	}

	/** THis is used to test, but the actual call is made from class SimulationController */
	public static void main(String args[]){

		int populationSize = 20; //total answers per question
		int numberOfSamples = 10000; //how many simulated crowds

		MonteCarloSimulator sim = new MonteCarloSimulator("SamplingPredictor");

		FileSessionDTO dto = new FileSessionDTO();
		HashMap<String, Microtask> microtaskMap = (HashMap<String, Microtask>) dto.getMicrotasks();

		sim.generateSiumalations(populationSize, numberOfSamples, microtaskMap, "all workers");
	}

}


