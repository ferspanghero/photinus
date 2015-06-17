package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class Worker implements Serializable{
	private static final long serialVersionUID = 1L;
	private String workerId;
	private String consentDate;
	private Hashtable<String,String> surveyAnswersMap;
	private Hashtable<String,String> skillTestAnswerMap;
	private Hashtable<String, String> rubricMap;
	private Hashtable<String, Boolean> gradeMap;
	private Integer grade;
	private String skillTestDuration;
	private String sessionId; //Stores the single working session session allowed per worker
	private String currentFileName; //File name that the worker is requesting to work on.  

	public Worker(String workerId, String consentDateStr, String currentFileName) {
		this.workerId = workerId;
		this.consentDate = consentDateStr;
		this.surveyAnswersMap =  new Hashtable<String,String>();
		this.grade = -1;
		this.skillTestDuration = "";
		this.sessionId = null;		
		this.rubricMap = new Hashtable<String,String>();
		this.skillTestAnswerMap = new Hashtable<String,String>();
		this.gradeMap = new Hashtable<String, Boolean>();
		this.currentFileName = currentFileName;
	}

	public String getWorkerId() {
		return workerId;
	}

	public String getConsentDate() {
		return consentDate;
	}

	public void setConsentDate(String consentDate) {
		this.consentDate = consentDate;
	}	

	public String getCurrentFileName() {
		return currentFileName;
	}

	public void setCurrentFileName(String currentFileName) {
		this.currentFileName = currentFileName;
	}
	
	
	public void addSurveyAnswer(String question, String answer){
		this.surveyAnswersMap.put(question, answer);
	}

	public String getSurveyAnswer(String question){
		return this.surveyAnswersMap.get(question);
	}

	public void setSkillAnswers(Hashtable<String, String> rubricMap,
			Hashtable<String, Boolean> gradeMap, Hashtable<String, String> answerMap, int grade, String duration) {
		this.setRubricMap(rubricMap);
		this.setGradeMap(gradeMap);
		this.setSkillTestAnswersMap(answerMap);
		this.setGrade(grade);
		this.setSkillTestDuration(duration);
	}

	private void setSkillTestAnswersMap(Hashtable<String, String> answerMap) {
		this.skillTestAnswerMap = answerMap;
		
	}

	private void setSkillTestDuration(String duration) {
		this.skillTestDuration = duration;
	}

	public String getSkillTestDuration() {
		return this.skillTestDuration;
	}

	public Hashtable<String, String> getRubricMap() {
		return rubricMap;
	}

	public void setRubricMap(Hashtable<String, String> rubricMap) {
		this.rubricMap = rubricMap;
	}

	public Hashtable<String, Boolean> getGradeMap() {
		return gradeMap;
	}

	public void setGradeMap(Hashtable<String, Boolean> gradeMap) {
		this.gradeMap = gradeMap;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public Hashtable<String, String> getSurveyAnswers() {
		return surveyAnswersMap;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;		
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public String getSurveyAnswersToString(){
		if(this.surveyAnswersMap==null ||this.surveyAnswersMap.isEmpty())
			return "no survey";
		else{
			Iterator<String> iter = this.surveyAnswersMap.keySet().iterator();
			String value="";
			while(iter.hasNext()){
				String key = iter.next();
				String content = this.surveyAnswersMap.get(key);
				if(content==null || content.isEmpty())
					content="-";
				content = content.replaceAll("[\n]"," ").replaceAll("[\r]"," ");
				value = value+key+"%"+content+"% ";	
			}
			return value;
		}
	}

	public boolean hasTakenTest() {
		if(this.grade==null || this.grade<0)
			return false;
		else 
			return true;
	}
	
	public boolean hasPassedTest()
	{
		boolean status = false;
		if (grade>=2)
			status =true;
		return status;
	}


}
