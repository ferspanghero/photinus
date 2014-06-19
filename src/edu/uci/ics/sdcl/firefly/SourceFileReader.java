package edu.uci.ics.sdcl.firefly;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SourceFileReader {

	public static void main(String args[]) throws IOException{
		CodeSnippetFactory codeSnippets = new CodeSnippetFactory
				("C:/Users/Danilo/Documents/GitHub/crowd-debug-firefly/src/sample/JustOneSample");
		ArrayList<CodeSnippet> methodsParsed = codeSnippets.generateSnippets();
		System.out.println();
		System.out.println("********************************");
		codeSnippets.printAll();
		QuestionFactory questionFactory = new QuestionFactory();
		System.out.println("----------Printing Concrete Questions-------------");
		ArrayList<ConcreteQuestion> concreteQuestionsMade = questionFactory.generateQuestions(methodsParsed);
		Integer j = 0;
		for (ConcreteQuestion concreteQuestionMade : concreteQuestionsMade)
		{
			j++;
			System.out.println(j + ". " + concreteQuestionMade.getQuestion());
		}
		System.out.println();
		System.out.println("~~~~~~~~~~~~~Reports~~~~~~~~~~~~~~~~");
		System.out.println("Number of Snippets: " + methodsParsed.size());
		System.out.println("Number of questions: " + concreteQuestionsMade.size());
	} 

	public SourceFileReader() {}

	//read file content into a string
		public static String readFileToString(String filePath) throws IOException {
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
}

