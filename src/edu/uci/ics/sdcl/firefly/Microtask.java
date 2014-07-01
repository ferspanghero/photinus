package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;

public class Microtask
{	
	private String question;
	private CodeSnippet method;
	private String questionType;
	private ArrayList<String> answer;
	private Integer statementStartsAt;
	private Integer bodyStartsAt;
	private Integer bodyEndsAt;
	
	public Microtask(String questionTypeArg, CodeSnippet methodArg, String questionArg, 
			Integer startingLineNumber)
	{
		this.setMethod(methodArg);
		this.setQuestion(questionArg);
		this.setQuestionType(questionTypeArg);
		this.statementStartsAt = startingLineNumber;
		this.bodyStartsAt = startingLineNumber;
		this.bodyEndsAt = startingLineNumber;
		QuestionFactory.concreteQuestionID++;
	}
	
	public Microtask(String questionTypeArg, CodeSnippet methodArg, String questionArg, 
			Integer startingLineNumber, Integer bodyStartingLineNumber, Integer bodyEndingLineNumber)
	{
		this.setMethod(methodArg);
		this.setQuestion(questionArg);
		this.setQuestionType(questionTypeArg);
		this.statementStartsAt = startingLineNumber;
		this.bodyStartsAt = bodyStartingLineNumber;
		this.bodyEndsAt = bodyEndingLineNumber;
		QuestionFactory.concreteQuestionID++;
	}

	public String getQuestion()
	{
		return question;
	}

	public void setQuestion(String question)
	{
		this.question = question;
	}
	
	public String getQuestionType()
	{
		return questionType;
	}

	public void setQuestionType(String questionType)
	{
		this.questionType = questionType;
	}

	public CodeSnippet getMethod()
	{
		return method;
	}

	public void setMethod(CodeSnippet method)
	{
		this.method = method;
	}

	public ArrayList<String> getAnswer()
	{
		return answer;
	}

	public void setAnswer(ArrayList<String> answer)
	{
		this.answer = answer;
	}

	public Integer getStatementStartsAt() {
		return statementStartsAt;
	}

	public void setStatementStartsAt(Integer lineNumber1) {
		this.statementStartsAt = lineNumber1;
	}

	public Integer getBodyStartsAt() {
		return bodyStartsAt;
	}

	public void setBodyStartsAt(Integer lineNumber2) {
		this.bodyStartsAt = lineNumber2;
	}

	public Integer getBodyEndsAt() {
		return bodyEndsAt;
	}

	public void setBodyEndsAt(Integer bodyEndsAt) {
		this.bodyEndsAt = bodyEndsAt;
	}
	
}