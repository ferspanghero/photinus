package test;

import org.junit.Assert.*;
import net.sourceforge.jwebunit.junit.JWebUnit.*;

import org.junit.Before;
import org.junit.Test;

public class ServerLoadTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Before
	public void prepare() {
		setBaseUrl("http://localhost:8080/test");
	}

	@Test
	public void testLogin() {
		beginAt("/home");
		clickLink("login");
		assertTitleEquals("Login");
		setTextField("username", "test");
		setTextField("password", "test123");
		submit();
		assertTitleEquals("Welcome, test!");
	}
}