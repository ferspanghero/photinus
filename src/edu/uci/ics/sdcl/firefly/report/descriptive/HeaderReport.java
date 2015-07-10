package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

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
	private Map<String, List<String>> content = new LinkedHashMap<String, List<String>>();
	private Map<String, Microtask> questions;
	
	/**
	 * CONSTRUCTOR
	 */
	public HeaderReport(Map<String, Microtask> content) {
		this.questions = content;
	}
	
	/**
	 * Filters the content and returns the HashMap containing
	 * the Headers and the contents of the table.
	 * @return: The map containing the columns and values of the table
	 */
	public Map<String, List<String>> generateReport()
	{
		PropertyManager properties = PropertyManager.initializeSingleton();
		List<String> bugCoveringIds = new ArrayList<String>();
		for (String id : properties.bugCoveringList.split(";")) {
			bugCoveringIds.add(id);
		}
		List<String> questionIDList = new ArrayList<String>();
		List<String> javaMethodList = new ArrayList<String>();
		List<String> questionTypeList = new ArrayList<String>();
		List<String> bugCoveringList = new ArrayList<String>();
		for (String questionID : questions.keySet()) {
			questionIDList.add(questionID);
			Microtask microtask = questions.get(questionID);
			javaMethodList.add(microtask.getFileName());
			questionTypeList.add(microtask.getQuestionType());
			bugCoveringList.add((verifyBugCovering(questionID, bugCoveringIds) ? "yes" : "no"));
		}
		this.content.put("Question ID", questionIDList);
		this.content.put("Java Method", javaMethodList);
		this.content.put("Bug Covering", bugCoveringList);
		this.content.put("Type of question", questionTypeList);
		
		return this.content;
	}
	
	/**
	 * Verifies weather a question is bug covering or not.
	 * 
	 * @param questionID: Question ID of the microtask to be verified.
	 * @param bugCoveringIDs: The list of all microtasks that are bug covering.
	 * @return: TRUE if it is bug covering FALSE otherwise.
	 */
	private boolean verifyBugCovering(String questionID, List<String> bugCoveringIDs)
	{
		boolean status = false;
		for (String ID : bugCoveringIDs) {
			if(ID.equalsIgnoreCase(questionID))
			{
				status = true;
				break;
			}
		}
		return status;
	}
	
}
