package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.report.LogReport;

public class LogReportTest {

	int numberOfExpectedMicrotasks;
	
	@Before
	public void setup(){
		this.numberOfExpectedMicrotasks=450;
	}
	
	///@Test
	public void test_prod1() {
		//String path = "C:\\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-1\\Oct25\\SnapShot12\\";
		String path = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-2October25\\";
		LogReport report = new LogReport(path);
		report.processLog();
	
		//assertEquals(2199,report.getConsents());
		assertEquals(253,report.getSurveys());
		assertEquals(1724,report.getSkillTests());
		assertEquals(254, report.getClosedSessions());
		assertEquals(500, report.getOpenedSessions());
		assertEquals(2927, report.getNumberOfMicrotasks());		
	}


	@Test
	public void test_prod2() {
		//String path = "C:\\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-1\\Oct25\\SnapShot12\\";
		String path = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-2October25\\Snapshot12\\";
		LogReport report = new LogReport(path);
		report.processLog();
	
		assertEquals(2002,report.getConsents());
		assertEquals(174,report.getSurveys());
		assertEquals(1519,report.getSkillTests());
		assertEquals(175, report.getClosedSessions());
		assertEquals(374, report.getOpenedSessions());
		assertEquals(2104, report.getNumberOfMicrotasks());			
	}
	
}
