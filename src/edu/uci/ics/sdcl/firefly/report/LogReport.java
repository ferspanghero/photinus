package edu.uci.ics.sdcl.firefly.report;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.Microtask;

public class LogReport {
	
	//Session Map
	HashMap<String, WorkerSession> sessionMap = new HashMap<String, WorkerSession>(); 
	
	HashMap<String, WorkerSession> closedSessionMap = new HashMap<String, WorkerSession>();
	
	HashMap<String, WorkerSession> incompleteSessionMap = new HashMap<String, WorkerSession>();
	
	HashMap<String, Microtask> microtaskMap = new HashMap<String, Microtask>();
	
	HashMap<String, Worker> workerMap = new HashMap<String, Worker>();
	
	ArrayList<String> timeStampList = new ArrayList<String>();
	
	HashMap<String,String> discardMap = new HashMap<String,String>();
	
	
	
	//Session Open
	
	//Microtask
	
	private String logPath = "c:/firefly/logs/";
	private String sessionFileName = "session-log.log";
	private String consentFileName = "consent-log.log";
	
	private File consentFile;
	private File sessionFile;
	
	public LogReport(){
		this.sessionFile = new File(logPath+sessionFileName);
		this.consentFile = new File(logPath+consentFileName);
	}
			
	//How to take the batch into consideration?
	public boolean processLog(){
		return loadConsents() && loadSessions();
	}
	
	private boolean loadConsents(){
		String line = "";
		while (line!=null){
			line = readLine(this.consentFile);
			processConsentLine(line);
		}
			return false;
	}
		
	public boolean processConsentLine(String line){ 
		
		StringTokenizer tokenizer = new StringTokenizer(line, "%");

		//Time extraction
		String token = tokenizer.nextToken();
		String timeStamp = extractTimeStamp(token);
		this.timeStampList.add(timeStamp);

		//Event extraction
		token = tokenizer.nextToken();
		
		tokenizer.nextToken(); //"workerId label"
		String workerId = tokenizer.nextToken();
		
		Worker worker;
		
		//Event resolution
		if(token.trim().matches("CONSENT")){
			tokenizer.nextToken(); //consentDate label
			String consentDate = tokenizer.nextToken();
			worker = new Worker(workerId,consentDate);
		}
		else
			if(token.trim().matches("SKILLTEST")){
				tokenizer.nextToken(); //sessionId label
				String sessionId = tokenizer.nextToken();
				
			}
			else
			if(token.trim().matches("SURVEY")){
				
				
			}
		
		
		return false;
		
	}
		
	private boolean loadSessions(){
		String line = "";
		while (line!=null){
			line = readLine(this.sessionFile);
			processSessionLine(line);
			
		}
			return false;
	}
	
	public boolean processSessionLine(String line){
		StringTokenizer tokenizer = new StringTokenizer(line, "%");

		//Time extraction
		String token = tokenizer.nextToken();
		String timeStamp = extractTimeStamp(token);
		this.timeStampList.add(timeStamp);
		
		//Event extraction
		token = tokenizer.nextToken();
		
		tokenizer.nextToken(); //"workerId label"
		String workerId = tokenizer.nextToken();
		tokenizer.nextToken(); //"sessionId label"
		String sessionId = tokenizer.nextToken(); 
		WorkerSession session;
		
		//Event resolution
		if(token.trim().matches("OPEN SESSION")){
			session = new WorkerSession(sessionId, new Vector<Microtask>()); 
			this.sessionMap.put(session.getId(), session);
		}
		else
			if(token.trim().matches("MICROTASK")){
				
				session = this.sessionMap.get(sessionId);
				tokenizer.nextToken(); //"microtaskId label"
				String microtaskId = tokenizer.nextToken();
				tokenizer.nextToken(); //FileName label
				String fileName = tokenizer.nextToken();
				tokenizer.nextToken(); //question label
				String question = tokenizer.nextToken();
				tokenizer.nextToken(); // answer label
				String answer = tokenizer.nextToken();
				tokenizer.nextToken(); // duration label
				String duration = tokenizer.nextToken();
				tokenizer.nextToken(); // explanation label
				String explanation = tokenizer.nextToken();
				
				Answer answerObj = new Answer(answer, explanation, workerId,duration, timeStamp);
				Vector<Answer> answerList = new Vector<Answer>();
				answerList.add(answerObj);
				
				Microtask microtask = this.microtaskMap.get(microtaskId);
				if(microtask==null){
					microtask = new Microtask(question, new Integer(microtaskId), answerList,fileName);
				}
				else{
					microtask.addAnswer(answerObj);
				}
				this.microtaskMap.put(microtaskId, microtask);
				
				//Session Microtask
				Microtask sessionMicrotask = new Microtask(question, new Integer(microtaskId), answerList,fileName);
				Vector<Microtask> microtaskList = session.getMicrotaskList();
				microtaskList.add(sessionMicrotask);
				session.setMicrotaskList(microtaskList);
				this.sessionMap.put(session.getId(),session);
			}
		else
			if(token.trim().matches("CLOSE SESSION")){
				session = this.sessionMap.get(sessionId);
				this.closedSessionMap.put(session.getId(),session);
			}
			
		
		
		return true;
		
	}
	
	
	private String extractTimeStamp(String token){
		StringTokenizer tokenizer = new StringTokenizer(token,"[");
		return tokenizer.nextToken().trim();
	}
	
	private String readLine(File fileName){ 

		//try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		  //  for(String line; (line = br.readLine()) != null; ) {
		        // process the line.
		    //}
		    // line is not visible here.
		//}
		
		return null;}

}
