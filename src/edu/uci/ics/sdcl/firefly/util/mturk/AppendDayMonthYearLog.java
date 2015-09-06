package edu.uci.ics.sdcl.firefly.util.mturk;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Add the day, month, year to each log line (currently there was only hour,min, sec, millsec.
 * 
 * @author adrianoc
 *
 */
public class AppendDayMonthYearLog {

	String day1 = "Tue 2015 Jul 07";
	String day2 = "Wed 2015 Jul 08";
	String day3 = "Thu 2015 Jul 09";
	String day4 = "Fri 2015 Jul 10";
	String day5 = "Sat 2015 Jul 11";
	String day6 = "Sun 2015 Jul 12";
	String day7 = "Mon 2015 Jul 13";

	String[] dayList = {day1, day2, day3, day4, day5, day6, day7};

	String sessionLogFileName_dateStamp = "session-log_consolidated_dateStamp.txt";
	String consentLogFileName_dateStamp = "consent-log_consolidated_dateStamp.txt";

	String sessionLogFileName_Cut = "session-log_consolidated_cut.txt";
	String consentLogFileName_Cut = "consent-log_consolidated_cut.txt";

	ArrayList<String> sessionLog_original_buffer = new ArrayList<String>();
	ArrayList<String> consentLog_original_buffer = new ArrayList<String>();
	
	LogReadWriter logReadWriter;
	
	public void run(){
		 this.logReadWriter = new LogReadWriter();
		sessionLog_original_buffer = logReadWriter.readToBuffer(5, sessionLogFileName_Cut);
		consentLog_original_buffer = logReadWriter.readToBuffer(5, consentLogFileName_Cut);
		
		ArrayList<String>  dayEntries = this.buildDayEntries(sessionLog_original_buffer);
		this.writeEntries(dayEntries, sessionLogFileName_dateStamp);
		dayEntries = this.buildDayEntries(consentLog_original_buffer);
		this.writeEntries(dayEntries, consentLogFileName_dateStamp);
	}

	public ArrayList<String> buildDayEntries(ArrayList<String> buffer){

		ArrayList<String> dayEntries =  new ArrayList<String>();
		
		for(String day: dayList){

			ArrayList<String> lineList = new ArrayList<String>();
			Integer hour=null;
			Integer midnight = 0;
			int i=0;
			while(hour<midnight && buffer.size()>i){
				String line = buffer.get(i);
				if(line!=null && line.length()>0)
					try{
						hour = new Integer(line.substring(0,1));  
						if(hour<midnight){
							dayEntries.add(day+" "+line);
						}
					}
				catch(Exception e){
					hour = 1;
				}
				i++;
			}
		}
		return dayEntries;
	}

	public void writeEntries(ArrayList<String> dayEntries, String fileDestination){
		this.logReadWriter.writeBackToBuffer(dayEntries, 5, fileDestination);
		
	}

}
