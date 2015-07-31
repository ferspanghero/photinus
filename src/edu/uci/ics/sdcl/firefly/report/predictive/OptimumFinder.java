package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	public void addPredictor(Predictor predictor){
		if(predictorList!=null)
			predictorList = new ArrayList<Predictor>();
		predictorList.add(predictor);
	}

	public void run(){
		for(HashMap<FilterCombination,AnswerData> map : dataList){
			for(FilterCombination filter : map.keySet()){
				AnswerData answerData = map.get(filter);
				for(Predictor predictor: predictorList){
					filter.addOutcome(predictor.getName(), predictor.compute(answerData));
				}
				filterOutcomeList.add(filter);
			}
		}
		printResults();
	}
	
	public void printResults(){
		for(FilterCombination filter: this.filterOutcomeList){
			System.out.println(filter.toString()+":"+filter.printOutcome());
		}
	}
	
		
}
