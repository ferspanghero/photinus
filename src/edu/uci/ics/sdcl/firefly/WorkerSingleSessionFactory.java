package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import test.RoundRobinTest.Session;
import edu.uci.ics.sdcl.firefly.controller.MicrotaskSelector;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;
import edu.uci.ics.sdcl.firefly.util.RandomKeyGenerator;


/**
 * Packages list of microtasks that will be performed in a sequence by a worker.
 * Microtasks within the same session must be from different code snippets.
 *  
 * @author Christian Adriano
 *
 */
public class WorkerSingleSessionFactory{

	/** It is used to uniquely identify each new WorkerSession. It is randomly generated.*/
	private String sessionId;

	/** Just counts the number of sessions. It is use as part of the sessionID generation */
	private RandomKeyGenerator keyGenerator;

	/** The map of all microtasks that will be used to generate WorkerSession objects */
	private HashMap<String,HashMap<String,ArrayList<Microtask>>> fileMethodMap;

	/** Storage for Microtasks */
	private MicrotaskStorage microtaskStorage;

	/** The worker session datastructure that will be generated */
	//HashMap<Integer, WorkerSession> workerSessionMap;

	/** 
	 * @param microtaskPerSession the number of microtasks that will be included in each session
	 */	
	private Integer microtaskPerSession;

	public WorkerSingleSessionFactory(int microtaskPerSession){
		this.microtaskStorage =  MicrotaskStorage.initializeSingleton();
		WorkerSessionStorage.initializeSingleton();
		//this.workerSessionMap = new HashMap<Integer, WorkerSession>();
		this.microtaskPerSession = microtaskPerSession;
		this.buildMethodMap();
		this.fillUpFileMethodMap();
		this.keyGenerator = new RandomKeyGenerator();
	}

	/** 
	 * @param copies the number of desired copies
	 * @return the list of worker sessions
	 */
	public Stack<WorkerSession> generateSessions(int answersPerMicrotask){

		//Final stack that will be persisted
		Stack<WorkerSession> workerSessionStack = new Stack<WorkerSession>();

		//Auxiliary lists
		ArrayList<WorkerSession> originalList = new ArrayList<WorkerSession>(); 

		//Obtain a list of N microtasks from N different codesnippets (i.e., methods)
		Vector<Microtask> mtaskList = this.nextMicrotaskList(this.microtaskPerSession);
		//Generate the original WorkerSessions
		while(mtaskList.size()> 0){
			this.sessionId = this.keyGenerator.generate();
			WorkerSession session = new WorkerSession(this.sessionId, mtaskList);
			originalList.add(session);
			mtaskList = this.nextMicrotaskList(this.microtaskPerSession);
		}

		workerSessionStack.addAll(originalList);
		
		if(answersPerMicrotask > 0){
			workerSessionStack.addAll(this.duplicateSessions(workerSessionStack, answersPerMicrotask-1));//Because we already have the original list.
		}
		
		return workerSessionStack;
	}
	
	
	/** 
	 * @param stack the original WorkerSession 
	 * @param copies the number of times the same session should be created
	 * @return the stack with duplicated sessions
	 * 
	 */
	private Stack<WorkerSession> duplicateSessions(Stack<WorkerSession> originalStack, int copies){

		Stack<WorkerSession> duplicateStack = new Stack<WorkerSession>();

		// produce # copies of the original sessions
		for(int j=0;j<copies;j++){
			//Generate the duplicated WorkerSessions
			for(int i=0;i<originalStack.size();i++){
				WorkerSession originalSession = originalStack.elementAt(i);
				this.sessionId = this.keyGenerator.generate();
				WorkerSession duplicateSession =  new WorkerSession(this.sessionId, originalSession.getMicrotaskList());
				duplicateStack.push(duplicateSession);
			}
		}
		return duplicateStack;	
	}
	
	/** for testing purposes */
	public HashMap<String, HashMap<String, ArrayList<Microtask>>> getFileMethodMap() {
		return this.fileMethodMap;
	}
	
	
	
