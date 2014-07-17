package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException; 
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.CodeSnippetFactory;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.controller.MicrotaskSelector;
import edu.uci.ics.sdcl.firefly.memento.MicrotaskMemento;

/**
 * Servlet implementation class MicrotaskController
 */
public class MicrotaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MicrotaskServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		//Collect and persist the answer
		//Mark the microtask as already answered

		String fileName = request.getParameter("fileName");
		int answer = new Integer(request.getParameter("answer")).intValue();
		String id = request.getParameter("id");
		String explanation = request.getParameter("explanation");

		MicrotaskMemento memento = new MicrotaskMemento();
		memento.insertAnswer(fileName, new Integer(id), new Answer(Answer.mapToString(answer),explanation));
 
		//Prepare next microtask request
		MicrotaskSelector selector = new MicrotaskSelector();
		MicrotaskSelector.SelectorReturn returnValues = selector.selectAnyMicrotask();

		if(returnValues==null){
			request.setAttribute("return_message","No microtasks available");
		}
		else{

			Microtask task = returnValues.task;
			System.out.println("Retrieved microtask id:"+task.getID()+" answers: "+task.getAnswerList().toString());
			fileName = returnValues.fileName;
			String fileContent = returnValues.fileContent;
			request.setAttribute("question", task.getQuestion());
			request.setAttribute("source", fileContent);   

			request.setAttribute("id", task.getID());
			request.setAttribute("fileName", fileName);
			request.setAttribute("explanation",""); //clean up the explanation field.

			request.setAttribute("startLine", task.getStartingLine());
			request.setAttribute("startColumn", task.getStartingColumn());
			request.setAttribute("endLine", task.getEndingLine());
			request.setAttribute("endColumn", task.getEndingColumn());
		}
		//display a new microtask
		request.getRequestDispatcher("/Question.jsp").include(request, response);
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 
		//Retrieve Microtask from Selector
		MicrotaskSelector selector = new MicrotaskSelector();
		MicrotaskSelector.SelectorReturn returnValues = selector.selectAnyMicrotask();

		if(returnValues==null){
			request.setAttribute("return_message","No microtasks available");
		}
		else{

			Microtask task = returnValues.task;
			System.out.println("Retrieved microtask id:"+task.getID()+" answers: "+task.getAnswerList().toString());
			String fileName = returnValues.fileName;
			String fileContent = returnValues.fileContent;
			request.setAttribute("question", task.getQuestion());
			request.setAttribute("source", fileContent);   

			request.setAttribute("id", task.getID());
			request.setAttribute("fileName", fileName);
			request.setAttribute("explanation",""); //clean up the explanation field.

			request.setAttribute("startLine", task.getStartingLine());
			request.setAttribute("startColumn", task.getStartingColumn());
			request.setAttribute("endLine", task.getEndingLine());
			request.setAttribute("endColumn", task.getEndingColumn());

			request.getRequestDispatcher("/Question.jsp").forward(request, response);
		}
	}

 
}
