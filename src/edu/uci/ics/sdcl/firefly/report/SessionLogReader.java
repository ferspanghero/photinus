package edu.uci.ics.sdcl.firefly.report;

import java.io.File;

import edu.uci.ics.sdcl.firefly.util.PropertyManager;

public class SessionLogReader {
	
	//Session Close
	
	//Session Open
	
	//Microtask
	
	private String logPath = "c:/firefly/logs/";
	private String sessionFileName = "session-log.log";
	private String consentFileName = "consent-log.log";
	
	private File sessionFile;
	private File consentFile;
	
	public SessionLogReader(){
		this.sessionFile = new File(logPath+sessionFileName);
		this.consentFile = new File(logPath+consentFileName);
		this.tokenizer = new StringTokenizer();
	}
			
	private boolean loadSessions(){
		String line = "";
		StringTokenizer tokeninzer
		while (line!=null){
			line = readLine(this.sessionFile);
			
		}
			
	}
	
	private boolean loadConsents(){}
			
	File file = new File()
	
	try(BufferedReader br = new BufferedReader(new FileReader(file))) {
	    for(String line; (line = br.readLine()) != null; ) {
	        // process the line.
	    }
	    // line is not visible here.
	}
	
	private String readLine(FileName){}

}
