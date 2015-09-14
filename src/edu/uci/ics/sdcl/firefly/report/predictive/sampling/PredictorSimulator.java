package edu.uci.ics.sdcl.firefly.report.predictive.sampling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.report.predictive.AnswerData;
import edu.uci.ics.sdcl.firefly.report.predictive.Outcome;
import edu.uci.ics.sdcl.firefly.report.predictive.PositiveVoting;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

public class PredictorSimulator {

	/** Levels of answers from 1 to 20 and the list of precision values calculated for each sample */
	private ArrayList<DataPoint> outcomes_PositiveVoting = new ArrayList<DataPoint>();
	
	/** Levels of answers from 1 to 20 and the list of precision values calculated for each sample */
	private ArrayList<DataPoint> outcomes_MajorityVoting = new ArrayList<DataPoint>();
	
	private String[] fileNameList = {"HIT01_8", "HIT02_24", "HIT03_6", "HIT04_7",
			"HIT05_35","HIT06_51","HIT07_33","HIT08_54"};

	private HashMap<String,String> bugCoveringMap;
	
	
	public class DataPoint {
		
		Double precision;
		Double recall;
		
		public DataPoint(){}
		
		HashMap<String, Outcome> fileNameOutcomeMap = new HashMap<String, Outcome>();
		
		public void computeAverages(){
			
			ArrayList<Double> precisionValues = new ArrayList<Double>();
			ArrayList<Double> recallValues = new ArrayList<Double>();
			
			for(String key: fileNameOutcomeMap.keySet()){
				Outcome outcome = fileNameOutcomeMap.get(key);
				precisionValues.add(outcome.precision);
				recallValues.add(outcome.recall);
			}
			precision = average(precisionValues);
			recall = average(recallValues);
		}
		
		
		private Double average(ArrayList<Double> values){
			
			Double total = 0.0;
			for(int i=0; i<values.size();i++){
				total = total + values.get(i);
			}
			return total/values.size();
		}
	}//DataPoint internal class
	
	
	public PredictorSimulator(){
		
		//Obtain bug covering question list
		PropertyManager manager = PropertyManager.initializeSingleton();
		bugCoveringMap = new HashMap<String,String>();
		String[] listOfBugPointingQuestions = manager.bugCoveringList.split(";");
		for(String questionID:listOfBugPointingQuestions){
			bugCoveringMap.put(questionID,questionID);
		}
	}
	
	public void computePositiveVoting(ArrayList<HashMap<String,Microtask>> listOfMicrotaskMaps){
		
		for(int i=0; i<listOfMicrotaskMaps.size();i++){
			
			String key = new Integer(i).toString();
			
			HashMap<String, Microtask> microtaskMap = listOfMicrotaskMaps.get(i);
		
			Integer totalDifferentWorkersAmongHITs = countWorkers(microtaskMap, null);
			
			DataPoint positiveVDataPoint = new DataPoint();
			DataPoint majorityVDataPoint = new DataPoint();
			
			for(String fileName: fileNameList){
				HashMap<String, ArrayList<String>> answerMap = extractAnswersForFileName(microtaskMap,fileName);
				Integer workerCountPerHIT = countWorkers(microtaskMap,fileName);
				AnswerData data = new AnswerData(fileName,answerMap,bugCoveringMap,workerCountPerHIT,totalDifferentWorkersAmongHITs);
				Outcome outcome = computePositiveVotingDataPoint(data);
				positiveVDataPoint.fileNameOutcomeMap.put(fileName, outcome);
				
				outcome = computeMajorityVotingDataPoint(data);
				majorityVDataPoint.fileNameOutcomeMap.put(fileName, outcome);
			}
			
			this.outcomes_PositiveVoting.add(positiveVDataPoint);
			this.outcomes_MajorityVoting.add(majorityVDataPoint);

		}
	}
	
	private Outcome computeMajorityVotingDataPoint(AnswerData data) {
		
		return null;
	}

	private Outcome computePositiveVotingDataPoint(AnswerData answerData){
		PositiveVoting predictor = new PositiveVoting();
		
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
		Boolean signal = positiveVoting.computeSignal(data);
		positiveVoting.computeSignalStrength(data);
		positiveVoting.computeNumberOfWorkers(data);
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

}
