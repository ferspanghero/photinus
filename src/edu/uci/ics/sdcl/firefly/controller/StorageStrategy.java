package edu.uci.ics.sdcl.firefly.controller;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;

/**
 * Implements the strategy pattern to select the type of persistency (file or memory)
 * @author adrianoc
 *
 */
public abstract class StorageStrategy {
	
			
	private static StorageStrategy concreteStrategy;
	
	public static StorageStrategy initializeSingleton(){
		return initializeLite();
	}
	
	private static StorageStrategy initializeLite(){
		concreteStrategy = LiteContainerManager.initializeSingleton();
		return concreteStrategy;
	}
	
	private static StorageStrategy initializeSerialization(){
		concreteStrategy = StorageManager.initializeSingleton();
		return concreteStrategy;
	}
	
	//Methods are in order of standard execution by workers performing tasks
	public abstract void cleanUpRepositories();
	/**
	 * 
	 * @param consentDateStr the date when the worker gave the consent for the current task
	 * @param fileName the file that will be investigated by the worker
	 * @return
	 */
	public abstract Worker insertConsent(String consentDateStr, String fileName);
	
	/** 
	 * 
	 * @param workerId
	 * @param fileName the name of the file related to the bug
	 * @return
	 */
	public abstract WorkerSession readNewSession(String workerId, String fileName);
	public abstract String getSessionIdForWorker(String workerId);
	public abstract boolean areThereMicrotasksAvailable();
	public abstract boolean updateMicrotaskAnswer(String sessionId, Integer microtaskId, Answer answer);
	public abstract Microtask getNextMicrotask(String sessionId);
	public abstract boolean insertSkillTest(Worker worker);
	public abstract boolean insertQuitReason(Worker worker, String answer);
	public abstract Worker readExistingWorker(String workerId);
	public abstract boolean insertSurvey(Worker worker);
	public abstract boolean insertFeedback(String feedback);
	public abstract WorkerSession readActiveSessionById(String sessionId);

	}
