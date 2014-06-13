package edu.uci.ics.sdcl.firefly;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CodeSnippetFactory {
	private String folderPath;
	private static ArrayList<CodeSnippet> codeSnippetList;
	
	public CodeSnippetFactory(String folderPathArg)
	{
		this.folderPath = folderPathArg;
		CodeSnippetFactory.codeSnippetList = new ArrayList<CodeSnippet>();
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
					@SuppressWarnings("unused")
					JavaParser parser = new JavaParser(SourceFileReader.readFileToString(filePath));
				}
			}
		}
	}
}
