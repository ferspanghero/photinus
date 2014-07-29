package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

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
	private HashMap<String,HashMap<String,ArrayList<Microtask>>> fileMethodMap;
	
	/** Storage of WorkerSessions */
	private WorkerSessionStorage sessionStorage = new WorkerSessionStorage();
	
	/** The worker session datastructure that will be generated */
	HashMap<Integer, WorkerSession> workerSessionMap;
	
	public WorkerSessionFactory(){
		this.sessionStorage = new WorkerSessionStorage();
		this.sessionID= 1;
		this.workerSessionMap = new HashMap<Integer, WorkerSession>();
		this.fileMethodMap = this.buildMethodMap();
	}
	
	/** 
	 * @param microtaskPerSession the number of microtasks that will be included in each session
	 * @param copies the number of times the same session should be created
	 * 
	 */
	public Stack<WorkerSession> generateSessions(int microtaskPerSession, int copies){
		
		//Final stack that will be persisted
		Stack<WorkerSession> workerSessionStack = new Stack<WorkerSession>();
		
		//Auxiliary lists
		ArrayList<WorkerSession> originalList = new ArrayList<WorkerSession>(); 
		ArrayList<WorkerSession> duplicateList = new ArrayList<WorkerSession>();
		
		
		//Obtain a list of N microtasks from N different codesnippets (i.e., methods)
		ArrayList<Microtask> mtaskList = this.obtainMicrotaskList(microtaskPerSession);
		//Generate the original WorkerSessions
		while(mtaskList.size()>0){
			WorkerSession session = new WorkerSession(sessionID,mtaskList);
			this.sessionID = this.sessionID + 1;
			originalList.add(session);
			mtaskList = this.obtainMicrotaskList(microtaskPerSession);
		}
		
		// produce # copies of the original sessions
		for(int j=0;j<copies;j++){
			//Generate the duplicated WorkerSessions
			for(int i=0;i<originalList.size();i++){
				WorkerSession originalSession = originalList.get(i);
				WorkerSession duplicateSession =  new WorkerSession(this.sessionID, originalSession.getMicrotaskList());
				this.sessionID = this.sessionID + 1;
				duplicateList.add(duplicateSession);
			}
		}
		
		
		//Store sessions in the final Stack
		for(int i=duplicateList.size()-1;i>=0;i--){
			//Traverses the lists from last to first to preserve an ascending order in the stack
			workerSessionStack.push(duplicateList.get(i));
		}
		
		//Store sessions in the final Stack
		for(int i=originalList.size()-1;i>=0;i--){
			//Traverses the lists from last to first to preserve an ascending order in the stack
			workerSessionStack.push(originalList.get(i));
		}
		
		return workerSessionStack;
	//	this.sessionStorage.insertAvailableSessions(workerSessionStack);
		
	}
	
	
	public ArrayList<Microtask> obtainMicrotaskList(int number){

		//TODO  sample N microtasks from different methods from the the fileMethoMap.

		ArrayList<Microtask> resultList = new ArrayList<Microtask>();

		Iterator<String> fileKeyIter = this.fileMethodMap.keySet().iterator();
		int found=0;
		while(found<number && fileKeyIter.hasNext()){
			String fileKey = (String) fileKeyIter.next();
			HashMap<String, ArrayList<Microtask>> methodMap = fileMethodMap.get(fileKey);

			if(methodMap!=null && !methodMap.isEmpty()){
				Iterator<String> methodKeyIter = methodMap.keySet().iterator();
				while(found<number && methodKeyIter.hasNext()){
					String methodKey = (String) methodKeyIter.next();
					ArrayList<Microtask> microtaskList = methodMap.get(methodKey);
					if(microtaskList!=null && !microtaskList.isEmpty()){
						Microtask microtask = microtaskList.get(0);

						resultList.add(microtask);

						microtaskList.remove(0);
						//Put the map back with the element removed
						methodMap.put(methodKey, microtaskList);
						fileMethodMap.put(fileKey, methodMap);
						found++;
					}
				}
			}
		}

		return resultList;
	}
	
	/** Build a datastructure to server as basis to generate the WorkerSession objects, because
	 * each WorkerSession can have only one microtask  for the same method.
	 * 
	 * @return A map indexed by filename and method name
	 */
	public HashMap<String,HashMap<String,ArrayList<Microtask>>> buildMethodMap(){
		// TODO implement a test for this method.
		
		//indexed by fileName and method name and List of microtasks
		HashMap<String,HashMap<String,ArrayList<Microtask>>> fileMethodMap = new HashMap<String,HashMap<String,ArrayList<Microtask>>> ();

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
					fileMethodMap.put(fileName, new HashMap<String,ArrayList<Microtask>>());
				}
				//Iterate over microtasks
				HashMap<Integer, Microtask> microtaskMap = map.getMicrotaskMap();
				Iterator<Integer> microtaskIter = microtaskMap.keySet().iterator();
				while(microtaskIter.hasNext()){
					Integer mtaskID = (Integer) microtaskIter.next();
					Microtask mtask = microtaskMap.get(mtaskID);
					String methodName = mtask.getMethod().getMethodSignature().getName();
									
					HashMap<String,ArrayList<Microtask>> methodMap = fileMethodMap.get(fileName);
					ArrayList<Microtask> methodMicrotaskList = methodMap.get(methodName);
					if(methodMicrotaskList==null){
						methodMicrotaskList = new ArrayList<Microtask>();
					}
					methodMicrotaskList.add(mtask);
					methodMap.put(methodName, methodMicrotaskList);
					fileMethodMap.put(fileName, methodMap);
				}				
			}
		}
		return fileMethodMap;
	}	
}
