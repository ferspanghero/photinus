package test;

import java.io.IOException;

import edu.uci.ics.sdcl.firefly.CodeSnippetFactory;
import edu.uci.ics.sdcl.firefly.SourceFileReader;

public class SourceFileReaderTest {

	public static void main(String args[]) throws IOException{
		

		String folderPath = "C:/Users/Christian Adriano/Documents/GitHub/crowd-debug-firefly/src/sample/JustOneSample/";
		String fileName = "SimpleSampleCode.java";
		String fileContent = SourceFileReader.readFileToString(folderPath+fileName);
		CodeSnippetFactory factory = new CodeSnippetFactory(fileName,fileContent);
		
	
		factory.generateSnippetsForFile();
		System.out.println();
		System.out.println("********************************");
		factory.printAll();
	} 

	public SourceFileReaderTest() {}


}

