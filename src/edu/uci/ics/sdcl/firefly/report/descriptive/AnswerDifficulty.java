package edu.uci.ics.sdcl.firefly.report.descriptive;


import edu.uci.ics.sdcl.firefly.Answer;

public class AnswerDifficulty extends AnswerReport {
	
	public AnswerDifficulty() {
		super();
	}
	
	@Override
	protected String reportData(Answer answer) {
		return new Integer(answer.getDifficulty()).toString();
	}

	@Override
	public String getType() {
		return "Difficulty of questions";
	}

	@Override
	public String getDataNature() {
		return "Difficulty counting";
	}
	
	

}
