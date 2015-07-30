package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.uci.ics.sdcl.firefly.util.PropertyManager;

public class CSVExporter implements DescriptiveReportWriter {
	private final String reportPath;
	private String reportFileName = "firefly_report.csv";
	
	public CSVExporter() {
		PropertyManager property = PropertyManager.initializeSingleton();
		this.reportPath = property.reportPath;
	}
	
	@Override
	public void exportReport(DescriptiveReport report) {
		PrintWriter file = null;
		try {
			file = new PrintWriter(reportPath+reportFileName);
			Map<String, List<String>> table = report.getTable();
			List<String> keys = new ArrayList<String>(table.keySet());
			for (String subTitle : keys) {
				file.write(subTitle+";");
			}
			for (int i = 0; i < table.get(keys.get(0)).size(); i++) {
				file.write("\n");
				for (String key : keys) {
					String data = table.get(key).get(i);
					file.write((data == null)? ";" : (data+";"));
				}
			}
			System.out.println("\"" + reportFileName +" \" written successfully on disk.");
		} catch (FileNotFoundException e) {
			System.out.println("REPORT ERROR: Error trying to create a file.");
			System.exit(0);
		}
		finally
		{
			file.close();
		}
	}



	@Override
	public void setReportName(String reportName) {
		this.reportFileName = reportName + this.reportFileName;
	}

}
