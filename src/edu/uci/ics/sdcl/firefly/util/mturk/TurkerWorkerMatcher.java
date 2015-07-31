package edu.uci.ics.sdcl.firefly.util.mturk;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileSessionDTO;

/** Utility class to process the logs generated in the 
 * experiment run of July 2015.
 * 
 * Objectives:
 * Consolidate different workerIDs from same turkerID
 * Differentiate sessionIDs with the name number but related to different sessions 
 * 
 * Load all Turkers with their Worker ID/BatchFile
 * If two turkers have the same workerID, than replace their IDs in the Logs
 * 
 * @author adrianoc
 *
 */
public class TurkerWorkerMatcher {

	//Alls maps, one indexed by turker other indexed by sessionsIDs
	HashMap<String, Turker> hitsMapAll = new HashMap<String, Turker>(); 
	HashMap<String, HashMap<String,Turker>>allTurkSessionsMap = new HashMap<String, HashMap<String,Turker>>();

	//Matching sessions maps.
	HashMap<String, Turker> hitsMap1 = new HashMap<String, Turker>(); 
	HashMap<String, Turker> hitsMap2 = new HashMap<String, Turker>();
	HashMap<String, WorkerSession> sessionMap = new HashMap<String, WorkerSession>();

	MTurkSessions checker; 
	LogReadWriter logReadWriter;
	
	private HashMap<String, HashMap<String, WorkerSession>> workerSessionsMap;

	private HashMap<String, HashMap<String, WorkerSession>> missinHITSessionMap;
	
	/** Constructor */
	public TurkerWorkerMatcher(){
		this.checker = new MTurkSessions();
		this.logReadWriter = new LogReadWriter();
	}

	//-----------------------------------

	public static void main(String[] args){

		TurkerWorkerMatcher matcher = new TurkerWorkerMatcher();

		matcher.listTurkersRunSessions();
	}

	/** 
	 * Look at each workerID to see if it is associated with two different turkers.
	 * Prints the results in the console.
	 */
	public void checkTurkerHasSameWorkerID(){

		this.loadHITs(checker.mturkLogs_S1,checker.mturkLogs_S2);

		this.matchWorkerIDs(checker.crowddebugLogs[0],hitsMap1);
		this.matchWorkerIDs(checker.crowddebugLogs[1],hitsMap2);

		this.printTurkersWithSameWorkerID();//Session (1,2,TP1, TP2, etc.), same workerID:  (TurkerID1:WorkerSession , TurkerID2:WorkerSession)
	}

	/**
	 * Look at same turker and check if this turker is associated with different workerIDs in different sessionLogs
	 * Prints the results in the console.
	 * 
	 * Order of checking: Session (TP6 x TP5, TP65 x TP4, TP654x TP3, TP6453x TP1, TP x S1, TPS x S2)
	 * 
	 * Order of checking: S1 x S2
	 */
	public void checkSameTurkerWithDifferentWorkerID(){

		this.loadHITs(checker.mturkLogs_S1,checker.mturkLogs_S2);

		this.matchWorkerIDs(checker.crowddebugLogs[0],hitsMap1);
		this.matchWorkerIDs(checker.crowddebugLogs[1],hitsMap2);

		this.printTurkerWithDifferentWorkerID();
	}

	/** 
	 * Checks if:
	 * - the code in the HIT exists in the session-log
	 * - the worker associated with that code in the session-log is not associate with other codes 
	 * that are actually from different workers 
	 */
	public void checkHITLogConsistency(){

		this.loadAllHITs();

		this.hitsMap1 = loadHIT(this.checker.mturkLogs_S1);

		this.loadSession(this.checker.crowddebugLogs[0]);

		this.matchWorkerIDs(checker.crowddebugLogs[0],this.hitsMap1);

		this.matchSessions(this.hitsMap1);

		this.findMissingHITSessions();

		checkAllNotClaimed();
	}


