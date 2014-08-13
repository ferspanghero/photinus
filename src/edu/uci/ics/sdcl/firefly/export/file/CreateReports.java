package edu.uci.ics.sdcl.firefly.export.file;

import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerStorage;

public class CreateReports {

	public static void main(String[] args) {
		CreateReports reports = new CreateReports();
		System.out.println(reports.createMicrotasksReport("C:\\Users\\Danilo\\Documents\\GitHub\\crowd-debug-firefly\\samples\\main\\java\\com\\netflix\\nfgraph\\util\\OrdinalMap_buggy.java"));
		System.out.println(reports.createWorkersReport());
		System.out.println(reports.createAnswersReport());
	}
	
	public boolean createMicrotasksReport(String fileNameWithPathAndExtension){
		MicrotaskStorage microtaskStore = new MicrotaskStorage();
		FileDebugSession microtasks = microtaskStore.read(fileNameWithPathAndExtension);
		ExcelMicrotasksReport microtaskReport = new ExcelMicrotasksReport();
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
			System.out.println(mapEntryWorker.getKey() + " " + mapEntryWorker.getValue().getHitId() + " " + mapEntryWorker.getValue().getGradeMap()
					+ "  " + mapEntryWorker.getValue().getSurveyAnswers());
		} */
		if (null != workers){
			ExcelUsersScoreReport.writeToXlsx(workers);
			return true;
		} else
			return false;
	}
	
	public boolean createAnswersReport(){
		WorkerSessionStorage workerSessionStorage = new WorkerSessionStorage();
		HashMap<String, Object> storage = workerSessionStorage.readStorage();
		if (null != storage){
			ExcelAnswersReport excelAnswersReport = new ExcelAnswersReport();
			return excelAnswersReport.writeToXlsx(storage);
		} else
			return false;
	}
}
