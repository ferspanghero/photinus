package edu.uci.ics.sdcl.firefly.report;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.Microtask;

public class LogReport {

	//Session Map
	private	HashMap<String, WorkerSession> sessionMap = new HashMap<String, WorkerSession>(); 

	private	HashMap<String, WorkerSession> closedSessionMap = new HashMap<String, WorkerSession>();

	private	HashMap<String, WorkerSession> openedSessionMap = new HashMap<String, WorkerSession>();

	private	HashMap<String, WorkerSession> incompleteSessionMap = new HashMap<String, WorkerSession>();

	private	HashMap<String, Microtask> microtaskMap = new HashMap<String, Microtask>();

	private	HashMap<String, Worker> workerMap = new HashMap<String, Worker>();

	private	HashMap<String, Worker> surveyMap = new HashMap<String, Worker>();

	private	HashMap<String, Worker> skillTestMap = new HashMap<String, Worker>();

	private	HashMap<String, Worker> consentMap = new HashMap<String, Worker>();

	private	ArrayList<String> timeStampList = new ArrayList<String>();

	//Sessions that are considered spurious
	private	HashMap<String,String> discardMap = new HashMap<String,String>();

	//Bug revealing Microtask IDs that we expect YES or Probably Yes
	private	int[] yesArray = {1, 3, 20, 25, 18, 61, 53, 51, 33, 69,71, 139,137,119,132,147,145,151,149,156,153,163,164,171,170,167,176,174,178,188,180};
	private HashMap<String,String> yesMap = new HashMap<String,String>();
	
	//Bug revealing Microtask IDs for not methodBody Yes or Probably Yes
	private	int[] yesNotBodyFunctionArray = {3, 20, 25, 18, 61, 53, 51, 71, 139,137,132,147,151,149,156,164,171,170,176,188,180};
	private HashMap<String,String> yesNotBodyFunctionMap = new HashMap<String,String>();
	
	private int totalNumberMicrotasks = 215;

	//File Name, Point (x=start;y=end)
	private HashMap<String, Point> fileNameTaskRange = new HashMap<String,Point>();


	//Session Open

	//Microtask

	private String logPath = "c:/firefly/logs/";
	private String sessionFileName = "session-log.log";
	private String consentFileName = "consent-log.log";

	private File consentFile; 
	private File sessionFile;

	public LogReport(String path){

		if(path!=null) this.logPath = path;

		this.sessionFile = new File(logPath+sessionFileName);
		this.consentFile = new File(logPath+consentFileName);
		for(int id:yesArray){
			this.yesMap.put(new Integer(id).toString(),"YES");
		}

		fileNameTaskRange.put("9buggy_Hystrix_buggy.txt",new Point(162,165));
		fileNameTaskRange.put("8buggy_AbstractReviewSection_buggy.txt",new Point(152,161));
		fileNameTaskRange.put("7buggy_ReviewTaskMapper_buggy.txt",new Point(144,151));
		fileNameTaskRange.put("6ReviewScopeNode_buggy.java",new Point(118,143));
		fileNameTaskRange.put("3buggy_PatchSetContentRemoteFactory_buggy.txt",new Point(68,117));
		fileNameTaskRange.put("2SelectTranslator_buggy.java",new Point(32,67));
		fileNameTaskRange.put("1buggy_ApacheCamel.txt",new Point(0,31));
		fileNameTaskRange.put("13buggy_VectorClock_buggy.txt",new Point(177,214));
		fileNameTaskRange.put("11ByteArrayBuffer_buggy.java",new Point(173,176));
		fileNameTaskRange.put("10HashPropertyBuilder_buggy.java",new Point(166,172));


	}

	//How to take the batch into consideration?
	public boolean processLog(){
		return loadConsents() && loadSessions();
	}

