package edu.uci.ics.sdcl.firefly.report.predictive;

public class CombinedFilterRange {

	private String rangeName;

	//Max values
	private int maxSessionDuration = 60*60*3; //default is 2h 
	private int maxAnswerDuration = 60*60*3;; //default is 2h 
	private int maxConfidence= -1; //5
	private int maxExplanationSize =-1;//1000
	private int maxWorkerScore= -1; //5, 4, 3
	private double maxYearsOfExperience=-1;
	private int maxWorkerIDKPercentage =100; //

	//Min values
	private int minDifficulty=0;//0;
	private int minWorkerIDKPercentage =0;//0;
	
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



	
	
	public String getRangeName() {
		return rangeName;
	}
	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}
	public int getMaxSessionDuration() {
		return maxSessionDuration;
	}
	public void setMaxSessionDuration(int maxSessionDuration) {
		this.maxSessionDuration = maxSessionDuration;
	}
	public int getMaxAnswerDuration() {
		return maxAnswerDuration;
	}
	public void setMaxAnswerDuration(int maxAnswerDuration) {
		this.maxAnswerDuration = maxAnswerDuration;
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
	
	public void setUndefinedWithDefault(){
		
		if(rangeName==null) rangeName = "ALL";

		//Range sets. There are two types. One is for setting the valid range and other to 
		//explicity excluding items that lie within that range.
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
	}
	

}
