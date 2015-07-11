package edu.uci.ics.sdcl.firefly.report.descriptive;

import edu.uci.ics.sdcl.firefly.Answer;

public class AnswerExplanationSize extends AnswerReport {

	@Override
	protected String reportData(Answer answer) {
		return new Integer(answer.getExplanation().length()).toString();
	}

	@Override
	public String getType() {
		return "Size of explanation (characters)";
	}

}