	/** 
	 * List all turkers who have HITs across session logs.
	 */
	public HashMap<String, Turker> listTurkersRunSessions(){

		this.loadAllHITs();

		//For each turker, check if turker has 
		for(String turkerStr : hitsMapAll.keySet()){
			Turker turker = hitsMapAll.get(turkerStr);

			int nonEmptyRunSessions = 0;
			int runSession = 0;
			for(int i=0; i<checker.mturkAllLogs.size();i++) {
				runSession++;
				String[] turkLogs = checker.mturkAllLogs.get(i);
				ArrayList<String> turkerHITList = getHITList(turker.turkerID, turkLogs);
				if(turkerHITList.size()>0)
					nonEmptyRunSessions++;
				//gets both the runSession number as the HITs in which the worker participated
				turker.runSessionMap.put(new Integer(runSession).toString(),turkerHITList);					
			}
			turker.nonEmptyRunSessions = nonEmptyRunSessions;
			hitsMapAll.put(turkerStr, turker);// saves turker back to the list
		}
		populateTurkerWorkerIDs();
		consolidateTurkerIDs();
		//printRunWorkerIDMap();
		printTurkerRunSessions();
		return this.hitsMapAll;
	}

	/** For each turker
	 * Obtain the sessionID associated with at a HITName (uses the Turker.runSessionMap)
	 * Obtains the sessionLog corresponding to the run
	 * Opens the log and extracts the workerID associated with the sessionID 
	 * Instantiates a new datastructure in Turker.runWorkerIDMap
	 */
	private void populateTurkerWorkerIDs(){

		//For each turker, check if turker has 
		for(String turkerStr : hitsMapAll.keySet()){
			Turker turker = hitsMapAll.get(turkerStr);

			for(String runID: turker.runSessionMap.keySet()){

				ArrayList<String> sessionIDList = turker.runSessionMap.get(runID);
				if(sessionIDList.size()>0){
					String sessionID = sessionIDList.get(0); // The first element is sufficient to obtain the workerID.
					Integer runIDInt = new Integer(runID);
					HashMap<String, WorkerSession> workerSessionMap = checker.workerSessionMapList.get(runIDInt-1);
					WorkerSession session = workerSessionMap.get(sessionID);
					
					if(session!=null){
						String workerID = session.getWorkerId();
						turker.runWorkerIDMap.put(runID, workerID);
					}
					else{
						System.out.println("Session is null! Should not be, sessionID:"+sessionID);
					}
				}
			}
		}
		
	}

	private void printRunWorkerIDMap(){

		for(Turker turker : this.hitsMapAll.values()){
			System.out.println();
			System.out.print(turker.turkerID+";"+turker.nonEmptyRunSessions+";"+turker.turkerFinalWorkerID+";");

			for(int i=1;i<9;i++){
				String workerID = turker.runWorkerIDMap.get(new Integer(i).toString());
				if(workerID==null)
					System.out.print(i+";-");
				else{
					System.out.print(i+";");
					System.out.print(workerID);
				}
				System.out.print(";");
			}
		}	
	}

	
	private void consolidateTurkerIDs(){
		for(String turkerStr : hitsMapAll.keySet()){
			Turker turker = hitsMapAll.get(turkerStr);
			turker.consolidadeID();
			hitsMapAll.put(turkerStr,turker);
		}
	}

	private ArrayList<String> getHITList(String turkerID, String[]turkerLogs){

		ArrayList<String> HITList = new ArrayList<String>();

		HashMap<String, Turker> hitMap = this.loadHIT(turkerLogs);

		for(String turkID : hitMap.keySet()){
			if(turkID.compareTo(turkerID)==0){
				Turker turker = hitMap.get(turkID);
				HITList.addAll(turker.sessionMap.keySet());
			}
		}	
		return HITList;
	}

	/**
	 * Prints for each turkerID the following string
	 * The HITnames are within curly brackets.
	 * 
	 * TurkerID:1:{}2:{}:3:{}:4:{}:5:{}:6{}:7:{}:8:{}
	 */
	private void printTurkerRunSessions(){

		for(Turker turker : this.hitsMapAll.values()){
			System.out.print(turker.turkerID+":"+turker.nonEmptyRunSessions+":");

			for(int i=1;i<9;i++){
				ArrayList<String> list = turker.runSessionMap.get(new Integer(i).toString());
				if(list==null)
					System.out.print(i+":{}:");
				else{
					System.out.print(i+":{");
					for(String hitname: list){
						System.out.print(hitname+ ",");
					}
					System.out.print("}:");
				}
			}
			System.out.println();
		}
	}



