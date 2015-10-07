package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.report.predictive.FilterCombination;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;


/**
 * Counts how many:
 * 
 * YES, NO, IDK per Java Method
 * How many answers in each different confidence level
 * How many answers in each different difficulty level
 * 
 * @author adrianoc
 *
 */
public class CountingAnalysis {

	FileSessionDTO sessionDTO;
	FileConsentDTO consentDTO;
	
	HashMap<String,Counter> hitMap;
	
	/** Internal class to hold the results of counting answer attributes */
	public class Counter{
		String hitName;
		String[] confidenceLevels = {"0","1","2","3","4","5"};
		HashMap<String, Integer> confidenceCountMap;
		
		String[] difficultyLevels = {"1","2","3","4","5"};
		HashMap<String, Integer> difficultyCountMap;
		
		String[] answerOptions = {"YES", "NO", "IDK"};
		HashMap<String, Integer> optionCountMap;
		
		public Counter(String hitName){
			this.hitName = hitName;
			confidenceCountMap = this.initialize(confidenceLevels);
			difficultyCountMap = this.initialize(difficultyLevels);
			optionCountMap = this.initialize(answerOptions);
		}
		
		public HashMap<String, Integer> initialize(String[] levelsList){
			HashMap<String, Integer> countMap = new HashMap<String, Integer>();	
			for(String level: levelsList){
				countMap.put(level, new Integer(0));
			}
			return countMap;
		}
		
		public void incrementConfidenceCount(String level){
			Integer value = this.confidenceCountMap.get(level)+1;
			this.confidenceCountMap.put(level, value);
		}
		
		public void incremementDifficultyCount(String level){
			Integer value = this.difficultyCountMap.get(level)+1;
			this.difficultyCountMap.put(level, value);	
		}

		public void incremementAnswerOptionCount(String option){
			Integer value = this.optionCountMap.get(option)+1;
			this.optionCountMap.put(option, value);	
		}
		
		public String toString(){
			String line = "";
			line = optionCountMap.get("YES").toString() +":"+optionCountMap.get("NO").toString()+":"+optionCountMap.get("IDK").toString();  
			
			for(String level: this.confidenceLevels ){
				line = line +  ":"+  confidenceCountMap.get(level).toString(); 
			}
			
			for(String level: this.difficultyLevels ){
				line = line + ":"+  difficultyCountMap.get(level).toString(); 
			}
			
			return line;
		}
		
	}
	
	
	
	public CountingAnalysis(){

		sessionDTO = new FileSessionDTO();
		consentDTO = new FileConsentDTO();
		PropertyManager manager = PropertyManager.initializeSingleton();
		String[] fileNameTokenList = manager.fileNameTokenList.split(":");

		hitMap  =  new 	HashMap<String, Counter>();
		for(String fileName:fileNameTokenList){
			hitMap.put(fileName, new Counter(fileName));
		}
	}
	
	
	
	public HashMap<String,Counter> count(){
		
		HashMap<String,Microtask> microtaskMap = (HashMap<String,Microtask>) sessionDTO.getMicrotasks();		
		for(Microtask microtask: microtaskMap.values()){
			String fileName = microtask.getFileName();
			Vector<Answer> answerList = microtask.getAnswerList();
			for(Answer answer: answerList){			
				addConfidenceCount(fileName, answer.getConfidenceOption());
				addDifficultyCount(fileName, answer.getDifficulty());
				addOptionCount(fileName,answer.getShortOption());
			}
		}
		return this.hitMap;	
	}
	
	
	private void addConfidenceCount(String fileName, Integer confidenceLevel){
		Counter counter = this.hitMap.get(fileName);
		counter.incrementConfidenceCount(confidenceLevel.toString());
		this.hitMap.put(fileName, counter);
	}
	
	private void addDifficultyCount(String fileName, Integer confidenceLevel){
		Counter counter = this.hitMap.get(fileName);
		counter.incremementDifficultyCount(confidenceLevel.toString());
		this.hitMap.put(fileName, counter);
	}
	
	private void addOptionCount(String fileName, String option){
		Counter counter = this.hitMap.get(fileName);
		counter.incremementAnswerOptionCount(option);
		this.hitMap.put(fileName, counter);
	}
	
	
	public void printResults(){	

		String destination = "C://firefly//answerCounterAttributes.txt";
		BufferedWriter log;
		try {
			log = new BufferedWriter(new FileWriter(destination));
			
			//print header
			String line = "Java Method : "+"YES:NO:IDK:0:1:2:3:4:5"+":1:2:3:4:5";
			log.write(line+"\n");
			
			for(String fileName: this.hitMap.keySet()){
				Counter counter = this.hitMap.get(fileName);
				line= fileName+":"+counter.toString();
				log.write(line+"\n");
			}
			log.close();
			System.out.println("file written at: "+destination);
		} 
		catch (Exception e) {
			System.out.println("ERROR while processing file:" + destination);
			e.printStackTrace();
		}
	}
	
	/** 
	 * Returns the worker who divides the workers in the middle. Only count 
	 * workers who have taken at least one microtask.
	 * 
	 * @return workerID
	 */
	public String getFirstPercentWorker(int position){
		
		HashMap<String, Worker> countWorkerMap = new HashMap<String, Worker>();
		ArrayList<String> workerList = new ArrayList<String>();
		
		FileSessionDTO sessionDTO = new FileSessionDTO();
		HashMap<String, WorkerSession> workerSessionMap = (HashMap<String, WorkerSession>) sessionDTO.getSessions();
		
		FileConsentDTO consentDTO =  new FileConsentDTO();
		HashMap<String, Worker> workerMap =  consentDTO.getWorkers();
		
		for(WorkerSession session: workerSessionMap.values()){
			if(session.getMicrotaskListSize()>0){
				String workerID = session.getWorkerId();
				Worker worker = workerMap.get(workerID);
				if(!countWorkerMap.containsKey(workerID)){
					countWorkerMap.put(workerID,worker);
					workerList.add(workerID);
				}
			}
		}
		
		int size  = countWorkerMap.size();
		
		System.out.println("countWorkerMap size: "+size+", position: "+position);


		return workerList.get(size-position);
		
	}
	
	public static void main(String[] args){
		CountingAnalysis counter = new CountingAnalysis();
		//counter.count();
		//counter.printResults();
		
		System.out.println("last 25% worker: "+ counter.getFirstPercentWorker(125));
		//System.out.println("50% worker: "+ counter.getFirstPercentWorker(2));
		
	}
	
}
