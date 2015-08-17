package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import edu.uci.ics.sdcl.firefly.Answer;

/**
 * 
 * WHAT ARE THRESHOLDS?
 * The Positive Voting method calculates the number of YES's answers that enable a fault to be located.
 * This number is called "Threshold". 
 * 
 * The threshold is the number of YES's that is larger than the one received by 
 * any non-bug-Covering questions and still smaller or equal to at
 * least one bug-covering question.
 * 
 * HOW MANY THRESHOLDS? WHY?
 * There are two possible thresholds with two different goals.
 * 
 * Goal of FirstThrehold:  maximize the true positives without producing any zero false positives, even at the cost of 
 * false negatives (bug covering questions below threshold). This goal recognizes that even in face of false negatives, 
 * we still can locate the fault, hence we want to minimize cost (false positives).
 * 
 * Goal of SecondThrehold: obtain at least one true positive with minimal number of false positives. This goal recognizes that 
 * the crowd did not draw a consensus over the fault, but at least two possible faults.
 *
 *HOW ARE THRESHOLDS CALCULATED?
 * FirstThreshold - Threshold at which most bug covering questions are above it and any non-bug covering question is above it.
 *  
 * If the maxNumber of YES's for Bug-Covering is not larger than the one for Non-Bug-Covering,
 * then returns the difference of YES's between the tow top Bug-Covering and Non-Bug-Covering questions.
 * This difference will be negative and show how many more YES's answers were necessary for the Bug-Covering to be
 * unambiguously distinguished from Non-Bug-Covering questions.
 *
 * Second Threshold - Threshold at which at least one bug covering question is above it and the fewest number of non-bug covering 
 * questions are above it.
 * 
 * There will be only one active thresholds, which will have the following attributes also calculated by this class:
 * - Number of True Positives
 * - Number of True Negatives
 * - Number of False Positives
 * - Number of False Negatives
 * - Strength of Signal
 * - Number of workers necessary for Threshold
 * 
 * @author adrianoc
 *
 */
public class PositiveVoting extends Predictor{

	private String name = "Positive Voting";

	private Integer maxYES_BugCovering=0;

	private Integer maxYES_NonBugCovering=0;

	private HashMap<String, Integer> questionYESCountMap;

	AnswerData data;

	private Integer firstThreshold=null;

	private Integer secondThreshold=null;

	private Integer finalThreshold=null;

	public PositiveVoting(){
		super();
	}

	public String getName(){
		return this.name;
	}

	@Override
	public Boolean computeSignal(AnswerData data){

		this.maxYES_BugCovering = 0;
		this.maxYES_NonBugCovering = 0;

		this.data = data;
		this.questionYESCountMap = this.computeNumberOfAnswers(Answer.YES);
		finalThreshold =  this.computeFirstThreshold();
	//	System.out.println(data.getHitFileName()+":"+finalThreshold+":"+getTruePositives());

		if(finalThreshold<0){
			finalThreshold = this.computeSecondThreshold();
		}

		if(finalThreshold>0)
			return true;
		else
			return false;
	}

	public Integer getThreshold(){
		return this.finalThreshold;
	}

	public Integer getFirstThreshold(){
		return this.firstThreshold;
	}

	public Integer getSecondThreshold(){
		return this.secondThreshold;
	}

	@Override
	/**
	 * @return if the threshold is positive, then returns the difference between Maximum YES's and the Threshold. If 
	 * threshold is zero, then return -1.0
	 */
	public Double computeSignalStrength(AnswerData data) {

		if(this.finalThreshold==null)
			this.computeSignal(data);

		if(finalThreshold>0){	
			Double truePositiveRate = this.computeTruePositiveRate();
			Integer extraVotes = this.maxYES_BugCovering - this.finalThreshold;
			return truePositiveRate * extraVotes;
		}
		else
			return -1.0;
	}

	@Override
	public Integer computeNumberOfWorkers(AnswerData data) {
		int maxAnswers=0; 
		for(ArrayList<String> answerList :  data.answerMap.values()){
			if(answerList.size()>maxAnswers)
				maxAnswers = answerList.size();
		}
		return maxAnswers;
	}

