package test;



import java.io.IOException;

import net.sourceforge.jwebunit.junit.WebTester;
import net.sourceforge.jwebunit.util.TestingEngineRegistry;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebResponse;
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
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDivElement;

import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;



public class ServerLoadTest implements Runnable{
	//Used for JWebUnit
	private static Logger loggerInfo;
	private static Logger loggerConsent;
	private static Logger loggerSession;
	WebTester webTester;

	//Used for HTMLUnit
	private WebClient webClient;
	private String workerId="0";
	private static int threadId=-1;
	private int defaultId;
	private int myThread;
	private static String localPath = "http://localhost:8080/Photinus/";
	private static String serverPath = "http://dellserver.ics.uci.edu:8080/crowd-debug/";
	private static String path;
	private static String filename = "7_TimePeriodValues";

	private HtmlPage nextPage;

	@Before
	public void setupHTMLUnit(){
		//logger = LoggerFactory.getLogger(ServerLoadTest.class);
	}

	public ServerLoadTest(int myThread){
		loggerInfo = LoggerFactory.getLogger("info");
		loggerConsent = LoggerFactory.getLogger("consent");
		loggerSession = LoggerFactory.getLogger("session");
		this.myThread = myThread;
		this.defaultId = this.myThread+50; //in case there is no workerID returned
		this.webClient = new WebClient();
		WebClientOptions options = webClient.getOptions();
		options.setThrowExceptionOnFailingStatusCode(false);
		options.setThrowExceptionOnScriptError(false);
		options.setTimeout(14000);
		this.webClient.setJavaScriptTimeout(14000);
		options.setCssEnabled(false);
	}


	public static void main(String args[]){
		try{
			path = localPath;
			int maxThreads =1;
			while(threadId<maxThreads){
				threadId++;
				//	System.out.println("Thread ="+threadId);
				(new Thread(new ServerLoadTest(threadId))).start();
			}
		}
		catch(Exception e){
			loggerInfo.error("FAILED Thread ="+threadId);
		}
	}

	@Override
	public void run(){

		try{
			CookieManager manager = new CookieManager();
			manager.clearCookies();
			if(runConsent())
				if(runSurvey()){
					if(runSkillTest())
					{
						boolean success=true;
						int i=0;
						while(success && i<1){
							success = runMicrotask();
							i++;
						}
						loggerSession.info("Thread ="+this.myThread+"; answered="+new Integer(i).toString()+" times.");
						if(success)
							runFeedback();
					}
				}
		}
		catch(Exception e){
			loggerInfo.error(e.toString());
		}
	}




	//@Test
	public boolean runConsent()throws Exception {
		
		final HtmlPage page = this.webClient.getPage(path+"ConsentForm.jsp"+"?"+filename);
		System.out.println("STARTED : "+page.getTitleText());

		final HtmlForm form = page.getFormByName("consentForm");
		final HtmlButtonInput button = form.getInputByName("yesButton");
		final HtmlCheckBoxInput checkBox = form.getInputByName("consentBox");

		// Change the value of the text field
		checkBox.click();

		// Now submit the form by clicking the button and get back the second page.
		this.nextPage = button.click();

		//System.out.println("Consent , nextpage="+nextPage.getTitleText());
		if(nextPage.getTitleText().matches("Error Page")){
			final HtmlForm messageForm = nextPage.getFormByName("errorForm");
			final HtmlInput messageInput = messageForm.getInputByName("message");
			String message = messageInput.getValueAttribute();
			this.workerId = Integer.toString(this.defaultId);
			loggerConsent.info("Consent=FAILED; threadId= "+ this.myThread+ "; Page="+ nextPage.getTitleText() + "; message="+message);
			return false;
		}
		else{// The survey page shows up
			//obtain the workerId
			System.out.println("ENDED : "+nextPage.getTitleText());
			final HtmlForm surveyForm = nextPage.getFormByName("surveyForm");
			final HtmlHiddenInput workerIdInput = surveyForm.getInputByName("workerId");
			this.workerId = workerIdInput.getValueAttribute();
			loggerConsent.info("Consent=SUCCESS; workerId= "+ this.workerId+ "; Page="+ nextPage.getTitleText());
			return true;
		}
	}


