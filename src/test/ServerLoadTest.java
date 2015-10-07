package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.Before;
import org.junit.Test;
//import org.openqa.selenium.support.ui.Sleeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

import edu.uci.ics.sdcl.firefly.util.PropertyManager;
import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;

public class ServerLoadTest implements Runnable{

	//Number of THREADS
	private static int maxThreads =20;
	
	//private static Logger loggerInfo;
	private static Logger loggerConsent;
	private static Logger loggerSession;
	WebTester webTester;

	//Used for HTMLUnit
	private WebClient webClient;
	private String workerId="0";
	private static int threadId=0;
	private int defaultId;
	private int myThread;
	private static String localPath = "http://localhost:8080/Photinus/";
	private static String serverPath = "http://dellserver.ics.uci.edu:8080/crowd_debug/";
	private static String path;
	private static String filename = "HIT04_7";//"8_DateTimeZone";//"6_CharSequenceTranslator";//
	private static int microtaskPerSession = 0;
//	private HtmlPage nextPage;

	@Before
	public void setupHTMLUnit(){
		//logger = LoggerFactory.getLogger(ServerLoadTest.class);
	}

	public ServerLoadTest(int myThread){
		PropertyManager property = PropertyManager.initializeSingleton();
		microtaskPerSession = property.microtasksPerSession;
		//loggerInfo = LoggerFactory.getLogger("info");
		loggerConsent = LoggerFactory.getLogger("consent");
		loggerSession = LoggerFactory.getLogger("session");
		this.myThread = myThread;
		this.defaultId = this.myThread+50; //in case there is no workerID returned
		this.webClient = new WebClient(BrowserVersion.CHROME);
		WebClientOptions options = this.webClient.getOptions();
		//to stop htmlUnit go verbose on console about css/javaScript errors with:
		this.webClient.setCssErrorHandler(new SilentCssErrorHandler());    
		this.webClient.setJavaScriptErrorListener(new JavaScriptErrorListener(){
			@Override
			public void loadScriptError(HtmlPage arg0, URL arg1, Exception arg2) {}
			@Override
			public void malformedScriptURL(HtmlPage arg0, String arg1,MalformedURLException arg2) {}
			@Override
			public void scriptException(HtmlPage arg0, ScriptException arg1) {}
			@Override
			public void timeoutError(HtmlPage arg0, long arg1, long arg2) {}});
		
		options.setThrowExceptionOnFailingStatusCode(false);
		options.setThrowExceptionOnScriptError(false);
		
		options.setTimeout(14000);
		options.setCssEnabled(false);
		this.webClient.setJavaScriptTimeout(14000);
	}


	public static void main(String args[]){
		try{
			path = serverPath; //localPath;
			while(threadId<maxThreads){
				threadId++;
				System.out.println("Thread ="+threadId);
				(new Thread(new ServerLoadTest(threadId))).start();
			}
		}
		catch(Exception e){
			System.err.println("FAILED Thread ="+threadId);
		}
	}
	
	/*//@Override
	public void run2(){
		try{
			CookieManager manager = new CookieManager();
			manager.clearCookies();
			System.out.println("Running Consent");
			runConsent();
			runSurvey();
			runMicrotask();
		}
		catch(Exception e){
			System.err.println(e.toString());
		}
	}*/

