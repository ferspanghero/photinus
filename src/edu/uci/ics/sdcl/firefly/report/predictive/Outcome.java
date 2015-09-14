package edu.uci.ics.sdcl.firefly.report.predictive;

/**
 *  Outcome of a predictor
 * 
 * @author adrianoc
 * @see Predictor, MajorityVoting, PositiveVoting
 */
public class Outcome {

	public FilterCombination filter;
	
	public String fileName;
	
	public String predictorType;
	
	public Boolean faultLocated;
	
	public Double signalStrength;
	
	/** Maximum different workers per question for this HIT */
	public Integer maxWorkerPerQuestion;
	
	/** All the YES, NO, IDK for all different questions in the same HIT */
	public Integer totalAnswersObtained;
	
	/** Minimal number of YES's (has different definitions for MajorityVoting and Positive Voting */
	public Integer threshold; 
	
	public Integer truePositives;
	
	public Integer trueNegatives;

	public Integer falsePositives;	
	
	public Integer falseNegatives;
	
	public Double precision;
	
	public Double recall;
	
	/** Total workers that contributed to one HIT after applying the combined filter */
	public Integer differentWorkersPerHIT;
	
	/** Total workers that remained after applying the combined filter */
	public Integer differentWorkersAmongHITs;
		
	public Outcome(FilterCombination filter, String fileName, String predictorType, Boolean faultLocated,
			Double signalStrength, Integer maxWorkerPerQuestion, Integer totalAnswers, Integer threshold,
			Integer truePositives, Integer trueNegatives,
			Integer falsePositives, Integer falseNegatives,Integer differentWorkersPerHIT, Integer differentWorkersAmongHITs) {
		super();
		this.filter = filter;
		this.fileName = new String(fileName.replace("HIT0", "J").replace("_","."));
		this.predictorType = predictorType;
		this.faultLocated = faultLocated;
		this.signalStrength = signalStrength;
		this.maxWorkerPerQuestion = maxWorkerPerQuestion;
		this.totalAnswersObtained = totalAnswers;
		this.threshold = threshold; 
		this.truePositives = truePositives;
		this.trueNegatives = trueNegatives;
		this.falsePositives = falsePositives;
		this.falseNegatives = falseNegatives;
		this.differentWorkersPerHIT = differentWorkersPerHIT;
		this.differentWorkersAmongHITs = differentWorkersAmongHITs;
		this.precision = this.computePrecision(this.truePositives, this.falsePositives);
		this.recall = this.computeRecall(this.truePositives, this.falseNegatives);
	}

	private Double computePrecision(int tp, int fp){
		Double tpD = new Double(tp);
		Double fpD =  new Double(fp);
		return tpD/(tpD+fpD);
	}
	
	private Double computeRecall(int tp, int fn){
		Double tpD = new Double(tp);
		Double fnD =  new Double(fn);
		return tpD/(tpD+fnD);
	}

	public static String getHeader(){
		
		return "HIT:Predictor:Fault located?:Signal strength:#Maximum workers per question:#Total answers obtained: #YES needed :"+
				"True positives:True negatives:False positives:False negatives:Different workers in HIT:"+
				"Different Workers among all HITs";	
	}
	
	public String toString(){
		
			return  fileName +":"+ predictorType +":"+ faultLocated +":"+ signalStrength +":"+ maxWorkerPerQuestion +":"+ totalAnswersObtained+
					":"+threshold +":"+	truePositives +":"+ trueNegatives +":"+ falsePositives +":"+ falseNegatives +":"+ differentWorkersPerHIT +
					":"+differentWorkersAmongHITs;	
	}
	
}
