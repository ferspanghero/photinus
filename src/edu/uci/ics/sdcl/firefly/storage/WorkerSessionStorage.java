package edu.uci.ics.sdcl.firefly.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

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
	
	private static final String NEW = "NEW";
	private static final String ACTIVE = "ACTIVE";
	private static final String CLOSED = "CLOSED";

	private String persistentFileName = "workersession.ser"; 

	public WorkerSessionStorage(){
		try{
			File file = new File(this.persistentFileName);
			if(!file.exists() ||  file.isDirectory()){
				// No files has been created yet. 

				// Create a sample object, that contains the default values.
				HashMap<String, HashMap<Integer, WorkerSession>> lifecycleMap = 
						new HashMap<String, HashMap<Integer, WorkerSession>>();

				ObjectOutputStream objOutputStream = new ObjectOutputStream( 
						new FileOutputStream(new File(this.persistentFileName)));

				objOutputStream.writeObject( lifecycleMap );
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

	public HashMap<Integer, WorkerSession> retrieveWorkerSessionMap(String lifecycle){

		try{
			HashMap<Integer,WorkerSession> workerSessionMap;
			File file = new File(this.persistentFileName);
			
			if(file.exists() && !file.isDirectory()){

				//Try to retrieve an existing file.
				ObjectInputStream objInputStream = new ObjectInputStream( 
						// By using "FileOutputStream" we will 
						// Read it from a File in the file system
						new FileInputStream(file));

				
				workerSessionMap = (HashMap<Integer, WorkerSession>) objInputStream.readObject();

				objInputStream.close();
				return workerSessionMap;
			}
			// No files has been created yet. 
			else{
				// Create a sample object, that contains the default values.
				workerSessionMap = new HashMap<Integer,WorkerSession>();

				ObjectOutputStream objOutputStream = new ObjectOutputStream( 
						// By using "FileOutputStream" we will 
						// Write it to a File in the file system
						new FileOutputStream(new File(this.persistentFileName)));

				objOutputStream.writeObject( workerSessionMap );
				objOutputStream.close();
				return workerSessionMap;
			}

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

	public Integer getNumberOfNewSessions(){
		HashMap<Integer, WorkerSession> workerSessionMap = retrieveWorkerSessionMap(WorkerSessionStorage.NEW);
		if(workerSessionMap!=null)
			return workerSessionMap.size();
		else
			return null;
	}
	
	
	public WorkerSession readNewWorkerSession(){
		
		//HashMap<String, HashMap<String, ArrayList<HashMap<String,WorkerSession>>>>> workerSessionMap = this.retrieveIndex(WorkerSessionStorage.NEW);
		//HashMap<>
		//if(workerSessionMap!=null && workerSessionMap.containsKey(id)){
			
		//}
		return null;
	}


	/** Stores a map of WorkerSession. 
	 * @param map is in indexed by WorkerSession ID
	 * @return true if operation succeeded, otherwise false.
	 */
	public boolean updateActiveWorkerSession(Integer id, WorkerSession session){
		HashMap<Integer,WorkerSession> workerSessionMap =this.retrieveIndex(WorkerSessionStorage.ACTIVE);
		if(workerSessionMap!=null && workerSessionMap.containsKey(id)){
			workerSessionMap.put(id,session);
			this.updateIndex(WorkerSessionStorage.ACTIVE, workerSessionMap);
			return true;
		}
		else
			return false;
	}

	/** Retrieves a WorkerSession
	 * 
	 * @param id the unique identifier for a WorkerSession
	 * @return a WorkerSession, in case any was found, return null.
	 */
	public WorkerSession readActiveWorkerSession(Integer id){
		
			HashMap<Integer,WorkerSession> workerSessionMap =this.retrieveIndex(WorkerSessionStorage.ACTIVE);

			if(workerSessionMap!=null && workerSessionMap.containsKey(id))
				return workerSessionMap.get(id);
			else
				return null;
		}


	public ArrayList<String> retrieveAvailableSessions(){
		return null;
	}


	public ArrayList<String> retrieveSessionIDs(){
		return null;
	}


	/**
	 * Adds a MTurk HIT ID for each WorkerSession
	 * 
	 */
	public void updateWorkerSessionsWithHitId(ArrayList<String> hitIDList){}

	/**
	 * @param lifecycle defines from which lifecycle the map should be retrieved
	 * @return the index of WorkerSession objects stored
	 */
	private   HashMap<Integer,WorkerSession> retrieveIndex(String lifecycle){
		try{
			ObjectInputStream objInputStream = new ObjectInputStream( 
					new FileInputStream(new File(this.persistentFileName)));

			HashMap<String, HashMap<Integer,WorkerSession>> lifecycleMap = 
					(HashMap<String, HashMap<Integer,WorkerSession>>) objInputStream.readObject();

			objInputStream.close();
			if(lifecycleMap!=null && lifecycleMap.containsKey(lifecycle))
				return lifecycleMap.get(lifecycle);
			else 
				return null;
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
	 * @param the index of WorkerSession objects stored
	 * @param lifecycle defines to which lifecycle the WorkerSession objects belong
	 * @return true if operation succeeded, otherwise false.
	 */
	private boolean updateIndex(String lifecycle, HashMap<Integer,WorkerSession> workerSessionMap){
		try{
			ObjectOutputStream objOutputStream = new ObjectOutputStream( 
					new FileOutputStream(new File(this.persistentFileName)));

			objOutputStream.writeObject( workerSessionMap );
			objOutputStream.close();
			return true;
		}
		catch(IOException exception){
			System.err.print("Error while opening microtasks serialized file:" + exception.toString());
			return false;
		}
		catch(Exception exception){
			System.err.print("Error while opening microtasks serialized file:" + exception.toString());
			return false;
		}
	}
}
