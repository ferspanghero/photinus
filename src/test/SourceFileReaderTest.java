package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import edu.uci.ics.sdcl.firefly.CodeSnippetFactory;
import edu.uci.ics.sdcl.firefly.SourceFileReader;

public class SourceFileReaderTest {

	public static void main(String args[]) throws IOException{
		CodeSnippetFactory codeSnippets = new CodeSnippetFactory("factorial",
				SourceFileReader.readFileToString("C:/Users/Danilo/Documents/GitHub/crowd-debug-firefly/src/sample/JustOneSample"));
		codeSnippets.generateSnippets();
		System.out.println();
		System.out.println("********************************");
		codeSnippets.printAll();
	} 

	public SourceFileReaderTest() {}


}

