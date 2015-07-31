package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.HashMap;

/** 
 * Class responsible to keep the multiple filter combinations and the respective outcome of
 * computing predictors with the data produced by a concrete filter combination.
 * 
 * @author adrianoc
 *
 */
public class FilterCombination {

	public HashMap<String,Range> combinationMap;
	
	/** Holds the outcome of predictors for the data associated with this filter */
	public HashMap<String, Integer> outcomeMap;
	
	/** Internal public class to hold the values of a filter */
	public class Range{
		Integer max;
		Integer min;
		
		public String toString(){
			return "["+min.toString()+","+max.toString()+"]";
		}
		
		public Range(int minValue, int maxValue){
			this.min = new Integer(minValue);
			this.max =  new Integer(maxValue);
		}
	}
	
	public void addOutcome(String predictorType, Integer outcome){
		if(outcomeMap==null)
			outcomeMap = new HashMap<String, Integer>();
		outcomeMap.put(predictorType,outcome);
	}
	
	public String toString(){
		String result="";
		for(String name : combinationMap.keySet()){
			result = result+"_"+name+combinationMap.get(name).toString();
		}
		return result;
	}
	
	
	public void addFilterParam(String filterName, int max, int min){
		if(combinationMap==null)
			combinationMap = new HashMap<String, Range>();
		combinationMap.put(filterName, new Range(min,max));
	}
	
	public Range getFilterParam(String filterName){
		return this.combinationMap.get(filterName);
	}
	
	public String printOutcome(){
		String result="";
		for(String name : this.outcomeMap.keySet()){
			Integer outcome = this.outcomeMap.get(name);
			result = result+":"+name+":"+outcome.toString();
		}
		return result;
	}
	
}
