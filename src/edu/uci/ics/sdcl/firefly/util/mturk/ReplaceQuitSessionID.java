package edu.uci.ics.sdcl.firefly.util.mturk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileConsentDTO;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileSessionDTO;

/** 
 * Session IDs that were quit are replaced
 * Also removes the SMOKE TEST entries from the logs
 * 
 * @author adrianoc
 *
 */
public class ReplaceQuitSessionID {

	StringBuffer buffer = new StringBuffer();
	MTurkSessions checker;
	LogReadWriter logReadWriter;
	
	public ReplaceQuitSessionID(){
		checker = new MTurkSessions();
		this.logReadWriter = new LogReadWriter();
	}


	public void replaceQuitSessions(){

		for(int i=0;i<8;i++){

			//Obtain list of smokeTest workers
			HashMap<String, Worker> smokeTestWorkerMap = this.listSmokeTestWorkers(checker.crowddebugConsentLogs[i]);
			
			//Remove smokeTest workers from log files
			removeSmokeTestWorkers (smokeTestWorkerMap,checker.crowddebugConsentLogs[i]);
			removeSmokeTestWorkers (smokeTestWorkerMap,checker.crowddebugLogs[i]);		
			
			//Obtain list of quit workers
			HashMap<String, Worker> quitWorkerMap = listQuitWorkers(checker.crowddebugConsentLogs[i]); 

			//Discover which of their sessions are incomplete 
			HashMap<String, String> sessionWorkerIDMap = quitSessions(checker.crowddebugLogs[i], quitWorkerMap);

			//Replace these sessionID occurrences for new sessionIDs
			replaceSessionIDs(sessionWorkerIDMap,checker.crowddebugLogs[i]);
		}
	}

	private HashMap<String, Worker> listSmokeTestWorkers(String consentFileName){
		HashMap<String, Worker> smokeWorkerMap = new HashMap<String, Worker>(); 

		FileConsentDTO dto = new FileConsentDTO(this.logReadWriter.getPath(1)+consentFileName);
		HashMap<String, Worker> workerMap = dto.getWorkers();

		for(Worker worker: workerMap.values()){
			String language = worker.getSurveyAnswer("Language");
			if(language!=null && language.compareTo("SMOKE TEST")==0){
				smokeWorkerMap.put(worker.getWorkerId(), worker);
			}
		}
		return smokeWorkerMap;
	}
	
	
	private void removeSmokeTestWorkers(HashMap<String, Worker> smokeTestWorkerMap,	String logFileName) {
		ArrayList<String> buffer = this.logReadWriter.readToBuffer(1,logFileName);
		
		ArrayList<String> newBuffer = (ArrayList<String>) buffer.clone();
		for(String workerID:smokeTestWorkerMap.keySet()){
			for(int i=0; i<buffer.size(); i++){
				String line = buffer.get(i); 
				if(line.contains(workerID)){
					newBuffer.set(i,"\n");
					//System.out.println("Removing line: "+i+":worker:"+workerID+":worker:"+logFileName);
				}
			}
		}		
		this.logReadWriter.writeBackToBuffer(newBuffer, 1, logFileName);
	}
	
	
	private HashMap<String, Worker> listQuitWorkers(String consentFileName){

		HashMap<String, Worker> quitWorkerMap = new HashMap<String, Worker>(); 

		FileConsentDTO dto = new FileConsentDTO(this.logReadWriter.getPath(2)+consentFileName);
		HashMap<String, Worker> workerMap = dto.getWorkers();

		for(Worker worker: workerMap.values()){
			if(worker.getQuitReasonList() !=null){
				quitWorkerMap.put(worker.getWorkerId(), worker);
				//System.out.println("Quit worker:"+worker.getWorkerId()+":file:"+consentFileName);
			}
		}
		return quitWorkerMap;
	}

