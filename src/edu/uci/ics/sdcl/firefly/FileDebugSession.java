package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map.Entry;

/**
 * Class to persist the information of debugging one file
 * 
 * @author Christian Adriano
 *
 */
public class FileDebugSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fileName;	// with path and extension
	
	private String fileContent;
	
	private Hashtable<Integer, Microtask> microtaskMap;
	
	private Integer maximumAnswerCount; 
	
	
	public FileDebugSession(String fileName, String fileContent, Hashtable<Integer,Microtask> microtaskMap){
		this.microtaskMap =  microtaskMap;
		this.fileName = fileName;
		this.fileContent = fileContent;
	}

	public Hashtable<Integer, Microtask> getMicrotaskMap() {
		return microtaskMap;
	}

	public void setMicrotaskMap(Hashtable<Integer, Microtask> microtaskMap) {
		this.microtaskMap = microtaskMap;
	}

	public String getFileName() {
		return fileName;
	}
	
	public Integer getMaximumAnswerCount() {
		return maximumAnswerCount;
	}
	
	public Microtask getMicrotask(Integer id){
		return this.microtaskMap.get(id);
	}
	
	public void insertMicrotask(Integer id, Microtask mtask){
		this.microtaskMap.put(id, mtask);
	}
	
	public String getFileContent() {
		return fileContent;
	}
	 
	public int getNumberOfMicrotasks(){
		return this.microtaskMap.size();
	}
	
	/** 
	 * If the provided number is smaller then maximumAnswerCount, then 
	 * increment maximumAnswerCount, otherwise, ignore 
	 * @param microtaskAnswersCount the total number of answers a certain microtask
	 *  received (within this debugging session)
	 *  @return true if maximumAnswerCount was actually incremented, otherwise false
	 */
	public boolean incrementAnswersReceived(int microtaskAnswersCount){
		if((maximumAnswerCount==null) || (maximumAnswerCount.intValue()<microtaskAnswersCount)){
			this.maximumAnswerCount = new Integer(microtaskAnswersCount);
			return true;
		}
		else
			return false;
	}
	
	public boolean isEmpty(){
		return (this.microtaskMap == null || this.microtaskMap.isEmpty());
	}

	public void append(FileDebugSession otherSession) {
		if (otherSession == null || otherSession.getMicrotaskMap() == null) {
			throw new IllegalArgumentException("otherSession");			
		}
		
		Hashtable<Integer, Microtask> otherMicrotaskMap = otherSession.getMicrotaskMap();
		int currentMicrotasksCount = this.microtaskMap.size();
		
		for (Entry<Integer, Microtask> entry : otherMicrotaskMap.entrySet()) {
			this.microtaskMap.put(entry.getKey() + currentMicrotasksCount, entry.getValue());
		}
	}
}