	public boolean runSkillTest() throws Exception	{
		//final HtmlPage page = this.webClient.getPage(path+"SkillTest.jsp");
		System.out.println("STARTED : "+nextPage.getTitleText());
		final HtmlForm form = this.nextPage.getFormByName("testForm");

		final HtmlInput radio1 = form.getInputByName("QUESTION1");
		radio1.click();
		radio1.setValueAttribute("c");
		final HtmlRadioButtonInput radio2 = form.getInputByName("QUESTION2");
		radio2.click();
		radio2.setValueAttribute("a");
		final HtmlRadioButtonInput radio3 = form.getInputByName("QUESTION3");
		radio3.click();
		radio3.setValueAttribute("d");
		final HtmlRadioButtonInput radio4 = form.getInputByName("QUESTION4");
		radio4.click();
		radio4.setValueAttribute("b");

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
		this.nextPage= button.click();

		if(nextPage.getTitleText().matches("Error Page")){
			final HtmlForm messageForm = nextPage.getFormByName("errorForm");
			final HtmlInput messageInput = messageForm.getInputByName("message");
			String message = messageInput.getValueAttribute();
			loggerConsent.info("Tests=FAILED; threadId= "+ this.myThread+"; Page="+nextPage.getTitleText()+ "; Message="+message);
			return false;
		}else
			if(nextPage.getTitleText().matches("Sorry Page")){
				final HtmlForm messageForm = nextPage.getFormByName("sorryForm");
				final HtmlInput messageInput = messageForm.getInputByName("message");
				String message = messageInput.getValueAttribute();
				loggerConsent.info("Tests=FAILED; threadId= "+ this.myThread+"; Page="+nextPage.getTitleText()+ "; Message="+message);
				return false;
			}
			else{// The question page shows up
				System.out.println("ENDED : "+nextPage.getTitleText());
				loggerConsent.info("Test=SUCCESS; workerId= "+ this.workerId+"; Page="+nextPage.getTitleText());
				return true;
			}
	}

	@Test
	public boolean runMicrotask() throws Exception {
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
		
		// Form submit button
		final HtmlSubmitInput button = answerForm.getInputByName("answerButton");
		nextPage = button.click();
		
		System.out.println("ENDED : "+nextPage.getTitleText());
		
		if(nextPage.getTitleText().matches("Error Page")){
			final HtmlForm messageForm = nextPage.getFormByName("errorForm");
			final HtmlInput messageInput = messageForm.getInputByName("message");
			String message = messageInput.getValueAttribute();
			loggerSession.info("Microtask=FAIILED; workerId= "+ this.workerId+"; Page="+nextPage.getTitleText()+ "; Message="+message);
			return false;
		}else
			if(nextPage.getTitleText().matches("Sorry Page")){
				final HtmlForm messageForm = nextPage.getFormByName("sorryForm");
				final HtmlInput messageInput = messageForm.getInputByName("message");
				String message = messageInput.getValueAttribute();
				loggerSession.info("Microtask=FAILED; threadId= "+ this.myThread+"; Page="+nextPage.getTitleText()+ "; Message="+message);
				return false;
			}
			else
				if(nextPage.getTitleText().matches("Feedback Page")){ // The feedback page shows up
					loggerConsent.info("Feedback Page");
					return true;
				}
				else{ // There are more questions so continue on the MicroTaskPage
					final HtmlForm microtaskForm = nextPage.getFormByName("answerForm");
					final HtmlInput microtaskId = microtaskForm.getInputByName("microtaskId");
					loggerSession.info("Microtask=SUCCESS; workerId= "+ this.workerId+"; microtaskId="+microtaskId.getValueAttribute()+ "; Page="+nextPage.getTitleText());
					return true;
				}
	}

