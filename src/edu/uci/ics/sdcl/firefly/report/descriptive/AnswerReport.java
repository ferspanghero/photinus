package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.HashMap;
import java.util.List;

import edu.uci.ics.sdcl.firefly.Microtask;


/**
 * This class holds a part of the DescriptiveReport,
 * this will be the part will which will vary most in
 * content. This is the GREEN part of the tables.
 * 
 * @author igMoreira
 *
 */
public abstract class AnswerReport {
	
	/**
	 * Holds the content of the GREEN part of the report
	 */
	protected HashMap<String, List<String>> content = new HashMap<String, List<String>>();
	
	/**
	 * Filters the content and returns the HashMap containing
	 * the Headers and the contents of the table.
	 * @return: The map containing the columns and values of the table
	 */
	public abstract HashMap<String, List<String>> generateReport( HashMap<String, List<String>> content, HashMap<String, Microtask> microtaks );
	
}
