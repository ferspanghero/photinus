package edu.uci.ics.sdcl.firefly.report;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

public class LogData {

	/** this is the map that will be used to compute statistics, currently can be the microtaskMap (superset) or 
	 * the completeSessions_microtaskMap, other subsets will be created.
	 */
	public HashMap<String, Microtask> workingMicrotaskMap;
	
	//Session Map
	public HashMap<String, WorkerSession> sessionMap = new HashMap<String, WorkerSession>(); 

	public	HashMap<String, WorkerSession> closedSessionMap = new HashMap<String, WorkerSession>();

	public	HashMap<String, WorkerSession> openedSessionMap = new HashMap<String, WorkerSession>();

	public	HashMap<String, Microtask> completeSessions_microtaskMap = new HashMap<String, Microtask>();

	public	HashMap<String, Microtask> microtaskMap = new HashMap<String, Microtask>();

	public	HashMap<String, Worker> workerMap = new HashMap<String, Worker>();

	public	HashMap<String, Worker> surveyMap = new HashMap<String, Worker>();

	public	HashMap<String, Worker> skillTestMap = new HashMap<String, Worker>();

	public	HashMap<String, Worker> consentMap = new HashMap<String, Worker>();

	public	ArrayList<String> timeStampList = new ArrayList<String>();

	//Sessions that are considered spurious
	public	HashMap<String,String> discardMap = new HashMap<String,String>();

	//Bug revealing Microtask IDs that we expect YES or Probably Yes
	public	int[] yesArray = {1, 3, 20, 25, 18, 61, 53, 51, 33, 69,71, 139,137,119,132,147,145,151,149,156,153,163,164,171,170,167,176,174,178,188,180};
	public HashMap<String,String> yesMap = new HashMap<String,String>();

	//Bug revealing Microtask IDs for not methodBody Yes or Probably Yes
	public	int[] yesNotBodyFunctionArray = {3, 20, 25, 18, 61, 53, 51, 71, 139,137,132,147,151,149,156,164,171,170,176,188,180};
	public HashMap<String,String> yesNotBodyFunctionMap = new HashMap<String,String>();

	public int totalNumberMicrotasks = 215;

	//File Name, Point (x=start;y=end)
	public HashMap<String, Point> fileNameTaskRange = new HashMap<String,Point>();

	public static String question[] = {"Gender", "Age", "Country", "Years progr.", "Difficulty", "Feedback"};   

	//Sessions for which the user wrote non-sense in the explanations 
	public String discardedNoiseSessions[] ={ };
	
	//Session Open

	//Microtask

	private String logPath = "c:/firefly/logs/";
	private String sessionFileName = "session-log.log";
	private String consentFileName = "consent-log.log";

	private File consentFile; 
	private File sessionFile;

	private String batchNumber;

