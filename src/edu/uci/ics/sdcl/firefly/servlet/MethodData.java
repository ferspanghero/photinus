package edu.uci.ics.sdcl.firefly.servlet;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.Answer;

public class MethodData {

	HashMap<String,String> questionAnswerMap = new HashMap<String,String>(); 

	HashMap<String, Integer> answerPerQuestionMap = new HashMap<String,Integer>();

	public MethodData(String question){
		this.questionAnswerMap.put(question, null);
		this.answerPerQuestionMap.put(question, new Integer(0));
	}

	public void addAnswerList(String question, ArrayList<Answer> answerList){
		if(answerList!=null){
			for(int i=0;i<answerList.size();i++){
				Answer answer = answerList.get(i);
				this.addAnswer(question, answer.getOption());
			}
		}
		else
			System.out.println("AnswerList is null! - question: "+ question);
	}

	public void addAnswer(String question, String newAnswer){
		if(newAnswer!=null && newAnswer.length()>0){
			String answers = this.questionAnswerMap.get(question);
			if(answers==null)
				answers = newAnswer;
			else{
				answers = answers +  ";"+newAnswer;
			}
			this.questionAnswerMap.put(question, answers);
			incrementAnswersPerQuestion(question);		
		}
		else{} //nothing.
	}


	private void incrementAnswersPerQuestion(String question){
		Integer count = this.answerPerQuestionMap.get(question); 		

		if(count==null){
			count = new Integer(0);
		}
		count++;
		this.answerPerQuestionMap.put(question, count);
	}

	public HashMap<String,String> getQuestionAnswerMap(){
		return this.questionAnswerMap;
	}

	public Integer numberOfAnswersPerQuestion(String question){
		return this.answerPerQuestionMap.get(question);
	}



}
