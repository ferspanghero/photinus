package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uci.ics.sdcl.firefly.Worker;

import edu.uci.ics.sdcl.firefly.storage.WorkerStorage;

/**
 * Servlet implementation class SkillTestServlet
 */
//@WebServlet("/skillTest")
public class SkillTestServlet extends HttpServlet {
	
		
	private static final long serialVersionUID = 1L;
    
	private String QUESTION1="QUESTION1";
	private String QUESTION2="QUESTION2";
	private String QUESTION3="QUESTION3";
	private String QUESTION4="QUESTION4";
	private String QUESTION5="QUESTION5";
	private HashMap<String, String> rubricMap = new HashMap<String,String>();
	
	
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
		
		String userId = request.getParameter("userId");
		String hitId = request.getParameter("hitId");
		String subAction = request.getParameter("subAction");
		
		request.setAttribute("userId", userId);
		request.setAttribute("hitId", hitId);
		request.setAttribute("subAction", "submitAnswers");
		
 
		if(subAction.compareTo("gradeAnswers")==0){
			int grade = this.retrieveAnswers(request, userId);
			if (grade>=3){
				request.setAttribute("userId", userId);
				request.setAttribute("hitId", hitId);
				request.setAttribute("subAction", "loadFirst");
				request.getRequestDispatcher("/QuestionMicrotask.jsp").include(request, response);
			}
			else{ 
				request.getRequestDispatcher("/Sorry.html").include(request, response);
			}
		}
	}

	/**
	 * Not used
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	

	
		
	private int retrieveAnswers(HttpServletRequest request, String userId){
		//Initialize rubric map
		rubricMap.put(QUESTION1,"a");
		rubricMap.put(QUESTION2,"a");
		rubricMap.put(QUESTION3,"a");
		rubricMap.put(QUESTION4,"a");
		rubricMap.put(QUESTION5,"a");
		
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
		String answer5 = request.getParameter("question5");
		answerMap.put(QUESTION5, answer5);
		
		HashMap<String, Boolean> gradeMap = this.gradeAnswers(answerMap);
		int grade = this.countCorrectAnswers(gradeMap);
		WorkerStorage workerStorage =  new WorkerStorage();
		Worker worker = workerStorage.read(userId);
		worker.setSkillAnswers(rubricMap,gradeMap,grade);
		workerStorage.insert(userId, worker);

		return grade;
	}
	
	private HashMap<String,Boolean> gradeAnswers(HashMap<String, String> answerMap){
		
		HashMap<String, Boolean> gradeMap= new HashMap<String, Boolean>();
		
		Boolean result=false;
		int grade = 0;
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
}
