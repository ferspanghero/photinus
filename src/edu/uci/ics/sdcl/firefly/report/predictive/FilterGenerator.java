package edu.uci.ics.sdcl.firefly.report.predictive;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;

public class FilterGenerator {


	public void bestFilters(){

		HashMap<String, CombinedFilterRange> map;
		CombinedFilterRange range;

		map = FilterGenerator.setupCombineScoreProfession();
		range = map.get(FilterGenerator.WORKER_SCORE_100_NON_STUDENT);		

		map = FilterGenerator.setupProfessionRangeFilters();
		range = map.get(FilterGenerator.WORKER_NON_STUDENT);		

		map = FilterGenerator.setupCombineScoreProfession();
		range = map.get(FilterGenerator.WORKER_SCORE_100_80_NON_STUDENT);	

		map = FilterGenerator.setupScoreRangeFilters();
		range = map.get(FilterGenerator.WORKER_SCORE_100);	

		map = FilterGenerator.setupAnswerDurations();
		range = map.get(FilterGenerator.ANSWER_DURATION_MIN_q1_q1);//Check whether NO's are faster than YES's in average.

		map = FilterGenerator.setupConfidenceDifficulty();
		range = map.get(FilterGenerator.CONFIDENCE_DIFFICULTY_UP_3_PERCENT);

		map =  FilterGenerator.setupExplanationSize();
		range = map.get(FilterGenerator.EXPLANATION_2_3_4_QT_57_2383);

		map =  FilterGenerator.setupQuestionsToExclude();
		range = map.get(FilterGenerator.CONDITIONAL_CLAUSE_ABOVE_3LINES);

		map =  FilterGenerator.setupNoFilters();
		range = map.get(FilterGenerator.NO_FILTERS);
		

		//run the first 6h, 9h, 12h, 24h, 48h using the filtered logs. //Or make a filter for that.

		map =  FilterGenerator.setupDateInterval();
		range = map.get(FilterGenerator.FIRST_9_HOURS);


	}


	/** Filter answers by answer duration */
	public static ArrayList<FilterCombination> generateAnswerFilterCombinations(){

		HashMap<String, CombinedFilterRange> map;
		CombinedFilterRange range;

		map = FilterGenerator.setupCombineScoreProfession();
		range = map.get(FilterGenerator.WORKER_SCORE_100_NON_STUDENT);	

		ArrayList<FilterCombination> filterList = new ArrayList<FilterCombination>();

		for(int minConfidence : range.getConfidenceList()){
			for(int maxDifficulty : range.getDifficulytList()){
				for(int minExplanationSize : range.getExplanationSizeList()){
					for(int minWorkerScore : range.getWorkerScoreList()){
						FilterCombination combination = new FilterCombination();

						combination.addFilterParam(FilterCombination.FIRST_ANSWER_DURATION, range.getMaxFirstAnswerDuration(), range.getMinFirstAnswerDuration());
						combination.addFilterParam(FilterCombination.SECOND_THIRD_ANSWER_DURATION, range.getMaxSecondThirdAnswerDuration(), range.getMinSecondThirdAnswerDuration());
						combination.addFilterParam(FilterCombination.CONFIDENCE_DIFFICULTY_PAIRS,range.getConfidenceDifficultyPairList());
						combination.addFilterParam(FilterCombination.CONFIDENCE_LEVEL, range.getMaxConfidence(), minConfidence);
						combination.addFilterParam(FilterCombination.DIFFICULTY_LEVEL, maxDifficulty,range.getMinDifficulty());
						combination.addFilterParam(FilterCombination.EXPLANATION_SIZE, range.getMaxExplanationSize(), minExplanationSize);
						combination.addFilterParam(FilterCombination.WORKER_SCORE_EXCLUSION, range.getWorkerScoreExclusionList());
						combination.addFilterParam(FilterCombination.WORKER_SCORE, range.getMaxWorkerScore(), minWorkerScore);
						combination.addFilterParam(FilterCombination.WORKER_IDK, range.getMaxWorkerIDKPercentage(),range.getMinWorkerIDKPercentage());
						combination.addFilterParam(FilterCombination.WORKER_PROFESSION, range.getProfessionExclusionList());
						combination.addFilterParam(FilterCombination.WORKER_YEARS_OF_EXEPERIENCE, range.getMaxYearsOfExperience(), range.getMinWorkerYearsOfExperience());
						combination.addFilterParam(FilterCombination.EXCLUDED_QUESTIONS, range.getQuestionsToExcludeMap());
						combination.addFilterParam(FilterCombination.FIRST_HOURS, range.getMaxDate(),range.getMinDate());

						filterList.add(combination);
					}
				}
			}
		}
		return filterList;
	}

