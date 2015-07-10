package edu.uci.ics.sdcl.firefly.report.descriptive;

import edu.uci.ics.sdcl.firefly.Answer;

public class AnswerOption extends AnswerReport {
	
	 public AnswerOption() {
		super();
	}
	
	@Override
	protected String reportData(Answer answer) {
		return answer.getOption();
	}

}
