package edu.uci.ics.sdcl.firefly.controller;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.servlet.SkillTestServlet;
import edu.uci.ics.sdcl.firefly.storage.SkillTestSource;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;
import edu.uci.ics.sdcl.firefly.util.RandomKeyGenerator;

public class LiteContainerManager extends StorageStrategy{

	/** Table with Session ID and session */
	private Hashtable<String, WorkerSession> activeSessionTable;

	/** Vector of closed Sessions */
	private Vector<WorkerSession> closedSessionVector;

	/** List of available sessions */
	private Hashtable<String,Stack<WorkerSession>> newSessionTable;

	/** Keeps track of Workers*/
	private Hashtable<String, Worker> workerTable;
	
	/** Keeps track of Session Microtasks already written */
	private Hashtable<String,Hashtable<String, Hashtable<Integer, Integer>>> sessionMicrotaskTable;

	/** Keeps track of Worker Consents, Surveys, and Feedback 
	 * Indexed by workerId and sessionId and has one of the labels "CONSENT", "SURVEY", "FEEDBACK"*/
	private Hashtable<String, Hashtable<String,String>> consentSurveyTestFeedbackTable;
		
	/** Logger for register consent, skilltest, and survey */
	private Logger consentLogger;

	/** Logger for sessions and microtasks  */
	private Logger sessionLogger;

	/** Random workerID generator */
	private static final RandomKeyGenerator keyGenerator = new RandomKeyGenerator();

	private static LiteContainerManager container;

	public synchronized static LiteContainerManager initializeSingleton(){
		if(container == null)
			container = new LiteContainerManager();
		return container;
	}
	
	public void killSingleton()
	{
		
		container = null;
	}
	
	public boolean isFileAvailable(String fileName)
	{
		if(newSessionTable.get(fileName) != null)
			return true;
		else
			return false;
	}

	public void cleanUpRepositories(){
		StorageManager storageManager = new StorageManager();
		storageManager.cleanUpRepositories();
	}

	private LiteContainerManager(){
		//Initialize Sessions
		WorkerSessionStorage storage = WorkerSessionStorage.initializeSingleton();
		newSessionTable = storage.retrieveNewSessionStorage();
		activeSessionTable = new Hashtable<String, WorkerSession>();
		closedSessionVector = new Vector<WorkerSession>();
		sessionMicrotaskTable = new Hashtable<String, Hashtable<String,Hashtable<Integer, Integer>>>();
		consentSurveyTestFeedbackTable = new Hashtable<String, Hashtable<String,String>>();
		
		//Initialize Worker table
		workerTable = new Hashtable<String, Worker>();

		//Initialize Loggers
		consentLogger = LoggerFactory.getLogger("consent");
		sessionLogger = LoggerFactory.getLogger("session");
		
		//Bind files with skill tests
		SkillTestSource skillTests = new SkillTestSource();
		for (String fileName : newSessionTable.keySet()) {
			skillTests.bindSkillTest(fileName);
		}
//		skillTests.bindSkillTest();
	}

	public synchronized  Microtask getNextMicrotask(String sessionId){
		if(this.activeSessionTable.containsKey(sessionId)){
			WorkerSession session = this.activeSessionTable.get(sessionId);	
			return session.getCurrentMicrotask();
		}
		else return null;
	}

	/** 
	 * @param workerId is used to associate the WorkerSession with a unique anonymous worker
	 * @param filename the name of the file with the possible bug.
	 * @return a new session, if there aren't new sessions available return null
	 */
	public synchronized WorkerSession readNewSession(String workerId, String fileName)	
	{
		Worker worker = this.workerTable.get(workerId);
		if(newSessionTable!=null && !newSessionTable.isEmpty()){
			Stack<WorkerSession> stack = newSessionTable.get(fileName);
			if(stack!=null && !stack.isEmpty()){
				WorkerSession  session = stack.pop();
				newSessionTable.put(fileName, stack);
				session.setWorkerId(workerId);
				sessionLogger.info("EVENT%OPEN SESSION% workerId%"+ workerId
						+"% sessionId%"+ session.getId()
						+"% fileName%"+fileName);
				worker.setSessionId(session.getId());
				this.activeSessionTable.put(session.getId(), session); 
				return session;
			}
			else return null;
		}
		else
			return null;
	}
	

