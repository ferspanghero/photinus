package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.HashMap;
import java.util.List;

import edu.uci.ics.sdcl.firefly.WorkerSession;


/**
 * This class holds a part of the DescriptiveReport,
 * this will be the part will which will vary most in
 * content. This is the GREEN part of the tables.
 * 
 * @author igMoreira
 *
 */
public class AnswerReport {
	
	/**
	 * Holds the content of the GREEN part of the report
	 */
	private HashMap<String, List<String>> content = new HashMap<String, List<String>>();
	
	/**
	 * Holds the desired filter for this class
	 */
	private Filter filter;
	
	/**
	 * CONSTRUCTOR
	 */
	public AnswerReport(Filter filter, List<WorkerSession> content) {
		
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
