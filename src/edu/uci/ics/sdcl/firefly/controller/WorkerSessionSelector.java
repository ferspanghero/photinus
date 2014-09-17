package edu.uci.ics.sdcl.firefly.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.util.PositionFinder;

/**
 * Selects the a WorkerSession for the first time or an active one.
 * 
 * @author Christian Adriano
 *
 */
public class WorkerSessionSelector {

	public WorkerSessionSelector(){}


	/** Load all data from a microtask to the request 
	 * 
	 * @param request
	 * @param task
	 * @return the new request with data to be displayed on the web page
	 */
	public HttpServletRequest generateRequest(HttpServletRequest request, Microtask task){
		
		System.out.println("Retrieved microtask id:"+task.getID()+" answers: "+task.getAnswerList().toString());
		System.out.println("Retrieved microtask bug report:" + task.getFailureDescription() +  " from fileName: "+task.getMethod().getFileName());
		
		request.setAttribute("microtaskId", task.getID());  
		request.setAttribute("fileName", task.getMethod().getFileName());
		
		String fileContent = task.getMethod().getCodeSnippetFromFileContent();	// Taking the method content instead of the whole file
		request.setAttribute("bugReport", task.getFailureDescription());
		request.setAttribute("question", task.getQuestion());
		request.setAttribute("source", fileContent); 	// content displayed on the first ACE Editor
		// chasing method positions for highlighting callees on Main Ace Editor
		if (!task.getMethod().getCallees().isEmpty()){
			StringBuilder commandStorage = new StringBuilder();
			StringBuilder commandStorage2 = new StringBuilder();
			String callesOnMainCommand = null;
			String callesOnMainCommand2 = null;
			for (CodeSnippet callee : task.getMethod().getCallees()) {
				commandStorage2.append(callee.getElementStartingLine()+"#");
				commandStorage2.append(callee.getElementStartingColumn()+"#");
				commandStorage2.append(callee.getElementEndingLine()+"#");
				commandStorage2.append(callee.getElementEndingColumn()+"#");
				
				
				commandStorage.append( methodChaser(callee.getMethodSignature().getName(), fileContent, false) );
			}
			if(commandStorage.length()>0)
				callesOnMainCommand = commandStorage.toString().substring(0, commandStorage.length()-1);	// -1 to remove last '#'
			System.out.println("calleesOnMain to be executed: " + callesOnMainCommand);
			
			if(commandStorage2.length()>0)
				callesOnMainCommand2 = commandStorage2.toString().substring(0, commandStorage2.length()-1);	// -1 to remove last '#'
			System.out.println("calleesOnMain2 to be executed: " + callesOnMainCommand2);
			
			request.setAttribute("calleesOnMain", callesOnMainCommand);
		} else
			request.setAttribute("calleesOnMain", null);

		/* preparing the second ACE Editor - callers */
		StringBuilder newFileContent;
		StringBuilder highlight;
		String highlightCallerCommand;

		String newline = System.getProperty("line.separator");
		if ( task.getMethod().getCallers().isEmpty() ){
			newFileContent = null;
			highlightCallerCommand = null;
			request.setAttribute("caller", null);
		} 
		else{
			newFileContent = new StringBuilder();
			highlight = new StringBuilder();
			highlightCallerCommand = new String();
			newFileContent.append("METHOD '" + task.getMethod().getMethodSignature().getName() + 
					"' IS CALLED BY THE FOLLOWING METHOD(S):");
			newFileContent.append(newline);
			newFileContent.append(newline);
			for (CodeSnippet caller : task.getMethod().getCallers()) {
				newFileContent.append(caller.getCodeSnippetFromFileContent());	// appending caller
				if ( task.getMethod().getCallers().indexOf(caller) < (task.getMethod().getCallers().size()-1) ){
					newFileContent.append(newline);	// appending two new lines in case it is NOT the last caller 
					newFileContent.append(newline);
				}
			}
			// chasing method positions for highlighting purposes
			//CodeSnippet caller = task.getMethod();
			//hightlight.append()
			highlight.append( methodChaser(task.getMethod().getMethodSignature().getName(), newFileContent.toString(), false) );
			if(highlight.length()>0)
				highlightCallerCommand = highlight.toString().substring(0, highlight.length()-1);	// -1 to remove last '#'
			System.out.println("Command to be executed: " + highlightCallerCommand);
			request.setAttribute("caller", newFileContent.toString());
		}
		// passing to jsp
		request.setAttribute("positionsCaller", highlightCallerCommand);


		/* preparing the third ACE Editor - callees */
		String highlightCalleeCommand;
		if ( task.getMethod().getCallees().isEmpty() ){
			newFileContent = null;
			highlightCalleeCommand = null;
			request.setAttribute("callee", null);
		} 
		else{
			newFileContent = new StringBuilder();
			highlight = new StringBuilder();
			highlightCalleeCommand = null;
			newFileContent.append("METHOD '" + task.getMethod().getMethodSignature().getName() + 
					"' CALLS THE FOLLOWING METHOD(S):");
			newFileContent.append(newline);
			newFileContent.append(newline);
			for (CodeSnippet callee : task.getMethod().getCallees()) {
				newFileContent.append(callee.getCodeSnippetFromFileContent());	// appending callee
				if ( task.getMethod().getCallees().indexOf(callee) < (task.getMethod().getCallees().size()-1) ){
					newFileContent.append(newline);	// appending two new lines in case it is NOT the last callee 
					newFileContent.append(newline);
				}
			}	
			// chasing method positions for highlighting purposes
			for (CodeSnippet callee : task.getMethod().getCallees()) {
				highlight.append( methodChaser(callee.getMethodSignature().getName(), newFileContent.toString(), true) );
			}
			if(highlight.length()>0)
				highlightCalleeCommand = highlight.toString().substring(0, highlight.length()-1);	// -1 to remove last '#'
			System.out.println("Command to be executed: " + highlightCalleeCommand);
			request.setAttribute("callee", newFileContent.toString());
		}
		// passing to jsp
		request.setAttribute("positionsCallee", highlightCalleeCommand);

		 

		
		request.setAttribute("explanation",""); //clean up the explanation field.

		request.setAttribute("startLine", task.getStartingLine());
		request.setAttribute("startColumn", task.getStartingColumn());
		request.setAttribute("endLine", task.getEndingLine());
		request.setAttribute("endColumn", task.getEndingColumn());

		request.setAttribute("methodStartingLine", task.getMethod().getMethodSignature().getLineNumber());
		
		return request;
	}
	
	
	