	public HashMap<String, Turker> loadHIT(String[] sessionLog){

		HashMap<String, Turker> hitMap = new HashMap<String,Turker>();

		for(String batchFile: sessionLog){																	
			hitMap= importHITCodes(batchFile, hitMap);		
		}
		return hitMap;
	}

	public void loadHITs(String[] firstLogs, String[] secondLogs){

		for(String batchFile: firstLogs){
			this.hitsMap1= importHITCodes(batchFile, hitsMap1);		
		}

		for(String batchFile: secondLogs){
			this.hitsMap2= importHITCodes(batchFile, hitsMap2);		
		}
	}

	public void loadSession(String crowdLog){

		FileSessionDTO sessionDTO = new FileSessionDTO(this.logReadWriter.getPath(2)+crowdLog);
		this.sessionMap = (HashMap<String, WorkerSession>) sessionDTO.getSessions();
		buildWorkerSessionsMap();
	}


	/** Builds a map with the following structure: 
	 *  Key: workerID
	 *  Value: HashMap<sessionID,session>
	 */
	private void buildWorkerSessionsMap(){

		this.workerSessionsMap = new HashMap<String, HashMap<String,WorkerSession>>();
		for(WorkerSession session: sessionMap.values()){
			String workerID = session.getWorkerId();
			HashMap<String,WorkerSession> map = this.workerSessionsMap.get(workerID);
			if(map==null)
				map = new HashMap<String,WorkerSession>();
			map.put(extract(session.getId()), session);
			this.workerSessionsMap.put(workerID, map);			
		}
	}


	public void matchSessions(int firstSession, int secondSession){

		matchWorkerIDs(checker.crowddebugLogs[firstSession],hitsMap1); //Load the sessions and populate turkers with respective workerIds.
		matchWorkerIDs(checker.crowddebugLogs[secondSession],hitsMap2);
	}


	private void matchWorkerIDs(String crowdLog, HashMap<String, Turker> hitsMap){

		for(Turker turker: hitsMap.values()){
			for(String sessionID: turker.sessionMap.keySet()){
				//System.out.println("Turker sessionID entered:"+sessionID);
				String workerID = retrieveWorkerID(sessionMap, sessionID.trim());
				if(workerID!=null && !this.workerHasExtraSessions(workerID, turker)){ //For merging, I will have to drop the workerHasExtraSessions condition.
					System.out.println("MATCH, turkerID:"+turker.turkerID+": workerID:"+workerID+": sessionID:"+sessionID );
					turker.setWorkerID(workerID,crowdLog);
					hitsMap.put(turker.turkerID, turker);
				}
				else{
					System.out.println("NOT FOUND VIABLE SESSION! turkerID:"+turker.turkerID+": sessionID:"+sessionID );
				}
			}
		}
	}


	/** Look for the sessionID that matches the workerID */
	private String retrieveWorkerID(HashMap<String, WorkerSession> sessionMap,
			String sessionID) {

		for(String composedSessionID: sessionMap.keySet()){
			//System.out.println("composedSessionID: "+composedSessionID);
			String sessionIDpart = composedSessionID.split(":")[0].trim();
			String workerIDpart = composedSessionID.split(":")[1].trim();

			if(sessionID.compareTo(sessionIDpart)==0){
				if((sessionMap.get(composedSessionID).getMicrotaskListSize()>0)){
					return workerIDpart;
				}
				else{
					System.out.println("EMPTY SESSION: "+sessionID);
					return workerIDpart;
				}
			}
		}
		return null;
	}


