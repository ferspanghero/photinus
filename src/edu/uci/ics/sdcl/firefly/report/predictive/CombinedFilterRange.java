package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * 
 * This class holds all possible attributes and the upper and lower bound for their values.
 * 
 * Not all attributes need to be set. We used -1 as flag to indicate that the attribute should not be used for filtering.
 * 
 * @author adrianoc
 */
public class CombinedFilterRange {

	private String rangeName;

	//Maximum values
	private double maxSessionDuration = -1; //default is 3h 
	private double maxFirstAnswerDuration = -1; //First answer
	private double maxSecondThirdAnswerDuration = -1; //Second and Third answerss 

	private int maxConfidence= -1; //5
	private int maxDifficulty = -1; //5
	private int maxExplanationSize =-1;//1000
	private int maxWorkerScore= -1; //5, 4, 3
	private double maxYearsOfExperience=-1;
	
	private int maxWorkerIDKPercentage =-1; //
	private Date maxDate=null;
	private int maxAnswers=-1;  //maximum number of answers per question
	private int[] maxAnswerList=null;
	
	//Minimum values
	private int minConfidence = -1; //0
	private int minDifficulty=-1;//0;
	private int minWorkerIDKPercentage =-1;//0;
	private double minWorkerYearsOfExperience=-1; //0
	private double minFirstAnswerDuration = -1;
	private double minSecondThirdAnswerDuration = -1;
	private Date minDate=null;
	
	//Range sets. There are two types. One is for setting the valid range and other to 
	//explicity excluding items that lie within that range.
	private int[] sessionDurationList;//{120, 180, 240, 360, 420, 480, 600};
	private int[] answerDurationList;//0, 15, 30, 45, 60, 120, 240};
	private int[] confidenceList;//{1,2,3,4,5};
	private int[] difficulytList;//2, 3, 4,5};//0,1,2,3,4,5};
	private int[] explanationSizeList;//0,25,50};//, 75, 100};
	private int[] workerScoreList;//3,4,5}; //3, 4, 5};
	private int[] workerScoreExclusionList;//{4};
	private int[] IDKpercentageList; //{25,50,75}; //{33};
	private String[] professionExclusionList;
	private double[] yearsOfExperienceList; //0, 1, 2, 3, 4, 5, 10, 15, 20, 25, 30, 35, 40
	private int minWorkerScore = -1;
	private int minExplanationSize =-1;

	private HashMap<String, Tuple> confidenceDiffcultyPairMap;

	private TreeMap<String,String> questionsToExcludeMap;





	//-----------------------------------------------------
	//getters and setters
	
