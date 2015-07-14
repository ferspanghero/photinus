package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.uci.ics.sdcl.firefly.Microtask;


/**
 * This class holds a part of the DescriptiveReport.
 * This is the YELLOW part of the tables.
 * 
 * @author igMoreira
 *
 */
public abstract class CountReport {

	/**
	 * Holds the content of the ORANGE part of the report
	 */
	private Map<String, List<String>> table = new LinkedHashMap<String, List<String>>();
	
	/**
	 * CONSTRUCTOR
	 */
	public CountReport() {
	}
	
	/**
	 * Filters the content and returns the HashMap containing
	 * the Headers and the contents of the table.
	 * @return: The map containing the columns and values of the table
	 */
	public abstract Map<String, List<String>> generateReport(Map<String, List<String>> content);
	
	public abstract String getType();
	
}
