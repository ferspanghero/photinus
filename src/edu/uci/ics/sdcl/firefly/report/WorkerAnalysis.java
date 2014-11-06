package edu.uci.ics.sdcl.firefly.report;

import java.awt.Point;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.report.LogAnalysis.Counter;

public class WorkerAnalysis {
	
	private NumberFormat formatter = new DecimalFormat("#0.00"); 

	LogData data;
	
	public HashMap<String,Counter> counterMap;
	
	public WorkerAnalysis(LogData data){
		this.data = data;
		counterMap = new HashMap<String,Counter>(); 
	}
	
	
	//Compute the answers for 1 worker, 2 workers, 3 workers, ....
	//Ignore workers who haven't answered any question
	private void computeResultsPerWorkerCluster(){
		int validWorkerCount=0;
		int totalWorkerCount=0;

		HashMap<String, Microtask> microtaskMap = new HashMap<String, Microtask>();
		
		//Iterate over workers, check if they have answer one task at least
		for(Worker worker: data.workerList){
			int microtaskCount=0;
			totalWorkerCount++;
			String workerId = worker.getWorkerId();
			if(data.checkWorkerDidMinimalOneTask(worker.getWorkerId())){
				validWorkerCount++;
				String sessionId = data.workerMap.get(workerId).getSessionId();
				WorkerSession session = data.sessionMap.get(sessionId);
				Vector<Microtask> microtaskList = session.getMicrotaskList();
				for(Microtask microtask: microtaskList){
					Answer answer = microtask.getAnswerByUserId(workerId);
					
					Microtask existing = microtaskMap.get(microtask.getID().toString());
					if(existing==null){
						existing = microtask.getSimpleVersion(); //create a new copy of the task, so we don't mixed with the old one.
					}
					else{
						existing.addAnswer(answer);					
					}

					microtaskMap.put(microtask.getID().toString(), existing);
					microtaskCount++;
				}
				//ready to send to compute TP, TN, FP, FN.
				LogAnalysis logAnalysis = new LogAnalysis(this.data);
				System.out.print(" active workers|"+validWorkerCount+"| total workers|"+totalWorkerCount+"| microtasks|"+microtaskCount+"|");
				//logAnalysis.answersPerMethod(true, data.yesMap, microtaskMap);
				logAnalysis.expectedYesAnswers(new Point(0,214),"1buggy_ApacheCamel.txt", microtaskMap, data.yesMap);
				logAnalysis.expectedNoAnswers(new Point(0,214),"1buggy_ApacheCamel.txt", microtaskMap, data.yesMap);
				
				HashMap<String,Result> resultMap = logAnalysis.bugReportResultMap;
				Result result = resultMap.get("Total");
				result.printNumbers();
			}
		}
	}
	
	private static WorkerAnalysis initializeLogs(){
		
		//String path = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-2October25\\Snapshot12\\";
		
		LogData data = new LogData(true, 0);
		
		String path = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-1\\Oct25\\SnapShot12\\";
		data.processLogProduction1(path);
		
		path = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\Production-2October25\\1Final\\";
		data.processLogProduction2(path);
		

		
		System.out.println("Logs loaded! Totals are:");
		System.out.println("Consents: "+data.getConsents());
		System.out.println("SkillTests: "+data.getSkillTests());
		System.out.println("Surveys: "+data.getSurveys());
		System.out.println("Sessions Opened: "+data.getOpenedSessions());
		System.out.println("Sessions Closed: "+data.getClosedSessions());
		System.out.println("Answers in Map: "+data.getNumberOfMicrotasks());
		System.out.println("Microtasks in List: "+data.microtaskList.size());
		
		WorkerAnalysis analysis = new WorkerAnalysis(data);
		return analysis;
	}
	
		
	public static void main(String[] args){
		
		WorkerAnalysis analysis = initializeLogs();
		
		analysis.computeResultsPerWorkerCluster();
	}
	
	
}
