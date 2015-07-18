package edu.uci.ics.sdcl.firefly.util.mturk;

import java.util.Collection;
import java.util.HashMap;

public class Turker {

	String turkerID;
	
	String workerID=null;
	
	/** SessionID, batchFile */
	HashMap<String, String> sessionMap = new HashMap<String, String>();
	
	public Turker(String turkerID, String sessionID, String batchFile){
		this.turkerID = turkerID;
		this.sessionMap.put(sessionID, batchFile);
	}
	
	public void setWorkerID(String id){
		if(workerID==null)
			this.workerID=id.trim();
		else
			if(workerID.compareTo(id)!=0){
				if(!TurkerWorkerMatcher.checkQuit(id))
					System.out.println("ATTENTION: TurkerID: "+turkerID+" already has workerID: "+workerID+". Trying to set to: "+ id);
				else
					System.out.println("OK QUIT: TurkerID: "+turkerID+" already has workerID: "+workerID+". But quitter is: "+ id);
			}
	}
	
	public void print(){
		System.out.println("TurkerID:WorkerID:SessionID");
		Collection<String> collection = sessionMap.values();
		for(String workerID: collection){
			String sessionID = sessionMap.get(workerID);
			System.out.println(turkerID+":"+workerID+":"+sessionID);
		}
	}

	public void addSession(String sessionID, String batchFile) {
		this.sessionMap.put(sessionID, batchFile);
	}
	
}
