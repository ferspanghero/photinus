package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.uci.ics.sdcl.firefly.Microtask;

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
	private HashMap<String, Microtask> questions;
	
	/**
	 * CONSTRUCTOR
	 */
	public HeaderReport(HashMap<String, Microtask> content) {
		this.questions = content;
	}
	
	/**
	 * Filters the content and returns the HashMap containing
	 * the Headers and the contents of the table.
	 * @return: The map containing the columns and values of the table
	 */
	public HashMap<String, List<String>> generateReport()
	{
		List<String> questionIDList = new ArrayList<String>();
		for (String questionID : questions.keySet()) {
			questionIDList.add(questionID);
		}
		this.content.put("Question ID", questionIDList);
		return this.content;
	}
	
}
