package edu.uci.ics.sdcl.firefly.servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.CodeSnippetFactory;
import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.QuestionFactory;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.WorkerSessionFactory;
import edu.uci.ics.sdcl.firefly.controller.StorageManager;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerStorage;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

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

//				String return_message = this.generateWorkerSessions();
				String return_message = this.generateSingleWorkerSession(); //For experiment setting.
				String microtasks_message = request.getParameter("microtasks_message");
				request.setAttribute("microtasks_message", microtasks_message);
				request.setAttribute("workerSessions_message", return_message);
				request.getRequestDispatcher("/FileUpload.jsp").forward(request, response);
			}
			else
				if(subAction.compareTo("delete")==0){
					this.delete();
					request.setAttribute("workerSessions_message", "");
					request.getRequestDispatcher("/FileUpload.jsp").forward(request, response);
				}
				else{
					request.setAttribute("error", "SubAction unknown");
					request.getRequestDispatcher("/ErrorPage.jsp").include(request, response);
				}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//process only if its multipart content
		String targetName = new String();
		String bugReport = new String();
		String fileName = new String();
		String fileContent = new String();
		String userId= new String();
		String hitId = new String();
		boolean gotBugReport = false;		// assuming that not all parameters that are necessary are known
		boolean gotSpecificMethod = false;

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
						else
							return_message = "File <b>"+ fileName+"</b> is empty!";

						request.setAttribute("fileName", fileName);  
					}
					else{
						// getting bug report
						if (item.getFieldName().equalsIgnoreCase("bugReport")){
							bugReport = item.getString();
							gotBugReport = true;
						}
						// getting specific method name
						if (item.getFieldName().equalsIgnoreCase("targetMethod")){
							targetName = item.getString();
							gotSpecificMethod = true;
						}
						if (item.getFieldName().equalsIgnoreCase("userId"))
							userId = item.getString();

						if (item.getFieldName().equalsIgnoreCase("hitId"))
							hitId = item.getString();	
					}
				}

				//Sanitize the content removing the tabs because the Javascript 
				//editor counts a tab as one space, which a tab is actually 4 spaces.
				fileContent = fileContent.replaceAll("\t","    ");
				
				// generating microtasks if...
				if (gotBugReport && gotSpecificMethod){
					String results = generateMicrotasks(fileName, fileContent, targetName, bugReport);

					//Store the UserId and HitId of the Researcher
					String path = getServletContext().getRealPath("/");
					WorkerStorage workerStorage = new WorkerStorage();
					Worker worker = new Worker(userId,hitId,new Date());
					workerStorage.insert(userId, worker);

					return_message = return_message + results;
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


	private String generateMicrotasks(String fileName, String fileContent, String methodName, String bugReport){
		//calls CodeSnippetFactory
		CodeSnippetFactory codeSnippetFactory = new CodeSnippetFactory(fileName,fileContent);
		ArrayList<CodeSnippet> snippetList = codeSnippetFactory.generateSnippetsForFile();

		//filtering by methodName
		boolean foundMatch = false;		// assuming it found the method specified
		System.out.print("candidates: ");
		ArrayList<CodeSnippet> filteredCodeSnippets = new ArrayList<CodeSnippet>();
		for (CodeSnippet codeSnippet : snippetList) {
			System.out.print(codeSnippet.getMethodSignature().getName() + ", ");
			if (codeSnippet.getMethodSignature().getName().equals(methodName))
			{
				filteredCodeSnippets.add(codeSnippet);
				foundMatch = true;
			}
		}
		System.out.println();

		String results = "";
		if (foundMatch){
			//calls QuestionFactory
			
			QuestionFactory questionFactory = new QuestionFactory ();
			String path = getServletContext().getRealPath("/");
			MicrotaskStorage storage = new MicrotaskStorage();
			
			HashMap<Integer, Microtask> microtaskMap = questionFactory.generateQuestions(filteredCodeSnippets, bugReport, storage.getNumberOfMicrotask());

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
		
		PropertyManager manager = new PropertyManager();
		
		//Generate the stack of New and Duplicated WorkerSession		
		WorkerSessionFactory sessionFactory = new WorkerSessionFactory();
		Stack<WorkerSession> originalStack = sessionFactory.generateSessions(manager.microtasksPerSession);
		Stack<WorkerSession> duplicatedStack = sessionFactory.duplicateSessions(originalStack,manager.answersPerMicrotask-1); //Because we already have the original stack.
		
		WorkerSessionStorage sessionStorage = new WorkerSessionStorage();
		sessionStorage.appendNewWorkerSessionStack(originalStack,sessionStorage.NEW);
		sessionStorage.appendNewWorkerSessionStack(duplicatedStack,sessionStorage.NEW_COPIES);

		int totalSessions = originalStack.size()+duplicatedStack.size(); 
		int existingSessions = sessionStorage.getNumberOfNewWorkerSessions(); 
	
		results = results + "Sessions generated: " +totalSessions+"<br>"+ 
					"Total Sessions available now: "+existingSessions;

		System.out.println("Results: "+results);

		return results;
	}
	
	private String generateSingleWorkerSession(){

		String results = new String();
		PropertyManager manager = new PropertyManager();
		
		//Generate the stack of New and Duplicated WorkerSession
		WorkerSessionFactory sessionFactory = new WorkerSessionFactory();
		Stack<WorkerSession> originalStack = sessionFactory.generateSingleSession();
				
		WorkerSessionStorage sessionStorage = new WorkerSessionStorage();
		sessionStorage.appendNewWorkerSessionStack(originalStack,sessionStorage.NEW);
		
		int totalSessions = originalStack.size(); 
		int existingSessions = sessionStorage.getNumberOfNewWorkerSessions(); 
	
		results = results + "Sessions generated: " +totalSessions+"<br>"+ 
					"Total Sessions available now: "+existingSessions;

		System.out.println("Results: "+results);

		return results;		
	}
	
	private void delete(){
		String path = getServletContext().getRealPath("/");
		StorageManager manager = new StorageManager(path);
		manager.cleanUpRepositories();
	}
}

