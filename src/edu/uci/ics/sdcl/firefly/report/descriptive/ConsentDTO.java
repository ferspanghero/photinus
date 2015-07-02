package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.HashMap;
import java.util.List;

import edu.uci.ics.sdcl.firefly.Worker;

/**
 * Responsible for doing the communication with the consent 
 * database, in this case the database is the log file. All
 * operations with the consentLog file shall be centered here.
 * 
 * @author igMoreira
 *
 */
public abstract class ConsentDTO {
	
	/**
	 * Will hold the data loaded from the database.
	 * Necessary not to keep connecting to the database.
	 */
	protected HashMap<String, Worker> workers = new HashMap<String, Worker>();
	
	/**
	 * Responsible for getting all the workers
	 * existent. If the workers were not loaded
	 * from the database it should read the database
	 * content first.
	 * @return: A list containing all the workers on the database
	 */
	public abstract List<Worker> getWorkers();
	
	/**
	 * Responsible for loading data from the database.
	 * And setting to the workers attribute.
	 */
	protected abstract void loadWorkers(); 
}