	/**
	 * Produces the next list of microtasks. The list produced is removed from the original set.
	 * 
	 * @param numberOfTasks of microtasks in the list
	 * @return sample N microtasks from different methods from the the fileMethoMap.
	 */
	private Vector<Microtask> nextMicrotaskList(int numberOfTasks){

		Vector<Microtask> resultList = new Vector<Microtask>();
		HashMap<String,Microtask> methodTracker = new HashMap<String,Microtask>();// Tracks whether a method with the same signature was not already added
		
		Iterator<String> fileKeyIter = this.fileMethodMap.keySet().iterator();
	//	boolean traversedTwice = false; //Controls that the fileKeyIter be traversed only twice	
		
		while(methodTracker.size()<numberOfTasks && fileKeyIter.hasNext()){
			String fileKey = (String) fileKeyIter.next();
			HashMap<String, ArrayList<Microtask>> methodMap = fileMethodMap.get(fileKey);

			if(methodMap!=null && !methodMap.isEmpty()){
				Iterator<String> methodKeyIter = methodMap.keySet().iterator();
				while(methodTracker.size()<numberOfTasks && methodKeyIter.hasNext()){
					String methodKey = (String) methodKeyIter.next();
					ArrayList<Microtask> microtaskList = methodMap.get(methodKey);
					if(microtaskList!=null && !microtaskList.isEmpty()){
						int randomPosition = 0;//microtaskList.size()/2;
						Microtask microtask = microtaskList.get(randomPosition);
						String methodName = microtask.getCodeSnippet().getMethodSignature().getName();
						
						//Avoids methods with the same name in the same session.
						//That avoids the risk of having buggy and fixed versions of the same method in the same session.
						if (!methodTracker.containsKey(methodName)){// && !taskAlreadyPerformed(microtask.getID().intValue())){ 
							methodTracker.put(methodName, microtask);
							resultList.add(microtask);
							microtaskList.remove(randomPosition);
							//Put the map back with the element removed
							methodMap.put(methodKey, microtaskList);
							fileMethodMap.put(fileKey, methodMap);
							//System.out.println("microtask added to session: "+microtask.getID());
						}
						else{
							//System.out.println("Successfully dealt with method name collision, method:  "+ methodName);
						}
					}
				}
			}
			
		/**	if(methodTracker.size()<numberOfTasks && !fileKeyIter.hasNext() && !traversedTwice){
				//Means that traversing once we could not fill-up the session with enough microtasks. 
				//Therefore, traverse the list of files>methods>microtasks>methods once more
				traversedTwice=true;
				
				fileKeyIter = this.fileMethodMap.keySet().iterator();
			}*/
			
		}
		
		//System.out.println("number of microtasks in session: "+ methodTracker.size());

		return resultList;
	}

