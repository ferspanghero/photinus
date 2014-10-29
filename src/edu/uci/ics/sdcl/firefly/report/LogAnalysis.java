package edu.uci.ics.sdcl.firefly.report;

import java.awt.Point;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;

public class LogAnalysis {

	private NumberFormat formatter = new DecimalFormat("#0.00"); 

	LogData data;
	
	public LogAnalysis(LogData data){
		this.data = data;
	}
	


	public void expectedYesAnswers(Point point){
		double counterYes=0;
		double counterAnswers=0;
		double counterNos=0;
		double counterICantTell=0;
		Iterator<String> iter = data.workingMicrotaskMap.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			Microtask task = data.workingMicrotaskMap.get(key);
			int id = task.getID().intValue();
			if(data.yesMap.containsKey(task.getID().toString())&& id>=point.x && id<=point.y){
				Vector<Answer> answerList = task.getAnswerList();
				for(Answer answer: answerList){
					if(answer.getOption().trim().matches(Answer.YES) || answer.getOption().trim().matches(Answer.PROBABLY_YES))
						counterYes++;
					else
						if(answer.getOption().trim().matches(Answer.NO) || answer.getOption().trim().matches(Answer.PROBABLY_NOT))
							counterNos++;
						else
							if(answer.getOption().matches(Answer.I_CANT_TELL))
								counterICantTell++;
				}
			}

		}

		System.out.println(" *** BUG REVEALING QUESTIONS - EXPECTED YES/Prob YES ANSWERS: ");
		counterAnswers = counterYes+counterNos+counterICantTell;
		System.out.println("Total Bug Revealing Answers="+counterAnswers);
		System.out.println("counterYes="+counterYes+ " = "+ formatter.format(counterYes/(counterYes+counterNos)*100)+ "%");
		System.out.println("counterNos="+counterNos + " = "+ formatter.format(counterNos/(counterYes+counterNos)*100)+ "%");
		System.out.println("counterICantTell="+counterICantTell + " = "+ formatter.format(counterICantTell/(counterAnswers)*100)+ "%");
		
