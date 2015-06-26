package edu.uci.ics.sdcl.firefly.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

/** 
 *  Writes, updates, and reads WorkerSession objects
 * 
 * WorkerSessions are organized in three groups: "New", "Active", "Closed"
 * 
 * @author Christian Adriano
 *
 */
public class WorkerSessionStorage {

	private static String persistentFileNameNew;
	private static String persistentFileNameClosed;

	private static String fileNameNew = "sessions_new.ser";
	private static String fileNameClosed = "sessions_closed.ser";

	/** Indexed by Session ID */
	private  Hashtable<String, WorkerSession> activeSessionTable;
	
	private String path;

	private static WorkerSessionStorage storage=null;
	private static Logger logger;

	public static synchronized WorkerSessionStorage initializeSingleton(){
		if(storage==null)
			storage = new WorkerSessionStorage();
		return storage;
	}

	private WorkerSessionStorage(){
		try{	
			PropertyManager manager = PropertyManager.initializeSingleton();
			this.path = manager.serializationPath;
			logger = LoggerFactory.getLogger("session");

			persistentFileNameNew = path + fileNameNew;
			File fileNewSession = new File(persistentFileNameNew);

			persistentFileNameClosed = path + fileNameClosed;
			File fileClosedSession = new File(persistentFileNameClosed);

			//If any of the files does not exist, them create them from scratch
			if(!fileNewSession.exists() ||  fileNewSession.isDirectory() || 
					!fileClosedSession.exists() ||  fileClosedSession.isDirectory() ){
				// No files has been created yet. 
				this.cleanUp();
			}
			else//No clean-up needed, just initialize the temporary datastructure.
				this.activeSessionTable=new Hashtable<String,WorkerSession>();
		}
		catch(Exception exception){
			exception.printStackTrace();
		}
	}



	/** Create new storages */
	public synchronized void cleanUp(){

		File file; 
		ObjectOutputStream objOutputStream; 

		try{
			//Initialize the NEW sessions FILE
			file = new File(persistentFileNameNew);

			/* Indexed by File name and the stack of sessions for each file */ 
			Hashtable<String, Stack<WorkerSession>> mapStack = new Hashtable<String, Stack<WorkerSession>>();

			objOutputStream = new ObjectOutputStream( 
					new FileOutputStream(file));

			objOutputStream.writeObject( mapStack );
			objOutputStream.close();

			//Initialize the CLOSED sessions FILE
			file = new File(persistentFileNameClosed);

			Vector<WorkerSession> list = new Vector<WorkerSession>();

			objOutputStream = new ObjectOutputStream( 
					new FileOutputStream(file));

			objOutputStream.writeObject( list );
			objOutputStream.close();

			//Initialize the ACTIVE sessions TABLE --- KEPT IN MEMORY
			activeSessionTable = new    Hashtable<String, WorkerSession>();

		}
		catch(IOException exception){
			exception.printStackTrace();
		}
		catch(Exception exception){
			exception.printStackTrace();
		}
	}



	//-----------------------------------------------------------------------------------------------------------
	// Manage the Storage of New WorkerSession 

