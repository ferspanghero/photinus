package edu.uci.ics.sdcl.firefly;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

public class CodeSnippetFactory {
	private String folderPath;
	private String fileContent;
	private String fileName;
	private String[] fileContentPerLine;
	private ArrayList<CodeSnippet> codeSnippetList= new ArrayList<CodeSnippet>();

	//public CodeSnippetFactory(String folderPathArg)
	//	{
	//		this.folderPath = folderPathArg;
	//	}

	public CodeSnippetFactory(String fileName, String fileContent){
		this.fileContent = fileContent;
		this.fileName = fileName;
		this.fileContentPerLine = this.fileContent.split("\r\n|\r|\n");
	}

	/** Generates code snippets for a file content  in memory (attribute fileContent) */
	public ArrayList<CodeSnippet> generateSnippetsForFile()
	{
		this.codeSnippetList = new ArrayList<CodeSnippet>(); //Cleans up the snippet list
		@SuppressWarnings("unused")
		JavaParser parser = new JavaParser(this);
		this.setCallers(this.codeSnippetList);
		this.setMethodTexts(this.codeSnippetList, this.fileContentPerLine);
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
					this.fileContent = SourceFileReader.readFileToString(filePath);
					this.fileContentPerLine = fileContent.split("\r\n|\r|\n");
					@SuppressWarnings("unused")
					JavaParser parser = new JavaParser(this);
				}
			}
		}
	}

	public void setCallers(ArrayList<CodeSnippet> codeSnippets)
	{
		System.out.println(" <> ");
		/* building and populating callers-callees structure */
		HashMap<CodeSnippet, ArrayList<String>> callersCalles = new HashMap<CodeSnippet, ArrayList<String>>();
		for (CodeSnippet codeSnippet : codeSnippets) {	// for each method (caller)
			ArrayList<String> callees = new ArrayList<String>();
			System.out.println("Caller: " + codeSnippet.getMethodSignature().getName());
			for (CodeElement codeElement : codeSnippet.getStatements()) {	// find the callees (within the method)
				if (CodeElement.METHOD_INVOCATION == codeElement.getType())
				{
					MyMethodCall methodInvocation = (MyMethodCall)codeElement;
					callees.add(methodInvocation.getName());
					System.out.println("--callee: " + methodInvocation.getName()); 
				}
			}
			callersCalles.put(codeSnippet, callees); //add to the hash map
			System.out.println(" <> ");
		}

		/* Updating the methods callers */
		for (CodeSnippet codeSnippet : codeSnippets) {
			String methodName = codeSnippet.getMethodSignature().getName();	// callee name
			Set<Map.Entry<CodeSnippet, ArrayList<String>>> set = callersCalles.entrySet();
			Iterator<Entry<CodeSnippet, ArrayList<String>>> i = set.iterator();
			while(i.hasNext()) 	// searching for caller (from other methods within this codeSnippet list)
			{
				Map.Entry<CodeSnippet, ArrayList<String>> me = (Map.Entry<CodeSnippet, ArrayList<String>>)i.next();
				if (-1 != me.getValue().indexOf(methodName))	// this methodName is called so...
					codeSnippet.addCaller(me.getKey());		// ...update its caller list field
			}
		}

	}

	// assuming that the method is NOT OVERLOADED
	private void setMethodTexts(ArrayList<CodeSnippet> codeSnippetsArg, String[] fileContentPerLineArg)
	{
		for (CodeSnippet codeSnippet : codeSnippetsArg) {
			int startingLine = codeSnippet.getMethodSignature().getLineNumber() -1;	// -1 because the vector starts from 0
			int endingLine = codeSnippet.getBodyEndingLine() -1;	// -1 because the vector starts from 0
			/* getting method content */
			Scanner scanner = new Scanner(this.fileContent);
			int j = 0;
			StringBuilder builder = new StringBuilder();
			String newline = System.getProperty("line.separator");
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if ( (j >= startingLine) && (j <= endingLine) )
				{
					builder.append(line + newline);
//					System.out.println("line: " + line);
				}
				j++;
				if (j > endingLine) break;
//				System.out.println("j: " + j);
			}
			scanner.close();
			/* updating on respective CodeSnippet */
			codeSnippet.setCodeSnippetFromFileContent(builder.toString());
//			System.out.println("@@@@@@@@@@@@@@@@@@@");
//			System.out.println(codeSnippet.getCodeSnippetFromFileContent());
			
			
		}
		System.out.println();
	}

	public String getFileName(){
		return fileName;
	}

	public String[] getFileContentPerLine() {
		return fileContentPerLine;
	}

	public ArrayList<CodeSnippet> getCodeSnippetList()
	{
		return codeSnippetList;
	}

	public void addToCodeSnippetList(CodeSnippet method)
	{
		this.codeSnippetList.add(method);
	}

	public String getFileContent()
	{
		return fileContent;
	}
}
