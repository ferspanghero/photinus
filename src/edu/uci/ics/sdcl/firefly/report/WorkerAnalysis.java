package edu.uci.ics.sdcl.firefly.report;

import java.awt.Point;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.report.LogAnalysis.Counter;

/**
 * This analysis was to compute the answers for 1 worker, 2 workers, 3 workers, ....
 * I have however done that in Excel by generation for each question the list of answers order in time.
 * 
 * My plan for this class is to look at how accurate each worker is.
 * For that I would have to compute TP, TN, FP, FN for each worker.
 * 
 * @author Christian Adriano
 *
 */
public class WorkerAnalysis {

	private NumberFormat formatter = new DecimalFormat("#0.00"); 

	LogData data;

	public HashMap<String,Counter> counterMap;

	public HashMap<String, Integer> workerAnswerCountMap;
	public HashMap<String, Integer> workerYesCountMap;
	public HashMap<String, Integer> workerProbablyYesCountMap;
	public HashMap<String, Integer> workerCorrectAnswerMap;
	public HashMap<String, Double> workerPercentCorrectAnswerMap;
	public HashMap<String, Integer> yesCountForCorrectLevelMap;

	public HashMap<String, Integer> workerWrongAnswerMap;
	public HashMap<String, Integer> levelMap;
	public HashMap<String, Integer> percentLevelMap;


	public WorkerAnalysis(LogData data){
		this.data = data;
		counterMap = new HashMap<String,Counter>(); 
	}



	private void computeResultsPerWorkerCluster(){
		int validWorkerCount=0;
		int totalWorkerCount=0;

		HashMap<String, Microtask> microtaskMap = new HashMap<String, Microtask>();

		//Iterate over workers, check if they have answer one task at least
		for(Worker worker: data.workerList){
			int microtaskCount=0;
			totalWorkerCount++;
			String workerId = worker.getWorkerId();
			if(data.checkWorkerDidMinimalOneTask(worker.getWorkerId())){
				validWorkerCount++;
				String sessionId = data.workerMap.get(workerId).getSessionId();
				WorkerSession session = data.sessionMap.get(sessionId);
				Vector<Microtask> microtaskList = session.getMicrotaskList();
				for(Microtask microtask: microtaskList){
					Answer answer = microtask.getAnswerByUserId(workerId);

					Microtask existing = microtaskMap.get(microtask.getID().toString());
					if(existing==null){
						existing = microtask.getSimpleVersion(); //create a new copy of the task, so we don't mixed with the old one.
					}
					else{
						existing.addAnswer(answer);					
					}

					microtaskMap.put(microtask.getID().toString(), existing);
					microtaskCount++;
				}
				//ready to send to compute TP, TN, FP, FN.
				LogAnalysis logAnalysis = new LogAnalysis(this.data);
				System.out.print(" active workers|"+validWorkerCount+"| total workers|"+totalWorkerCount+"| microtasks|"+microtaskCount+"|");
				//logAnalysis.answersPerMethod(true, data.yesMap, microtaskMap);
				logAnalysis.expectedYesAnswers(new Point(0,214),"1buggy_ApacheCamel.txt", microtaskMap, data.yesMap);
				logAnalysis.expectedNoAnswers(new Point(0,214),"1buggy_ApacheCamel.txt", microtaskMap, data.yesMap);

				HashMap<String,Result> resultMap = logAnalysis.bugReportResultMap;
				Result result = resultMap.get("Total");
				result.printNumbers();
			}
		}
	}



	/** 
	 * Produces a data structure of the following:
	 * Map<String key, Map<String workerId, Worker workerObj> >  Key is the level of correct answers (from zero to 10) and
	 * 
	 * 
	 * 
	 */
	private void computeCorrectAnswersPerWorker(){

		workerAnswerCountMap = new HashMap<String,Integer>();
		workerYesCountMap = new HashMap<String,Integer>();
		workerProbablyYesCountMap = new HashMap<String,Integer>();
		workerCorrectAnswerMap = new HashMap<String,Integer>();
		workerWrongAnswerMap = new  HashMap<String,Integer>();

		Iterator<String> iter = data.workerMap.keySet().iterator();

		while(iter.hasNext()){
			String workerId = iter.next();
			Worker worker = data.workerMap.get(workerId);
			String sessionId = worker.getSessionId();
			if(sessionId!=null){
				WorkerSession session = data.sessionMap.get(sessionId);
				Vector<Microtask> taskList = session.getMicrotaskList();
				if(taskList!=null){
					for(Microtask task: taskList){
						Vector<Answer>  answerList = task.getAnswerList();
						for(Answer answer: answerList){
							if(answer.getWorkerId().matches(workerId)){

								incrementAnswer(workerId);
								if(answer.getOption().matches(Answer.YES)){
									incrementYesAnswers(workerId);
								}else
									if(answer.getOption().matches(Answer.PROBABLY_YES)){
										incrementProbablyYesAnswers(workerId);
									}
								//else ignores

								//Check if answer is correct and increment
								checkIncrementCorrectAnswer(task.getID().toString(),answer.getOption(),workerId);

							}
							//else ignores.
						}
					}
				}
			}
		}
		//else do nothing, because session is empty.
		computePercentCorrectWorkerCountMap();
		computeLevelCorrectWorkers();
		computePercentageCorrectAnswersPerWorker();
	}

