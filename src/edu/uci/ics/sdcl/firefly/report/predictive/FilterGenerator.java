package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.ArrayList;

public class FilterGenerator {

	//Max values
	static int maxSessionDuration = 60*60*3;//2h max
	static int maxAnswerDuration = 60*60*3;//2h max
	static int maxConfidence = -1;
	static int maxExplanationSize =-1;//1000;
	static int maxWorkerScore = -1;

	//Min values
	static int minDifficulty = 0;//0;
	static int minWorkerIDKPercentage = 0;//0;

	static int[] sessionDurationList = {120, 180, 240, 360, 420, 480, 600};
	static int[] answerDurationList = {-1};//0, 15, 30, 45, 60, 120, 240};
	static int[] confidenceList = {-1};//{1,2,3,4,5};
	static int[] difficulytList =  {-1};//2, 3, 4,5};//0,1,2,3,4,5};
	static int[] explanationSizeList ={-1};//0,25,50};//, 75, 100};
	static int[] workerScoreList= {-1}; //3, 4, 5};
	static int[] workerExclusionList = {};//{4};
	static int[] IDKpercentageList = {-1}; //{33};
	static String[] professionExclusionList = {"Graduate_Student","Undergraduate_Student"};//{, "Hobbyist","Professional_Developer"};//{,,"Other",,"Graduate_Student", };

	/** Filter answers by session duration */
	public static ArrayList<FilterCombination> generateSessionFilterCombinations(){

		ArrayList<FilterCombination> filterList = new ArrayList<FilterCombination>();

		for(int minDuration: sessionDurationList){
			for(int minConfidence : confidenceList){
				for(int maxDifficulty : difficulytList){
					for(int minExplanationSize : explanationSizeList){
						for(int minWorkerScore : workerScoreList){
							for(int maxWorkerIDKPercentage : IDKpercentageList){
								FilterCombination combination = new FilterCombination();
								combination.addFilterParam(FilterCombination.SESSION_DURATION, maxSessionDuration, minDuration);
								combination.addFilterParam(FilterCombination.CONFIDENCE_LEVEL, maxConfidence, minConfidence);
								combination.addFilterParam(FilterCombination.DIFFICULTY_LEVEL, maxDifficulty, minDifficulty);
								combination.addFilterParam(FilterCombination.EXPLANATION_SIZE, maxExplanationSize, minExplanationSize);
								combination.addFilterParam(FilterCombination.WORKER_SCORE_EXCLUSION, workerExclusionList);
								combination.addFilterParam(FilterCombination.WORKER_SCORE, maxWorkerScore, minWorkerScore);
								combination.addFilterParam(FilterCombination.WORKER_IDK, maxWorkerIDKPercentage, minWorkerIDKPercentage);
								combination.addFilterParam(FilterCombination.WORKER_PROFESSION, professionExclusionList);
								filterList.add(combination);
							}
						}
					}
				}
			}
		}
		return filterList;
	}




	/** Filter answers by answer duration */
	public static ArrayList<FilterCombination> generateAnswerFilterCombinations(){

		ArrayList<FilterCombination> filterList = new ArrayList<FilterCombination>();

		for(int minDuration: answerDurationList){
			for(int minConfidence : confidenceList){
				for(int maxDifficulty : difficulytList){
					for(int minExplanationSize : explanationSizeList){
						for(int minWorkerScore : workerScoreList){
							for(int maxWorkerIDKPercentage : IDKpercentageList){ 
								FilterCombination combination = new FilterCombination();
								combination.addFilterParam(FilterCombination.ANSWER_DURATION, maxAnswerDuration, minDuration);
								combination.addFilterParam(FilterCombination.CONFIDENCE_LEVEL, maxConfidence, minConfidence);
								combination.addFilterParam(FilterCombination.DIFFICULTY_LEVEL, maxDifficulty, minDifficulty);
								combination.addFilterParam(FilterCombination.EXPLANATION_SIZE, maxExplanationSize, minExplanationSize);
								combination.addFilterParam(FilterCombination.WORKER_SCORE_EXCLUSION, workerExclusionList);
								combination.addFilterParam(FilterCombination.WORKER_SCORE, maxWorkerScore, minWorkerScore);
								combination.addFilterParam(FilterCombination.WORKER_IDK, maxWorkerIDKPercentage,minWorkerIDKPercentage);
								combination.addFilterParam(FilterCombination.WORKER_PROFESSION, professionExclusionList);
								filterList.add(combination);
							}
						}
					}
				}
			}
		}
		return filterList;
	}



}
