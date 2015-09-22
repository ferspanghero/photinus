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
		}
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
}