		System.out.println("-------------------------------------------");
		
	}

	/**
	 * Removed Method Body questions from the set.
	 */
	public void expectedYesAnswersLessMethodBody(){
		double counterYes=0;
		double counterAnswers=0;
		double counterNos=0;
		double counterICantTell=0;
		Iterator<String> iter = data.workingMicrotaskMap.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			Microtask task = data.workingMicrotaskMap.get(key);
			if(data.yesNotBodyFunctionMap.containsKey(task.getID().toString())){
				Vector<Answer> answerList = task.getAnswerList();
				for(Answer answer: answerList){
					if(answer.getOption().trim().matches(Answer.YES) || answer.getOption().trim().matches(Answer.PROBABLY_YES))
						counterYes++;
					else
						if(answer.getOption().trim().matches(Answer.NO) || answer.getOption().trim().matches(Answer.PROBABLY_NOT))
							counterNos++;
						else
							if(answer.getOption().matches(Answer.I_CANT_TELL))
								counterICantTell++;
				}
			}

		}

		System.out.println(" *** BUG REVEALING QUESTIONS BUT METHOD BODY QUESTIONS - EXPECTED YES/Prob YES ANSWERS: ");
		counterAnswers = counterYes+counterNos+counterICantTell;
		System.out.println("Total Bug Revealing Answers="+counterAnswers);
		System.out.println("counterYes="+counterYes+ " = "+ formatter.format(counterYes/(counterYes+counterNos)*100)+ "%");
		System.out.println("counterNos="+counterNos + " = "+ formatter.format(counterNos/(counterYes+counterNos)*100)+ "%");
		System.out.println("counterICantTell="+counterICantTell + " = "+ formatter.format(counterICantTell/(counterAnswers)*100)+ "%");
		
		System.out.println("-------------------------------------------");
	

	}
	
	public void expectedNoAnswers(Point point){
			double counterYes=0;
			double counterAnswers=0;
			double counterNos=0;
			double counterICantTell=0;
			Iterator<String> iter = data.workingMicrotaskMap.keySet().iterator();
			while(iter.hasNext()){
				String key = iter.next();
				Microtask task = data.workingMicrotaskMap.get(key);
				int id = task.getID().intValue();
				if(!data.yesMap.containsKey(task.getID().toString())&& id>=point.x && id<=point.y){
					Vector<Answer> answerList = task.getAnswerList();
					for(Answer answer: answerList){
						if(answer.getOption().trim().matches(Answer.YES) || answer.getOption().trim().matches(Answer.PROBABLY_YES))
							counterYes++;
						else
							if(answer.getOption().trim().matches(Answer.NO) || answer.getOption().trim().matches(Answer.PROBABLY_NOT))
								counterNos++;
							else
								if(answer.getOption().trim().matches(Answer.I_CANT_TELL))
									counterICantTell++;
					}
				}
			}

			System.out.println(" *** NO-BUG REVEALING QUESTIONS - EXPECTED No/Prob No ANSWERS: ");
			counterAnswers = counterYes+counterNos+counterICantTell;
			System.out.println("Total NO-BUG Revealing Answers="+counterAnswers);
			System.out.println("counterYes="+counterYes+ " = "+ formatter.format(counterYes/(counterNos+counterYes)*100)+ "%");
			System.out.println("counterNos="+counterNos + " = "+ formatter.format(counterNos/(counterYes+counterNos)*100)+ "%");
			System.out.println("counterICantTell="+counterICantTell + " = "+ formatter.format(counterICantTell/(counterAnswers)*100)+ "%");
			
			System.out.println("-------------------------------------------");
			
		

		}
	
	public static void main(String[] args){
		String path = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-2October25\\Snapshot15\\";
		//String path = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-2October25\\Snapshot12\\";
		LogData data = new LogData();
		data.processLogProduction2(path);
		
		path = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-1\\Oct25\\SnapShot12\\";
		//data.processLogProduction1(path);
		
		LogAnalysis analysis = new LogAnalysis(data);
		
		System.out.println("----------------------------------------------------------------");
		System.out.println("--- CONSOLIDATED BATCHES ---------------------------------------");
		System.out.println("Consents: "+data.getConsents());
		System.out.println("SkillTests: "+data.getSkillTests());
		System.out.println("Surveys: "+data.getSurveys());
		System.out.println("Sessions Opened: "+data.getOpenedSessions());
		System.out.println("Sessions Closed: "+data.getClosedSessions());
		System.out.println("Answers: "+data.getNumberOfMicrotasks());
		
		//Configure the MicrotaskMap
		data.workingMicrotaskMap = data.microtaskMap;
		
		//General
		analysis.expectedYesAnswers(new Point(0,214));
		analysis.expectedNoAnswers(new Point(0,214));
		//analysis.expectedYesAnswersLessMethodBody();
		
		
		//Configure the MicrotaskMap
		data.workingMicrotaskMap = data.microtaskMap;
		
		System.out.println("----------------------------------------------------------------");
		System.out.println("--- ANALYSIS PER FILE -----------------------------------------");
		System.out.println();
		
		System.out.println("---- EXPECTED YES ----");
		Iterator<String> iter = data.fileNameTaskRange.keySet().iterator();
		while(iter.hasNext()){
			String fileName = iter.next();
			System.out.println("BUG REPORT: "+ fileName);
			Point point = data.fileNameTaskRange.get(fileName);
			analysis.expectedYesAnswers(point);
		}
	
		
		
		System.out.println();
		
		System.out.println("---- EXPECTED NO ----");
		iter = data.fileNameTaskRange.keySet().iterator();
		while(iter.hasNext()){
			String fileName = iter.next();
			System.out.println("BUG REPORT: "+ fileName);
			Point point = data.fileNameTaskRange.get(fileName);
			analysis.expectedNoAnswers(point);
		}
	
		System.out.println();
	//Configure the MicrotaskMap
	data.computeMicrotaskFromCompleteSessions();
	data.workingMicrotaskMap = data.completeSessions_microtaskMap;
	
	System.out.println("---- EXPECTED YES >>FILTER: ONLY COMPLETE SESSIONS ----");
	iter = data.fileNameTaskRange.keySet().iterator();
	while(iter.hasNext()){
		String fileName = iter.next();
		System.out.println("BUG REPORT: "+ fileName);
		Point point = data.fileNameTaskRange.get(fileName);
		analysis.expectedYesAnswers(point);
	}
	
	System.out.println();
	
	System.out.println("---- EXPECTED NO >>FILTER: ONLY COMPLETE SESSIONS ----");
	iter = data.fileNameTaskRange.keySet().iterator();
	while(iter.hasNext()){
		String fileName = iter.next();
		System.out.println("BUG REPORT: "+ fileName);
		Point point = data.fileNameTaskRange.get(fileName);
		analysis.expectedNoAnswers(point);
	}
	
	
	}
	
}
