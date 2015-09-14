package edu.uci.ics.sdcl.firefly.report.predictive;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileSessionDTO;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

/** Selects all first, second and third answers for each Java method */
public class AnswerOrderCounter {

	ArrayList<Tuple> firstAnswerList;
	ArrayList<Tuple> secondAnswerList;
	ArrayList<Tuple> thirdAnswerList;

	HashMap<String,String> firstWorkerCount;
	HashMap<String,String> secondWorkerCount;
	HashMap<String,String> thirdWorkerCount;

	HashMap<String,String> bugCoveringMap;

	double minimumDuration_1stAnswer;
	double maximumDuration_1stAnswer ;

	double minimumDuration_2ndAnswer;
	double maximumDuration_2ndAnswer ;

	double minimumDuration_3rdAnswer;
	double maximumDuration_3rdAnswer ;

	public class Tuple{

		double TP=0.0;
		double TN=0.0;
		double FN=0.0;
		double FP=0.0;

		String questionID=null;
		String javaMethod=null;

		double duration=0.0;
		String answerOption=null;

		boolean isEmpty = false;

		public String toString(){

			if(questionID==null){
				isEmpty=true;
				return ", , , , , , , ";
			}
			else
				return javaMethod+","+questionID+","+duration+","+TP+","+TN+","+FN+","+FP+","+answerOption;

		}
	}

	public AnswerOrderCounter (double minimumDuration_1stAnswer, double maximumDuration_1stAnswer,
			double minimumDuration_2ndAnswer, double maximumDuration_2ndAnswer,
			double minimumDuration_3rdAnswer, double maximumDuration_3rdAnswer){

		this.minimumDuration_1stAnswer = minimumDuration_1stAnswer;
		this.maximumDuration_1stAnswer = maximumDuration_1stAnswer;

		this.minimumDuration_2ndAnswer = minimumDuration_2ndAnswer;
		this.maximumDuration_2ndAnswer = maximumDuration_2ndAnswer;

		this.minimumDuration_3rdAnswer = minimumDuration_3rdAnswer;
		this.maximumDuration_3rdAnswer = maximumDuration_3rdAnswer;

		//Obtain bug covering question list
		PropertyManager manager = PropertyManager.initializeSingleton();
		bugCoveringMap = new HashMap<String,String>();
		String[] listOfBugPointingQuestions = manager.bugCoveringList.split(";");
		for(String questionID:listOfBugPointingQuestions){
			this.bugCoveringMap.put(questionID,questionID);
		}

		//Initialize datastructures
		this.firstAnswerList = new ArrayList<Tuple>();
		this.secondAnswerList = new ArrayList<Tuple>();
		this.thirdAnswerList = new ArrayList<Tuple>();

		this.firstWorkerCount = new HashMap<String,String>();
		this.secondWorkerCount = new HashMap<String,String>();
		this.thirdWorkerCount = new HashMap<String,String>();
	}

	/** Builds the three maps of answer duration, which can be printed by 
	 * method 
	 * @param microtaskMap
	 */
	public void buildDurationsByOrder(HashMap<String, Microtask> microtaskMap){

		for(Microtask microtask: microtaskMap.values()){
			Vector<Answer> answerList = microtask.getAnswerList();
			for(Answer answer : answerList){
				String order = new Integer(answer.getOrderInWorkerSession()).toString();
				Double duration = new Double(answer.getElapsedTime())/1000; //In seconds

				if(validDuration(order, duration)){
					Tuple tuple = computeCorrectness(microtask.getID().toString(),answer.getOption());
					tuple.duration = duration;
					tuple.javaMethod = microtask.getFileName();
					tuple.answerOption = answer.getShortOption();
					addAnswer(order,tuple,answer.getWorkerId());
				}


			}
		}
	}