	public String getRangeName() {
		return rangeName;
	}
	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}
	public double getMaxSessionDuration() {
		return maxSessionDuration;
	}
	public void setMaxSessionDuration(int maxSessionDuration) {
		this.maxSessionDuration = maxSessionDuration;
	}
	public double getMaxFirstAnswerDuration() {
		return maxFirstAnswerDuration;
	}
	public void setMaxFirstAnswerDuration(double d) {
		this.maxFirstAnswerDuration = d;
	}
	
	public double getMaxSecondThirdAnswerDuration() {
		return maxSecondThirdAnswerDuration;
	}
	public void setMaxSecondThirdAnswerDuration(double d) {
		this.maxSecondThirdAnswerDuration = d;
	}
	public int getMaxConfidence() {
		return maxConfidence;
	}
	public void setMaxConfidence(int maxConfidence) {
		this.maxConfidence = maxConfidence;
	}
	public int getMaxExplanationSize() {
		return maxExplanationSize;
	}
	public void setMaxExplanationSize(int maxExplanationSize) {
		this.maxExplanationSize = maxExplanationSize;
	}
	public int getMaxWorkerScore() {
		return maxWorkerScore;
	}
	public void setMaxWorkerScore(int maxWorkerScore) {
		this.maxWorkerScore = maxWorkerScore;
	}
	
	public double getMinFirstAnswerDuration() {
		return this.minFirstAnswerDuration;
	}
	
	public double setMinFirstAnswerDuration(double duration) {
		return this.minFirstAnswerDuration = duration;
	}
	
	public double getMinSecondThirdAnswerDuration() {
		return this.minSecondThirdAnswerDuration;
	}
	
	public double setMinSecondThirdAnswerDuration(double duration) {
		return this.minSecondThirdAnswerDuration = duration;
	}
	
	public int getMinDifficulty() {
		return minDifficulty;
	}
	public void setMinDifficulty(int minDifficulty) {
		this.minDifficulty = minDifficulty;
	}
	public int getMinWorkerIDKPercentage() {
		return minWorkerIDKPercentage;
	}
	public void setMinWorkerIDKPercentage(int minWorkerIDKPercentage) {
		this.minWorkerIDKPercentage = minWorkerIDKPercentage;
	}
	public int[] getSessionDurationList() {
		return sessionDurationList;
	}
	public void setSessionDurationList(int[] sessionDurationList) {
		this.sessionDurationList = sessionDurationList.clone();
	}
	public int[] getAnswerDurationList() {
		return answerDurationList;
	}
	public void setAnswerDurationList(int[] answerDurationList) {
		this.answerDurationList = answerDurationList.clone();
	}
	public int[] getConfidenceList() {
		return confidenceList;
	}
	public void setConfidenceList(int[] confidenceList) {
		this.confidenceList = confidenceList.clone();
	}
	public int[] getDifficulytList() {
		return difficulytList;
	}
	public void setDifficulytList(int[] difficulytList) {
		this.difficulytList = difficulytList.clone();
	}
	public int[] getExplanationSizeList() {
		return explanationSizeList;
	}
	public void setExplanationSizeList(int[] explanationSizeList) {
		this.explanationSizeList = explanationSizeList.clone();
	}
	public int[] getWorkerScoreList() {
		return workerScoreList;
	}
	public void setWorkerScoreList(int[] workerScoreList) {
		this.workerScoreList = workerScoreList.clone();
	}
	public int[] getWorkerScoreExclusionList() {
		return workerScoreExclusionList;
	}
	public void setWorkerScoreExclusionList(int[] workerScoreExclusionList) {
		this.workerScoreExclusionList = workerScoreExclusionList.clone();
	}
	public int[] getIDKpercentageList() {
		return IDKpercentageList;
	}
	public void setIDKpercentageList(int[] iDKpercentageList) {
		IDKpercentageList = iDKpercentageList.clone();
	}
	public String[] getProfessionExclusionList() {
		return professionExclusionList;
	}
	public void setProfessionExclusionList(String[] professionExclusionList) {
		this.professionExclusionList = professionExclusionList.clone();
	}

	public void setYearsOfExperienceList(double[] is) {
		this.yearsOfExperienceList = is.clone();
	}

	public double[] getYearsOfExperienceList() {
		return this.yearsOfExperienceList;
	}
	
	public void setMaxWorkerYearsOfExperience(double i) {
		this.maxYearsOfExperience = i;
	}
	
	public double getMaxYearsOfExperience() {
		return this.maxYearsOfExperience;
	}

	public void setMaxWorkerIDKPercentage(int i) {
		this.maxWorkerIDKPercentage = i;
	}
	
	public int getMaxWorkerIDKPercentage() {
		return this.maxWorkerIDKPercentage;	
	}
	
	public void setMinWorkerYearsOfExperience(double d) {
		this.minWorkerYearsOfExperience = d;
	}
	
	public double getMinWorkerYearsOfExperience() {
		return minWorkerYearsOfExperience;
	}
	
	public void setConfidenceDifficultyPairMap(
			HashMap<String, Tuple> map) {
		this.confidenceDiffcultyPairMap = map;
	}
	
	public HashMap<String,Tuple> getConfidenceDifficultyPairList() {
		return this.confidenceDiffcultyPairMap;
	}
	
	public TreeMap<String,String> getQuestionsToExcludeMap(){
		return this.questionsToExcludeMap;
	}
	
	public void setQuestionsToExcludeMap(TreeMap<String,String>  map){
		this.questionsToExcludeMap = map;
	}
	
	public Date getMaxDate() {
		return maxDate;
	}
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}
	public Date getMinDate() {
		return minDate;
	}
	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}
	
	public int getMaxAnswers() {
		return maxAnswers;
	}
	public void setMaxAnswers(int maxAnswers) {
		this.maxAnswers = maxAnswers;
	}
	
	public void setMaxAnswerList(int[] maxList) {
		this.maxAnswerList = maxList;	
	}
	
	public int[] getMaxAnswerList() {
		return this.maxAnswerList;	
	}
	
	public int getMaxDifficulty() {
		return this.maxDifficulty;
	}
	
	public void setMaxDifficulty(int difficulty) {
		this.maxDifficulty = difficulty;
	}
	
	public int getMinConfidence() {
		return this.minConfidence;
	}
	
	public void setMinConfidence(int confidence) {
		this.minConfidence =  confidence;
	}
	
	public int getMinWorkerScore() {
		return this.minWorkerScore;
	}
	
	public void setMinWorkerScore(int score) {
		this.minWorkerScore = score;
	}
	
	public int getMinExplanationSize() {
		return this.minExplanationSize;
	}
	
	public void setMinExplanationSize(int size){
		this.minExplanationSize = size;
	}
	
	//----------------------------------------
	
	public void setUndefinedWithDefault(){
		
		if(rangeName==null) rangeName = "ALL";

		//Range sets. There are two types. One is for setting the valid range and other to 
		//explicitly excluding items that lie within that range.
		if(sessionDurationList==null) sessionDurationList= new int[] {-1};//{120, 180, 240, 360, 420, 480, 600};
		if(answerDurationList==null) answerDurationList = new int[] {-1};//0, 15, 30, 45, 60, 120, 240};
		if(confidenceList==null) confidenceList = new int[] {-1};//{1,2,3,4,5};
		if(difficulytList==null) difficulytList= new int[] {-1};//2, 3, 4,5};//0,1,2,3,4,5};
		if(explanationSizeList==null)explanationSizeList= new int[] {-1};//0,25,50};//, 75, 100};
		if(workerScoreList==null) workerScoreList=  new int[] {-1};//3,4,5}; //3, 4, 5};
		if(workerScoreExclusionList==null)workerScoreExclusionList = new int[] {};//{4};
		if(IDKpercentageList==null)IDKpercentageList = new int[] {-1}; //{25,50,75}; //{33};
		if(professionExclusionList==null) professionExclusionList = new String[]{};
		if(yearsOfExperienceList==null)yearsOfExperienceList = new double[] {-1.0}; //{25,50,75}; //{33};
		if(confidenceDiffcultyPairMap==null) confidenceDiffcultyPairMap = new HashMap<String,Tuple>();
		if(questionsToExcludeMap==null) questionsToExcludeMap = new TreeMap<String,String>();
		if(this.maxAnswerList==null) maxAnswerList= new int[]{-1};
	}
	
	
	
	
	

}
