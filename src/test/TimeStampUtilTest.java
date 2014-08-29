package test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.util.TimeStampUtil;

public class TimeStampUtilTest {

	private String initialTime;
	private String timeSpan;

	@Before
	public void setup() throws Exception {
		
		int startYear = 2014;
		int startMonth = 4;
		int startDayOfMonth = 11;
		int startHour = 15;
		int startMinutes = 34;
		int startSeconds = 10;
		
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.set(Calendar.YEAR, startYear);
		startCalendar.set(Calendar.YEAR, startMonth);
		startCalendar.set(Calendar.YEAR, startDayOfMonth);
		startCalendar.set(Calendar.YEAR, startHour);
		startCalendar.set(Calendar.YEAR, startMinutes);
		startCalendar.set(Calendar.YEAR, startSeconds);
		
		int endYear = 2014;
		int endMonth = 4;
		int endDayOfMonth = 11;
		int endHour = 15;
		int endMinutes = 44;
		int endSeconds = 20;
		
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(Calendar.YEAR, endYear);
		endCalendar.set(Calendar.YEAR, endMonth);
		endCalendar.set(Calendar.YEAR, endDayOfMonth);
		endCalendar.set(Calendar.YEAR, endHour);
		endCalendar.set(Calendar.YEAR, endMinutes);
		endCalendar.set(Calendar.YEAR, endSeconds);
		
		int spanYear = 0;
		int spanMonth = 0;
		int spanDayOfMonth = 0;
		int spanHour = 0;
		int spanMinutes = 10;
		int spanSeconds = 10;
		
		this.initialTime = new String(startYear+":"+startMonth+":"+startDayOfMonth+":"+startHour+":"+startMinutes+":"+startSeconds);
		
		this.timeSpan =  new String(spanYear+":"+spanMonth+":"+spanDayOfMonth+":"+spanHour+":"+spanMinutes+":"+spanSeconds);
	}

	@Test
	public void test() {
		
	/*	Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.set(Calendar.YEAR, endYear);
		nowCalendar.set(Calendar.YEAR, endMonth);
		nowCalendar.set(Calendar.YEAR, endDayOfMonth);
		nowCalendar.set(Calendar.YEAR, endHour);
		nowCalendar.set(Calendar.YEAR, endMinutes);
		nowCalendar.set(Calendar.YEAR, endSeconds);
		
		int endYear = 2014;
		int endMonth = 4;
		int endDayOfMonth = 11;
		int endHour = 15;
		int endMinutes = 44;
		int endSeconds = 20;
		
		Calendar
		
	
		
		//TimeStampUtil.computeTimeSpan(this.initialTime);
	
		
		
		Calendar initial = Calendar.getInstance();
		long initialTime = initial.getTimeInMillis();
		
		String initialTimeStr = new Double(initialTime).toString();
		
		long finalTime = 
		
		*/
	}

}