	private void checkIncrementCorrectAnswer(String microtaskId, String option, String workerId) {
		String value = data.yesMap.get(microtaskId);
		if(value!=null){ //means that microtaskId is yes question.
			if(option.matches(Answer.YES) || option.matches(Answer.PROBABLY_YES)){
				incrementCorrectAnswer(workerId);
			}
			else
				incrementWrongAnswer(workerId);
		}
		else{
			if(option.matches(Answer.NO) || option.matches(Answer.PROBABLY_NOT)){
				incrementCorrectAnswer(workerId);
			}
			else
				incrementWrongAnswer(workerId);
		}

	}

	private void incrementWrongAnswer(String workerId) {
		Integer answerCount = this.workerWrongAnswerMap.get(workerId);
		if(answerCount==null)
			answerCount = new Integer(1);
		else
			answerCount++;
		this.workerWrongAnswerMap.put(workerId, answerCount);	
	}


	private void incrementCorrectAnswer(String workerId) {
		Integer answerCount = this.workerCorrectAnswerMap.get(workerId);
		if(answerCount==null)
			answerCount = new Integer(1);
		else
			answerCount++;
		this.workerCorrectAnswerMap.put(workerId, answerCount);	
	}



	private void incrementProbablyYesAnswers(String workerId) {
		Integer answerCount = this.workerProbablyYesCountMap.get(workerId);
		if(answerCount==null)
			answerCount = new Integer(1);
		else
			answerCount++;
		this.workerProbablyYesCountMap.put(workerId, answerCount);		
	}

	private void incrementYesAnswers(String workerId) {
		Integer answerCount = this.workerYesCountMap.get(workerId);
		if(answerCount==null)
			answerCount = new Integer(1);
		else
			answerCount++;
		this.workerYesCountMap.put(workerId, answerCount);
	}

	/**
	 * Just increments the counter for number of answers given by a worker
	 * @param workerId
	 */
	private void incrementAnswer(String workerId) {
		Integer answerCount = this.workerAnswerCountMap.get(workerId);
		if(answerCount==null)
			answerCount = new Integer(1);
		else
			answerCount++;
		this.workerAnswerCountMap.put(workerId, answerCount);
	}


	private void computePercentCorrectWorkerCountMap(){
		workerPercentCorrectAnswerMap = new HashMap<String,Double>();
		Iterator<String> iter = this.workerAnswerCountMap.keySet().iterator();

		while(iter.hasNext()){
			String workerId = iter.next();
			Integer answerCount = this.workerAnswerCountMap.get(workerId);
			Integer correctCount = this.workerCorrectAnswerMap.get(workerId);
			
			//System.out.println(workerId+"|"+answerCount+"|"+correctCount);
			if(correctCount==null) correctCount = 0;
			workerPercentCorrectAnswerMap.put(workerId, new Double(100*correctCount.doubleValue()/answerCount.doubleValue()));
			
		}
	}
	

	/** 
	 * Computes the percentages of answers correct. The percentages follow in buckets of 10% size, 
	 * starting with zero and ending with 100%).
	 */
	private void computePercentageCorrectAnswersPerWorker(){
		this.percentLevelMap = new HashMap<String, Integer>();
		
		for(int level=0;level<110;level=level+10){
			percentLevelMap.put(new Integer(level).toString(), 0);
		}
	
		Iterator<String> iter = this.workerPercentCorrectAnswerMap.keySet().iterator();

		while(iter.hasNext()){
				String workerId = iter.next();
				Double workerCountPercent = this.workerPercentCorrectAnswerMap.get(workerId);
				
				double num = workerCountPercent.doubleValue()/10;
				int iPart = (int) num;
				Integer integerPart = new Integer(iPart*10);
				
				String workerCountStr = integerPart.toString();
				Integer count = percentLevelMap.get(workerCountStr);
				
				if(count!=null)
					count++;
				else
					count=1;
				
				percentLevelMap.put(workerCountStr,count);			
		}
	}

	private static WorkerAnalysis initializeLogs(){

		LogData data = new LogData(false, 0);

		String path = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\RawDataLogs\\";
		//			"C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\RawDataLogs\\";
		data.processLogProduction1(path);

		//String path = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\RawDataLogs\\";
		data.processLogProduction2(path);

		System.out.println("Logs loaded! Totals are:");
		System.out.println("Consents: "+data.getConsents());
		System.out.println("SkillTests: "+data.getSkillTests());
		System.out.println("Surveys: "+data.getSurveys());
		System.out.println("Sessions Opened: "+data.getOpenedSessions());
		System.out.println("Sessions Closed: "+data.getClosedSessions());
		System.out.println("Answers in Map: "+data.getNumberOfMicrotasks());
		System.out.println("Workers in Map: "+data.workerMap.size());

		WorkerAnalysis analysis = new WorkerAnalysis(data);
		return analysis;
	}

