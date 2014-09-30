package edu.uci.ics.sdcl.firefly.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

/** 
 *  Writes, updates, and reads WorkerSession objects
 *  
 *  WorkerSessions are held in the following datastructure
 *  HashMap<Integer, WorkerSession> workerSessionMap;
 *  
 *  Where: 
 *  Integer = session id; 
 *  ArrayList is the list of microtasks
 * 
 * WorkerSessions are organized in three groups: "New", "Active", "Closed"
 * Each group has a workerSesionMap.
 * 
 * New WorkerSessions are stored  
 * 
 * @author Christian Adriano
 *
 */
public class WorkerSessionStorage {

	private static ObjectInputStream objInputStream;

	public static final String NEW = "NEW"; //Key for the original Stack<WorkerSession>
	public static final String ACTIVE = "ACTIVE"; //Key for a Map<Integer, WorkerSession> 
	public static final String CLOSED = "CLOSED"; //Key for an ArrayList<WorkerSession>

	private static boolean semaphore=false; //avoid trying to open a file while other is still reading.

	private static String persistentFileName; 

	private static String fileNameNew = "workersession_new.ser";
	private static String fileNameClosed = "workersession_closed.ser";


	private static HashMap<String, WorkerSession> activeMap;

	private static String path;

	private static WorkerSessionStorage storage=null;

	private WorkerSessionStorage(){
		PropertyManager manager = new PropertyManager();
		this.path = manager.serializationPath;

		try{

			File file; 
			ObjectOutputStream objOutputStream; 
			HashMap<String,Object> storage;

			//Initialize the NEW sessions FILE
			persistentFileName = path + fileNameNew;
			file = new File(persistentFileName);
			if(!file.exists() ||  file.isDirectory()){
				// No files has been created yet. 

				storage= initializeEmptyStorage();

				objOutputStream = new ObjectOutputStream( 
						new FileOutputStream(new File(persistentFileName)));

				objOutputStream.writeObject( storage );
				objOutputStream.close();
			}
			//Initialize the CLOSED sessions FILE
			persistentFileName = path + fileNameClosed;

			file = new File(persistentFileName);
			if(!file.exists() ||  file.isDirectory()){
				// No files has been created yet. 

				storage= initializeEmptyStorage();

				objOutputStream = new ObjectOutputStream( 
						new FileOutputStream(new File(persistentFileName)));

				objOutputStream.writeObject( storage );
				objOutputStream.close();

			}
			//Initialize the ACTIVE sessions MAP
			activeMap = new   HashMap<String, WorkerSession>();

		}
		catch(IOException exception){
			exception.printStackTrace();
		}
		catch(Exception exception){
			exception.printStackTrace();
		}
	}

	public static WorkerSessionStorage initializeSingleton(){
		if(storage==null)
			storage = new WorkerSessionStorage();
		return storage;
	}

	/** Delete all data from the Storage */
	public static void cleanUp(){


		File file; 
		ObjectOutputStream objOutputStream; 
		HashMap<String,Object> storage;

		try{
			//Initialize the NEW sessions FILE
			file = new File(path + fileNameNew);
			if(!file.exists() ||  file.isDirectory()){
				
				// No files has been created yet. 
				storage= initializeEmptyStorage();

				objOutputStream = new ObjectOutputStream( 
						new FileOutputStream(file));

				objOutputStream.writeObject( storage );
				objOutputStream.close();
			}

			//Initialize the CLOSED sessions FILE

			file = new File(path + fileNameClosed);
			if(!file.exists() ||  file.isDirectory()){

				// No files has been created yet. 
				storage= initializeEmptyStorage();

				objOutputStream = new ObjectOutputStream( 
						new FileOutputStream(file));

				objOutputStream.writeObject( storage );
				objOutputStream.close();
			}
		}
		catch(IOException exception){
			exception.printStackTrace();
		}
		catch(Exception exception){
			exception.printStackTrace();
		}
	}

	private static HashMap<String,Object> initializeEmptyStorage(){
		// Create a empty storage for ALL three datastructures (NEW, NEW_COPIES, ACTIVE, CLOSED)
		HashMap<String,Object> storage = new HashMap<String,Object>();
		Stack<WorkerSession> stack = new Stack<WorkerSession>();
		HashMap<Integer, WorkerSession> map = new HashMap<Integer,WorkerSession>();
		ArrayList<WorkerSession> list = new ArrayList<WorkerSession>();
		storage.put(NEW,stack);
		storage.put(CLOSED, list);

		activeMap = new  HashMap<String, WorkerSession>();

		return storage;
	}



	//-----------------------------------------------------------------------------------------------------------
	// Manage the Storage of New WorkerSession 