	private boolean validDuration(String order, Double duration){

		double max = 0.0;
		double min = 0.0;

		if(order.compareTo("1")==0){
			max = this.maximumDuration_1stAnswer;
			min = this.minimumDuration_1stAnswer;
		}
		else{
			if(order.compareTo("2")==0){
				max = this.maximumDuration_2ndAnswer;
				min = this.minimumDuration_2ndAnswer;
			}
			else{
				if(order.compareTo("3")==0){
					max = this.maximumDuration_3rdAnswer;
					min = this.minimumDuration_3rdAnswer;
				}
				else
					System.out.println("ERROR, INDEX LARGER THAN 3. In AnswerOrderCounter : "+order);
			}
		}

		return (duration<=max&& duration>min);

	}


	private void addAnswer(String order,Tuple tuple, String workerID){
		//System.out.println(order+":"+duration.toString());
		if(order.compareTo("1")==0){
			this.firstAnswerList.add(tuple);
			this.firstWorkerCount.put(workerID, workerID);
		}
		else{
			if(order.compareTo("2")==0){
				this.secondAnswerList.add(tuple);
				this.secondWorkerCount.put(workerID, workerID);
			}
			else{
				if(order.compareTo("3")==0){
					this.thirdAnswerList.add(tuple);
					this.thirdWorkerCount.put(workerID, workerID);
				}
				else
					System.out.println("ERROR, INDEX LARGER THAN 3. In AnswerOrderCounter : "+order);
			}
		}
	}

	private Tuple computeCorrectness(String questionID, String answerOption){

		Tuple tuple = new Tuple();

		if(this.bugCoveringMap.containsKey(questionID)){
			if(answerOption.compareTo(Answer.YES)==0)
				tuple.TP++;
			else
				if(answerOption.compareTo(Answer.NO)==0) //Ignores IDK	
					tuple.FN++;
		}
		else{
			if(answerOption.compareTo(Answer.NO)==0)
				tuple.TN++;
			else
				if(answerOption.compareTo(Answer.YES)==0)	 //Ignores IDK				
					tuple.FP++;
		}

		tuple.questionID = questionID;

		return tuple;


	}



	private String getDurationOrderFileHeader(){
		String header = "FirstJavaMethod,FirstID,FirstDuration, FirstTP, FirstTN, FirstFN, FirstFP,FirstOption,"+
				"SecondJavaMethod,SecondID,SecondDuration, SecondTP, SecondTN, SecondFN, SecondFP,SecondOption,"+
				"ThirdJavaMethod,ThirdID,ThirdDuration, ThirdTP, ThirdTN, ThirdFN, ThirdFP,ThirdOption";
		return header;
	}

