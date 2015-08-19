package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterGenerator {


	/** Filter answers by answer duration */
	public static ArrayList<FilterCombination> generateAnswerFilterCombinations(){

		HashMap<String, CombinedFilterRange> map = FilterGenerator.setupYearsExperienceRangeFilters();
		CombinedFilterRange range = map.get(FilterGenerator.WORKER_YEARS_OF_EXPERIENCE);		

		ArrayList<FilterCombination> filterList = new ArrayList<FilterCombination>();

		for(int minDuration: range.getAnswerDurationList()){
			for(int minConfidence : range.getConfidenceList()){
				for(int maxDifficulty : range.getDifficulytList()){
					for(int minExplanationSize : range.getExplanationSizeList()){
						for(int minWorkerScore : range.getWorkerScoreList()){
							for(int maxWorkerIDKPercentage : range.getIDKpercentageList()){ 
								for(double minYearsOfExperience : range.getYearsOfExperienceList()){ 

									FilterCombination combination = new FilterCombination();
									combination.addFilterParam(FilterCombination.ANSWER_DURATION, range.getMaxAnswerDuration(), minDuration);
									combination.addFilterParam(FilterCombination.CONFIDENCE_LEVEL, range.getMaxConfidence(), minConfidence);
									combination.addFilterParam(FilterCombination.DIFFICULTY_LEVEL, maxDifficulty,range.getMinDifficulty());
									combination.addFilterParam(FilterCombination.EXPLANATION_SIZE, range.getMaxExplanationSize(), minExplanationSize);
									combination.addFilterParam(FilterCombination.WORKER_SCORE_EXCLUSION, range.getWorkerScoreExclusionList());
									combination.addFilterParam(FilterCombination.WORKER_SCORE, range.getMaxWorkerScore(), minWorkerScore);
									combination.addFilterParam(FilterCombination.WORKER_IDK, maxWorkerIDKPercentage,range.getMinWorkerIDKPercentage());
									combination.addFilterParam(FilterCombination.WORKER_PROFESSION, range.getProfessionExclusionList());
									combination.addFilterParam(FilterCombination.WORKER_YEARS_OF_EXEPERIENCE, range.getMaxYearsOfExperience(), minYearsOfExperience);
									filterList.add(combination);
								}
							}
						}
					}
				}
			}
		}
		return filterList;
	}

	private static String NO_FILTERS = "no filters";
	private static String WORKER_YEARS_OF_EXPERIENCE = "years of experience programming";
	private static String WORKER_PERCENT_IDK = "25%, 50%, 75% IDK max";
	private static String WORKER_SCORE_100 ="100% score worker only";
	private static String WORKER_SCORE_80 ="80% score worker only";
	private static String WORKER_SCORE_60 ="60% score worker only";
	private static String WORKER_SCORE_100_80 ="100% and 80% score workers";
	private static String WORKER_SCORE_80_60 ="80% and 60% score workers";
	
	private static String WORKER_PROFESSIONAL_PROGRAMMER ="Professional Developer Only";
	private static String WORKER_GRADUATE_STUDENT ="Graduate Student Only";
	private static String WORKER_UNDERGRADUATE_STUDENT ="Undergraduate Student Only";
	private static String WORKER_HOBBYIST ="Hobbyist Only";
	private static String WORKER_OTHER ="Other Only";
	private static String WORKER_NON_STUDENT ="Non-Student Only";
	private static String WORKER_STUDENT ="Student Only";
	
	
	private static HashMap<String,CombinedFilterRange> setupNoFilters(){

		HashMap<String,CombinedFilterRange> rangeMap = new 	HashMap<String,CombinedFilterRange>();

		CombinedFilterRange range = new CombinedFilterRange();
		range.setRangeName(NO_FILTERS);
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		return rangeMap;
	}
	
	private static HashMap<String,CombinedFilterRange> setupScoreRangeFilters(){

		HashMap<String,CombinedFilterRange> rangeMap = new 	HashMap<String,CombinedFilterRange>();

		//WorkerScore
		CombinedFilterRange range = new CombinedFilterRange();
		range.setRangeName(WORKER_SCORE_100);
		range.setMaxWorkerScore(5);
		range.setWorkerScoreExclusionList(new int[] {3,4});
		range.setWorkerScoreList(new int[]{5});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_SCORE_80);
		range.setMaxWorkerScore(4);
		range.setWorkerScoreExclusionList(new int[] {3});
		range.setWorkerScoreList(new int[]{4});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_SCORE_60);
		range.setMaxWorkerScore(3);
		range.setWorkerScoreList(new int[]{3});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_SCORE_100_80);
		range.setMaxWorkerScore(5);
		range.setWorkerScoreExclusionList(new int[] {3});
		range.setWorkerScoreList(new int[]{4});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_SCORE_80_60);
		range.setMaxWorkerScore(4);
		range.setWorkerScoreExclusionList(new int[] {5});
		range.setWorkerScoreList(new int[]{3,4});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		return rangeMap;

	}

	private static HashMap<String,CombinedFilterRange> setupProfessionRangeFilters(){

		HashMap<String,CombinedFilterRange> rangeMap = new 	HashMap<String,CombinedFilterRange>();

		CombinedFilterRange range = new CombinedFilterRange();
		range.setRangeName(WORKER_PROFESSIONAL_PROGRAMMER);
		range.setProfessionExclusionList(new String[] {"Graduate_Student","Undergraduate_Student","Hobbyist","Other"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_GRADUATE_STUDENT);
		range.setProfessionExclusionList(new String[] {"Professional_Developer","Undergraduate_Student","Hobbyist","Other"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_UNDERGRADUATE_STUDENT);
		range.setProfessionExclusionList(new String[] {"Professional_Developer","Graduate_Student","Hobbyist","Other"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_HOBBYIST);
		range.setProfessionExclusionList(new String[] {"Professional_Developer","Graduate_Student","Undergraduate_Student","Other"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_OTHER);
		range.setProfessionExclusionList(new String[] {"Professional_Developer","Graduate_Student","Undergraduate_Student","Hobbyist"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_NON_STUDENT); 
		range.setProfessionExclusionList(new String[] {"Graduate_Student","Undergraduate_Student"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_STUDENT);
		range.setProfessionExclusionList(new String[] {"Professional_Developer","Hobbyist","Other"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		return rangeMap;
	}

	private static HashMap<String,CombinedFilterRange> setupIDKRangeFilters(){

		HashMap<String,CombinedFilterRange> rangeMap = new 	HashMap<String,CombinedFilterRange>();

		CombinedFilterRange range = new CombinedFilterRange();
		range.setRangeName(WORKER_PERCENT_IDK);
		range.setIDKpercentageList(new int[] {25,50,75});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		return rangeMap;
	}

	private static HashMap<String,CombinedFilterRange> setupYearsExperienceRangeFilters(){

		HashMap<String,CombinedFilterRange> rangeMap = new 	HashMap<String,CombinedFilterRange>();

		CombinedFilterRange range = new CombinedFilterRange();
		range.setRangeName(WORKER_YEARS_OF_EXPERIENCE);
		range.setMaxWorkerYearsOfExperience(50.0);
		range.setYearsOfExperienceList(new double[] {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 10.0, 15.0, 20.0 ,25.0, 30.0, 35.0, 40.0});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		return rangeMap;
	}



//--------------------------------------------------------------------------------------------------------------------
	/** Filter answers by session duration */
	/*public static ArrayList<FilterCombination> generateSessionFilterCombinations(){

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
								combination.addFilterParam(FilterCombination.WORKER_SCORE_EXCLUSION, workerScoreExclusionList);
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
	}*/







}
