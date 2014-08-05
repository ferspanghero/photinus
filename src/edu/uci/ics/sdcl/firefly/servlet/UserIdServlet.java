package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uci.ics.sdcl.firefly.ScreeningTest;

/**
 * Servlet implementation class UserIdServlet
 */
@WebServlet("/UserIdServlet")
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
		String userId = request.getParameter("userId");	
		String hitId = request.getParameter("hitId");
		Date currentDate = new Date();
		ScreeningTest testSubject = new ScreeningTest(userId, hitId, currentDate);
		System.out.println("User Id: " + testSubject.getUserId());
		System.out.println("HIT Id: " + testSubject.getHitId());
		System.out.println("Date: " + testSubject.getConsentDate());
		request.getRequestDispatcher("/Survey.jsp").include(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