	public void matchSessions(HashMap<String, Turker> hitsMap){

		//Get each turker list the sessions 
		for(Turker turker: hitsMap.values()){

			//Accumulate all sessions in the log that are associated to a workerID
			HashMap<String, WorkerSession> foundSessionsInLog = new HashMap<String, WorkerSession>();
			HashMap<String, String> workerWithInvalidSession = new HashMap<String,String>();


			for(String turkWorkerID: turker.workerIDMap.keySet()){

				HashMap<String, WorkerSession> logSessionsMap = this.workerSessionsMap.get(turkWorkerID);
				if(logSessionsMap==null){
					workerWithInvalidSession.put(turker.turkerID,turkWorkerID);
					System.out.println("NOT FOUND IN LOGS! turkerID:"+turker.turkerID+": turkWorkerID:"+turkWorkerID);
				}
				else{ 
					//Accumulate all sessions found for that workerID 
					for(WorkerSession logSession : logSessionsMap.values()){ 
						foundSessionsInLog.put(extract(logSession.getId()), logSession );
					}
				}
			}


			//Now compare the sessions accumulated against the sessions in the hitMap 
			for(String turkSession: turker.sessionMap.keySet()){
				WorkerSession logSession = foundSessionsInLog.get(turkSession);
				if(logSession==null){
					System.out.println("Mismatching Session! turkerID:"+turker.turkerID+
							": turkSession:"+turkSession);
				}
				else
					foundSessionsInLog.remove(turkSession);
			}

			if(foundSessionsInLog.isEmpty()){
				turker.printTurkerWorkerSessionLog();
			}
			else{
				System.out.print("Turker incomplete, sessions not found in sessionLog HITMaps, turkerID :"+turker.turkerID+":");
				for(String sessionID: foundSessionsInLog.keySet()){
					System.out.print(sessionID+":");	
				}	
				System.out.println();
				System.out.print("Turker incomplete, sessions not found in sessionLog HITMaps, turkerID :"+turker.turkerID+":");
				for(String sessionID: foundSessionsInLog.keySet()){
					WorkerSession session = foundSessionsInLog.get(sessionID);
					System.out.print(session.getFileName()+":");	
				}	
				System.out.println();


				System.out.print("Turker incomplete, sessions not found in sessionLog HITMaps, turkerID :"+turker.turkerID+":");
				for(String sessionID: foundSessionsInLog.keySet()){
					WorkerSession session = foundSessionsInLog.get(sessionID);
					System.out.print(session.getWorkerId()+":");	
				}	
				System.out.println();

				//Save the missing sessions to be inspected lated.
				addMissingHITSessions(turker.turkerID, foundSessionsInLog);
			}	
		}
	}

	private void addMissingHITSessions(String turkerID,
			HashMap<String, WorkerSession> foundSessionsInLog) {

		if (this.missinHITSessionMap == null)
			this.missinHITSessionMap = new HashMap<String,HashMap<String, WorkerSession>>();

		this.missinHITSessionMap.put(turkerID, foundSessionsInLog);
	}


	/** Search in the AllHITs for sessions that were in the log, but were not in the HITs of Sessions-2
	 * 
	 * If not found, then turker did not reclaimed the sessionID
	 * If found a session in a HIT, check if it is from the worker. 
	 *	 If positive, has to merge worker code from Session-2 with the session corresponding to that HIT.
	 * 			(I am not merging now, just checking)
	 * 	 If negative, then turker did not reclaimed that ID
	 * 
	 * 
	 */
	public void findMissingHITSessions(){

		if(missinHITSessionMap!=null)
			for(String turkerID: this.missinHITSessionMap.keySet()){
				HashMap<String,WorkerSession> missingLogSessionsMap = this.missinHITSessionMap.get(turkerID);
				for(String logSessionID: missingLogSessionsMap.keySet()){
					WorkerSession logSession = missingLogSessionsMap.get(logSessionID);
					if(!this.allTurkSessionsMap.containsKey(logSessionID)){
						System.out.println("Not reclaimed, turkerID: "+ turkerID+":" +logSessionID+":"+logSession.getFileName());
					}
					else{
						HashMap<String, Turker> turkerMap = this.allTurkSessionsMap.get(logSessionID);
						if(!turkerMap.containsKey(turkerID)){
							System.out.println("Not reclaimed, turkerID: "+ turkerID+":" +logSessionID+":"+logSession.getFileName());
						}
						else{
							Turker foundTurker = turkerMap.get(turkerID);
							String batchFile = foundTurker.sessionBatchMap.get(logSessionID);
							System.out.println("Found ELSEWHERE logSession, turkerID: "+ turkerID+":" +logSessionID+":"+batchFile);
						}
					}
				}
			}
	}

