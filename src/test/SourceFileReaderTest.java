package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


import edu.uci.ics.sdcl.firefly.JavaParser;

public class SourceFileReaderTest {

	public static void main(String args[]){
		SourceFileReaderTest tester = new SourceFileReaderTest();
		tester.runTest();
	}
	
	String path = "C:/Users/Christian Adriano/Documents/GitHub/crowd-debug-firefly/src/sample/SimpleSampleCode.java"; 

	public SourceFileReaderTest() {}


	public StringBuffer readFileToBuffer(String path){

		if(path==null){
			return null;
		}
		else{
			try{
				File folderPath = new File(path);		// setting the File to the path
				BufferedReader buffer = new BufferedReader(new FileReader(folderPath));
				String currentLine;
				StringBuffer wholeFile= new StringBuffer();
				while( (currentLine = buffer.readLine()) != null ){
					//currentLine.toCharArray();
					wholeFile.append(currentLine);
				}
				buffer.close();
				return wholeFile;
			}	
			catch (Exception e){
				e.printStackTrace();
				return null;
			}

		}
	}
	
	
	
	public void runTest(){
		
		StringBuffer buffer = readFileToBuffer(this.path);
		JavaParser parser = new JavaParser(buffer);
		try{
			parser.printICompilationUnitInfo();
			parser.printMethods();
		}
		catch(Exception e){
			System.err.println("Exception at runTest :" +e.toString());
		}
		
	}

}

