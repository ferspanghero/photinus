package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.controller.StorageManager;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;
import edu.uci.ics.sdcl.firefly.storage.SkillTestSource;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerStorage;
import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;

/**
 * Servlet implementation class ConsentServlet
 */
public class ConsentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String SkillTestPage = "/SkillTest.jsp";
	private String ErrorPage = "/ErrorPage.jsp";

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

		if(subAction.compareTo("loadQuestions")==0){
			WorkerSessionStorage sessionStorage = WorkerSessionStorage.initializeSingleton();
			if(sessionStorage.getNumberOfNewWorkerSessions()<=0){
				this.showErrorPage(request, response, "Dear contributor, there aren't any more tasks available. Please wait for the next batch of HITs");
			}
			else{
				String consentDateStr= TimeStampUtil.getTimeStampMillisec();
				WorkerStorage workerStorage = WorkerStorage.initializeSingleton();
				Worker worker = workerStorage.generateNewWorker(consentDateStr);
				// now passing parameters to the next page
				request.setAttribute("workerId", worker.getWorkerId().toString());
				request.setAttribute("subAction", "gradeAnswers");
				request = this.loadQuestions(request, response);
				request.getRequestDispatcher(SkillTestPage).forward(request, response);
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

}