	@Override
	public Integer getTruePositives() {

		if(this.finalThreshold<=0)
			return 0;
		else{
			int count=0;
			for(String questionID: this.questionYESCountMap.keySet()){
				if(data.bugCoveringMap.containsKey(questionID)){
					Integer yesCount = this.questionYESCountMap.get(questionID);
					if(yesCount!=null && yesCount>=this.finalThreshold){
						count++;
					}
				}
			}
			return count;
		}
	}

	@Override
	public Integer getTrueNegatives() {

		if(this.finalThreshold<=0)
			return 0;
		else{
			int count=0;
			for(String questionID: this.questionYESCountMap.keySet()){
				if(!data.bugCoveringMap.containsKey(questionID)){
					Integer yesCount = this.questionYESCountMap.get(questionID);
					if(yesCount<this.finalThreshold){
						count++;
					}
				}
			}
			return count;
		}
	}

	@Override
	public Integer getFalsePositives() {

		int count=0;
		for(String questionID: this.questionYESCountMap.keySet()){
			if(!data.bugCoveringMap.containsKey(questionID)){
				Integer yesCount = this.questionYESCountMap.get(questionID);
				if(yesCount>=this.finalThreshold || this.finalThreshold<=0){
					count++;
				}
			}
		}
		return count;
	}

	@Override
	public Integer getFalseNegatives() {

		int count=0;
		for(String questionID: this.questionYESCountMap.keySet()){
			if(data.bugCoveringMap.containsKey(questionID)){
				Integer yesCount = this.questionYESCountMap.get(questionID);
				if(yesCount<this.finalThreshold || this.finalThreshold<=0){
					count++;
				}
			}
		}
		return count;
	}

	@Override
	public Integer getNumberBugCoveringQuestions(){

		Integer count=0;

		for(String questionID: this.questionYESCountMap.keySet()){
			if(data.bugCoveringMap.containsKey(questionID))			
				count ++;
		}
		return count;
	}


	//----------------------------------------------------------------------------------------------------------

