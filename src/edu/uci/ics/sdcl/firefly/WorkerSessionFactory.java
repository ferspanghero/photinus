package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import test.RoundRobinTest.Session;
import edu.uci.ics.sdcl.firefly.controller.MicrotaskSelector;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;
import edu.uci.ics.sdcl.firefly.util.PathUtil;
import edu.uci.ics.sdcl.firefly.util.RandomKeyGenerator;


/**
 * Generates all sessions for all available bugs (uploaded files)
 * Each session is dedicated to one bug. Each session has multiple questions related to one bug. 
 * @author Christian Adriano
 *
 */
public class WorkerSessionFactory{

	/** It is used to uniquely identify each new WorkerSession. It is randomly generated.*/
	private String sessionId;

	/** Just counts the number of sessions. It is use as part of the sessionID generation */
	private RandomKeyGenerator keyGenerator;

	/** The map of all microtasks that will be used to generate WorkerSession objects */
	private LinkedHashMap<String,Hashtable<String,Vector<Microtask>>> fileMethodMap;

	/** Storage for Microtasks */
	private MicrotaskStorage microtaskStorage;

	/** The worker session datastructure that will be generated */
	LinkedHashMap<String, Stack<WorkerSession>> workerSessionMap;

	/** 
	 * @param microtaskPerSession the number of microtasks that will be included in each session
	 */	
	private Integer microtaskPerSession;

	public WorkerSessionFactory(int microtaskPerSession){
		this.microtaskStorage =  MicrotaskStorage.initializeSingleton();
		WorkerSessionStorage.initializeSingleton();
		this.workerSessionMap = new LinkedHashMap<String, Stack<WorkerSession>>();
		this.microtaskPerSession = microtaskPerSession;
		this.buildMethodMap();
		//this.fillUpFileMethodMap();
		this.keyGenerator = new RandomKeyGenerator();
	}

	
	/** 
	 * @param copies the number of desired copies
	 * @return the list of worker sessions
	 */
	public LinkedHashMap<String, Stack<WorkerSession>> generateSessions(int answersPerMicrotask){

		//For each pair (file, method) generate all sessions
		Iterator<String> fileNameIter = this.fileMethodMap.keySet().iterator();
		while(fileNameIter.hasNext()){
			String fileName = fileNameIter.next();
			Hashtable<String, Vector<Microtask>> microtaskMap= this.fileMethodMap.get(fileName);
			Iterator<String> methodNameIter = microtaskMap.keySet().iterator();
			String methodName = methodNameIter.next();
			Vector<Microtask> microtaskList = microtaskMap.get(methodName);
			Stack<WorkerSession> fileWorkerSessionStack = this.composeSessions(microtaskList,fileName);

			//duplicate sessions
			if(answersPerMicrotask>1){
				fileWorkerSessionStack = this.duplicateSessions(fileWorkerSessionStack, answersPerMicrotask, fileName);
			}
			// System.out.println("Number of sessions available: "+fileWorkerSessionStack.size()+ ", answers per microtask:"+answersPerMicrotask );
			this.workerSessionMap.put(fileName, fileWorkerSessionStack);	
		}

		return workerSessionMap;
	}
	
	
	/** 
	 * @param stack the original WorkerSession 
	 * @param copies the number of times the same session should be created
	 * @return the stack with duplicated sessions
	 * 
	 */
	private Stack<WorkerSession> duplicateSessions(Stack<WorkerSession> originalStack, int copies, String fileName){

		Stack<WorkerSession> duplicateStack = new Stack<WorkerSession>();
		
		System.out.println("filename:" + fileName + "copies requested: "+copies);
		// produce # copies of the original sessions
		for(int j=0;j<copies;j++){
			//Generate the duplicated WorkerSessions
			for(int i=0;i<originalStack.size();i++){
				WorkerSession originalSession = originalStack.elementAt(i);
				String sessionId = this.keyGenerator.generate();
				//System.out.println("duplicated sessionId ="+sessionId);
				WorkerSession duplicateSession =  new WorkerSession(sessionId, fileName, originalSession.getMicrotaskList());
				duplicateStack.push(duplicateSession);
			}
		}
		System.out.println("---File name: "+ fileName+", after duplication, total sessions: "+duplicateStack.size());
		return duplicateStack;	
	}
	