	@Override
	public void run(){
		HtmlPage nextPage = null;
		try{
			CookieManager manager = new CookieManager();
			manager.clearCookies();
			if((nextPage = runConsent(nextPage)) != null)
				if((nextPage = runSurvey(nextPage)) != null){
					if((nextPage = runSkillTest(nextPage)) != null)
					{
						boolean success=true;
						int i=0;
						while(success && i< 3){
							success = ((nextPage = runMicrotask(nextPage)) != null);
							i++;
						}
						loggerSession.info("Thread ="+this.myThread+"; answered="+new Integer(i).toString()+" times.");
						if(success)
							nextPage = runFeedback(nextPage);
					}
				}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}




	//@Test
	public HtmlPage runConsent(HtmlPage nextPage)throws Exception {
		
		final HtmlPage page = this.webClient.getPage(path+"ConsentForm.jsp"+"?"+filename);
		System.out.println("STARTED : "+page.getTitleText());

		final HtmlForm form = page.getFormByName("consentForm");
		final HtmlButtonInput button = form.getInputByName("yesButton");
		final HtmlCheckBoxInput checkBox = form.getInputByName("consentBox");

		// Change the value of the text field
		checkBox.click();

		// Now submit the form by clicking the button and get back the second page.
		nextPage = button.click();

		//System.out.println("Consent , nextpage="+nextPage.getTitleText());
		if(nextPage.getTitleText().matches("Error Page")){
			final HtmlForm messageForm = nextPage.getFormByName("errorForm");
			final HtmlInput messageInput = messageForm.getInputByName("message");
			String message = messageInput.getValueAttribute();
			this.workerId = Integer.toString(this.defaultId);
			loggerConsent.info("Consent=FAILED; threadId= "+ this.myThread+ "; Page="+ nextPage.getTitleText() + "; message="+message);
			return null;
		}
		else{// The survey page shows up
			//obtain the workerId
			System.out.println("ENDED : "+nextPage.getTitleText());
			final HtmlForm surveyForm = nextPage.getFormByName("surveyForm");
			final HtmlHiddenInput workerIdInput = surveyForm.getInputByName("workerId");
			this.workerId = workerIdInput.getValueAttribute();
			loggerConsent.info("Consent=SUCCESS; workerId= "+ this.workerId+ "; Page="+ nextPage.getTitleText());
			Thread.sleep(50);
			return nextPage;
		}
	}


	public HtmlPage runSkillTest(HtmlPage nextPage) throws Exception	{
		//final HtmlPage page = this.webClient.getPage(path+"SkillTest.jsp");
		System.out.println("STARTED : "+nextPage.getTitleText());
		final HtmlForm form = nextPage.getFormByName("testForm");

		final HtmlInput radio1 = form.getInputByName("QUESTION1");
		radio1.click();
		radio1.setValueAttribute("e"); //d WORDFINDER
		final HtmlRadioButtonInput radio2 = form.getInputByName("QUESTION2");
		radio2.click();
		radio2.setValueAttribute("b"); //b
		final HtmlRadioButtonInput radio3 = form.getInputByName("QUESTION3");
		radio3.click();
		radio3.setValueAttribute("a"); //e
		final HtmlRadioButtonInput radio4 = form.getInputByName("QUESTION4");
		radio4.click();
		radio4.setValueAttribute("d"); //a
		final HtmlRadioButtonInput radio5 = form.getInputByName("QUESTION5");
		radio5.click();
		radio5.setValueAttribute("d"); //d
		
		form.setActionAttribute("skillTest");
		HtmlHiddenInput input1 = form.getInputByName("workerId");
		input1.setValueAttribute(this.workerId);
		HtmlHiddenInput input2 = form.getInputByName("timeStamp");
		input2.setValueAttribute(TimeStampUtil.getTimeStampMillisec());
		HtmlHiddenInput input3 = form.getInputByName("subAction");
		input3.setValueAttribute("gradeAnswers");
		
		HtmlInput isTest = form.getInputByName("isTest");
		isTest.setValueAttribute("true");
		
		final HtmlInput button = form.getInputByName("answerButton");
		//System.out.println("Test clicked, workerId= "+ this.workerId);
		nextPage= button.click();

		if(nextPage.getTitleText().matches("Error Page")){
			final HtmlForm messageForm = nextPage.getFormByName("errorForm");
			final HtmlInput messageInput = messageForm.getInputByName("message");
			String message = messageInput.getValueAttribute();
			loggerConsent.info("Tests=FAILED; threadId= "+ this.myThread+"; Page="+nextPage.getTitleText()+ "; Message="+message);
			return null;
		}else
			if(nextPage.getTitleText().matches("Sorry Page")){
				final HtmlForm messageForm = nextPage.getFormByName("sorryForm");
				final HtmlInput messageInput = messageForm.getInputByName("message");
				String message = messageInput.getValueAttribute();
				loggerConsent.info("Tests=FAILED; threadId= "+ this.myThread+"; Page="+nextPage.getTitleText()+ "; Message="+message);
				return null;
			}
			else{// The question page shows up
				System.out.println("ENDED : "+nextPage.getTitleText());
				loggerConsent.info("Test=SUCCESS; workerId= "+ this.workerId+"; Page="+nextPage.getTitleText());
				Thread.sleep(50);
				return nextPage;
			}
	}

	@Test
	public HtmlPage runMicrotask(HtmlPage nextPage) throws Exception {
		System.out.println("STARTED : "+nextPage.getTitleText());
		//final HtmlPage page = this.webClient.getPage(path+"QuestionMicrotask.jsp");
		final HtmlForm answerForm = nextPage.getFormByName("answerForm");

		// Answer radio field
		final HtmlInput radioAnswer = answerForm.getInputByName("answer");
		radioAnswer.click();
		radioAnswer.setValueAttribute("3"); //Means a "Not at all"

		// Confidence radio field
		final HtmlInput radioConfidence = answerForm.getInputByName("confidence");
		radioConfidence.click();
		radioConfidence.setValueAttribute("5");
		
		// Explanation textarea
		final HtmlTextArea textAreaExplanation = answerForm.getTextAreaByName("explanation");
		textAreaExplanation.click();
		textAreaExplanation.setNodeValue("My explanation");

		// Difficulty radio
		final HtmlInput radioDifficulty = answerForm.getInputByName("difficulty");
		radioDifficulty.click();
		radioDifficulty.setValueAttribute("3");
		
		//So it calls the LoadTest version
		HtmlInput isTest = answerForm.getInputByName("isTest");
		isTest.setValueAttribute("true");
		
		// Form submit button
		final HtmlSubmitInput button = answerForm.getInputByName("answerButton");
		nextPage = button.click();
		
		System.out.println("ENDED : "+nextPage.getTitleText());
		
		if(nextPage.getTitleText().matches("Error Page")){
			final HtmlForm messageForm = nextPage.getFormByName("errorForm");
			final HtmlInput messageInput = messageForm.getInputByName("message");
			String message = messageInput.getValueAttribute();
			loggerSession.info("Microtask=FAILED; workerId= "+ this.workerId+"; Page="+nextPage.getTitleText()+ "; Message="+message);
			return null;
		}else
			if(nextPage.getTitleText().matches("Sorry Page")){
				final HtmlForm messageForm = nextPage.getFormByName("sorryForm");
				final HtmlInput messageInput = messageForm.getInputByName("message");
				String message = messageInput.getValueAttribute();
				loggerSession.info("Microtask=FAILED; threadId= "+ this.myThread+"; Page="+nextPage.getTitleText()+ "; Message="+message);
				return null;
			}
			else
				if(nextPage.getTitleText().matches("Feedback Page")){ // The feedback page shows up
					loggerConsent.info("Feedback Page");
					Thread.sleep(50);
					return nextPage;
				}
				else{ // There are more questions so continue on the MicroTaskPage
					final HtmlForm microtaskForm = nextPage.getFormByName("answerForm");
					final HtmlInput microtaskId = microtaskForm.getInputByName("microtaskId");
					loggerSession.info("Microtask=SUCCESS; workerId= "+ this.workerId+"; microtaskId="+microtaskId.getValueAttribute()+ "; Page="+nextPage.getTitleText());
					Thread.sleep(50);
					return nextPage;
				}
	}

	@Test
	public HtmlPage runSurvey(HtmlPage nextPage) throws Exception {
		System.out.println("STARTED : "+nextPage.getTitleText());
		//final HtmlPage page = this.webClient.getPage(path+"QuestionMicrotask.jsp");
		final HtmlForm surveyForm = nextPage.getFormByName("surveyForm");

		// Experience Radio
		final HtmlInput radioExperience = surveyForm.getInputByName("experience");
		radioExperience.click();
		radioExperience.setValueAttribute("Hobbyist");
		
		// Programming language field
		final HtmlInput textFieldLanguage = surveyForm.getInputByName("language");
		textFieldLanguage.click();
		textFieldLanguage.setValueAttribute("Java");
		
		// Programming language field
		final HtmlInput textFieldYears = surveyForm.getInputByName("years");
		textFieldYears.click();
		textFieldYears.setValueAttribute("2");
		
		// Learned checkbox
		final HtmlInput checkboxLearned = surveyForm.getInputByName("learned");
		checkboxLearned.click();
		checkboxLearned.setValueAttribute("University");
		
		// Gender radio
		final HtmlInput radioGender = surveyForm.getInputByName("gender");
		radioGender.click();
		radioGender.setValueAttribute("Female");

		// Age text field
		final HtmlInput age = surveyForm.getInputByName("age");
		age.setValueAttribute("21");

		// Country of residence text field
		final HtmlInput country = surveyForm.getInputByName("country");
		country.setValueAttribute("USA");

		// Submit form
		final HtmlSubmitInput button = surveyForm.getInputByName("surveySubmit");
		nextPage = button.click();
		
		System.out.println("ENDED : "+nextPage.getTitleText());
		
		if(nextPage.getTitleText().matches("Error Page")){
			final HtmlForm messageForm = nextPage.getFormByName("errorForm");
			final HtmlInput messageInput = messageForm.getInputByName("message");
			String message = messageInput.getValueAttribute();
			loggerConsent.info("Survey=FAIILED; workerId= "+ this.workerId+"; Page="+nextPage.getTitleText()+ "; Message="+message);
			return null;
		}else
			if(nextPage.getTitleText().matches("Sorry Page")){
				final HtmlForm messageForm = nextPage.getFormByName("sorryForm");
				final HtmlInput messageInput = messageForm.getInputByName("message");
				String message = messageInput.getValueAttribute();
				loggerConsent.info("Survey=FAILED; threadId= "+ this.myThread+"; Page="+nextPage.getTitleText()+ "; Message="+message);
				return null;
			}
			else{ // It comes to Skill Test page
				loggerConsent.info("Session=OPENED; workerId= "+ this.workerId+"; Page="+nextPage.getTitleText());
				Thread.sleep(50);
				return nextPage;
			}


	}
	
	public HtmlPage runFeedback(HtmlPage nextPage) throws IOException, InterruptedException
	{
		System.out.println("STARTED : "+nextPage.getTitleText());
		// Feedback form
		final HtmlForm feedBackForm = nextPage.getFormByName("feedback");
		
		// Feedback text area
		final HtmlTextArea textAreaFeedback = feedBackForm.getTextAreaByName("feedback");
		textAreaFeedback.click();
		textAreaFeedback.setNodeValue("No feedback at all");
		
		//Feedback submit button
		final HtmlInput submitButton = feedBackForm.getInputByName("feedbackSubmit");
		nextPage = submitButton.click();
		
		System.out.println("ENDED : "+nextPage.getTitleText());
		
		if(nextPage.getTitleText().matches("Error Page")){
			final HtmlForm messageForm = nextPage.getFormByName("errorForm");
			final HtmlInput messageInput = messageForm.getInputByName("message");
			String message = messageInput.getValueAttribute();
			loggerConsent.info("Survey=FAIILED; workerId= "+ this.workerId+"; Page="+nextPage.getTitleText()+ "; Message="+message);
			return null;
		}
		else
		{ // Thanks page shows up
			final HtmlForm reasonForm = nextPage.getFormByName("thanksForm");
			final HtmlInput sessionId = reasonForm.getInputByName("sessionId");
			final DomElement divKey = nextPage.getElementById("key");
			loggerConsent.info("Session=CLOSED; workerId= "+ this.workerId+"; sessionId="+sessionId.getValueAttribute() + "; MechanicalTurk=" +divKey.getNodeValue());
			Thread.sleep(50);
			return nextPage;
		}
		
	}

}