	public LogData(){

		for(int id:yesArray){
			this.yesMap.put(new Integer(id).toString(),"YES");
		}

		for(int id:this.yesNotBodyFunctionArray){
			this.yesNotBodyFunctionMap.put(new Integer(id).toString(),"YES");
		}

		for(String id:this.discardedNoiseSessions){
			this.discardMap.put(id,id);
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

		System.out.println("Total of different questions = 215");
		System.out.println("Total different bug revealing questions = "+this.yesArray.length);

		
	}

	/**
	 * Get answers only from session which were completed,i.e., closed
	 */
	public void computeMicrotaskFromCompleteSessions(){
		System.out.println("Compute completed sessions");
		int counter=0;
		int microtaskCounter = 0;
		Iterator<String> iter = this.closedSessionMap.keySet().iterator();
		while(iter.hasNext()){
			String sessionId = iter.next();
			
			WorkerSession session = this.closedSessionMap.get(sessionId);
			String workerId = session.getWorkerId();
			
			Vector<Microtask> answerList = session.getMicrotaskList();
			
			
			if(answerList!=null  && answerList.size()>10)
				System.out.println("Session id="+sessionId+" has "+answerList.size()+" answers!");
			
			
			for(Microtask task: answerList){
				Microtask existing = this.completeSessions_microtaskMap.get(task.getID().toString());
				Answer answer = task.getAnswerByUserId(workerId);
				
				
				if(existing==null){
					existing = task.getSimpleVersion(); //create a new copy of the task, so we don't mixed with the old one.
				}
				else{
					existing.addAnswer(answer);					
				}
				
				this.completeSessions_microtaskMap.put(task.getID().toString(), existing);
				microtaskCounter++;
			}
			counter ++;
		}
		System.out.println("counter complete sessions:"+counter+ ", complete microtasks:"+microtaskCounter);				
	}
	
	/**
	 * 
	 * @param batchNumber to take the batch into consideration (marks the sessionId with the batchNumber
	 * @return
	 */
	public boolean processLogProduction2(String path){
		this.batchNumber = "2-";

		if(path!=null) this.logPath = path;
		this.sessionFile = new File(logPath+sessionFileName);
		this.consentFile = new File(logPath+consentFileName);

		boolean ok = loadConsents(2) && loadSessions(2);
		System.out.println("Production 2 logs loaded! Totals are:");
		System.out.println("Consents: "+this.getConsents());
		System.out.println("SkillTests: "+this.getSkillTests());
		System.out.println("Surveys: "+this.getSurveys());
		System.out.println("Sessions Opened: "+this.getOpenedSessions());
		System.out.println("Sessions Closed: "+this.getClosedSessions());
		System.out.println("Answers: "+this.getNumberOfMicrotasks());
		
		return ok;
	}

	private boolean loadConsents(int type){
		try(BufferedReader br = new BufferedReader(new FileReader(this.consentFile))) {
			for(String line; (line = br.readLine()) != null; ) {
				if(type==2)
					processConsentLine(line);
				else
					this.processConsentLineProduction1(line);
			}
			return true;
		}
		catch(Exception e){
			System.err.println(e);	
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
		String workerId = this.batchNumber+tokenizer.nextToken().trim();

		Worker worker;
		//Event resolution
		if(token.trim().matches("CONSENT")){
			tokenizer.nextToken(); //consentDate label
			String consentDate = tokenizer.nextToken();
			worker = new Worker(workerId,consentDate);
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
					String sessionId = this.batchNumber + tokenizer.nextToken();
					worker = this.workerMap.get(workerId);
					tokenizer.nextToken(); //Feedback label
					tokenizer.nextToken(); //Feedback value
					tokenizer.nextToken(); //Gender label
					String gender = tokenizer.nextToken(); //Gender value
					tokenizer.nextToken(); //Years Prog. label
					String experience = tokenizer.nextToken(); //Years Prog value					
					tokenizer.nextToken(); //Difficulty label
					String difficulty = tokenizer.nextToken(); //Difficulty value
					tokenizer.nextToken(); //Country label
					String country = tokenizer.nextToken(); //Country value
					tokenizer.nextToken(); //Age label
					String age = tokenizer.nextToken(); //Age value

					worker.setSessionId(sessionId);

					worker.addSurveyAnswer(question[0], gender);
					worker.addSurveyAnswer(question[1], age);
					worker.addSurveyAnswer(question[2], country);
					worker.addSurveyAnswer(question[3], experience);
					worker.addSurveyAnswer(question[4], difficulty);

					this.workerMap.put(worker.getWorkerId(), worker);

					this.surveyMap.put(worker.getWorkerId(), worker);
				}


		return false;

	}

	private boolean loadSessions(int type){
		try(BufferedReader br = new BufferedReader(new FileReader(this.sessionFile))) {
			for(String line; (line = br.readLine()) != null; ) {
				if(type==2)
					processSessionLine(line);
				else
					this.processSessionLineProduction1(line);
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
		String workerId = this.batchNumber+tokenizer.nextToken().trim();
		tokenizer.nextToken(); //"sessionId label"
		String sessionId = this.batchNumber + tokenizer.nextToken().trim(); 
		WorkerSession session;

		//Event resolution
		if(token.trim().matches("OPEN SESSION")){
			session = new WorkerSession(sessionId, new Vector<Microtask>());
			session.setWorkerId(workerId);
			this.sessionMap.put(session.getId(), session);
			this.openedSessionMap.put(session.getId(), session);
		}
		else
			if(token.trim().matches("MICROTASK")){

				session = this.sessionMap.get(sessionId);
				tokenizer.nextToken(); //"microtaskId label"
				String microtaskId = tokenizer.nextToken().trim();
				tokenizer.nextToken(); //FileName label
				String fileName = tokenizer.nextToken().trim();
				
				//Some answers are associated to the wrong file name
				if(tasksDoesNotPertainToFileName(microtaskId,fileName))
					return false;
				
				//Some answers are duplicated
				if(taskAlreadyInSession(microtaskId,session))
					return false;
				
				tokenizer.nextToken(); //question label
				String question = tokenizer.nextToken().trim();
				tokenizer.nextToken(); // answer label
				String answer = tokenizer.nextToken().trim();
				tokenizer.nextToken(); // duration label
				String duration = tokenizer.nextToken().trim();
				tokenizer.nextToken(); // explanation label
				String explanation;
				if(totalTokens==18)
					explanation= tokenizer.nextToken().trim();
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
				//if(fileName.matches("1buggy_ApacheCamel.txt")&&microtaskId.matches("1"))
					//System.out.println("####### adding taskId:"+microtaskId+" to 1buggy_ApacheCamel.txt!" +" sessionId="+sessionId);
				this.microtaskMap.put(microtaskId, microtask);

				//Session Microtask
				answerList = new Vector<Answer>();
				answerList.add(answerObj);
				Microtask sessionMicrotask = new Microtask(question, new Integer(microtaskId), answerList,fileName);
				Vector<Microtask> microtaskList = session.getMicrotaskList();
				microtaskList.add(sessionMicrotask);
				session.setMicrotaskList(microtaskList);
				this.sessionMap.put(session.getId(),session);

			}
			else
				if(token.trim().matches("CLOSE SESSION")){
					if(!this.closedSessionMap.containsKey(sessionId)){
						session = this.sessionMap.get(sessionId);
						this.closedSessionMap.put(session.getId(),session);
					}
				}

		return true;
	}


	private boolean taskAlreadyInSession(String microtaskId,
			WorkerSession session) {
		boolean repeated=false;
		Vector<Microtask> microtaskList = session.getMicrotaskList();
		for(Microtask task: microtaskList){
			if(task.getID().intValue()==new Integer(microtaskId).intValue()){
				//System.out.println("!!!!!!! repeated microtask "+ microtaskId+" in session " + session.getId());
				repeated=true;
				break;
			}
		}
		return repeated;
	}

	public boolean tasksDoesNotPertainToFileName(String microtaskId, String fileName) {
		
		Point point = this.fileNameTaskRange.get(fileName);
		int id = new Integer(microtaskId).intValue();
		if((id<point.x)||(id>point.y)){ //Out of expected range for the fileName
			//System.out.println("%%%%%%% microtask "+ microtaskId+" does not pertain to "+ fileName);
			return true;
		}
		
		else
			return false;
		}

	public String extractTimeStamp(String token){
		StringTokenizer tokenizer = new StringTokenizer(token,"[");
		return tokenizer.nextToken().trim();
	}

	

	//-------------------------------------------------------------------------

	/**
	 * 
	 * @param batchNumber to take the batch into consideration (marks the sessionId with the batchNumber
	 * @return
	 */
	public boolean processLogProduction1(String path){
		this.batchNumber = "1-";

		if(path!=null) this.logPath = path;
		this.sessionFile = new File(logPath+sessionFileName);
		this.consentFile = new File(logPath+consentFileName);

		boolean ok = loadConsents(1) && loadSessions(1);
		System.out.println("Logs loaded! Totals are:");
		System.out.println("Consents: "+this.getConsents());
		System.out.println("SkillTests: "+this.getSkillTests());
		System.out.println("Surveys: "+this.getSurveys());
		System.out.println("Sessions Opened: "+this.getOpenedSessions());
		System.out.println("Sessions Closed: "+this.getClosedSessions());
		System.out.println("Answers: "+this.getNumberOfMicrotasks());

		return ok;
	}


	private String nextTokenValue(String label, String line){
		if(line.length()<=0) return "";
		else{
			//System.out.print("label:"+ label);
			int labelStart = line.indexOf(label);
			int labelEnd = line.indexOf('=',labelStart+1);

			int valueEnd = line.indexOf(';',labelEnd);
			if(valueEnd<0 || valueEnd>=line.length())
				valueEnd = line.length();

			String value = line.substring(labelEnd+1, valueEnd).trim();
			//System.out.println(",value:"+value);
			return value;
		}
	}


	//---------------------------------------------------------------------------

	public boolean processConsentLineProduction1(String line){ 
		//System.out.println("Line : "+line);
		
		if(line==null || line.length()<=0 || line.indexOf("workerId")<0) //invalid line
			return false;
		
		
		//Time extraction
		String token = line.substring(0,line.indexOf("["));
		String timeStamp = extractTimeStamp(token.trim());
		this.timeStampList.add(timeStamp);

		//Event extraction
		//tokenizer.nextToken(""); //"workerId label"
		String workerId = this.batchNumber + nextTokenValue("workerId",line);

		token = nextTokenValue("EVENT", line);

		Worker worker;
		//Event resolution
		if(token.trim().matches("CONSENT")){
			//			tokenizer.nextToken(); //consentDate label
			String consentDate = nextTokenValue("consentDate",line);
			worker = new Worker(workerId,consentDate);
			this.workerMap.put(worker.getWorkerId(), worker);

			this.consentMap.put(worker.getWorkerId(), worker);

		}
		else
			if(token.trim().matches("SKILLTEST")){

				worker = this.workerMap.get(workerId);

				Hashtable<String, Boolean> gradeMap = new Hashtable<String, Boolean>();
				//test 1 label
				gradeMap.put("QUESTION1", new Boolean(nextTokenValue("test1",line))); 
				//test 2 label
				gradeMap.put("QUESTION2", new Boolean(nextTokenValue("test2",line)));
				//test 3 label
				gradeMap.put("QUESTION3", new Boolean(nextTokenValue("test3",line)));
				//test 4 label
				gradeMap.put("QUESTION4", new Boolean(nextTokenValue("test4",line)));

				//grade label
				String grade = nextTokenValue("grade",line);
				//testDuration label
				String duration = nextTokenValue("testDuration",line);
				worker.setGrade(new Integer(grade).intValue());

				//Retrieve answers
				worker.setSkillAnswers(null, gradeMap, null, new Integer(grade).intValue(), duration);

				this.workerMap.put(worker.getWorkerId(), worker);

				this.skillTestMap.put(worker.getWorkerId(), worker);
			}
			else
				if(token.trim().matches("SURVEY")){
					//sessionId label
					String sessionId = nextTokenValue("sessionId",line);
					sessionId=this.batchNumber+sessionId;
					String country = nextTokenValue("Country",line);
					String age = nextTokenValue("Age",line);
					String experience = nextTokenValue("Years progr.",line);
					String difficulty = nextTokenValue("Difficulty",line);
					String gender = nextTokenValue("Gender",line);
					worker = this.workerMap.get(workerId);

					worker.setSessionId(sessionId);
					worker.addSurveyAnswer(question[0], gender);
					worker.addSurveyAnswer(question[1], age);
					worker.addSurveyAnswer(question[2], country);
					worker.addSurveyAnswer(question[3], experience);
					worker.addSurveyAnswer(question[4], difficulty);
					//worker.addSurveyAnswer(question[5], feedback);

					this.workerMap.put(worker.getWorkerId(), worker);

					this.surveyMap.put(worker.getWorkerId(), worker);
				}


		return false;

	}

	
	public boolean processSessionLineProduction1(String line){
		
		//System.out.println("Line : "+line);
		
		if(line==null ||line.length()<=0 || line.indexOf("workerId")<0) //invalid line
			return false;
		
		
		//Time extraction
		String token = line.substring(0,line.indexOf("["));
		String timeStamp = extractTimeStamp(token.trim());
		this.timeStampList.add(timeStamp);

		String workerId = this.batchNumber + nextTokenValue("workerId",line);
				
		String sessionId = nextTokenValue("sessionId",line);
		sessionId=this.batchNumber+ sessionId; 
		WorkerSession session;

		//Event extraction
		token = nextTokenValue("EVENT", line);		
		
		//Event resolution
		if(token.matches("OPEN SESSION")){
			session = new WorkerSession(sessionId, new Vector<Microtask>()); 
			session.setWorkerId(workerId);
			this.sessionMap.put(session.getId(), session);
			this.openedSessionMap.put(session.getId(), session);
		}
		else
			if(token.matches("MICROTASK")){

				session = this.sessionMap.get(sessionId);
				String microtaskId = nextTokenValue("microtaskId", line);
				//fileName label
				String fileName = nextTokenValue("fileName", line);
				
				//Some answers are associated to the wrong file name
				if(tasksDoesNotPertainToFileName(microtaskId,fileName))
					return false;
				
				//Some answers are duplicated
				if(taskAlreadyInSession(microtaskId,session))
					return false;
				
				//question label
				String question = nextTokenValue("question", line);
				// answer label
				String answer = nextTokenValue("answer", line);
				// duration label
				String duration = nextTokenValue("duration", line);
				// explanation label
				String explanation = nextTokenValue("explanation", line);
				
				Answer answerObj = new Answer(answer, explanation, workerId,duration, timeStamp);
				Vector<Answer> answerList = new Vector<Answer>();
				answerList.add(answerObj);

			//	if(microtaskId.matches("1"))
			//		System.out.println("####### adding taskId:"+microtaskId+" to 1buggy_ApacheCamel.txt!"+"answer: "+answer );
				Microtask microtask = this.microtaskMap.get(microtaskId);
				if(microtask==null){
					microtask = new Microtask(question, new Integer(microtaskId), answerList,fileName);
				}
				else{
					microtask.addAnswer(answerObj);
				}
				this.microtaskMap.put(microtaskId, microtask);

				//Session Microtask
				answerList = new Vector<Answer>();
				answerList.add(answerObj);
				Microtask sessionMicrotask = new Microtask(question, new Integer(microtaskId), answerList,fileName);
				Vector<Microtask> microtaskList = session.getMicrotaskList();
				microtaskList.add(sessionMicrotask);
				session.setMicrotaskList(microtaskList);
				this.sessionMap.put(session.getId(),session);
			}
			else
				if(token.matches("CLOSE SESSION")){
					if(!this.closedSessionMap.containsKey(sessionId)){
						session = this.sessionMap.get(sessionId);
						this.closedSessionMap.put(session.getId(),session);
					}
				}

		return true;
	}
	



	//---------------------------------------------------------------------------

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




}