	private static String NO_FILTERS = "no filters";
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

	private static final String WORKER_SCORE_NON_STUDENT = "Scores and Non-Students";
	private static final String WORKER_SCORE_STUDENT = "Scores and Students";
	private static final String WORKER_SCORE_80_NON_STUDENT = "Score 80% and Non-Students";
	private static final String WORKER_SCORE80_STUDENT = "Score 80% and Students";
	private static final String WORKER_SCORE60_NON_STUDENT = "Score 60% and Non-Students";
	private static final String WORKER_SCORE60_STUDENT = "Score 60% and Students";
	private static final String WORKER_SCORE60_80_NON_STUDENT = "Score 60% to 80% and Non-Students";
	private static final String WORKER_SCORE60_80_STUDENT = "Score 60% to 80% and Students";
	private static final String WORKER_SCORE_100_NON_STUDENT = "WORKER_SCORE_100_NON_STUDENT";
	private static final String WORKER_SCORE_100_80_NON_STUDENT = "WORKER_SCORE_100_80_NON_STUDENT";

	private static final String WORKER_PERCENT_IDK_1_34 = "WORKER_PERCENT_IDK_1_34";
	private static final String WORKER_PERCENT_IDK_35_67 = "WORKER_PERCENT_IDK_35_67";
	private static final String WORKER_PERCENT_IDK_68_100 = "WORKER_PERCENT_IDK_68_100";
	private static final String WORKER_PERCENT_IDK_0 = "WORKER_PERCENT_IDK_0";

	private static final String WORKER_YEARS_OF_EXPERIENCE_0_1 = "WORKER_YEARS_OF_EXPERIENCE_0_1";
	private static final String WORKER_YEARS_OF_EXPERIENCE_1_5 = "WORKER_YEARS_OF_EXPERIENCE_1_5";
	private static final String WORKER_YEARS_OF_EXPERIENCE_5_10 = "WORKER_YEARS_OF_EXPERIENCE_5_10";
	private static final String WORKER_YEARS_OF_EXPERIENCE_10_15 = "WORKER_YEARS_OF_EXPERIENCE_10_15";
	private static final String WORKER_YEARS_OF_EXPERIENCE_15_50 = "WORKER_YEARS_OF_EXPERIENCE_15_50";

	private static final String SESSION_DURATION_0_2 = "SESSION_DURATION_0_2";
	private static final String SESSION_DURATION_2_4 = "SESSION_DURATION_2_4";
	private static final String SESSION_DURATION_4_8 = "SESSION_DURATION_4_8";

	//First answer 1st Qu.= 160.93s //Second Third answers  1st Qu.:  69.387s
	//FILTER both first and second-third answers 
	private static final String ANSWER_DURATION_MIN_0_0  = "ANSWER_DURATION_MIN_0_0"; 
	private static final String ANSWER_DURATION_MIN_30_15 = "ANSWER_DURATION_MIN_30_15"; 
	private static final String ANSWER_DURATION_MIN_60_30 = "ANSWER_DURATION_MIN_60_30";
	private static final String ANSWER_DURATION_MIN_120_60 = "ANSWER_DURATION_MIN_120_60";
	private static final String ANSWER_DURATION_MIN_q1_q1 = "ANSWER_DURATION_MIN_q1_q1";
	private static final String ANSWER_DURATION_MIN_q1_q3 = "ANSWER_DURATION_MIN_q1_q3";

