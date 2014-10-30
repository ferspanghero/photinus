package edu.uci.ics.sdcl.firefly.report;

import java.awt.Point;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;

public class LogAnalysis {

	private NumberFormat formatter = new DecimalFormat("#0.00"); 

	LogData data;
	
	public HashMap<String,Counter> counterMap;
	
	public LogAnalysis(LogData data){
		this.data = data;
		counterMap = new HashMap<String,Counter>(); 
	}
	


	public void expectedYesAnswers(Point point, String fileName, HashMap<String, Microtask>microtaskMap, HashMap<String,String> yesMap){
		double counterYes=0;
		double counterAnswers=0;
		double counterNos=0;
		double counterICantTell=0;
		Iterator<String> iter = microtaskMap.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			Microtask task = microtaskMap.get(key);
		
			int id = task.getID().intValue();
			if(yesMap.containsKey(task.getID().toString())&& id>=point.x && id<=point.y){
				Vector<Answer> answerList = task.getAnswerList();
				
				for(Answer answer: answerList){
					//if(fileName.trim().matches("1buggy_ApacheCamel.txt") && id==1)
						//System.out.println("####### counting TOTALs taskId:"+id+" to 1buggy_ApacheCamel.txt!" + " answer="+answer.getOption());
					if(answer.getOption().trim().matches(Answer.YES) || answer.getOption().trim().matches(Answer.PROBABLY_YES)){
						
						counterYes++;
					}
					else
						if(answer.getOption().trim().matches(Answer.NO) || answer.getOption().trim().matches(Answer.PROBABLY_NOT)){

							counterNos++;
						}
						else
							if(answer.getOption().matches(Answer.I_CANT_TELL)){
							
								counterICantTell++;
							}
								
				}
			}

		}

		//System.out.println(" *** BUG REVEALING QUESTIONS - EXPECTED YES/Prob YES ANSWERS: ");
		counterAnswers = counterYes+counterNos+counterICantTell;
		System.out.println(fileName+ " Bug Revealing Answers="+counterAnswers);
		System.out.println("counterYes="+counterYes+ " = "+ formatter.format(counterYes/(counterYes+counterNos)*100)+ "%");
		System.out.println("counterNos="+counterNos + " = "+ formatter.format(counterNos/(counterYes+counterNos)*100)+ "%");
		System.out.println("counterICantTell="+counterICantTell + " = "+ formatter.format(counterICantTell/(counterAnswers)*100)+ "%");
	
		this.counterMap.put(fileName,new Counter(counterYes,counterNos, counterICantTell, fileName,counterAnswers));

		
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

		//System.out.println(" *** BUG REVEALING QUESTIONS BUT METHOD BODY QUESTIONS - EXPECTED YES/Prob YES ANSWERS: ");
		counterAnswers = counterYes+counterNos+counterICantTell;
		System.out.println("Total Bug Revealing Answers="+counterAnswers);
		System.out.println("counterYes="+counterYes+ " = "+ formatter.format(counterYes/(counterYes+counterNos)*100)+ "%");
		System.out.println("counterNos="+counterNos + " = "+ formatter.format(counterNos/(counterYes+counterNos)*100)+ "%");
		System.out.println("counterICantTell="+counterICantTell + " = "+ formatter.format(counterICantTell/(counterAnswers)*100)+ "%");
			
