package test;



import net.sourceforge.jwebunit.junit.WebTester;
import net.sourceforge.jwebunit.util.TestingEngineRegistry;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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



public class ServerLoadTest implements Runnable{
	//Used for JWebUnit
	private static Logger logger;
	WebTester webTester;

	//Used for HTMLUnit
	private WebClient webClient;
	private int workerId=0;
	private static int threadId=-1;
	private int defaultId;
	private int myThread;
	private static String path = "http://localhost:8080/crowd-debug-firefly/";

	private HtmlPage nextPage;
	
	@Before
	public void setupHTMLUnit(){
		//logger = LoggerFactory.getLogger(ServerLoadTest.class);
	}

	public ServerLoadTest(int myThread){
		this.myThread = myThread;
		this.defaultId = this.myThread+50; //in case there is no workerID returned
		this.webClient = new WebClient();
		WebClientOptions options = webClient.getOptions();
		options.setThrowExceptionOnFailingStatusCode(false);
		options.setThrowExceptionOnScriptError(false);
		options.setTimeout(10000);
		this.webClient.setJavaScriptTimeout(1000);
		options.setCssEnabled(false);
	}


	public static void main(String args[]){
		try{
			int maxThreads = 100;
			while(threadId<maxThreads){
				threadId++;
				//	System.out.println("Thread ="+threadId);
				(new Thread(new ServerLoadTest(threadId))).start();
			}
		}
		catch(Exception e){
			System.out.println("FAILED Thread ="+threadId);
		}
	}

	@Override
	public void run(){

		try{
			if(runConsent())
				if(runSkillTest()){
					boolean success=true;
					int i=0;
					while(success && i<10){
						success = runMicrotask();
						i++;
					}
					System.out.println("Thread ="+this.myThread+"; answered="+new Integer(i).toString()+" times.");
				}
			
			/*runConsent();
			runSkillTest();
			runMicrotask();
			*/
		}
		catch(Exception e){
			System.err.println(e.toString());
		}
	}




	//@Test
	public boolean runConsent()throws Exception {
		final HtmlPage page = this.webClient.getPage(path+"ConsentForm.jsp");

		final HtmlForm form = page.getFormByName("consentForm");
		final HtmlButtonInput button = form.getInputByName("yesButton");
		final HtmlCheckBoxInput checkBox = form.getInputByName("consentBox");

		// Change the value of the text field
		checkBox.click();

		//System.out.println("Consent clicked, thread="+ threadId);
		// Now submit the form by clicking the button and get back the second page.
		this.nextPage = button.click();

		//System.out.println("Consent , nextpage="+nextPage.getTitleText());
		if(nextPage.getTitleText().matches("Error Page")){
			final HtmlForm messageForm = nextPage.getFormByName("errorForm");
			final HtmlInput messageInput = messageForm.getInputByName("message");
			String message = messageInput.getValueAttribute();
			this.workerId = this.defaultId;
			System.out.println("Consent=FAILED; threadId= "+ this.myThread+ "; Page="+ nextPage.getTitleText() + "; message="+message);
			return false;
		}
		else{
			//obtain the workerId
			final HtmlForm testForm = nextPage.getFormByName("testForm");
			final HtmlHiddenInput workerIdInput = testForm.getInputByName("workerId");
			this.workerId = new Integer(workerIdInput.getValueAttribute()).intValue();
			//System.out.println("Consent=SUCCESS; workerId= "+ this.workerId+ "; Page="+ nextPage.getTitleText());
			//System.out.println("worker Id set correctly="+this.workerId);
			return true;
		}
	}


	public boolean runSkillTest() throws Exception	{
		//final HtmlPage page = this.webClient.getPage(path+"SkillTest.jsp");

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
		input1.setValueAttribute(new Integer(this.workerId).toString());
		HtmlHiddenInput input2 = form.getInputByName("timeStamp");
		input2.setValueAttribute(TimeStampUtil.getTimeStampMillisec());
		HtmlHiddenInput input3 = form.getInputByName("subAction");
		input3.setValueAttribute("gradeAnswers");

		final HtmlInput button = form.getInputByName("answerButton");
		//System.out.println("Test clicked, workerId= "+ this.workerId);
		this.nextPage= button.click();

		if(nextPage.getTitleText().matches("Error Page")){
			final HtmlForm messageForm = nextPage.getFormByName("errorForm");
			final HtmlInput messageInput = messageForm.getInputByName("message");
			String message = messageInput.getValueAttribute();
			System.out.println("Tests=FAIILED; threadId= "+ this.myThread+"; Page="+nextPage.getTitleText()+ "; Message="+message);
			return false;
		}else
			if(nextPage.getTitleText().matches("Sorry Page")){
				final HtmlForm messageForm = nextPage.getFormByName("sorryForm");
				final HtmlInput messageInput = messageForm.getInputByName("message");
				String message = messageInput.getValueAttribute();
				System.out.println("Tests=FAIILED; threadId= "+ this.myThread+"; Page="+nextPage.getTitleText()+ "; Message="+message);
				return false;
			}
			else{
				//System.out.println("Test=SUCCESS; workerId= "+ this.workerId+"; Page="+nextPage.getTitleText());
				return true;
			}


	}

	@Test
	public boolean runMicrotask() throws Exception {
		//final HtmlPage page = this.webClient.getPage(path+"QuestionMicrotask.jsp");
		final HtmlForm answerForm = nextPage.getFormByName("answerForm");

		//answerForm.setActionAttribute("microtask");
	//	HtmlHiddenInput input1 = answerForm.getInputByName("workerId");
		//input1.setValueAttribute("workerId");
	//	HtmlHiddenInput input2 =answerForm.getInputByName("timeStamp");
		//input2.setValueAttribute(TimeStampUtil.getTimeStampMillisec());

		final HtmlInput radio = answerForm.getInputByName("answer");
		radio.click();
		radio.setValueAttribute("5"); //Means a "No"

		final HtmlSubmitInput button = answerForm.getInputByName("submit");
		//System.out.println("Microtask clicked, workerId= "+ this.workerId);
		this.nextPage= button.click();


		if(nextPage.getTitleText().matches("Error Page")){
			final HtmlForm messageForm = nextPage.getFormByName("errorForm");
			final HtmlInput messageInput = messageForm.getInputByName("message");
			String message = messageInput.getValueAttribute();
			System.out.println("Microtask=FAIILED; workerId= "+ this.workerId+"; Page="+nextPage.getTitleText()+ "; Message="+message);
			return false;
		}else
			if(nextPage.getTitleText().matches("Sorry Page")){
				final HtmlForm messageForm = nextPage.getFormByName("sorryForm");
				final HtmlInput messageInput = messageForm.getInputByName("message");
				String message = messageInput.getValueAttribute();
				System.out.println("Microtask=FAILED; threadId= "+ this.myThread+"; Page="+nextPage.getTitleText()+ "; Message="+message);
				return false;
			}
			else{
				//System.out.println("Microtask=SUCCESS; workerId= "+ this.workerId+"; Page="+nextPage.getTitleText());
				return true;
			}
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