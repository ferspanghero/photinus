package edu.uci.ics.sdcl.firefly;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SourceFileReader {

	public static void main(String args[]) throws IOException{
		CodeSnippetFactory codeSnippets = new CodeSnippetFactory("OrdinalMap_buggy", 
				readFileToString("C:\\Users\\Danilo\\Documents\\GitHub\\crowd-debug-firefly\\samples\\main\\java\\com\\netflix\\nfgraph\\util\\OrdinalMap_buggy.java")); // file name and file content
		ArrayList<CodeSnippet> methodsParsed = codeSnippets.generateSnippetsForFile();
		for (CodeSnippet codeSnippet : methodsParsed) {
			System.out.println("fileName: " + codeSnippet.getFileName());
			System.out.println("--> " + codeSnippet.getMethodSignature().getName());
			System.out.println("callers: ");
			for (CodeSnippet caller : codeSnippet.getCallers())
			{
				System.out.print("\t");
				System.out.print(caller.getMethodSignature().getName());
				if (codeSnippet.getCallers().indexOf(caller) < (codeSnippet.getCallers().size()-1))
					System.out.print(", ");
				else
					System.out.println();
			}
			System.out.println("callees: ");
			for (CodeSnippet callee : codeSnippet.getCallees())
			{
				System.out.print("\t");
				System.out.print(callee.getMethodSignature().getName());
				if (codeSnippet.getCallees().indexOf(callee) < (codeSnippet.getCallees().size()-1))
					System.out.print(", ");
				else
					System.out.println();
			}
			System.out.println();
		}
		System.out.println();
		//		codeSnippets.printAll();
		QuestionFactory questionFactory = new QuestionFactory();
		System.out.println("----------Printing Concrete Questions-------------");
		questionFactory.generateQuestions(methodsParsed,"Debug Report Message", 0);
		HashMap<Integer, Microtask> concreteQuestionsMade = questionFactory.getConcreteQuestions();
		Set<Map.Entry<Integer, Microtask>> set = concreteQuestionsMade.entrySet();
		Iterator<Entry<Integer, Microtask>> i = set.iterator();
		while(i.hasNext()) 
		{
			Map.Entry<Integer, Microtask> me = (Map.Entry<Integer, Microtask>)i.next();
			System.out.print("ID = " + me.getValue().getID() + ": ");
			System.out.println(me.getValue().getQuestion());
		}
		System.out.println();
		System.out.println("~~~~~~~~~~~~~Reports~~~~~~~~~~~~~~~~");
		System.out.println("Number of Snippets: " + methodsParsed.size());
		System.out.println("Number of questions: " + concreteQuestionsMade.size());
		

		System.out.println("****************Method callers****************");
		for (CodeSnippet snippet : methodsParsed) {
			System.out.print(snippet.getMethodSignature().getName() + " is called by [");
			ArrayList<CodeSnippet> methods = snippet.getCallers();
			for (CodeSnippet codeSnippet : methods) {
				System.out.print(codeSnippet.getMethodSignature().getName() + ", ");
			}
			System.out.println("]");

		}

		/* for debug purposes 
		for (CodeSnippet method : methodsParsed) {
			System.out.println("Method: " + method.getMethodSignature());
			ArrayList<CodeElement> statements = method.getStatements();
			for (CodeElement codeElement : statements) {
				// just for ifs
				if ( CodeElement.IF_CONDITIONAL == codeElement.getType() )
				{
					MyIfStatement ifElement = (MyIfStatement)codeElement;
					System.out.println("Element Begining: (" + codeElement.getElementStartingLine() + ", " 
							+ codeElement.getElementStartingColumn() + ") Ending: (" + codeElement.getElementEndingLine()
							+ ", " + codeElement.getElementEndingColumn() + ")");
					if (ifElement.isThereIsElse())
						System.out.println("Body Begining: (" + codeElement.getBodyStartingLine() + ", " 
							+ codeElement.getBodyStartingColumn() + ") Ending: (" + ifElement.getElseEndingLine()
							+ ", " + ifElement.getElseEndingColumn() + ")");
					else
						System.out.println("Body Begining: (" + codeElement.getBodyStartingLine() + ", " 
								+ codeElement.getBodyStartingColumn() + ") Ending: (" + codeElement.getBodyEndingLine()
								+ ", " + codeElement.getBodyEndingColumn() + ")");
					if (ifElement.isIfOfAnElse())
						System.out.println("this if belongs to an Else!");
					System.out.println("---");
				}
			}
			System.out.println("~~~~");
		} */
	} 

	public SourceFileReader() {}
	//read file content into a string
	public static String readFileToString(String filePath)  {
		try{
			System.out.println();
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("Reading file '" + filePath + "'");
			StringBuilder fileData = new StringBuilder(1000);
			BufferedReader reader = new BufferedReader(new FileReader(filePath));

			char[] buf = new char[10];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				//				System.out.println(numRead);
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}

			reader.close();

			return  fileData.toString();
		}
		catch(IOException exception){
			exception.printStackTrace();
			return null;
		}
	}
}

