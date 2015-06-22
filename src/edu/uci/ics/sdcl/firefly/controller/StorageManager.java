package edu.uci.ics.sdcl.firefly.controller;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerStorage;

/**
 * Manages all the write and read operations to the persistent objects.
 * 
 * @author Christian Medeiros Adriano
 *
 */
public class StorageManager extends StorageStrategy{

	private WorkerSessionStorage sessionStorage;
	private MicrotaskStorage microtaskStorage;
	private WorkerStorage workerStorage;
	
	public StorageManager(){
		this.sessionStorage = WorkerSessionStorage.initializeSingleton();
		this.microtaskStorage = MicrotaskStorage.initializeSingleton();
		this.workerStorage = WorkerStorage.initializeSingleton();
	}
	
	/**
	 * Keeps two storages consistent - microtask storage and worker session storage.
	 * 
	 * 1. Adds the answer for a microtask to the existing ones for
	 *   the same microtask. Persist the microtask back to the microtask storage.
	 * 2. Updates the answered microtask in the respective worker session. 
	 *  Persist the microtaks back to the Worker Session storage.
	 * 3. Logs both operations in a text file.
	 * 
	 * @return true if all three operations succeeded, false if any of them failed.
	 */
	public synchronized boolean updateMicrotaskAnswer(String sessionId, Integer microtaskId, Answer answer){

		//set answer to the microtask in the WorkerSession
		WorkerSession session = this.sessionStorage.setSessionMicrotaskAnswer(sessionId,microtaskId,answer);
		
		if(session!=null){
		
		//add another answer to the microtask 	
		return this.microtaskStorage.insertAnswer(session.getPreviousMicrotask().getFileName(), microtaskId, answer);

		}
		else
			return false;
	}


	/** 
	 * @param workerId is used to associate the WorkerSession with a unique anonymous worker
	 * @param hitIT is used to associate the WorkerSession with the Mechanical Turk HIT
	 * @return a new session, if there aren't new sessions available return null
	 */
	public synchronized WorkerSession readNewSession(String workerId,String fileName){
		WorkerSession session = this.sessionStorage.readNewWorkerSession(fileName);
		if(session!=null){
			session.setWorkerId(workerId);
			this.sessionStorage.updateActiveWorkerSession(session);
			//Now update the worker info.
			Worker worker = this.workerStorage.readExistingWorker(workerId);
			worker.setSessionId(session.getId());
			this.workerStorage.updateWorker(worker);
			return session;
		}
		else
			return null;	
	}


	public synchronized  Microtask getNextMicrotask(String sessionId){
		WorkerSession session = sessionStorage.readActiveWorkerSessionByID(sessionId);
		if(session!=null)
			return session.getCurrentMicrotask();
		else
			return null;
	}
	
	/** Clean repositories by substituting current data structures
	 * with empty ones.
	 */
	public void cleanUpRepositories(){
		this.workerStorage.cleanUp();

		this.sessionStorage.cleanUp();

		this.microtaskStorage.cleanUp();
	}

	public boolean areThereMicrotasksAvailable(){
		if(this.sessionStorage.getNumberOfNewWorkerSessions()<=0)
			return false;
		else return true;
	}

	public String getSessionIdForWorker(String workerId) {
		return workerStorage.readExistingWorker(workerId).getSessionId();
	}
	
	public synchronized boolean insertSkillTest(Worker worker){
		return workerStorage.insertSkillTest(worker);
	}
	
	public synchronized Worker insertConsent(String consentDateStr, String fileName){
		return this.workerStorage.generateNewWorker(consentDateStr, fileName);
	}
	
	public synchronized boolean insertSurvey(Worker worker){
		return workerStorage.insertSurvey(worker);
	}

	@Override
	public Worker readExistingWorker(String workerId) {
		return workerStorage.readExistingWorker(workerId);
	}

	@Override
	public boolean insertQuitReason(Worker worker, String answer) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public WorkerSession readActiveSessionById(String sessionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Worker insertNewWorker(String consentDateStr, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertConsent(Worker worker, String consentDateStr,
			String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertFeedback(String feedback, Worker workerId) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
