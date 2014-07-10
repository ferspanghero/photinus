package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.controller.MicrotaskSelector;

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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Prepare the microtask page to be loaded
		System.out.println("in Microtask Post...");

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
			request.setAttribute("source", task.getMethod().getMethodBody());

			request.setAttribute("id", task.getID().toString());
			request.setAttribute("fileName", fileName);


			RequestDispatcher view = request.getRequestDispatcher("/Question.jsp");
			view.forward(request, response);
		}
	}

}
