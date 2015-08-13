package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Each question has a vote count which is basically Number of YES's minus the Number of NO's.
 * 
 * @author adrianoc
 *
 */
public class MajorityVoting extends Predictor{

	private String name = "Majority Voting";
	
	private HashMap<String, Integer> voteMap;
	
	private AnswerData data;

	public Integer compute(AnswerData data){
		this.data = data;
		HashMap<String, Integer> questionYESCountMap = this.computeNumberOfYES(data.getAnswerMap());
		HashMap<String, Integer> questionNoCountMap = this.computeNumberOfNO(data.getAnswerMap());
		this.voteMap = this.computeQuestionVoteMap(questionYESCountMap,questionNoCountMap); 
		return this.computeLocatedBugFinding(voteMap,data.getBugCoveringMap());	
	}

	public Integer getFalsePositives(){
		if(this.voteMap!=null)
			return computeFalsePositives(this.voteMap, data.getBugCoveringMap());
		else
			return null;
	}
	
	public String getName(){
		return this.name;
	}


	public MajorityVoting(){
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
	 * @param questionOptionsMap questionID and list of answer options (YES, NO, IDK)
	 * @return a map <questionID, number of NO's>
	 */
	private HashMap<String, Integer> computeNumberOfNO(HashMap<String, ArrayList<String>> questionOptionsMap){

		HashMap<String, Integer> questionNOCountMap= new HashMap<String, Integer>(); 

		for(String questionID: questionOptionsMap.keySet()){
			ArrayList<String> optionList = questionOptionsMap.get(questionID);
			int counter = 0;
			for(String option : optionList){
				//System.out.println(option);
				if(option.compareTo("NO, THERE IS NOT AN ISSUE")==0)
					counter++;
			}
			//System.out.println("counter:"+counter);
			questionNOCountMap.put(questionID, new Integer(counter));
		}
		return questionNOCountMap;
	}

	/**
	 * Each question has a vote count which is basically Number of YES's minus the Number of NO's.
	 * 
	 * 
	 * @param questionYESCountMap
	 * @param questionNOCountMap
	 * @return map of questions and respective votes
	 */
	private HashMap<String,Integer> computeQuestionVoteMap(HashMap<String, Integer> questionYESCountMap,
			HashMap<String, Integer> questionNOCountMap) {

		HashMap<String,Integer> voteMap =  new HashMap<String,Integer>();
		for(String questionID : questionYESCountMap.keySet()){
			Integer yesCount = questionYESCountMap.get(questionID);
			Integer noCount = questionNOCountMap.get(questionID);

			Integer vote = yesCount - noCount;
			voteMap.put(questionID, vote);
		}

		return voteMap;
	}

	private Integer computeLocatedBugFinding(HashMap<String, Integer> voteMap,
			HashMap<String, String> bugCoveringMap) {
		
		Integer quantityLocated=0;
		Integer totalBugCoveringInVoteMap=0;
		
		for(String questionID: bugCoveringMap.keySet()){
			Integer vote = voteMap.get(questionID);
			if(vote!=null){
				totalBugCoveringInVoteMap++;
				if(vote>0)
					quantityLocated++;
			}
		}
		
		return totalBugCoveringInVoteMap!=0 ? quantityLocated/totalBugCoveringInVoteMap: 0;
	}
	
	
	private Integer computeFalsePositives(HashMap<String, Integer> voteMap,
			HashMap<String, String> bugCoveringMap) {
		
		Integer quantityFalsePositives=0;

		for(String questionID: voteMap.keySet()){
			if(!bugCoveringMap.containsKey(questionID)){
				Integer vote = voteMap.get(questionID);
				if(vote!=null && vote<0)
					quantityFalsePositives = quantityFalsePositives +1;
			}
		}
		return quantityFalsePositives;
	}

	/** Used to test the Majority voting functions */
	public static void main(String[] args){
		
		HashMap<String,String> bugCoveringMap = new HashMap<String,String>();
		
		bugCoveringMap.put("1","1");// received one yes
		bugCoveringMap.put("3","3");// received four yes's
		
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
		
		MajorityVoting predictor = new MajorityVoting();
		
		Integer bugCoveringQuestionsLocated = predictor.compute(data);
		Integer totalBugCovering = 2;
		
		Double percentageFaults = new Double( bugCoveringQuestionsLocated/totalBugCovering) * 100;
		
		System.out.println("expected: 500% bug covering question located, actual: "+ percentageFaults.toString());
		
		
		Integer falsePositives = predictor.getFalsePositives();
		System.out.println("expected: 1, actual: "+ falsePositives.toString());
	}
	
	
}
