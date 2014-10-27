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
	public void test_2927() {
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
	public void test_1962() {
		//String path = "C:\\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-1\\Oct25\\SnapShot12\\";
		String path = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-2October25\\Snapshot10\\";
		LogReport report = new LogReport(path);
		report.processLog();
	
	
		assertEquals(1839,report.getConsents());
		//assertEquals(165,report.getSurveys());
		assertEquals(1391,report.getSkillTests());
		assertEquals(161, report.getClosedSessions());
		assertEquals(355, report.getOpenedSessions());
		//assertEquals(1962, report.getNumberOfMicrotasks());	
		
	}
	
}
