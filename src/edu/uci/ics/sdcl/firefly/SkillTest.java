package edu.uci.ics.sdcl.firefly;

public class SkillTest {
	private String name;
	private StringBuffer sourceCode;
	private String[] questions;
	private String[][] options;
	private String[] answers;
	
	public SkillTest(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
			result[i] = this.answers[i].trim().toLowerCase().equals(this.options[i][letter].trim().toLowerCase());
		}
		return result;
	}
	
}
