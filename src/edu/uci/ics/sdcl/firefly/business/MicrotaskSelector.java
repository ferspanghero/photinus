package edu.uci.ics.sdcl.firefly.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Iterator;

import edu.uci.ics.sdcl.firefly.Answer;
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

	/** String = file name, Integer = largest number of answers yet received by a question for a file.*/
	HashMap <String, Integer> answerCountMap = new HashMap<String,Integer>();

	public MicrotaskSelector(){}


	public void incrementSelector(String fileName, int numberOfAnswers){
		Integer maxAnswer= this.answerCountMap.get(fileName);
		if(maxAnswer!=null)
			if(maxAnswer.intValue()<numberOfAnswers)
				answerCountMap.put(fileName, new Integer(numberOfAnswers));
			else{}
				//ignore it, because there is a microtask with more answers than that.
		else//initialize the vector with the first entry.
			answerCountMap.put(fileName, new Integer(numberOfAnswers));
	}

	public Microtask selectMicrotask(String fileName){

		MicrotaskMemento memento = new MicrotaskMemento();
		HashMap<Integer, Microtask> map = memento.read(fileName);
		if(map==null){
			return null;
		}
		else{
			Set<Integer> keySet = map.keySet();	
			Integer maxAnswers= this.answerCountMap.get(fileName);
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
	
	/** 
	 * 
	 * @param fileName
	 * @return the maximum number of answers received for microtasks on code snippets from a given file
	 */
	public int getMaxNumberOfAnswers(String fileName){
		
		Integer maxObj = this.answerCountMap.get(fileName);
		if (maxObj==null)
				return 0;
		else
			return maxObj.intValue();
	}
}