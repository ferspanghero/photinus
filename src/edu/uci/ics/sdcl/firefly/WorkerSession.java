package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;
import java.util.Vector;

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

	private String fileName;

	//private static Logger logger;
	
	/** 
	 * Initializes the array and the counter to the first position in the array 
	 * @param id
	 * @param originalId 
	 * @param microtaskList
	 */
	public WorkerSession(String id, Vector<Microtask> microtaskList){
		//logger = LoggerFactory.getLogger(WorkerSession.class);
		this.id = id;
		this.microtaskList = microtaskList;
		if(this.microtaskList!=null && this.microtaskList.size()>0)
			this.currentIndex = 0; //points to the first element
		else
			this.currentIndex = -1;
	}

	/** 
	 * Constructor for the session which all microtasks are from the same file.
	 * Initializes the array and the counter to the first position in the array 
	 * @param id
	 * @param originalId 
	 * @param fileName file name to which all microtasks are related. 
	 * @param microtaskList
	 */
	public WorkerSession(String id, String fileName, Vector<Microtask> microtaskList){
		//logger = LoggerFactory.getLogger(WorkerSession.class);
		this.id = id;
		this.fileName = fileName;
		this.microtaskList = microtaskList;
		if(this.microtaskList!=null && this.microtaskList.size()>0)
			this.currentIndex = 0; //points to the first element
		else
			this.currentIndex = -1;
	}
	
	public WorkerSession getLightVersion(){
		Vector<Microtask> simpleList = new Vector<Microtask>();
		for(Microtask task: this.microtaskList){
			simpleList.add(task.getSimpleVersion());
		}
		WorkerSession lightSession = new WorkerSession(this.getId(),simpleList);
		lightSession.setWorkerId(this.getWorkerId());
		lightSession.currentIndex = simpleList.size();
		return lightSession;
	}
	
	
	/**
	 * 
	 * @return true if the counter points to a position within the list, otherwise false.
	 */
	public boolean isClosed(){
		if(currentIndex<0 || currentIndex>=microtaskList.size())
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @return true if the counter+1 points to a position within the list, otherwise false.
	 */
	public boolean hasNext(){
		if((this.currentIndex+1)<microtaskList.size())
			return true;
		else
			return false;
	}
	
	/** Sets the currentIndex to zero
	 * This is necessary to make the session available to a new worker.
	 * This happens when the current worker quits in the middle of the session.
	 */
	public void reset(){
		this.currentIndex = 0;
	}
	
	public String getId(){
		return this.id;
	}
	
	public Microtask getCurrentMicrotask(){
		if(isClosed())
			return null;
		else
			return this.microtaskList.get(currentIndex);
	}

	public Microtask getPreviousMicrotask() {
		if(this.currentIndex==0)
			return null;
		else
			return this.microtaskList.elementAt(this.currentIndex-1);
	}
	
	/** 
	 * 
	 * @return the position for the current microtask in the list. The position can be out of the range of the list. For that, use the method hasCurrent(); 
	 */
	public Integer getCurrentIndex(){
		return this.currentIndex;
	}
	
	/** 
	 * 
	 * @return the position for the current microtask in the list. Starting at 1; 
	 */
	public Integer getCurrentIndexPlus(){
		return this.currentIndex + 1; 
	}
	
	public Vector<Microtask> getMicrotaskList() {
		return this.microtaskList;
	}
	
	public Integer getMicrotaskListSize(){
		return this.microtaskList.size();
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
		if((microtask == null) || microtaskId==null || (microtask.getID().intValue() != microtaskId.intValue())){
			return false;
		}
		else{
			microtask.addAnswer(answer);
			this.microtaskList.set(this.currentIndex,microtask);
			this.currentIndex++;
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

	public void setMicrotaskList(Vector<Microtask> list) {
		this.microtaskList = list;	
	}

	public String getFileName() {
		return this.fileName;//Assume that all microtasks are from the same file.
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
}