	@Test
	public boolean runSurvey() throws Exception {
		System.out.println("STARTED : "+nextPage.getTitleText());
		//final HtmlPage page = this.webClient.getPage(path+"QuestionMicrotask.jsp");
		final HtmlForm surveyForm = nextPage.getFormByName("surveyForm");

		// Experience Radio
		final HtmlInput radioExperience = surveyForm.getInputByName("experience");
		radioExperience.click();
		radioExperience.setValueAttribute("Undergraduate student");
		
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
		age.setValueAttribute("1");

		// Country of residence text field
		final HtmlInput country = surveyForm.getInputByName("country");
		country.setValueAttribute("1");

		// Submit form
		final HtmlSubmitInput button = surveyForm.getInputByName("surveySubmit");
		this.nextPage= button.click();
		
		System.out.println("ENDED : "+nextPage.getTitleText());
		
		if(nextPage.getTitleText().matches("Error Page")){
			final HtmlForm messageForm = nextPage.getFormByName("errorForm");
			final HtmlInput messageInput = messageForm.getInputByName("message");
			String message = messageInput.getValueAttribute();
			loggerConsent.info("Survey=FAIILED; workerId= "+ this.workerId+"; Page="+nextPage.getTitleText()+ "; Message="+message);
			return false;
		}else
			if(nextPage.getTitleText().matches("Sorry Page")){
				final HtmlForm messageForm = nextPage.getFormByName("sorryForm");
				final HtmlInput messageInput = messageForm.getInputByName("message");
				String message = messageInput.getValueAttribute();
				loggerConsent.info("Survey=FAILED; threadId= "+ this.myThread+"; Page="+nextPage.getTitleText()+ "; Message="+message);
				return false;
			}
			else{ // It comes to Skill Test page
				loggerConsent.info("Session=OPENED; workerId= "+ this.workerId+"; Page="+nextPage.getTitleText());
				return true;
			}


	}
	
	public boolean runFeedback() throws IOException
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
			return false;
		}
		else
		{ // Thanks page shows up
			final HtmlForm reasonForm = nextPage.getFormByName("thanksForm");
			final HtmlInput sessionId = reasonForm.getInputByName("sessionId");
			final DomElement divKey = nextPage.getElementById("key");
			loggerConsent.info("Session=CLOSED; workerId= "+ this.workerId+"; sessionId="+sessionId.getValueAttribute() + "; MechanicalTurk=" +divKey.getNodeValue());
			return true;
		}
		
	}

	//----------------------------------------------------------------------------------------------------------
	//Old JWebUnit stuff

	public void prepare() {
		webTester = new WebTester();
		webTester.setBaseUrl("http://localhost:8080/firefly/");
		webTester.setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);    // use WebDriver
		loggerInfo = LoggerFactory.getLogger(ServerLoadTest.class);
	}


	public void testConsentForm() {
		//Consent Page
		loggerInfo.debug("Starting Load Test");
		System.out.println("Starting Load Test");
		webTester.setIgnoreFailingStatusCodes(true);

		webTester.beginAt("ConsentForm.jsp");
		webTester.assertFormElementPresent("consentForm");
		webTester.assertCheckboxPresent("consentBox");
		webTester.checkCheckbox("consentBox");
		loggerInfo.debug("Before click yesButton");
		webTester.setHiddenField("subAction", "loadQuestions");
		webTester.submit();//clickButton("yesButton");
		//webTester.submit();

		//Test Page
		loggerInfo.debug("Before assert Title");
		webTester.assertTitleEquals("Java Skill Test");
		webTester.beginAt("SkillTest.jsp");
//		workerId++;
		webTester.setHiddenField("workerId", new Integer(workerId).toString());
		webTester.assertRadioOptionPresent("QUESTION1", "1");

		webTester.setHiddenField("workerId", new Integer(workerId).toString());
		loggerInfo.debug("after setHiddenField");

	}


}