	private synchronized void restoreSession(String sessionId){
		WorkerSession session = this.activeSessionTable.get(sessionId);
		String fileName = session.getFileName();
		session.reset();
		Stack<WorkerSession> stack = newSessionTable.get(fileName);
		if(stack==null){
			stack = new Stack<WorkerSession>();
		}
		stack.push(session);
		newSessionTable.put(fileName, stack);		
	}

	public synchronized boolean areThereMicrotasksAvailable(){
		return !newSessionTable.isEmpty();
	}


	/**
	 * @return true if all three operations succeeded, false if any of them failed.
	 */
	public synchronized boolean updateMicrotaskAnswer(String sessionId, Integer microtaskId, Answer answer)
	{
		//set answer to the microtask in the WorkerSession 
		WorkerSession session = this.activeSessionTable.get(sessionId);

		if(session!=null){
			session.insertMicrotaskAnswer(microtaskId,answer); 

			Microtask microtask = session.getPreviousMicrotask();

			Worker worker = workerTable.get(answer.getWorkerId());

			String explanation = answer.getExplanation().replaceAll("[\n]"," ").replaceAll("[\r]"," ");
			if(updateSessionMicrotaskTable(worker.getWorkerId(),sessionId,microtaskId)){
				sessionLogger.info("EVENT%MICROTASK% workerId%"+ answer.getWorkerId()+"% fileName%"+microtask.getFileName()
						+"% sessionId%"+ sessionId+"% microtaskId%"+microtaskId
						+"% question%"+ microtask.getQuestion()+"% answer%"+answer.getOption()
						+"% confidenceLevel%"+answer.getConfidenceOption()
						+ "% difficulty%"+answer.getDifficulty()
						+"% duration%"+answer.getElapsedTime()
						+"% explanation%"+explanation);

				if(session.isClosed()){//Move session to closed //EVENT
					this.closedSessionVector.add(session);
					sessionLogger.info("EVENT%CLOSE SESSION% workerId%"+ session.getWorkerId()+"% sessionId%"+ session.getId()+"% fileName%"+worker.getCurrentFileName());
					this.activeSessionTable.remove(session.getId());
				}
			}
			return true;	
		} 
		else
			return false;
	}

	private synchronized boolean updateSessionMicrotaskTable(String workerId, String sessionId, Integer microtaskId){
		Hashtable<String,Hashtable<Integer, Integer>> sessionMap = sessionMicrotaskTable.get(workerId);
		Hashtable<Integer, Integer> microtaskMap;
		if(sessionMap==null){
			sessionMap = new Hashtable<String,Hashtable<Integer, Integer>>();
			microtaskMap = new Hashtable<Integer, Integer>();
			microtaskMap.put(microtaskId,microtaskId);
			sessionMap.put(workerId, microtaskMap);
			this.sessionMicrotaskTable.put(sessionId, sessionMap);
			return true;
		}
		else{
			microtaskMap = sessionMap.get(sessionId);
			if(microtaskMap==null){
				microtaskMap = new Hashtable<Integer, Integer>();
				microtaskMap.put(microtaskId,microtaskId);
				sessionMap.put(sessionId, microtaskMap);
				this.sessionMicrotaskTable.put(workerId, sessionMap);
				return true;
			}
			else
				if(!microtaskMap.containsKey(microtaskId)){
					microtaskMap.put(microtaskId,microtaskId);
					sessionMap.put(sessionId, microtaskMap);
					this.sessionMicrotaskTable.put(workerId, sessionMap);
					return true;
				}
				else 
					return false;
		}
	}
	
	private synchronized boolean updateConsentSurveyTestFeedbackTable(String workerId, String fileName, String label){
		Hashtable<String, String> fileNameMap = this.consentSurveyTestFeedbackTable.get(workerId);
		if(fileNameMap==null){
			fileNameMap = new Hashtable<String, String>();
			fileNameMap.put(fileName,label);
			this.consentSurveyTestFeedbackTable.put(workerId, fileNameMap);
			return true;
		}
		else
			if(!fileNameMap.containsKey(fileName)){
				fileNameMap.put(fileName,label);
				this.consentSurveyTestFeedbackTable.put(workerId, fileNameMap);
				return true;
			}
		else {
			String writtenLabel = fileNameMap.get(fileName);
			if(label.compareTo(writtenLabel)!=0){
				fileNameMap.put(fileName, label);
				this.consentSurveyTestFeedbackTable.put(workerId, fileNameMap);
				return true;
			}
			else
				return false;
		}
		
	}
	
	
	public synchronized String getSessionIdForWorker(String workerId) {
		String test= workerTable.get(workerId).getSessionId();
		return test;
	}


