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
	private StorageStrategy storage;

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

		String subAction = request.getParameter("subAction");
		String fileName = request.getParameter("fileName");
		System.out.println("in ConsentServlet, fileName requested: "+fileName);
		storage = StorageStrategy.initializeSingleton();
		
		if(subAction.compareTo("loadQuestions")==0){
			if(!storage.areThereMicrotasksAvailable()){
				this.showErrorPage(request, response, "Dear contributor, no more tasks are available. Please wait for the next batch of HITs");
			}
			else{
				Worker worker = null;
				Cookie workerCookie = validateWorker(request);
				if(workerCookie != null)
				{
					worker = storage.readExistingWorker(workerCookie.getValue());
					if(worker == null)
						newWorker(request, response, worker, fileName);
					else
						existingWorker(request, response, worker, fileName);
				}
				else
					newWorker(request, response, worker, fileName);
			}
		}
		else
			this.showErrorPage(request, response, "action not recognized: "+ subAction);
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
	
	private void loadFirstMicrotask(HttpServletRequest request, HttpServletResponse response, Worker worker, String fileName, String page) throws ServletException, IOException {
		storage = StorageStrategy.initializeSingleton();
		WorkerSession  session = storage.readNewSession(worker.getWorkerId(), fileName);
		//System.out.println("loadFirstMicrotask, session= "+session);
		if(session==null || session.isClosed())
			//Means that it is the first worker session. There should be at least one microtask. If not it is an Error.
			showErrorPage(request, response,"@ SkillTestServlet - no microtask available");
		else{
			// Sets the workerID cookie
			response.addCookie(new Cookie("w", worker.getWorkerId()));
			//Restore data for next Request
			request.setAttribute("timeStamp", TimeStampUtil.getTimeStampMillisec());
			request.setAttribute("workerId", worker.getWorkerId());
			request.setAttribute("sessionId", session.getId());
			
			//Load the new Microtask data into the Request
			request = MicrotaskServlet.generateRequest(request, storage.getNextMicrotask(session.getId()));
			request.getRequestDispatcher(page).forward(request, response);
		}
	}
	
	private void newWorker(HttpServletRequest request, HttpServletResponse response, Worker worker, String fileName) throws ServletException, IOException
	{
		String consentDateStr= TimeStampUtil.getTimeStampMillisec();
		worker = storage.insertConsent(consentDateStr,fileName);
		surveyPage(request, response, worker, fileName);
	}
	
	private void existingWorker(HttpServletRequest request, HttpServletResponse response, Worker worker, String fileName) throws ServletException, IOException
	{
		questionPage(request, response, worker, fileName);
	}
	
	private void surveyPage(HttpServletRequest request, HttpServletResponse response, Worker worker, String fileName) throws ServletException, IOException
	{
		loadFirstMicrotask(request, response, worker, fileName, SurveyPage);
	}
	
	private void questionPage(HttpServletRequest request, HttpServletResponse response, Worker worker, String fileName) throws ServletException, IOException
	{
		loadFirstMicrotask(request, response, worker, fileName, QuestionMicrotaskPage);
	}
	
	private void showErrorPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
		request.setAttribute("error", message);
		request.setAttribute("executionId", "before consent");
		request.getRequestDispatcher(ErrorPage).forward(request, response);
	}
}
