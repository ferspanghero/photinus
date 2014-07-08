package edu.uci.ics.sdcl.firefly;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CodeSnippetFactory {
	private String folderPath;
	private static String fileContent;
	private String fileName;
	private static String[] fileContentePerLine;
	private static ArrayList<CodeSnippet> codeSnippetList= new ArrayList<CodeSnippet>();
	
	public CodeSnippetFactory(String folderPathArg)
	{
		this.folderPath = folderPathArg;
	}
	
	public CodeSnippetFactory(String fileName, String fileContent){
		this.fileContent = fileContent;
		this.fileName = fileName;
	}

	/** Generates code snippets for a file content  in memory (attribute fileContent) */
	public ArrayList<CodeSnippet> generateSnippetsForFile()
	{
		this.codeSnippetList = new ArrayList<CodeSnippet>(); //Cleans up the snippet list
		fileContentePerLine = this.fileContent.split("\r\n|\r|\n");
		@SuppressWarnings("unused")
		JavaParser parser = new JavaParser(fileContent);
		return this.codeSnippetList;
	}
	
	/** Generates code snippets for all files in a the provide filePath */
	public ArrayList<CodeSnippet> generateSnippets()
	{
		try
		{
			ParseFilesInDir();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return codeSnippetList;
	}
	
	public void printAll()
	{
		System.out.println("Printing all methods");
		for (CodeSnippet method : codeSnippetList)
		{
			System.out.println("--");
			System.out.println(method);
		}
	}
	
	//loop directory to get file list
	private void ParseFilesInDir() throws IOException
	{
		//				File dirs = new File("."); 				// For files under this folder
		//				String dirPath = dirs.getCanonicalPath() + File.separator+"src"+File.separator;
		//				System.out.println(dirPath);
		File root = new File(this.folderPath);
		File[] files = root.listFiles ( );
		String filePath = null;
		if( files != null)
		{
			for (File f : files ) {
				filePath = f.getAbsolutePath();
				if(f.isFile()){
		/* this implementation require endLine to be update by its codeSnippet constructor */
					fileContent = SourceFileReader.readFileToString(filePath);
					fileContentePerLine = fileContent.split("\r\n|\r|\n");
					@SuppressWarnings("unused")
					JavaParser parser = new JavaParser(fileContent);
				}
			}
		}
	}
	
	

	public static String[] getFileContentePerLine() {
		return fileContentePerLine;
	}
	
	public ArrayList<CodeSnippet> getCodeSnippetList()
	{
		return codeSnippetList;
	}

	public static void addToCodeSnippetList(CodeSnippet method)
	{
		CodeSnippetFactory.codeSnippetList.add(method);
	}
	
	public static Integer getIndexCSList(CodeSnippet method)
	{
		return CodeSnippetFactory.codeSnippetList.indexOf(method); // -1 if does not exist
	}
	
	public static String getFileContent()
	{
		return fileContent;
	}
}
