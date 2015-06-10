package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException; 
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.MicrotaskContextFactory;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.controller.StorageStrategy;
import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;

/**
 * Servlet implementation class MicrotaskController
 * 
 *  * @author Christian Medeiros Adriano
 */
public class QuitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String QuitPage = "/QuitReason.jsp";
	private String ErrorPage = "/ErrorPage.jsp";
	private String QuestionMicrotaskPage = "/QuestionMicrotask.jsp";
	private StorageStrategy storage ;
	private String workerId;
	private String answer;
	
	public static final String HARD="THE TASK IS TOO HARD";
	public static final String BORING="THE TASK IS TOO BORING";
	public static final String LONG="THE TASK IS TOO LONG";
	public static final String OTHER="OTHER";

	private MicrotaskContextFactory workerSessionSelector;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QuitServlet() {
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
		//System.out.println("In MicrotaskServlet ");
		if(request.getParameter("workerId").equals("consentForm")){
			request.setAttribute("workerId", request.getParameter("workerId")); 
			request.getRequestDispatcher(QuitPage).include(request, response);
			storage = StorageStrategy.initializeSingleton();
			storage.insertQuitReason(null, "consentForm");
		}else if(request.getParameter("reason")!=null){
			this.workerId = request.getParameter("workerId");
			System.out.println("Worker ------------------ " + this.workerId);
			this.answer = mapAnswerValue(request.getParameter("reason"));
			//Restore data for next Request
			request.setAttribute("workerId",this.workerId);

			//String subAction = request.getParameter("subAction");

			storage = StorageStrategy.initializeSingleton();
			String sessionId = storage.getSessionIdForWorker(workerId);
			Worker worker = storage.readExistingWorker(this.workerId);
			storage.insertQuitReason(worker, this.answer);
		}else{
			request.setAttribute("workerId", request.getParameter("workerId")); 
			request.getRequestDispatcher(QuitPage).include(request, response);
		}
		

	}

	private void showErrorPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
		request.setAttribute("error", message);
		request.setAttribute("executionId", this.workerId);
		request.getRequestDispatcher(ErrorPage).include(request, response);
	}
	
	protected String mapAnswerValue(String answer){
		if(answer == "hard"){
			return HARD;
		}else if(answer=="boring"){
			return BORING;
		}else if(answer=="long"){
			return LONG;
		}else{
			return OTHER;
		}
	}

}
