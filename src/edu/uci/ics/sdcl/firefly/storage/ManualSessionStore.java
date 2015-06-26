package edu.uci.ics.sdcl.firefly.storage;

import java.util.Hashtable;

import edu.uci.ics.sdcl.firefly.util.PropertyManager;

/** Manually define which HITs and Microtasks should be considered to generate WorkerSessions.
 * This is necessary if one wishes to crowdsource a subset of all questions for each Java method.
 * 
 * Format is the following: 
 * 
 * FileName1: Filename2
 *
 * Microtasks separated by semicolon are from the same fileName. 
 * Groups of microtasks from different fileNames are separated by column 
 * 
 * microtaskId1; microtaskId2: microtask3; microtask4 : microtask5; microtask6: 
 * 
 * @author adrianoc
 *
 */
public class ManualSessionStore {

		
	Hashtable<String, Hashtable<Integer,Integer>> sessionMicrotaskMap = new Hashtable<String, Hashtable<Integer,Integer>>();
	String fileNameList;
	String microtaskList;
	
	public ManualSessionStore(){
		PropertyManager manager = PropertyManager.initializeSingleton();
		fileNameList = manager.fileNameTokenList;
		microtaskList = manager.microtaskTokenList;
	}
	
	public Hashtable<String, Hashtable<Integer,Integer>> generate(){

		Hashtable<String, Hashtable<Integer,Integer>> sessionMicrotaskMap = new Hashtable<String, Hashtable<Integer,Integer>>();
		
		String[] fileTokenizer = fileNameList.split(":");
		String[] microtaskGroupTokenizer = microtaskList.split(":");
		for(int i=0; i<fileTokenizer.length; i++){
			String filename = fileTokenizer[i];
			if(filename!=null && filename.length()>0){
				
				//Now get the group for that filename
				if(microtaskGroupTokenizer.length>i){
					String sessionMicrotaskTokens = microtaskGroupTokenizer[i];
					if(sessionMicrotaskTokens!=null && sessionMicrotaskTokens.length()>0){
						Hashtable<Integer,Integer> microtaskIDList = new Hashtable<Integer,Integer>();
						String[] microtaskTokenizer = sessionMicrotaskTokens.split(";");
						
						//Now get all microtasks from this group
						for(String microtaskIDStr : microtaskTokenizer){
							 
							if(sessionMicrotaskTokens!=null && sessionMicrotaskTokens.length()>0){
								try{
									Integer microtaskID = new Integer(microtaskIDStr);
									microtaskIDList.put(microtaskID,microtaskID);
								}
								catch(Exception e){
									System.err.println("In ManualSessionStore, microtask ID is invalid: " + microtaskIDStr);
								}
							}
						}
						
						//Add the list to the file Name
						sessionMicrotaskMap.put(filename, microtaskIDList);
					}
				}
			}
			
		}
		return sessionMicrotaskMap;
	}
	

	
}
