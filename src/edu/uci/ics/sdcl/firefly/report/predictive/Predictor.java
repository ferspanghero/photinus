package edu.uci.ics.sdcl.firefly.report.predictive;

public abstract class Predictor {

	public abstract String getName();
	
	public abstract Integer getNumberBugCoveringQuestions();
	
	public abstract Boolean computeSignal(AnswerData data);
	
	public abstract Double computeSignalStrength(AnswerData data);
	
	public abstract Integer computeNumberOfWorkers(AnswerData data);

	public abstract Integer getThreshold();

	public abstract Integer getTruePositives();
	
	public abstract Integer getTrueNegatives();
	
	public abstract Integer getFalsePositives();
	
	public abstract Integer getFalseNegatives();
	
}
