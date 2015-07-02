package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.List;

import edu.uci.ics.sdcl.firefly.WorkerSession;

/**
 * Responsible for filter a collection of Sessions.
 * 
 * @author igMoreira
 *
 */
public interface Filter {

	/**
	 * Applies the filter on the desired content.
	 * @param content: Desired content to be filtered
	 * @return: The filtered content
	 */
	public List<WorkerSession> apply(List<WorkerSession> content);
	
}