		System.out.println("-------------------------------------------");
	

	}
	
	public void expectedNoAnswers(Point point,String fileName, HashMap<String, Microtask>microtaskMap, HashMap<String,String> yesMap){
			double counterYes=0;
			double counterAnswers=0;
			double counterNos=0;
			double counterICantTell=0;
			Iterator<String> iter = microtaskMap.keySet().iterator();
			while(iter.hasNext()){
				String key = iter.next();
				Microtask task = microtaskMap.get(key);
				int id = task.getID().intValue();
				if(!yesMap.containsKey(task.getID().toString())&& id>=point.x && id<=point.y){
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

			//System.out.println(" *** NO-BUG REVEALING QUESTIONS - EXPECTED No/Prob No ANSWERS: ");
			counterAnswers = counterYes+counterNos+counterICantTell;
			System.out.println(fileName+" NO-BUG Expected Answers="+counterAnswers);
			System.out.println("counterYes="+counterYes+ " = "+ formatter.format(counterYes/(counterNos+counterYes)*100)+ "%");
			System.out.println("counterNos="+counterNos + " = "+ formatter.format(counterNos/(counterYes+counterNos)*100)+ "%");
			System.out.println("counterICantTell="+counterICantTell + " = "+ formatter.format(counterICantTell/(counterAnswers)*100)+ "%");
			
			this.counterMap.put(fileName,new Counter(counterYes,counterNos, counterICantTell, fileName,counterAnswers));
			
			System.out.println("-------------------------------------------");
			
		

		}
	
	public class Counter{
		public Counter(double counterYes, double counterNos, double counterICantTell, String fileName,
				double counterAnswers) {
			super();
			this.yes = counterYes;
			this.no = counterNos;
			this.iCantTell = counterICantTell;
			this.fileName = fileName;
			this.totalAnswers = counterAnswers;
		}
		public double yes;
		public double no;
		public double iCantTell;
		public String fileName;
		public double totalAnswers;
	}		
	
	public static void main(String[] args){
		String path = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-2October25\\1Final\\";
		//String path = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-2October25\\Snapshot12\\";
		LogData data = new LogData();
		data.processLogProduction2(path);
		
		path = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-1\\Oct25\\SnapShot12\\";
		data.processLogProduction1(path);
		
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
		
		//General
		analysis.expectedYesAnswers(new Point(0,214),"Total", data.microtaskMap, data.yesMap);
		analysis.expectedNoAnswers(new Point(0,214),"Total", data.microtaskMap, data.yesMap);
		//analysis.expectedYesAnswersLessMethodBody();
		
		
		//Configure the MicrotaskMap
		Iterator<String> iter;
		System.out.println("----------------------------------------------------------------");
		System.out.println("--- ANALYSIS PER FILE -----------------------------------------");
		System.out.println();
		
		System.out.println("---- EXPECTED YES ----");
		iter = data.fileNameTaskRange.keySet().iterator();
		while(iter.hasNext()){
			String fileName = iter.next();
			System.out.print("BUG REPORT: "+ fileName+", ");
			Point point = data.fileNameTaskRange.get(fileName);
			analysis.expectedYesAnswers(point,fileName,data.microtaskMap, data.yesMap);
		}
		
		System.out.println();
		
		System.out.println("---- EXPECTED NO ----");
		iter = data.fileNameTaskRange.keySet().iterator();
		while(iter.hasNext()){
			String fileName = iter.next();
			System.out.print("BUG REPORT: "+ fileName+", ");
			Point point = data.fileNameTaskRange.get(fileName);
			analysis.expectedNoAnswers(point,fileName,data.microtaskMap, data.yesMap);
		}
	
		System.out.println();
	//
	//Configure the MicrotaskMap
		
	data.computeMicrotaskFromCompleteSessions();

	data.workingMicrotaskMap = data.completeSessions_microtaskMap;
	
	System.out.println("---- EXPECTED YES >>FILTER: ONLY COMPLETE SESSIONS ----");
	iter = data.fileNameTaskRange.keySet().iterator();
	while(iter.hasNext()){
		String fileName = iter.next();
		System.out.print("BUG REPORT: "+ fileName+", ");
		Point point = data.fileNameTaskRange.get(fileName);
		analysis.expectedYesAnswers(point,fileName,data.microtaskMap, data.yesMap);
	}
	
	System.out.println();
	
	System.out.println("---- EXPECTED NO >>FILTER: ONLY COMPLETE SESSIONS ----");
	iter = data.fileNameTaskRange.keySet().iterator();
	while(iter.hasNext()){
		String fileName = iter.next();
		System.out.print("BUG REPORT: "+ fileName+", ");
		Point point = data.fileNameTaskRange.get(fileName);
		analysis.expectedNoAnswers(point,fileName,data.microtaskMap, data.yesMap);
	}
	
	
	}
	
}
