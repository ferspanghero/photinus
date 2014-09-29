package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import edu.uci.ics.sdcl.firefly.controller.MicrotaskSelector;
import edu.uci.ics.sdcl.firefly.controller.MicrotaskSelector.SelectorReturn;
import edu.uci.ics.sdcl.firefly.servlet.MethodData;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;
import edu.uci.ics.sdcl.firefly.util.LeastCommonDenominator;
import edu.uci.ics.sdcl.firefly.util.RandomKeyGenerator;


/**
 * Packages list of microtasks that will be performed in a sequence by a worker.
 * Microtasks within the same session must be from different code snippets.
 *  
 * @author Christian Adriano
 *
 */
public class WorkerSessionFactory {

	/** It is used to uniquely identify each new WorkerSession. It is randomly generated.*/
	private String sessionId;

	/** Just counts the number of sessions. It is use as part of the sessionID generation */
	private RandomKeyGenerator keyGenerator;

	/** The map of all microtasks that will be used to generate WorkerSession objects */
	private HashMap<String,HashMap<String,ArrayList<Microtask>>> fileMethodMap;

	/** Storage of WorkerSessions */
	private WorkerSessionStorage sessionStorage;

	/** Storage for Microtasks */
	private MicrotaskStorage microtaskStorage;

	/** The worker session datastructure that will be generated */
	HashMap<Integer, WorkerSession> workerSessionMap;

	/** 
	 * @param microtaskPerSession the number of microtasks that will be included in each session
	 */	
	private Integer microtaskPerSession;

	public WorkerSessionFactory(int microtaskPerSession){
		this.microtaskStorage = new MicrotaskStorage();
		this.sessionStorage = new WorkerSessionStorage();
		this.workerSessionMap = new HashMap<Integer, WorkerSession>();
		this.microtaskPerSession = microtaskPerSession;
		this.buildMethodMap();
		this.fillUpFileMethodMap();
		this.keyGenerator = new RandomKeyGenerator();
	}

	/** 
	 * @return the list of worker sessions
	 */
	public Stack<WorkerSession> generateSessions(){

		//Final stack that will be persisted
		Stack<WorkerSession> workerSessionStack = new Stack<WorkerSession>();

		//Auxiliary lists
		ArrayList<WorkerSession> originalList = new ArrayList<WorkerSession>(); 

		//Obtain a list of N microtasks from N different codesnippets (i.e., methods)
		ArrayList<Microtask> mtaskList = this.nextMicrotaskList(this.microtaskPerSession);
		//Generate the original WorkerSessions
		while(mtaskList.size()>0){
			this.sessionId = this.keyGenerator.generate();
			WorkerSession session = new WorkerSession(this.sessionId, mtaskList);
			originalList.add(session);
			mtaskList = this.nextMicrotaskList(this.microtaskPerSession);
		}

		//Store sessions in the final Stack
		for(int i=originalList.size()-1;i>=0;i--){
			//Traverses the lists from last to first to preserve an ascending order in the stack
			workerSessionStack.push(originalList.get(i));
		}
		return workerSessionStack;
	}

