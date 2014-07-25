package edu.uci.ics.sdcl.firefly.storage;

/**
 * Manages all the write and read operations to the persistent objects.
 * Keeps the consistency and generates a txt log of all operations.
 * 
 * @author Christian Adriano
 *
 */
public class StorageManager {
	
	String logFile="operations.log";
	private static final String READ="READ";
	private static final String WRITE="WRITE";

	public StorageManager(){}
	
	public void writeMicrotask(){
		//TODO 
	}
	
	public void readMicrotask(Integer id){
		//TODO
	}
	
	private void writeLog(String operation, String data){
		//TODO
	}

}
