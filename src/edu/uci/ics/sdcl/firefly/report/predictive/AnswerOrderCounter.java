package edu.uci.ics.sdcl.firefly.report.predictive;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileSessionDTO;

/** Selects all first, second and third answers for each Java method */
public class AnswerOrderCounter {

	ArrayList<Double> firstAnswerList;
	ArrayList<Double> secondAnswerList;
	ArrayList<Double> thirdAnswerList;
	
	Double outlierCut = new Double(60*30); //30min
	
	public AnswerOrderCounter (){
		this.firstAnswerList = new ArrayList<Double>();
		this.secondAnswerList = new ArrayList<Double>();
		this.thirdAnswerList = new ArrayList<Double>();

	}
	
	public void buildMaps(HashMap<String, Microtask> microtaskMap){
		
		for(Microtask microtask: microtaskMap.values()){
			Vector<Answer> answerList = microtask.getAnswerList();
			for(Answer answer : answerList){
				String order = new Integer(answer.getOrderInWorkerSession()).toString();
				Double duration = new Double(answer.getElapsedTime())/1000; //In seconds
 				if(duration<outlierCut)
					addAnswer(order,duration);
			}
		}
	}
	
	private void addAnswer(String order,Double duration){
		//System.out.println(order+":"+duration.toString());
		if(order.compareTo("1")==0){
			this.firstAnswerList.add(duration);
		}
		else{
			if(order.compareTo("2")==0)
				this.secondAnswerList.add(duration);
			else{
				if(order.compareTo("3")==0)
					this.thirdAnswerList.add(duration);
				else
					System.out.println("ERROR, INDEX LARGER THAN 3. In AnswerOrderCounter : "+order);
			}
		}
	}
	
	
	private String getHeader(){
		return "First,Second,Third";
	}
	
	public void printResults(){	

		String destination = "C://firefly//answerOrderDuration.csv";
		BufferedWriter log;
		
		try {
			log = new BufferedWriter(new FileWriter(destination));
			//Print file header

			log.write(getHeader()+"\n");
			
			boolean allEmpty = false;
			int i=0;
			while(!allEmpty){
				String firstValue = getElement(firstAnswerList,i);
				String secondValue = getElement(secondAnswerList,i);
				String thirdValue = getElement (thirdAnswerList, i);
				
				String line= firstValue+","+ secondValue+","+thirdValue;
				log.write(line+"\n");
			
				int condition = firstValue.length() + secondValue.length() + thirdValue.length();
				if(condition ==0)
					allEmpty=true;
				i++;
			}
		
			log.close();
			System.out.println("file written at: "+destination);
		} 
		catch (Exception e) {
			System.out.println("ERROR while processing file:" + destination);
			e.printStackTrace();
		}
	}
	
	private String getElement(ArrayList<Double> list, int i){
		if(list.size()>i)
			return list.get(i).toString();
		else 
			return "";
	}
	
	public static void main(String args[]){
		
		AnswerOrderCounter order = new AnswerOrderCounter();
		FileSessionDTO sessionDTO = new FileSessionDTO();
		HashMap<String,Microtask> microtaskMap =  (HashMap<String, Microtask>) sessionDTO.getMicrotasks();
		order.buildMaps(microtaskMap);
		order.printResults();
		
	}
}
