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
public abstract class DescriptiveReport {

	/**
	 * Holds all the contents of the report.
	 */
	private HashMap<String, List<String>> table = new HashMap<String, List<String>>();
	
	/**
	 * Calls the ReportWrite so the content
	 * of this report will be written in an
	 * external source, an excel sheet for instance
	 */
	public abstract void exportReport();
}
