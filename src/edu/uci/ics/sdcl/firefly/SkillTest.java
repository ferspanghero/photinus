package edu.uci.ics.sdcl.firefly;

public class SkillTest {

	private StringBuffer sourceCode;
	private String[] questions;
	private String[][] options;
	private String[] answers;
	
	public StringBuffer getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(StringBuffer sourceCode) {
		this.sourceCode = sourceCode;
	}
	public String[] getQuestions() {
		return questions;
	}
	public void setQuestions(String[] questions) {
		this.questions = questions;
	}
	public String[][] getOptions() {
		return options;
	}
	public void setOptions(String[][] options) {
		this.options = options;
	}
	public String[] getAnswers() {
		return answers;
	}
	public void setAnswers(String[] answers) {
		this.answers = answers;
	}
	
	public boolean[] applyRubrics(String[] answers)
	{
		boolean[] result = new boolean[answers.length];
		for (int i=0; i<answers.length; i++) {
			int letter = ((answers[i].charAt(0)) - 97);
			result[i] = this.answers[i].equals(this.options[i][letter]);
		}
		return result;
	}
	
}
