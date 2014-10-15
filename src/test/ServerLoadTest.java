package test;

import static org.junit.Assert.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import net.sourceforge.jwebunit.junit.JWebUnit;

import org.junit.Before;
import org.junit.Test;

public class ServerLoadTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Before
	public void prepare() {
		setBaseUrl("http://localhost:8080/crowd-debug-firefly/");
	}

	@Test
	public void testConsentForm() {
		//Consent Page
		beginAt("ConsentForm.jsp");
		JWebUnit.setLabeledFormElementField("consentBox", "1");
		clickButton("Yes, I want to participate");
		
		//Test Page
		assertTitleEquals("Java Skill Test");

		/*setTextField("username", "test");
		setTextField("password", "test123");
		submit();
		assertTitleEquals("Welcome, test!");
		*/
	}
}