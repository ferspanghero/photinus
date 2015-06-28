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

	private String ThanksPage = "/Thanks.jsp";
	private StorageStrategy storage ;

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
			String feedback = request.getParameter("feedback");
			if(feedback!=null)
				feedback = feedback.replaceAll("[\n\r]", " ");
			request.setAttribute("feedback", feedback); 
			//Restore data for next Request
			request.setAttribute("key",request.getParameter("key"));
			String workerId = request.getParameter("workerId");

			storage = StorageStrategy.initializeSingleton();
			Worker worker = storage.readExistingWorker(workerId);
			storage.insertFeedback(feedback , worker);
			request.getRequestDispatcher(ThanksPage).include(request, response);
		}
		

	}

}
