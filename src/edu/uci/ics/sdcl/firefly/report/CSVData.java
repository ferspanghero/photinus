package edu.uci.ics.sdcl.firefly.report;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.report.LogAnalysis.Counter;

/** 
 * Generates the data in a  CSV
 * @author adrianoc
 *
 */
public class CSVData {

	private NumberFormat formatter = new DecimalFormat("#0.00"); 

	LogData data;
	
	public HashMap<String,Counter> counterMap;
	public HashMap<String, Result> bugReportResultMap;
	
	public CSVData(LogData data){
		this.data = data;
		counterMap = new HashMap<String,Counter>(); 
		bugReportResultMap = new HashMap<String, Result>();
	}
	
	private static CSVData initializeLogs(){
		LogData data = new LogData(false, 0);
		
		String path = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-1\\Oct25\\SnapShot12\\";
		data.processLogProduction1(path);
		
		path = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-2October25\\1Final\\";
		//String path = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-2October25\\Snapshot12\\";
		data.processLogProduction2(path);
		
		System.out.println("Logs loaded! Totals are:");
		System.out.println("Consents: "+data.getConsents());
		System.out.println("SkillTests: "+data.getSkillTests());
		System.out.println("Surveys: "+data.getSurveys());
		System.out.println("Sessions Opened: "+data.getOpenedSessions());
		System.out.println("Sessions Closed: "+data.getClosedSessions());
		System.out.println("Answers in Map: "+data.getNumberOfMicrotasks());
		System.out.println("Microtasks in List: "+data.microtaskList.size());
		
		CSVData csvData = new CSVData(data);
		return csvData;
	}
	
	
	private ArrayList<String> writeMicrotaskAnswers(){
		
		ArrayList<String> contentList = new ArrayList<String>();
		
		System.out.println("Size of list: "+ data.microtaskList.size());
		
		for(Microtask microtask: data.microtaskList){
			String yes="0";
			String no="0";
			String ICantTell="0";
			
			StringBuffer buffer = new StringBuffer();
			buffer.append(microtask.getID().toString());
			buffer.append("|");
			Answer answer = microtask.getAnswerList().get(0);

			if(answer.getOption().trim().matches(Answer.YES) || answer.getOption().trim().matches(Answer.PROBABLY_YES))
				yes="1";
			else
				if(answer.getOption().trim().matches(Answer.NO) || answer.getOption().trim().matches(Answer.PROBABLY_NOT))
					no="1";
				else
					if(answer.getOption().matches(Answer.I_CANT_TELL))
						ICantTell="1";
					else
						System.out.println("Microtask:"+microtask.getID().toString()+"Answer with no option:"+answer.getOption());
			
			buffer.append(yes);
			buffer.append("|");
			buffer.append(no);
			buffer.append("|");
			buffer.append(ICantTell);
						
			contentList.add(buffer.toString());
			
		}
		System.out.println("contentList size="+ contentList.size());
		return contentList;
	}
	
	private void printToFile(String fileNamePath, ArrayList<String> contentList){
		try{
			File file = new File(fileNamePath);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for(String content: contentList){
				writer.write(content);
				writer.newLine();
			}
			writer.close();
		}
		catch(Exception e){
			System.err.println(e.toString());
		}
	}
	
		
	public static void main(String[] args){
		
		CSVData csvData = initializeLogs();
		String fileNamePath = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\DataAnalysis\\allAnswers.txt";

		csvData.printToFile(fileNamePath, csvData.writeMicrotaskAnswers());
		System.out.println("file written, look at: "+fileNamePath);
	}
	
	
}
