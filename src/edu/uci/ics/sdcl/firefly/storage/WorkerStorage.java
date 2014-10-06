package edu.uci.ics.sdcl.firefly.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.servlet.SkillTestServlet;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

public class WorkerStorage {
	private String persistentFileName = "consent.ser";

	private static WorkerStorage storage;
	private static Logger logger;
	
	public synchronized static WorkerStorage initializeSingleton(){
		if(storage == null)
			storage = new WorkerStorage();
		return storage;
	}
	
	private WorkerStorage() {
		
		try{
			PropertyManager manager = PropertyManager.initializeSingleton();
			String path = manager.serializationPath;
			logger = LoggerFactory.getLogger(WorkerStorage.class);
			
			this.persistentFileName = path + this.persistentFileName;
			File file = new File(this.persistentFileName);
			if(!file.exists() ||  file.isDirectory()){
				// No files has been created yet. 

				// Create a sample object, that contains the default values.
				HashMap<String, Worker> workerMap = new HashMap<String, Worker>();

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
	
	public void cleanUp(){
		this.updateIndex(new HashMap<String,Worker>());
	}
	
	public boolean insert(String workerId, Worker worker){

		HashMap<String, Worker> workerMap = this.retrieveIndex();

		if((workerMap!=null)&&(worker!=null)){
			//Logging
			if(worker.getGrade()!=null && worker.getGrade()>0)
				
				logger.info("workerId:"+worker.getWorkerId()+ ", sessionId:"+worker.getSessionId()
					+", test1:"+worker.getGradeMap().get(SkillTestServlet.QUESTION1)
					+", test2:"+worker.getGradeMap().get(SkillTestServlet.QUESTION2)
					+", test3:"+worker.getGradeMap().get(SkillTestServlet.QUESTION3)
					+", test4:"+worker.getGradeMap().get(SkillTestServlet.QUESTION4)
					+", grade:"+worker.getGrade()
					+", testDuration:"+worker.getSkillTestDuration()
					+", survey:{"+worker.getSurveyAnswersToString()+"}");
			else
				logger.info("workerId:"+worker.getWorkerId()+ ", sessionId:"+worker.getSessionId()
						+", consentDate:" + worker.getConsentDate().toString());
			
			//Object persistence
			workerMap.put(workerId, worker);
			return this.updateIndex(workerMap);	
		}		
		else
			return false;
	}
	
	public Worker readSingleWorker(String workerId){

		HashMap<String, Worker> workerMap = this.retrieveIndex();

		if(workerMap!=null && workerMap.containsKey(workerId))
			return workerMap.get(workerId);
		else
			return null;
	}
	
	public HashMap<String, Worker> readAllWorkers(){
		return this.retrieveIndex();
	}
	
	public boolean remove(String workerId) {

		HashMap<String, Worker> workerMap = this.retrieveIndex();

		if(workerMap!=null && !workerMap.isEmpty()){
			workerMap.remove(workerId);
			return this.updateIndex(workerMap);				
		}		
		else
			return false;
	}
	
	/**
	 * @return a worker identifier that does not exist in the storage yet.
	 */
	public String getNewWorkerKey() {
		HashMap<String, Worker> indexMap = this.retrieveIndex();
		Integer keyInt = new Integer(indexMap.size()); 
		String key = keyInt.toString();
		while(indexMap.containsKey(key)){
			keyInt++;
			key = keyInt.toString();
		}
		return key;
	}
	
	private synchronized boolean updateIndex(HashMap<String, Worker> workerMap){
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
	private synchronized HashMap<String, Worker> retrieveIndex(){
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