	/** for testing purposes */
	public LinkedHashMap<String, Hashtable<String, Vector<Microtask>>> getFileMethodMap() {
		return this.fileMethodMap;
	}

	//--------------------------------------------------------------------------------------------------------------
	/* NEW SESSION GENERATION FOR MULTIPLE QUESTIONS FOR SAME BUG PER WORKER */
	
	
	/**
	 * Stack of available positions in the list.
	 */
	private Stack<Integer> initializeIndexStack(Vector<Microtask> list){
		Stack<Integer> availablePositionsStack = new Stack<Integer>();
		for(int j=list.size()-1;j>=0;j--){
			Integer position = new Integer(j);
			availablePositionsStack.push(position);
		}
		return availablePositionsStack;
	}

	
	private ArrayList<Boolean> initializeAvailabilityList(int size){
		ArrayList<Boolean> availabilityList= new ArrayList<Boolean>();
		for (int i=0;i<size;i++){
			availabilityList.add(new Boolean(true));
		}
		return availabilityList;
	}
	
	
	/** Calculates the minimal number of sessions per file in order to cover all questions
	 * at least once.
	 * @param numberOfQuestionsPerFile
	 * @param numberOfMicrotaksPerSession
	 * @return
	 */
	public int minimalSessionsPerFile(int numberOfQuestionsPerFile, int numberOfMicrotaksPerSession){
		double sessionsPerFileDouble = numberOfQuestionsPerFile/numberOfMicrotaksPerSession;
		int sessionsPerFileInt = (int) sessionsPerFileDouble; 
		double remainderDouble = Math.IEEEremainder(numberOfQuestionsPerFile,numberOfMicrotaksPerSession);
		int remainderInt = new Double(remainderDouble).intValue();
		sessionsPerFileInt = sessionsPerFileInt+Math.abs(remainderInt);
		return sessionsPerFileInt;
	}
	
	/** This is a Round-Robin-like algorithm that compose different sessions in order
	 * that all microtasks are present in at least one session. 
	 * 
	 * @param list
	 * @return stack of sessions
	 */
	private Stack<WorkerSession> composeSessions(Vector<Microtask> list, String fileName){

		Stack<WorkerSession> sessionStack = new Stack<WorkerSession>();
		
		Stack<Integer> availablePositionsStack = this.initializeIndexStack(list);
		ArrayList<Boolean> availabilityList = this.initializeAvailabilityList(list.size());
		
		int sessionsPerFile = this.minimalSessionsPerFile(list.size(), this.microtaskPerSession);
		System.out.println("FileName: "+ fileName+", Microtasks: "+list.size());
		
		HashMap<Integer, Integer> mtaskMap = new HashMap<Integer, Integer>(); 
		
		for(int i=0;i<sessionsPerFile;i++)//Compose all different sessions for one file 

			if(!availablePositionsStack.isEmpty()){	
				int pos=availablePositionsStack.pop().intValue(); //get the top one
						
				Vector<Microtask> sessionMicrotaskList = new Vector<Microtask>(); 
				for(int j=0;  j<microtaskPerSession; j++){ //Compose one session with N microtasks			
					int	next = pos;
					availabilityList.set(next,new Boolean(false));//Make the microtask not available anymore
					Microtask microtask = list.get(next);
					if(!containsMicrotask(sessionMicrotaskList,microtask)){
						sessionMicrotaskList.add((microtask));
						mtaskMap.put(microtask.getID(),microtask.getID());
						System.out.println("--- task: "+microtask.getID());
					}
					pos = next + sessionsPerFile;
					while( pos<list.size() && availabilityList.get(pos).booleanValue()==false)//find the next available microtask
						pos++;
					if(pos>=list.size())
						pos = pos - list.size();
				}
				this.sessionId = this.keyGenerator.generate();
				WorkerSession session = new WorkerSession(this.sessionId, fileName, sessionMicrotaskList);
				sessionStack.push(session);
			}
		
		System.out.println("---IN SESSIONS expected microtasks: "+list.size()+", actual microtasks:" +mtaskMap.size());
		System.out.println("FileName: "+ fileName+", expectedSessions: "+sessionsPerFile+ ", actualSessions: "+sessionStack.size());
		

		
		return sessionStack;
	}
	