	private void computeLevelCorrectWorkers(){
		
		levelMap = new HashMap<String,Integer>();
		for(int level=0;level<11;level++){
			levelMap.put(new Integer(level).toString(), 0);
		}
	
		Iterator<String> iter = this.workerCorrectAnswerMap.keySet().iterator();

		while(iter.hasNext()){
				String workerId = iter.next();
				Integer workerCount = this.workerCorrectAnswerMap.get(workerId);
				String workerCountStr = workerCount.toString();
				Integer count = levelMap.get(workerCountStr);
				count++;
				levelMap.put(workerCountStr,count);			
		}
	}

	public void printCorrectAnswer_WorkerCount(){

		System.out.println("Workers who answered questions: "+ this.workerAnswerCountMap.size());
		
		Iterator<String> iter = this.workerCorrectAnswerMap.keySet().iterator();

		while(iter.hasNext()){
			String workerId = iter.next();
			Integer workerCount = this.workerCorrectAnswerMap.get(workerId);
			System.out.println(workerId+"|"+workerCount);
		}
	}
	
	
	public void printPercentCorrectAnswer_WorkerCount(){

		System.out.println("Workers who answered questions: "+ this.workerAnswerCountMap.size());
		
		Iterator<String> iter = this.percentLevelMap.keySet().iterator();

		while(iter.hasNext()){
			String level = iter.next();
			Integer workerCount = this.percentLevelMap.get(level);
			System.out.println(workerCount);//+"|"+ level);
		}
		
		
	}
	
	
	public void printWorkerCountPerLevelCorrectAnswers(){

		System.out.println("Workers who answered questions: "+ this.workerAnswerCountMap.size());
		
		Iterator<String> iter = this.levelMap.keySet().iterator();

		while(iter.hasNext()){
			String level = iter.next();
			Integer workerCount = this.levelMap.get(level);
			System.out.println(workerCount); //level);//"|"+
		}
	}
	
	
	public void printWorkerPercentCorrectAnswers(){

		System.out.println("Workers who answered questions: "+ this.workerAnswerCountMap.size());
		
		Iterator<String> iter = this.workerPercentCorrectAnswerMap.keySet().iterator();

		while(iter.hasNext()){
			String workerId = iter.next();
			Double percent = this.workerPercentCorrectAnswerMap.get(workerId);
			System.out.println(workerId+"|"+percent); //level);//"|"+
		}
	}
	
	public void printWorkerYesCount(){

		System.out.println("Workers who answered questions: "+ this.workerAnswerCountMap.size());
		
		Iterator<String> iter = this.workerAnswerCountMap.keySet().iterator();

		while(iter.hasNext()){
			String workerId = iter.next();
			Integer yesCount = this.workerYesCountMap.get(workerId);
			if(yesCount==null) yesCount =0;
			Integer answerCount = this.workerAnswerCountMap.get(workerId);
			Double percent = this.workerPercentCorrectAnswerMap.get(workerId);
			Integer correctCount = this.workerCorrectAnswerMap.get(workerId);
			if(correctCount==null) correctCount =0;
			System.out.println(correctCount); //workerId+"|"+yesCount+"|"+answerCount+"|"+percent.toString()); //level);//"|"+
		}
	}
	
	
	//-------------------------------------------------------------------------------
	// NOT TESTED YET
	
	/**
	 * Just increments the counter for number of answers given by a worker
	 * @param workerId

	 */
	private void incrementWorkerCountYesMap(String correctLevel){
			
			Integer yesCount = this.yesCountForCorrectLevelMap.get(correctLevel);
			if(yesCount==null)
				yesCount = new Integer(1);
			else
				yesCount++;
			this.yesCountForCorrectLevelMap.put(correctLevel, yesCount);
		}
	
	
	public void printWorkerPercentCorrectAnswers(int numberYes){

		yesCountForCorrectLevelMap = new HashMap<String, Integer>();
		Iterator<String> iter = this.workerYesCountMap.keySet().iterator();
		
		while(iter.hasNext()){
			String workerId = iter.next();
			Integer yesCount = workerYesCountMap.get(workerId);
			if(yesCount==numberYes){
				Integer workerCount = this.workerCorrectAnswerMap.get(workerId);
				this.incrementWorkerCountYesMap(workerCount.toString());
			}
			//else discard
		}
	}

	//---------------------------------------------------------------------------------------
	
	public static void main(String[] args){

		WorkerAnalysis analysis = initializeLogs();

		//analysis.computeResultsPerWorkerCluster();

		analysis.computeCorrectAnswersPerWorker();

	//	analysis.printPercentCorrectAnswer_WorkerCount();
		//analysis.printWorkerPercentCorrectAnswers();
		analysis.printWorkerYesCount();
	}


}
