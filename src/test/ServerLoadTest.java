package test;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import net.sourceforge.jwebunit.junit.JWebUnit;
import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.Before;
import org.junit.Test;

public class ServerLoadTest {

	WebTester webTester;
	
	@Before
	public void prepare() {
		webTester = new WebTester();
		webTester.setBaseUrl("http://localhost:8080/firefly/");
	}

	@Test
	public void testConsentForm() {
		//Consent Page

		webTester.beginAt("ConsentForm.jsp");
		webTester.assertFormElementPresent("consentForm");
		webTester.assertCheckboxPresent("consentBox");
		webTester.checkCheckbox("consentBox");
		webTester.clickButton("yesButton");
		
		//Test Page
		webTester.assertTitleEquals("Java Skill Test");

		/*setTextField("username", "test");
		setTextField("password", "test123");
		submit();
		assertTitleEquals("Welcome, test!");
		*/
	}
}