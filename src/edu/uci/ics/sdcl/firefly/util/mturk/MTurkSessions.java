package edu.uci.ics.sdcl.firefly.util.mturk;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileConsentDTO;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileSessionDTO;

/**
 * Checks whether the a turkID relates to the correct workerIDs 
 * 
 * To be correct a Worker has the same number of Java methods in SessionLog and in MTurkLogs
 * 
 * @author adrianoc
 *
 */
public class MTurkSessions {


	/** All mturk log file names */
	ArrayList<String[]> mturkAllLogs = new ArrayList<String[]>();

	public MTurkSessions(){

		//one list of MTurk Logs 
		mturkAllLogs.add(this.mturkLogs_S1);  //0
		mturkAllLogs.add(this.mturkLogs_S2);  //1
		mturkAllLogs.add(this.mturkLogs_TP1); //2
		mturkAllLogs.add(this.mturkLogs_TP2); //3
		mturkAllLogs.add(this.mturkLogs_TP3); //4
		mturkAllLogs.add(this.mturkLogs_TP4); //5
		mturkAllLogs.add(this.mturkLogs_TP5); //6
		mturkAllLogs.add(this.mturkLogs_TP6); //7

		//Initialize FileDTOs for sessionLogs

		for(int i=0; i<8;i++){
			workerMapList.add(loadWorkers(this.crowddebugConsentLogs[i]));
		}

		for(int i=0; i<8;i++){
			workerSessionMapList.add(loadSessions(this.crowddebugLogs[i],i));
		}	
	}	

	/** Load all workers in memory */
	private HashMap<String, Worker> loadWorkers (String consentFileName){
		FileConsentDTO dto = new FileConsentDTO(folder+consentFileName);

		return dto.getWorkers();			
	}

	/** Load all workerSessions in memory */
	private HashMap<String, WorkerSession> loadSessions(String sessionFileName, Integer runID){
		FileSessionDTO dto = new FileSessionDTO(folder+sessionFileName);
		return removeQuitSessions(dto.getSessions(),runID);
	}

	/**
	 * Remove sessions in which the worker quit, because these sessions will
	 * be replicated by being taken by a different worker.
	 */
	private HashMap<String, WorkerSession> removeQuitSessions(HashMap<String, WorkerSession> map, Integer runID){

		HashMap<String,WorkerSession> duplicatedSessionMap = new HashMap<String,WorkerSession>();

		HashMap<String, WorkerSession> curatedSessionMap = new HashMap<String, WorkerSession>();

		for(String composedSessionID: map.keySet()){
			String sessionID = composedSessionID.split(":")[0].trim();
			String workerID = composedSessionID.split(":")[1].trim();
			WorkerSession session = map.get(composedSessionID);
			
			if(!isSmokeTest(workerID, runID)){ //Ignore smoke test worker

				if(curatedSessionMap.containsKey(sessionID)){ //Place both conflicting sessions in the duplicatedSession
					duplicatedSessionMap.put(composedSessionID,session);
					WorkerSession prevCuratedSession = curatedSessionMap.get(sessionID);
					duplicatedSessionMap.put(sessionID+":"+prevCuratedSession.getWorkerId().trim(),prevCuratedSession);
					curatedSessionMap.remove(sessionID);

					if(sessionID.compareTo("352eG6G8I245")==0){
						System.out.println("duplicated session:"+sessionID+":worker:"+workerID+":"+prevCuratedSession.getWorkerId());
					}
				}
				else
					curatedSessionMap.put(sessionID, session);
			}
		}

		HashMap<String, WorkerSession> cleanedMap = extractNonQuitWorkers(duplicatedSessionMap, runID);
		for(String composedSessionID: cleanedMap.keySet()){
			String sessionID = composedSessionID.split(":")[0].trim();
			WorkerSession session = map.get(composedSessionID);
			curatedSessionMap.put(sessionID,session);
		}

		return curatedSessionMap;
	}

