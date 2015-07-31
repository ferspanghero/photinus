package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.ArrayList;
import java.util.HashMap;

/** Holds all answers for each question for a Java Method */
public class AnswerData {
	
	/** name of the HIT (HIT01_8, HIT02_24, etc.) */
	String hitFileName;
	
	/** questionID, list of answer as YES, NO, IDK */
	HashMap<String, ArrayList<String>> answerMap;
	
	/** list of bug-covering question */
	HashMap<String, String> bugCoveringMap;

	public AnswerData(String hitFileName,
			HashMap<String, ArrayList<String>> answerMap,
			HashMap<String, String> bugCoveringMap) {
		this.hitFileName = hitFileName;
		this.answerMap = answerMap;
		this.bugCoveringMap = bugCoveringMap;
	}

	public String getHitFileName() {
		return hitFileName;
	}

	public HashMap<String, ArrayList<String>> getAnswerMap() {
		return answerMap;
	}

	public HashMap<String, String> getBugCoveringMap() {
		return bugCoveringMap;
	}
	
	
	

}
