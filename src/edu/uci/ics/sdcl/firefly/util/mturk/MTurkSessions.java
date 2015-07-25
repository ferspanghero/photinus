package edu.uci.ics.sdcl.firefly.util.mturk;

import java.util.ArrayList;
import java.util.HashMap;

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
	ArrayList<String> mturkAllLogs = new ArrayList<String>();
	
	public MTurkSessions(){
		
		//one list of MTurk Logs 
		addLists(this.mturkLogs_TP1);
		addLists(this.mturkLogs_TP2);
		addLists(this.mturkLogs_TP3);
		addLists(this.mturkLogs_TP4);
		addLists(this.mturkLogs_TP5);
		addLists(this.mturkLogs_TP6);
		addLists(this.mturkLogs_S1);
		addLists(this.mturkLogs_S2);
	
	}
	
	private void addLists(String[] fileList){
		for(int i=0;i<fileList.length;i++){
			this.mturkAllLogs.add(fileList[i]);
		}
	}
	
	
	
	//-------------------------------------------------------------------
	
	

	String folder = "C:/firefly/stage/";

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
			"session-log-6.log",   //S1
			"session-log-28.log",  //S2
			"session-log-35.log",  //TP1
			"session-log-43.log",  //TP2
			"session-log-46.log",  //TP3
			"session-log-54.log",  //TP4
			"session-log-58.log",  //TP5
			"session-log-64.log"   //TP6
	};
	
	//Consolidate logs
	String crowdLog_TP123456 = "session-log-35-43_46_54_58_64_TOPUPs.txt";
	
	String crowdLog_S1S2 = "session-log-S1-S2.txt";	
	
	
}
