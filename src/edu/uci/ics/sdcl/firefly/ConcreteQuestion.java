package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;

public class ConcreteQuestion
{	
	private String question;
	private CodeSnippet method;
	private String questionType;
	private ArrayList<String> answer;
	
	public ConcreteQuestion(String questionTypeArg, CodeSnippet methodArg, String questionArg)
	{
		this.setMethod(methodArg);
		this.setQuestion(questionArg);
		this.setQuestionType(questionTypeArg);
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
	
}