	/** Check whether a worker has any extra session that is not in the HIT list of a Turker 
	 * @param hitsMap */
	private boolean workerHasExtraSessions(String workerID, Turker turker){

		HashMap<String,WorkerSession> workerSessionMap = this.workerSessionsMap.get(workerID);
		for(WorkerSession composedSession: workerSessionMap.values()){
			String workerSessionID = extract(composedSession.getId());
			if(!turker.sessionMap.containsKey(workerSessionID)){

				if((composedSession.getMicrotaskListSize()<3)) //Disconsider empty or incomplete sessions. 
					return false;
				else
					if	(this.sessionID_Not_ClaimedByOtherTurkers(turker, workerSessionID))//Disconsider sessions that were not claimed by others (possibly worker did extra session), 
						return false;  		//However, it might be ok even if a worker in a different logSession had claimed the same sessionID, the test in place now to check whether the 
				//turker claimed the HIT in the wrong logSession.
					else{
						System.out.println("Invalid worker for turker:"+turker.turkerID+": worker:"+workerID+": session:"+workerSessionID+" HIT:"+composedSession.getFileName());
						return true;
					}
			}
		}
		return false;

	}


	private boolean sessionID_Not_ClaimedByOtherTurkers(Turker turker, String sessionID){
		HashMap<String, Turker> turkerMap = allTurkSessionsMap.get(sessionID);
		if(turkerMap==null)//no turker claimed the session
			return true;
		else
			if(turkerMap.size()<=1) //the only turker claiming it is the one being questioned 
				return true;
			else 
				return false;
	}

	private String extract(String composedSessionID){
		return composedSessionID.split(":")[0].trim();
	}



	//-----------------------------------------------------------------------------------------
	// PRINTING METHODS

	public void loadAllHITs(){

		for(String[] batchFileList : checker.mturkAllLogs)
			for(String batchFile: batchFileList)
				this.hitsMapAll= importHITCodes(batchFile, this.hitsMapAll);		

		System.out.println("Total HITs imported: "+this.hitsMapAll.size());
	}

	public void printToFileAllTurkerHITs(){

		HashMap<String, Turker> allHITMap = new HashMap<String,Turker>();
		for(String batchFile: checker.mturkLogs_S2){
			allHITMap= importHITCodes(batchFile, allHITMap);		
		}

		System.out.println("TurkerID:HIT, number of workers: "+ allHITMap.size());
		for(Turker turker: allHITMap.values()){
			turker.printTurkerSessionHITs();
		}
	}

	public void printTurkerWithDifferentWorkerID() {

		for(Turker turker1: this.hitsMap1.values()){
			Turker turker2 = this.hitsMap2.get(turker1.turkerID);
			if(turker2!=null && turker1!=null && turker2.workerID!=null && turker1.workerID!=null){
				if(turker2.workerID.compareTo(turker1.workerID)!=0){
					System.out.println("*** Disambiguate workerID:"+turker1.workerID+" for: "+turker1.turkerID+" and workerkID:" +turker2.workerID+" for: "+turker2.turkerID);
				}
				else{//turker is in both RUNs, but has different workerIDs. We need to consolidate to one.
					if(turker1.workerID.compareTo(turker2.workerID)!=0){
						System.out.println("!!! Consolidate TurkerID:"+turker1.turkerID+": workerID1:"+turker1.workerID+": workerID2:"+turker2.workerID);
					}
					else{
						System.out.println("OK in both RUNS - TurkerID:"+turker1.turkerID+": workerID1:"+turker1.workerID);
					}
				}
			}
			else{
				//System.out.println("turkerID: "+turker1.turkerID+" NOT present in both RUNS");
			}
		}
	}


	public void printTurkersWithSameWorkerID() {

		for(Turker turker1: this.hitsMap1.values()){
			for(Turker turker2: this.hitsMap2.values()){
				if(turker1.workerID!=null && turker2.workerID!=null)
					if((turker1.turkerID.compareTo(turker2.turkerID)!=0) && (turker1.workerID.compareTo(turker2.workerID)==0)){
						System.out.println("??? Disambiguate workerID:"+turker1.workerID+" for: "+turker1.turkerID+" and workerkID:" +turker2.workerID+" for: "+turker2.turkerID);
					}
			}		
		}
	}


