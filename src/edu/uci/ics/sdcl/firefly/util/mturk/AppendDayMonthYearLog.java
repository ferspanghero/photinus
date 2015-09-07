package edu.uci.ics.sdcl.firefly.util.mturk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

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
	
	String midnightDateStr = "59:59:59.999";
	String startDateStr = "00:00:00.000";
	Date midnightDate;
	Date startDate;
	DateFormat format = new SimpleDateFormat("HH:mm:ss.S", Locale.ENGLISH);

	String[] dayList = {day1, day2, day3, day4, day5, day6, day7};

	String sessionLogFileName_dateStamp = "session-log_consolidated_daystamp.txt";
	String consentLogFileName_dateStamp = "consent-log_consolidated_daystamp.txt";

	String sessionLogFileName_missingWorker = "session-log_consolidated_missingWorker.txt";
	String consentLogFileName_missingWorker = "consent-log_consolidated_missingWorker.txt";

	ArrayList<String> sessionLog_original_buffer = new ArrayList<String>();
	ArrayList<String> consentLog_original_buffer = new ArrayList<String>();

	LogReadWriter logReadWriter;

	public AppendDayMonthYearLog(){

		try {
			this.midnightDate =  format.parse(midnightDateStr);
			this.startDate =  format.parse(startDateStr);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


	public void run(){

		this.logReadWriter = new LogReadWriter();
		sessionLog_original_buffer = logReadWriter.readToBuffer(6, sessionLogFileName_missingWorker);
		consentLog_original_buffer = logReadWriter.readToBuffer(6, consentLogFileName_missingWorker);

		ArrayList<String>  dayEntries = this.buildDayEntries(sessionLog_original_buffer);
		this.writeEntries(dayEntries, sessionLogFileName_dateStamp);
		dayEntries = this.buildDayEntries(consentLog_original_buffer);
		this.writeEntries(dayEntries, consentLogFileName_dateStamp);
	}

	public ArrayList<String> buildDayEntries(ArrayList<String> buffer){

		ArrayList<String> dayEntries =  new ArrayList<String>();

		int i=0;
		Date previousDate = startDate;
		Date currentDate = startDate;
		int dayIndex=0;
		String day=day1;	

		while(buffer.size()>i && dayIndex<dayList.length){

			String line = buffer.get(i);
			if(line!=null && line.length()>0){
				String dateStr = line.substring(0,11);

				try{
					previousDate= currentDate;
					currentDate = format.parse(dateStr);

					if(currentDate!=null){
						//System.out.print(format.format(currentDate));
						if(currentDate.compareTo(previousDate)>=0){
							//System.out.println("> "+format.format(previousDate));
							dayEntries.add(day+" "+line);
						}
						else{
							System.out.print(format.format(currentDate));
							System.out.println("SMALLER THAN "+format.format(previousDate)+", day:"+dayIndex);
							dayIndex++;
							if(dayIndex<dayList.length){
								day = dayList[dayIndex];
								dayEntries.add(day+" "+line);
							}
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			else{
				dayEntries.add(line);
			}
			i++;
		}
		return dayEntries;
	}


	public void writeEntries(ArrayList<String> dayEntries, String fileDestination){
		this.logReadWriter.writeBackToBuffer(dayEntries, 6, fileDestination);
	}

	public static void main(String args[]){
		AppendDayMonthYearLog appender = new AppendDayMonthYearLog();
		appender.run();
	}

}
