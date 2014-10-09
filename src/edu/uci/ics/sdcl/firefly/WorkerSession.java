package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;

/**
 * Represents a working session, which is a set of microtasks performed by a single worker
 * 
 * @author Christian Medeiros Adriano
 *
 */
public class WorkerSession implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/** Unique identifier for the working session object. */
	private String id;
	
	/** All microtask package in the same worker session. See WorkerSessionFactory for better context. */
	private Vector<Microtask> microtaskList;
	
	/** Keeps track of the position of the current microtask */
	private Integer currentIndex;

	/** Associates a session with an anonymous user */
	private String workerId;

	private static Logger logger;
	
	/** 
	 * Initializes the array and the counter to the first position in the array 
	 * @param id
	 * @param originalId 
	 * @param microtaskList
	 */
	public WorkerSession(String id, Vector<Microtask> microtaskList){
		logger = LoggerFactory.getLogger(WorkerSession.class);
		this.id = id;
		this.microtaskList = microtaskList;
		if(this.microtaskList!=null && this.microtaskList.size()>0)
			this.currentIndex = 0; //points to the first element
		else
			this.currentIndex = -1;
	}

	/** 
	 * Save a microtask and increments the index to point to the next in the list
	 * @param task the microtask that was answered
	 * @return true if the microtask was effectively stored, false otherwise.
	 */
	private boolean storeCurrentMicrotask(Microtask task){
		if(hasCurrent()){
			this.microtaskList.set(this.currentIndex,task);
			this.currentIndex++; 
			return true;
		}
		else
			return false;
	}
	
	/**
	 * 
	 * @return true if the counter points to a position within the list, otherwise false.
	 */
	public boolean hasCurrent(){
		if(currentIndex<0 || currentIndex>microtaskList.size()-1)
			return false;
		else
			return true;
	}

	public String getId(){
		return this.id;
	}
	
	/**
	 * 
	 * @return null if the list is empty or the counter already reached the end of the list.
	 */
	public Microtask getCurrentMicrotask(){
		if(!hasCurrent())
			return null;
		else
			return this.microtaskList.get(currentIndex);
	}

	/** 
	 * 
	 * @return the position for the current microtask in the list. The position can be out of the range of the list. For that, use the method hasCurrent(); 
	 */
	public Integer getCurrentIndex(){
		return this.currentIndex;
	}

	public Vector<Microtask> getMicrotaskList() {
		return this.microtaskList;
	}
	
	/**
	 * 
	 * @param microtaskId
	 * @param answer
	 * @param elapsedTime 
	 * @param timeStamp 
	 * @return true if successful, otherwise false. In case of success, the microtask counter is incremented.
	 */
	public boolean insertMicrotaskAnswer(Integer microtaskId, Answer answer) {
		
		Microtask microtask = this.getCurrentMicrotask();
		if((microtask == null) || (microtask.getID().intValue() != microtaskId.intValue())){
			logger.error("EVENT= ERROR; workerId="+answer.getWorkerId()+ ";Answer was already stored; microtaskId="+ microtaskId+ "; answer="+ answer.getOption() );
			return false;
		}
		else{
			microtask.addAnswer(answer);
			this.storeCurrentMicrotask(microtask);
			return true;
		}
	}

	public void setWorkerId(String id) {
		this.workerId = id;		
	}

	public String getWorkerId() {
		return this.workerId;		
	}

	public String getTotalElapsedTime() {
		double totalTime = 0;
		for(Microtask microtask: microtaskList){
			Answer answer = microtask.getAnswerByUserId(this.workerId);
			if(answer!=null)
				totalTime = totalTime + new Double(answer.getElapsedTime());
		}
		return Double.toString(totalTime);
	}
	
}
