package edu.uci.ics.sdcl.firefly.util.mturk;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

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

	String folder = "C:/firefly/stage/";

	String[] mturkLogs_TP23456 = {	

			"HIT03_6 6_v0_TP2.csv",
			"HIT04_7 7_v1_TP2.csv",
			"HIT05_35 4_TP2.csv",
			"HIT06_51 4_TP2.csv",
			"HIT07_33 4_TP2.csv",
			"HIT08_54 4_TP2.csv",

			"HIT03_6 6-v2_TP3.csv",
			"HIT04_7 7_TP3.csv",
			"HIT05_35 5_TP3.csv",
			"HIT06_51 5_TP3.csv",
			"HIT06_51 6_TP3.csv",
			"HIT07_33 5_TP3.csv",

			"HIT08_54 5_TP4.csv",
			"HIT04_7 8_TP4.csv",
			"HIT03_6 7_TP4.csv",

			"HIT04_7 10_TP5.csv",

			"HIT04_7 11_TP6.csv",
			"008 HIT08_54_b2_TP6.csv",
			"004 HIT04_7 1_TP6.csv",
			"008 HIT08_54 TP6.csv",

	};

	String crowdLog_TP23456 = "session-log-43_46_54_58_64_TOPUPs.txt";


	//----------------------------------

	String[] mturkLogs_TP1 = {
			"hit01_8 3_TopUp1.csv",
			"hit03_6_5_TopUp1.csv",
			"hit04_7_6_TopUp1.csv",
			"hit05_35batch5_TopUp1.csv",
			"hit06_51batch3-TopUp1.csv",
			"hit07_33batch3-TopUp1.csv",
			"hit08_54batch3-TopUp1.csv"
	};


	String crowdLog_TP1 = "session-log-35_c.log";

	//----------------------------------------




	String[] mturkLogs_S1 = {
			"HIT01_08_1.csv",
			"HIT02_24_1.csv",
			"HIT03_6_1.csv",
			"HIT04_7_1.csv",		
	};


	String crowdLog_S1 = "session-log-6c.txt";


	//--------------------------------------

	String[] mturkLogs_S2 = {
			"HIT05_35 2.csv",
			"HIT06_51 2.csv",
			"HIT07_33 2.csv",
			"HIT02_24 2.csv",
			"HIT03_6 2.csv",
			"HIT01_8 2.csv",
			"HIT08_54_1.csv",
			"HIT08_54 3.csv",
			"HIT06_51_1.csv",
			"HIT04_7 5.csv"
	};


	String crowdLog_S2 = "session-log-28.log";

	HashMap<String, Turker> hitsMap1 = new HashMap<String, Turker>(); 
	HashMap<String, Turker> hitsMap2 = new HashMap<String, Turker>();

	//-----------------------------------

	public static void main(String[] args){
		TurkerWorkerMatcher matcher = new TurkerWorkerMatcher();
		matcher.loadHITs();

		matcher.loadSessions();  //Load the sessions and populate turkers with respective workerIds.

		//matcher.printTurkersWithSameWorkerID();//Session (1,2,TP1, TP2, etc.), same workerID:  (TurkerID1:WorkerSession , TurkerID2:WorkerSession)

		//matcher.printTurkerWithDifferentWorkerID();
		//Order of checking: Session (TP6 x TP5, TP65 x TP4, TP654x TP3, TP6453x TP1, TP x S1, TPS x S2 

	}


	public void loadHITs(){

		for(String batchFile: mturkLogs_S1){
			//	this.hitsMap1= importHITCodes(batchFile, hitsMap1);		
		}

		for(String batchFile: mturkLogs_S2){
			this.hitsMap2= importHITCodes(batchFile, hitsMap2);		
		}
	}

	public void loadSessions(){
		//	matchWorkerIDs(crowdLog_S1,hitsMap1); //Load the sessions and populate turkers with respective workerIds.
		matchWorkerIDs(crowdLog_S2,hitsMap2);
	}


	public void matchWorkerIDs(String crowdLog, HashMap<String, Turker> hitsMap){


		FileSessionDTO sessionDTO = new FileSessionDTO(this.folder+crowdLog);
		HashMap<String, WorkerSession> sessionMap = sessionDTO.getSessions();

		for(Turker turker: hitsMap.values()){
			for(String sessionID: turker.sessionMap.keySet()){
				//System.out.println("Turker sessionID entered:"+sessionID);
					String workerID = retrieveWorkerID(sessionMap, sessionID.trim());
					if(workerID!=null){
						//System.out.println("found, turkerID:"+turker.turkerID+": workerID:"+workerID+": sessionID:"+sessionID );
						turker.setWorkerID(workerID);
						hitsMap.put(turker.turkerID, turker);
					}
					else{
						//System.out.println("NOT FOUND! turkerID:"+turker.turkerID+": sessionID:"+sessionID );
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
				}
					
			}
		}
		return null;
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
		String path = this.folder +  batchFile;

		HashMap<String, String> codeMap = new HashMap<String, String>();

		BufferedReader log = null;
		try {

			System.out.println("Batch: "+path);
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


	public boolean hasMicrotasks(WorkerSession session){
		return (session.getMicrotaskListSize()>0);
	}


	//----------------------------------------------------------------------------------------------------------------------------------

	public static boolean checkQuit(String workerID){
		for(String id: quitWorkerID_S2){
			if(id.compareTo(workerID)==0) return true;
		}
		return false;
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



}
