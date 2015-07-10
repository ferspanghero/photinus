package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This holds the complete descriptive report table,
 * whit all parts assembled on it. 
 * 
 * @author igMoreira
 *
 */
public class DescriptiveReport {

	
	/**
	 * Holds all the contents of the report.
	 */
	private Map<String, List<String>> table = new LinkedHashMap<String, List<String>>();
	private DescriptiveReportWriter exporter;
	
	public DescriptiveReport(Map<String, List<String>> content, DescriptiveReportWriter exporter) {
		this.table = content;
		this.exporter = exporter;
	}
	
	public Map<String, List<String>> getTable() {
		return table;
	}
	
	/**
	 * Calls the ReportWrite so the content
	 * of this report will be written in an
	 * external source, an excel sheet for instance
	 */
	public void exportReport()
	{
		exporter.exportReport(this);
	}

	
	
}
