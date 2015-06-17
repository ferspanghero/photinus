package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;










import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.controller.StorageManager;
import edu.uci.ics.sdcl.firefly.controller.StorageStrategy;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;
import edu.uci.ics.sdcl.firefly.storage.SkillTestSource;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerStorage;
import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;

/**
 * Servlet implementation class ConsentServlet
 */
public class ConsentServlet extends HttpServlet {

	private String SkillTestPage = "/SkillTest.jsp";
	private String ErrorPage = "/ErrorPage.jsp";
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
						loadFirstMicrotask(request, response, worker);
				}
				else
					newWorker(request, response, worker, fileName);
			}
		}
		else{
			this.showErrorPage(request, response, "action not recognized: "+ subAction);
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	private HttpServletRequest loadQuestions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SkillTestSource source = new SkillTestSource();
		request.setAttribute("editor1", source.getSourceOne());
		//request.setAttribute("editor2", source.getSourceTwo());
		request.setAttribute("subAction", "gradeAnswers");
		return request;
	}
	
	private void showErrorPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
		request.setAttribute("error", message);
		request.setAttribute("executionId", "before consent");
		request.getRequestDispatcher(ErrorPage).forward(request, response);
	}
	
	private Cookie validateWorker(HttpServletRequest request)
	{
		String authCookie = "w";
		Cookie result = null;
		for(Cookie c : request.getCookies())
		{
			if(authCookie.equals(c.getName()))
			{
				result = c;
				break;
			}
		}
		return result;
	}
	
	private void loadFirstMicrotask(HttpServletRequest request, HttpServletResponse response, Worker worker) throws ServletException, IOException {
		storage = StorageStrategy.initializeSingleton();
		WorkerSession  session = storage.readNewSession(worker.getWorkerId(), worker.getCurrentFileName());
		//System.out.println("loadFirstMicrotask, session= "+session);
		if(session==null || session.isClosed())
			//Means that it is the first worker session. There should be at least one microtask. If not it is an Error.
			showErrorPage(request, response,"@ SkillTestServlet - no microtask available");
		else{
			//Restore data for next Request
			request.setAttribute("timeStamp", TimeStampUtil.getTimeStampMillisec());

			//Load the new Microtask data into the Request
			request = MicrotaskServlet.generateRequest(request, storage.getNextMicrotask(session.getId()));
			request.getRequestDispatcher(QuestionMicrotaskPage).forward(request, response);
		}
	}
	
	private void newWorker(HttpServletRequest request, HttpServletResponse response, Worker worker, String fileName) throws ServletException, IOException
	{
		String consentDateStr= TimeStampUtil.getTimeStampMillisec();
		worker = storage.insertConsent(consentDateStr,fileName);
		// now passing parameters to the next page
		request.setAttribute("workerId", worker.getWorkerId().toString());
		request.setAttribute("subAction", "gradeAnswers");
		response.addCookie(new Cookie("w", worker.getWorkerId()));
		request = this.loadQuestions(request, response);
		request.getRequestDispatcher(SkillTestPage).forward(request, response);
	}

}
