package edu.uci.ics.sdcl.firefly.util.mturk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Turker {

	String turkerID;
	
	String turkerFinalWorkerID;

	/** The number of run session in which the worker participated */
	Integer nonEmptyRunSessions=0;
	
	HashMap<String, String> workerIDMap = new HashMap<String,String>();

	/** SessionID, batchFile */
	HashMap<String, String> sessionBatchMap = new HashMap<String, String>();
	
	/** SessionID, HIT Name */
	HashMap<String, String> sessionMap = new HashMap<String, String>();

	/** RunSession, sessionID (RunSession starts with 1 and goes up to 8) 
	 *  RunSession is every time a new batch of WorkerSessions was generated */
	HashMap<String, ArrayList<String>> runSessionMap = new HashMap<String, ArrayList<String>>();
	
	/** RunSession, workerID (RunSession starts with 1 and goes up to 8)
	 *  RunSession is every time a new batch of WorkerSessions was generated */
	HashMap<String, String> runWorkerIDMap = new HashMap<String, String>();
	
	String workerID = null;


	public Turker(String turkerID, String sessionID, String batchFile){
		this.turkerID = turkerID;
		this.sessionMap.put(sessionID, this.getHITName(batchFile));
		this.sessionBatchMap.put(sessionID,batchFile);
	}


	public void setWorkerID(String id,String sessionLog){

		if(workerID==null)
			this.workerID = id.trim();
		else
			if(!TurkerWorkerMatcher.checkQuit(id))
				System.out.println("ATTENTION: TurkerID: "+turkerID+" already has workerIDs: "+workerID+". Trying to set to: "+ id);
			else
				System.out.println("OK QUIT: TurkerID: "+turkerID+" already has workerID: "+workerID+". But quitter is: "+ id);
		
		workerIDMap.put(id,sessionLog);
	}


	/** Print workerIDs and their sessionLogs 
	 * TurkerID:WorkerID:HIT
	 * */
	public void printTurkerWorkerSessionLog(){
		if(workerIDMap.size()==0)
			System.out.println("No worker ID for turker: "+ turkerID);
		else{
			for(String workerID:  workerIDMap.keySet()){
				String sessionLog = workerIDMap.get(workerID);
				System.out.println(turkerID+":"+workerID+":"+sessionLog);
			}
		}
	}

	
	/** Print the sessionIDs attributed to this Turker 
	 *  "TurkerID:SessionID:HIT";
	 * */
	public void printTurkerSessionHITs(){
		Iterator<String> iter = sessionMap.keySet().iterator();
		while(iter.hasNext()){
			String sessionID = iter.next();
			String HITName = sessionMap.get(sessionID);
			System.out.println(turkerID+":"+sessionID+":"+HITName);
		}
	}

	
	public void addSession(String sessionID, String batchFile) {
		this.sessionMap.put(sessionID, getHITName(batchFile));
		this.sessionBatchMap.put(sessionID,batchFile);
	}

	
	public String getHITName(String batchFileName){

		String firstPart = batchFileName.split("_")[1];
		String secondPart = batchFileName.split("_")[2];

		return firstPart + "_" + secondPart;
	}

	public void consolidadeID(){
		
		String tempID="";
		for(String runWorkerID : this.runWorkerIDMap.values()){
			if(runWorkerID!=null && runWorkerID.length()>0)
				tempID = tempID.concat(runWorkerID+":");
		}
		this.turkerFinalWorkerID = tempID.substring(0, tempID.lastIndexOf(":"));
	}
	

	public static void main(String[] args){

		String batchFileName =  "HIT_01_8_1.csv";
		Turker turker = new Turker("1","002","HIT01_8_1.csv");
		String actual = turker.getHITName(batchFileName);

		if(actual.compareTo("01_8")==0) 
			System.out.println("worked! actual:"+actual);
		else
			System.out.println("Error");

		String test = "id:id2:";
		test = test.substring(0, test.lastIndexOf(":"));
		System.out.println(test);
		
	}
}
