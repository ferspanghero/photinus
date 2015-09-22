package edu.uci.ics.sdcl.firefly.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileSessionDTO;

/**
 * 
 * Utility methods to count answers, workers, elapsed time, and extract microtasks per file name
 * 
 * @author adrianoc
 *
 */
public class MicrotaskMapUtil {

	private static Answer getFirstAnswer(HashMap<String, Microtask> map){

		Answer currentAnswer=null;

		for(Microtask microtask:map.values()){
			Vector<Answer> answerList = microtask.getAnswerList();
			for(Answer answer:answerList){
				if(currentAnswer==null)
					currentAnswer=answer;
				else{
					Date answerDate = answer.getTimeStampDate(); 
					if(answerDate.compareTo(currentAnswer.getTimeStampDate())<0)
						currentAnswer = answer;
				}
			}	
		}
		return currentAnswer;
	}

	private static Answer getLastAnswer(HashMap<String,Microtask> map){

		Answer currentAnswer=null;

		for(Microtask microtask:map.values()){
			Vector<Answer> answerList = microtask.getAnswerList();
			for(Answer answer:answerList){
				if(currentAnswer==null)
					currentAnswer=answer;
				else{
					Date answerDate = answer.getTimeStampDate(); 
					if(answerDate.compareTo(currentAnswer.getTimeStampDate())>0)
						currentAnswer = answer;
				}
			}	
		}
		return currentAnswer;
	}

	private static double computeElapsedTime_Hours(Date startDate, Date endDate){
		double millisec = endDate.getTime() - startDate.getTime();
		return millisec /(3600 *1000);
	}

	
	//-------------------------------------------------------------------------------------------------------
	public static Integer countWorkers(
			HashMap<String, Microtask> filteredMicrotaskMap, String fileName) {

		HashMap<String,String> workerMap = new HashMap<String, String>();
		for(Microtask task: filteredMicrotaskMap.values()){
			if(fileName==null || task.getFileName().compareTo(fileName)==0){
				for(Answer answer:task.getAnswerList()){
					String workerID = answer.getWorkerId();
					workerMap.put(workerID, workerID);
				}
			}
		}
		return workerMap.size();
	}
	
	public static Double countAnswers(HashMap<String, Microtask> map){

		double count=0.0;
		for(Microtask microtask: map.values()){
			count = count + microtask.getNumberOfAnswers();
		}
		return count;
	}

	public static Double computeElapsedTimeForAnswerLevels(HashMap<String, Microtask> map){

			Answer firstAnswer = getFirstAnswer(map);
			Answer lastAnswer = getLastAnswer(map);
			return computeElapsedTime_Hours(firstAnswer.getTimeStampDate(),lastAnswer.getTimeStampDate());
	}
	
	public static HashMap<String, ArrayList<String>>  extractAnswersForFileName(
			HashMap<String, Microtask> microtaskMap,String fileName){

		int answerCount = 0;
		HashMap<String, ArrayList<String>> resultMap = new HashMap<String, ArrayList<String>>();

		for(Microtask task:microtaskMap.values() ){
			//System.out.println("fileName: "+fileName+":"+task.getFileName());
			if(task.getFileName().compareTo(fileName)==0){
				resultMap.put(task.getID().toString(),task.getAnswerOptions());
				answerCount = answerCount+task.getAnswerOptions().size();
			}
		}
		//System.out.println(fileName+" has "+answerCount+" answers");
		return resultMap;
	}
	//----------------------------------------------------------------------------------------------------------

	
}
