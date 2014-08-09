package edu.uci.ics.sdcl.firefly.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.Worker;

public class WorkerStorage {
	private String persistentFileName = "consent.ser";

	public WorkerStorage() {
		try{
			File file = new File(this.persistentFileName);
			if(!file.exists() ||  file.isDirectory()){
				// No files has been created yet. 

				// Create a sample object, that contains the default values.
				HashMap<String, Worker> workerMap = new HashMap<String, Worker>();	// String = userId

				ObjectOutputStream objOutputStream = new ObjectOutputStream( 
						new FileOutputStream(new File(this.persistentFileName)));

				objOutputStream.writeObject( workerMap );
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
	
	public boolean insert(String userId, Worker worker){

		HashMap<String, Worker> workerMap = this.retrieveIndex();

		if(workerMap!=null){
			workerMap.put(userId, worker);
			return this.updateIndex(workerMap);	
		}		
		else
			return false;
	}
	
	public Worker readSingleWorker(String userId){

		HashMap<String, Worker> workerMap = this.retrieveIndex();

		if(workerMap!=null && workerMap.containsKey(userId))
			return workerMap.get(userId);
		else
			return null;
	}
	
	public HashMap<String, Worker> readAllWorkers(){
		return this.retrieveIndex();
	}
	
	public boolean remove(String userId) {

		HashMap<String, Worker> workerMap = this.retrieveIndex();

		if(workerMap!=null && !workerMap.isEmpty()){
			workerMap.remove(userId);
			return this.updateIndex(workerMap);				
		}		
		else
			return false;
	}
	
	private boolean updateIndex(HashMap<String, Worker> workerMap){
		try{
			ObjectOutputStream objOutputStream = new ObjectOutputStream( 
					new FileOutputStream(new File(this.persistentFileName)));

			objOutputStream.writeObject( workerMap );
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
	
	@SuppressWarnings("unchecked")
	private HashMap<String, Worker> retrieveIndex(){
		try{
			HashMap<String,Worker> workerMap;
			ObjectInputStream objInputStream = new ObjectInputStream( 
					new FileInputStream(new File(this.persistentFileName)));

			workerMap = (HashMap<String, Worker>) objInputStream.readObject();

			objInputStream.close();
			return workerMap;
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
