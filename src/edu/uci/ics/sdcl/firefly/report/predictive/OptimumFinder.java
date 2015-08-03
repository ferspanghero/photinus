package edu.uci.ics.sdcl.firefly.report.predictive;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap; 


import edu.uci.ics.sdcl.firefly.Microtask; 
import edu.uci.ics.sdcl.firefly.report.descriptive.FileSessionDTO;
import edu.uci.ics.sdcl.firefly.report.descriptive.Filter;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

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

	ArrayList<FilterCombination> filterOutcomeList = new ArrayList<FilterCombination>();

	public OptimumFinder(ArrayList<HashMap<FilterCombination,AnswerData>> dataList){
		this.dataList = dataList;
	}

	public void addPredictor(Predictor pred){
		if(predictorList==null)
			this.predictorList = new ArrayList<Predictor>();
		this.predictorList.add(pred);
	}

	public void run(){
		for(HashMap<FilterCombination,AnswerData> map : dataList){
			for(FilterCombination filter : map.keySet()){
				AnswerData answerData = map.get(filter);
				for(Predictor predictor: predictorList){
					String outcomeKey = predictor.getName()+":"+answerData.getHitFileName();
					filter.addOutcome(outcomeKey, predictor.compute(answerData));
				}
				filterOutcomeList.add(filter);
			}
		}
	}


	public void printResults(){	

		String destination = "C://firefly//optimumFinder.txt";
		BufferedWriter log;
		try {
			log = new BufferedWriter(new FileWriter(destination));
			for(FilterCombination filter: this.filterOutcomeList){
				String line= filter.toString()+":"+filter.printOutcome();
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



	private static HashMap<String, ArrayList<String>>  extractAnswersForFileName(
			HashMap<String, Microtask> microtaskMap,String fileName){

		HashMap<String, ArrayList<String>> resultMap = new HashMap<String, ArrayList<String>>();

		for(Microtask task:microtaskMap.values() ){
			//System.out.println("fileName: "+fileName+":"+task.getFileName());
			if(task.getFileName().compareTo(fileName)==0)
				resultMap.put(task.getID().toString(),task.getAnswerOptions());
		}

		return resultMap;

	}

	public static void main(String[] args){



		//Obtain bugcovering question list
		PropertyManager manager = PropertyManager.initializeSingleton();
		HashMap<String,String> bugCoveringMap = new HashMap<String,String>();
		String[] listOfBugPointingQuestions = manager.bugCoveringList.split(";");
		for(String questionID:listOfBugPointingQuestions){
			bugCoveringMap.put(questionID,questionID);
		}

		//Produce the list of filters
		ArrayList<FilterCombination> filterList = FilterGenerator.generateAnswerFilterCombinations();


		//Filter answers
		FileSessionDTO sessionDTO = new FileSessionDTO();
		HashMap<String, Microtask> microtaskMap = (HashMap<String, Microtask>) sessionDTO.getMicrotasks();

		String[] fileNameList = {"HIT01_8", "HIT02_24", "HIT03_6", "HIT04_7",
				"HIT05_35","HIT06_51","HIT07_33","HIT08_54"};

		ArrayList<HashMap<FilterCombination,AnswerData>> processingList = new 	ArrayList<HashMap<FilterCombination,AnswerData>> ();

		//Apply filter and extract data by fileName
		for(FilterCombination combination :  filterList){
			//FilterCombination combination =  filterList.get(0);

			Filter filter = combination.getFilter();
			HashMap<String, Microtask> filteredMicrotaskMap = (HashMap<String, Microtask>) filter.apply(microtaskMap);
		
			for(String fileName: fileNameList){
				//String fileName = "HIT01_8";
				HashMap<String, ArrayList<String>> answerMap = extractAnswersForFileName(filteredMicrotaskMap,fileName);
				AnswerData data = new AnswerData(fileName,answerMap,bugCoveringMap);
				HashMap<FilterCombination, AnswerData> map = new HashMap<FilterCombination, AnswerData>();
				map.put(combination, data);
				processingList.add(map);
			}
		}





		OptimumFinder finder =  new OptimumFinder(processingList);
		finder.addPredictor(new StrengthSignal());
		finder.run();
		finder.printResults();




	}

}
