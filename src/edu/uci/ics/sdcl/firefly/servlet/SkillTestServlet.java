package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uci.ics.sdcl.firefly.TimeStampUtil;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.controller.StorageManager;
import edu.uci.ics.sdcl.firefly.controller.WorkerSessionSelector;
import edu.uci.ics.sdcl.firefly.storage.WorkerStorage;

/**
 * Servlet implementation class SkillTestServlet
 */
public class SkillTestServlet extends HttpServlet {
	
		
	private static final long serialVersionUID = 1L;
    
	private String QUESTION1="QUESTION1";
	private String QUESTION2="QUESTION2";
	private String QUESTION3="QUESTION3";
	private String QUESTION4="QUESTION4";
	//private String QUESTION5="QUESTION5";
	private HashMap<String, String> rubricMap = new HashMap<String,String>();

	private String SorryPage = "/Sorry.html";
	private String ErrorPage = "/ErrorPage.jsp";
	private String QuestionMicrotaskPage = "/QuestionMicrotask.jsp";
 
	private String userId;
	private String hitId;
	
	
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
		
		this.userId = request.getParameter("userId");
		this.hitId = request.getParameter("hitId");
		String subAction = request.getParameter("subAction");
		
		request.setAttribute("userId", this.userId);
		request.setAttribute("hitId", this.hitId);
		request.setAttribute("subAction", "submitAnswers");
 
		if(subAction.compareTo("gradeAnswers")==0){
			int grade = this.retrieveAnswers(request);
			if (grade>=2){
				request.setAttribute("subAction", "loadFirst");
				loadFirstMicrotask(request,response);
			}
			else{ 
				request.getRequestDispatcher(SorryPage).include(request, response);
			}
		}
	}

	/**
	 * Not used
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

		
	private int retrieveAnswers(HttpServletRequest request){
		//Initialize rubric map
		rubricMap.put(QUESTION1,"c");
		rubricMap.put(QUESTION2,"a");
		rubricMap.put(QUESTION3,"d");
		rubricMap.put(QUESTION4,"b");
		//rubricMap.put(QUESTION5,"c");
		
		//Retrieve time taken to answer
		String timeStamp = request.getParameter("timeStamp");
	 	String duration = TimeStampUtil.computeElapsedTime(timeStamp, TimeStampUtil.getTimeStampMillisec());
		
		//Retrieve answers
		HashMap<String, String> answerMap = new HashMap<String, String>();
		String answer1 = request.getParameter("question1");
		answerMap.put(QUESTION1, answer1);
		String answer2 = request.getParameter("question2");
		answerMap.put(QUESTION2, answer2);
		String answer3 = request.getParameter("question3");
		answerMap.put(QUESTION3, answer3);
		String answer4 = request.getParameter("question4");
		answerMap.put(QUESTION4, answer4);
		//String answer5 = request.getParameter("question5");
		//answerMap.put(QUESTION5, answer5);
		HashMap<String, Boolean> gradeMap = this.gradeAnswers(answerMap);
		int grade = this.countCorrectAnswers(gradeMap);
		WorkerStorage workerStorage =  new WorkerStorage();

		Worker worker = workerStorage.readSingleWorker(this.userId);

		worker.setSkillAnswers(rubricMap,gradeMap,grade, duration);
		workerStorage.insert(userId, worker);

		return grade;
	}
	
	private HashMap<String,Boolean> gradeAnswers(HashMap<String, String> answerMap){
		
		HashMap<String, Boolean> gradeMap= new HashMap<String, Boolean>();
		
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
	
	
	private int countCorrectAnswers(HashMap<String, Boolean> gradeMap){
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
		String path = getServletContext().getRealPath("/");
		StorageManager manager = new StorageManager(path);
		WorkerSession  session = manager.readNewSession(this.userId, this.hitId);
		
		if(session==null || !session.hasCurrent())
		 	//Means that it is the first user session. There should be at least one microtask. If not it is an Error.
			showErrorPage(request, response,"@ SkillTestServlet - no microtask available");
		else{
			//Restore data for next Request
			request.setAttribute("sessionId",session.getId());
			request.setAttribute("timeStamp", TimeStampUtil.getTimeStampMillisec());
			WorkerSessionSelector selector = new WorkerSessionSelector();
			//load the new Microtask data into the Request
			request = selector.generateRequest(request, session.getCurrentMicrotask());
			request.getRequestDispatcher(QuestionMicrotaskPage).include(request, response);
		}
	}
	
	private void showErrorPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
		request.setAttribute("error", message);
		request.getRequestDispatcher(ErrorPage).include(request, response);
	}
	
	
	
}
