package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.ArrayList;
import java.util.HashMap;

public class DataPoint {

	public Double averagePrecision;
	public Double averageRecall;
	public Double elapsedTime;
	public Integer totalWorkers;
	public Integer faultsLocated=0;
	public Integer falsePositives=0;

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

