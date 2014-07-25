package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import edu.uci.ics.sdcl.firefly.servlet.MethodData;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;


/**
 * Packages list of microtasks that will be performed in a sequence by a worker.
 * Microtasks within the same session must be from different code snippets.
 *  
 * @author Christian Adriano
 *
 */
public class WorkerSessionFactory {

	/** Starts with 1. It is used to uniquely identify each new WorkerSession */
	private Integer sessionID;
	
	/** The map of all microtasks that will be used to generate WorkerSession objects */
	private HashMap<String,HashMap<String,HashMap<Integer, Microtask>>> fileMethodMap;
	
	/** Storage of WorkerSessions */
	private WorkerSessionStorage sessionStorage = new WorkerSessionStorage();
	
	/** The worker session datastructure that will be generated */
	HashMap<Integer, WorkerSession> workerSessionMap;
	
	public WorkerSessionFactory(){
		this.sessionStorage = new WorkerSessionStorage();
		this.sessionID= 1;
		this.workerSessionMap = new HashMap<Integer, WorkerSession>();
	}
	
	/** 
	 * @param microtaskPerSession the number of microtasks that will be included in each session
	 * @param copies the number of times the same session should be created
	 * 
	 */
	public void generateSessions(int microtaskPerSession, int copies){
		
		//Obtain a list of 10 microtasks from 10 different codesnippets (i.e., methods)
	
		this.fileMethodMap = this.buildMethodMap();
		
		//Create all microtasks
		
		while(!fileMethodMap.isEmpty()){
			ArrayList<Microtask> mtaskList = this.obtainMicrotaskList(microtaskPerSession);
			WorkerSession session = new WorkerSession(sessionID,mtaskList);
			this.sessionID = this.sessionID + 1;
			this.workerSessionMap.put(sessionID, session);
		}
		
		//TODO implement the cloning to happen when all WorkerSession have already been generated.
		WorkerSession session=null;
		//generate copies
		for(int i=0;i<copies;i++){
			WorkerSession copiedSession =  new WorkerSession(this.sessionID, session.getMicrotaskList());
			this.sessionID = this.sessionID + 1;
			this.workerSessionMap.put(sessionID, session);
		}
		
		
		
	}
	
	
	private ArrayList<Microtask> obtainMicrotaskList(int number){
		//TODO  sample n microtasks from different methods from the the fileMethoMap.
		
		
		return null;
	}
	
	/** Build a datastructure to server as basis to generate the WorkerSession objects
	 * 
	 * @return A map indexed by filename and method name
	 */
	private HashMap<String,HashMap<String,HashMap<Integer, Microtask>>> buildMethodMap(){
		// TODO implement a test for this method.
		// TODO consolidate the persistency in one single point, may be the WorkerSession.
		// TODO study how to mitigate loosing the serialized data.
		
		//indexed by fileName and method name and HashMap of microtasks
		HashMap<String,HashMap<String,HashMap<Integer, Microtask>>> fileMethodMap = new HashMap<String,HashMap<String,HashMap<Integer, Microtask>>> ();

		MicrotaskStorage storage = new MicrotaskStorage();
		Set<String> sessionSet= storage.retrieveDebuggingSessionNames();
		if((sessionSet==null) || (!sessionSet.iterator().hasNext())){
			//EMPTY!!!
			System.out.println("DebugSession empty!!");
		}
		else{
			Iterator<String> sessionIterator = sessionSet.iterator();
			while(sessionIterator.hasNext()){
				String fileName = (String) sessionIterator.next();
				FileDebugSession map = storage.read(fileName);
				
				if(fileMethodMap.get(fileName)==null){
					fileMethodMap.put(fileName, new HashMap<String,HashMap<Integer, Microtask>>());
				}
				//Iterate over microtasks
				HashMap<Integer, Microtask> microtaskMap = map.getMicrotaskMap();
				Iterator<Integer> microtaskIter = microtaskMap.keySet().iterator();
				while(microtaskIter.hasNext()){
					Integer mtaskID = (Integer) microtaskIter.next();
					Microtask mtask = microtaskMap.get(mtaskID);
					String methodName = mtask.getMethod().getMethodSignature().getName();
									
					HashMap<String,HashMap<Integer, Microtask>> methodMap = fileMethodMap.get(fileName);
					HashMap<Integer, Microtask> methodMicrotaskMap = methodMap.get(methodName);
					if(methodMicrotaskMap==null){
						methodMicrotaskMap = new HashMap<Integer, Microtask>();
					}
					methodMicrotaskMap.put(mtaskID, mtask);
					methodMap.put(methodName, methodMicrotaskMap);
					fileMethodMap.put(fileName, methodMap);
				}				
			}

		}
		return fileMethodMap;
	}
	
}
