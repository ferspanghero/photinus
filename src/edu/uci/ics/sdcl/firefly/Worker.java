package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;


public class Worker implements Serializable{
	private static final long serialVersionUID = 1L;
	private String workerId;
	private String consentDate;
	private Hashtable<String,String> surveyAnswersMap;
	private String[] skillTestAnswerMap; // THIS IS NOT USED BY ANY CLASS
	private String[] rubricMap;
	private boolean[] gradeMap;
	private Integer grade;
	private String skillTestDuration;
	private String sessionId; //Stores the single working session session allowed per worker
	private String currentFileName; //File name that the worker is requesting to work on. 
	private Hashtable<String, Integer> fileHistory = new Hashtable<String, Integer>();
	private Boolean answeredSurvey;

	private String gender;
	private String country;
	private int yearsProgramming;
	private int age;
	private String feedback;
	private int difficulty;

	public Worker(String workerId, String consentDateStr, String currentFileName) {
		this.workerId = workerId;
		this.consentDate = consentDateStr;
		this.surveyAnswersMap =  new Hashtable<String,String>();
		this.grade = -1;
		this.skillTestDuration = "";
		this.sessionId = null;		
		this.rubricMap = new String[5];
		this.skillTestAnswerMap = new String[5];
		this.gradeMap = new boolean[5];
		this.currentFileName = currentFileName;
		this.answeredSurvey = false;
	}

	public Boolean hasAnsweredSurvey(){
		return this.answeredSurvey;
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
		this.answeredSurvey = true;
	}

	public String getSurveyAnswer(String question){
		return this.surveyAnswersMap.get(question);
	}

	public void setSkillAnswers(String[] rubricMap,
			boolean[] gradeMap, String[] answerMap, int grade, String duration) {
		this.setRubricMap(rubricMap);
		this.setGradeMap(gradeMap);
		this.setSkillTestAnswersMap(answerMap);
		this.setGrade(grade);
		this.setSkillTestDuration(duration);
	}

	private void setSkillTestAnswersMap(String[] answerMap) {
		this.skillTestAnswerMap = answerMap;
		
	}

	public void setSkillTestDuration(String duration) {
		this.skillTestDuration = duration;
	}

	public String getSkillTestDuration() {
		return this.skillTestDuration;
	}

	public String[] getRubricMap() {
		return rubricMap;
	}

	public void setRubricMap(String[] rubricMap2) {
		this.rubricMap = rubricMap2;
	}

	public boolean[] getGradeMap() {
		return gradeMap;
	}

	public void setGradeMap(boolean[] gradeMap2) {
		this.gradeMap = gradeMap2;
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
			value = value.substring(0, value.lastIndexOf("% "));
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
		if (this.grade>=3)
			return true;
		else 
			return false;
	}
	
	public void addFileHistory(String file)
	{
		this.setCurrentFileName(file);
		Integer usedTimes = this.fileHistory.get(file);
		if(usedTimes == null)
			this.fileHistory.put(file, 1);
		else
			this.fileHistory.put(file, usedTimes +1);
	}
	
	public boolean isAllowedFile(String file)
	{
		Integer usedTimes = this.fileHistory.get(file);
		if(usedTimes == null)
			return true;
		else 
			return false;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getYearsProgramming() {
		return yearsProgramming;
	}

	public void setYearsProgramming(int yearsProgramming) {
		this.yearsProgramming = yearsProgramming;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

}
