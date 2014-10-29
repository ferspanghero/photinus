package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.report.LogData;

public class LogReportTest {

	int numberOfExpectedMicrotasks;
	
	@Before
	public void setup(){
		this.numberOfExpectedMicrotasks=450;
	}
	
	@Test
	public void test_prod2() {
		//String path = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-1\\Oct25\\SnapShot12\\";
		String path = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-2October25\\SnapShot15\\";
		LogData report = new LogData();
		report.processLogProduction2(path);
	
		assertEquals(2498,report.getConsents());
		assertEquals(219,report.getSurveys());
		assertEquals(1914,report.getSkillTests());
		assertEquals(221, report.getClosedSessions());
		assertEquals(471, report.getOpenedSessions());
		assertEquals(2650, report.getNumberOfMicrotasks());		
	}


	//@Test
	public void test_prod1() {
		String path = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-1\\Oct25\\SnapShot12\\";
		//String path = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-2October25\\Snapshot12\\";
		LogData report = new LogData();
		report.processLogProduction1(path);
	
		
		assertEquals(2199,report.getConsents());
		assertEquals(252,report.getSurveys());
		assertEquals(1724,report.getSkillTests());
		assertEquals(254, report.getClosedSessions());
		assertEquals(500, report.getOpenedSessions());
		assertEquals(2927, report.getNumberOfMicrotasks());
		
	}
	
}
