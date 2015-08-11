package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.report.descriptive.Filter;

/** 
 * Class responsible to keep the multiple filter combinations and the respective outcome of
 * computing predictors with the data produced by a concrete filter combination.
 * 
 * @author adrianoc
 *
 */
public class FilterCombination {

	public static final String ANSWER_DURATION = "ANSWER_DURATION";
	public static final String SESSION_DURATION = "SESSION_DURATION";
	public static final String CONFIDENCE_LEVEL = "CONFIDENCE_LEVEL";
	public static final String DIFFICULTY_LEVEL = "DIFFICULTY_LEVEL";
	public static final String WORKER_SCORE = "WORKER_SCORE";
	public static final String WORKER_SCORE_EXCLUSION = "WORKER_SCORE_EXCLUSION";
	public static final String EXPLANATION_SIZE = "EXPLANATION_SIZE";
	public static final String WORKER_IDK = "WORKER_IDK";


	public HashMap<String,Range> combinationMap;

	/** Holds the outcome of predictors for the data associated with this filter */
	public HashMap<String, Integer> outcomeMap;

	/** Internal public class to hold the values of a filter */
	public class Range{
		Integer max=-1;
		Integer min=-1;
		int[] list;

		public String toString(){
			if(list!=null && list.length>0)
				return "[excluded" +listToString(list)+  "]";
			else
				return "["+min.toString()+","+max.toString()+"]";
		}

		public String listToString(int[] list){
			String result ="";
			for(int value: list){
				String valueStr = new Integer(value).toString();
				result = result + ","+valueStr;
			}
			result = result.substring(0, result.length()-1);
			return result;
		}

		public Range(int minValue, int maxValue){
			this.min = new Integer(minValue);
			this.max =  new Integer(maxValue);
		}

		public Range(int[] workerExclusionList) {
			this.list = workerExclusionList;
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


	public Filter getFilter(){

		Filter filter = new Filter();

		for(String filterName: this.combinationMap.keySet()){

			int min=-1;
			int max=-1;

			if(this.combinationMap.get(filterName).min!=null){
				min = this.combinationMap.get(filterName).min.intValue();
				max = this.combinationMap.get(filterName).max.intValue();
			}

			if(filterName.compareTo(FilterCombination.ANSWER_DURATION)==0){
				filter.setAnswerDurationCriteria(new Double(min), new Double(max));
			}
			else
				if(filterName.compareTo(FilterCombination.SESSION_DURATION)==0){
					filter.setSessionDurationCriteria(new Double(min), new Double(max));
				}
				else
					if(filterName.compareTo(FilterCombination.CONFIDENCE_LEVEL)==0){
						filter.setConfidenceCriteria(min, max);
					}
					else
						if(filterName.compareTo(FilterCombination.DIFFICULTY_LEVEL)==0){
							filter.setDifficultyCriteria(min, max);
						}
						else 
							if(filterName.compareTo(FilterCombination.EXPLANATION_SIZE)==0){
								filter.setExplanationSizeCriteria(min, max);
							}
							else 
								if(filterName.compareTo(FilterCombination.WORKER_SCORE)==0){
									filter.setWorkerScoreCriteria(min, max);
								}
								else
									if(filterName.compareTo(FilterCombination.WORKER_SCORE_EXCLUSION)==0){
										filter.setWorkerScoreExclusionList(this.combinationMap.get(filterName).list);
									}
									else
										if(filterName.compareTo(FilterCombination.WORKER_IDK)==0){
											System.out.println("Min:"+min+"Max:"+max);
											filter.setIDKPercentageCriteria(new Double(min), new Double(max));
										}
		}
		return filter;	
	}

	public void addFilterParam(String workerScoreExclusion,
			int[] workerExclusionList) {
		if(combinationMap==null)
			combinationMap = new HashMap<String, Range>();
		combinationMap.put(workerScoreExclusion, new Range(workerExclusionList));
	}


}
