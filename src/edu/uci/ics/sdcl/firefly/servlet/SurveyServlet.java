package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.controller.StorageStrategy;
import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;

/**
 * Servlet implementation class SurveyServlet
 */
public class SurveyServlet extends HttpServlet {
    public static String question[] = {"Gender", "Age", "Country", "Years progr.", "Difficulty"};   
    public SurveyServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StorageStrategy storage = StorageStrategy.initializeSingleton();
		String workerId = request.getParameter("workerId");
		Worker subject = storage.readExistingWorker(workerId);
		if (null != subject){
			WorkerSession  session = storage.readNewSession(subject.getWorkerId(), subject.getCurrentFileName());
			subject = storage.readExistingWorker(workerId);//Ugly, but needed now so the worker comes with the actual session ID.
			subject.addSurveyAnswer(question[0], request.getParameter("gender"));
			subject.addSurveyAnswer(question[1], request.getParameter("age"));
			subject.addSurveyAnswer(question[2], request.getParameter("country"));
			subject.addSurveyAnswer(question[3], request.getParameter("experience"));

			//Store result
			storage.insertSurvey(subject);
			request.setAttribute("workerId",subject.getWorkerId());
			request.setAttribute("sessionId",subject.getSessionId());
			request.setAttribute("timeStamp", TimeStampUtil.getTimeStampMillisec());
			request = MicrotaskServlet.generateRequest(request, storage.getNextMicrotask(session.getId()));
			request.getRequestDispatcher("/QuestionMicrotask.jsp").forward(request, response);
			//request = MicrotaskServlet.generateRequest(request, storage.getNextMicrotask(session.getId()));
			//request.getRequestDispatcher("/QuestionMicrotask.jsp").forward(request, response);
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