	/**
	 * Build a map of TurkerIDs and the Turker entered in Mechanical Turk
	 * @param path
	 * @return
	 */
	public HashMap<String, Turker> importHITCodes(String batchFile, HashMap<String, Turker> hitsMap){

		int turkerIDPos = 0;
		int sessionIDPos = 1;
		String path = checker.folder_MTurkLogs +  batchFile;

		HashMap<String, String> codeMap = new HashMap<String, String>();

		BufferedReader log = null;
		try {

			//System.out.println("Batch: "+path);
			log = new BufferedReader(new FileReader(path));
			String line = log.readLine(); //discards first line

			if(line==null){ 
				log.close();
				return null;
			}

			if(line.split(",").length >2){
				turkerIDPos = 17;
				sessionIDPos = 29;
			}
			else{
				turkerIDPos = 0;
				sessionIDPos = 1;
			}	

			int repetition=1;
			String repeated="repeated";
			while ((line = log.readLine()) != null) {
				if(line.equals(""))
					continue;

				String turkerID = line.split(",")[turkerIDPos].trim();
				//System.out.println(turkerID);
				String code = line.split(",")[sessionIDPos].trim(); //completion code entered by the worker
				Turker turker;
				//System.out.println(turkerID+":"+code);


				if(hitsMap.containsKey(turkerID)){
					turker = hitsMap.get(turkerID);
					turker.addSession(code,batchFile);
				}
				else
					turker = new Turker(turkerID, code, batchFile);

				hitsMap.put(turkerID, turker);


				//Update allTurkSessionMap
				if(this.allTurkSessionsMap.containsKey(code)){
					HashMap<String, Turker> turkerMap = this.allTurkSessionsMap.get(code);
					turkerMap.put(turkerID, turker);
					this.allTurkSessionsMap.put(code, turkerMap);
				}
				else{
					HashMap<String, Turker> turkerMap = new HashMap<String, Turker>();
					turkerMap.put(turkerID, turker);
					this.allTurkSessionsMap.put(code, turkerMap);
				}

				//Check if same code was used by more than worker
				if(codeMap.containsKey(code)){
					System.out.println("repeated: "+turkerID+":"+code);
					codeMap.put(code + "_"+ repeated+"_"+repetition, turkerID);
					repetition++;
				}
				else
					codeMap.put(code, turkerID);

			}
			log.close();
			return hitsMap;

		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Could not open log file: "+ path);
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.out.println("ERROR: Could not read log file: "+ path);
			e.printStackTrace();
			return null;
		}
	}




	//----------------------------------------------------------------------------------------------------------------------------------

	public static boolean checkQuit(String workerID){
		for(String id: quitWorkerID_S2){
			if(id.compareTo(workerID)==0) return true;
		}
		return false;
	}


	public static boolean checkAllNotClaimed(){

		TurkerWorkerMatcher matcher = new TurkerWorkerMatcher();
		matcher.loadAllHITs();
		boolean flag= true;
		for(String sessionID:notClaimedIDs ){
			if(matcher.allTurkSessionsMap.containsKey(sessionID)){
				System.out.println("actually claimed: "+sessionID);
				flag = false;
			}
		}
		return flag;
	}


	public static String[] quitWorkerID_S1 = {
		"66GI-3E0i-4-83",
		"96GC-5a-2g0-58",
		"193II5A4G917",
		"265Gc5A-5I0-48",
		"298Ic-9A0E8-80",
		"335Ia3A9c161",
		"373iG0E3C-18-8",
		"406cI-5A0g-91-1",
		"429EC-1I-1e-77-9",
		"350gG8A3I-31-3",
		"384EI-4i7I-5-2-6",
		"551Cg7C1A-2-1-4",
		"550ii2G3e-89-1",
		"561ag2A0g9-3-8",
		"598Ga7E5c-5-9-4",
		"604Cc-7a0e3-5-9",
		"594eA-4G9a-80-2",
		"611Ee-3C-4G-903",
		"628ce-1a8I-9-7-9",
		"693ag0C9I-13-6",
		"776eE-1G6a-274",
		"779ia2G-6e4-2-1",
		"766gc-6i2i-65-9",
		"804ig5I6g6-4-9",
		"855ce-6e-8A-7-3-2",
		"886Ag6C-1a-2-54",
	};

