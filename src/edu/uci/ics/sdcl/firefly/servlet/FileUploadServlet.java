package edu.uci.ics.sdcl.firefly.servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.CodeSnippetFactory;
import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.QuestionFactory;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;

import java.util.*; 

public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileUploadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//process only if its multipart content
		String targetName = new String();
		String fileName = new String();
		String fileContent = new String();
		if(ServletFileUpload.isMultipartContent(request)){
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			String return_message ="";
			try {
				List<FileItem>  items = upload.parseRequest(request);
				for(FileItem item: items){	//Already enables more than one file upload.
					if (!item.isFormField()) { 
						fileName = item.getName(); 
						long sizeInBytes = item.getSize();
						if(sizeInBytes>0){
							// reading fileContent
							Scanner scanner = new Scanner(item.getInputStream(),"Cp1252");
							fileContent = scanner.useDelimiter("\\A").next();		  				
							return_message = "File <b>"+ fileName+"</b> was successfully uploaded.<br> Size: "+sizeInBytes+" bytes <br>";
							scanner.close();
						}
						else
							return_message = "File <b>"+ fileName+"</b> is empty!";
						
						request.setAttribute("return_message",return_message);
						request.setAttribute("fileName", fileName); 
						//request.setAttribute("source", source);
					}
					else{
						// getting specific method name
						if (item.getFieldName().equalsIgnoreCase("targetMethod")){
							targetName = item.getString();
							String results = generateMicrotasks(fileName, fileContent, targetName);
							return_message = return_message + results;
						}
						request.setAttribute("return_message", return_message);
					}
				}
			} catch (FileUploadException e) {
				request.setAttribute("return_message", "File upload failed. " + e);
			}
		}
		else
			request.setAttribute("return_message","");
 
		request.getRequestDispatcher("/FileUpload.jsp").forward(request, response);
	}


	private String generateMicrotasks(String fileName, String fileContent, String methodName){
		//calls CodeSnippetFactory
		CodeSnippetFactory codeSnippetFactory = new CodeSnippetFactory(fileName,fileContent);
		ArrayList<CodeSnippet> snippetList = codeSnippetFactory.generateSnippetsForFile();
		
		//filtering by methodName
		System.out.print("candidates: " + methodName);
		ArrayList<CodeSnippet> filteredCodeSnippets = new ArrayList<CodeSnippet>();
		for (CodeSnippet codeSnippet : snippetList) {
			System.out.print(codeSnippet.getMethodSignature().getName() + ", ");
			if (codeSnippet.getMethodSignature().getName().equals(methodName))
				filteredCodeSnippets.add(codeSnippet);
		}
		System.out.println();
		//calls QuestionFactory
		QuestionFactory questionFactory = new QuestionFactory ();
		HashMap<Integer, Microtask> microtaskMap = questionFactory.generateQuestions(filteredCodeSnippets);
		FileDebugSession fileDebuggingSession = new FileDebugSession(fileName,fileContent, microtaskMap);

		//Persist data
		MicrotaskStorage memento = new MicrotaskStorage();
		memento.replace(fileName, fileDebuggingSession);

		int numberOfCodeSnippets = snippetList.size();
		int numberOfMicrotasks = microtaskMap.size();

		String results = "";
		if (microtaskMap!= null && microtaskMap.size() > 0){
			results = "Number of code snippets: "+numberOfCodeSnippets+ "<br> Microtasks generated: " + numberOfMicrotasks+"<br>";
		}
		else
			results = "No Microtasks were generated. Please review the file and method name.";
		System.out.println("Results: "+results);
		return results;
	}



}

