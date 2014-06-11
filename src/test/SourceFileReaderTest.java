package test;

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.IOException;

import edu.uci.ics.sdcl.firefly.CodeSnippetFactory;

public class SourceFileReaderTest {

	public static void main(String args[]) throws IOException{
		CodeSnippetFactory codeSnippets = new CodeSnippetFactory
				("C:/Users/Danilo/Documents/GitHub/crowd-debug-firefly/src/sample/JustOneSample");
		codeSnippets.generateSnippets();
		System.out.println();
		System.out.println("********************************");
		codeSnippets.printAll();
	} 

	public SourceFileReaderTest() {}

	//read file content into a string
		public static String readFileToString(String filePath) throws IOException {
			System.out.println();
			System.out.println("#################################");
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

