package edu.uci.ics.sdcl.firefly.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import edu.uci.ics.sdcl.firefly.WorkerSession;

public class AvailableWorkerSessionList {

	private ArrayList<HashMap<String, WorkerSession>> duplicateList;
	
	private HashMap<String, WorkerSession> originalMap;
	
	public AvailableWorkerSessionList(){}
	
	/**
	 * 
	 * @return the next WorkerSession available. First try to 
	 * deplete all WorkerSession in the originalMap, then starts
	 * to consume the WorkerSessions from the duplicateList.
	 * 
	 */
	public WorkerSession getWorkerSession(){
		if(originalMap.isEmpty())
			return this.getDuplicateWorkerSession();
		else
			return this.getOriginalWorkerSession();
	}
	
	/**	
	 * @return WorkerSession from the originalMap. The 
	 * WorkerSession is randomly extracted from the map.
	 */
	private WorkerSession getOriginalWorkerSession(){
		return getRandomWorkerSession(this.originalMap);
	}

	
	/**
	 * @return WorkerSession from the duplicateList
	 */
	private WorkerSession getDuplicateWorkerSession(){
		
		if(this.duplicateList.isEmpty())
			return null;
		
		else{
		
			boolean found = true;
			int index = this.duplicateList.size()-1;
			HashMap<String, WorkerSession> map = this.duplicateList.get(index);
			while(!found){
				if(map.isEmpty() && index>0){
					index--;
					map = this.duplicateList.get(index);
				}
				else{
					
				}
					
				
			}
		}
		return null;
	} 
	

	private WorkerSession getRandomWorkerSession(HashMap<String,WorkerSession> map){
		Iterator<String> iter = map.keySet().iterator();
		Random random = new Random(map.size());
		int position = random.nextInt();
		for(int i=0;i<position;i++){
			iter.next();
		}
		return map.get(iter.next());	
	}
	
	public void removeWorkerSession(String id){}
}
