package edu.uci.ics.sdcl.firefly.report.descriptive;

import edu.uci.ics.sdcl.firefly.Answer;

public class AnswerConfidence extends AnswerReport{

	@Override
	protected String reportData(Answer answer) {
		return new Integer(answer.getConfidenceOption()).toString();
	}

	@Override
	public String getType() {
		return "Levels of confidence of answers";
	}

	@Override
	public String getDataNature() {
		return "Confidence Counting";
	}

}
