package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *  Search for the best result among all different data filters applied
 * 
 *  Uses a goal function that basically looks for the fewer amount of YES's which still enable to select only bug covering questions.
 * 
 * @author adrianoc
 *
 */
public class OptimumFinder {

	/**
	 * @param questionOptionsMap questionID and list of answer options (YES, NO, IDK)
	 * @return a map <questionID, number of YES's>
	 */
	private HashMap<String, Integer> computeNumberOfYES(HashMap<String, ArrayList<String>> questionOptionsMap){
		
		HashMap<String, Integer> questionYESCounterMap= new HashMap<String, Integer>(); 
		
		for(String questionID: questionOptionsMap.keySet()){
			ArrayList<String> optionList = questionOptionsMap.get(questionID);
			int counter = 0;
			for(String option : optionList){
				if(option.compareTo("YES")==0)
					counter++;
			}
			questionYESCounterMap.put(questionID, new Integer(counter));
		}
		
		return questionYESCounterMap;
	}
	
	
	
}
