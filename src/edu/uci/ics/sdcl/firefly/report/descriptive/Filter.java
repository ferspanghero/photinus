package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import sun.security.util.PendingException;
import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;

/**
 * Responsible for filter a collection of Sessions.
 * 
 * @author igMoreira
 *
 */
public class Filter {
	
	private int[] explanationSize = new int[2];
	private int[] confidence = new int[2];
	private int[] difficulty = new int[2];
	private double[] answerDuration = new double[2];
	private int[] workerScore = new int[2];
	private double[] sessionDuration = new double[2];
	private double[] workerIDKPercentage = new double[2];
	
	private static Map<String, Integer> sessionDurationMap = new HashMap<String, Integer>();
	private static Map<String, Double> workerIDKMap = new HashMap<String, Double>();
	
	public int[] getExplanationSize() {
		return explanationSize;
	}

	public int[] getConfidence() {
		return confidence;
	}

	public int[] getDifficulty() {
		return difficulty;
	}

	public double[] getAnswerDuration() {
		return answerDuration;
	}

	public int[] getWorkerScore() {
		return workerScore;
	}

	public double[] getSessionDuration() {
		return sessionDuration;
	}

	public Filter() {
		Arrays.fill(explanationSize, -1);
		Arrays.fill(confidence, -1);
		Arrays.fill(difficulty, -1);
		Arrays.fill(answerDuration, -1);
		Arrays.fill(workerScore, -1);
		Arrays.fill(sessionDuration, -1);
		Arrays.fill(workerIDKPercentage, -1);
	}
	
	public void setExplanationSizeCriteria(int minimum, int maximum)
	{
		if((minimum > maximum) && (maximum != -1))
		{
			System.out.println("The minimum value of the filter cannot be greater than the maximum");
		}
		else{
			this.explanationSize[0] = minimum;
			this.explanationSize[1] = maximum;
		}
	}

	public void setConfidenceCriteria(int minimum, int maximum)
	{
		if((minimum > maximum) && (maximum != -1))
		{
			System.out.println("The minimum value of the filter cannot be greater than the maximum");
		}
		else{
			this.confidence[0] = minimum;
			this.confidence[1] = maximum;
		}
	}

	public void setDifficultyCriteria(int minimum, int maximum)
	{
		if((minimum > maximum) && (maximum != -1))
		{
			System.out.println("The minimum value of the filter cannot be greater than the maximum");
		}
		else{
			this.difficulty[0] = minimum;
			this.difficulty[1] = maximum;
		}
	}

	public void setAnswerDurationCriteria(double minimum, double maximum)
	{
		if((minimum > maximum) && (maximum != -1))
		{
			System.out.println("The minimum value of the filter cannot be greater than the maximum");
		}
		else{
			this.answerDuration[0] = minimum;
			this.answerDuration[1] = maximum;
		}
	}
	
	public void setSessionDurationCriteria(double minimum, double maximum)
	{
		if((minimum > maximum) && (maximum != -1))
		{
			System.out.println("The minimum value of the filter cannot be greater than the maximum");
		}
		else{
			this.sessionDuration[0] = minimum;
			this.sessionDuration[1] = maximum;
		}
	}
	
	public void setWorkerScoreCriteria(int minimum, int maximum)
	{
		if((minimum > maximum) && (maximum != -1))
		{
			System.out.println("The minimum value of the filter cannot be greater than the maximum");
		}
		else{
			this.workerScore[0] = minimum;
			this.workerScore[1] = maximum;
		}
	}
	
	public void setIDKPercentageCriteria(double minimum, double maximum)
	{
		if((minimum > maximum) && (maximum != -1))
		{
			System.out.println("The minimum value of the filter cannot be greater than the maximum");
		}
		else{
			this.workerIDKPercentage[0] = minimum;
			this.workerIDKPercentage[1] = maximum;
		}
	}
	