	/**
	 * @return the position of all the methodName occurrences on the following 
	 * format: startingLine#startingColumn#endingLine#endingColumn#... and so on
	 * @param methodName
	 * @param fileContent
	 * @param methodCallStrict
	 * @return
	 */
	protected String methodChaser(String methodName, String fileContent, boolean methodCallStrict)
	{
		StringBuilder highlightCommand = new StringBuilder();
		String stringLines[] = fileContent.split("\r\n|\r|\n");
		int currentLine = 0;
		//		System.out.println("method name: " + methodName);
		for (String line : stringLines) {
			currentLine++;
			//			System.out.println("Line " + currentLine + ": " + line);
			if (line == null || line.length() <= 0)	continue;	// if line does not have any content, just skip it
			if (methodCallStrict)
				if (!line.contains("public") && !line.contains("protected") && !line.contains("private"))
					continue;	// looking just for methodCalls not other occurrences when methodStrict is true (that will be skipped)
			String[] stringWords = line.split("\\s+");			// splitting lines into words
			List<String> wordsPerLine = stringArraytoList(stringWords);
			int wordLengthAccumulator = 0;	// just for the edge case of the same function being executed more than once per line
			for (int index = 0; index < wordsPerLine.size(); index++) {
				boolean isMethodCall = false;	// assuming there is NOT a method Call
				if ( wordsPerLine.get(index).contains(methodName)){// && wordsPerLine.size()==methodName.length()){
					
					// now, to make sure its a method call, let's find the parenthesis 	
					if ( wordsPerLine.get(index).contains("(") )
					{	// also contains an opening parenthesis - method call for sure
						isMethodCall = true;
						System.out.println("Method call.1 WLA- " + wordLengthAccumulator +"- at line " + currentLine + ": " + line);
					}
					else if ( index < (wordsPerLine.size()-1) )	// if it is NOT the last word
					{	// look for parenthesis on the next word
						if ( wordsPerLine.get(index+1).contains("(") )
						{	// its also a method call for sure
							isMethodCall = true;
							//							System.out.println("Method call.2 at word - " + wordsPerLine.get(index+1) + "- at line " + currentLine + ": " + line);
						}	// otherwise continue false, it is not a method call
					}
				}
				if (isMethodCall)
				{
					//Commented code below highlights only the method name. Current working code highlights the parameters too
					//Integer startLine = currentLine;
					//Integer startColumn = line.indexOf(methodName, wordLengthAccumulator);
					//Integer endColumn = startColumn+ methodName.length();
					//Integer endLine = startLine;
					//System.out.println("compute for caller: "+ startLine+","+startColumn+","+endLine+ ","+ endColumn);

					//					System.out.println("passing column: " + line.indexOf(methodName, wordLengthAcumulator) + " W.A.: " + wordLengthAcumulator);
					PositionFinder positionFinder = 
							new PositionFinder(currentLine, line.indexOf(methodName, wordLengthAccumulator), stringLines, '(', ')');
					positionFinder.computeEndPosition();

					highlightCommand.append(positionFinder.getStartingLineNumber()+"#");
					highlightCommand.append(line.indexOf(methodName, wordLengthAccumulator)+"#");// because I want the start column of the word
					highlightCommand.append(positionFinder.getEndingLineNumber()+"#");
					highlightCommand.append(positionFinder.getEndingColumnNumber()+"#");
					//					wordLengthAccumulator = invocationPosition.getStartingColumnNumber();
					//					System.out.println("Positions appended: " + highlightCommand + " WA: " + wordLengthAcumulator);
				}
				wordLengthAccumulator = line.indexOf(wordsPerLine.get(index), wordLengthAccumulator) + wordsPerLine.get(index).length();
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
