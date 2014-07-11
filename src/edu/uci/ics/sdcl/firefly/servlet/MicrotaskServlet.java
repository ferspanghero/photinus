package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

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
		System.out.println("in Get");
		
		String fileName = request.getParameter("fileName");
		int answer = new Integer(request.getParameter("answerOption")).intValue();
		String id = request.getParameter("id");
		String explanation = request.getParameter("explanation");
		
		MicrotaskMemento memento = new MicrotaskMemento();
		memento.insertAnswer(fileName, new Integer(id), new Answer(Answer.mapToString(answer),explanation));
		
		//display a new microtask
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Prepare the microtask page to be loaded 

		//Retrieve Microtask from Selector
		MicrotaskSelector selector = new MicrotaskSelector();
		MicrotaskSelector.SelectorReturn returnValues = selector.selectAnyMicrotask();

		if(returnValues==null){
			request.setAttribute("return_message","No microtasks available");
		}
		else{
			Microtask task = returnValues.task;
			String fileName = returnValues.fileName;
			request.setAttribute("question", task.getQuestion());
			request.setAttribute("source", CodeSnippetFactory.getFileContent());  //task.getMethod().getMethodBody()

			request.setAttribute("id", task.getID());
			request.setAttribute("fileName", fileName);
			
			request.setAttribute("startLine", task.getStartingLine());
			request.setAttribute("startColumn", task.getStartingColumn());
			request.setAttribute("endLine", task.getEndingLine());
			request.setAttribute("endColumn", task.getEndingColumn());

			RequestDispatcher view = request.getRequestDispatcher("/Microtask.jsp");
			view.forward(request, response);
		}
	}

}
