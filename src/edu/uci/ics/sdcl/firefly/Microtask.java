package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;
import java.util.ArrayList;

public class Microtask implements Serializable
{	
	private String question;
	private CodeSnippet method;
	private String questionType;
	private ArrayList<Answer> answerList;

	private Integer startingLine;
	private Integer startingColumn;
	private Integer endingLine;
	private Integer endingColumn;
	
	public Microtask(String questionTypeArg, CodeSnippet methodArg, String questionArg, 
			Integer startingLineNumber, Integer startingColumnNumber, 
			Integer endingLineNumber, Integer endingColumnNumber)
	{
		this.setMethod(methodArg);
		this.setQuestion(questionArg);
		this.setQuestionType(questionTypeArg);
		this.startingLine = startingLineNumber;
		this.startingColumn = startingColumnNumber;
		this.endingLine = endingLineNumber;
		this.endingColumn = endingColumnNumber;
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

	public ArrayList<Answer> getAnswerList()
	{
		return answerList;
	}

	public void setAnswer(ArrayList<Answer> answerList)
	{
		this.answerList = answerList;
	}
	
	/* getters for the position */
	public Integer getStartingLine() {
		return startingLine;
	}

	public Integer getStartingColumn() {
		return startingColumn;
	}

	public Integer getEndingLine() {
		return endingLine;
	}

	public Integer getEndingColumn() {
		return endingColumn;
	}

	
}