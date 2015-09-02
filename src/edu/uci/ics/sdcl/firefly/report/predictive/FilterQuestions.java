package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileSessionDTO;

/**
 * Class to filter conditional clause questions with more than 3 LOCs
 * 
 * @author adrianoc
 */
public class FilterQuestions {

	int[] excludeQuestions = {7,26,40,44,48,55,62,66,82,84,88,92,98,115,124};//Conditional clause questions with more than 3 Lines of code
	
	HashMap<String, Integer> excludedQuestionMap;
	
	public FilterQuestions(){
		this.excludedQuestionMap = new HashMap<String,Integer>();
		for(int excluded : this.excludeQuestions){
			this.excludedQuestionMap.put(new Integer(excluded).toString(), new Integer(excluded));
		}
	}
	
	public int countWorkers(){
		
		HashMap<String, String> workerMap =  new HashMap<String, String> ();
		FileSessionDTO dto = new FileSessionDTO();
		HashMap<String, WorkerSession> sessionMap = (HashMap<String, WorkerSession>) dto.getSessions();
		
		for(WorkerSession session: sessionMap.values()){
			Vector<Microtask> microtaskList = session.getMicrotaskList();
			for(Microtask microtask : microtaskList){
				String questionID = microtask.getID().toString();
				if(!this.excludedQuestionMap.containsKey(questionID)){
					ArrayList<String> workerList = (ArrayList<String>) microtask.getWorkerIds();
					for(String workerID : workerList){
						workerMap.put(workerID,  workerID);
					}
				}
			}
		}
		return workerMap.size();
	}
	
	public static void main(String args[]){
		
		FilterQuestions filter =  new FilterQuestions();
		System.out.println(filter.countWorkers());
	}
	
}
