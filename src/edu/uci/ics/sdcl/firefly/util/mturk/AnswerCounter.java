package edu.uci.ics.sdcl.firefly.util.mturk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileSessionDTO;

public class AnswerCounter {

	public static HashMap<Integer, Microtask> totalMicrotaskMap; 
	public static HashMap<Integer, Integer> counterMap;

	public static String logPath =  "C:/firefly/logs/";
	
	public static String[] fileList = {"HIT01_8", "HIT02_24", "HIT03_6", "HIT04_7",
			"HIT05_35","HIT06_51","HIT07_33","HIT08_54"};

	
	public static void initializeMaps(){
		totalMicrotaskMap = new HashMap<Integer, Microtask>(); 
		counterMap = new HashMap<Integer, Integer> ();
	}


	public static int countAnswers(HashMap<String, Microtask> map){
		int counter=0;
		for(Microtask task: map.values()){
			counter = task.getNumberOfAnswers() + counter;
		}
		return counter;
	}
	

	public static void main(String[] args){
		AnswerCounter.initializeMaps();
		AnswerCounter.addMicrotasks("session-log-6.log"); //First session
		AnswerCounter.addMicrotasks("session-log-28.log"); //Second session
		AnswerCounter.addMicrotasks("session-log-35.log"); //Third session - topUp1
		AnswerCounter.addMicrotasks("session-log-43.log"); //Fourth session - topUp2
		AnswerCounter.addMicrotasks("session-log-46.log"); //Fifth session - topUp3
		AnswerCounter.addMicrotasks("session-log-53.log"); //Sixth session - topUp4
		AnswerCounter.addMicrotasks("session-log-57.log"); //Seventh session - topUp5
		AnswerCounter.addMicrotasks("session-log-63.log"); //Eight session - topUp6
		
		AnswerCounter.printCounterMap();
	}

	
	/** The number of answers per microtask */
	public static void printCounterMap(){
		Iterator<Integer> iterCounter = counterMap.keySet().iterator();
		while(iterCounter.hasNext()){
			Integer taskID = iterCounter.next();
			Microtask task = totalMicrotaskMap.get(taskID);
			String fileName = task.getFileName();
			int count = task.getAnswerList().size();
			
			System.out.println(fileName+";"+taskID+";"+count);	
		}
	}

	/** Add the new items from the session.log */
	public static void addMicrotasks(String sessionFile){
		
		FileSessionDTO sessionDTO = new FileSessionDTO(logPath+sessionFile);
		HashMap<String, WorkerSession> sessionMap_2 = (HashMap<String, WorkerSession>)sessionDTO.getSessions();
		System.out.println(sessionFile+" number of sessions: "+ sessionMap_2.size());
		
		//Count consumed sessions
		HashMap<String, Integer> sessionCountMap =  new HashMap<String, Integer>();
		for(String fileName: fileList){
			sessionCountMap.put(fileName, new Integer(countNumberSessions(sessionMap_2,fileName)));
			
		}
		printSessionCount(sessionCountMap);
		
		//get microtasks for Session-2
		
		HashMap<String, Microtask> microtaskMap = (HashMap<String, Microtask>) sessionDTO.getMicrotasks();
		System.out.println("number of answers: "+ microtaskMap.size());

		Iterator<String> iter = microtaskMap.keySet().iterator();
		while(iter.hasNext()){
			String taskID = iter.next();
			Microtask task = microtaskMap.get(taskID);
			Integer count = task.getAnswerList().size();
			if(counterMap.containsKey(taskID)){
				Integer aux = counterMap.get(task.getID());
				count = count + aux;
			}
			counterMap.put(task.getID(), count);
			
			if(totalMicrotaskMap.containsKey(task.getID())){
				Microtask task1 = totalMicrotaskMap.get(task.getID());
				Vector<Answer> answerList2 = task.getAnswerList();
				for(Answer answer: answerList2){
					task1.addAnswer(answer);
				}
				totalMicrotaskMap.put(task.getID(), task1);
			}
			else
				totalMicrotaskMap.put(task.getID(), task);
			
			
		}

		
	}

	
	