	public void printDurationOrderLists(){	

		String destination = "C://firefly//answerOrderDuration.csv";
		BufferedWriter log;

		try {
			log = new BufferedWriter(new FileWriter(destination));
			//Print file header

			log.write(getDurationOrderFileHeader()+"\n");

			boolean allEmpty = false;
			int i=0;
			while(!allEmpty){
				Tuple firstValue = getElement(firstAnswerList,i);
				Tuple secondValue = getElement(secondAnswerList,i);
				Tuple thirdValue = getElement (thirdAnswerList, i);

				String line= firstValue.toString()+","+ secondValue.toString()+","+thirdValue.toString();
				log.write(line+"\n");

				if(firstValue.isEmpty && secondValue.isEmpty && thirdValue.isEmpty)
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

	private Tuple getElement(ArrayList<Tuple> list, int i){

		if(list.size()>i)
			return list.get(i);
		else 
			return new Tuple();
	}

	private double computeListAccuracy(ArrayList<Tuple> tupleList){
		double TP=0.0;
		double TN=0.0;
		double FN=0.0;
		double FP=0.0;

		for(Tuple tuple: tupleList){
			TP = TP + tuple.TP;
			TN = TN + tuple.TN;
			FN = FN + tuple.FN;
			FP = FP + tuple.FP;

		}

		double accuracy = (TP+TN)/(TP+TN+FP+FN);

		return accuracy;
	}

	public void printQuartileAccuracy(){


		System.out.print("1stAnswer: "+computeListAccuracy(this.firstAnswerList)+"; ");
		System.out.print("2ndAnswer: "+computeListAccuracy(this.secondAnswerList)+"; ");
		System.out.println("3rdAnswer: "+computeListAccuracy(this.thirdAnswerList));



	}

	public int getTotalQuartileAnswers(){
		return  (this.firstAnswerList.size() + this.secondAnswerList.size() + this.thirdAnswerList.size());
	}
	
	public int getTotalQuartileWorkers(){
		return  (this.firstAnswerList.size() + this.secondAnswerList.size() + this.thirdAnswerList.size());
	}

	public void printNumberOfAnswersByOrder(){

		System.out.print("1stAnswer size: "+this.firstAnswerList.size()+"; ");
		System.out.print("2ndAnswer size: "+this.secondAnswerList.size()+"; ");
		System.out.println("3rdAnswer size: "+this.thirdAnswerList.size());
	}
	
	public void printNumberOfWorkersByOrder(){

		System.out.print("1stAnswer Worker Count: "+this.firstWorkerCount.size()+"; ");
		System.out.print("2ndAnswer Worker Count: "+this.secondWorkerCount.size()+"; ");
		System.out.println("3rdAnswer Worker Count: "+this.thirdWorkerCount.size());
	}

	
	public HashMap<String,String> getAllWorkersMap(){
		HashMap<String,String> allMap = new HashMap<String, String>();
		allMap.putAll(this.firstWorkerCount);
		allMap.putAll(this.secondWorkerCount);
		allMap.putAll(this.thirdWorkerCount);
		return allMap;
	}
	
	public static void main(String args[]){

		analyseAllAnswers();
		//analysesQuartiles();
	}
	
	public static void analyseAllAnswers(){
		
		for(int i=0;i<4;i++){
			AnswerOrderCounter order = new AnswerOrderCounter(0, 7200,0, 7200,0, 7200);
			FileSessionDTO sessionDTO = new FileSessionDTO();
			HashMap<String,Microtask> microtaskMap =  (HashMap<String, Microtask>) sessionDTO.getMicrotasks();
			order.buildDurationsByOrder(microtaskMap);
			order.printDurationOrderLists();//CSV FILE
		}
		
	}
	
	public static void analysesQuartiles(){
		double[] firstAnswerQuartileVector = {0,167.4,333.4,683.9,3600};
		double[] secondAnswerQuartileVector = {0,69.9,134.0,266.4,3600};
		double[] thirdAnswerQuartileVector = {0,55.8,104.9,202.6,3600};

		HashMap<String, String> distinctWorkersMap = new HashMap<String, String>();
		for(int i=0;i<4;i++){
			AnswerOrderCounter order = new AnswerOrderCounter(firstAnswerQuartileVector[i], firstAnswerQuartileVector[i+1],
					secondAnswerQuartileVector[i], secondAnswerQuartileVector[i+1],
					thirdAnswerQuartileVector[i], thirdAnswerQuartileVector[i+1]);
			FileSessionDTO sessionDTO = new FileSessionDTO();
			HashMap<String,Microtask> microtaskMap =  (HashMap<String, Microtask>) sessionDTO.getMicrotasks();
			order.buildDurationsByOrder(microtaskMap);
			order.printDurationOrderLists();//CSV FILE
			
			System.out.println();
			System.out.print("Quartile "+i +" :");
			order.printQuartileAccuracy();
			System.out.println("Quartile "+i +" number of answers= " +order.getTotalQuartileAnswers());
			order.printNumberOfAnswersByOrder();
			System.out.println("Quartile "+i +" number of workers= " +order.getTotalQuartileWorkers());
			order.printNumberOfWorkersByOrder();
			
			distinctWorkersMap.putAll(order.getAllWorkersMap());
		}
		
		System.out.println("Distinct workers in quartile = "+distinctWorkersMap.size());
	}
}