	/** List the sessions that have to be marked because they were quit before being finished .
	 * 
	 * @param quitWorkerMap 
	 * @return sessionID, workerID
	 */
	private HashMap<String,String> quitSessions(String sessionFileName, HashMap<String, Worker> quitWorkerMap){
		
		FileSessionDTO dto = new FileSessionDTO(this.logReadWriter.getPath(2)+sessionFileName);
		HashMap<String, WorkerSession> sessionMap = (HashMap<String,WorkerSession>) dto.getSessions();
		
		//
		HashMap<String, String> quitSessionMap = new HashMap<String,String>();
		
		for(Worker worker: quitWorkerMap.values()){
			if(worker.getQuitReasonList()!=null){
				for(WorkerSession session : sessionMap.values()){
					if(session.getWorkerId().compareTo(worker.getWorkerId())==0)
						if(session.getMicrotaskListSize()<3){
							quitSessionMap.put(session.getId(),worker.getWorkerId());
							//System.out.println("Quit: "+worker.getWorkerId()+": session:"+session.getId()+" answers:"+
							//session.getMicrotaskListSize()+":"+sessionFileName+":"+worker.quitFileToString()+":"+worker.quitReasonToString());
						}
				}
			}
		}
		return quitSessionMap;
	}

	/**
	 * 
	 * @param sessionWorkerIDMap <sessionID, workerID>
	 * @param sessionFileName
	 */
	private void replaceSessionIDs(HashMap<String, String> sessionWorkerIDMap, String sessionFileName){
		ArrayList<String> buffer = this.logReadWriter.readToBuffer(2,sessionFileName);

		//System.out.println("replacing at: buffer size: "+buffer.size() + ": replace list: "+sessionWorkerIDMap.size());
		ArrayList<String> newBuffer = replaceQuitSessions(buffer,sessionWorkerIDMap);

		this.logReadWriter.writeBackToBuffer(newBuffer, 2, sessionFileName);
	}


	/** Adds a suffix _q to all sessions in which the worker quit */
	private ArrayList<String> replaceQuitSessions(ArrayList<String> buffer,HashMap<String,String> queryReplace ){

		ArrayList<String> newBuffer = (ArrayList<String>) buffer.clone();
		for(String sessionID:queryReplace.keySet()){
			String workerID = queryReplace.get(sessionID);
			for(int i=0; i<buffer.size(); i++){
				String line = buffer.get(i); 
				if(line.contains(workerID) && line.contains(sessionID)){
					line = line.replaceAll(sessionID,sessionID+"_q");
					newBuffer.set(i, line);
				}
			}
		}
		return newBuffer;
	}

	/** This method consolidates worker IDs for worker who removed cookies,
	 * so they ended having two different workerIDs for the same SessionLog.
	 * 
	 * The list of these workers were manually detected based on conflicts in 
	 * the output generated by function TurkerWorkerMatcher.listTurkersRunSessions()
	 */
	public void replaceID_WorkersRemovedCookies(){
		
		String[] worker1_session28 = {"5Ci2A-8i-29-2", "1582ai0e2I065"};
		String[] worker2_session28 = {"1298II-8i-7e-88-6", "27Ei-8i0A-3-59"};
		
		//Session Log
		ArrayList<String> buffer = this.logReadWriter.readToBuffer(0,checker.crowddebugLogs[1]);
		buffer = replaceSingleWorkerID(buffer, worker1_session28);
		buffer = replaceSingleWorkerID(buffer, worker2_session28);
		this.logReadWriter.writeBackToBuffer(buffer, 0, checker.crowddebugLogs[1]);

		//Consent Log
		buffer = this.logReadWriter.readToBuffer(0,checker.crowddebugConsentLogs[1]);
		buffer = replaceSingleWorkerID(buffer, worker1_session28);
		buffer = replaceSingleWorkerID(buffer, worker2_session28);
		this.logReadWriter.writeBackToBuffer(buffer, 0, checker.crowddebugConsentLogs[1]);
			
	}
	 
	
	private ArrayList<String> replaceSingleWorkerID(ArrayList<String> buffer, String[] replacement){
		
		ArrayList<String> newBuffer = (ArrayList<String>) buffer.clone();
		
		for(int i=0;i<buffer.size();i++){
			String line = buffer.get(i);
			if(line.contains(replacement[0])){
				System.out.println("replacing at: "+i+": line: "+line);
				line = line.replaceAll(replacement[0], replacement[1]);
				newBuffer.set(i, line);
				
			}
		}
		
		return newBuffer;
		
	}
	
	//----------------------------------------------------------------------------------------------


	public static void main(String[] args){
		ReplaceQuitSessionID replacer = new ReplaceQuitSessionID();
		//replacer.replaceID_WorkersRemovedCookies();
		replacer.replaceQuitSessions();
		System.out.println("Done, Quit and SmokeTest processed, check folders");
	}


	//------------------------------------------------------------------------------------
}
