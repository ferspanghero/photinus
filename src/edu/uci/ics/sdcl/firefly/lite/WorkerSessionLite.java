package edu.uci.ics.sdcl.firefly.lite;

public class WorkerSessionLite {

	public int[] microtaskList;
	public String sessionId;
	public int workerId;
	
	public WorkerSessionLite(String sessionId, int[] microtaskList, int workerId){
		this.sessionId = sessionId;
		this.microtaskList = microtaskList.clone();
		this.workerId = workerId;
	}
	
}
