package edu.uci.ics.sdcl.firefly.report.predictive;

/**
 *  Outcome of a predictor
 * 
 * @author adrianoc
 * @see Predictor, MajorityVoting, PositiveVoting
 */
public class Outcome {

	FilterCombination filter;
	
	String fileName;
	
	String predictorType;
	
	Boolean faultLocated;
	
	Double signalStrength;
	
	Integer maxWorkerPerQuestion;
	
	Integer threshold; //minimal number of YES's (has different definitions for MajorityVoting and Positive Voting
	
	Integer truePositives;
	
	Integer trueNegatives;

	Integer falsePositives;	
	
	Integer falseNegatives;
	
	Integer totalDifferentWorkers;
		
	public Outcome(FilterCombination filter, String fileName, String predictorType, Boolean faultLocated,
			Double signalStrength, Integer maxWorkerPerQuestion, Integer threshold,
			Integer truePositives, Integer trueNegatives,
			Integer falsePositives, Integer falseNegatives,Integer totalDifferentWorkers) {
		super();
		this.filter = filter;
		this.fileName = fileName;
		this.predictorType = predictorType;
		this.faultLocated = faultLocated;
		this.signalStrength = signalStrength;
		this.maxWorkerPerQuestion = maxWorkerPerQuestion;
		this.threshold = threshold; 
		this.truePositives = truePositives;
		this.trueNegatives = trueNegatives;
		this.falsePositives = falsePositives;
		this.falseNegatives = falseNegatives;
		this.totalDifferentWorkers = totalDifferentWorkers;
	}


	public static String getHeader(){
		
		return "HIT : Predictor : Fault located? : Signal strength : # Workers per question : #YES needed :"+
				"True positives : True negatives : False positives : False negatives : Total different workers";	
	}

	
	public String toString(){
		
			return  fileName +":"+ predictorType +":"+ faultLocated +":"+ signalStrength +":"+ maxWorkerPerQuestion +":"+ threshold +":"+
					truePositives +":"+ trueNegatives +":"+ falsePositives +":"+ falseNegatives +":"+ totalDifferentWorkers;	
	}
	
}
