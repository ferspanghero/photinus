package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Worker implements Serializable{
	private static final long serialVersionUID = 1L;
	private String userId;
	private Date consentDate;
	private HashMap<String,String> surveyAnswers;
	private HashMap<String, String> rubricMap;
	private HashMap<String, Boolean> gradeMap;
	private Integer grade;
	private String skillTestDuration;
	private String sessionId; //Stores the single working session session allowed per worker
	
	
	public Worker(String userId, Date consentDate) {
		this.userId = userId;
		this.consentDate = consentDate;
		this.surveyAnswers =  new HashMap<String,String>();
		this.grade = null;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public void setSkillAnswers(HashMap<String, String> rubricMap,
			HashMap<String, Boolean> gradeMap, int grade, String duration) {
		this.setRubricMap(rubricMap);
		this.setGradeMap(gradeMap);
		this.setGrade(grade);
		this.setSkillTestDuration(duration);
		
	}

	private void setSkillTestDuration(String duration) {
		this.skillTestDuration = duration;
		
	}
	
	public String getSkillTestDuration() {
		return this.skillTestDuration;
		
	}

	public HashMap<String, String> getRubricMap() {
		return rubricMap;
	}

	public void setRubricMap(HashMap<String, String> rubricMap) {
		this.rubricMap = rubricMap;
	}

	public HashMap<String, Boolean> getGradeMap() {
		return gradeMap;
	}

	public void setGradeMap(HashMap<String, Boolean> gradeMap) {
		this.gradeMap = gradeMap;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}
	
	public HashMap<String, String> getSurveyAnswers() {
		return surveyAnswers;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;		
	}

	public String getSessionId() {
		return this.sessionId;
	}

	
}
