package edu.uci.ics.sdcl.firefly;

import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class TimeStampUtil {


	/**
	 * @return a string representation of current time 
	 */
	public static String getTimeStamp(){
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		int seconds = cal.get(Calendar.SECOND);
		
		return (new String(year+":"+month+":"+dayOfMonth+":"+hourOfDay+":"+minutes+":"+seconds));
	}

	/** 
	 * Computes the time span between now and the provided timestamp
	 * @param initialTimeStamp
	 * @return
	 */
	public static String computeTimeSpan(String initialTimeStamp){
		Date currentTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentTime);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		int seconds = cal.get(Calendar.SECOND);
		
		//Obtain the data from the initialTimeStam string
		StringTokenizer tokenizer = new StringTokenizer(initialTimeStamp);
		Integer yearInit = new Integer(tokenizer.nextToken(":"));
		Integer monthInit = new Integer(tokenizer.nextToken(":"));
		Integer dayOfMonthInit = new Integer(tokenizer.nextToken(":"));
		Integer hourOfDayInit = new Integer(tokenizer.nextToken(":"));
		Integer minutesInit = new Integer(tokenizer.nextToken(":"));
		Integer secondsInit = new Integer(tokenizer.nextToken(":"));
		
		int yearSpan = year - yearInit;
		int monthSpan = month - monthInit;
		int dayOfMonthSpan = dayOfMonth - dayOfMonthInit;
		int hourOfDaySpan = hourOfDay - hourOfDayInit;
		int minutesSpan = minutes - minutesInit;
		int secondsSpan = seconds - secondsInit;
		
		return (new String(yearSpan+":"+monthSpan+":"+dayOfMonthSpan+":"+hourOfDaySpan+":"+minutesSpan+":"+secondsSpan));
	}
	
}
