package test;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.util.PropertyManager;

public class PropertyManagerTest {


	@Test
	public void testReportPath() {
		PropertyManager manager = PropertyManager.initializeSingleton();
		String reportPath = manager.reportPath;
		System.out.println("ReportPath = "+reportPath);
		String expectedPath = "C:/Users/Christian Adriano/Documents/GitHub/";
		assertTrue(reportPath.compareTo(expectedPath)==0);
	}

	@Test
	public void testSerializationPath() {
		PropertyManager manager = PropertyManager.initializeSingleton();	
		String serializationPath = manager.serializationPath;
		System.out.println("SerializationPath = "+serializationPath);
		String expectedPath = "C:/Users/Christian Adriano/Documents/GitHub/";
		assertTrue(serializationPath.compareTo(expectedPath)==0);
	}
	
	@Test
	public void testAnswersPerMicrotask() {
		PropertyManager manager = PropertyManager.initializeSingleton();	
		int answersPerMicrotask = manager.answersPerMicrotask;
		System.out.println("answersPerMicrotask = "+answersPerMicrotask);
		int expected = 10;
		assertTrue(answersPerMicrotask == expected);
	}
	
	@Test
	public void testMicrotasksPerSession() {
		PropertyManager manager = PropertyManager.initializeSingleton();
		int microtasksPerSession = manager.microtasksPerSession;
		System.out.println("answersPerMicrotask = "+microtasksPerSession);
		int expected = 10;
		assertTrue(microtasksPerSession == expected);
	}
	
}