	/** Build a datastructure that indexes filename, methodName and all microtasks for that method name
	 * 
	 * @return A map indexed by filename and method name
	 */
	private void buildMethodMap(){

		//indexed by fileName and method name and List of microtasks
		this.fileMethodMap = new HashMap<String,HashMap<String,ArrayList<Microtask>>> ();


		Set<String> sessionSet= microtaskStorage.retrieveDebuggingSessionNames();
		if((sessionSet==null) || (!sessionSet.iterator().hasNext())){
			//EMPTY!!!
			System.out.println("DebugSession empty!!");
		}
		else{
			Iterator<String> sessionIterator = sessionSet.iterator();
			while(sessionIterator.hasNext()){
				String fileName = (String) sessionIterator.next();
				FileDebugSession map = this.microtaskStorage.read(fileName);

				if(fileMethodMap.get(fileName)==null){
					fileMethodMap.put(fileName, new HashMap<String,ArrayList<Microtask>>());
				}
				//Iterate over microtasks
				Hashtable<Integer, Microtask> microtaskMap = map.getMicrotaskMap();
				Iterator<Integer> microtaskIter = microtaskMap.keySet().iterator();
				while(microtaskIter.hasNext()){
					Integer mtaskID = (Integer) microtaskIter.next();
					Microtask mtask = microtaskMap.get(mtaskID);
					String methodName = mtask.getCodeSnippet().getMethodSignature().getName();

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
	}
	
	/** 
	 * @return the number of microtasks for a specific method in a file
	 */
	private int getNumberOfMicrotasks(String fileName, String methodName){
		
		HashMap<String, ArrayList<Microtask>> mtaskMap = this.fileMethodMap.get(fileName);
		if(mtaskMap==null)
			return 0;
		else{
			ArrayList<Microtask> mtaskList = mtaskMap.get(methodName);
			if(mtaskList==null)
				return 0;
			else
				return mtaskList.size();
		}
	}
	
	/**
	 * Obtains the sizes of all microtask lists for each method
	 * @return the size of the largest microtask list
	 */
	private int obtainLargestSize (){
		int largest = 0;
		Iterator <String> fileNameIterator = this.fileMethodMap.keySet().iterator();
		//Discover the largest microtask list
		while(fileNameIterator.hasNext()){
			String fileName = fileNameIterator.next();
			HashMap<String,ArrayList<Microtask>> methodMap = this.fileMethodMap.get(fileName);
			Iterator <String> methodIterator = methodMap.keySet().iterator();
			while(methodIterator.hasNext()){
				String methodName = methodIterator.next();
				ArrayList<Microtask> microtaskList = methodMap.get(methodName);
				if(microtaskList.size()>largest)
					largest=microtaskList.size();
			}
		}
		return largest;
	}
	
	private ArrayList<Microtask> fillUpList(long numberOfElements, ArrayList<Microtask> list){
		
		ArrayList<Microtask> composedList = new ArrayList<Microtask>();
		composedList.addAll(list);
		int i=0;
		while(composedList.size()<numberOfElements){
			composedList.add(list.get(i));
			i++;
			if(i>=list.size())
				i=0;	
		}
		return composedList;
	}
	
	/** Fill up the list of microtasks so all codesnippets have the same number of microtasks.
	 * That is necessary to guarantee that all Sessions have the same number of microtasks.
	 * 
	 * @return
	 */
	private void fillUpFileMethodMap (){
		int largestSize = this.obtainLargestSize();
		
		int remainder = largestSize % this.microtaskPerSession;
		int fillup;
		if(remainder>0)
			fillup = this.microtaskPerSession - remainder;
		else
			fillup=0;
		int targetSize = largestSize+fillup;
			
		Iterator <String> fileNameIterator = this.fileMethodMap.keySet().iterator();
		//Discover the largest microtask list
		while(fileNameIterator.hasNext()){
			String fileName = fileNameIterator.next();
			HashMap<String,ArrayList<Microtask>> methodMap = this.fileMethodMap.get(fileName);
			Iterator <String> methodIterator = methodMap.keySet().iterator();
			while(methodIterator.hasNext()){
				String methodName = methodIterator.next();
				ArrayList<Microtask> microtaskList = methodMap.get(methodName);
			//	System.out.println("original microtaskList.size = "+ microtaskList.size());
				if(microtaskList.size()>0){
					methodMap.put(methodName, this.fillUpList(targetSize, microtaskList));
					this.fileMethodMap.put(fileName, methodMap);
				}
			//	System.out.println("NEW microtaskList.size = "+ methodMap.get(methodName).size());
			}
		}
	}
	

	/** Method used to generate all microtask in one single session
	 * This method is used only for testing and experiment setup
	 * 
	 * @return a list with a single worker session
	 */
	public Stack<WorkerSession> generateSingleSession(){

		//Stack for the single Session
		Stack<WorkerSession> workerSessionStack = new Stack<WorkerSession>();
		this.sessionId = this.keyGenerator.generate();

		MicrotaskSelector selector = new MicrotaskSelector();
		Vector<Microtask> mtaskList =selector.selectAllMicrotasks();

		WorkerSession session = new WorkerSession(this.sessionId, mtaskList);
		workerSessionStack.push(session);

		return workerSessionStack;
	}

	
	/** Check whether a microtask was already performed */
	public boolean taskAlreadyPerformed(int taskId){
		
		 int [] list =  {0,1,2,3,8,9,10,12,14,15,16,17,18,18,20,20,21,21,22,
					23,25,26,30,31,33,34,35,35,36,37,38,38,39,42,46,50,51,54,56,56,58,
					60,61,61,67,72,73,75,75,77,78,79,81,83,84,85,88,97,99,101,102,109,
					111,112,113,114,115,116,116,117,118,118,119,119,120,121,121,122,122,
					123,123,124,124,124,126,126,126,127,127,128,128,129,129,130,130,131,
					131,132,132,132,133,133,134,134,135,135,136,136,137,137,137,138,139,
					139,142,142,143,143,144,144,145,145,145,145,146,146,146,146,147,147,
					148,148,148,149,149,150,150,151,151,151,152,152,153,153,153,154,154,
					155,156,157,157,157,157,157,158,158,158,158,159,159,159,160,160,161,
					161,161,162,162,162,162,162,162,163,163,163,163,163,163,163,164,164,
					164,164,165,165,165,165,165,166,166,167,167,167,168,168,169,169,169,
					169,170,170,171,171,171,172,172,172,172,173,173,173,173,173,174,174,
					174,174,174,174,175,175,175,175,176,176,176,176,176,176,178,179,180,
					180,180,182,183,184,185,193,194,195,196,197,198,198,199,199,202,203,
					204,205,206,208,210,211,213,214} ;
		
		for(int id: list){
			if(id==taskId)
				return true;
			else
				if(id>taskId)
					break;
		}
		return false;
	}
	
	
}
