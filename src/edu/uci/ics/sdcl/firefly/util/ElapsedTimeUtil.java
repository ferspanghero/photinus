package edu.uci.ics.sdcl.firefly.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;

public class ElapsedTimeUtil {

	
	public static Double getElapseTime(HashMap<String,Microtask> microtaskMap){
		
		Vector<Answer> answerList = getListOfAnswers(microtaskMap);	
		//Collections.sort(answerList);
		
		//for(Answer answer: answerList){
			//Date timeStamp = answer.getTimeStampDate();
			//if(timeStamp==null){
				//System.out.println("Answer workerID:"+ answer.getWorkerId()+ ", timestamp:"+ answer.getTimeStamp());
			//}
		//}
		
		Answer firstAnswer = answerList.get(0);
		
		Date firstDate = firstAnswer.getTimeStampDate();
		
		Answer lastAnswer = answerList.lastElement();
		Date lastDate = lastAnswer.getTimeStampDate();
		
		System.out.println(firstAnswer.getTimeStampDate()+":"+lastAnswer.getTimeStampDate());
		
		long millisec = lastDate.getTime() - firstDate.getTime();
		double hours = millisec /(1000*3600);
		
		return hours;
	}
	
	private static Vector<Answer> getListOfAnswers(HashMap<String,Microtask> microtaskMap){
		
		Vector<Answer> answerList = new Vector<Answer>();
		
		Iterator<String> iter = microtaskMap.keySet().iterator();
		while(iter.hasNext()){
			Microtask task = microtaskMap.get(iter.next());
			Vector<Answer> taskAnswerlist = task.getAnswerList();
			for(Answer answer: taskAnswerlist){
				if(answer.getTimeStampDate()!=null)
					answerList.add(answer);
				else
					System.out.println("Answer workerID:"+ answer.getWorkerId()+ ", timestamp:"+ answer.getTimeStamp());
			}
		}
		return answerList;
	}
	
	
	private static void firstLastMicrotask(HashMap<String,Microtask> microtaskMap){	
		Microtask firstTask=null;
		Microtask lastTask=null;
		
		Iterator<String> iter = microtaskMap.keySet().iterator();
		if(iter.hasNext()){
			firstTask = microtaskMap.get(iter.next());
		}
		
		while(iter.hasNext()){
			lastTask = microtaskMap.get(iter.next());
		}
		
		String firstTimeStamp="";
		Vector<Answer> answerList = firstTask.getAnswerList();
		if(answerList!=null && answerList.size()>0)
		 firstTimeStamp = answerList.get(0).getTimeStamp();
		
		String lastTimeStamp="";
		answerList = lastTask.getAnswerList();
		if(answerList!=null && answerList.size()>0)
			lastTimeStamp = answerList.get(0).getTimeStamp();

		System.out.println("firstDate: "+ firstTimeStamp+", lastDate:"+lastTimeStamp+ 
				":"+computeDifferencesMinutes(firstTimeStamp, lastTimeStamp));
	}
	
	private static Double computeDifferencesMinutes(String firstTimeStamp,
			String lastTimeStamp) {

		//Remove last 5 characters
		firstTimeStamp = firstTimeStamp.substring(0, firstTimeStamp.length()-5);
		System.out.println(firstTimeStamp);
		
		lastTimeStamp = lastTimeStamp.substring(0, lastTimeStamp.length()-5);
		System.out.println(lastTimeStamp);
		
		DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z", Locale.ENGLISH);
		try {
			Date dateFirst = format.parse(firstTimeStamp);
			Date dateSecond = format.parse(lastTimeStamp);
			
			long millisec = dateSecond.getTime() - dateFirst.getTime();
			double hours = millisec /(1000*3600);
			
			return hours;
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	public static void testResult(){
		
		String dateFirstStr = "Tue 2015 Jul 07 08:18:44.521";
		String dateSecondStr = "Tue 2015 Jul 07 08:18:44.525";
		
		DateFormat format = new SimpleDateFormat("EEE yyyy MMM dd HH:mm:ss.S", Locale.ENGLISH);
		try {
			Date dateFirst = format.parse(dateFirstStr);
			Date dateSecond = format.parse(dateSecondStr);
			
			long millisec = dateSecond.getTime() - dateFirst.getTime();
			double hours = millisec /(1000*3600);
			
			System.out.println("millisec: " +millisec);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		testResult();
	}
	
}
