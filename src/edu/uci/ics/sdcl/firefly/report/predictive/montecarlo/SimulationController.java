package edu.uci.ics.sdcl.firefly.report.predictive.montecarlo;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.report.descriptive.Filter;

/**
 * 
 * The simulation has the following steps:
 * 1- Generate sub-crowd filters
 * 2- Obtain the microtasks for each of these sub-crowds
 * 3- Obtain the maximum common number of answers for each sub-crowd
 * 4- Cut the answers from each sub-crowd to this maximum
 * 5- For each sub-crowd generate random samples from 1 to the maximum common answers minus 1
 * 6- For each sample compute Precision, Recall, TP, TN, FP, FN, elapsed time, #workers, #answers
 * 7- For all the samples compute the average of all these values.
 * 8- Write results to a file
 * 
 * @author adrianoc
 *
 */
public class SimulationController {

	/**
	 * 1- Generate sub-crowd filters
	 * @return
	 */
	private ArrayList<SubCrowd> generateSubCrowdFilters(){
		
		return new ArrayList<SubCrowd>();
	}
	
	/**
	 * 2- Obtain the microtasks for each of these sub-crowds
	 * 
	 * @param filterList
	 * @return
	 */
	private ArrayList<SubCrowd> generateSubCrowdMicrotasks(ArrayList<SubCrowd> subCrowdList){
		
		return subCrowdList;
	}
	
	/**
	 * 3- Obtain the maximum common number of answers for each sub-crowd
	 * 
	 */
	private ArrayList<SubCrowd> generateMaximumCommonAnswers(ArrayList<SubCrowd> subCrowdList){
		
		return subCrowdList;
	}
	
	/**
	 * 4- Cut the answers from each sub-crowd to the maximum of answers per question
	 */
	private ArrayList<SubCrowd> cutAnswersListsToMaximum(ArrayList<SubCrowd> subCrowdList){
		
		return subCrowdList;
	}
	
}
