package edu.uci.ics.sdcl.firefly.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileSessionDTO;

public class AnswerCounter {

	public static String[] fileList = {"HIT01_8", "HIT02_24", "HIT03_6", "HIT04_7",
			"HIT05_35","HIT06_51","HIT07_33","HIT08_54"};//,"HIT10_59"};


	public static void main(String[] args){

		String logPath = "C:/firefly/logs/";
		String firstSession = "session-log-6.log";
		
		String sessionFile = firstSession; 

		HashMap<Integer, Microtask> totalMicrotaskMap = new HashMap<Integer, Microtask>(); 
		HashMap<Integer, Integer> counterMap = new HashMap<Integer, Integer> ();

		FileSessionDTO sessionDTO_1 = new FileSessionDTO(logPath+sessionFile);
		HashMap<String, WorkerSession> sessionMap_1 = sessionDTO_1.getSessions();
		System.out.println("sessionMap_1 size: "+ sessionMap_1.size());
		
		HashMap<String, Microtask> microtaskMap_1 = sessionDTO_1.getMicrotasks();
		System.out.println("microtaskMap_1 size: "+ microtaskMap_1.size());
			
		Iterator<String> iter = microtaskMap_1.keySet().iterator();
		while(iter.hasNext()){
			String taskID = iter.next();
			Microtask task1 = microtaskMap_1.get(taskID);
			Integer count = task1.getNumberOfAnswers();
			counterMap.put(task1.getID(), count);
			totalMicrotaskMap.put(task1.getID(), task1);
		}
		
		String secondSession = "session-log-25.log";
		
		FileSessionDTO sessionDTO_2 = new FileSessionDTO(logPath+secondSession);
		HashMap<String, WorkerSession> sessionMap_2 = sessionDTO_2.getSessions();
		System.out.println("sessionMap_2 size: "+ sessionMap_2.size());
		
		//Count consumed sessions
		HashMap<String, Integer> sessionCountMap =  new HashMap<String, Integer>();
		for(String fileName: fileList){
			sessionCountMap.put(fileName, new Integer(countNumberSessions(sessionMap_2,fileName)));
			
		}
		printSessionCount(sessionCountMap);
		
		//get microtasks for Session-2
		
		HashMap<String, Microtask> microtaskMap_2 = sessionDTO_2.getMicrotasks();
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

		//Print counterMap
		Iterator<Integer> iterCounter = counterMap.keySet().iterator();
		while(iterCounter.hasNext()){
			Integer taskID = iterCounter.next();
			Microtask task = totalMicrotaskMap.get(taskID);
			String fileName = task.getFileName();
			int count = task.getAnswerList().size();
			
			//System.out.println(fileName+";"+taskID+";"+count);	
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
	
}
