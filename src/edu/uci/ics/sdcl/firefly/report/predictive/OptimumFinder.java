package edu.uci.ics.sdcl.firefly.report.predictive;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap; 


import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask; 
import edu.uci.ics.sdcl.firefly.report.descriptive.FileSessionDTO;
import edu.uci.ics.sdcl.firefly.report.descriptive.Filter;
import edu.uci.ics.sdcl.firefly.util.ElapsedTimeUtil;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;
import edu.uci.ics.sdcl.firefly.util.mturk.AnswerCounter;

/**
 *  Uses the predictor StrengthSignal to search for the best result among all different filtered datasets.
 * 
 *  Uses a goal function that basically looks for the fewer amount of YES's which still enable to select only bug covering questions.
 * 
 * @author adrianoc
 *
 */
public class OptimumFinder {
	

	ArrayList<HashMap<FilterCombination,AnswerData>> dataList;

	ArrayList<Predictor> predictorList;

	ArrayList<Outcome> filterOutcomeList = new ArrayList<Outcome>();
	
	ArrayList<String> hitFileNameList = new 	ArrayList<String>();

	public OptimumFinder(ArrayList<HashMap<FilterCombination,AnswerData>> dataList){
		this.dataList = dataList;
	}

	public void addPredictor(Predictor pred){
		if(predictorList==null)
			this.predictorList = new ArrayList<Predictor>();
		this.predictorList.add(pred);
	}

	public void run(){
		
		for(HashMap<FilterCombination,AnswerData> map : dataList){ //one for each file (8)
			for(FilterCombination filter : map.keySet()){ //one time
				AnswerData answerData = map.get(filter);
				
				for(Predictor predictor: predictorList){ //one time
					
					Outcome outcome = new Outcome(filter,
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
						
					filterOutcomeList.add(outcome);					
				}
			}
		}
	}


	public void printResults(){	

		String destination = "C://firefly//optimumFinder.txt";
		BufferedWriter log;
		try {
			log = new BufferedWriter(new FileWriter(destination));
			//Print file header

			log.write(getHeader()+"\n");
			for(Outcome outcome: this.filterOutcomeList){
				String line= outcome.filter.toString(FilterCombination.headerList)+":"+outcome.toString();
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
	

	
	
	private String getHeader(){
	
		return FilterCombination.getFilterHeaders() + Outcome.getHeader();
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
	


	public static void main(String[] args){

		//Obtain bug covering question list
		PropertyManager manager = PropertyManager.initializeSingleton();
		HashMap<String,String> bugCoveringMap = new HashMap<String,String>();
		String[] listOfBugPointingQuestions = manager.bugCoveringList.split(";");
		for(String questionID:listOfBugPointingQuestions){
			bugCoveringMap.put(questionID,questionID);
		}

		//Produce the list of filters
		ArrayList<FilterCombination> filterList = FilterGenerator.generateAnswerFilterCombinationList();

		String[] fileNameList = {"HIT01_8", "HIT02_24", "HIT03_6", "HIT04_7",
								"HIT05_35","HIT06_51","HIT07_33","HIT08_54"};

		ArrayList<HashMap<FilterCombination,AnswerData>> processingList = new 	ArrayList<HashMap<FilterCombination,AnswerData>> ();

		//Apply filter and extract data by fileName
		//System.out.println("FilterList size: "+ filterList.size());
		for(FilterCombination combination :  filterList){
			//FilterCombination combination =  filterList.get(0);
			FileSessionDTO sessionDTO = new FileSessionDTO();
			HashMap<String, Microtask> microtaskMap = (HashMap<String, Microtask>) sessionDTO.getMicrotasks();
			
			Filter filter = combination.getFilter();

			HashMap<String, Microtask> filteredMicrotaskMap = (HashMap<String, Microtask>) filter.apply(microtaskMap);
			
		
			Integer totalDifferentWorkersAmongHITs = countWorkers(filteredMicrotaskMap, null);
			
			System.out.println("Elapsed time: "+ElapsedTimeUtil.getElapseTime(filteredMicrotaskMap)+
					", number of answers: "+AnswerCounter.countAnswers(filteredMicrotaskMap) + 
					", number of workers: "+totalDifferentWorkersAmongHITs);
			
			
			for(String fileName: fileNameList){
				HashMap<String, ArrayList<String>> answerMap = extractAnswersForFileName(filteredMicrotaskMap,fileName);
				Integer workerCountPerHIT = countWorkers(filteredMicrotaskMap,fileName);
				AnswerData data = new AnswerData(fileName,answerMap,bugCoveringMap,workerCountPerHIT,totalDifferentWorkersAmongHITs);
				HashMap<FilterCombination, AnswerData> map = new HashMap<FilterCombination, AnswerData>();
				map.put(combination, data);
				processingList.add(map);
			}
		}

		OptimumFinder finder =  new OptimumFinder(processingList);
		finder.addPredictor(new PositiveVoting());
		finder.addPredictor(new MajorityVoting());
		finder.run();
		finder.printResults();
	}
	
}