	/** 
	 * @param stack the original WorkerSession 
	 * @param copies the number of times the same session should be created
	 * @return the stack with duplicated sessions
	 * 
	 */
	public Stack<WorkerSession> duplicateSessions(Stack<WorkerSession> originalStack, int copies){

		Stack<WorkerSession> duplicateStack = new Stack<WorkerSession>();

		// produce # copies of the original sessions
		for(int j=0;j<copies;j++){
			//Generate the duplicated WorkerSessions
			for(int i=0;i<originalStack.size();i++){
				WorkerSession originalSession = originalStack.elementAt(i);
				if(originalSession.getId() == null)
					System.out.println("ERROR originalSession is NULL for i="+i+ " sessionID= "+this.sessionId);
				this.sessionId = this.keyGenerator.generate();
				WorkerSession duplicateSession =  new WorkerSession(this.sessionId, originalSession.getMicrotaskList());
				//				System.out.println("WSF @99: " + originalSession.getId());

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
	public ArrayList<Microtask> nextMicrotaskList(int numberOfTasks){

		ArrayList<Microtask> resultList = new ArrayList<Microtask>();
		HashMap<String,Microtask> methodTracker = new HashMap<String,Microtask>();// Tracks whether a method with the same signature was not already added
		
		Iterator<String> fileKeyIter = this.fileMethodMap.keySet().iterator();
		boolean traversedTwice = false; //Controls that the fileKeyIter be traversed only twice	
		
		while(methodTracker.size()<numberOfTasks && fileKeyIter.hasNext()){
			String fileKey = (String) fileKeyIter.next();
			HashMap<String, ArrayList<Microtask>> methodMap = fileMethodMap.get(fileKey);

			if(methodMap!=null && !methodMap.isEmpty()){
				Iterator<String> methodKeyIter = methodMap.keySet().iterator();
				while(methodTracker.size()<numberOfTasks && methodKeyIter.hasNext()){
					String methodKey = (String) methodKeyIter.next();
					ArrayList<Microtask> microtaskList = methodMap.get(methodKey);
					if(microtaskList!=null && !microtaskList.isEmpty()){
						int randomPosition = microtaskList.size()/2;
						Microtask microtask = microtaskList.get(randomPosition);
						String methodName = microtask.getCodeSnippet().getMethodSignature().getName();
						
						//Avoids methods with the same name in the same session.
						//That avoids the risk of having buggy and fixed versions of the same method in the same session.
						if (!methodTracker.containsKey(methodName)){ 
							methodTracker.put(methodName, microtask);
							resultList.add(microtask);
							microtaskList.remove(randomPosition);
							//Put the map back with the element removed
							methodMap.put(methodKey, microtaskList);
							fileMethodMap.put(fileKey, methodMap);
						}
						else{
							System.out.println("Successfully dealt with method name collision, method:  "+ methodName);
						}
					}
				}
			}
			
			if(methodTracker.size()<numberOfTasks && !fileKeyIter.hasNext() && !traversedTwice){
				//Means that traversing once we could not fill-up the session with enought microtasks. 
				//Therefore, traverse the list of files>methods>microtasks>methods once more
				traversedTwice=true;
				
				fileKeyIter = this.fileMethodMap.keySet().iterator();
			}
		}
		
		System.out.println("number of microtasks in session: "+ methodTracker.size());

		return resultList;
	}

	/** Build a datastructure to server as basis to generate the WorkerSession objects, because
	 * each WorkerSession can have only one microtask  for the same method.
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
				HashMap<Integer, Microtask> microtaskMap = map.getMicrotaskMap();
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
	 * Obtains the sizes of all microtask lists for each method
	 * @return the size of the largest microtask list
	 */
	private long obtainLargestSize (){
		long largest = 0;
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
	
	/**
	 * 
	 * The size is least common denominator (LCD) among list sizes and the number of microtasks per session 
	 * @param methodMap
	 * @return the required minimum size for all microtaks lists. 
	 */

	private long computeLowestCommonDenominator(ArrayList<Integer> numberList){
		return LeastCommonDenominator.lcm(numberList.toArray());
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
		long largestSize = this.obtainLargestSize();
		long targetSize = LeastCommonDenominator.lcm(this.microtaskPerSession.longValue(), largestSize);
		
		Iterator <String> fileNameIterator = this.fileMethodMap.keySet().iterator();
		//Discover the largest microtask list
		while(fileNameIterator.hasNext()){
			String fileName = fileNameIterator.next();
			HashMap<String,ArrayList<Microtask>> methodMap = this.fileMethodMap.get(fileName);
			Iterator <String> methodIterator = methodMap.keySet().iterator();
			while(methodIterator.hasNext()){
				String methodName = methodIterator.next();
				ArrayList<Microtask> microtaskList = methodMap.get(methodName);
				System.out.println("original microtaskList.size = "+ microtaskList.size());
				if(microtaskList.size()>0){
					methodMap.put(methodName, this.fillUpList(targetSize, microtaskList));
					this.fileMethodMap.put(fileName, methodMap);
				}
				System.out.println("NEW microtaskList.size = "+ methodMap.get(methodName).size());
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
		ArrayList<Microtask> mtaskList =selector.selectAllMicrotasks();

		WorkerSession session = new WorkerSession(this.sessionId, mtaskList);
		workerSessionStack.push(session);

		return workerSessionStack;
	}

	
}
