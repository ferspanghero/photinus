package edu.uci.ics.sdcl.firefly.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.servlet.SkillTestServlet;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

public class WorkerStorage {
	private String persistentFileName = "consent.ser";

	private static WorkerStorage storage;
	private static Logger logger;
	private static Hashtable<String, Worker> workerTable;
	
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
				workerTable = new Hashtable<String, Worker>();

				ObjectOutputStream objOutputStream = new ObjectOutputStream( 
						new FileOutputStream(new File(this.persistentFileName)));

				objOutputStream.writeObject( workerTable );
				objOutputStream.close();
			}
			else
				workerTable=this.retrieveIndex();
		}
		catch(IOException exception){
			logger.error(exception.toString());
		}
		catch(Exception exception){
			logger.error(exception.toString());
		}
	}
	
	public void cleanUp(){
		this.updateIndex(new Hashtable<String,Worker>());
	}
	
	
	public synchronized boolean insertConsent(Worker worker) {
		if(worker!=null){
			logger.info("EVENT= CONSENT; workerId="+worker.getWorkerId()+ "; sessionId="+worker.getSessionId()
					+"; consentDate=" + worker.getConsentDate().toString());	
	
			return this.updateWorker(worker);
		}
		else{
			logger.error("EVENT= ERROR; could not store worker CONSENT.");
			return false;
		}
	}
	
	public synchronized boolean insertSurvey(String parameter, Worker worker) {
		if(worker!=null){
			logger.info("EVENT= SURVEY; workerId="+worker.getWorkerId()+ "; sessionId="+worker.getSessionId()
					+"; survey="+worker.getSurveyAnswersToString());
			return this.updateWorker(worker);
		}
		else{
			logger.error("EVENT= ERROR; could not store worker SURVEY.");
			return false;
		}
	}
	
	
	public synchronized boolean insertSKillTest(Worker worker){
		if(worker!=null && worker.getGrade()!=null && worker.getGrade()>0){		
				logger.info("EVENT= SKILLTEST; workerId="+worker.getWorkerId()+ "; sessionId="+worker.getSessionId()
					+"; test1="+worker.getGradeMap().get(SkillTestServlet.QUESTION1)
					+"; test2="+worker.getGradeMap().get(SkillTestServlet.QUESTION2)
					+"; test3="+worker.getGradeMap().get(SkillTestServlet.QUESTION3)
					+"; test4="+worker.getGradeMap().get(SkillTestServlet.QUESTION4)
					+"; grade="+worker.getGrade()
					+"; testDuration="+worker.getSkillTestDuration());
				//persist data
				return updateWorker(worker);
		}
		else{
			logger.error("EVENT= ERROR; could not store worker SKILLTEST.");
			return false;
		}
	}
	
	private boolean updateWorker(Worker worker){
		//Object persistence
		if((workerTable!=null)&&(worker!=null)){
			workerTable.put(worker.getWorkerId(), worker);
			return this.updateIndex(workerTable);	
		}		
		else{
			logger.error("EVENT= ERROR; could not store worker.");
			return false;
		}
	}
	
	public synchronized Worker readExistingWorker(String workerId){

		if(workerTable!=null && workerTable.containsKey(workerId))
			return workerTable.get(workerId);
		else
			return null;
	}
	
	public synchronized Hashtable<String, Worker> readAllWorkers(){
		return this.retrieveIndex();
	}
	
	public synchronized boolean remove(String workerId) {

		workerTable = this.retrieveIndex();

		if(workerTable!=null && !workerTable.isEmpty()){
			workerTable.remove(workerId);
			return this.updateIndex(workerTable);				
		}		
		else
			return false;
	}
	
	/**
	 * @return a worker identifier that does not exist in the storage yet.
	 */
	public synchronized String getNewWorkerKey() {
		Integer keyInt = new Integer(workerTable.size()); 
		String key = keyInt.toString();
		while(workerTable.containsKey(key)){//Avoid to use an already existing Worker ID
			keyInt++;
			key = keyInt.toString();
		}
		return key;
	}
	
	//------------------------------------------------------------------------------------------------
	
	private synchronized boolean updateIndex(Hashtable<String, Worker> workerTable){
		try{
			if(workerTable==null){
				logger.error("EVENT= ERROR; Avoided trying to write nullpointer in Worker repository.");
				return false;
			}
			else{
			ObjectOutputStream objOutputStream = new ObjectOutputStream( 
					new FileOutputStream(new File(this.persistentFileName)));

			objOutputStream.writeObject( workerTable );
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
	
	@SuppressWarnings("unchecked")
	private synchronized Hashtable<String, Worker> retrieveIndex(){
		try{
			ObjectInputStream objInputStream = new ObjectInputStream( 
					new FileInputStream(new File(this.persistentFileName)));

			workerTable = (Hashtable<String, Worker>) objInputStream.readObject();

			objInputStream.close();
			return workerTable;
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
