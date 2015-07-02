package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.HashMap;
import java.util.List;

/**
 * This class holds a part of the DescriptiveReport.
 * This is the BLUE part of the tables.
 * 
 * @author igMoreira
 *
 */
public class HeaderReport {

	/**
	 * Holds the content of the BLUE part of the report
	 */
	private HashMap<String, List<String>> content = new HashMap<String, List<String>>();
	
	/**
	 * CONSTRUCTOR
	 */
	public HeaderReport(HashMap<String, List<String>> content) {
	}
	
	/**
	 * Filters the content and returns the HashMap containing
	 * the Headers and the contents of the table.
	 * @return: The map containing the columns and values of the table
	 */
	public HashMap<String, List<String>> generateReport()
	{
		throw new UnsupportedOperationException("The method generateReport is not implemented yet");
	}
	
}
