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
import java.util.Iterator;
import java.util.Vector;

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

		String path = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\RawDataLogs\\";
		data.processLogProduction1(path);

		//String path = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\RawDataLogs\\";
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


	private ArrayList<String> writeMicrotaskAnswers_ZeroOnes(){

		ArrayList<String> contentList = new ArrayList<String>();

		//System.out.println("Size of list: "+ data.microtaskList.size());

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
		//System.out.println("contentList size="+ contentList.size());
		return contentList;
	}

	private ArrayList<String> writeMicrotaskAnswers_Labels(){

		ArrayList<String> contentList = new ArrayList<String>();

		//System.out.println("Size of list: "+ data.microtaskList.size());

		for(Microtask microtask: data.microtaskList){
			String yes="0";
			String no="0";
			String ICantTell="0";

			StringBuffer buffer = new StringBuffer();
			buffer.append(microtask.getID().toString());
			buffer.append("|");
			Answer answer = microtask.getAnswerList().get(0);			
			buffer.append(answer.getOption());

			contentList.add(buffer.toString());

		}
		System.out.println("contentList size="+ contentList.size());
		return contentList;
	}	


	private ArrayList<String> writeAnswersInTime_Labels(){
		ArrayList<String> contentList = new ArrayList<String>();

		System.out.println("Size of list: "+ data.microtaskMap.size());

		Iterator<String> iter=data.microtaskMap.keySet().iterator();

		while(iter.hasNext()){
			StringBuffer buffer = new StringBuffer();//new line
			String id = iter.next();
			Microtask task = data.microtaskMap.get(id);
			buffer.append(task.getID().toString());
			buffer.append("|");

			Vector<Answer> answerList = task.getAnswerList();
			for(int i=0;i<answerList.size();i++){
				Answer answer = answerList.get(i);
				buffer.append(answer.getOption());
				if((i+1)<answerList.size()) //only appends if it is not the last position
					buffer.append("|");
			}
			contentList.add(buffer.toString());
		}

		System.out.println("contentList size="+ contentList.size());
		return contentList;

	}

	private ArrayList<String> writeAnswersInTime_Duration(){
		ArrayList<String> contentList = new ArrayList<String>();

		System.out.println("Size of list: "+ data.microtaskMap.size());

		Iterator<String> iter=data.microtaskMap.keySet().iterator();

		while(iter.hasNext()){
			StringBuffer buffer = new StringBuffer();//new line
			String id = iter.next();
			Microtask task = data.microtaskMap.get(id);
			buffer.append(task.getID().toString());
			buffer.append("|");

			Vector<Answer> answerList = task.getAnswerList();
			for(int i=0;i<answerList.size();i++){
				Answer answer = answerList.get(i);
				buffer.append(answer.getElapsedTime());
				if((i+1)<answerList.size()) //only appends if it is not the last position
					buffer.append("|");
			}
			contentList.add(buffer.toString());
		}

		System.out.println("contentList size="+ contentList.size());
		return contentList;

	}

	private ArrayList<String> writeAnswersFilteredWorker_ICantTell(Integer numberOfICanTell){
		ArrayList<String> contentList = new ArrayList<String>();
		System.out.println("Size of list: "+ data.microtaskMap.size());

		Iterator<String> iter=data.microtaskMap.keySet().iterator();

		while(iter.hasNext()){
			boolean bufferHasAtLeastOneAnswer=false;
			StringBuffer buffer = new StringBuffer();//new line
			String id = iter.next();
			Microtask task = data.microtaskMap.get(id);
			buffer.append(task.getID().toString());
			buffer.append("|");

			Vector<Answer> answerList = task.getAnswerList();
			for(int i=0;i<answerList.size();i++){
				Answer answer = answerList.get(i);
				String workerId = answer.getWorkerId();
				Integer count = data.workerICantTellMap.get(workerId);
				if(count.intValue()<numberOfICanTell){
					bufferHasAtLeastOneAnswer=true;
					buffer.append(answer.getElapsedTime());
					if((i+1)<answerList.size()) //only appends if it is not the last position
						buffer.append("|");
				}
				else
					System.out.println("Worker "+ workerId+" discarded, I Cannot Tell count= "+count);
			}
			//Check if buffer has at least one answer, otherwise, ignore it
			if(bufferHasAtLeastOneAnswer)
				contentList.add(buffer.toString());
		}
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

		String path = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\DataAnalysis\\BaseDataInTime\\";

		csvData.printToFile(path+"all.txt", csvData.writeMicrotaskAnswers_ZeroOnes());

		csvData.printToFile(path+"allAnswer.txt", csvData.writeMicrotaskAnswers_Labels());

		csvData.printToFile(path+"allAnswersInTime.txt", csvData.writeAnswersInTime_Labels());

		csvData.printToFile(path+"allAnswersInTime_Duration", csvData.writeAnswersInTime_Duration());

		System.out.println("files written, look at: "+path);
	}


}
