package edu.uci.ics.sdcl.firefly.servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.CodeSnippetFactory;
import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.MicrotaskContextFactory;
import edu.uci.ics.sdcl.firefly.QuestionFactory;
import edu.uci.ics.sdcl.firefly.SourceFileReader;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.WorkerSessionFactory;
import edu.uci.ics.sdcl.firefly.controller.StorageManager;
import edu.uci.ics.sdcl.firefly.report.ReportGenerator;
import edu.uci.ics.sdcl.firefly.storage.LiteContainer;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerStorage;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;
import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;

import java.util.*; 

public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileUploadServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String subAction = request.getParameter("subAction");

		if(subAction!=null){

			if(subAction.compareTo("generateWorkerSessions")==0){
				String return_message = this.generateWorkerSessions();
				//makeSessionsAvailable();
				String microtasks_message = request.getParameter("microtasks_message");
				request.setAttribute("microtasks_message", microtasks_message);
				request.setAttribute("workerSessions_message", return_message);
				request.getRequestDispatcher("/FileUpload.jsp").forward(request, response);
			}
			else
				if(subAction.compareTo("delete")==0){
					this.delete();
					request.setAttribute("workerSessions_message", "");
					request.setAttribute("reports_message", "<b>Repositories Deleted!</b>");
					request.getRequestDispatcher("/FileUpload.jsp").forward(request, response);
				}
				if(subAction.compareTo("generateReports")==0){
					ReportGenerator generator = new ReportGenerator();
					generator.runReports();
					request.setAttribute("reports_message", "Reports Generated!");
					request.getRequestDispatcher("/FileUpload.jsp").forward(request, response);
				}
				else{
					request.setAttribute("error", "SubAction unknown");
					request.setAttribute("executionId", "before consent");
					request.getRequestDispatcher("/ErrorPage.jsp").include(request, response);
				}
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//process only if its multipart content
		String targetMethodName = new String();
		String numberOfParameters = null;
		String bugReport = new String();
		String fileName = new String();
		String fileContent = new String();
		String workerId= new String();
		boolean loadPerMethod = false;

		if(ServletFileUpload.isMultipartContent(request)){
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			String return_message ="";
			try {
				List<FileItem>  items = upload.parseRequest(request);
				for(FileItem item: items){	//Already enables more than one file upload.
					if (!item.isFormField()) { 
						fileName = item.getName();				
						long sizeInBytes = item.getSize();
						if(sizeInBytes>0){
							// reading fileContent
							Scanner scanner = new Scanner(item.getInputStream(),"Cp1252");
							fileContent = scanner.useDelimiter("\\A").next();		
							return_message = "File <b>"+ fileName+"</b> was successfully uploaded.<br> Size: "+sizeInBytes+" bytes <br>";
							scanner.close();
						}
						else{
							//No fileName provided, so it will execute a standard load.
							loadPerMethod = false;
						}
						request.setAttribute("fileName", fileName);  
					}
					else{
						// getting bug report
						if (item.getFieldName().equalsIgnoreCase("bugReport")){
							bugReport = item.getString();
						}
						// getting specific method name
						if (item.getFieldName().equalsIgnoreCase("targetMethod")){
							StringTokenizer tokenizer = new StringTokenizer(item.getString());						
							if(tokenizer.countTokens()==2){
								targetMethodName = tokenizer.nextToken();
								numberOfParameters = tokenizer.nextToken();
							}
							else{
								targetMethodName = item.getString();
							}
							if(targetMethodName!=null && targetMethodName.length()>0)
								loadPerMethod = true;
						}
						if (item.getFieldName().equalsIgnoreCase("workerId"))
							workerId = item.getString();
					}
				}

				//Sanitize the content removing the tabs because the Javascript 
				//editor counts a tab as one space, which a tab is actually 4 spaces.
				fileContent = fileContent.replaceAll("\t","    ");
				
				//Store the workerId Researcher
				WorkerStorage workerStorage =  WorkerStorage.initializeSingleton();;
				Worker worker = new Worker(workerId,TimeStampUtil.getTimeStampMillisec());
				workerStorage.insertConsent(worker);
				
				// generating microtasks individualized or in bulk
				if (loadPerMethod){
					return_message = return_message + generateMicrotasks(fileName, fileContent, targetMethodName,numberOfParameters, bugReport);;
				}
				else{//in bulk
					return_message = return_message + this.bulkUpload();
				}
				request.setAttribute("workerSessions_message", "");
				request.setAttribute("microtasks_message", return_message);

			}
			catch (FileUploadException e) {
				request.setAttribute("microtasks_message", "File upload failed. " + e);
			}
		}
		else
			request.setAttribute("microtasks_message","");

		request.getRequestDispatcher("/FileUpload.jsp").forward(request, response);
	}


	private String generateMicrotasks(String fileName, String fileContent, String methodName, String numberOfParameters, String bugReport){
		//calls CodeSnippetFactory
		CodeSnippetFactory codeSnippetFactory = new CodeSnippetFactory(fileName,fileContent);
		Vector<CodeSnippet> snippetList = codeSnippetFactory.generateSnippetsForFile();
		int parameterSize;
		if(numberOfParameters==null)
			parameterSize=-1;
		else
			parameterSize = new Integer(numberOfParameters).intValue();
		
		//filtering by methodName
		boolean foundMatch = false;		// assuming it found the method specified
		//System.out.print("candidates: ");
		Vector<CodeSnippet> filteredCodeSnippets = new Vector<CodeSnippet>();
		for (CodeSnippet codeSnippet : snippetList) {
			if (codeSnippet.getMethodSignature().getName().matches(methodName)){
				if((parameterSize!=-1) && (codeSnippet.getMethodSignature().getParameterList() !=null)){
					if(codeSnippet.getMethodSignature().getParameterList().size()== parameterSize){
					//Means that a parameter count was provided, so have to match that as well.
					//not only the method name
						filteredCodeSnippets.add(codeSnippet);
						foundMatch = true;
						break;
					}
					else{
						//do nothing, ignore method, because they have same name, but different parameters.
						//System.out.println("ignoring method for different parameter size: "+codeSnippet.getMethodSignature().toString());
					}
				}
				else{//no parameter list was provided, so just add the first occurrence of the method
					filteredCodeSnippets.add(codeSnippet);
					foundMatch = true;
					break;
				}
			}
			else{
			//ignore method, because has a different method name
			}
		}
		

		String results = "";
		if (foundMatch){
			//calls QuestionFactory
			
			QuestionFactory questionFactory = new QuestionFactory ();
			MicrotaskContextFactory contextFactory = new MicrotaskContextFactory();
			MicrotaskStorage storage = MicrotaskStorage.initializeSingleton();;
			
			Hashtable<Integer, Microtask> microtaskMap = questionFactory.generateMicrotasks(filteredCodeSnippets, bugReport, storage.getNumberOfMicrotask());
			microtaskMap = contextFactory.generateMicrotaskContext(microtaskMap);
			
			if (microtaskMap!= null && microtaskMap.size() > 0){
				FileDebugSession fileDebuggingSession = new FileDebugSession(fileName, fileContent, microtaskMap);
				int generatedMicrotasks = microtaskMap.size();
				
				//Persist data
				storage.insert(fileName, fileDebuggingSession); //append to existing fileDebugSessions

				int generatedCodeSnippets = snippetList.size();
				int numberOfMicrotasks = storage.getNumberOfMicrotask();
				results = "Code snippets generated: "+generatedCodeSnippets+ 
						"<br> Microtasks generated: " + generatedMicrotasks+"<br>"+
						"Total Microtasks available now: "+ numberOfMicrotasks + "<br>";
				
				Logger logger = LoggerFactory.getLogger(FileUploadServlet.class);
				logger.info("EVENT: FileUpload; File="+ fileName+"; CodeSnippets="+generatedCodeSnippets+"; Microtasks="+generatedMicrotasks);
				
				System.out.println("Results: "+results);
			}
			else
				results = "No Microtasks were generated! Please review the file uploaded.";
		}
		else
			results = "No Microtasks were generated! The method name was not found in the file.";




		return results;
	}
	

	private String generateWorkerSessions(){

		String results = new String();
		
		PropertyManager manager = PropertyManager.initializeSingleton();
		
		//Generate the stack of New and Duplicated WorkerSession		
		WorkerSessionFactory sessionFactory = new WorkerSessionFactory(manager.microtasksPerSession);

		Stack<WorkerSession> originalStack = sessionFactory.generateSessions(manager.answersPerMicrotask); 
				
		WorkerSessionStorage storage = WorkerSessionStorage.initializeSingleton();
		storage.writeNewWorkerSessionStack(originalStack);

		int totalSessions = originalStack.size();
		int existingSessions = storage.getNumberOfNewWorkerSessions(); 
	
		results = "<b>Sessions generated: </b>" +totalSessions+"<br>"+ 
					"<b>Total Sessions available now: </b> "+existingSessions;

		//System.out.println("Results: "+results);

		return results;
	}
	
	private int makeSessionsAvailable(){
		WorkerSessionStorage storage = WorkerSessionStorage.initializeSingleton();
		Vector<WorkerSession> sessionList = storage.retrieveNewSessionStorage();
		
		Hashtable<String, Vector<Microtask>> sessionTable = new Hashtable<String, Vector<Microtask>>();
		
		for(WorkerSession session: sessionList){
			Vector<Microtask> taskList = session.getMicrotaskList();
			Vector<Microtask> microtaskLiteVector = new Vector<Microtask>();
			for(Microtask task: taskList){
				Microtask taskLite = task.getLiteVersion();
				microtaskLiteVector.add(taskLite);
			}
			sessionTable.put(session.getId(), microtaskLiteVector);
		}
		
		LiteContainer container = LiteContainer.initializeSingleton();
		container.setSessionTable(sessionTable);
		return sessionTable.size();
	}
	
	/* 
	 * Used for testing purposes
	 */
	private String generateSingleWorkerSession(){

		String results = new String();
		
		//Generate the stack of New and Duplicated WorkerSession
		WorkerSessionFactory sessionFactory = new WorkerSessionFactory(20);
		Stack<WorkerSession> originalStack = sessionFactory.generateSingleSession();
				
		WorkerSessionStorage storage = WorkerSessionStorage.initializeSingleton();
		storage.writeNewWorkerSessionStack(originalStack);
		
		int totalSessions = originalStack.size(); 
		int existingSessions = storage.getNumberOfNewWorkerSessions(); 
	
		results = results + "Sessions generated: " +totalSessions+"<br>"+ 
					"Total Sessions available now: "+existingSessions;

		System.out.println("Results: "+results);

		return results;		
	}
	
	private void delete(){
		StorageManager manager = new StorageManager();
		manager.cleanUpRepositories();
	}
	
	private String bulkUpload(){
	
		Logger logger = LoggerFactory.getLogger(FileUploadServlet.class);
		PropertyManager manager = PropertyManager.initializeSingleton();
		String path = manager.fileUploadSourcePath;
		System.out.println("path: "+path);
		
		String[] fileList = {"1buggy_ApacheCamel.txt", "2SelectTranslator_buggy.java", "3buggy_PatchSetContentRemoteFactory_buggy.txt", "6ReviewScopeNode_buggy.java", 
				"7buggy_ReviewTaskMapper_buggy.txt", "8buggy_AbstractReviewSection_buggy.txt", "9buggy_Hystrix_buggy.txt",
				"10HashPropertyBuilder_buggy.java", "11ByteArrayBuffer_buggy.java","13buggy_VectorClock_buggy.txt"};
		
		String [] methodList = { "acquireExclusiveReadLock","appendColumn","addComments","convertScopeToDescription",
				"mapScope","appendMessage","endCurrentThreadExecutingCommand","calculateNumPopulatedBytes","grow","merge"};
		
		String [] failureList = { "Program execution causes NullPointerException","Program execution causes NullPointerException",
				"When we can't get a file version for whatever reason, an Null Pointer Exception occurs." ,"Program execution causes NullPointerException",
				"Program execution causes UnsupportedMethodException","Program execution causes ClassCastException","Program execution causes NoSuchElementException",
				"Probing algorithm spinning indefinitely trying to find a hole in a byte sequence.",
				"NegativeArraySizeException for data larger than 2GB / 3." , 
				"java.lang.IllegalArgumentException: Version -532 is not in the range (1, 32767) in ClockEntry constructor."};
		String message="";
		for(int i=0; i<fileList.length; i++){
			String fileName = fileList[i];
			String methodName = methodList[i];
			String failureDescription = failureList[i];
			
			String fileContent = SourceFileReader.readFileToString(path+fileName);
			
			String result = generateMicrotasks(fileName, fileContent, methodName,null, failureDescription);
			logger.info("Event = UPLOAD; File="+ fileName+ "; MethodName="+ methodName+ "; Microtasks="+result);
			message = message + methodName+ ", " ;
		}
		return  "<b>Loaded methods: </b>" + message.substring(0, message.length()-2);
	}
}

