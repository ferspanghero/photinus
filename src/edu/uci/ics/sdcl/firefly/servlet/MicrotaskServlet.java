package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException; 



import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;






import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.PositionFinder;
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
			StringBuilder newFileContent = new StringBuilder();
			StringBuilder highlight = new StringBuilder();
			String highlightCommand = new String();
			String newline = System.getProperty("line.separator");
			if ( task.getMethod().getCallers().isEmpty() ){
				newFileContent = null;
				highlightCommand = null;
			} else 
			{
				newFileContent.append("THE METHOD ABOVE IS CALLED BY: ");
				newFileContent.append(newline);
				for (CodeSnippet caller : task.getMethod().getCallers()) {
					newFileContent.append(caller.getCodeSnippetFromFileContent());	// appending caller
					if ( task.getMethod().getCallers().indexOf(caller) < (task.getMethod().getCallers().size()-1) ){
						newFileContent.append(newline);	// appending two new lines in case it is NOT the last caller 
						newFileContent.append(newline);
					}
				}
				// chasing method positions for highlighting purposes
				for (CodeSnippet caller : task.getMethod().getCallers()) {
					highlight.append( methodChaser(caller.getMethodSignature().getName(), newFileContent.toString()) );
				}
				highlightCommand = highlight.toString().substring(0, highlight.length()-1);	// -1 to remove last '#'
				System.out.println("Command to be executed: " + highlightCommand);
			}
			// passing to jsp
			request.setAttribute("positionsCaller", highlightCommand);
			request.setAttribute("caller", newFileContent.toString());

			/* preparing the third ACE Editor - callees */
			newFileContent.setLength(0);	// reseting the builder
			newFileContent.append("THE METHOD ABOVE HAS CALLEE(S): ");
			newFileContent.append(newline);
			for (CodeSnippet callee : task.getMethod().getCallees()) {
				newFileContent.append(callee.getCodeSnippetFromFileContent());	// appending caller
				if ( task.getMethod().getCallees().indexOf(callee) < (task.getMethod().getCallees().size()-1) ){
					newFileContent.append(newline);	// appending two new lines in case it is NOT the last callee 
					newFileContent.append(newline);
				}
			}			
			request.setAttribute("callee", newFileContent.toString());
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

	// return the position of all the methodName occurrences on the following format:
	// startingLine#startingColumn#endingLine#endingColumn#... and so on
	protected String methodChaser(String methodName, String fileContent)
	{
		PositionFinder invocationPosition;
		StringBuilder highlightCommand = new StringBuilder();
		String stringLines[] = fileContent.split("\r\n|\r|\n");
		int currentLine = 0;
		System.out.println("method name: " + methodName);
		for (String line : stringLines) {
			currentLine++;
			System.out.println("Line " + currentLine + ": " + line);
			if (line == null || line.length() <= 0)	continue;	// if line does not have any content, just skip it
			String[] stringWords = line.split("\\s+");			// splitting lines into words
			List<String> wordsPerLine = stringArraytoList(stringWords);
			int wordLengthAcumulator = 0;	// just for the edge case of the same function being executed more than once per line
			for (int index = 0; index < wordsPerLine.size(); index++) {
				boolean isMethodCall = false;				// assuming there is NOT a method Call
				if ( wordsPerLine.get(index).contains(methodName) )	// checking if the method name matched the word
				{	// now, to make sure its a method call, let's find the parenthesis 	
					if ( wordsPerLine.get(index).contains("(") )
					{	// also contains an opening parenthesis - method call for sure
						isMethodCall = true;
						System.out.println("Method call at line " + currentLine + ": " + line);
					}
					else if ( index < (wordsPerLine.size()-1) )	// if it is NOT the last word
					{	// look for parenthesis on the next word
						if ( wordsPerLine.get(index).contains("(") )
						{	// its also a method call for sure
							isMethodCall = true;
							System.out.println("Method call at line " + currentLine + ": " + line);
						}	// otherwise continue false, it is not a method call
					}
				}
				if (isMethodCall)
				{
					System.out.println("passing column: " + line.indexOf(methodName, wordLengthAcumulator) + " W.A.: " + wordLengthAcumulator);
					invocationPosition = 
							new PositionFinder(currentLine, line.indexOf(methodName, wordLengthAcumulator), stringLines, '(', ')');
					highlightCommand.append(invocationPosition.getStartingLineNumber()+"#");
					highlightCommand.append(line.indexOf(methodName, wordLengthAcumulator)+"#");// because I want the start column of the word
					highlightCommand.append(invocationPosition.getEndingLineNumber()+"#");
					highlightCommand.append(invocationPosition.getEndingColumnNumber()+"#");
					wordLengthAcumulator = invocationPosition.getStartingColumnNumber();
					System.out.println("Positions appended: " + highlightCommand + " WA: " + wordLengthAcumulator);
				}
			}
		}
		return highlightCommand.toString();
	}

	private List<String> stringArraytoList( String texts[] ) 
	{
		List<String> list = new ArrayList<String>();
		// transforming into List and removing null and empty entries
		for(String string : texts) {
			if( string != null && string.length() > 0) {
				list.add(string);
			}
		}
		// returning the list
		return list;
	}

}
