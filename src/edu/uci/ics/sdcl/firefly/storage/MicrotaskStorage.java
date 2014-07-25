package edu.uci.ics.sdcl.firefly.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Set;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.Microtask;

/** Decouples the persistence mechanism for Microtasks
 * 
 * Holds the index for all microtasks for all files. 
 * String key = file name, ArrayList is the list of microtasks
 * 
 * HashMap<String,FileDebugSession> debugSessionMap;
 * 
 * @author Christian Adriano
 *
 */
public class MicrotaskStorage {

	private String persistentFileName = "microtasks.ser";


	public MicrotaskStorage(){
	
	 try{
	 	File file = new File(this.persistentFileName);
			if(!file.exists() ||  file.isDirectory()){
				// No files has been created yet. 

				// Create a sample object, that contains the default values.
				HashMap<String,FileDebugSession> debugSessionMap = new HashMap<String,FileDebugSession>();

				ObjectOutputStream objOutputStream = new ObjectOutputStream( 
						new FileOutputStream(new File(this.persistentFileName)));

				objOutputStream.writeObject( debugSessionMap );
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
 

	/** Retrieves the list of microtasks for provided file
	 * 
	 * @param fileName
	 * @return
	 */
	public FileDebugSession read(String fileName){

		HashMap<String,FileDebugSession> debugSessionMap = this.retrieveIndex();

		if(debugSessionMap!=null && debugSessionMap.containsKey(fileName))
			return debugSessionMap.get(fileName);
		else
			return null;
	}

	/** Retrieves the all the file debugging sessions .
	 * A debugging session is identified by a file name
	 * 
	 * @return a set of debugging sessions
	 */
	public Set<String> retrieveDebuggingSessionNames(){	
		HashMap<String,FileDebugSession> debugSessionMap = this.retrieveIndex();

		if(debugSessionMap!=null && !debugSessionMap.isEmpty())
			return debugSessionMap.keySet();
		else
			return null;			
	}

	/** 
	 * Add a new answer to a microtask
	 * @param fileName  the file from which the codesnippet was obtained
	 * @param microtaskId the unique identifier for the microtask
	 * @param answer the answer obtained from the user
	 */
	public void insertAnswer(String fileName, Integer microtaskId, Answer answer){ 

		FileDebugSession fileDebuggingSession = this.read(fileName);
		Microtask mtask = fileDebuggingSession.getMicrotask(microtaskId);
		mtask.addAnswer(answer);
		fileDebuggingSession.incrementAnswersReceived(mtask.getNumberOfAnswers());
		//TODO change this system.out to be a log
		System.out.println("Inserting microtask id:"+mtask.getID()+" answers: "+mtask.getNumberOfAnswers()+" : "+mtask.getAnswerList().toString());
		this.insert(fileName, fileDebuggingSession);
	}

	/** Insert a new List of Microtasks. It overwrites any existing one for the same file.
	 * 
	 * @param fileName the name of the file containing the methods for which microtasks were generated
	 * @param fileDebugSession the list of microtasks associated to the file
	 * @return true if operation succeeded, otherwise false.
	 */
	public boolean insert(String fileName, FileDebugSession fileDebuggingSession){

		HashMap<String,FileDebugSession> debugSessionMap = this.retrieveIndex();

		if(debugSessionMap!=null){
			debugSessionMap.put(fileName, fileDebuggingSession);
			return this.updateIndex(debugSessionMap);	
		}		
		else
			return false;
	}

	/** Replaces the existing list of Microtasks for a new one. 
	 * 
	 * @param fileName
	 * @param microtaskList
	 */
	public void replace(String fileName, FileDebugSession fileDebuggingSession){
		this.remove(fileName);
		this.insert(fileName, fileDebuggingSession);
	}


	/**
	 * Removes the debug session data associated to the provided file name
	 * @param fileName the name of the file containing the methods for which microtasks were generated
	 * @return true if operation succeeded, otherwise false.
	 */
	public boolean remove(String fileName) {

		HashMap<String,FileDebugSession> debugSessionMap = this.retrieveIndex();

		if(debugSessionMap!=null && !debugSessionMap.isEmpty()){
			debugSessionMap.remove(fileName);
			return this.updateIndex(debugSessionMap);				
		}		
		else
			return false;
	}

	/**
	 * 
	 * @return the index of microtasks stored in the file
	 */
	private HashMap<String,FileDebugSession> retrieveIndex(){
		try{
			HashMap<String,FileDebugSession> debugSessionMap;
			ObjectInputStream objInputStream = new ObjectInputStream( 
					new FileInputStream(new File(this.persistentFileName)));

			debugSessionMap = (HashMap<String, FileDebugSession>) objInputStream.readObject();

			objInputStream.close();
			return debugSessionMap;
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
	 * @param the index of microtasks stored in the file
	 * @return true if operation succeeded, otherwise false.
	 */
	private boolean updateIndex(HashMap<String,FileDebugSession> debugSessionMap){
		try{
			ObjectOutputStream objOutputStream = new ObjectOutputStream( 
					new FileOutputStream(new File(this.persistentFileName)));

			objOutputStream.writeObject( debugSessionMap );
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
