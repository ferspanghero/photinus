package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.storage.SkillTestSource;
import edu.uci.ics.sdcl.firefly.storage.WorkerStorage;

/**
 * Servlet implementation class UserIdServlet
 */
//@WebServlet("/UserIdServlet")
public class UserIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserIdServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String subAction = request.getParameter("subAction");
		
		if(subAction.compareTo("consentObtained") == 0){
			Date currentDate = new Date();
			request.setAttribute("dateOfConsent", currentDate.toString());
			request.getRequestDispatcher("/UserId.jsp").forward(request, response);
		}
		else
		if(subAction.compareTo("loadQuestions")==0){
				String userId = request.getParameter("userId");	
				String hitId = request.getParameter("hitId");
				//TODO Check whether the Worker isn't already in the database
				
				String dateOfConsent = request.getParameter("dateOfConsent");
				Date currentDate;
				try{
					currentDate = DateFormat.getDateInstance().parse(dateOfConsent);
				}
				catch(ParseException e){
					currentDate = new Date();
				}
				Worker testSubject = new Worker(userId, hitId, currentDate);
				WorkerStorage consentStore = new WorkerStorage();
				consentStore.insert(userId, testSubject);
				System.out.println("User Id: " + consentStore.read(userId).getUserId());
				System.out.println("HIT Id: " + consentStore.read(userId).getHitId());
				System.out.println("Date: " + consentStore.read(userId).getConsentDate());
				// now passing parameters to the next page
				request.setAttribute("userId", userId);
				request.setAttribute("hitId", hitId);
				request.setAttribute("subAction", "gradeAnswers");
				request = this.loadQuestions(request, response);
				request.getRequestDispatcher("/SkillTest.jsp").include(request, response);
			}
			else{
				//Error page
			}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	private HttpServletRequest loadQuestions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SkillTestSource source = new SkillTestSource();
		request.setAttribute("editor1", source.getSourceOne());
		request.setAttribute("editor2", source.getSourceTwo());
		request.setAttribute("subAction", "gradeAnswers");
		return request;
	}
	
}
