package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.HashMap;
import java.util.List;

import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;

/**
 * Responsible for doing the communication with the session 
 * database, in this case the database is the log file. All
 * operations with the sessionLog file shall be centered here.
 * 
 * @author igMoreira
 *
 */
public abstract class SessionDTO {

	/**
	 * Will hold the data loaded from the database.
	 * Necessary not to keep connecting to the database.
	 */
	protected HashMap<String, WorkerSession> sessions = new HashMap<String, WorkerSession>();
	
	/**
	 * Will hold the data loaded from the database.
	 * Necessary not to keep connecting to the database.
	 */
	protected HashMap<String, Microtask> microtasks = new HashMap<String, Microtask>();
	
	/**
	 * Responsible for getting all the sessions
	 * existent. If the sessions were not loaded
	 * from the database it should read the database
	 * content first.
	 * @return: A list containing all the sessions on the database
	 */
	public abstract List<Worker> getSessions(); //TODO: Implement the get session on SessionDTO.
	
	
	public abstract List<Microtask> getMicrotasks();//TODO: Implement the get microtasks on the SessionDTO.
	
	/**
	 * Responsible for loading data from the database
	 * and setting to the sessions attribute.
	 */
	protected void loadSessions()
	{
		//TODO: Implement read the database sessions
	}
	
	/**
	 * Responsible for loading data from the database and
	 * setting to the microtasks attribute.
	 */
	protected void loadMicrotasks()
	{
		//TODO: Implement read the database microtasks
	}
}
