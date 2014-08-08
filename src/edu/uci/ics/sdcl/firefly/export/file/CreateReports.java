package edu.uci.ics.sdcl.firefly.export.file;

import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;

public class CreateReports {

	public static void main(String[] args) {
		System.out.println(createMicrotasksReport("C:\\Users\\Danilo\\Documents\\GitHub\\crowd-debug-firefly\\samples\\main\\java\\com\\netflix\\nfgraph\\util\\OrdinalMap_buggy.java"));
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

}
