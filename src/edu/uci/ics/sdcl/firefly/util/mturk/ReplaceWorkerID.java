package edu.uci.ics.sdcl.firefly.util.mturk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Replaces workerIDs with consolidated workerIDs
 * This is done in order to guarantee that a same Mechanical Turk worker has the same 
 * worker ID among different log files.
 * 
 * @author adrianoc
 */
public class ReplaceWorkerID {
	
	StringBuffer buffer = new StringBuffer();
	MTurkSessions checker;
	ReplaceQuitSessionID quitReplacer;
	LogReadWriter logReadWriter;
	
	public ReplaceWorkerID(){
		checker = new MTurkSessions();
		this.quitReplacer = new ReplaceQuitSessionID();
		this.logReadWriter = new LogReadWriter();
	}

	
	public void replace(HashMap<String, Turker> hitsMapAll){
		this.replaceIDSessionLogs(hitsMapAll);
		this.replaceIDConsentLogs(hitsMapAll);
	}

	
	private void replaceIDSessionLogs(HashMap<String, Turker> hitsMapAll){

		for(int i=0; i<8;i++){
			HashMap<String,String> queryReplace = this.obtainTurkersInRunSession(new Integer (i+1).toString(),hitsMapAll);

			String sessionLogFile = this.checker.crowddebugLogs[i];
			ArrayList<String> buffer = logReadWriter.readToBuffer(3,sessionLogFile);

			System.out.println("replacing at:" +i+": buffer size: "+buffer.size() + ": replace list: "+queryReplace.size());
			ArrayList<String> newBuffer = replace(buffer,queryReplace);

			String destFileName = sessionLogFile.substring(0,sessionLogFile.indexOf("."));
			destFileName = destFileName+"_curated.txt";

			logReadWriter.writeBackToBuffer(newBuffer, 3, destFileName);
		}
	}

	
	private ArrayList<String> replace(ArrayList<String> buffer,HashMap<String,String> queryReplace ){

		ArrayList<String> newBuffer = (ArrayList<String>) buffer.clone();
		for(String originalID:queryReplace.keySet()){
			String finalID = queryReplace.get(originalID);
			for(int i=0; i<buffer.size(); i++){
				String line = buffer.get(i); 
				if(line.contains(originalID)){
					line = line.replaceAll(originalID,finalID);
					newBuffer.set(i, line);
				}
			}
		}
		return newBuffer;
	}

	private HashMap<String,String> obtainTurkersInRunSession(String runSessionNumber, HashMap<String, Turker> hitsMapAll){

		//Index is the ID to query and the value is the replace 
		HashMap<String,String> queryReplace = new HashMap<String,String>();

		System.out.println("hitsMapAll.size():"+hitsMapAll.size());
		
		for(String turkerStr : hitsMapAll.keySet()){
			Turker turker = hitsMapAll.get(turkerStr);
			String workerID = turker.runWorkerIDMap.get(runSessionNumber);
			if(workerID!=null){
				queryReplace.put(workerID, turker.turkerFinalWorkerID);
			}
		}
		return queryReplace;
	}



	private void replaceIDConsentLogs(HashMap<String, Turker> hitsMapAll){
		//TODO implement it 
	}

	//-------------------------------------------------------------------------------
	public static void main(String[] args){
		
		ReplaceWorkerID replacer = new ReplaceWorkerID();
		TurkerWorkerMatcher matcher = new TurkerWorkerMatcher();
		HashMap<String, Turker> hitsMapAll = matcher.listTurkersRunSessions();
		replacer.replace(hitsMapAll);
	}
	
}
