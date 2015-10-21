package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.WorkerSessionFactory;
import edu.uci.ics.sdcl.firefly.controller.StorageStrategy;
import edu.uci.ics.sdcl.firefly.report.ReportGenerator;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerStorage;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;
import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;

public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileUploadServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String subAction = request.getParameter("subAction");

		if (subAction != null) {

			if (subAction.compareTo("generateWorkerSessions") == 0) {
				String return_message = this.generateWorkerSessions();
				// makeSessionsAvailable();
				String microtasks_message = request.getParameter("microtasks_message");
				request.setAttribute("microtasks_message", microtasks_message);
				request.setAttribute("workerSessions_message", return_message);
				request.getRequestDispatcher("/FileUpload.jsp").forward(request, response);
			} else if (subAction.compareTo("delete") == 0) {
				this.delete();
				request.setAttribute("workerSessions_message", "");
				request.setAttribute("reports_message", "<b>Repositories Deleted!</b>");
				request.getRequestDispatcher("/FileUpload.jsp").forward(request, response);
			} else if (subAction.compareTo("generateReports") == 0) {
				ReportGenerator generator = new ReportGenerator();
				generator.runReports();
				request.setAttribute("reports_message", "Reports Generated!");
				request.getRequestDispatcher("/FileUpload.jsp").forward(request, response);
			} else {
				request.setAttribute("error", "SubAction unknown");
				request.setAttribute("executionId", "before consent");
				request.getRequestDispatcher("/ErrorPage.jsp").include(request, response);
			}
			StorageStrategy storage = StorageStrategy.initializeSingleton();
			storage.killSingleton();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// process only if its multipart content
		String targetMethodName = new String();
		String numberOfParameters = null;
		String bugReport = new String();
		String testCase = new String();
		String fileName = new String();
		String fileContent = new String();
		String workerId = new String();
		boolean loadPerMethod = false;

		if (ServletFileUpload.isMultipartContent(request)) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			String return_message = "";
			try {
				List<FileItem> items = upload.parseRequest(request);
				for (FileItem item : items) { // Already enables more than one
												// file upload.
					if (!item.isFormField()) {
						fileName = item.getName();
						long sizeInBytes = item.getSize();
						if (sizeInBytes > 0) {
							// reading fileContent
							Scanner scanner = new Scanner(item.getInputStream(), "Cp1252");
							fileContent = scanner.useDelimiter("\\A").next();
							return_message = "File <b>" + fileName + "</b> was successfully uploaded.<br> Size: "
									+ sizeInBytes + " bytes <br>";
							scanner.close();
						} else {
							// No fileName provided, so it will execute a
							// standard load.
							loadPerMethod = false;
						}
						request.setAttribute("fileName", fileName);
					} else {
						// getting bug report
						if (item.getFieldName().equalsIgnoreCase("bugReport")) {
							bugReport = item.getString();
						}
						if (item.getFieldName().equalsIgnoreCase("testCase")) {
							testCase = item.getString();
						}
						// getting specific method name
						if (item.getFieldName().equalsIgnoreCase("targetMethod")) {
							StringTokenizer tokenizer = new StringTokenizer(item.getString());
							if (tokenizer.countTokens() == 2) {
								targetMethodName = tokenizer.nextToken();
								numberOfParameters = tokenizer.nextToken();
							} else {
								targetMethodName = item.getString();
							}
							if (targetMethodName != null && targetMethodName.length() > 0)
								loadPerMethod = true;
						}
						if (item.getFieldName().equalsIgnoreCase("workerId"))
							workerId = item.getString();
					}
				}

				// Sanitize the content removing the tabs because the Javascript
				// editor counts a tab as one space, which a tab is actually 4
				// spaces.
				fileContent = fileContent.replaceAll("\t", "    ");

				// Store the workerId Researcher
				WorkerStorage workerStorage = WorkerStorage.initializeSingleton();
				;
				Worker worker = new Worker(workerId, TimeStampUtil.getTimeStampMillisec(), "default");
				workerStorage.insertConsent(worker);

				// generating microtasks individualized or in bulk
				if (loadPerMethod) {
					return_message = return_message + generateMicrotasks(fileName, fileContent, targetMethodName,
							numberOfParameters, bugReport, testCase);
					;
				} else {// in bulk
					return_message = return_message + this.bulkUpload();
				}
				request.setAttribute("workerSessions_message", "");
				request.setAttribute("microtasks_message", return_message);

			} catch (FileUploadException e) {
				request.setAttribute("microtasks_message", "File upload failed. " + e);
			}
		} else
			request.setAttribute("microtasks_message", "");

		request.getRequestDispatcher("/FileUpload.jsp").forward(request, response);
	}

	private String generateMicrotasks(String fileName, String fileContent, String methodName, String numberOfParameters,
			String bugReport, String testCase) {
		// 2 types of tasks: User Interface (UI) and Architecture (AR)
		// 4 decision points per type
		// 20 users per decision point
		// Total: 2 x 4 x 20 = 160 tasks
		final int MAX_DECISION_POINTS = 4;
		final int MAX_WORKERS = 20;
		Hashtable<Integer, Microtask> microtasksTable = new Hashtable<>(MAX_DECISION_POINTS * MAX_WORKERS);
		int microtaskId = 1;

		for (int j = 1; j <= MAX_WORKERS; j++) {
			for (int k = 1; k <= MAX_DECISION_POINTS; k++) {
				Microtask microtask = new Microtask(Integer.toString(k), microtaskId++, null, fileName);

				microtask.setQuestionType(fileName);
				microtasksTable.put(microtask.getID(), microtask);
			}
		}		

		String results = "";

		MicrotaskStorage storage = MicrotaskStorage.initializeSingleton();
		FileDebugSession fileDebuggingSession = new FileDebugSession(fileName, fileContent, microtasksTable);
		int generatedMicrotasks = microtasksTable.size();

		// Persist data
		storage.insert(fileName, fileDebuggingSession); // append to
														// existing
														// fileDebugSessions

		int numberOfMicrotasks = storage.getNumberOfMicrotask();
		results = "Microtasks generated: " + generatedMicrotasks + "<br>" + "Total Microtasks available now: "
				+ numberOfMicrotasks + "<br>";

		Logger logger = LoggerFactory.getLogger(FileUploadServlet.class);
		logger.info("EVENT =FileUpload; FileName=" + fileName + "; Microtasks=" + generatedMicrotasks);

		System.out.println("Results: " + results);

		return results;
	}

	private int countSessionsInMap(Hashtable<String, Stack<WorkerSession>> newSessionsMap) {
		int counter = 0;
		Iterator<String> iter = newSessionsMap.keySet().iterator();
		while (iter.hasNext()) {
			String fileName = iter.next();
			System.out.println("fileName: " + fileName);
			Stack<WorkerSession> sessionStack = newSessionsMap.get(fileName);
			counter = counter + sessionStack.size();
		}
		return counter;
	}

	private String generateWorkerSessions() {

		String results = new String();

		PropertyManager manager = PropertyManager.initializeSingleton();

		// Generate the stack of New and Duplicated WorkerSession
		WorkerSessionFactory sessionFactory = new WorkerSessionFactory(manager.microtasksPerSession);

		Hashtable<String, Stack<WorkerSession>> newSessionsMap = sessionFactory
				.generateSessions(manager.answersPerMicrotask);

		WorkerSessionStorage storage = WorkerSessionStorage.initializeSingleton();

		storage.writeNewWorkerSessionMap(newSessionsMap);

		int totalSessions = countSessionsInMap(newSessionsMap);
		int existingSessions = storage.getNumberOfNewWorkerSessions();

		results = "<b>Sessions generated: </b>" + totalSessions + "<br>" + "<b>Total Sessions available now: </b> "
				+ existingSessions;

		// System.out.println("Results: "+results);

		return results;
	}

	/*
	 * Used for testing purposes
	 */
	private String generateSingleWorkerSession() {

		String results = new String();

		// Generate the stack of New and Duplicated WorkerSession
		WorkerSessionFactory sessionFactory = new WorkerSessionFactory(20);
		Hashtable<String, Stack<WorkerSession>> newSessionsMap = sessionFactory.generateSingleSession();

		WorkerSessionStorage storage = WorkerSessionStorage.initializeSingleton();

		storage.writeNewWorkerSessionMap(newSessionsMap);

		int totalSessions = countSessionsInMap(newSessionsMap);
		int existingSessions = storage.getNumberOfNewWorkerSessions();

		results = results + "Sessions generated: " + totalSessions + "<br>" + "Total Sessions available now: "
				+ existingSessions;

		System.out.println("Results: " + results);

		return results;
	}

	private void delete() {
		// Clean up memory
		StorageStrategy storage = StorageStrategy.initializeSingleton();
		storage.cleanUpRepositories();
	}

	/**
	 * Older bug reports (experiment-1) String[] fileList =
	 * {"1buggy_ApacheCamel.txt", "2SelectTranslator_buggy.java",
	 * "3buggy_PatchSetContentRemoteFactory_buggy.txt",
	 * "6ReviewScopeNode_buggy.java", "7buggy_ReviewTaskMapper_buggy.txt",
	 * "8buggy_AbstractReviewSection_buggy.txt", "9buggy_Hystrix_buggy.txt",
	 * "10HashPropertyBuilder_buggy.java",
	 * "11ByteArrayBuffer_buggy.java","13buggy_VectorClock_buggy.txt"};
	 * 
	 * String [] methodList = {
	 * "acquireExclusiveReadLock","appendColumn","addComments",
	 * "convertScopeToDescription",
	 * "mapScope","appendMessage","endCurrentThreadExecutingCommand",
	 * "calculateNumPopulatedBytes","grow","merge"};
	 * 
	 * String [] failureList = { "Program execution causes NullPointerException"
	 * ,"Program execution causes NullPointerException",
	 * "When we can't get a file version for whatever reason, an Null Pointer Exception occurs."
	 * ,"Program execution causes NullPointerException",
	 * "Program execution causes UnsupportedMethodException",
	 * "Program execution causes ClassCastException",
	 * "Program execution causes NoSuchElementException",
	 * "Probing algorithm spinning indefinitely trying to find a hole in a byte sequence."
	 * , "NegativeArraySizeException for data larger than 2GB / 3." ,
	 * "java.lang.IllegalArgumentException: Version -532 is not in the range (1, 32767) in ClockEntry constructor."
	 * };
	 * 
	 */

	/**
	 * Loads files in bulk. The files must be in the local folder (typically
	 * ./samples/bulkLoadPhotinus)
	 * 
	 * @return
	 */
	private String bulkUpload() {

		Logger logger = LoggerFactory.getLogger(FileUploadServlet.class);
		PropertyManager manager = PropertyManager.initializeSingleton();
		String path = manager.fileUploadSourcePath;
		System.out.println("path: " + path);

		// To avoid changes, the concept of CrowdDebug "files" will be borrowed
		// to represent CrowdDesign task hard-coded types
		String[] fileList = { "UI", "AR" };// ,"HIT10_59.java"};

		// String[] methodList = { "forOffsetHoursMinutes", "getPaint",
		// "translate", "updateBounds", "add", "addNumber",
		// "toClass", "toLocale" };// ,"findLevel"};

		// String[] failureList = { "java.lang.IllegalArgumentException: Minutes
		// out of range:-15",
		// "java.lang.IllegalArgumentException: Color parameter outside of
		// expected range: Red Green Blue",
		// "java.lang.StringIndexOutOfBoundsException: String index out of
		// range:2",
		// "junit.framework.AssertionFailedError: expected:&lt;1&gt; but
		// was:&lt;3&gt;",
		// "java.lang.ClassCastException: [Ljava.lang.Object; cannot be cast to
		// [Ljava.lang.String;",
		// "junit.framework.ComparisonFailure: expected:&lt;var x=[-0.]0&gt; but
		// was:&lt;var x=[]0&gt;",
		// "java.lang.NullPointerException",
		// "java.lang.IllegalArgumentException: Invalid locale format:fr__POSIX"
		// };
		// "Elevator block will not function.",
		// };

		// String[] testList = {
		// "assertEquals(DateTimeZone.forID(\"-02:15\"),
		// DateTimeZone.forOffsetHoursMinutes(-2, -15));",
		// "GrayPaintScale gps = new GrayPaintScale(); c = (Color)
		// gps.getPaint(-0.5); assertTrue(c.equals(Color.black));",
		// "assertEquals(\"\uD83D\uDE30\",
		// StringEscapeUtils.escapeCsv(\"\uD83D\uDE30\"));",
		// "TimePeriodValues s = new TimePeriodValues(\"Test\"); s.add(new
		// SimpleTimePeriod(0L, 50L), 3.0); assertEquals(1,
		// s.getMaxMiddleIndex());",
		// "String[] stringArray=null; String aString=null; String[] sa=
		// ArrayUtils.add(stringArray,aString); fail(\"Should have caused
		// IllegalArgumentException\");",
		// "assertEquals(\"var x = -0.0\", parsePrint(\"var x = -0.0;\", false,
		// CodePrinter.DEFAULT_LINE_LENGTH_THRESHOLD));",
		// "assertTrue(Arrays.equals(new Class[]{String.class, null,
		// Double.class}, ClassUtils.toClass(new Object[]{\"Test\",null,99d} ) )
		// );",
		// "Locale locale = LocaleUtils.toLocale(\"fr__POSIX\");" };
		// "Assuming the default searchDistance is 20 blocks, set a elevator
		// block at y=20 then another at y=40.",
		// };

		String message = "";
		for (int i = 0; i < fileList.length; i++) {
			String fileName = fileList[i];
			// String methodName = methodList[i];
			// String failureDescription = failureList[i];
			// String testDescription = testList[i];

			// CrowdDesign does not use file inputs
			String fileContent = null; // SourceFileReader.readFileToString(path
										// + fileName);

			String result = generateMicrotasks(fileName, fileContent, "CrowdDesign", null, "CrowdDesign",
					"CrowdDesign");
			logger.info("Event =UPLOAD; File=" + fileName + "; MethodName=" + "CrowdDesign" + "; Microtasks=" + result);
			message = message + "CrowdDesign" + ", ";
		}
		return "<b>Loaded methods: </b>" + message.substring(0, message.length() - 2);
	}
}
