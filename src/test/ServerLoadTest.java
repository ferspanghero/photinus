package test;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Date;

import net.sourceforge.jwebunit.junit.JWebUnit;
import net.sourceforge.jwebunit.junit.WebTester;
import net.sourceforge.jwebunit.util.TestingEngineRegistry;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
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

import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;
import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;



public class ServerLoadTest {
	private static Logger logger;
	WebTester webTester;
	int workerId=100;
	
	@Before
	public void setupHTMLUnit(){
		//logger = LoggerFactory.getLogger(ServerLoadTest.class);
	}

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

		/*setTextField("username", "test");
		setTextField("password", "test123");
		submit();
		assertTitleEquals("Welcome, test!");
		*/
	}
	
	
	//@Test
	public void runConsents()throws Exception {
		WebClient webClient = new WebClient();
		WebClientOptions options = webClient.getOptions();
		options.setThrowExceptionOnFailingStatusCode(false);
		options.setThrowExceptionOnScriptError(false);
		options.setTimeout(10000);
		//webClient.setThrowExceptionOnFailingStatusCode(false);
		//webClient.set.setThrowExceptionOnScriptError(false);
	    final HtmlPage page = webClient.getPage("http://localhost:8080/firefly/ConsentForm.jsp");
	    
	    final HtmlForm form = page.getFormByName("consentForm");

	    final HtmlButtonInput button = form.getInputByName("yesButton");
	    final HtmlCheckBoxInput checkBox = form.getInputByName("consentBox");

	    // Change the value of the text field
	    checkBox.click();

	    // Now submit the form by clicking the button and get back the second page.
	    final HtmlPage page2 = button.click();

	    System.out.println("page 2 = "+ page2.getTitleText());
	    
	    webClient.closeAllWindows();
	    
	}
	
	@Test
	public void testSkillTestForm() throws Exception{
		//Generate Consents
		try{
		for(int i=0;i<500;i++){
			runConsents();
		}
		
		//Answer Tests
		for(int i=0;i<500;i++){
			runSkillTests(i);
		}
		}
		catch(Exception e){
			System.err.println(e.toString());
		}
	}
			
		
		
	public void runSkillTests(int workerId) throws FailingHttpStatusCodeException, MalformedURLException, IOException
		{
		WebClient webClient = new WebClient();//BrowserVersion.CHROME);
		WebClientOptions options = webClient.getOptions();
		options.setThrowExceptionOnFailingStatusCode(false);
		options.setThrowExceptionOnScriptError(false);
		options.setTimeout(10000);
		webClient.setJavaScriptTimeout(45000);
		//options.setJavaScriptEnabled(false);
		options.setCssEnabled(false);
		//webClient.setThrowExceptionOnFailingStatusCode(false);
		//webClient.set.setThrowExceptionOnScriptError(false);
	    final HtmlPage page = webClient.getPage("http://localhost:8080/firefly/SkillTest.jsp");
	    
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
	    input1.setValueAttribute("workerId");
	    HtmlHiddenInput input2 = form.getInputByName("timeStamp");
	    input2.setValueAttribute(TimeStampUtil.getTimeStampMillisec());
	    HtmlHiddenInput input3 = form.getInputByName("subAction");
	    input3.setValueAttribute("gradeAnswers");
	    		
	    		
	    final HtmlInput button = form.getInputByName("answerButton");
	    button.click();
		
	}
	
	public void testMicrotaskForm() {
		//Microtask Page

		//First Microtask
		webTester.beginAt("QuestionMicrotask.jsp");
	//	webTester.setHiddenField(inputName, value);
		webTester.assertFormElementPresent("consentForm");
		webTester.assertCheckboxPresent("consentBox");
	}
	
	
	
}