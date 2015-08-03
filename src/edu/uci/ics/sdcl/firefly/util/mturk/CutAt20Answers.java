package edu.uci.ics.sdcl.firefly.util.mturk;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Remove entries from SessionLog that exceed 20 answers per question
 * 
 * @author adrianoc
 *
 */
public class CutAt20Answers {

	String sessionLogFileName = "session-log_consolidated.txt";
	String consentLogFileName = "consent-log_consolidated.txt";

	String sessionLogFileName_Cut = "session-log_consolidated_cut.txt";
	String consentLogFileName_Cut = "consent-log_consolidated_cut.txt";

	HashMap<String, Integer> microtaskAnswerCountMap = new 	HashMap<String, Integer>();


	public void run(){
		LogReadWriter logReadWriter = new LogReadWriter();
		ArrayList<String> buffer = logReadWriter.readToBuffer(4,sessionLogFileName);

		ArrayList<String> newBuffer = this.removeExcessiveAnswers(buffer);
		logReadWriter.writeBackToBuffer(newBuffer, 4, sessionLogFileName_Cut);
		
		this.printMicrotaskWithMoreThan20Answers();
		//this.checkAllMicrotasksHave20Answers(newBuffer);
	}


	private ArrayList<String> removeExcessiveAnswers(ArrayList<String> buffer){

		ArrayList<String> newBuffer = (ArrayList<String>) buffer.clone();

		for(int i=0; i<buffer.size(); i++){
			String line = buffer.get(i);
			if(!line.contains("MICROTASK")){
				newBuffer.set(i,line);
			}
			else{
				String microtaskID = line.split("%")[9];
				if(!hasMoreThan20(microtaskID)){
					newBuffer.set(i,line);
				}
				else{
					newBuffer.set(i, "/n");
				}
			}
		}
		return newBuffer;
	}


	/** 
	 * 
	 * @param microtaskID
	 * @return true if microtask has more than 20 answers, false otherwise
	 */
	private boolean hasMoreThan20(String microtaskID){

		Integer count = this.microtaskAnswerCountMap.get(microtaskID);

		if(count==null){
			count = new Integer(1);
			this.microtaskAnswerCountMap.put(microtaskID, count);
			return false;
		}
		else{
			count++;
			this.microtaskAnswerCountMap.put(microtaskID, count);
			if(count<=20){				
				return false;
			}
			else{
				return true;
			} 
		}
	}
	
	
	private void printMicrotaskWithMoreThan20Answers(){
	
		int i=0;
		for(String microtaskID: this.microtaskAnswerCountMap.keySet()){
			Integer count = this.microtaskAnswerCountMap.get(microtaskID);
			if(count>20){
				System.out.println("Microtasks with more than 20 answers:"+microtaskID+":answers:"+count.toString());
				i++;
			}		
		}
		System.out.println("Total microtasks: "+i);	
	}


	private void checkAllMicrotasksHave20Answers(ArrayList<String>buffer){
	
		HashMap<String,Integer> microtaskCounter = new HashMap<String,Integer>();
		
		for(int i=0; i<buffer.size(); i++){
			String line = buffer.get(i);
			if(line.contains("MICROTASK")){
				String id = line.split("%")[9];
				Integer counter = microtaskCounter.get(id);
				if(counter==null)
					counter= new Integer(0);
				counter++;
				microtaskCounter.put(id,counter);
			}
		}
			
		int i=0;
		for(String microtaskID: microtaskCounter.keySet()){
			Integer count = microtaskCounter.get(microtaskID);
			if(count!=20){
				System.out.println("Microtasks incomplete:"+microtaskID+":answers:"+count.toString());
				i++;
			}
		}
		System.out.println("Total problematic microtasks: "+i);
	}
	
	
	public static void main(String[] args){
		CutAt20Answers cutter = new CutAt20Answers();
		cutter.run();
	}

}
