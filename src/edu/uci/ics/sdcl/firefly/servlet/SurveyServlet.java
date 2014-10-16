package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.controller.StorageManager;
import edu.uci.ics.sdcl.firefly.storage.WorkerStorage;

/**
 * Servlet implementation class SurveyServlet
 */
public class SurveyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public static String question[] = {"Gender", "Age", "Country", "Years progr.", "Difficulty", "Feedback"};   

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SurveyServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WorkerStorage subjectStore =  WorkerStorage.initializeSingleton();	// to retrieve date from database
		Worker subject = subjectStore.readExistingWorker(request.getParameter("workerId"));
		if (null != subject){
			subject.addSurveyAnswer(question[0], request.getParameter("gender"));
			subject.addSurveyAnswer(question[1], request.getParameter("age"));
			subject.addSurveyAnswer(question[2], request.getParameter("country"));
			subject.addSurveyAnswer(question[3], request.getParameter("experience"));
			subject.addSurveyAnswer(question[4], request.getParameter("difficulty"));
			subject.addSurveyAnswer(question[5], request.getParameter("feedback"));
			
			//Store result
			subjectStore.insertSurvey(request.getParameter("workerId"), subject);
			
			//Displays the Thanks message		
			request.setAttribute("key", subject.getSessionId());
			request.getRequestDispatcher("/Thanks.jsp").forward(request, response);
		} else{
			request.setAttribute("executionId", request.getParameter("workerId"));
			request.setAttribute("error", "@SurveyServlet - object 'subject' is null");
			//Displays the error page
			request.getRequestDispatcher("/ErrorPage.jsp").forward(request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
