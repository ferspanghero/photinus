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

	public static final String NEW = "NEW"; //Key for the original Stack<WorkerSession>
	public static final String NEW_COPIES = "NEW_COPIES"; //Key for the Stack of copies
	public static final String ACTIVE = "ACTIVE"; //Key for a Map<Integer, WorkerSession> 
	public static final String CLOSED = "CLOSED"; //Key for an ArrayList<WorkerSession>
	
	private String persistentFileName = "workersession.ser"; 

	public WorkerSessionStorage(){
		try{
			File file = new File(this.persistentFileName);
			if(!file.exists() ||  file.isDirectory()){
				// No files has been created yet. 

				// Create a empty storage for ALL three datastructures (NEW, NEW_COPIES, ACTIVE, CLOSED)
				HashMap<String,Object> storage = new HashMap<String,Object>();
				Stack<WorkerSession> stack = new Stack<WorkerSession>();
				Stack<WorkerSession> copiesStack = new Stack<WorkerSession>();
				HashMap<Integer, WorkerSession> map = new HashMap<Integer,WorkerSession>();
				ArrayList<WorkerSession> list = new ArrayList<WorkerSession>();
				storage.put(NEW,stack);
				storage.put(NEW_COPIES,copiesStack);
				storage.put(ACTIVE, map);
				storage.put(CLOSED, list);

				ObjectOutputStream objOutputStream = new ObjectOutputStream( 
						new FileOutputStream(new File(this.persistentFileName)));

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



	//-----------------------------------------------------------------------------------------------------------
	// Manage the Storage of New WorkerSession 

	/**
	 * 
	 * @param newStack to be persisted (appends to the existing one)
	 * @param type specify if this is a stack of copies or a stack of original, because they are stored separately
	 * @return true is operation was successful, otherwise, false.
	 */
	public boolean appendNewWorkerSessionStack(Stack<WorkerSession> newStack, String type){

		try{
			if(!type.equals(NEW) && !type.equals(NEW_COPIES))
				return false; //Wrong type was provided
			else{	
				File file = new File(this.persistentFileName); 
				ObjectInputStream objInputStream = new ObjectInputStream( 
						new FileInputStream(file));
				HashMap<String,Object> storage = (HashMap<String, Object>) objInputStream.readObject();
				objInputStream.close();

				Stack<WorkerSession>  stack = (Stack<WorkerSession>)storage.get(type);
				stack.addAll(newStack);
				storage.put(type, stack);	

				ObjectOutputStream objOutputStream = new ObjectOutputStream( 
						new FileOutputStream(new File(this.persistentFileName)));
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
	 * 
	 * @param newStack to be persisted (overwrites the existing one)
	 * @param type specify if this is a stack of copies or a stack of original, because they are stored separately
	 * @return true is operation was successful, otherwise, false.
	 */
	private boolean overwriteNewWorkerSessionStack(Stack<WorkerSession> newStack, String type){
	try{
		if(!type.equals(NEW) && !type.equals(NEW_COPIES))
			return false; //Wrong type was provided
		else{	
			File file = new File(this.persistentFileName); 
			ObjectInputStream objInputStream = new ObjectInputStream( 
					new FileInputStream(file));
			HashMap<String,Object> storage = (HashMap<String, Object>) objInputStream.readObject();
			objInputStream.close();

			storage.put(type,newStack);
			
			ObjectOutputStream objOutputStream = new ObjectOutputStream( 
					new FileOutputStream(new File(this.persistentFileName)));
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
			if(this.getNumberOfNewWorkerSessions(NEW_COPIES)>0)
				type = NEW_COPIES;
			else
				return null; //There aren't any NEW WorkerSessions Available

		Stack<WorkerSession> stack = this.retrieveWorkerSessionStack(type);
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
	 * 
	 * @return the size of the stack of new WorkerSessions
	 */
	public Integer getNumberOfNewWorkerSessions(String type){
		Stack<WorkerSession> stack = this.retrieveWorkerSessionStack(type);
		if(stack!=null && !stack.isEmpty())
			return stack.size();
		else
			return 0;
	}

	/**
	 * 
	 * @return an existing stack of WorkerSessions, null in case it is not possible to create a stack in the file.
	 */
	private Stack<WorkerSession> retrieveWorkerSessionStack(String type){
		try{

			File file = new File(this.persistentFileName);

			ObjectInputStream objInputStream = new ObjectInputStream( 
					new FileInputStream(file));

			HashMap<String,Object> storage = (HashMap<String, Object>) objInputStream.readObject();
			objInputStream.close();

			if(!type.equals(NEW) && !type.equals(NEW_COPIES))
				return null; //Error, the stack should always be there, even when the stack is empty.
			else
				return (Stack<WorkerSession>)storage.get(type);

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


	//-----------------------------------------------------------------------------------------------------------
	// Manage the Storage of Active WorkerSessions

	/** Stores a map of WorkerSession. 
	 * @param map is in indexed by WorkerSession ID
	 * @return true if operation succeeded, otherwise false.
	 */
	public boolean updateActiveWorkerSession(WorkerSession session){
		HashMap<Integer,WorkerSession> workerSessionMap =this.retrieveActiveWorkerSessionMap();
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
	public boolean setSessionMicrotaskAnswer(Integer sessionId,Integer microtaskId, Answer answer) {
		
		WorkerSession session = this.readActiveWorkerSessionByID(sessionId);
		if(session==null)
			return false;
		else{
			if(session.insertMicrotaskAnswer(microtaskId,answer))
				return(this.updateActiveWorkerSession(session));
			else
				return false;
		}
	}
	
	/** Retrieves a WorkerSession
	 * 
	 * @param id the unique identifier for a WorkerSession
	 * @return a WorkerSession, in case any was found, return null.
	 */
	public WorkerSession readActiveWorkerSessionByID(Integer id){

		HashMap<Integer,WorkerSession> workerSessionMap =this.retrieveActiveWorkerSessionMap();

		if(workerSessionMap!=null && workerSessionMap.containsKey(id))
			return workerSessionMap.get(id);
		else
			return null;
	}
	
		
	private HashMap<Integer, WorkerSession> retrieveActiveWorkerSessionMap(){

		try{
			File file = new File(this.persistentFileName); 

			ObjectInputStream objInputStream = new ObjectInputStream( 
					new FileInputStream(file));

			HashMap<String,Object> storage  = (HashMap<String,Object>) objInputStream.readObject();
			objInputStream.close();

			if(storage.containsKey(ACTIVE))
				return (HashMap<Integer,WorkerSession>) storage.get(ACTIVE);
			else
				return null; //Error, the map should always be there, even when the map is empty.
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



	/**
	 * Adds a MTurk HIT ID for each WorkerSession
	 * 
	 */
	public void updateWorkerSessionsWithHitId(ArrayList<String> hitIDList){}



	/**
	 * 
	 * @param the index of WorkerSession objects stored
	 * @return true if operation succeeded, otherwise false.
	 */
	private boolean overwriteActiveWorkerSessionMap(HashMap<Integer,WorkerSession> newWorkerSessionMap){
		try{
			File file = new File(this.persistentFileName); 

			ObjectInputStream objInputStream = new ObjectInputStream( 
					new FileInputStream(file));

			HashMap<String,Object> storage = (HashMap<String,Object>) objInputStream.readObject();
			objInputStream.close();
			
			storage.put(ACTIVE, newWorkerSessionMap);
			
			ObjectOutputStream objOutputStream = new ObjectOutputStream( 
					new FileOutputStream(new File(this.persistentFileName)));

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
		try{
			File file = new File(this.persistentFileName); 

			ObjectInputStream objInputStream = new ObjectInputStream( 
					new FileInputStream(file));

			HashMap<String,Object> storage = (HashMap<String,Object>) objInputStream.readObject();
			objInputStream.close();
			
			return (ArrayList<WorkerSession>) storage.get(CLOSED);
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
	
	/**
	 * 
	 * @param closedSessionList the list of closed WorkerSession (it will overwrite the existing one)
	 * @return true if operation succeeded, otherwise false.
	 */
	private boolean overwriteClosedWorkerSessionList(ArrayList<WorkerSession> closedSessionList){
		try{
			File file = new File(this.persistentFileName); 

			ObjectInputStream objInputStream = new ObjectInputStream( 
					new FileInputStream(file));

			HashMap<String,Object> storage = (HashMap<String,Object>) objInputStream.readObject();
			objInputStream.close();
			
			storage.put(CLOSED, closedSessionList);
			
			ObjectOutputStream objOutputStream = new ObjectOutputStream( 
					new FileOutputStream(new File(this.persistentFileName)));

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
	
	public HashMap<String, Object> readStorage(){
		try{
			File file = new File(this.persistentFileName); 

			ObjectInputStream objInputStream = new ObjectInputStream( 
					new FileInputStream(file));

			HashMap<String,Object> storage  = (HashMap<String,Object>) objInputStream.readObject();
			objInputStream.close();
			return storage;
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

}
	