	private boolean loadConsents(){
		System.out.println("load consent");
		try(BufferedReader br = new BufferedReader(new FileReader(this.consentFile))) {
			for(String line; (line = br.readLine()) != null; ) {
				System.out.println("line: "+line);
				processConsentLine(line);
			}
			System.out.println("Consent finished");
			return true;
		}
		catch(Exception e){
			System.err.println(e.toString());	
			return false;
		}
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
		System.out.println("token consent");
		//Event resolution
		if(token.trim().matches("CONSENT")){
			tokenizer.nextToken(); //consentDate label
			String consentDate = tokenizer.nextToken();
			worker = new Worker(workerId,consentDate);
			System.out.println("consent found");
			this.workerMap.put(worker.getWorkerId(), worker);

			this.consentMap.put(worker.getWorkerId(), worker);

		}
		else
			if(token.trim().matches("SKILLTEST")){

				worker = this.workerMap.get(workerId);

				Hashtable<String, Boolean> gradeMap = new Hashtable<String, Boolean>();
				tokenizer.nextToken(); //test 1 label
				gradeMap.put("QUESTION1", new Boolean(tokenizer.nextToken())); 
				tokenizer.nextToken(); //test 2 label
				gradeMap.put("QUESTION2", new Boolean(tokenizer.nextToken()));
				tokenizer.nextToken(); //test 3 label
				gradeMap.put("QUESTION3", new Boolean(tokenizer.nextToken()));
				tokenizer.nextToken(); //test 4 label
				gradeMap.put("QUESTION4", new Boolean(tokenizer.nextToken()));

				tokenizer.nextToken(); //grade label
				String grade = tokenizer.nextToken();
				tokenizer.nextToken(); //duration label
				String duration = tokenizer.nextToken();
				worker.setGrade(new Integer(grade).intValue());

				//Retrieve answers
				worker.setSkillAnswers(null, gradeMap, null, new Integer(grade).intValue(), duration);

				this.workerMap.put(worker.getWorkerId(), worker);

				this.skillTestMap.put(worker.getWorkerId(), worker);
			}
			else
				if(token.trim().matches("SURVEY")){
					tokenizer.nextToken(); //sessionId label
					String sessionId = tokenizer.nextToken();
					worker = this.workerMap.get(workerId);
					worker.setSessionId(sessionId);

					this.workerMap.put(worker.getWorkerId(), worker);

					this.surveyMap.put(worker.getWorkerId(), worker);
				}


		return false;

	}

	private boolean loadSessions(){
		try(BufferedReader br = new BufferedReader(new FileReader(this.sessionFile))) {
			for(String line; (line = br.readLine()) != null; ) {
				System.out.println("line: "+line);
				processSessionLine(line);
			}
			return true;
		}
		catch(Exception e){
			System.err.println(e.toString());	
			return false;
		}
	}

	public boolean processSessionLine(String line){
		StringTokenizer tokenizer = new StringTokenizer(line, "%");
		int totalTokens = tokenizer.countTokens();

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
			this.openedSessionMap.put(session.getId(), session);
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
				String explanation;
				if(totalTokens==18)
					explanation= tokenizer.nextToken();
				else 
					explanation="";

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

	public int getNumberOfMicrotasks(){
		int counter=0;
		Iterator<String> iter = this.microtaskMap.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			Microtask task = this.microtaskMap.get(key);
			counter = counter + task.getNumberOfAnswers();
		}
		return counter;
	}

	public int getSurveys(){
		return this.surveyMap.size();
	}

	public int getConsents(){
		return this.consentMap.size();
	}

	public int getSkillTests(){
		return this.skillTestMap.size();
	}

	public int getOpenedSessions(){
		return this.openedSessionMap.size();
	}

	public int getClosedSessions(){
		return this.closedSessionMap.size();
	}

	public void expectecYesAnswers(){
		int counterYes=0;
		int counterAnswers=0;
		int counterNos=0;
		int counterICantTell=0;
		Iterator<String> iter = this.microtaskMap.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			Microtask task = this.microtaskMap.get(key);
			if(this.yesMap.containsKey(task.getID().toString())){
				Vector<Answer> answerList = task.getAnswerList();
				for(Answer answer: answerList){
					if(answer.getOption().matches(Answer.YES))
						counterYes++;
					else
						if(answer.getOption().matches(Answer.NO))
							counterNos++;
						else
							if(answer.getOption().matches(Answer.I_CANT_TELL))
								counterICantTell++;
				}
			}

		}

		System.out.println(" *** BUG REVEALING QUESTIONS - EXPECTED YES/Prob YES ANSWERS: ");
		counterAnswers = counterYes+counterNos+counterICantTell;
		System.out.println("Total Bug Revealing Answers="+counterAnswers);
		System.out.println("counterYes="+counterYes+ " > "+ counterYes/(counterYes+counterNos)*100+ "%");
		System.out.println("counterNos="+counterNos + " > "+ counterYes/(counterYes+counterNos)*100+ "%");
		System.out.println("counterICantTell="+counterICantTell + " > "+ counterICantTell/(counterAnswers)*100+ "%");
		
		System.out.println("-------------------------------------------");
		
		System.out.println("Total of different questions = 215");
		System.out.println("Total different bug revealing questions = "+this.yesArray.length);

	}

	public int expectedNoAnswers(){
		int counter=0;

		return counter;
	}

}