	public synchronized boolean insertSkillTest(Worker worker) 
	{
		if(worker!=null){
			if(updateConsentSurveyTestFeedbackTable(worker.getWorkerId(),worker.getCurrentFileName(),"SKILLTEST")){
				consentLogger.info("EVENT%SKILLTEST% workerId%"+worker.getWorkerId()
					+ "% fileName%"+worker.getCurrentFileName()
					+"% test1%"+worker.getGradeMap().get(SkillTestServlet.QUESTION1)
					+"% test2%"+worker.getGradeMap().get(SkillTestServlet.QUESTION2)
					+"% test3%"+worker.getGradeMap().get(SkillTestServlet.QUESTION3)
					+"% test4%"+worker.getGradeMap().get(SkillTestServlet.QUESTION4)
					+"% grade%"+worker.getGrade()
					+"% testDuration%"+worker.getSkillTestDuration()
					);
			}
				this.workerTable.put(worker.getWorkerId(), worker);
				return true;
		}
		else{
			consentLogger.error("EVENT%ERROR% could not store worker SKILL TEST.");
			return false;
		}
	}
	
	public synchronized boolean insertQuitReason(Worker worker, String answer)
	{
		if(worker != null){
			if(updateConsentSurveyTestFeedbackTable(worker.getWorkerId(),worker.getCurrentFileName(),"QUIT")){
				consentLogger.info("EVENT%QUIT% workerId%"+worker.getWorkerId()
						+ "% fileName%"+worker.getCurrentFileName()
						+"% answer%"+answer);
				this.workerTable.put(worker.getWorkerId(), worker);
				
				//Put the session back to the pile so another worker can take it.
				restoreSession(worker.getSessionId());
			}
			return true;
		}else{
			if(answer=="consentForm"){
				consentLogger.error("EVENT%ERROR% user quit in consent page.");
			}else{
				consentLogger.error("EVENT%ERROR% could not store worker EXIT REASON.");
			}
			return false;
		}
	}
	
	
	public synchronized Worker insertNewWorker(String consentDateStr, String fileName){
		Worker worker = new Worker(keyGenerator.generate(),consentDateStr, fileName);
		this.workerTable.put(worker.getWorkerId(), worker);
		return worker;
	}
	
	public synchronized boolean insertConsent(Worker worker, String consentDateStr, String fileName) 
	{
		if(worker!=null){
			if(updateConsentSurveyTestFeedbackTable(worker.getWorkerId(),worker.getCurrentFileName(),"CONSENT")){
			consentLogger.info("EVENT%CONSENT% workerId%"+worker.getWorkerId()
					+"% fileName%"+worker.getCurrentFileName()
					+"% consentDate%" + worker.getConsentDate().toString());	
			}			
			return true;
		}
		else{
			consentLogger.error("EVENT%ERROR% could not store worker SURVEY.");
			return false;
		}
	}

	public synchronized boolean insertSurvey(Worker worker) 
	{
		if(worker!=null){
			if(updateConsentSurveyTestFeedbackTable(worker.getWorkerId(),worker.getCurrentFileName(),"SURVEY")){
				consentLogger.info("EVENT%SURVEY% workerId%"+worker.getWorkerId()
					+ "% fileName%"+worker.getCurrentFileName()
					+ "% "+worker.getSurveyAnswersToString());
			}
			return true;
		}
		else{
			consentLogger.error("EVENT%ERROR% could not store worker SURVEY.");
			return false;
		}
	}

	public synchronized Worker readExistingWorker(String workerId) {
		return this.workerTable.get(workerId);
	}

	public synchronized boolean insertFeedback(String feedback, Worker worker) {
		if(worker!=null && updateConsentSurveyTestFeedbackTable(worker.getWorkerId(),worker.getCurrentFileName(),"FEEDBACK")){
			consentLogger.info("EVENT%FEEDBACK% %workerID%"+worker.getWorkerId()+"% fileName%"+worker.getCurrentFileName()+" %feedback%" +feedback );
		}
		return true;
	}
	
	public synchronized  WorkerSession readActiveSessionById(String sessionId)
	{
		return this.activeSessionTable.get(sessionId);
	}
}
