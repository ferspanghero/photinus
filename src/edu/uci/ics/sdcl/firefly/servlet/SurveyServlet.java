package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.controller.StorageStrategy;

/**
 * Servlet implementation class SurveyServlet
 */
public class SurveyServlet extends HttpServlet {
    //public static String question[] = {"Gender", "Age", "Country", "Years progr.", "Difficulty", "Feedback"};   
    public static String question[] = {"Difficulty", "Feedback"};   
    public SurveyServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StorageStrategy storage = StorageStrategy.initializeSingleton();
		Worker subject = storage.readExistingWorker(request.getParameter("workerId"));
		if (null != subject){
//			subject.addSurveyAnswer(question[0], request.getParameter("gender"));
//			subject.addSurveyAnswer(question[1], request.getParameter("age"));
//			subject.addSurveyAnswer(question[2], request.getParameter("country"));
//			subject.addSurveyAnswer(question[3], request.getParameter("experience"));
			subject.addSurveyAnswer(question[0], request.getParameter("difficulty"));
			subject.addSurveyAnswer(question[1], request.getParameter("feedback"));
			
			//Store result
			storage.insertSurvey(subject);
			
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
