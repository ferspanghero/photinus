package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.controller.StorageStrategy;
import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;

/**
 * Servlet implementation class ConsentServlet
 */
public class ConsentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private String ErrorPage = "/ErrorPage.jsp";
	private final String SurveyPage = "/Survey.jsp";
	private final String QuestionMicrotaskPage = "/QuestionMicrotask.jsp";
	private final String SorryPage = "/SorryPage.jsp";
	private StorageStrategy storage;
	private String hasAlreadyTakenTask ="Dear participant, you have already taken this task,"
			+ " please obtain the link for a new task. "
			+ "In case you have doubts, please send an email"
			+ " to adrianoc@uci.edu.";
	
	private String fileName;
	private String consentDateStr;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ConsentServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.fileName = request.getParameter("fileName");
		
		storage = StorageStrategy.initializeSingleton();
		this.consentDateStr= TimeStampUtil.getTimeStampMillisec();	
		if(!storage.areThereMicrotasksAvailable()){
			this.showErrorPage(request, response, "Dear contributor, no more tasks are available. Please wait for the next batch of HITs");
		}
		else if(!storage.isFileAvailable(fileName))
		{
			showErrorPage(request, response, "Dear worker, at this moment there are no questions available for the requested file.");
		}
		else{
			Worker worker = null;
			Cookie workerCookie = validateWorker(request);
			if(workerCookie != null)
			{
				worker = storage.readExistingWorker(workerCookie.getValue());
				if(worker == null)
					serveNewWorker(request, response);
				else
					serveExistingWorker(request, response, worker);
			}
			else
				serveNewWorker(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
	
	private Cookie validateWorker(HttpServletRequest request)
	{
		String authCookie = "w";
		Cookie result = null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null)
		{
			for(Cookie c : cookies)
			{
				if(authCookie.equals(c.getName()))
				{
					result = c;
					break;
				}
			}
		}
		return result;
	}
	
	/** When worker has already taken the test. The worker is allowed to go straight to the microtask page */
	private void loadFirstMicrotask(HttpServletRequest request, HttpServletResponse response, Worker worker, 
			String fileName, String page) throws ServletException, IOException 
	{		
		worker.addFileHistory(fileName);
		WorkerSession  session = storage.readNewSession( worker.getWorkerId(), fileName);
		// Sets the workerID cookie
		response.addCookie(new Cookie("w", worker.getWorkerId()));
		//Restore data for next Request
		request.setAttribute("timeStamp", TimeStampUtil.getTimeStampMillisec());
		request.setAttribute("workerId", worker.getWorkerId());
		request.setAttribute("sessionId", session.getId());
		//Data for the Progress bar
		request.setAttribute("currentTask", session.getCurrentIndexPlus());
		request.setAttribute("totalTasks", session.getMicrotaskListSize());
		
		//Load the new Microtask data into the Request
		request = MicrotaskServlet.generateRequest(request, storage.getNextMicrotask(session.getId()));
		request.getRequestDispatcher(page).forward(request, response);
	}
	
	/** When worker has not taken the test yet, the worker has to first fill in a survey. */
	private void loadSurveyPage(HttpServletRequest request, HttpServletResponse response, Worker worker, 
			String fileName, String page) throws ServletException, IOException 
	{
		if(worker.isAllowedFile(fileName)){
			worker.addFileHistory(fileName);
			// Sets the workerID cookie
			response.addCookie(new Cookie("w", worker.getWorkerId()));
			//Restore data for next Request
			request.setAttribute("timeStamp", TimeStampUtil.getTimeStampMillisec());
			request.setAttribute("workerId", worker.getWorkerId());
			request.setAttribute("fileName", fileName);//don't need that.

			//Load the survey page
			request.getRequestDispatcher(page).forward(request, response);
		} 	
	}
	
	
	private void serveNewWorker(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Worker worker = storage.insertNewWorker(consentDateStr,fileName);
		if(storage.insertConsent(worker, consentDateStr,fileName)){
			loadSurveyPage(request, response, worker, fileName, SurveyPage);
		}
		else{
			showErrorPage(request, response, ErrorPage);
		}
	}
	
	private void serveExistingWorker(HttpServletRequest request, HttpServletResponse response, Worker worker) throws ServletException, IOException
	{
		if(worker.isAllowedFile(fileName) && (worker.hasPassedTest())){
			storage.insertConsent(worker, consentDateStr,fileName);
			loadFirstMicrotask(request, response, worker, fileName, QuestionMicrotaskPage);
		}
		else
			if(worker.isAllowedFile(fileName) && !(worker.hasPassedTest())){ //The worker will have a second chance to take the test for a different HIT
				storage.insertConsent(worker, consentDateStr,fileName);
				loadSurveyPage(request, response, worker, fileName, SurveyPage);
			}
		else
			if(!worker.isAllowedFile(fileName)){
				sorryPage(request, response,this.hasAlreadyTakenTask);
			}
			else 
			if(!worker.hasPassedTest()){ 
				sorryPage(request, response, "Dear worker, you didn't get the minimal qualifying grade to perform this task." );		
			}			
	}
	
	
	private void showErrorPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
		request.setAttribute("error", message);
		request.setAttribute("executionId", "before consent");
		request.getRequestDispatcher(ErrorPage).forward(request, response);
	}
	
	private void sorryPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException
	{
		request.setAttribute("message", message);
		request.getRequestDispatcher(SorryPage).include(request, response);
	}
}
