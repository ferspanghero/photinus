package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException; 
 

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.controller.MicrotaskSelector;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;

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

		MicrotaskStorage storage = new MicrotaskStorage();
		storage.insertAnswer(fileName, new Integer(id), new Answer(Answer.mapToString(answer),explanation));
 
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
			String fileContent = task.getMethod().getCodeSnippetFromFileContent();	// Taking the method content instead of the whole file
			request.setAttribute("question", task.getQuestion());
			request.setAttribute("source", fileContent); 	// content displayed on the first ACE Editor
			/* preparing the second ACE Editor - callers */
			StringBuilder builder = new StringBuilder();
			StringBuilder highlight = new StringBuilder();
			String newline = System.getProperty("line.separator");
			builder.append("THE METHOD ABOVE IS CALLED BY: ");
			builder.append(newline);
			for (CodeSnippet caller : task.getMethod().getCallers()) {
				builder.append(caller.getCodeSnippetFromFileContent());	// appending caller
				if ( task.getMethod().getCallers().indexOf(caller) < (task.getMethod().getCallers().size()-1) ){
					builder.append(newline);	// appending two new lines in case it is NOT the last caller 
					builder.append(newline);
				}
			}
			// chasing methods for highlighting purposes
			highlight.append("/");
			for (CodeSnippet callee : task.getMethod().getCallees()) {
				highlight.append( methodChaser(callee.getMethodSignature().getName(), builder.toString()) );
			}
			String highlightCommand = highlight.toString().substring(0, highlight.length()-2) + "/g";
			System.out.println("Command to be executed: " + highlightCommand);
			// passing to jsp
			request.setAttribute("caller", builder.toString());
			request.setAttribute("words", highlightCommand);

			/* preparing the third ACE Editor - callees */
			builder.setLength(0);	// reseting the builder
			builder.append("THE METHOD ABOVE HAS CALLEE(S): ");
			builder.append(newline);
			for (CodeSnippet callee : task.getMethod().getCallees()) {
				builder.append(callee.getCodeSnippetFromFileContent());	// appending caller
				if ( task.getMethod().getCallees().indexOf(callee) < (task.getMethod().getCallees().size()-1) ){
					builder.append(newline);	// appending two new lines in case it is NOT the last callee 
					builder.append(newline);
				}
			}			
			request.setAttribute("callee", builder.toString());
//			request.setAttribute("words", "/size|int/g");
			
			request.setAttribute("id", task.getID());
			request.setAttribute("fileName", fileName);
			request.setAttribute("explanation",""); //clean up the explanation field.

			request.setAttribute("startLine", task.getStartingLine());
			request.setAttribute("startColumn", task.getStartingColumn());
			request.setAttribute("endLine", task.getEndingLine());
			request.setAttribute("endColumn", task.getEndingColumn());
			
			request.setAttribute("methodStartingLine", task.getMethod().getMethodSignature().getLineNumber());
			
		}
		//display a new microtask
		request.getRequestDispatcher("/QuestionMicrotask.jsp").include(request, response);
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
			String fileContent = task.getMethod().getCodeSnippetFromFileContent();	// Taking the method content instead of the whole file
			request.setAttribute("question", task.getQuestion());
			request.setAttribute("source", fileContent);   
			/* preparing the second ACE Editor - callers */
			StringBuilder builder = new StringBuilder();
			String newline = System.getProperty("line.separator");
			builder.append("THE METHOD ABOVE IS CALLED BY: ");
			builder.append(newline);
			for (CodeSnippet caller : task.getMethod().getCallers()) {
				builder.append(caller.getCodeSnippetFromFileContent());	// appending caller
				if ( task.getMethod().getCallers().indexOf(caller) < (task.getMethod().getCallers().size()-1) ){
					builder.append(newline);	// appending two new lines in case it is NOT the last caller 
					builder.append(newline);
				}
			}		
			
			request.setAttribute("caller", builder.toString());
			
			/* preparing the third ACE Editor - callees */
			builder.setLength(0);	// reseting the builder
			builder.append("THE METHOD ABOVE HAS CALLEE(S): ");
			builder.append(newline);
			for (CodeSnippet callee : task.getMethod().getCallees()) {
				builder.append(callee.getCodeSnippetFromFileContent());	// appending caller
				if ( task.getMethod().getCallees().indexOf(callee) < (task.getMethod().getCallees().size()-1) ){
					builder.append(newline);	// appending two new lines in case it is NOT the last callee 
					builder.append(newline);
				}
			}			
			request.setAttribute("callee", builder.toString());
			
			request.setAttribute("id", task.getID());
			request.setAttribute("fileName", fileName);
			request.setAttribute("explanation",""); //clean up the explanation field.

			request.setAttribute("startLine", task.getStartingLine());
			request.setAttribute("startColumn", task.getStartingColumn());
			request.setAttribute("endLine", task.getEndingLine());
			request.setAttribute("endColumn", task.getEndingColumn());
			
			request.setAttribute("methodStartingLine", task.getMethod().getMethodSignature().getLineNumber());

			//Calls the microtask page
			request.getRequestDispatcher("/QuestionMicrotask.jsp").forward(request, response);
		}
	}
	
	protected String methodChaser(String methodName, String fileContent)
	{
		StringBuilder methodChased = new StringBuilder();
		StringBuilder highlightCommand = new StringBuilder();
		String[] words = fileContent.split("\\s+");		// splitting fileContent into words
		int index;
		for (index = 0; index < words.length; index++) {
			System.out.println("word: " + words[index]);
			methodChased.setLength(0);					// reseting methodChased
			if ( words[index].contains(methodName) )	// checking if the method name matched the word
			{	// now, to make sure its a method call, let's find the parenthesis 
				System.out.println("This word contains " + methodName + ": " + words[index]);	
				if ( words[index].contains("(") )
				{	// also contains an opening parenthesis - method call for sure
					highlightCommand.append( endingMethodChaser(methodChased, words, index) + "|");
				}
				else
				{	// no parenthesis, look on the next word
					if ( words[index+1].contains("(") )
					{	// its also a method call for sure
						methodChased.append(words[index] + " ");	// space to match the fileContent
						highlightCommand.append( endingMethodChaser(methodChased, words, index+1) + "|");
					}	// otherwise do nothing, it is not a method call
				}
			}
			index++;
		}
		System.out.println("Method chased: " + highlightCommand.toString());
		return highlightCommand.toString();
	}
	
	private StringBuilder endingMethodChaser(StringBuilder incompleteStringMethod, String[] words, int openingParenthesisIndex)
	{
		int braces = 0;			// counting the number of non-matching parenthesis found
		int index2 = openingParenthesisIndex;
		do 
		{	// finding the end of the parenthesis 
			if ( words[index2].contains(")") )
			{	// found a matching parenthesis
				braces--;
			} else if ( words[index2].contains("(") )
			{	// found another non-matching parenthesis
				braces++;
			}
			incompleteStringMethod.append(words[index2++] + " ");	// space to match the fileContent
		} while (0 < braces);
		return incompleteStringMethod;	// now it is complete!
	}

 
}
