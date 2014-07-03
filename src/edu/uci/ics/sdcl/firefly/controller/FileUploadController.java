package edu.uci.ics.sdcl.firefly.controller;

import edu.uci.ics.sdcl.firefly.*;
import edu.uci.ics.sdcl.firefly.memento.MicrotaskMemento;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Servlet implementation for the page FileUpload.jsp
 */
public class FileUploadController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileUploadController() {
		super();
	}

	/**
	 * Processes the file by generating and persisting the microtasks
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			String action = request.getParameter("action");
			System.out.println("action="+action);
			String json = request.getParameter("json");
			JSONObject jsonData = (JSONObject) JSONValue.parse(json);
			String fileName = ((String)jsonData.get("fileName"));  
			System.out.println("fileName: "+ fileName);
			
			if (action.equals("uploadFile")) {
				response.setContentType("text/json");
				PrintWriter out = response.getWriter();
				
				//calls CodeSnippetFactory
				CodeSnippetFactory codeSnippetFactory = new CodeSnippetFactory(fileName);
				ArrayList<CodeSnippet> snippetList = codeSnippetFactory.generateSnippets(); //TO DO change so generateSnippets Accetps fileName
				
				//calls QuestionFactory
				QuestionFactory questionFactory = new QuestionFactory ();
				HashMap<Integer, Microtask> microtaskMap = questionFactory.generateQuestions(snippetList);
				FileDebugSession fileDebuggingSession = new FileDebugSession(fileName,microtaskMap);
										
				//Persist data
				MicrotaskMemento memento = new MicrotaskMemento();
				memento.insert(fileName, fileDebuggingSession);
								
				int numberOfCodeSnippets = snippetList.size();
				int numberOfMicrotasks = microtaskMap.size();// = codeSnippetFactory.getNumberOfMicrotasks();
				
				String status = "";
				JSONObject result = new JSONObject();
				
				if (microtaskMap!= null && microtaskMap.size() > 0)
					status = "Microtasks generated: " + numberOfMicrotasks+", "+"Number of code snippets: "+numberOfCodeSnippets;
				else
					status = "No Microtasks were generated. Please review the file.";
				
				result.put("message",status);
				String jsonResult = JSONObject.toJSONString(result);
				
				out.println(jsonResult);
				out.flush();
				out.close();
			} 
		} catch (Exception ex) {
			System.out.println("{\"message\":\"Error - caught exception in FileUploadController\"}");
		} 
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("in Post UploadController - not implemented");
	}

}
