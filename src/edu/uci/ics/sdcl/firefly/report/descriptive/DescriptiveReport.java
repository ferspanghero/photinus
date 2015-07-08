package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.HashMap;
import java.util.List;

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
	private HashMap<String, List<String>> table = new HashMap<String, List<String>>();
	
	public DescriptiveReport(HashMap<String, List<String>> content) {
		this.table = content;
	}
	
	/**
	 * Calls the ReportWrite so the content
	 * of this report will be written in an
	 * external source, an excel sheet for instance
	 */
	public void exportReport()
	{
		throw new UnsupportedOperationException("method not implemented yet");
	}
}
