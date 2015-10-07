package edu.uci.ics.sdcl.firefly.report.descriptive;

import edu.uci.ics.sdcl.firefly.Answer;

public class AnswerDuration extends AnswerReport {

	@Override
	protected String reportData(Answer answer) {
		String number = answer.getElapsedTime();
		String firstNumber = number.replaceFirst(".*?(\\d+).*", "$1");
		Integer seconds = Integer.valueOf(firstNumber)/1000;
		return seconds.toString();
	}

	@Override
	public String getType() {
		return "Duration in seconds";
	}

	@Override
	public String getDataNature() {
		return "Duration";
	}

}
