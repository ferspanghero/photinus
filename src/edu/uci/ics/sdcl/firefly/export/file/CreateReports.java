package edu.uci.ics.sdcl.firefly.export.file;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;
import edu.uci.ics.sdcl.firefly.storage.WorkerStorage;

public class CreateReports {

	public static void main(String[] args) {
		System.out.println(createMicrotasksReport("C:\\Users\\Danilo\\Documents\\GitHub\\crowd-debug-firefly\\samples\\main\\java\\com\\netflix\\nfgraph\\util\\OrdinalMap_buggy.java"));
		System.out.println(createWorkersReport());
	}
	
	public static boolean createMicrotasksReport(String fileNameWithPathAndExtension){
		MicrotaskStorage microtaskStore = new MicrotaskStorage();
		FileDebugSession microtasks = microtaskStore.read(fileNameWithPathAndExtension);
		if (null != microtasks){
			ExcelMicrotasksReport.writeToXlsx(microtasks);
			return true;
		} else
			return false;
	}

	public static boolean createWorkersReport(){
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
}