	/**
	 * Applies the filter on the desired content.
	 * @param content: Desired content to be filtered
	 * @return: The filtered content
	 */
	public Map<String, Microtask> apply(Map<String, Microtask> content)
	{
		Map<String, Microtask> aux = new LinkedHashMap<String, Microtask>();
		aux.putAll(content);
		List<Integer> removeIndex = new ArrayList<Integer>();
		ConsentDTO workerDTO = new FileConsentDTO();
		
		
		for (String questionID : aux.keySet()) {
			Vector<Answer> answerList = aux.get(questionID).getAnswerList();
			for (int i = 0; i < answerList.size(); i++) {				
				Answer answer = answerList.get(i);
				if((explanationSize[0] != -1) || (explanationSize[1] != -1))
				{
					if( (explanationSize[0] != -1) && (answer.getExplanation().length() < explanationSize[0]))
					{
						removeIndex.add(i);
						continue;
					}
					if( (explanationSize[1] != -1) && (answer.getExplanation().length() > explanationSize[1]))
					{
						removeIndex.add(i);
						continue;
					}
				}
				if((confidence[0] != -1) || (confidence[1] != -1))
				{
					if( (confidence[0] != -1) && (answer.getConfidenceOption() < confidence[0]))
					{
						removeIndex.add(i);
						continue;
					}
					if( (confidence[1] != -1) && (answer.getConfidenceOption() > confidence[1]))
					{
						removeIndex.add(i);
						continue;
					}
				}
				if((difficulty[0] != -1) || (difficulty[1] != -1))
				{
					if( (difficulty[0] != -1) && (answer.getDifficulty() < difficulty[0]))
					{
						removeIndex.add(i);
						continue;
					}
					if( (difficulty[1] != -1) && (answer.getDifficulty() > difficulty[1]))
					{
						removeIndex.add(i);
						continue;
					}
				}
				if((answerDuration[0] != -1) || (answerDuration[1] != -1))
				{
					if( (answerDuration[0] != -1) && ((Double.valueOf(answer.getElapsedTime())/1000) < answerDuration[0]))
					{
						removeIndex.add(i);
						continue;
					}
					if( (answerDuration[1] != -1) && ((Double.valueOf(answer.getElapsedTime())/1000) > answerDuration[1]))
					{
						removeIndex.add(i);
						continue;
					}
				}
				if((workerScore[0] != -1) || (workerScore[1] != -1) )
				{
					
					Map<String, Worker> workers = workerDTO.getWorkers();
					Worker worker = workers.get(answer.getWorkerId());
					//if(worker==null)
						//System.out.println("worker null answer.getWorkerId"+answer.getWorkerId());
					if(worker!=null && (workerScore[0] != -1) && (worker.getGrade() < workerScore[0]))
					{
						removeIndex.add(i);
						continue;
					}
					if( worker!=null && (workerScore[1] != -1) && (worker.getGrade() > workerScore[1]))
					{
						removeIndex.add(i);
						continue;
					}
				}
				if((sessionDuration[0] != -1) || (sessionDuration[1] != -1) )
				{
					calculateSessionsDuration();
					if( (sessionDuration[0] != -1) && (sessionDurationMap.get(questionID+":"+answer.getWorkerId()) < sessionDuration[0]))
					{
						removeIndex.add(i);
						continue;
					}
					if( (sessionDuration[1] != -1) && (sessionDurationMap.get(questionID+":"+answer.getWorkerId()) > sessionDuration[1]))
					{
						removeIndex.add(i);
						continue;
					}
				}
				if((workerIDKPercentage[0] != -1) || (workerIDKPercentage[1] != -1))
				{
					calculateWorkersIDK();
					if( (workerIDKPercentage[0] != -1) && (workerIDKMap.get(answer.getWorkerId()) < workerIDKPercentage[0]))
					{
						removeIndex.add(i);
						continue;
					}
					if( (workerIDKPercentage[1] != -1) && (workerIDKMap.get(answer.getWorkerId()) > workerIDKPercentage[1]))
					{
						removeIndex.add(i);
						continue;
					}
				}
			}
			Collections.reverse(removeIndex);
			for (Integer index : removeIndex) {
				Microtask question = content.get(questionID);
				question.getAnswerList().removeElementAt(index);
			}
			removeIndex.clear();
		}
		
		return content;
	}
	
	private void calculateSessionsDuration()
	{
		if(sessionDurationMap.size() == 0)
		{
			FileSessionDTO dto = new FileSessionDTO();
			Map<String, WorkerSession> sessions = dto.getSessions();
			for (WorkerSession session : sessions.values()) {
				int total = 0;
				for (Microtask question : session.getMicrotaskList()) {
					Answer answer = question.getAnswerList().firstElement();
					String firstNumber = answer.getElapsedTime().replaceFirst(".*?(\\d+).*", "$1");
					total += (Integer.valueOf( firstNumber ) / 1000 );
				}
				for (Microtask question : session.getMicrotaskList()) {
					Answer answer = question.getAnswerList().firstElement();
					sessionDurationMap.put((question.getID().toString() + ":" + answer.getWorkerId()), total );
				}
			}
		}
	}
	
	private void calculateWorkersIDK()
	{
		if(workerIDKMap.size() == 0)
		{
			FileSessionDTO sessionDTO = new FileSessionDTO();
			Map<String, Microtask> questions = sessionDTO.getMicrotasks();
			Map<String, Integer[]> aux = new HashMap<String, Integer[]>();
			for (Microtask question : questions.values()) {
				for (Answer answer : question.getAnswerList()) {
					Integer[] values = null;
					if(aux.containsKey(answer.getWorkerId()))
						values = aux.get(answer.getWorkerId());
					else
					{
						values = new Integer[2];
						Arrays.fill(values, 0);
					}
					values[0] += (answer.getOption().equals(Answer.I_DONT_KNOW)) ? 1 : 0;
					values[1]++;
					aux.put(answer.getWorkerId(), values);
				}
			}
			for (String workerID : aux.keySet()) {
				Integer[] values = aux.get(workerID);
				Double percentage = 0.0;
				if(values[1] != 0)
					percentage = (((double)(100 * values[0])) / values[1]);
				workerIDKMap.put(workerID, percentage);
			}
		}
	}
}
