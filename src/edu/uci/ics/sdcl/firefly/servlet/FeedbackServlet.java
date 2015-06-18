package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException; 

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uci.ics.sdcl.firefly.MicrotaskContextFactory;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.controller.StorageStrategy;

/**
 * Servlet implementation class MicrotaskController
 * 
 *  * @author Christian Medeiros Adriano
 */
public class FeedbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String ErrorPage = "/ErrorPage.jsp";
	private String ThanksPage = "/Thanks.jsp";
	private StorageStrategy storage ;
	private String workerId;
	private String feedback;

	private MicrotaskContextFactory workerSessionSelector;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FeedbackServlet() {
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
		if(request.getParameter("feedback")!=null){
			this.feedback = request.getParameter("feedback");
			request.setAttribute("feedback", this.feedback); 
			//Restore data for next Request
			request.setAttribute("key",request.getParameter("key"));

			storage = StorageStrategy.initializeSingleton();
			storage.insertFeedback(this.feedback);
			request.getRequestDispatcher(ThanksPage).include(request, response);
		}
		

	}

	private void showErrorPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
		request.setAttribute("error", message);
		request.setAttribute("executionId", this.workerId);
		request.getRequestDispatcher(ErrorPage).include(request, response);
	}

}
