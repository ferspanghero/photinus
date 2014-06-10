package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


import java.io.IOException;

import edu.uci.ics.sdcl.firefly.JavaParser;

public class SourceFileReaderTest {

	public static void main(String args[]) throws IOException{
		ParseFilesInDir();
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
	 
		//loop directory to get file list
		public static void ParseFilesInDir() throws IOException{
//			File dirs = new File("."); 				// For files under this folder
//			String dirPath = dirs.getCanonicalPath() + File.separator+"src"+File.separator;
			String dirPath = "C:/Users/Danilo/Documents/GitHub/crowd-debug-firefly/src/sample/";
//			System.out.println(dirPath);
			File root = new File(dirPath);
			File[] files = root.listFiles ( );
			String filePath = null;
	 
			 for (File f : files ) {
				 filePath = f.getAbsolutePath();
				 if(f.isFile()){
					 @SuppressWarnings("unused")
					JavaParser parser = new JavaParser(readFileToString(filePath));
				 }
			 }
		}
}

