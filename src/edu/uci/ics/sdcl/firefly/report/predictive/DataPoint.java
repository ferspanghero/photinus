package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.ArrayList;
import java.util.HashMap;

public class DataPoint {

	public Double averagePrecision=0.0;
	public Double averageRecall=0.0;
	public Double elapsedTime=0.0;
	public Double totalWorkers=0.0;
	public Double totalAnswers=0.0;
	public Double faultsLocated=0.0;
	public Double falsePositives=0.0;
	public Double falseNegatives=0.0;
	public Double truePositives=0.0;
	public Double trueNegatives=0.0;
	public Double numberOfOutcomes=0.0;

	private static String[] header = { "average Precision", "average Recall", "elapsed Time", "total Workers", 
			"total Answers", "faults Located", "true Positives", "true Negatives", 
			 "false Positives", "false Negatives", "number of Outcomes Simulated"} ;
	
	public DataPoint(){}

	public HashMap<String, Outcome> fileNameOutcomeMap = new HashMap<String, Outcome>();

	public void computeAverages(){

		ArrayList<Double> precisionValues = new ArrayList<Double>();
		ArrayList<Double> recallValues = new ArrayList<Double>();

		for(String key: fileNameOutcomeMap.keySet()){
			Outcome outcome = fileNameOutcomeMap.get(key);
			precisionValues.add(outcome.precision);
			recallValues.add(outcome.recall);
			if(outcome.precision!=0)
				faultsLocated++;
			falsePositives = falsePositives + outcome.falsePositives;
			falseNegatives = falseNegatives + outcome.falseNegatives;
			truePositives = truePositives + outcome.truePositives;
			trueNegatives = trueNegatives + outcome.trueNegatives;
		}
		numberOfOutcomes = new Double(fileNameOutcomeMap.size());
		averagePrecision = average(precisionValues);
		averageRecall = average(recallValues);
	}

	private Double average(ArrayList<Double> values){

		Double total = 0.0;
		for(int i=0; i<values.size();i++){
			total = total + values.get(i);
		}
		return total/values.size();
	}
	
	/**
	 * 
	 * @param suffix necessary to identify the type of predictor that produced this datapoint
	 * @return
	 */
	public static String getHeader(String suffix){
		
		StringBuffer titles=new StringBuffer();
		for(String label: header){
			titles.append(label);
			titles.append(suffix);
			titles.append(",");
		}
		return titles.toString();
	}
	
	public String toString(){
		return this.averagePrecision+","+this.averageRecall+","+this.elapsedTime+","+this.totalWorkers+","+
				this.totalAnswers+","+this.faultsLocated+","+this.truePositives+","+this.trueNegatives+","+
				this.falsePositives+","+this.falseNegatives+","+this.numberOfOutcomes;
	}


}

