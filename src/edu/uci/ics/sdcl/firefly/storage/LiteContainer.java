package edu.uci.ics.sdcl.firefly.storage;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Microtask;

public class LiteContainer {

	/** Table with Session ID and session */
	public  Hashtable<String, Vector<Microtask>> microtaskTable;
	
	/** List of available sessions */
	public  Vector<String> availableSessionList;
	
	/** Worker ID */
	public int lastWorkerId;
			
	private static LiteContainer container;
	
	public synchronized static LiteContainer initializeSingleton(){
		if(container == null)
			container = new LiteContainer();
		return container;
	}

	private LiteContainer(){}
	
	public void setSessionTable(Hashtable<String, Vector<Microtask>> table){
		microtaskTable = table;
		lastWorkerId=-1;
	}

	public synchronized  Microtask getNextMicrotask(String sessionId, int lastMicrotaskId){
		Vector<Microtask> list = microtaskTable.get(sessionId);
		if(list.size()>0 && list.size()>(lastMicrotaskId+1)){
			return list.elementAt(lastMicrotaskId+1);
		}
		else return null;
	}
	
	public synchronized int getNextWorkerId(){
		this.lastWorkerId++;
		return this.lastWorkerId;
	}
	
	public synchronized String getNewWorkerSession(){
		if(availableSessionList.size()>0){
			String sessionId = this.availableSessionList.lastElement();
			this.availableSessionList.remove(availableSessionList.size()-1);
			return sessionId;
		}
		else
			return null;
	}
	
	
}