	//Second Third answers  1st Qu.:  69.387s
	private static final String SECOND_THIRD_ANSWER_DURATION_MIN_15s = "SECOND_THIRD_ANSWER_DURATION_MIN_15s"; 
	private static final String SECOND_THIRD_ANSWER_DURATION_MIN_30s = "SECOND_THIRD_ANSWER_DURATION_MIN_30s";
	private static final String SECOND_THIRD_ANSWER_DURATION_MIN_60s = "SECOND_THIRD_ANSWER_DURATION_MIN_60s";

	private static final String CONFIDENCE_DIFFICULTY_UP_3_PERCENT = "CONFIDENCE_DIFFICULTY_UP_3PERCENT";
	private static final String CONFIDENCE_DIFFICULTY_HIGH_CONFIDENCE = "CONFIDENCE_DIFFICULTY_HIGH_CONFIDENCE";
	private static final String CONFIDENCE_DIFFICULTY_LOW_DIFFICULTY = "CONFIDENCE_DIFFICULTY_LOW_DIFFICULTY";

	private static final String EXPLANATION_1QT_0_53 = "EXPLANATION_1QT_0_53";
	private static final String EXPLANATION_2QT_53_99 = "EXPLANATION_2QT_53_99";
	private static final String EXPLANATION_3QT_99_171 = "EXPLANATION_3QT_99_171";
	private static final String EXPLANATION_4_QT_171_2383 = "EXPLANATION_4_QT_171_2383";
	private static final String EXPLANATION_2_3_4_QT_57_2383 = "EXPLANATION_2_3_4_QT_57_2383";

	private static final String COMBINED_DURATION_CONFIDENCE_EXPLANATIONSIZE_1QT_3PERCENT_1QT = "COMBINED_DURATION_CONFIDENCE_EXPLANATIONSIZE_1QT_3PERCENT_1QT";
	private static final String CONDITIONAL_CLAUSE_ABOVE_3LINES = "CONDITIONAL_CLAUSE_ABOVE_3LINES";

