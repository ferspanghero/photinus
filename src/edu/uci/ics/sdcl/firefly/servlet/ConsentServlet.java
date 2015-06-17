package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.controller.StorageStrategy;
import edu.uci.ics.sdcl.firefly.storage.SkillTestSource;
import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;

/**
 * Servlet implementation class ConsentServlet
 */
public class ConsentServlet extends HttpServlet {

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
		String fileName = request.getParameter("fileName");
		System.out.println("in ConsentServlet, fileName requested: "+fileName);
		StorageStrategy storage = StorageStrategy.initializeSingleton();
		
		if(subAction.compareTo("loadQuestions")==0){
			if(!storage.areThereMicrotasksAvailable()){
				this.showErrorPage(request, response, "Dear contributor, no more tasks are available. Please wait for the next batch of HITs");
			}
			else{
				String consentDateStr= TimeStampUtil.getTimeStampMillisec();
				Worker worker = storage.insertConsent(consentDateStr,fileName);
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
