package edu.uci.ics.sdcl.firefly.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Iterator;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.memento.MicrotaskMemento;

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

	public Microtask selectMicrotask(String fileName){

		MicrotaskMemento memento = new MicrotaskMemento();
		FileDebugSession debugSession = memento.read(fileName);
		if((debugSession==null) || (debugSession.getMicrotaskMap()==null)){
			return null;
		}
		else{
			HashMap<Integer, Microtask> map = debugSession.getMicrotaskMap();
			Set<Integer> keySet = map.keySet();	
			Integer maxAnswers= debugSession.getMaximumAnswerCount();
			Iterator<Integer> iter = keySet.iterator();
			Integer key = (Integer) iter.next();

			//Check if there is a max number of questions. If not, then return the fist Microtask.
			if((maxAnswers == null) || (maxAnswers.intValue()==0)){
				return map.get(key);
			}
			else{//Select a Microtask with less answers than the maximum.

				boolean found=false;
				Microtask task=null;
				while(!found && iter.hasNext()){
					key = iter.next();
					task = map.get(key);
					ArrayList<Answer> answers = task.getAnswerList();
					if(answers!=null && answers.size()<maxAnswers)
						found=true;
				}
				//Returns the last task found, having found or not.
				return task;
			}//else
		}//else
	}//selectMicrotask

}