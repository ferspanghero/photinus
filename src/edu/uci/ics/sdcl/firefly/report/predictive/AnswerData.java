package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.ArrayList;
import java.util.HashMap;


/** Holds all answers for each question for a Java Method */
public class AnswerData {
	
	/** name of the HIT (HIT01_8, HIT02_24, etc.) */
	String hitFileName;
	
	/** the total different workers in this data set */
	Integer workerCount;
	
	/** questionID, list of answer as YES, NO, IDK */
	HashMap<String, ArrayList<String>> answerMap;
	
	/** list of bug-covering question */
	HashMap<String, String> bugCoveringMap;

	public AnswerData(String hitFileName,
			HashMap<String, ArrayList<String>> answers,
			HashMap<String, String> bugCoveringMap, Integer workerCount) {
		this.hitFileName = hitFileName;
		this.answerMap = answers;
		this.bugCoveringMap = bugCoveringMap;
		this.workerCount = workerCount;
	}

	public String getHitFileName() {
		return hitFileName;
	}

	public Integer getWorkerCount(){
		return this.workerCount;
	}
	
	public HashMap<String, ArrayList<String>> getAnswerMap() {
		return answerMap;
	}

	public HashMap<String, String> getBugCoveringMap() {
		return bugCoveringMap;
	}
	
	
	

}
