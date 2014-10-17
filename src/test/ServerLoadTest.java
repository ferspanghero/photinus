package test;


import java.io.IOException;
import java.net.MalformedURLException;

import net.sourceforge.jwebunit.junit.WebTester;
import net.sourceforge.jwebunit.util.TestingEngineRegistry;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;



public class ServerLoadTest{// implements Runnable{
	//Used for JWebUnit
	private static Logger logger;
	WebTester webTester;

	//Used for HTMLUnit
	private WebClient webClient;
	int workerId=100;

	@Before
	public void setupHTMLUnit(){
		//logger = LoggerFactory.getLogger(ServerLoadTest.class);
	}

	public ServerLoadTest(int workerId){
		this.workerId = workerId;
		this.webClient = new WebClient();
		WebClientOptions options = webClient.getOptions();
		options.setThrowExceptionOnFailingStatusCode(false);
		options.setThrowExceptionOnScriptError(false);
		options.setTimeout(1000);
		this.webClient.setJavaScriptTimeout(4500);
		options.setCssEnabled(false);
	}


	public static void main(String args[]){
		//for(int i=0;i<1;i++)
			//(new Thread(new ServerLoadTest(i))).start();
		ServerLoadTest test = new ServerLoadTest(0);
		try{
		test.runConsent();
		test.runSkillTest();
		test.runMicrotask();
		}
		catch(Exception e){
			System.err.println(e.toString());
		}
	}
	
	///@Override
	public void run(){
		//Generate Consents
	
		try{
			System.out.println("Thread "+workerId);
			runConsent();
			runSkillTest();
			runMicrotask();
		}
		catch(Exception e){
			System.err.println(e.toString());
		}
	}




	//@Test
	public void runConsent()throws Exception {

		//webClient.setThrowExceptionOnFailingStatusCode(false);
		//webClient.set.setThrowExceptionOnScriptError(false);
		final HtmlPage page = this.webClient.getPage("http://localhost:8080/firefly/ConsentForm.jsp");

		final HtmlForm form = page.getFormByName("consentForm");

		final HtmlButtonInput button = form.getInputByName("yesButton");
		final HtmlCheckBoxInput checkBox = form.getInputByName("consentBox");

		// Change the value of the text field
		checkBox.click();

		System.out.println("Consent clicked, workerId= "+ this.workerId);
		// Now submit the form by clicking the button and get back the second page.
		final HtmlPage page2 = button.click();

		System.out.println("Consent given; workerId= "+ this.workerId+ "; Page="+ page2.getTitleText());

		webClient.closeAllWindows();

	}

	public void runSkillTest() throws FailingHttpStatusCodeException, MalformedURLException, IOException
	{
		//webClient.setThrowExceptionOnFailingStatusCode(false);
		//webClient.set.setThrowExceptionOnScriptError(false);
		final HtmlPage page = this.webClient.getPage("http://localhost:8080/firefly/SkillTest.jsp");

		final HtmlForm form = page.getFormByName("testForm");

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
		input1.setValueAttribute(new Integer(this.workerId).toString());
		HtmlHiddenInput input2 = form.getInputByName("timeStamp");
		input2.setValueAttribute(TimeStampUtil.getTimeStampMillisec());
		HtmlHiddenInput input3 = form.getInputByName("subAction");
		input3.setValueAttribute("gradeAnswers");
		
		final HtmlInput button = form.getInputByName("answerButton");
		System.out.println("Test clicked, workerId= "+ this.workerId);
		final HtmlPage pageMicrotask= button.click();
		System.out.println("Test stored; workerId= "+ this.workerId+"; Page=="+pageMicrotask.getTitleText());
		webClient.closeAllWindows();
	}

	@Test
	public void runMicrotask() throws Exception {
		
		//Microtask Page
		final HtmlPage page = this.webClient.getPage("http://localhost:8080/firefly/QuestionMicrotask.jsp");
		final HtmlForm answerForm = page.getFormByName("answerForm");
		
		answerForm.setActionAttribute("microtask");
		HtmlHiddenInput input1 = answerForm.getInputByName("workerId");
		input1.setValueAttribute("workerId");
		HtmlHiddenInput input2 =answerForm.getInputByName("timeStamp");
		input2.setValueAttribute(TimeStampUtil.getTimeStampMillisec());
		
		final HtmlInput radio = answerForm.getInputByName("answer");
		radio.click();
		radio.setValueAttribute("5"); //Means a "No"
		
		final HtmlInput button = answerForm.getInputByName("submitButton");
		System.out.println("Microtask clicked, workerId= "+ this.workerId);
		final HtmlPage pageMicrotask= button.click();
		System.out.println("Microtask stored; workerId= "+ this.workerId+"; Page=="+pageMicrotask.getTitleText());
		webClient.closeAllWindows();

	}


	//----------------------------------------------------------------------------------------------------------
	//Old JWebUnit stuff

	public void prepare() {
		webTester = new WebTester();
		webTester.setBaseUrl("http://localhost:8080/firefly/");
		webTester.setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);    // use WebDriver
		logger = LoggerFactory.getLogger(ServerLoadTest.class);
	}


	public void testConsentForm() {
		//Consent Page
		logger.debug("Starting Load Test");
		System.out.println("Starting Load Test");
		webTester.setIgnoreFailingStatusCodes(true);

		webTester.beginAt("ConsentForm.jsp");
		webTester.assertFormElementPresent("consentForm");
		webTester.assertCheckboxPresent("consentBox");
		webTester.checkCheckbox("consentBox");
		logger.debug("Before click yesButton");
		webTester.setHiddenField("subAction", "loadQuestions");
		webTester.submit();//clickButton("yesButton");
		//webTester.submit();

		//Test Page
		logger.debug("Before assert Title");
		webTester.assertTitleEquals("Java Skill Test");
		webTester.beginAt("SkillTest.jsp");
		workerId++;
		webTester.setHiddenField("workerId", new Integer(workerId).toString());
		webTester.assertRadioOptionPresent("QUESTION1", "1");

		webTester.setHiddenField("workerId", new Integer(workerId).toString());
		logger.debug("after setHiddenField");

	}


}