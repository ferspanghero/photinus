package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.uci.ics.sdcl.firefly.Microtask;
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
	protected Map<String, WorkerSession> openSessions = new HashMap<String, WorkerSession>();
	
	/**
	 * Will hold the data loaded from the database.
	 * Necessary not to keep connecting to the database.
	 */
	protected Map<String, WorkerSession> closedSessions = new HashMap<String, WorkerSession>();
	
	/**
	 * Will hold the data loaded from the database.
	 * Necessary not to keep connecting to the database.
	 */
	protected Map<String, Microtask> microtasks = new HashMap<String, Microtask>();
	
	/**
	 * Responsible for getting all the sessions
	 * existent. If the sessions were not loaded
	 * from the database it should read the database
	 * content first.
	 * @return: A list containing all the sessions on the database
	 */
	public abstract Map<String,WorkerSession> getSessions();
	
	
	public abstract Map<String,Microtask> getMicrotasks();
	
	/**
	 * Responsible for loading data from the database
	 * and setting to the sessions attribute.
	 */
	protected  abstract void loadSessions();
	
	public abstract ArrayList<WorkerSession> getSessionsByWorkerID(String workerID);
	
}
