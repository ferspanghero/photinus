package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException; 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.controller.StorageManager;
import edu.uci.ics.sdcl.firefly.controller.WorkerSessionSelector;
import edu.uci.ics.sdcl.firefly.util.PositionFinder;
import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;

/**
 * Servlet implementation class MicrotaskController
 * 
 *  * @author Christian Medeiros Adriano
 */
public class MicrotaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String SurveyPage = "/Survey.jsp";
	private String ErrorPage = "/ErrorPage.jsp";
	private String QuestionMicrotaskPage = "/QuestionMicrotask.jsp";
 
	private String userId;
	
	private WorkerSessionSelector workerSessionSelector;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MicrotaskServlet() {
		super();
		this.workerSessionSelector = new WorkerSessionSelector();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
	
	/**
	 * 	Collects and persist the answer. Also marks the microtask as already answered
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.userId = request.getParameter("userId");
		
		//Restore data for next Request
		request.setAttribute("userId",this.userId);
		
		String subAction = request.getParameter("subAction");
				
		if(subAction.compareTo("loadFirst")==0)
			loadFirstMicrotask(request, response);
		else
			if(subAction.compareTo("loadNext")==0)
				loadNextMicrotask(request, response);
			else
				showErrorPage(request, response,"@ MicrotaskServlet - subAction not recognized");

	}
	
	
	private void loadFirstMicrotask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		StorageManager manager = new StorageManager();
		WorkerSession  session = manager.readNewSession(this.userId);
		
		if(session==null || !session.hasCurrent())
		 	//Means that it is the first user session. There should be at least one microtask. If not it is an Error.
			showErrorPage(request, response,"@ MicrotaskServlet - no microtask available");
		else{
			//Restore data for next Request
			request.setAttribute("timeStamp", TimeStampUtil.getTimeStampMillisec());
			request.setAttribute("sessionId",session.getId());
				
			//load the new Microtask data into the Request
			request = this.workerSessionSelector.generateRequest(request, session.getCurrentMicrotask());
			request.getRequestDispatcher(QuestionMicrotaskPage).include(request, response);
		}
	}
	
	
	private void loadNextMicrotask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		int answer = new Integer(request.getParameter("answer")).intValue();
		String microtaskId = request.getParameter("microtaskId");
		String sessionId = request.getParameter("sessionId");
		String explanation = request.getParameter("explanation");
	 	String fileName = request.getParameter("fileName");
	 	String timeStamp = request.getParameter("timeStamp");
	 	String elapsedTime = TimeStampUtil.computeElapsedTime(timeStamp, TimeStampUtil.getTimeStampMillisec());
	 	
	 			
		//Save answers from the previous microtask
		StorageManager manager = new StorageManager();
		manager.updateMicrotaskAnswer(fileName, sessionId, new Integer(microtaskId), new Answer(Answer.mapToString(answer),explanation), elapsedTime, timeStamp);

		//Restore data for next Request
		request.setAttribute("timeStamp", TimeStampUtil.getTimeStampMillisec());
		request.setAttribute("sessionId", sessionId);
		
		//Continue working on existing session
		WorkerSession session = manager.readActiveSession(sessionId);	
		
		//Decide where to send to send the user
		if(session==null || !session.hasCurrent())
			//No more microtasks, move to the Survey page
			request.getRequestDispatcher(SurveyPage).include(request, response);
		else{
			//Displays a new microtask
			request = this.workerSessionSelector.generateRequest(request, session.getCurrentMicrotask());
			request.getRequestDispatcher(QuestionMicrotaskPage).include(request, response);
		}
	}
	
	
	
	private void showErrorPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
		request.setAttribute("error", message);
		request.getRequestDispatcher(ErrorPage).include(request, response);
	}
	


}
