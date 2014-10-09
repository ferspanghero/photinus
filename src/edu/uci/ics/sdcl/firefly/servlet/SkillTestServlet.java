package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.ServletException; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uci.ics.sdcl.firefly.MicrotaskContextFactory;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.controller.StorageManager;
import edu.uci.ics.sdcl.firefly.storage.WorkerStorage;
import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;

/**
 * Servlet implementation class SkillTestServlet
 * 
 * @author Christian Medeiros Adriano
 */
public class SkillTestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String QUESTION1="QUESTION1";
	public static final String QUESTION2="QUESTION2";
	public static final String QUESTION3="QUESTION3";
	public static final String QUESTION4="QUESTION4";
	//private String QUESTION5="QUESTION5";
	private Hashtable<String, String> rubricMap = new Hashtable<String,String>();

	private String SorryPage = "/SorryPage.jsp";
	private String ErrorPage = "/ErrorPage.jsp";
	private String QuestionMicrotaskPage = "/QuestionMicrotask.jsp";

	private String workerId;
	private WorkerStorage workerStorage;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SkillTestServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.workerId = request.getParameter("workerId");
		String subAction = request.getParameter("subAction");

		request.setAttribute("workerId", this.workerId);
		request.setAttribute("subAction", "submitAnswers");

		//First check if the worker hasn't already taken the test
		this.workerStorage =   WorkerStorage.initializeSingleton();;
		Worker worker = workerStorage.readExistingWorker(this.workerId);
		if(worker.hasTakenTest()){
			request.setAttribute("message", "Dear worker, you don't qualify to perform the task, because our system indicates that you have already taken this test. Please wait for the next round of experiments.");
			request.getRequestDispatcher(SorryPage).include(request, response);
		}
		else{
			if(subAction.compareTo("gradeAnswers")==0){
				int grade = this.processAnswers(request,worker);
				if (grade>=2){
					request.setAttribute("subAction", "loadFirst");
					loadFirstMicrotask(request,response);
				}
				else{ 
					request.setAttribute("message", "Dear worker, you didn't get the minimal qualifying grade to perform the task.");
					request.getRequestDispatcher(SorryPage).include(request, response);
				}
			}
		}
	}

	/**
	 * Not used
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}


	private int processAnswers(HttpServletRequest request, Worker worker){
		//Initialize rubric map
		rubricMap.put(QUESTION1,"c");
		rubricMap.put(QUESTION2,"a");
		rubricMap.put(QUESTION3,"d");
		rubricMap.put(QUESTION4,"b");

		//Retrieve time taken to answer
		String timeStamp = request.getParameter("timeStamp");
		String duration = TimeStampUtil.computeElapsedTime(timeStamp, TimeStampUtil.getTimeStampMillisec());

		//Retrieve answers
		Hashtable<String, String> answerMap = new Hashtable<String, String>();
		String answer1 = request.getParameter("QUESTION1");
		answerMap.put(QUESTION1, answer1);
		String answer2 = request.getParameter("QUESTION2");
		answerMap.put(QUESTION2, answer2);
		String answer3 = request.getParameter("QUESTION3");
		answerMap.put(QUESTION3, answer3);
		String answer4 = request.getParameter("QUESTION4");
		answerMap.put(QUESTION4, answer4);
		
		Hashtable<String, Boolean> gradeMap = this.gradeAnswers(answerMap);
		int grade = this.countCorrectAnswers(gradeMap);

		worker.setSkillAnswers(rubricMap,gradeMap,answerMap,grade, duration);
		workerStorage.insertSKillTest(worker);

		return grade;
	}

	private Hashtable<String,Boolean> gradeAnswers(Hashtable<String, String> answerMap){

		Hashtable<String, Boolean> gradeMap= new Hashtable<String, Boolean>();

		Boolean result=false;
		Iterator<String> keyIterator = answerMap.keySet().iterator();
		while( keyIterator.hasNext()){
			String key = keyIterator.next();
			String answer = answerMap.get(key);
			String rubric = rubricMap.get(key);
			if(answer!=null && answer.compareTo(rubric)==0)
				result=true;
			else
				result=false;
			gradeMap.put(key, result);
		}
		return gradeMap;
	}


	private int countCorrectAnswers(Hashtable<String, Boolean> gradeMap){
		int grade = 0;
		Iterator<String> keyIterator = gradeMap.keySet().iterator();
		while( keyIterator.hasNext()){
			String key = keyIterator.next();
			if(gradeMap.get(key))
				grade++;
		}
		return grade;
	}


	private void loadFirstMicrotask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		StorageManager manager = new StorageManager();
		WorkerSession  session = manager.readNewSession(this.workerId);

		if(session==null || !session.hasCurrent())
			//Means that it is the first worker session. There should be at least one microtask. If not it is an Error.
			showErrorPage(request, response,"@ SkillTestServlet - no microtask available");
		else{
			//Restore data for next Request
			request.setAttribute("sessionId",session.getId());
			request.setAttribute("timeStamp", TimeStampUtil.getTimeStampMillisec());
			
			//Load the new Microtask data into the Request
			request = MicrotaskServlet.generateRequest(request, session.getCurrentMicrotask());
			request.getRequestDispatcher(QuestionMicrotaskPage).forward(request, response);
		}
	}

	private void showErrorPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
		request.setAttribute("error", message);
		request.setAttribute("executionId", this.workerId);
		request.getRequestDispatcher(ErrorPage).include(request, response);
	}

}
