package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * This class holds a part of the DescriptiveReport.
 * This is the YELLOW part of the tables.
 * 
 * @author igMoreira
 *
 */
public class CountReport {

	/**
	 * Holds the content of the ORANGE part of the report
	 */
	private Map<String, List<String>> table = new LinkedHashMap<String, List<String>>();
	
	/**
	 * CONSTRUCTOR
	 */
	public CountReport(Map<String, List<String>> content) {
	}
	
	/**
	 * Filters the content and returns the HashMap containing
	 * the Headers and the contents of the table.
	 * @return: The map containing the columns and values of the table
	 */
	public Map<String, List<String>> generateReport()
	{
		throw new UnsupportedOperationException("The method generateReport is not implemented yet");
	}
}