	/**
	 * 
	 * @param newStack to be persisted (overwrites the existing one)
	 * @param type specify if this is a stack of copies or a stack of original, because they are stored separately
	 * @return true is operation was successful, otherwise, false.
	 */
	public synchronized boolean writeNewWorkerSessionMap(LinkedHashMap<String, Stack<WorkerSession>> mapStack){
		try{
			if(mapStack==null){
				logger.error("EVENT%ERROR% Avoided trying to write nullpointer in Worker repository.");
				return false;
			}
			else{
				ObjectOutputStream objOutputStream = new ObjectOutputStream( 
						new FileOutputStream(new File(persistentFileNameNew)));
				objOutputStream.writeObject( mapStack );
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
	 * @param fileName the file name from which the session is to be retrieved
	 * @return a workerSession from the stack new WorkerSessions. If the stack is empty returns null.
	 */
	public synchronized WorkerSession readNewWorkerSession(String fileName){

		LinkedHashMap<String, Stack<WorkerSession>> mapStack = retrieveNewSessionStorage();
		if(mapStack==null || mapStack.isEmpty())
			return null;
		else{
			Stack<WorkerSession> stack = mapStack.get(fileName);
			if(stack==null || stack.isEmpty())
				return null;
			else{
				WorkerSession session = stack.pop();
				mapStack.put(fileName, stack);
				if(writeNewWorkerSessionMap(mapStack)) //Save the updated stack
					return session;
				else
					return null;
			}
		}
	}

	
	private int countSessionsInMap(LinkedHashMap<String,Stack<WorkerSession>> sessionsMap){
		int counter=0;
		Iterator<String> iter = sessionsMap.keySet().iterator();
		while(iter.hasNext()){
			String fileName = iter.next();
			Stack<WorkerSession> sessionStack = sessionsMap.get(fileName);
			counter = counter + sessionStack.size();
		}
		return counter;
	}
	
	/**
	 * @return the size of the stack of new WorkerSessions
	 */
	public synchronized int getNumberOfNewWorkerSessions(){
		LinkedHashMap<String, Stack<WorkerSession>> sessionsMap = retrieveNewSessionStorage();
		return countSessionsInMap(sessionsMap);
	}


	//-----------------------------------------------------------------------------------------------------------
	// Manage the Storage of Active WorkerSessions

	/** 
	 * @return true if operation succeeded, otherwise false.
	 */
	public synchronized boolean updateActiveWorkerSession(WorkerSession session){

		if(activeSessionTable !=null){
			if(session.isClosed()){ //it means that the Session was completed
				activeSessionTable.remove(session.getId()); //removes from active map
				return (addClosedWorkerSession(session)); //moves to closed list
			}
			else{//Session still have uncompleted microtasks				
				activeSessionTable.put(session.getId(), session);
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
	public synchronized WorkerSession setSessionMicrotaskAnswer(String sessionId,Integer microtaskId, Answer answer) {

		WorkerSession session = this.readActiveWorkerSessionByID(sessionId);
		if(session==null){
			logger.error("EVENT= ERROR; Session is null id "+sessionId);
			return null;
		}
		else{
			if(session.insertMicrotaskAnswer(microtaskId,answer)){
				logger.info("EVENT%SESSION% workerId%"+ answer.getWorkerId()+"% sessionId%"+ sessionId+"% microtaskId%"+microtaskId+
						"% answer%"+answer.getOption()+"% Confidence Level%"+answer.getConfidenceOption());
				if(this.updateActiveWorkerSession(session))
					return session;
				else 
					return null;
			}
			else
				return null;
		}
	}

	/** Retrieves a WorkerSession
	 * 
	 * @param userId the unique identifier for a WorkerSession
	 * @return a WorkerSession, in case any was found, return null.
	 */
	public synchronized WorkerSession readActiveWorkerSessionByID(String sessionId){

		if(activeSessionTable!=null && activeSessionTable.containsKey(sessionId)){
			return activeSessionTable.get(sessionId);
		}
		else
			return null;
	}



	//----------------------------------------------------------------------------------------------------------
	//Manage Closed Sessions


	public synchronized boolean addClosedWorkerSession(WorkerSession session){
		Vector<WorkerSession> closedSessionList = retrieveClosedSessionStorage();
		if(closedSessionList!=null){
			closedSessionList.add(session.getLightVersion());
			logger.info("EVENT%CLOSING SESSION% workerId%"+session.getWorkerId()+"% sessionId%"+ session.getId());
			if(this.overwriteClosedWorkerSessionList(closedSessionList))
				return true;
			else
				return false;
		}
		else{
			logger.error("Closed Session List is null");
			return false;
		}
	}


	/**
	 * 
	 * @param closedSessionList the list of closed WorkerSession (it will overwrite the existing one)
	 * @return true if operation succeeded, otherwise false.
	 */
	private synchronized boolean overwriteClosedWorkerSessionList(Vector<WorkerSession> closedSessionList){
		try{
			if(closedSessionList==null){
				logger.error("EVENT%ERROR% Avoided trying to write nullpointer in Worker repository.");
				return false;
			}
			else{
				ObjectOutputStream objOutputStream = new ObjectOutputStream( 
						new FileOutputStream(new File(persistentFileNameClosed)));

				objOutputStream.writeObject( closedSessionList );
				objOutputStream.close();
				return true;
			}
		}
		catch(IOException exception){
			logger.error(exception.toString());
			return false;
		}
		catch(Exception exception){
			logger.error(exception.toString());
			return false;
		}
	}

	//------------------------------------------------------------------------------------------------------------------------
	// Retrieve the storages

	public synchronized LinkedHashMap<String, Stack<WorkerSession>> retrieveNewSessionStorage(){
		try{

			File file = new File(persistentFileNameNew); 

			ObjectInputStream objInputStream = new ObjectInputStream( 
					new FileInputStream(file));

			LinkedHashMap<String,Stack<WorkerSession>> map  = (LinkedHashMap<String,Stack<WorkerSession>> ) objInputStream.readObject();
			objInputStream.close();

			return map;
		}
		catch(IOException exception){
			logger.error(exception.toString());
			return null;
		}
		catch(Exception exception){
			logger.error(exception.toString());
			return null;
		}
	}


	public synchronized Vector<WorkerSession> retrieveClosedSessionStorage(){
		try{

			File file = new File(persistentFileNameClosed); 

			ObjectInputStream objInputStream = new ObjectInputStream( 
					new FileInputStream(file));

			Vector<WorkerSession> list = (Vector<WorkerSession>) objInputStream.readObject();
			objInputStream.close();

			return list;
		}
		catch(IOException exception){
			exception.printStackTrace();
			return null;
		}
		catch(Exception exception){
			exception.printStackTrace();
			return null;
		}
	}

	public synchronized Hashtable<String, WorkerSession> retrieveActiveSessionStorage() {
		return this.activeSessionTable;
	}

}

