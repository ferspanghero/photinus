package edu.uci.ics.sdcl.firefly.report;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerStorage;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

public class ReportGenerator {
	
	public static void main(String[] args) {
		ReportGenerator reports = new ReportGenerator();
		System.out.println(reports.createMicrotasksReport());
		System.out.println(reports.createWorkersReport());
		System.out.println(reports.createAnswersReport());
	}
	
	public void runReports(){
		System.out.println(createMicrotasksReport());
		System.out.println(createWorkersReport());
		System.out.println(createAnswersReport());
	}
	
	public boolean createMicrotasksReport(){
		MicrotaskStorage microtaskStore = new MicrotaskStorage();
		HashMap<String, FileDebugSession> microtasks = microtaskStore.readAllDebugSessions();
		MicrotasksReportGenerator microtaskReport = new MicrotasksReportGenerator();
		return microtaskReport.writeToXlsx(microtasks);
	}

	public boolean createWorkersReport(){
		WorkerStorage workersStore = new WorkerStorage();
		HashMap<String, Worker> workers= workersStore.readAllWorkers();
		// for debug purposes:
		/* Set<Map.Entry<String, Worker>> setWorkers = workers.entrySet();
		Iterator<Entry<String, Worker>> iterateWorkers = setWorkers.iterator();
		while(iterateWorkers.hasNext())
		{
			Map.Entry<String, Worker> mapEntryWorker = (Map.Entry<String, Worker>)iterateWorkers.next();
			System.out.println(mapEntryWorker.getKey() + " " + mapEntryWorker.getValue().getHitId() + 
				" " + mapEntryWorker.getValue().getGradeMap() + "  " + mapEntryWorker.getValue().getSurveyAnswers());
		} */
		if (null != workers){
			WorkersReportGenerator generator = new WorkersReportGenerator();
			generator.writeToXlsx(workers);
			return true;
		} else
			return false;
	}
	
	public boolean createAnswersReport(){
		WorkerSessionStorage workerSessionStorage = WorkerSessionStorage.initializeSingleton();
		Vector<WorkerSession> closedSessions = workerSessionStorage.retrieveClosedSessionStorage();
		Hashtable<String, WorkerSession> activeSessions = workerSessionStorage.retrieveActiveSessionStorage();
		Stack<WorkerSession> newSessions = workerSessionStorage.retrieveNewSessionStorage();
		
		if (closedSessions!= null && activeSessions!=null && newSessions!=null){
			SessionsReportGenerator excelAnswersReport = new SessionsReportGenerator();
			return excelAnswersReport.writeToXlsx(closedSessions,activeSessions, newSessions);
		} else
			return false;
	}
}
