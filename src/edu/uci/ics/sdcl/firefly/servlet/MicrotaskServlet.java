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
import edu.uci.ics.sdcl.firefly.MicrotaskContextFactory;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.controller.StorageManager;
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
	private StorageManager manager ;
	private String workerId;

	private MicrotaskContextFactory workerSessionSelector;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MicrotaskServlet() {
		super();
		this.workerSessionSelector = new MicrotaskContextFactory();
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

		this.workerId = request.getParameter("workerId");

		//Restore data for next Request
		request.setAttribute("workerId",this.workerId);

		//String subAction = request.getParameter("subAction");

		manager = new StorageManager();
		String sessionId = manager.getSessionId(workerId);
		if(sessionId == null)
			loadFirstMicrotask(request, response);
		else
			loadNextMicrotask(request, response);
		
		/**
		if(subAction.compareTo("loadFirst")==0)
			loadFirstMicrotask(request, response);
		else
			if(subAction.compareTo("loadNext")==0)
				loadNextMicrotask(request, response);
			else
				showErrorPage(request, response,"@ MicrotaskServlet - subAction not recognized");
				*/
	}


	private void loadFirstMicrotask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		WorkerSession  session = manager.readNewSession(this.workerId);

		if(session==null || !session.hasCurrent())
			//Means that it is the first worker session. There should be at least one microtask. If not it is an Error.
			showErrorPage(request, response,"@ MicrotaskServlet - no microtask available");
		else{
			//Restore data for next Request
			request.setAttribute("timeStamp", TimeStampUtil.getTimeStampMillisec());

			//load the new Microtask data into the Request
			request = MicrotaskServlet.generateRequest(request, session.getCurrentMicrotask());
			request.getRequestDispatcher(QuestionMicrotaskPage).include(request, response);
		}
	}


	private void loadNextMicrotask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int answer = new Integer(request.getParameter("answer")).intValue();
		String microtaskId = request.getParameter("microtaskId");
		StorageManager manager = new StorageManager();
		
		String sessionId = manager.getSessionId(this.workerId); //request.getParameter("sessionId");
		String explanation = request.getParameter("explanation");
		String timeStamp = request.getParameter("timeStamp");
		String elapsedTime = TimeStampUtil.computeElapsedTime(timeStamp, TimeStampUtil.getTimeStampMillisec());


		//Save answers from the previous microtask
		
		boolean success = manager.updateMicrotaskAnswer(sessionId, new Integer(microtaskId),
				new Answer(Answer.mapToString(answer),explanation, this.workerId, elapsedTime, timeStamp));

		if(!success){
			this.showErrorPage(request, response, "Your answer could not be stored. In case you have used the back button, "
					+ "please restart the HIT by going to the following link: http://dellserver.ics.uci.edu:8080/firefly/ConsentForm.jsp");
		}
		else{
			//Restore data for next Request
			request.setAttribute("timeStamp", TimeStampUtil.getTimeStampMillisec());

			//Continue working on existing session
			WorkerSession session = manager.readActiveSession(sessionId);	

			//Decide where to send to send the worker
			if(session==null || !session.hasCurrent())
				//No more microtasks, move to the Survey page
				request.getRequestDispatcher(SurveyPage).include(request, response);
			else{
				//Displays a new microtask
				request = MicrotaskServlet.generateRequest(request, session.getCurrentMicrotask());
				request.getRequestDispatcher(QuestionMicrotaskPage).include(request, response);
			}
		}
	}



	private void showErrorPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
		request.setAttribute("error", message);
		request.setAttribute("executionId", this.workerId);
		request.getRequestDispatcher(ErrorPage).include(request, response);
	}

	
	
	/** Populate needed attributes from the microtask in the HTTP request 
	 * 
	 * @param request
	 * @param task
	 * @return the new request with data to be displayed on the web page
	 */
	public static HttpServletRequest generateRequest(HttpServletRequest request, Microtask task){

		//System.out.println("Retrieved microtask id:"+task.getID()+" answers: "+task.getAnswerList().toString());
		//	System.out.println("Retrieved microtask bug report:" + task.getFailureDescription() +  " from fileName: "+task.getCodeSnippet().getFileName());

		request.setAttribute("microtaskId", task.getID());  
	
		request.setAttribute("bugReport", task.getFailureDescription());
		request.setAttribute("question", task.getQuestion());
		
		//set source code of codeSnippet - First ACE Editor
		request.setAttribute("source", task.getCodeSnippet().getCodeSnippetFromFileContent()); 	
		request.setAttribute("sourceLOCS", task.getCodeSnippet().getLOCS());
		request.setAttribute("calleesInMain", task.getSnippetHightlights());

		//sets caller codeSnippets - Second ACE Editor
		request.setAttribute("callerLOCS", task.getCallerLOCS());
		request.setAttribute("caller", task.getCallerFileContent());
		request.setAttribute("positionsCaller", task.getCallerHightlights());

		//sets callee codeSnippets - Third ACE Editor
		request.setAttribute("callee", task.getCalleeFileContent());
		request.setAttribute("calleeLOCS", task.getCalleeLOCS());
		request.setAttribute("positionsCallee", task.getCalleeHightlights());
		
		request.setAttribute("explanation",""); //clean up the explanation field.
		request.setAttribute("startLine", task.getStartingLine());
		request.setAttribute("startColumn", task.getStartingColumn());
		request.setAttribute("endLine", task.getEndingLine());
		request.setAttribute("endColumn", task.getEndingColumn());

		request.setAttribute("methodStartingLine", task.getCodeSnippet().getElementStartingLine());

		return request;
	}


}