	public static String[] quitWorkerID_S2 = {
		"0ie7A3G-3-8-7",
		"119CC0g1g7-24",
		"124aA0a-6I5-2-9",
		"41eI0e3i-806",
		"145GG9e7E9-7-5",
		"138iA3e-9A-85-8",
		"86GA-3G-4C8-5-2",
		"183Ei0I-4c-5-67",
		"212GA0e-7A-925",
		"92ea8E5i07-8",
		"241aC5A9C120",
		"225ca6i-4G404",
		"264aa2G-6g-4-12",
		"92ea8E5i07-8",
		"293GC-8A2g-1-51",
		"290Ea0C1E82-6",
		"344aG4i-5e-6-93",
		"378Ie4a4c0-3-7",
		"388eg-9A4C1-3-2",
		"207GI-1i3I004",
		"397Ea0E1e-761",
		"432ag-4C6c0-42",
		"333aA2c-1a4-28",
		"465ie-9I5c11-4",
		"599GE-7I6c0-4-6",
		"584cC0A4G-821",
		"623ei-3i-6G-1-11",
		"604Ic-1I5i105",
		"678eg6E-1i620",
		"704gE-1E5a-815",
		"711CI1g-7G-6-6-5",
		"715Ca-6I-2i71-8",
		"785Ge4A-9I8-90",
		"825GA-3i-2c-4-3-1",
		"883ae6c-1C1-3-3",
		"917AE6G-1e-186",
		"909Ee-1C5I-65-5",
		"537Cg0e-7E-5-68",
		"976aG2C3g419",
		"92ea8E5i07-8",
		"1016IA6C1E13-1",
		"1036GI8I-8e6-30",
		"86GA-3G-4C8-5-2",
		"1070Cc3A-8C-6-32",
		"1057Ce-9i-9E-105",
		"1115GC-8i-4i282",
		"1129GE7I6i339",
		"1139Ei9a-7g-98-4",
		"1150cG-4E-2c808",
		"1144aG9I5E-609",
		"1164Ac-1A6E-19-8",
		"1192ei5a-8c530",
		"1199gG-2a-9I-60-7",
		"1246Gc7A-7G130",
		"1370ea0E-6C-67-9",
		"1404Cc-4a6a8-2-6",
		"1444gi0C-6a-69-2",
		"1509Ia1a0g-367",
		"1550cA6g7G-4-55",
		"1503ge-6g-9C95-7",
		"1564ia5g-9e0-94",
		"1623GC7c8c9-37",
		"1636Ga0a-3e701",
		"1629ce-1A-9C-9-4-4",
		"1651AC0i-8g0-13",
		"509ag3C6G-120",
	};


