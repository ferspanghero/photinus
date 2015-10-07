package edu.uci.ics.sdcl.firefly.util.mturk;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Remove entries from SessionLog that exceed 20 answers per question
 * Also replaces the % symbol for the word "percent" in the workers explanations
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

		buffer = this.removeExcessiveAnswers(buffer);

		buffer = this.replaceExplanationsWithPercentage(buffer);

		logReadWriter.writeBackToBuffer(buffer, 4, sessionLogFileName_Cut);

		//this.printMicrotaskWithMoreThan20Answers();
		this.checkAllMicrotasksHave20Answers(buffer);
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
					newBuffer.set(i,"\n");
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


	private ArrayList<String> replaceExplanationsWithPercentage(ArrayList<String> buffer){

		String[] sessionList = {
				"38ae9a-2G-80-4",
				"221gI-9E7a1-3-9",
				"163eE-5c6C-8-21",
				"352eG6G8I245",
				"760gA-5G-9C-2-76",
				"315EA-8G8E705",
				"417AC-1e-3I5-50",
				"72iG-9A-8i02-1",
				"29CA-5C6g71-2",
			"5gI3C-9i-50-7"};

		String[] microtaskList = {
				"14",
				"25",
				"18",
				"86",
				"97",
				"81",
				"1",
				"52",
				"81",
		"47"};

		HashMap<String, String> sessionMicrotaskMap = new HashMap<String,String>();

		for(int i=0; i<sessionList.length;i++){
			sessionMicrotaskMap.put(sessionList[i], microtaskList[i]);
		}

		ArrayList<String> newBuffer = (ArrayList<String>) buffer.clone();

		for(int i=0; i<buffer.size(); i++){
			String line = buffer.get(i);
			if(line.contains("MICROTASK")){
				String sessionID = line.split("%")[7];
				if(sessionMicrotaskMap.containsKey(sessionID)){
					String sessionMicrotaskID = sessionMicrotaskMap.get(sessionID);
					String logMicrotaskID = line.split("%")[9];
					if(sessionMicrotaskID.compareTo(logMicrotaskID)==0){
						newBuffer.set(i, this.replacePercentSymbol(line));
					}
				}
			}
		}	
		return newBuffer;
	}


	private String replacePercentSymbol(String line){

		String[] elements = line.split("%");
		if(elements.length<25){//Means that there is no % symbol in the explanation text.
			//System.out.println(line);
			//System.out.println(line.split("\t")[0]);
			//System.out.println(line.split("\t")[1]);
			return line;
		}
		else{
			String explanationPart1 = line.split("%")[23];
			String explanationPart2 = line.split("%")[24];
			int end = line.lastIndexOf("explanation") + "explanation%".length();
			String subLine = line.substring(0, end);
			subLine = subLine + explanationPart1+ " percent" + explanationPart2;
			return subLine;
		}
	}

	public void testReplacePercentSymbol(){
		String input = "http-bio-8080-exec-8925] INFO  - EVENT%MICROTASK% workerId%27Ei-8i0A-3-59:15cG-7i7C996% fileName%HIT02_24% sessionId%38ae9a-2G-80-4% microtaskId%14% questionType%VARIABLE_DECLARATION% question%Is there any issue with the use or the definition of variable 'g' in the source code below that might be related to the failure?% answer%YES, THERE IS AN ISSUE% confidenceLevel%4% difficulty%3% duration%247533.0% explanation%The calculation of g appears to result in a negative integer value if the parameter value is negative to begin with. Passing a negative g into Color would produce the error and there is nothing in the definition of g that seeks to prevent that. And the calculation certainly looks like it would come out negative in that scenario. Again without understanding the relevance of lowerbound and upperbound, I cannot be 100% certain, as a caveat.";		
		System.out.println(this.replacePercentSymbol(input));
	}

	//----------------------------------------------------------------------------------------------
	public static void main(String[] args){
		CutAt20Answers cutter = new CutAt20Answers();
		cutter.run();

		//cutter.testReplacePercentSymbol();
	}

}