	private boolean containsMicrotask(Vector<Microtask> taskList, Microtask microtask){
		for(Microtask task: taskList){
			if(task.getID().intValue() == microtask.getID().intValue())
				return true;
		}
		return false;
	}
	
	/** I believe I need to traverse the whole list collecting available items. 
	 * I believe it will be easier to have them in a stack an just pop them....
	 * @param pos
	 * @param list
	 * @param availabilityList
	 * @return
	 */
	private int findNextAvailableTask(int pos, Vector<Microtask> list, ArrayList<Boolean> availabilityList){
		
		for(int count=0;count<list.size();count++){
			while( pos<list.size() && availabilityList.get(pos).booleanValue()==false)//find the next available microtask
				pos++;
			if(pos>=list.size())
				pos = pos - list.size();
		}
		
		return pos;
		
	}
	
	//--------------------------------------------------------------------------------------------------------------
	/* OLDER SESSION GENERATOR */ 
		
	

	/** Build a datastructure that indexes filename, methodName and all microtasks for that method name
	 * 
	 * @return A map indexed by filename and method name
	 */
	private void buildMethodMap(){

		//indexed by fileName and method name and List of microtasks
		this.fileMethodMap = new LinkedHashMap<String,Hashtable<String,Vector<Microtask>>> ();


		Set<String> sessionSet= microtaskStorage.retrieveDebuggingSessionNames();
		if((sessionSet==null) || (!sessionSet.iterator().hasNext())){
			//EMPTY!!!
			System.out.println("DebugSession empty!!");
		}
		else{
			Iterator<String> sessionIterator = sessionSet.iterator();
			while(sessionIterator.hasNext()){
				String fileName = (String) sessionIterator.next();
				System.out.println("in WorkerSessionFactory, filename="+fileName);
				FileDebugSession map = this.microtaskStorage.read(fileName);

				if(fileMethodMap.get(fileName)==null){
					fileMethodMap.put(fileName, new Hashtable<String,Vector<Microtask>>());
				}
				//Iterate over microtasks
				Hashtable<Integer, Microtask> microtaskMap = map.getMicrotaskMap();
				Iterator<Integer> microtaskIter = microtaskMap.keySet().iterator();
				while(microtaskIter.hasNext()){
					Integer mtaskID = (Integer) microtaskIter.next();
					Microtask mtask = microtaskMap.get(mtaskID);
					String methodName = mtask.getCodeSnippet().getMethodSignature().getName();

					Hashtable<String,Vector<Microtask>> methodMap = fileMethodMap.get(fileName);
					Vector<Microtask> methodMicrotaskList = methodMap.get(methodName);
		
					if(methodMicrotaskList==null){
						methodMicrotaskList = new Vector<Microtask>();
					}								
					methodMicrotaskList.add(mtask);
					methodMap.put(methodName, methodMicrotaskList);
					fileMethodMap.put(fileName, methodMap);
				}				
			}
		}
	}
	
/*
	
	/**
	 * Obtains the sizes of all microtask lists for each method
	 * @return the size of the largest microtask list
	 
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
	
	*//** Fill up the list of microtasks so all codesnippets have the same number of microtasks.
	 * That is necessary to guarantee that all Sessions have the same number of microtasks.
	 * 
	 * @return
	 *//*
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
*/

	/** Method used to generate all microtask in one single session
	 * This method is used only for testing and experiment setup
	 * 
	 * @return all sessions from all files
	 */
	public LinkedHashMap<String, Stack<WorkerSession>>  generateSingleSession(){

		//Stack for the single Session
		Stack<WorkerSession> workerSessionStack = new Stack<WorkerSession>();
		this.sessionId = this.keyGenerator.generate();

		MicrotaskSelector selector = new MicrotaskSelector();
		Vector<Microtask> mtaskList =selector.selectAllMicrotasks();

		String fileNameSingle = "singleFileName";
		WorkerSession session = new WorkerSession(this.sessionId, fileNameSingle, mtaskList);
		LinkedHashMap<String, Stack<WorkerSession>> mapStack = new LinkedHashMap<String, Stack<WorkerSession>>(); 
		workerSessionStack.push(session);
		mapStack.put(fileNameSingle, workerSessionStack);

		return mapStack;
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
