package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;
import java.util.ArrayList;

/** Represents a microtask in the system.
 * A microtasks comprises a Question and a CodeSnippet, and a list of Answers.
 * @author Christian Adriano
 *
 */
public class Microtask implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Question question;
	private CodeSnippet code;
	private ArrayList<Answer> answerList;
	
	
	public Microtask(Question question, CodeSnippet code) { 
		this.question = question;
		this.code = code;
	}
	
	public void addAnswer(Answer answer){
		this.answerList.add(answer);
	}
	
	public Question getQuestion() {
		return question;
	}
	public CodeSnippet getCode() {
		return code;
	}
	public ArrayList<Answer> getAnswerList() {
		return answerList;
	}	
	
}
