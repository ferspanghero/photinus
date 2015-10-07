package edu.uci.ics.sdcl.firefly.report.descriptive;

import edu.uci.ics.sdcl.firefly.Answer;

public class AnswerOption extends AnswerReport {
	
	 public AnswerOption() {
		super();
	}
	
	@Override
	protected String reportData(Answer answer) {
		return shortenOption(answer.getOption());
	}
	
	private String shortenOption(String option){
		if(option.compareTo("YES, THERE IS AN ISSUE")==0)
			return "YES";
		else
			if(option.compareTo("NO, THERE IS NOT AN ISSUE")==0)
				return "NO";
			else
				if(option.compareTo("I DON'T KNOW")==0)
					return "IDK";
				else
					return "ERROR";
	}

	@Override
	public String getType() {
		return "Answer option";
	}

	@Override
	public String getDataNature() {
		return "Answer Option Counting";
	}

}
