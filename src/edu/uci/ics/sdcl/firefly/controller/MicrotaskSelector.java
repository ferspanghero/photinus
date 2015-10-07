package edu.uci.ics.sdcl.firefly.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Iterator;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;

/**
 * Selects the next microtask to be published for execution.
 * 
 * The only rule for selection is that a microtasks was either 
 * not executed yet or have a current number of answers that is not larger than any other existing microtask.
 * Thereby we try to have all microtasks receiving the same amount of answers.
 * 
 * @author Christian Adriano
 *
 */
public class MicrotaskSelector {

	
	public Vector<Microtask> selectAllMicrotasks(){
		
		Vector<Microtask> mtaskList = new Vector<Microtask>();
		MicrotaskStorage memento = MicrotaskStorage.initializeSingleton();
		Hashtable<String, FileDebugSession> map = memento.readAllDebugSessions();
		if((map!=null) && (map.keySet()!=null)){
			Set<String>keySet = map.keySet();
			for(String key: keySet){
				FileDebugSession session = map.get(key);
				Hashtable<Integer, Microtask> mtaskMap = session.getMicrotaskMap();
				if((mtaskMap!=null) && (mtaskMap.keySet()!=null)){
					Set<Integer>mtaskKeySet = mtaskMap.keySet();
					for(Integer mtaskKey: mtaskKeySet){
						Microtask mtask = mtaskMap.get(mtaskKey);
						if(mtask!=null)
							mtaskList.add(mtask);
					}
			}
		}
		}
		return mtaskList;
	}
	
	/** 
	 * @return a list with a microtask and the fileName identifying a debugging session.
	 * @see selectMicrotask(String fileName)
	 */
	public SelectorReturn selectAnyMicrotask(){
		MicrotaskStorage memento = MicrotaskStorage.initializeSingleton();;
		Set<String> debuggingSessionNameSet = memento.retrieveDebuggingSessionNames();
		if((debuggingSessionNameSet==null) || (!debuggingSessionNameSet.iterator().hasNext())){
			return null;
		}
		else{
			String fileName = debuggingSessionNameSet.iterator().next();
			FileDebugSession session = memento.read(fileName);
			String fileContent = session.getFileContent(); 
			Microtask task = this.selectMicrotask(fileName);
			return new SelectorReturn (task,fileName,fileContent);	
		}
	}
	
	
	public class SelectorReturn{
		
		public Microtask task;
		public String fileName;
		public String fileContent;
		
		public SelectorReturn(Microtask task,  String fileName, String fileContent){ 
			this.fileName=fileName; 
			this.task=task;
			this.fileContent = fileContent;
		}
	}
	
	/** 
	 * @param the identifier of a debugging session 
	 * @return a microtask from the debugging session provided
	 */
	public Microtask selectMicrotask(String fileName){

		MicrotaskStorage memento = MicrotaskStorage.initializeSingleton();
		FileDebugSession debugSession = memento.read(fileName);
		if( (debugSession==null) || (debugSession.isEmpty()) ){
			return null;
		}
		else{
			Hashtable<Integer, Microtask> map = debugSession.getMicrotaskMap();
			Set<Integer> keySet = map.keySet();	
			Integer maxAnswers= debugSession.getMaximumAnswerCount();
			Iterator<Integer> iter = keySet.iterator();
			Integer key = (Integer) iter.next();

			//Check if there is a max number of questions. If not, then return the first Microtask.
			if((maxAnswers == null) || (maxAnswers.intValue()==0)){
				return map.get(key);
			}
			else{//Select a Microtask with less answers than the maximum.

				boolean found=false;
				Microtask task=null;
				while(!found && iter.hasNext()){
					key = iter.next();
					task = map.get(key);
					Vector<Answer> answers = task.getAnswerList();
					if(answers!=null && answers.size()<maxAnswers)
						found=true;
				}
				//Returns the last task read, having found one with less answers than max or not.
				return task;
			}//else
		}//else
	}//selectMicrotask

}