	/**
	 * @param answerOption the answer option (YES, NO, IDK)
	 * @return a map <questionID, number of answer of that option>
	 */
	private HashMap<String, Integer> computeNumberOfAnswers(String answerOption){

		HashMap<String, Integer> countMap = new HashMap<String, Integer>(); 

		for(String questionID: data.answerMap.keySet()){
			ArrayList<String> optionList = data.answerMap.get(questionID);
			int counter = 0;
			for(String option : optionList){
				//System.out.println(option);
				if(option.compareTo(answerOption)==0)
					counter++;
			}
			//System.out.println("counter:"+counter);
			countMap.put(questionID, new Integer(counter));
		}
		return countMap;
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
	private Integer computeFirstThreshold(){
		//this.maxYES_BugCovering = 0;
		//this.maxYES_NonBugCovering = 0;

		//Compute Max YES among Bug Covering
		for(String questionID: this.questionYESCountMap.keySet()){
			int yesCount = this.questionYESCountMap.get(questionID).intValue();
			//if(this.data.getHitFileName().compareTo("HIT06_51")==0)
			//System.out.println(questionID+":"+yesCount);

			if(data.bugCoveringMap.containsKey(questionID)){
				if(maxYES_BugCovering < yesCount)
					maxYES_BugCovering = yesCount;
			}
			else{
				maxYES_NonBugCovering = (maxYES_NonBugCovering < yesCount) ? yesCount: maxYES_NonBugCovering;
			}
		}

		//if(this.data.getHitFileName().compareTo("HIT06_51")==0){
			//System.out.println(maxYES_BugCovering+":"+maxYES_NonBugCovering);

		//}


		if( maxYES_BugCovering > maxYES_NonBugCovering)
			this.firstThreshold = maxYES_NonBugCovering +1;
		else
			this.firstThreshold = maxYES_BugCovering - maxYES_NonBugCovering;

		return this.firstThreshold;
	}

	private Integer computeSecondThreshold(){

		//Check if the maxYES_BugCovering is larger than at least one non-BugCovering question.		
		if(isThere_NonBugCoveringBelow(this.maxYES_BugCovering))
			this.secondThreshold = this.maxYES_BugCovering;
		else
			this.secondThreshold = -1; //There is not a viable threshold 

		return this.secondThreshold;		
	}

	/** 
	 * Looks for a non-bug covering question that has less YES's than the MaxYES's of 
	 * bug covering questions.
	 * 
	 * @param maxYES_BugCovering
	 * @return true if found a non-bug covering with more YES's than maxYes_Covering, otherwise, false.
	 */
	private boolean isThere_NonBugCoveringBelow(Integer maxYES_BugCovering) {

		Iterator<String> iter = this.questionYESCountMap.keySet().iterator();
		boolean found = false;

		while(iter.hasNext() && !found){
			String questionID= iter.next();
			if(!data.bugCoveringMap.containsKey(questionID)){
				Integer yesCount = this.questionYESCountMap.get(questionID);
				if(yesCount<this.maxYES_BugCovering)
					found=true;
			}
		}
		return found;
	}

	private Integer computeTruePositives(){

		Integer count=0;

		for(String questionID: questionYESCountMap.keySet()){
			if(data.bugCoveringMap.containsKey(questionID)){
				Integer vote = questionYESCountMap.get(questionID);
				if(vote!=null && vote>=this.finalThreshold){
					count++;
				}
			}
		}	
		return count;
	}


	private Integer computeTrueNegatives(){

		Integer count=0;

		for(String questionID: questionYESCountMap.keySet()){
			if(!data.bugCoveringMap.containsKey(questionID)){
				Integer vote = questionYESCountMap.get(questionID);
				if(vote!=null && vote<=this.maxYES_NonBugCovering){
					count++;
				}
			}
		}	
		return count;
	}


	private Double computeTruePositiveRate(){
		Double numberOfBugCovering = this.getNumberBugCoveringQuestions().doubleValue();
		Double numberOfTruePositives = this.getTruePositives().doubleValue();
		return numberOfTruePositives / numberOfBugCovering;
	}

	// NOT COMPLETED IMPLEMENTED.
	//------------------------------------------------------------------------------------------------------------------
	private Integer computeNextThreshold(HashMap<String, Integer> selectedQuestionYESCountMap){

		ArrayList<String> questionsSortedByYESCount = sortQuestionsByYES(selectedQuestionYESCountMap);

		Integer threshold=0;

		while(selectedQuestionYESCountMap.size()>0 && threshold<=0){

			questionsSortedByYESCount = sortQuestionsByYES(selectedQuestionYESCountMap);

			threshold = computeNextThreshold(selectedQuestionYESCountMap);
			if(threshold<=0)
				selectedQuestionYESCountMap = removeTopNonBugCovering(selectedQuestionYESCountMap);
		}

		return threshold;
	}

	private ArrayList<String> sortQuestionsByYES(HashMap<String, Integer> selectedQuestionYESCountMap){
		return null;
	}

	private HashMap<String, Integer> removeTopNonBugCovering(HashMap<String, Integer> selectedQuestionYESCountMap){
		return null;
	}
	//-----------------------------------------------------------------------------------------------------------------


	//----------------------------------------------------------------------------------------------------------------

	public static void main(String[] args){

		HashMap<String,String> bugCoveringMap = new HashMap<String,String>();

		bugCoveringMap.put("1","1");//1 yes
		bugCoveringMap.put("3","3");//4 yes's

		HashMap<String, ArrayList<String>> answerMap = new HashMap<String, ArrayList<String>>();

		ArrayList<String> answerList = new ArrayList<String>();//2 yes's
		answerList.add(Answer.YES);
		answerList.add(Answer.NO);
		answerList.add("IDK");
		answerList.add(Answer.YES);
		answerMap.put("0",answerList);

		answerList = new ArrayList<String>();//1 yes
		answerList.add(Answer.YES);
		answerList.add(Answer.NO);
		answerList.add(Answer.NO);
		answerList.add(Answer.NO);
		answerMap.put("1",answerList);

		answerList = new ArrayList<String>();//2 yes's
		answerList.add(Answer.YES);
		answerList.add(Answer.YES);
		answerList.add(Answer.NO);
		answerList.add(Answer.NO);
		answerMap.put("2",answerList);

		answerList = new ArrayList<String>();//4 yes's
		answerList.add(Answer.YES);
		answerList.add(Answer.YES);
		answerList.add(Answer.YES);
		answerList.add(Answer.YES);
		answerMap.put("3",answerList);

		String hitFileName = "HIT00_0";

		AnswerData data = new AnswerData(hitFileName,answerMap,bugCoveringMap,4,4);

		PositiveVoting predictor = new PositiveVoting();

		System.out.println("expected: 3, actual: "+ predictor.computeSignal(data).toString());	
	}



}
