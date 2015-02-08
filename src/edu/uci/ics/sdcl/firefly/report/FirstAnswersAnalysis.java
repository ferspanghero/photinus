package edu.uci.ics.sdcl.firefly.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.report.LogAnalysis.Counter;

public class FirstAnswersAnalysis {

	LogData data;
	
	HashMap<Integer,Microtask> filteredMicrotaskMap; 
	
	public FirstAnswersAnalysis(LogData data){
		this.data = data;
		filteredMicrotaskMap = new HashMap<Integer,Microtask>();
		//counterMap = new HashMap<String,Counter>(); 
		//bugReportResultMap = new HashMap<String, Result>();
	}
	
	/**
	 * Remove all answers above a threshold
	 * @param maximumAnswers the number of answers that will be considered for each worker
	 */
	public void filterWorkerAnswers(int maximumAnswers){	
		
		Iterator<String> workerIdIter = data.workerMap.keySet().iterator();
		while(workerIdIter.hasNext()){
			String workerId = workerIdIter.next();
			Worker worker = data.workerMap.get(workerId);
			String sessionId = worker.getSessionId();
			WorkerSession session = data.sessionMap.get(sessionId);
			Vector<Microtask> microtaskVector = session.getMicrotaskList();
			int i=0;
			while(i<microtaskVector.size() && i<maximumAnswers){
				Microtask workerMicrotask = microtaskVector.elementAt(i);
				addToFilteredMicrotasks(workerMicrotask,workerId);
				i++;
			}
		}
	}
	
	/** Add the worker's microtask to the list */
	public void addToFilteredMicrotasks(Microtask workerMicrotask, String workerId){
	
		Microtask filteredMicrotask = this.filteredMicrotaskMap.get(workerMicrotask.getID());
		
		if(filteredMicrotask==null)
			this.filteredMicrotaskMap.put(workerMicrotask.getID(), workerMicrotask);
		else{
			Answer workerAnswer = workerMicrotask.getAnswerByUserId(workerId);
			filteredMicrotask.addAnswer(workerAnswer);
			this.filteredMicrotaskMap.put(filteredMicrotask.getID(), filteredMicrotask);
		}
		
	}

	private ArrayList<String> writeAnswerLabels_Filtered_by_DURATION_GRADE_IDK(Double minimumDuration, Double maxDuration, Integer minimumGrade, Integer maxGrade, Integer numberOfICanTell, Integer lowerNumberOfICantTell){
		ArrayList<String> contentList = new ArrayList<String>();

		//System.out.println("Size of microtask Map: "+ data.microtaskMap.size());

		Iterator<String> iter=data.microtaskMap.keySet().iterator();
		discardedWorkerMap = new HashMap<String, String>();//just to analyze who are the workers in terms of skill test score
		activeWorkerMap = new HashMap<String, String>();
		int answerCount=0;
		int validAnswers=0;
		while(iter.hasNext()){
			StringBuffer buffer = new StringBuffer();//new line
			String id = iter.next();
			Microtask task = data.microtaskMap.get(id);
			buffer.append(task.getID().toString());
			buffer.append("|");
			int validMicrotaskAnswers=0;
			Vector<Answer> answerList = task.getAnswerList();
			for(int i=0;i<answerList.size();i++){
				Answer answer = answerList.get(i);					
				String workerId = answer.getWorkerId();
				Integer count = data.workerICantTellMap.get(workerId);
				Worker worker = data.workerMap.get(workerId);
				Integer grade = worker.getGrade();	
				Double duration = new Double(answer.getElapsedTime());
				if(count!=null && count.intValue()<numberOfICanTell && count.intValue()>lowerNumberOfICantTell && 
						grade!=null && grade>=minimumGrade && grade<=maxGrade && 
						duration>=minimumDuration&& duration<=maxDuration){
					answerCount++;
					validMicrotaskAnswers++;
					activeWorkerMap.put(workerId, workerId);
					buffer.append(answer.getOption());
					buffer.append("|");
				}
				else{
					//System.out.println("Worker "+ workerId+" discarded, I Cannot Tell count= "+count+", grade="+grade+", duration="+duration);
					this.discardedWorkerMap.put(workerId,workerId);
				}
			}

			if(validMicrotaskAnswers>10)
				validAnswers = validAnswers+10;//because we would ignore anything above 10 answers for one same question.
			else
				validAnswers = validAnswers+validMicrotaskAnswers;
			contentList.add(buffer.toString());
		}
		System.out.println("valid answers=" + validAnswers+ ", active workers: "+this.activeWorkerMap.size());
		//System.out.println(validAnswers);//this.activeWorkerMap.size());// ", active workers: "+);
		return contentList;
	}

	
	private static FirstAnswersAnalysis initializeLogs(){
		boolean rejectNoisyWorkers = false;
		double minimalDuration = 0;
		LogData data = new LogData(false, 0);	
		
		String samsungPath = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\RawDataLogs\\";
		String dellPath = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\RawDataLogs\\";
		String path = samsungPath;
		
		data.processLogProduction1(path);
		data.processLogProduction2(path);

		data.printSummary();
		
		FirstAnswersAnalysis firstData = new FirstAnswersAnalysis(data);
		return firstData;
	}
	
	
	public static void main(String[] args){

		FirstAnswersAnalysis firstData = initializeLogs();

		String path = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\DataAnalysis\\BaseDataInTime\\questionType\\";
			// "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\DataAnalysis\\BaseDataInTime\\combined123\\";

		Double minDuration = new Double(0.00);
		Double maxDuration = new Double(10000.00);
		int minScore=3;
		int maxScore=4;
		int maxICT=2;
		int minICT=0;
		String fileName;
		int firstAnswer=1;
		
		//firstData.printToFile(path+fileName, firstData.writeAnswerLabels_Filtered_by_DURATION_GRADE_IDK( minDration, maxDuration, minScore,maxScore,maxICT,minICT,firstAnswers));
	}
	
}
