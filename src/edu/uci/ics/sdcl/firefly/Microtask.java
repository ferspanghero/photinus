package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;

/** Represents a microtask in the system.
 * A microtasks comprises a Question and a CodeSnippet, and a list of Answers.
 * @author Christian Adriano
 *
 */
public class Microtask {

	private Question question;
	private CodeSnippet code;
	private ArrayList<Answer> answerList;
	
	
	public Microtask(Question question, CodeSnippet code) {
		super();
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
