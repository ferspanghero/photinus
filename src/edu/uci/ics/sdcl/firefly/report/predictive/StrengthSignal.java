package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Strength Signal calculates the number of YES's answers that enable a fault to be located.
 * This number is called "Threshold". 
 * 
 * The threshold is the number of YES's that is larger than the one received by 
 * any non-bug-Covering questions and still smaller or equal to at
 * least one bug-covering question.
 *  
 * If the maxNumber of YES's for Bug-Covering is not larger than the one for Non-Bug-Covering,
 * then returns the difference of YES's between the tow top Bug-Covering and Non-Bug-Covering questions.
 * This difference will be negative and show how many more YES's answers were necessary for the Bug-Covering to be
 * unambiguously distinguished from Non-Bug-Covering questions.
 * 
 * @author adrianoc
 *
 */
public class StrengthSignal extends Predictor{

	private String name = "Strength Signal";
		
	public Integer compute(AnswerData data){
		HashMap<String, Integer> questionYESCountMap = this.computeNumberOfYES(data.getAnswerMap());
		return this.computeThreshold(questionYESCountMap, data.getBugCoveringMap());		
	}
	
	public String getName(){
		return this.name;
	}
	
	
	public StrengthSignal(){
		super();
	}
	//----------------------------------------------------------------------------------------------------------
	
	/**
	 * @param questionOptionsMap questionID and list of answer options (YES, NO, IDK)
	 * @return a map <questionID, number of YES's>
	 */
	private HashMap<String, Integer> computeNumberOfYES(HashMap<String, ArrayList<String>> questionOptionsMap){
		
		HashMap<String, Integer> questionYESCountMap= new HashMap<String, Integer>(); 
		
		for(String questionID: questionOptionsMap.keySet()){
			ArrayList<String> optionList = questionOptionsMap.get(questionID);
			int counter = 0;
			for(String option : optionList){
				//System.out.println(option);
				if(option.compareTo("YES, THERE IS AN ISSUE")==0)
					counter++;
			}
			//System.out.println("counter:"+counter);
			questionYESCountMap.put(questionID, new Integer(counter));
		}
		return questionYESCountMap;
	}
	
	
	/**
	 * The threshold is the number of YES's that is larger than the one received by 
	 * any non-bug-Covering questions and still smaller or equal to at
	 * least one bug-covering question.
	 *  
	 * If the maxNumber of YES's for Bug-Covering is not larger than the one for Non-Bug-Covering,
	 * then returns the difference of YES's between the tow top Bug-Covering and Non-Bug-Covering questions.
	 * This difference will be negative and show how many more YES's answers were necessary for the Bug-Covering to be
	 * unambiguously distinguished from Non-Bug-Covering questions. 
	 * 
	 * @param questionYESCountMap
	 * @param bugCoveringMap
	 * @return if number of max
	 */
	private Integer computeThreshold(HashMap<String, Integer> questionYESCountMap, HashMap<String, String> bugCoveringMap){
		
		int maxYes_BugCovering=0;
		int maxYes_NonBugCovering=0;
		
		//Compute Max YES among Bug Covering
		for(String questionID: questionYESCountMap.keySet()){
			int yesCount = questionYESCountMap.get(questionID).intValue();
			if(bugCoveringMap.containsKey(questionID)){
				maxYes_BugCovering = (maxYes_BugCovering < yesCount) ? yesCount: maxYes_BugCovering;
			}
			else{
				maxYes_NonBugCovering = (maxYes_NonBugCovering < yesCount) ? yesCount: maxYes_NonBugCovering;
			}
		}
		
		if( maxYes_BugCovering > maxYes_NonBugCovering)
			return maxYes_NonBugCovering +1;
		else
			return maxYes_BugCovering - maxYes_NonBugCovering; 
	}
	
	
	public static void main(String[] args){
		
		HashMap<String,String> bugCoveringMap = new HashMap<String,String>();
		
		bugCoveringMap.put("1","1");//1 yes
		bugCoveringMap.put("3","3");//4 yes's
		
		HashMap<String, ArrayList<String>> answerMap = new HashMap<String, ArrayList<String>>();
		
		ArrayList<String> answerList = new ArrayList<String>();//2 yes's
		answerList.add("YES, THERE IS AN ISSUE");
		answerList.add("NO");
		answerList.add("IDK");
		answerList.add("YES, THERE IS AN ISSUE");
		answerMap.put("0",answerList);
		
		answerList = new ArrayList<String>();//1 yes
		answerList.add("YES, THERE IS AN ISSUE");
		answerList.add("NO");
		answerList.add("NO");
		answerList.add("NO");
		answerMap.put("1",answerList);
		
		answerList = new ArrayList<String>();//2 yes's
		answerList.add("YES, THERE IS AN ISSUE");
		answerList.add("YES, THERE IS AN ISSUE");
		answerList.add("NO");
		answerList.add("NO");
		answerMap.put("2",answerList);
		
		answerList = new ArrayList<String>();//4 yes's
		answerList.add("YES, THERE IS AN ISSUE");
		answerList.add("YES, THERE IS AN ISSUE");
		answerList.add("YES, THERE IS AN ISSUE");
		answerList.add("YES, THERE IS AN ISSUE");
		answerMap.put("3",answerList);

		
		String hitFileName = "HIT00_0";
		
		AnswerData data = new AnswerData(hitFileName,answerMap,bugCoveringMap);
		
		StrengthSignal predictor = new StrengthSignal();
		
		System.out.println("expected: 3, actual: "+ predictor.compute(data).toString());
		
	}
	
}
