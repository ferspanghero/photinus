package edu.uci.ics.sdcl.firefly.report.predictive;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileConsentDTO;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileSessionDTO;

/**
 * Filter workers by the time they took the microtask.
 * 
 * 25% first workers
 * 50% first workers
 * 25% last workers
 * 50% last workers
 * 
 * @author adrianoc
 *
 */
public class FirstRepondersAnalysis {

	
	String path;
	
	public FirstRepondersAnalysis(){
		path = "c:\\firefly\\ResponseAnalysis\\dataResponse\\";
	}
	
	/** 
	 * Returns the worker who divides the workers in the middle. Only count 
	 * workers who have taken at least one microtask.
	 * 
	 * @param position the last worker that should be accounted
	 * @return workerID
	 */
	public HashMap<String, Worker> buildFirstResponderMap(int position){
		
		HashMap<String, Worker> countWorkerMap = new HashMap<String, Worker>();
		
		FileSessionDTO sessionDTO = new FileSessionDTO();
		HashMap<String, WorkerSession> workerSessionMap = (HashMap<String, WorkerSession>) sessionDTO.getSessions();
		
		FileConsentDTO consentDTO =  new FileConsentDTO();
		HashMap<String, Worker> workerMap =  consentDTO.getWorkers();
		
		Iterator<String> sessionIterator = workerSessionMap.keySet().iterator();
		
		while(sessionIterator.hasNext() && countWorkerMap.size()<position){
			String sessionID = sessionIterator.next();
			WorkerSession session = workerSessionMap.get(sessionID); 	
			if(session.getMicrotaskListSize()>0){
				String workerID = session.getWorkerId();
				Worker worker = workerMap.get(workerID);
				countWorkerMap.put(workerID,worker);
				}
			}
		
		int size  = countWorkerMap.size();
		
		System.out.println("countWorkerMap size: "+size+", position: "+position);

		return countWorkerMap;
	}
	
	/** 
	 *
	 * 
	 * @param position the last worker that should be accounted
	 * @return workerID
	 */
	public HashMap<String, Worker> buildFromLastFirstResponderMap(int position){
		
		HashMap<String, Worker> countWorkerMap = new HashMap<String, Worker>();
		
		FileSessionDTO sessionDTO = new FileSessionDTO();
		HashMap<String, WorkerSession> workerSessionMap = (HashMap<String, WorkerSession>) sessionDTO.getSessions();
		
		FileConsentDTO consentDTO =  new FileConsentDTO();
		HashMap<String, Worker> workerMap =  consentDTO.getWorkers();
		
		Stack<String> sessionStack = new Stack<String>();
		
		Iterator<String> sessionIterator = workerSessionMap.keySet().iterator();
		while(sessionIterator.hasNext() && countWorkerMap.size()<position){
			String sessionID = sessionIterator.next();
			sessionStack.push(sessionID);
		}
		
		while(!sessionStack.isEmpty() && countWorkerMap.size()<position){
			String sessionID = sessionStack.pop();
			WorkerSession session = workerSessionMap.get(sessionID); 	
			if(session.getMicrotaskListSize()>0){
				String workerID = session.getWorkerId();
				Worker worker = workerMap.get(workerID);
				countWorkerMap.put(workerID,worker);
				}
			}
		
		int size  = countWorkerMap.size();
		
		System.out.println("countWorkerMap size: "+size+", position: "+position);

		return countWorkerMap;
	}
	
	
	//filter Session-log
	public void filterLog(HashMap<String, Worker> workerMap, String sourceFileName, String destFileName){
		
	
		ArrayList<String> sessionLogList = this.readToBuffer(sourceFileName);
		ArrayList<String> processedList = new ArrayList<String>();
		
		for(String line:sessionLogList){
			if(lineContainsValidWorker(line,workerMap))
				processedList.add(line);
		}
		
		this.writeBackToBuffer(processedList, destFileName);
		
	}
	
	private boolean lineContainsValidWorker(String line,HashMap<String, Worker> workerMap ){
		
		for(String workerID : workerMap.keySet()){
			if(line.contains(workerID)){
				return true;
			}
		}
		
		return false;
	}
	
	
	/** 
	 * Flush the buffer back to the file
	 * @param newBuffer
	 * @param destFileName
	 */
	private void writeBackToBuffer(ArrayList<String> newBuffer, String destFileName){

		String destination = path  + destFileName;
		BufferedWriter log;
		try {
			log = new BufferedWriter(new FileWriter(destination));
			for(String line : newBuffer)
				log.write(line+"\n");
			log.close();
		} 
		catch (Exception e) {
			System.out.println("ERROR while processing file:" + destination);
			e.printStackTrace();
		}
	}
	
	
	/** Load all the file into a StringBuffer */
	private ArrayList<String> readToBuffer( String sessionLog){

		ArrayList<String> buffer = new ArrayList<String>();

		BufferedReader log;
		try {
			log = new BufferedReader(new FileReader(path + sessionLog));
			String line = null;
			while ((line = log.readLine()) != null) {
				buffer.add(line);
			}
			log.close();
			return buffer;
		} 
		catch (Exception e) {
			System.out.println("ERROR while processing file:" + path+sessionLog);
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args){
		String sessionLog = "session-log_consolidated_final.txt";
		String sessionLogNew = "session-log_consolidated_2nd-25percentWorkers.txt";
		
		String consetLog = "consent-log_consolidated_final.txt";
		String consentLogNew = "consent-log_consolidated_2nd-25percentWorkers.txt";
		

		FirstRepondersAnalysis responders = new FirstRepondersAnalysis();
		
		HashMap<String, Worker> workerMap = responders.buildFromLastFirstResponderMap(128);
		responders.filterLog(workerMap,sessionLog, sessionLogNew);
		responders.filterLog(workerMap,consetLog, consentLogNew);
	}
	
}
