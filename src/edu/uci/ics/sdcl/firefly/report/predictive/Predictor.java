package edu.uci.ics.sdcl.firefly.report.predictive;

public abstract class Predictor {

	public abstract String getName();
	
	public abstract Integer compute(AnswerData data);
	
	
	
}