	private static final String FIRST_6_HOURS= "FIRST_6_HOURS";
	private static final String FIRST_9_HOURS= "FIRST_9_HOURS";
	private static final String FIRST_12_HOURS= "FIRST_12_HOURS";
	private static final String FIRST_15_HOURS= "FIRST_15_HOURS";
	private static final String FIRST_18_HOURS= "FIRST_18_HOURS";
	private static final String FIRST_24_HOURS= "FIRST_24_HOURS";
	private static final String FIRST_36_HOURS= "FIRST_36_HOURS";
	private static final String FIRST_48_HOURS= "FIRST_48_HOURS";

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
		range.setRangeName(WORKER_PERCENT_IDK_1_34);
		range.setMaxWorkerIDKPercentage(34);
		range.setMinWorkerIDKPercentage(1);
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_PERCENT_IDK_35_67);
		range.setMaxWorkerIDKPercentage(67);
		range.setMinWorkerIDKPercentage(35);
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_PERCENT_IDK_68_100);
		range.setMaxWorkerIDKPercentage(100);
		range.setMinWorkerIDKPercentage(68);
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_PERCENT_IDK_0);
		range.setMaxWorkerIDKPercentage(0);
		range.setMinWorkerIDKPercentage(0);
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		return rangeMap;
	}

	private static HashMap<String,CombinedFilterRange> setupYearsExperienceRangeFilters(){

		HashMap<String,CombinedFilterRange> rangeMap = new 	HashMap<String,CombinedFilterRange>();

		CombinedFilterRange range = new CombinedFilterRange();
		range.setRangeName(WORKER_YEARS_OF_EXPERIENCE_0_1);
		range.setMaxWorkerYearsOfExperience(1.0);
		range.setMinWorkerYearsOfExperience(0.0);
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_YEARS_OF_EXPERIENCE_1_5);
		range.setMaxWorkerYearsOfExperience(5.0);
		range.setMinWorkerYearsOfExperience(1.1);
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_YEARS_OF_EXPERIENCE_5_10);
		range.setMaxWorkerYearsOfExperience(10.0);
		range.setMinWorkerYearsOfExperience(5.1);
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_YEARS_OF_EXPERIENCE_10_15);
		range.setMaxWorkerYearsOfExperience(15.0);
		range.setMinWorkerYearsOfExperience(10.1);
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		range = new CombinedFilterRange();
		range.setRangeName(WORKER_YEARS_OF_EXPERIENCE_15_50);
		range.setMaxWorkerYearsOfExperience(50.0);
		range.setMinWorkerYearsOfExperience(15.1);
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		return rangeMap;
	} 


	private static HashMap<String,CombinedFilterRange> setupCombineScoreProfession(){

		HashMap<String,CombinedFilterRange> rangeMap = new 	HashMap<String,CombinedFilterRange>();

		//----------------------------------
		CombinedFilterRange range = new CombinedFilterRange();
		range.setRangeName(WORKER_SCORE_NON_STUDENT);

		range.setMaxWorkerScore(5);
		range.setWorkerScoreExclusionList(new int[] {});
		range.setWorkerScoreList(new int[]{3,4,5});

		range.setProfessionExclusionList(new String[] {"Graduate_Student","Undergraduate_Student"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(WORKER_SCORE_80_NON_STUDENT);

		range.setMaxWorkerScore(4);
		range.setWorkerScoreExclusionList(new int[] {3,5});
		range.setWorkerScoreList(new int[]{4});

		range.setProfessionExclusionList(new String[] {"Graduate_Student","Undergraduate_Student"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);


		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(WORKER_SCORE_100_NON_STUDENT);

		range.setMaxWorkerScore(5);
		range.setWorkerScoreExclusionList(new int[] {3,4});
		range.setWorkerScoreList(new int[]{5});

		range.setProfessionExclusionList(new String[] {"Graduate_Student","Undergraduate_Student"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);


		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(WORKER_SCORE_100_80_NON_STUDENT);

		range.setMaxWorkerScore(5);
		range.setWorkerScoreExclusionList(new int[] {3});
		range.setWorkerScoreList(new int[]{4});

		range.setProfessionExclusionList(new String[] {"Graduate_Student","Undergraduate_Student"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);


		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(WORKER_SCORE60_NON_STUDENT);

		range.setMaxWorkerScore(3);
		range.setWorkerScoreExclusionList(new int[] {4,5});
		range.setWorkerScoreList(new int[]{3});

		range.setProfessionExclusionList(new String[] {"Graduate_Student","Undergraduate_Student"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(WORKER_SCORE60_80_NON_STUDENT);

		range.setMaxWorkerScore(4);
		range.setWorkerScoreExclusionList(new int[] {5});
		range.setWorkerScoreList(new int[]{3});

		range.setProfessionExclusionList(new String[] {"Graduate_Student","Undergraduate_Student"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(WORKER_SCORE80_STUDENT);

		range.setMaxWorkerScore(4);
		range.setWorkerScoreExclusionList(new int[] {3,5});
		range.setWorkerScoreList(new int[]{4});

		range.setProfessionExclusionList(new String[] {"Professional_Developer","Hobbyist","Other"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);


		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(WORKER_SCORE60_STUDENT);

		range.setMaxWorkerScore(3);
		range.setWorkerScoreExclusionList(new int[] {4,5});
		range.setWorkerScoreList(new int[]{3});

		range.setProfessionExclusionList(new String[] {"Professional_Developer","Hobbyist","Other"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);


		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(WORKER_SCORE60_80_STUDENT);

		range.setMaxWorkerScore(4);
		range.setWorkerScoreExclusionList(new int[] {5});
		range.setWorkerScoreList(new int[]{3});

		range.setProfessionExclusionList(new String[] {"Professional_Developer","Hobbyist","Other"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		//-----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(WORKER_SCORE_STUDENT);
		range.setMaxWorkerScore(5);
		range.setWorkerScoreExclusionList(new int[] {});
		range.setWorkerScoreList(new int[]{3,4,5});

		range.setProfessionExclusionList(new String[] {"Professional_Developer","Hobbyist","Other"});
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		return rangeMap;
	}


	private static HashMap<String,CombinedFilterRange> setupAnswerDurations(){

		HashMap<String,CombinedFilterRange> rangeMap = new 	HashMap<String,CombinedFilterRange>();

		//----------------------------------
		CombinedFilterRange range = new CombinedFilterRange();
		range.setRangeName(ANSWER_DURATION_MIN_0_0); //NO FILTER

		range.setMaxFirstAnswerDuration(600);//1hour 3rd quartile
		range.setMinFirstAnswerDuration(0);
		range.setMaxSecondThirdAnswerDuration(300);//1hour 3rd quartile
		range.setMinSecondThirdAnswerDuration(0);

		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(ANSWER_DURATION_MIN_30_15); 

		range.setMaxFirstAnswerDuration(1800);//1hour
		range.setMinFirstAnswerDuration(30);
		range.setMaxSecondThirdAnswerDuration(1800);//1hour
		range.setMinSecondThirdAnswerDuration(15);

		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(ANSWER_DURATION_MIN_60_30);

		range.setMaxFirstAnswerDuration(1800);//1hour
		range.setMinFirstAnswerDuration(60);

		range.setMaxSecondThirdAnswerDuration(1800);//1hour
		range.setMinSecondThirdAnswerDuration(30);

		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(ANSWER_DURATION_MIN_120_60);

		range.setMaxFirstAnswerDuration(1800);//1hour
		range.setMinFirstAnswerDuration(120);

		range.setMaxSecondThirdAnswerDuration(1800);//1hour
		range.setMinSecondThirdAnswerDuration(60);

		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(ANSWER_DURATION_MIN_q1_q1);  //Remove first QUARTILE

		range.setMaxFirstAnswerDuration(3600);//1hour 
		range.setMinFirstAnswerDuration(167.4);

		range.setMaxSecondThirdAnswerDuration(3600);//1hour
		range.setMinSecondThirdAnswerDuration(69.9);

		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);
		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(ANSWER_DURATION_MIN_q1_q3);

		range.setMaxFirstAnswerDuration(683.90);//1hour
		range.setMinFirstAnswerDuration(167.4);

		range.setMaxSecondThirdAnswerDuration(228.700);//1hour  
		range.setMinSecondThirdAnswerDuration(69.9);

		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		return rangeMap;
	}

	private static HashMap<String,CombinedFilterRange> setupConfidenceDifficulty(){

		HashMap<String,CombinedFilterRange> rangeMap = new 	HashMap<String,CombinedFilterRange>();

		//----------------------------------
		CombinedFilterRange range = new CombinedFilterRange();
		range.setRangeName(CONFIDENCE_DIFFICULTY_UP_3_PERCENT);  

		HashMap<String, Tuple>  map = Tuple.generateAllCombinations(5, 5);
		map.remove(new Tuple(5,1).toString());
		map.remove(new Tuple(5,2).toString());
		map.remove(new Tuple(5,3).toString());
		map.remove(new Tuple(4,2).toString());
		map.remove(new Tuple(4,3).toString());
		map.remove(new Tuple(4,4).toString());
		map.remove(new Tuple(3,3).toString());
		map.remove(new Tuple(3,4).toString());
		//map.remove(new Tuple(0,4).toString()); don't need to consider IDK answers
		//map.remove(new Tuple(0,5).toString());

		System.out.println("Size of exclusion map:" + map.size());
		range.setConfidenceDifficultyPairMap(map);

		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(CONFIDENCE_DIFFICULTY_HIGH_CONFIDENCE); 
		map = Tuple.generateAllCombinations(5, 5);

		map.remove(new Tuple(5,1).toString());
		map.remove(new Tuple(5,2).toString());
		map.remove(new Tuple(5,3).toString());
		map.remove(new Tuple(5,4).toString());
		map.remove(new Tuple(5,5).toString());

		range.setConfidenceDifficultyPairMap(map);

		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(CONFIDENCE_DIFFICULTY_LOW_DIFFICULTY);   

		map = Tuple.generateAllCombinations(5, 5);
		map.remove(new Tuple(0,1).toString());
		map.remove(new Tuple(1,1).toString());
		map.remove(new Tuple(2,1).toString());
		map.remove(new Tuple(3,1).toString());
		map.remove(new Tuple(4,1).toString());
		map.remove(new Tuple(5,1).toString());


		range.setConfidenceDifficultyPairMap(map);

		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		return rangeMap;
	}

	private static HashMap<String,CombinedFilterRange> setupExplanationSize(){

		HashMap<String,CombinedFilterRange> rangeMap = new 	HashMap<String,CombinedFilterRange>();

		//----------------------------------
		CombinedFilterRange range = new CombinedFilterRange();
		//		range.setRangeName(EXPLANATION_1QT_0_53) ;  //  1st Qu.:  53.0
		//
		//		int[] explanationSizeList_1 = {0};
		//		range.setMaxExplanationSize(53);
		//		range.setExplanationSizeList(explanationSizeList_1);
		//		range.setUndefinedWithDefault();
		//		rangeMap.put(range.getRangeName(),range);
		//
		//		//----------------------------------
		//		range = new CombinedFilterRange();
		//		range.setRangeName(EXPLANATION_2QT_53_99) ;  // Median :  99.0       
		//
		//		int[] explanationSizeList_2 = {53};
		//		range.setMaxExplanationSize(99);
		//		range.setExplanationSizeList(explanationSizeList_2);
		//		range.setUndefinedWithDefault();
		//		rangeMap.put(range.getRangeName(),range);
		//
		//		//----------------------------------
		//
		//		//----------------------------------
		//		range = new CombinedFilterRange();
		//		range.setRangeName(EXPLANATION_3QT_99_171) ;  // 3rd Qu.: 171.0 
		//		int[] explanationSizeList_3 = {99};
		//		range.setMaxExplanationSize(171);
		//		range.setExplanationSizeList(explanationSizeList_3);
		//		range.setUndefinedWithDefault();
		//		rangeMap.put(range.getRangeName(),range);
		//
		//		//----------------------------------
		//		range = new CombinedFilterRange();
		//		range.setRangeName(EXPLANATION_4_QT_171_2383) ;  //  Max 2383           
		//
		//		int[] explanationSizeList_4 = {171};
		//
		//		range.setMaxExplanationSize(2383);
		//
		//		range.setExplanationSizeList(explanationSizeList_4);
		//		range.setUndefinedWithDefault();
		//		rangeMap.put(range.getRangeName(),range);


		//----------------------------------
		range = new CombinedFilterRange();
		range.setRangeName(EXPLANATION_2_3_4_QT_57_2383) ;  // REMOVED 1Quartile          

		int[] explanationSizeList = {57};

		range.setMaxExplanationSize(2383);

		range.setExplanationSizeList(explanationSizeList);
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		return rangeMap;
	}

	private static HashMap<String,CombinedFilterRange> setupCombinedDurationConfidenceDifficultyExplanationSize(){

		HashMap<String,CombinedFilterRange> rangeMap = new 	HashMap<String,CombinedFilterRange>();

		//----------------------------------
		CombinedFilterRange range = new CombinedFilterRange();
		range.setRangeName(COMBINED_DURATION_CONFIDENCE_EXPLANATIONSIZE_1QT_3PERCENT_1QT) ;  //  Exclude 1q and consider only 3% confidence, difficulty cells.


		//Duration
		range.setMaxFirstAnswerDuration(3600);//1hour 
		range.setMinFirstAnswerDuration(167.4);

		range.setMaxSecondThirdAnswerDuration(3600);//1hour
		range.setMinSecondThirdAnswerDuration(69.9);


		//Confidence,Difficulty
		HashMap<String, Tuple>  map = Tuple.generateAllCombinations(5, 5);
		map.remove(new Tuple(5,1).toString());
		map.remove(new Tuple(5,2).toString());
		map.remove(new Tuple(5,3).toString());
		map.remove(new Tuple(4,2).toString());
		map.remove(new Tuple(4,3).toString());
		map.remove(new Tuple(4,4).toString());
		map.remove(new Tuple(3,3).toString());
		map.remove(new Tuple(3,4).toString());
		map.remove(new Tuple(0,4).toString());
		map.remove(new Tuple(0,5).toString());

		range.setConfidenceDifficultyPairMap(map);

		//ExplanationSize
		int[] explanationSizeList_1 = {53}; //Minimal
		range.setExplanationSizeList(explanationSizeList_1);

		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);

		return rangeMap;
	}


	private static HashMap<String,CombinedFilterRange> setupQuestionsToExclude(){

		HashMap<String,CombinedFilterRange> rangeMap = new 	HashMap<String,CombinedFilterRange>();

		//----------------------------------
		CombinedFilterRange range = new CombinedFilterRange();
		range.setRangeName(CONDITIONAL_CLAUSE_ABOVE_3LINES) ;  // REMOVED QUESTIONS COVERING MORE THAN THREE LOCS       

		int[] excludeQuestions = {7,26,40,44,48,55,62,66,82,84,88,92,98,115,124};//Conditional clause questions with more than 3 Lines of code

		TreeMap<String, String> excludedQuestionMap;

		excludedQuestionMap = new TreeMap<String,String>();
		for(int excluded : excludeQuestions){
			String questionID = new Integer(excluded).toString();
			excludedQuestionMap.put(questionID, questionID);
		}	
		range.setQuestionsToExcludeMap(excludedQuestionMap);
		range.setUndefinedWithDefault();
		rangeMap.put(range.getRangeName(),range);
		return rangeMap;
	}

	private static HashMap<String,CombinedFilterRange> setupDateInterval(){

		HashMap<String,CombinedFilterRange> rangeMap = new 	HashMap<String,CombinedFilterRange>();

		CombinedFilterRange range = createDateRange(6,FIRST_6_HOURS);
		rangeMap.put(range.getRangeName(), range);
		//----------------------------------
		range = createDateRange(9,FIRST_9_HOURS);
		rangeMap.put(range.getRangeName(), range);
		//----------------------------------
		range = createDateRange(12,FIRST_12_HOURS);
		rangeMap.put(range.getRangeName(), range);
		//----------------------------------
		range = createDateRange(15,FIRST_15_HOURS);
		rangeMap.put(range.getRangeName(), range);
		//----------------------------------
		range = createDateRange(18,FIRST_18_HOURS);
		rangeMap.put(range.getRangeName(), range);
		//----------------------------------
		range = createDateRange(24,FIRST_24_HOURS);
		rangeMap.put(range.getRangeName(), range);
		//----------------------------------
		range = createDateRange(36,FIRST_36_HOURS);
		rangeMap.put(range.getRangeName(), range);
		//----------------------------------
		range = createDateRange(48,FIRST_48_HOURS);
		rangeMap.put(range.getRangeName(), range);
		
		return rangeMap;

	}

	private static CombinedFilterRange createDateRange(long hours, String label){
		CombinedFilterRange range = new CombinedFilterRange();
		range.setRangeName(label) ; 

		String startDateStr = "Tue 2015 Jul 07 08:19:56";
		DateFormat format = new SimpleDateFormat("EEE yyyy MMM dd HH:mm:ss", Locale.ENGLISH);
		try {
			Date startDate = format.parse(startDateStr);
			long startTime = startDate.getTime();
			long endTime = startTime + hours*3600*1000;
			Date endDate = (Date) startDate.clone();
			endDate.setTime(endTime);
			range.setMaxDate(endDate);
			range.setMinDate(startDate);
			range.setUndefinedWithDefault();
			return range;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}



}
