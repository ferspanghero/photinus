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
import edu.uci.ics.sdcl.firefly.memento.MicrotaskMemento;

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
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//process only if its multipart content
		if(ServletFileUpload.isMultipartContent(request)){
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			String return_message ="";
			try {
				List<FileItem>  items = upload.parseRequest(request);
				for(FileItem item: items){//Already enables more than one file upload.
					if (!item.isFormField()) { 
						String fileName = item.getName(); 
						long sizeInBytes = item.getSize();

						if(sizeInBytes>0){
							String fileContent = new Scanner(item.getInputStream(),"Cp1252").useDelimiter("\\A").next();		  				
							return_message = "File "+ fileName+" was successfully uploaded, size: "+sizeInBytes+"\n";
							String results = generateMicrotasks(fileName, fileContent);
							return_message = return_message +"\n"+ results ;
						}
						else
							return_message = "File "+ fileName+" is empty!";
						request.setAttribute("return_message",return_message);
					}
					else
						request.setAttribute("return_message",
								"Sorry this servlet only handles file upload requests.");
				}
			} catch (FileUploadException e) {
				request.setAttribute("return_message", "File upload failed. " + e);
			}
		}
		else
			request.setAttribute("return_message",
					"Sorry this servlet only handles file upload requests.");

		request.getRequestDispatcher("/FileUpload.jsp").forward(request, response);
	}


	private String generateMicrotasks(String fileName, String fileContent){
		//calls CodeSnippetFactory
		CodeSnippetFactory codeSnippetFactory = new CodeSnippetFactory(fileName,fileContent);
		ArrayList<CodeSnippet> snippetList = codeSnippetFactory.generateSnippetsForFile();

		//calls QuestionFactory
		QuestionFactory questionFactory = new QuestionFactory ();
		HashMap<Integer, Microtask> microtaskMap = questionFactory.generateQuestions(snippetList);
		FileDebugSession fileDebuggingSession = new FileDebugSession(fileName,microtaskMap);

		//Persist data
		MicrotaskMemento memento = new MicrotaskMemento();
		memento.insert(fileName, fileDebuggingSession);

		int numberOfCodeSnippets = snippetList.size();
		int numberOfMicrotasks = microtaskMap.size();

		String results = "";
		if (microtaskMap!= null && microtaskMap.size() > 0){
			results = "Microtasks generated: " + numberOfMicrotasks+", "+"Number of code snippets: "+numberOfCodeSnippets;
		}
		else
			results = "No Microtasks were generated. Please review the file.";

		return results;
	}



}
