package edu.uci.ics.sdcl.firefly.report.descriptive;

import edu.uci.ics.sdcl.firefly.Answer;

public class AnswerDuration extends AnswerReport {

	@Override
	protected String reportData(Answer answer) {
		return answer.getElapsedTime();
	}

}