	public static int countNumberSessions(HashMap<String, WorkerSession> map, String fileName){
		
		int count = 0;
		Iterator<String> iter = map.keySet().iterator();
		while(iter.hasNext()){
			String sessionID = iter.next();
			WorkerSession session = map.get(sessionID);
			//ystem.out.println(session.getFileName());
			if(session.getFileName().compareTo(fileName)==0){
				count++;
			}
		}
		return count;
		
	}
	
	public static void printSessionCount(HashMap<String, Integer> sessionMapCount){
		
		Iterator<String> iter = sessionMapCount.keySet().iterator();
		while(iter.hasNext()){
			String fileName = iter.next();
			Integer count = sessionMapCount.get(fileName);
			System.out.println(fileName+";"+count);
		}
	}
	

//-------------------------------------------------------------------
	public static void oldMain(String[] args){

		String firstSession = "session-log-6.log";
		
		String sessionFile = firstSession; 
		
		FileSessionDTO sessionDTO_1 = new FileSessionDTO(logPath+sessionFile);
		HashMap<String, WorkerSession> sessionMap_1 = (HashMap<String, WorkerSession>) sessionDTO_1.getSessions();
		System.out.println("sessionMap_1 size: "+ sessionMap_1.size());
		
		HashMap<String, Microtask> microtaskMap_1 = (HashMap<String, Microtask>) sessionDTO_1.getMicrotasks();
		System.out.println("microtaskMap_1 size: "+ microtaskMap_1.size());
			
		Iterator<String> iter = microtaskMap_1.keySet().iterator();
		while(iter.hasNext()){
			String taskID = iter.next();
			Microtask task1 = microtaskMap_1.get(taskID);
			Integer count = task1.getNumberOfAnswers();
			counterMap.put(task1.getID(), count);
			totalMicrotaskMap.put(task1.getID(), task1);
		}
		
		String secondSession = "session-log-28.log";
		
		FileSessionDTO sessionDTO_2 = new FileSessionDTO(logPath+secondSession);
		HashMap<String, WorkerSession> sessionMap_2 = (HashMap<String, WorkerSession> )sessionDTO_2.getSessions();
		System.out.println("sessionMap_2 size: "+ sessionMap_2.size());
		
		//Count consumed sessions
		HashMap<String, Integer> sessionCountMap =  new HashMap<String, Integer>();
		for(String fileName: fileList){
			sessionCountMap.put(fileName, new Integer(countNumberSessions(sessionMap_2,fileName)));
			
		}
		printSessionCount(sessionCountMap);
		
		//get microtasks for Session-2
		
		HashMap<String, Microtask> microtaskMap_2 =  (HashMap<String, Microtask>) sessionDTO_2.getMicrotasks();
		System.out.println("microtaskMap_2 size: "+ microtaskMap_2.size());

		Iterator<String> iter2 = microtaskMap_2.keySet().iterator();
		while(iter2.hasNext()){
			String taskID2 = iter2.next();
			Microtask task2 = microtaskMap_2.get(taskID2);
			Integer count = task2.getAnswerList().size();
			if(counterMap.containsKey(taskID2)){
				Integer aux = counterMap.get(task2.getID());
				count = count + aux;
			}
			counterMap.put(task2.getID(), count);
			
			if(totalMicrotaskMap.containsKey(task2.getID())){
				Microtask task1 = totalMicrotaskMap.get(task2.getID());
				Vector<Answer> answerList2 = task2.getAnswerList();
				for(Answer answer: answerList2){
					task1.addAnswer(answer);
				}
				totalMicrotaskMap.put(task2.getID(), task1);
			}
			else
				totalMicrotaskMap.put(task2.getID(), task2);
			
			
		}

	}


}