	/**
	 * 
	 * @param newStack to be persisted (appends to the existing one)
	 * @param type specify if this is a stack of copies or a stack of original, because they are stored separately
	 * @return true is operation was successful, otherwise, false.
	 */
	public boolean appendNewWorkerSessionStack(Stack<WorkerSession> newStack){

		try{
			File file = new File(persistentFileName); 
			objInputStream = new ObjectInputStream( 
					new FileInputStream(file));
			HashMap<String,Object> storage = (HashMap<String, Object>) objInputStream.readObject();
			objInputStream.close();

			Stack<WorkerSession>  stack = (Stack<WorkerSession>)storage.get(type);
			stack.addAll(newStack);
			//storage.put(stack);	

			ObjectOutputStream objOutputStream = new ObjectOutputStream( 
					new FileOutputStream(new File(persistentFileName)));
			objOutputStream.writeObject( storage );
			objOutputStream.close();
			return true;
		}
		catch(IOException exception){
			exception.printStackTrace();
			return false;
		}
		catch(Exception exception){
			exception.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @param newStack to be persisted (overwrites the existing one)
	 * @param type specify if this is a stack of copies or a stack of original, because they are stored separately
	 * @return true is operation was successful, otherwise, false.
	 */
	private boolean overwriteNewWorkerSessionStack(Stack<WorkerSession> newStack, String type){
		try{
			if(!type.equals(NEW))
				return false; //Wrong type was provided
			else{	
				HashMap<String,Object> storage = readStorage();
				storage.put(type,newStack);

				ObjectOutputStream objOutputStream = new ObjectOutputStream( 
						new FileOutputStream(new File(persistentFileName)));
				objOutputStream.writeObject( storage );
				objOutputStream.close();
				return true;
			}
		}
		catch(IOException exception){
			exception.printStackTrace();
			return false;
		}
		catch(Exception exception){
			exception.printStackTrace();
			return false;
		}
	}

	/**
	 * Obtains a WorkerSession from the stack of new WorkerSessions or the copies stack. Prioritizes the 
	 * the stack with new original WorkerSessions. Only when this last one is empty, then reads from the 
	 * copies stack. After that the method moves the retrieved WorkerSession to the list of active ones.
	 * 
	 * @return a workerSession from the stack new WorkerSessions. If the stack is empty returns null.
	 */
	public WorkerSession readNewWorkerSession(){

		String type;
		if(this.getNumberOfNewWorkerSessions(NEW)>0)
			type = NEW;
		else
			return null; //There aren't any NEW WorkerSessions Available

		Stack<WorkerSession> stack = this.retrieveWorkerSessionStack(NEW);
		if(stack.isEmpty())
			return null;
		else{
			WorkerSession session = stack.pop(); 
			if(this.overwriteNewWorkerSessionStack(stack,type)){ //Save the updated stack
				if(this.updateActiveWorkerSession(session))
					return session;
				else
					return null;
			}
			else return null;
		}
	}

	/**
	 * @param type NEW or NEW_COPIES
	 * @return the size of the stack of new WorkerSessions
	 */
	public int getNumberOfNewWorkerSessions(String type){
		Stack<WorkerSession> stack = this.retrieveWorkerSessionStack(type);
		if(stack!=null && !stack.isEmpty())
			return stack.size();
		else
			return 0;
	}

	/**
	 * 
	 * @return the size of the stack of new WorkerSessions
	 */
	public int getNumberOfNewWorkerSessions(){
		Stack<WorkerSession> stackNew = this.retrieveWorkerSessionStack(this.NEW);
		int total = 0;
		if(stackNew!=null && !stackNew.isEmpty())
			total = stackNew.size();
		return total;
	}


	/**
	 * 
	 * @return an existing stack of WorkerSessions, null in case it is not possible to create a stack in the file.
	 */
	private Stack<WorkerSession> retrieveWorkerSessionStack(String type){

		HashMap<String,Object> storage = readStorage();

		if(!type.equals(NEW))
			return null; //Error, the stack should always be there, even when the stack is empty.
		else
			return (Stack<WorkerSession>)storage.get(type);
	}


	//-----------------------------------------------------------------------------------------------------------
	// Manage the Storage of Active WorkerSessions

	/** Stores a map of WorkerSession. 
	 * @param map is in indexed by WorkerSession ID
	 * @return true if operation succeeded, otherwise false.
	 */
	public boolean updateActiveWorkerSession(WorkerSession session){
		HashMap<String,WorkerSession> workerSessionMap =this.retrieveActiveWorkerSessionMap();
		if(workerSessionMap!=null){
			if(!session.hasCurrent()){ //it means that the Session was completed
				workerSessionMap.remove(session.getId()); //removes from active map
				overwriteActiveWorkerSessionMap(workerSessionMap); //overwrites active map 
				if(addClosedWorkerSession(session)) //moves to closed list
					return true;
				else
					return false;
			}
			else{//Session still have uncompleted microtasks
				workerSessionMap.put(session.getId(),session);
				overwriteActiveWorkerSessionMap(workerSessionMap); //overwrites active map
				return true;
			}
		}
		else
			return false;
	}

	/**
	 * @param sessionId the identifier for the session
	 * @param microtaskId the identifier of a microtask in that session
	 * @param answer the answer provided by a user
	 * @return true if operation was successful, otherwise false
	 */
	public boolean setSessionMicrotaskAnswer(String sessionId,Integer microtaskId, Answer answer) {

		WorkerSession session = this.readActiveWorkerSessionByID(sessionId);
		if(session==null){
			System.out.println("Session is null, id= "+sessionId);
			return false;
		}
		else{
			if(session.insertMicrotaskAnswer(microtaskId,answer)){
				//log the operations in a text file
				//writeLog (SessionId, File Name, Method Name, Question, Answer, Time Duration);
				return(this.updateActiveWorkerSession(session));
			}
			else
				return false;
		}
	}

	/** Retrieves a WorkerSession
	 * 
	 * @param userId the unique identifier for a WorkerSession
	 * @return a WorkerSession, in case any was found, return null.
	 */
	public WorkerSession readActiveWorkerSessionByID(String sessionId){

		HashMap<String,WorkerSession> workerSessionMap =this.retrieveActiveWorkerSessionMap();

		if(workerSessionMap!=null && workerSessionMap.containsKey(sessionId))
			return workerSessionMap.get(sessionId);
		else
			return null;
	}


	private HashMap<String, WorkerSession> retrieveActiveWorkerSessionMap(){

		HashMap<String,Object> storage  = readStorage();

		if(storage.containsKey(ACTIVE))
			return (HashMap<String,WorkerSession>) storage.get(ACTIVE);
		else
			return null; //Error, the map should always be there, even when the map is empty.

	}



	/**
	 * 
	 * @param the index of WorkerSession objects stored
	 * @return true if operation succeeded, otherwise false.
	 */
	private boolean overwriteActiveWorkerSessionMap(HashMap<String,WorkerSession> newWorkerSessionMap){
		try{

			HashMap<String,Object> storage = readStorage();

			storage.put(ACTIVE, newWorkerSessionMap);

			ObjectOutputStream objOutputStream = new ObjectOutputStream( 
					new FileOutputStream(new File(persistentFileName)));

			objOutputStream.writeObject( storage );
			objOutputStream.close();
			return true;
		}
		catch(IOException exception){
			exception.printStackTrace();
			return false;
		}
		catch(Exception exception){
			exception.printStackTrace();
			return false;
		}
	}


	//----------------------------------------------------------------------------------------------------------
	//Manage Closed Sessions


	public boolean addClosedWorkerSession(WorkerSession session){
		ArrayList<WorkerSession> closedSessionList = readClosedWorkerSessionList();
		if(closedSessionList!=null){
			closedSessionList.add(session);
			if(this.overwriteClosedWorkerSessionList(closedSessionList))
				return true;
			else
				return false;
		}
		else
			return false;
	}


	/**
	 * 
	 * @return closedSessionList the list of closed WorkerSession
	 */
	public ArrayList<WorkerSession> readClosedWorkerSessionList(){

		HashMap<String,Object> storage = readStorage();

		return (ArrayList<WorkerSession>) storage.get(CLOSED);
	}

	/**
	 * 
	 * @param closedSessionList the list of closed WorkerSession (it will overwrite the existing one)
	 * @return true if operation succeeded, otherwise false.
	 */
	private boolean overwriteClosedWorkerSessionList(ArrayList<WorkerSession> closedSessionList){
		try{
			File file = new File(persistentFileName); 

			ObjectInputStream objInputStream = new ObjectInputStream( 
					new FileInputStream(file));

			HashMap<String,Object> storage = readStorage();

			storage.put(CLOSED, closedSessionList);

			ObjectOutputStream objOutputStream = new ObjectOutputStream( 
					new FileOutputStream(new File(persistentFileName)));

			objOutputStream.writeObject( storage );
			objOutputStream.close();
			return true;
		}
		catch(IOException exception){
			exception.printStackTrace();
			return false;
		}
		catch(Exception exception){
			exception.printStackTrace();
			return false;
		}
	}



	public static HashMap<String, Object> readStorage(){
		try{

			if(semaphore)
				System.out.println("Reached UP semaphore: "+ semaphore);
			else
				System.out.println("Reached DOWN Semaphore : "+ semaphore);
			while(semaphore)  pause();
			semaphore=true;
			System.out.println("Semaphore dropped, finally: "+ semaphore );
			System.out.println("Raised semaphore, my turn!: "+ semaphore);

			File file = new File(persistentFileName); 

			objInputStream = new ObjectInputStream( 
					new FileInputStream(file));

			HashMap<String, Object> storage  = (HashMap<String, Object>) objInputStream.readObject();
			objInputStream.close();
			semaphore=false;

			System.out.println("Done! semaphore released: "+ semaphore);
			return storage;
		}
		catch(IOException exception){
			exception.printStackTrace();
			semaphore=false;
			return null;
		}
		catch(Exception exception){
			exception.printStackTrace();
			semaphore=false;
			return null;
		}
	}

	public static void pause(){

		//do something   
		long timeToSleep = 1000;
		long start, end, slept;
		boolean interrupted=false;

		if(timeToSleep > 0){
			start=System.currentTimeMillis();
			try{
				Thread.sleep(timeToSleep);
			}
			catch(InterruptedException e){

				//work out how much more time to sleep for
				end=System.currentTimeMillis();
				slept=end-start;
				timeToSleep-=slept;
				interrupted=true;
			}
		}

		if(interrupted){
			//restore interruption before exit
			Thread.currentThread().interrupt();
		}
	}

}