	/** Test whether the worker as actually a smoke test */
	private boolean isSmokeTest(String workerID, Integer runID){
		HashMap<String, Worker> workerMap = this.workerMapList.get(runID);
		Worker worker = workerMap.get(workerID);		

		if(worker!=null){
			String language = worker.getSurveyAnswer("Language");
			if(language.compareTo("SMOKE TEST")==0){
			//	System.out.println("found smoketest: "+workerID);
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}

	/** Remove workers that actually quit the session, therefore, the microtasks should be attributed to a different worker. */
	private HashMap<String, WorkerSession> extractNonQuitWorkers(HashMap<String, WorkerSession> map, Integer runID){

		ArrayList<String> markToRemove = new ArrayList<String>();

		for(String composedSessionID: map.keySet()){
			String sessionID = composedSessionID.split(":")[0].trim();
			String workerID = composedSessionID.split(":")[1].trim();
			WorkerSession session = map.get(composedSessionID);


			if(isQuitWorker(workerID,runID)){
				markToRemove.add(composedSessionID);
				if(session.getMicrotaskListSize()>0){
					System.out.println("Exceptional case! Quit worker did at least one microtask, workerID:"+ workerID+": sessionID:"+sessionID+": run:"+runID);
				}
			}
		}

		for(String composedID:markToRemove){
			map.remove(composedID);
		}

		return map;
	}

	/** Decides whether the worker quit based on the reason provided (if size is larger than zero)*/ 
	private boolean isQuitWorker(String workerID, Integer runID){
		HashMap<String, Worker> workerMap = this.workerMapList.get(runID);
		Worker worker = workerMap.get(workerID);

		if(worker==null){
			System.out.println("Worker not found in consent file,workerID:"+workerID+":run:"+runID);
			return true;
		}
		else
			if(worker.getQuitReason()!=null){
				//System.out.println("quit reason: "+ workerID+ ":"+worker.getQuitReason());
				return true;
			}
			else
				return false;

	}

	//-------------------------------------------------------------------



	String folder = "C:/firefly/stage/logs/";

	String[] mturkLogs_TP6 = { 
			"HIT_04_7_11_TP6.csv",
			"HIT_08_54_008_TP6.csv",
			"HIT_08_54_b2_008_TP6.csv",
			"HIT_04_7_1_004_TP6.csv"
	};

	String[] mturkLogs_TP5 = { 
			"HIT_04_7_10_TP5.csv"
	};

	String[] mturkLogs_TP4 = {
			"HIT_08_54_5_TP4.csv",
			"HIT_04_7_8_TP4.csv",
			"HIT_03_6_7_TP4.csv",
			"HIT_06_51_7_TP4.csv",
			"HIT_05_35_6_TP4.csv"
	};

	String[] mturkLogs_TP3 = {
			"HIT_03_6_6_v2_TP3.csv",
			"HIT_04_7_7_TP3.csv",
			"HIT_05_35_5_TP3.csv",
			"HIT_06_51_5_TP3.csv",
			"HIT_06_51_6_TP3.csv",
			"HIT_07_33_5_TP3.csv"
	};

	String[] mturkLogs_TP2 = {
			"HIT_03_6_6_v0_TP2.csv",
			"HIT_04_7_7_v1_TP2.csv",
			"HIT_05_35_4_TP2.csv",
			"HIT_06_51_4_TP2.csv",
			"HIT_07_33_4_TP2.csv",
			"HIT_08_54_4_TP2.csv"
	};

	String[] mturkLogs_TP1 = {
			"HIT_01_8_3_TP1.csv",
			"HIT_03_6_5_TP1.csv",
			"HIT_04_7_6_TP1.csv",
			"HIT_05_35_b5_TP1.csv",
			"HIT_06_51_b3_TP1.csv",
			"HIT_07_33_b3_TP1.csv",
			"HIT_08_54_b3_TP1.csv"
	};

	String[] mturkLogs_S1 = {
			"HIT_01_08_1.csv",
			"HIT_02_24_1.csv",
			"HIT_03_6_1.csv",
			"HIT_04_7_1.csv",		
	};

	String[] mturkLogs_S2 = {
			"HIT_05_35_2.csv",
			"HIT_06_51_2.csv",
			"HIT_07_33_2.csv",
			"HIT_02_24_2.csv",
			"HIT_03_6_2.csv",
			"HIT_01_8_2.csv",
			"HIT_08_54_1.csv",
			"HIT_08_54_3.csv",
			"HIT_06_51_1.csv",
			"HIT_04_7_5.csv"
	};	

	//--------------------------------------------------------------------------------------

	// LOGS from CrowdDebug
	String[] crowddebugLogs = { 
			"session-log-6.log",   //S1 run-1
			"session-log-28.log",  //S2 run-2
			"session-log-35.log",  //TP1 run-3
			"session-log-43.log",  //TP2 run-4
			"session-log-46.log",  //TP3 run-5
			"session-log-54.log",  //TP4 run-6
			"session-log-58.log",  //TP5 run-7
			"session-log-64.log"   //TP6 run-8
	};


	// LOGS from CrowdDebug
	String[] crowddebugConsentLogs = { 
			"consent-log-6.log",   //S1 run-1
			"consent-log-28.log",  //S2 run-2
			"consent-log-35.log",  //TP1 run-3
			"consent-log-43.log",  //TP2 run-4
			"consent-log-46.log",  //TP3 run-5
			"consent-log-54.log",  //TP4 run-6
			"consent-log-58.log",  //TP5 run-7
			"consent-log-64.log"   //TP6 run-8
	};

	//Consolidate logs
	String crowdLog_TP123456 = "session-log-35-43_46_54_58_64_TOPUPs.txt";

	String crowdLog_S1S2 = "session-log-S1-S2.txt";	


	ArrayList<HashMap<String,WorkerSession>> workerSessionMapList =new ArrayList<HashMap<String, WorkerSession>>();

	ArrayList<HashMap<String,Worker>> workerMapList =new ArrayList<HashMap<String, Worker>>();
}
