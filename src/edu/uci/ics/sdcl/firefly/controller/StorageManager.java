package edu.uci.ics.sdcl.firefly.controller;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;

/**
 * Manages all the write and read operations to the persistent objects.
 * Keeps the consistency and generates a txt log of all operations.
 * 
 * @author Christian Adriano
 *
 */
public class StorageManager {
	
	private WorkerSessionStorage sessionStorage;
	private MicrotaskStorage microtaskStorage;
	
	public StorageManager(){
		sessionStorage = new WorkerSessionStorage();
		microtaskStorage = new MicrotaskStorage();
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
	public boolean updateMicrotaskAnswer(String fileName, Integer sessionId, Integer microtaskId, Answer answer){
		//set answer to the microtask in the WorkerSession
		boolean success1 = sessionStorage.setSessionMicrotaskAnswer(sessionId,microtaskId,answer);
		
		//add another answer to the microtask 
		boolean success2 = microtaskStorage.insertAnswer(fileName, microtaskId, answer);
		 
		//log the operations in a text file
		//writeLog (Session, Method Name, Question, Answer, Time Duration);
		
		 return success1 && success2;
	}
	
	
	/** 
	 * @param userId is used to associate the WorkerSession with a unique anonymous worker
	 * @param hitIT is used to associate the WorkerSession with the Mechanical Turk HIT
	 * @return a new session, if there aren't new sessions available return null
	 */
	public WorkerSession readNewSession(String userId, String hitId){
		WorkerSession session = sessionStorage.readNewWorkerSession();
		if(session!=null){
			session.setUserId(userId);
			session.setHitId(hitId);
			sessionStorage.updateActiveWorkerSession(session);
			return session;
		}
		else
			return null;	
	}
	
	/**
	 * 
	 * @param sessionId 
	 * @return a session that is already active (received at least one answer), otherwise null 
	 * if the session is not active anymore or if the session ID is invalid
	 */
	public WorkerSession readActiveSession(Integer sessionId){
		WorkerSession session = sessionStorage.readActiveWorkerSessionByID(sessionId);
		return session;
	}
	
	private void writeLog(String operation, String data){
		//TODO
	}

}
