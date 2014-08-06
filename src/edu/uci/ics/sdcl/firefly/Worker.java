package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Worker implements Serializable{
	private static final long serialVersionUID = 1L;
	private String userId;
	private String hitId;
	private Date consentDate;
	private HashMap<String,String> surveyAnswers;
	
	public Worker(String userId, String hitId, Date consentDate) {
		this.userId = userId;
		this.hitId = hitId;
		this.consentDate = consentDate;
		this.surveyAnswers =  new HashMap<String,String>();
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHitId() {
		return hitId;
	}

	public void setHitId(String hit) {
		this.hitId = hit;
	}

	public Date getConsentDate() {
		return consentDate;
	}

	public void setConsentDate(Date consentDate) {
		this.consentDate = consentDate;
	}	
	
	public void addAnswer(String question, String answer){
		this.surveyAnswers.put(question, answer);
	}
	
	public String getAnswer(String question){
		return this.surveyAnswers.get(question);
	}
}
