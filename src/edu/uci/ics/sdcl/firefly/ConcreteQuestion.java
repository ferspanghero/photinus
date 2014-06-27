package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;

public class ConcreteQuestion
{	
	private String question;
	private CodeSnippet method;
	private String questionType;
	private ArrayList<String> answer;
	private Integer lineNumber1;
	private Integer lineNumber2;
	
	public ConcreteQuestion(String questionTypeArg, CodeSnippet methodArg, String questionArg, 
			Integer startingLineNumber)
	{
		this.setMethod(methodArg);
		this.setQuestion(questionArg);
		this.setQuestionType(questionTypeArg);
		this.lineNumber1 = startingLineNumber;
		this.lineNumber2 = null;
		QuestionFactory.concreteQuestionID++;
	}
	
	public ConcreteQuestion(String questionTypeArg, CodeSnippet methodArg, String questionArg, 
			Integer startingLineNumber, Integer endingLineNumber)
	{
		this.setMethod(methodArg);
		this.setQuestion(questionArg);
		this.setQuestionType(questionTypeArg);
		this.lineNumber1 = startingLineNumber;
		this.lineNumber2 = endingLineNumber;
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

	public Integer getLineNumber1() {
		return lineNumber1;
	}

	public void setLineNumber1(Integer lineNumber1) {
		this.lineNumber1 = lineNumber1;
	}

	public Integer getLineNumber2() {
		return lineNumber2;
	}

	public void setLineNumber2(Integer lineNumber2) {
		this.lineNumber2 = lineNumber2;
	}
	
}
