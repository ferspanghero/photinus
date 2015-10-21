package edu.uci.ics.sdcl.firefly.servlet;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;

import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.SkillTest;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.controller.StorageStrategy;
import edu.uci.ics.sdcl.firefly.storage.SkillTestStorage;
import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;

/**
 * Servlet implementation class SkillTestServlet
 * 
 * @author Christian Medeiros Adriano
 */
public class SkillTestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String QUESTION1 = "QUESTION1";
	public static final String QUESTION2 = "QUESTION2";
	public static final String QUESTION3 = "QUESTION3";
	public static final String QUESTION4 = "QUESTION4";

	private String SorryPage = "/SorryPage.jsp";
	private String ErrorPage = "/ErrorPage.jsp";
	private final String QuestionMicrotaskPage = "/QuestionMicrotask.jsp";
	private final String MicrotaskLoadTestPage = "/MicrotaskLoadTest.jsp";

	private Worker worker;
	private StorageStrategy storage;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SkillTestServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String workerId = request.getParameter("workerId");
		if (workerId != null) {
			request.setAttribute("workerId", workerId);
			request.setAttribute("timeStamp", TimeStampUtil.getTimeStampMillisec());
			// First check if the worker hasn't already taken the test
			this.storage = StorageStrategy.initializeSingleton();
			;
			this.worker = storage.readExistingWorker(workerId);
			if (this.worker == null) {
				showErrorPage(request, response, "Execution ID does not exist in database.");
			} else if (worker.hasTakenTest()) {// worker has taken the test for
												// this file
				sorryPage(request, response, "Dear participant, you have already taken this task,"
						+ " please obtain the link for a new task and try again."
						+ " Thank you. In case you have doubts, please send an email" + " to adrianoc@uci.edu.");
			} else {
				int grade = this.processAnswers(request);
				worker.setGrade(grade);
				if (worker.hasPassedTest()) {
					loadFirstMicrotask(request, response);
				} else {
					request.setAttribute("message",
							"Dear worker, you didn't get the minimal qualifying grade to perform the task.");
					request.getRequestDispatcher(SorryPage).include(request, response);
				}
			}
		}
	}

	/**
	 * Not used
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	private int processAnswers(HttpServletRequest request) {
		// Retrieve time taken to answer
		String timeStamp = this.worker.getConsentDate();
		String duration = TimeStampUtil.computeElapsedTime(timeStamp, TimeStampUtil.getTimeStampMillisec());

		// Retrieve answers
		String[] answers = new String[5];
		for (int i = 0; i < answers.length; i++) {
			answers[i] = request.getParameter("QUESTION" + (i + 1));
		}
		SkillTestStorage skillStorage = new SkillTestStorage();
		SkillTest exam = skillStorage.getSource(worker.getCurrentFileName());
		boolean[] gradeMap = exam.applyRubrics(answers);
		int grade = this.countCorrectAnswers(gradeMap);

		worker.setSkillAnswers(exam.getAnswers(), gradeMap, answers, grade, duration);

		storage.insertSkillTest(this.worker);

		return grade;
	}

	private int countCorrectAnswers(boolean[] gradeMap) {
		int grade = 0;
		for (boolean b : gradeMap) {
			grade += (b) ? 1 : 0;
		}
		return grade;
	}

	private void loadFirstMicrotask(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String fileName = worker.getCurrentFileName();
		WorkerSession session = storage.readNewSession(worker.getWorkerId(), fileName);
		if (session == null || session.isClosed())
			// Means that it is the first worker session. There should be at
			// least one microtask. If not it is an Error.
			showErrorPage(request, response, "@ SkillTestServlet - no microtask available");
		else {
			// Restore data for next Request
			request.setAttribute("fileName", fileName);
			request.setAttribute("timeStamp", TimeStampUtil.getTimeStampMillisec());

			// Load the new Microtask data into the Request

			// Data for the Progress bar
			request.setAttribute("currentTask", session.getCurrentIndexPlus());
			request.setAttribute("totalTasks", session.getMicrotaskListSize());

			// For load test
			//Boolean isTest = Boolean.valueOf(request.getParameter("isTest"));
			// System.out.println("isTest: "+isTest);
			// request = MicrotaskServlet.generateRequest(request,
			// storage.getNextMicrotask(session.getId()));
			// if(isTest)
			// request.getRequestDispatcher(MicrotaskLoadTestPage).forward(request,
			// response);
			// else
			// request.getRequestDispatcher(QuestionMicrotaskPage).forward(request,
			// response);

			Microtask microtask = storage.getNextMicrotask(session.getId());

			String decisionPointId = microtask.getQuestionType() + microtask.getQuestion(); // I.E: UI1, AR4, etc.
			String encryptedDecisionPointId = getEncryptedData(decisionPointId.getBytes());

			if (encryptedDecisionPointId == null || worker.getSessionId() == null || worker.getWorkerId() == null) {
				throw new RuntimeException("Error while encrypting microtask " + microtask.getID());
			}

			String redirectURL = "http://sdclturkone.meteor.com/" + worker.getSessionId() + "/" + worker.getWorkerId() + "/" + encryptedDecisionPointId;			
			
			// Redirects to CrowdDesign prototype
			response.sendRedirect(redirectURL); 
		}
	}

	private String getEncryptedData(byte[] data) {
		try {
			KeyGenerator KeyGen = KeyGenerator.getInstance("AES");
			KeyGen.init(128);

			Cipher encryptCipher = Cipher.getInstance("AES");

			byte[] key = "1234567890ABCDEF".getBytes(Charsets.UTF_8);			

			SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

			encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			
			byte[] encryptedData = Base64.getEncoder().encode(encryptCipher.doFinal(data));
					
			return new String(encryptedData, Charsets.UTF_8).replace("+", "_").replace("/", "@"); // Replaces slash with underscore to avoid messing with the URL
		} catch (Exception ex) {
			System.out.println(ex);
		}

		return null;
	}

	private void showErrorPage(HttpServletRequest request, HttpServletResponse response, String message)
			throws ServletException, IOException {
		request.setAttribute("error", message);
		request.setAttribute("executionId", this.worker.getWorkerId());
		request.getRequestDispatcher(ErrorPage).include(request, response);
	}

	private void sorryPage(HttpServletRequest request, HttpServletResponse response, String message)
			throws ServletException, IOException {
		request.setAttribute("message", message);
		request.getRequestDispatcher(SorryPage).include(request, response);
	}

}