	private static String[] notClaimedIDs = { 
		"97gI6G0a19-4",
		"944ec0G-8g-911",
		"943ac-8i-3I5-42",
		"942IE9e7E7-95",
		"93aa2G4g627",
		"930gg-1E7i0-5-8",
		"92Aa8E8C18-3",
		"929ia-7A-6c-87-5",
		"927Eg7G2c-6-26",
		"926eg1i3A-134",
		"920CC-3g6C1-6-8",
		"91Cc4i3a000",
		"90Ca-3i-3E04-4",
		"902gI-4G9e5-76",
		"89GE6I-7G22-6",
		"896Ic-5a0a1-8-7",
		"88AC7i0C-8-1-8",
		"86cg-4A-9E-118",
		"862EG-5a2c9-12",
		"860ce-8i0i6-26",
		"85EG-7C-1c253",
		"857iE1A-3E90-8",
		"84EE7E2G71-1",
		"78Eg9C0a-1-9-3",
		"776cc-5E-8c-7-9-5",
		"770cE9I-8g-1-4-4",
		"766ie4g2a8-5-8",
		"765ce-7a-9E-4-62",
		"764EE7e3c-1-7-1",
		"762gA-4A-2I-95-3",
		"761Ea2g9A-6-70",
		"759AG-3A-5a6-46",
		"758CE-9g-9g1-8-2",
		"756Ig-5G2I-7-2-3",
		"752Gc-2E9A20-8",
		"74IE5g1C5-97",
		"749Ci-2C7G64-1",
		"740II-8c3G-4-37",
		"73iC-9C-6E061",
		"735EA-5I-7C-2-7-3",
		"733ii-2G2a-526",
		"724CC7A7c9-6-7",
		"720cE7a1C4-4-5",
		"711gE8i-5c9-20",
		"708Ai-6c-3A-45-7",
		"705Ec-6E-4I108",
		"703ca-4g-4G710",
		"701Ge0E-1a-5-6-5",
		"699Ia8i4A-4-87",
		"698EE5A3C-6-79",
		"697aE8i2I418",
		"695CC4g-7g3-62",
		"694cA0i9a-8-50",
		"691IA-5C6I-71-3",
		"689ca-9I-2G-455",
		"686eC-2a8C-370",
		"685cE-9a8A-412",
		"684Ei7i9E84-6",
		"683GC-9A-2g-702",
		"682Ee-7E-2i-4-1-9",
		"680ia-6e6a-9-7-9",
		"679aI-1E-5I-84-8",
		"676AI5I-9a27-5",
		"671ai-9g-2a801",
		"66cg6a9G7-9-8",
		"669ee3i7g27-5",
		"668AG3I-8E4-73",
		"665ce-2E4E41-6",
		"665ce-2E4E41-6",
		"660gI4G9C3-29",
		"657eG-1I5i346",
		"656Eg-4I-5C-985",
		"649cg-3e8A0-50",
		"649cg-3e8A0-50",
		"649cg-3e8A0-50",
		"648iE-6C-4I01-2",
		"646gC-5E-7G-5-8-3",
		"643gE4i-1I75-9",
		"642ca1i-2C4-72",
		"639AI-1g6G808",
		"638ea1c-9g-5-3-5",
		"638ea1c-9g-5-3-5",
		"637IC0I7g0-3-1",
		"635iI1a-2A2-53",
		"633Ce7G-8e-9-7-3",
		"632cg-6G2g-5-1-7",
		"631CG1A-6g4-7-7",
		"629II-4G5A1-59",
		"629II-4G5A1-59",
		"627EE6g0A-193",
		"621EI3G-5G-2-10",
		"620eC8I3e7-23",
		"618ge-5c4c3-52",
		"616GI-2c7i6-58",
		"615gg-1A-3E-7-4-9",
		"614AG-1C7A35-6",
		"613gE-9c7a007",
		"611CC6e-7i903",
		"609ic3A-5G03-8",
		"608IC1c-3c-300",
		"557ce0c2e-495",
		"554Ig-4a3A-307",
		"552eE7E4a-2-84",
		"551IG-8a-9C8-57",
		"550cc-6A-6I-1-8-2",
		"54eg7E-6a0-35",
		"53Ea3G0g-6-40",
		"49Ge2A-3A97-4",
		"48eA8I-5G8-1-5",
		"437iG-6a-4G-7-79",
		"435Gc-2g7e38-1",
		"433AI-9c3e-2-5-6",
		"425cg-3C-4I8-7-1",
		"409ea6a7c-5-10",
		"386aE0E-5i83-4",
		"384aE1C0e-10-2",
		"383gI4I6c4-7-7",
		"356Ec7i-9C7-60",
		"355Ig-2e-9g-284",
		"352eG6G8I245",
		"345Ce-8I8I-700",
		"342gi5C6e-25-2",
		"342gi5C6e-25-2",
		"341Ec-3G1i8-2-4",
		"340eI0a-3c-987",
		"340eI0a-3c-987",
		"339cC2A0a-78-9",
		"336cA1I9C-6-32",
		"333Gc3A7E-8-48",
		"328Ge1G4c-8-3-1",
		"313eC3g-8G74-4",
		"306Ic2a-7a-1-6-5",
		"304GG0c0I-429",
		"296AC-9G7C-7-40",
		"295ia-3A-8i-430",
		"286Ii8g-8G5-11",
		"284ac-6c-7g6-7-8",
		"27gA7g9c-3-69",
		"276Ec-3A-6a7-44",
		"269CA-3c-9i1-64",
		"24CI-5C3G-583",
		"240ca9i-7e-96-3",
		"230EA-4E-3E0-21",
		"230EA-4E-3E0-21",
		"226ei6e2E461",
		"221gI-9E7a1-3-9",
		"21gg-8e3a-2-93",
		"197GG2g-9e6-9-8",
		"197GG2g-9e6-9-8",
		"195ga9I2e-3-3-2",
		"191gg2G-4a-8-8-2",
		"189ac6A2c22-7",
		"188ac-7g-3E766",
		"187IA-4i-9g0-3-1",
		"184IC-2c-1a140",
		"184IC-2c-1a140",
		"183eI-8A-8c-8-96",
		"182iC9c-3I71-5",
		"16gE1g-5E64-7",
		"103AC-6A0G-68-7",
		"100ae0i-5c08-8",
	};


}
