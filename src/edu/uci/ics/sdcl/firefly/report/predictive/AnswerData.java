package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.ArrayList;
import java.util.HashMap;


/** Holds all answers for each question for a Java Method */
public class AnswerData {
	
	/** name of the HIT (HIT01_8, HIT02_24, etc.) */
	String hitFileName;
	
	/** the total different workers in this data set */
	Integer workerCount;
	
	/** Total workers that remained after applying the combined filter */
	Integer differentWorkersAmongHITs;
	
	/** questionID, list of answer as YES, NO, IDK */
	HashMap<String, ArrayList<String>> answerMap;
	
	/** list of bug-covering question */
	HashMap<String, String> bugCoveringMap;

	public AnswerData(String hitFileName,
			HashMap<String, ArrayList<String>> answers,
			HashMap<String, String> bugCoveringMap, Integer workerCount, Integer differentWorkersAmongHITs) {
		this.hitFileName = hitFileName;
		this.answerMap = answers;
		this.bugCoveringMap = bugCoveringMap;
		this.workerCount = workerCount;
		this.differentWorkersAmongHITs = differentWorkersAmongHITs;
	}

	public String getHitFileName() {
		return hitFileName;
	}

	public Integer getWorkerCount(){
		return this.workerCount;
	}	
	
	public Integer getDifferentWorkersAmongHITs() {
		return this.differentWorkersAmongHITs;
	}

	public HashMap<String, ArrayList<String>> getAnswerMap() {
		return answerMap;
	}

	public HashMap<String, String> getBugCoveringMap() {
		return bugCoveringMap;
	}

	public Integer getTotalAnswers() {
		int answerCount = 0;
		for(String questionID: this.answerMap.keySet()){
			ArrayList<String> answerList = answerMap.get(questionID);
			answerCount = answerCount + answerList.size();
		}
		return answerCount;
	}
	
	
	

}
