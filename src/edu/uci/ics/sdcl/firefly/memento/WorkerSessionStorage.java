package edu.uci.ics.sdcl.firefly.memento;

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
 * @author Christian Adriano
 *
 */
public class WorkerSessionStorage {

	private String persistentFileName = "workersession.ser"; 

	public WorkerSessionStorage(){

	}

	public HashMap<Integer, WorkerSession> retrieveWorkerSessionMap(){

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

	public Integer getNumberOfSessions(){
		HashMap<Integer, WorkerSession> workerSessionMap = retrieveWorkerSessionMap();
		if(workerSessionMap!=null)
			return workerSessionMap.size();
		else
			return null;
	}


	/** Stores a map of WorkerSession. 
	 * @param map is in indexed by WorkerSession ID
	 */
	public void writeWorkerSession(Integer id, WorkerSession session){

		try{
			File file = new File(this.persistentFileName);
			if(file.exists() && !file.isDirectory()){

				//Try to retrieve an existing file.
				ObjectInputStream objInputStream = new ObjectInputStream( 
						// By using "FileOutputStream" we will 
						// Read it from a File in the file system
						new FileInputStream(file));

				HashMap<Integer, WorkerSession> workerSessionMap = (HashMap<Integer, WorkerSession>) objInputStream.readObject();


				ObjectOutputStream objOutputStream = new ObjectOutputStream( 
						// By using "FileOutputStream" we will 
						// Write it to a File in the file system
						new FileOutputStream(new File(this.persistentFileName)));

				objOutputStream.writeObject( workerSessionMap );
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

	/** Retrieves a WorkerSession
	 * 
	 * @param id the unique identifier for a workersession
	 * @return a workersession, in case any was found, return null.
	 */
	public WorkerSession readWorkerSession(Integer id){
		try{
			HashMap<Integer,WorkerSession> workerSessionMap;
			ObjectInputStream objInputStream = new ObjectInputStream( 
					// By using "FileOutputStream" we will 
					// Read it to a File in the file system
					new FileInputStream(new File(this.persistentFileName)));

			workerSessionMap = (HashMap<Integer,WorkerSession>) objInputStream.readObject();
			objInputStream.close();

			if(workerSessionMap!=null && workerSessionMap.containsKey(id))
				return workerSessionMap.get(id);
			else
				return null;
		}
		catch(IOException exception){
			System.err.print("Error while opening workersession serialized file:" + exception.toString());
			return null;
		}
		catch(Exception exception){
			System.err.print("Error while opening workersession serialized file:" + exception.toString());
			return null;
		}
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


}
