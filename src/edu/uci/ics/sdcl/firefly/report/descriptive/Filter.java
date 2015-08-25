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
	private double[] yearsOfExperience = new double[2];

	private double[] FirstAnswerDuration = new double[2];
	private double[] SecondThirdAnswerDuration = new double[2];


	/** map of professions to be accepted */
	private HashMap<String, String> workerProfessionMap = new HashMap<String, String> (); //Hobbyist, Professional Programmer, Undergrad, Graduate, Others

	/** this is a list of scores that must be excluded */
	private HashMap<String, Integer> workerScoreExclusionMap = new HashMap<String,Integer>();

	private  Map<String, Integer> sessionDurationMap = new HashMap<String, Integer>();
	private  Map<String, Double> workerIDKMap = new HashMap<String, Double>();



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

	public HashMap<String, String>  getWorkerProfessionMap(){
		return workerProfessionMap;
	}

	public Filter() {
		Arrays.fill(explanationSize, -1);
		Arrays.fill(confidence, -1);
		Arrays.fill(difficulty, -1);
		Arrays.fill(answerDuration, -1);
		Arrays.fill(workerScore, -1);
		Arrays.fill(sessionDuration, -1);
		Arrays.fill(workerIDKPercentage, -1);
		Arrays.fill(yearsOfExperience,-1.0);
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
			this.workerScore[0] 	= minimum;
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

	public void setWorkerScoreExclusionList(int [] exclusionList)
	{
		for(int score : exclusionList){
			this.workerScoreExclusionMap.put(new Integer(score).toString(), score);
		}
	}

	public void setWorkerProfessionMap(String[] list) 
	{
		for(String profession : list){			
			this.workerProfessionMap.put(profession, profession);
		}
	}

	public void setYearsOfExperience(double minimum, double maximum)
	{
		if((minimum > maximum) && (maximum != -1.0))
		{
			System.out.println("The minimum value of the filter cannot be greater than the maximum");
		}
		else{
			this.yearsOfExperience[0] 	= minimum;
			this.yearsOfExperience[1] = maximum;
		}
	}

	/**
	 * Applies the filter on the desired content.
	 * @param content: Desired content to be filtered
	 * @return: The filtered content
	 */
	public Map<String, Microtask> apply(HashMap<String, Microtask> microtaskMap)
	{
		Map<String, Microtask> content = (Map<String, Microtask>)  microtaskMap.clone();
		Map<String, Microtask> aux = new LinkedHashMap<String, Microtask>();
		aux.putAll(content);
		List<Integer> removeIndex = new ArrayList<Integer>();	
		FileConsentDTO workerDTO = new FileConsentDTO();
		HashMap<String,Worker> workerMap = workerDTO.getWorkers();
		for (String questionID : aux.keySet()) {
			Vector<Answer> answerList = aux.get(questionID).getAnswerList();
			for (int i = 0; i < answerList.size(); i++) {				
				Answer answer = answerList.get(i);
				String workerID = answer.getWorkerId();
				Worker worker = workerMap.get(workerID);
				if(worker==null){ //Does not accept answers from workers who are not in the ConsentLog.
					System.out.print("Worker not in ConsentLog: "+ workerID);				
				}else{
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
					if((FirstAnswerDuration[0] != -1) || (FirstAnswerDuration[1] != -1))
					{
						if(answer.getOrderInWorkerSession()==1){
							if( (FirstAnswerDuration[0] != -1) && ((Double.valueOf(answer.getElapsedTime())/1000) < FirstAnswerDuration[0]))
							{
								System.out.println("removing first, duration:"+FirstAnswerDuration[0]+":value: "+Double.valueOf(answer.getElapsedTime())/1000);
								removeIndex.add(i);
								continue;
							}
							if( (FirstAnswerDuration[1] != -1) && ((Double.valueOf(answer.getElapsedTime())/1000) > FirstAnswerDuration[1]))
							{
								removeIndex.add(i);
								continue;
							}
						}
					}
					if((SecondThirdAnswerDuration[0] != -1) || (SecondThirdAnswerDuration[1] != -1))
					{
						if(answer.getOrderInWorkerSession()==2 || answer.getOrderInWorkerSession()==3){
							if( (SecondThirdAnswerDuration[0] != -1) && ((Double.valueOf(answer.getElapsedTime())/1000) < SecondThirdAnswerDuration[0]))
							{
								System.out.println("removing second/third, duration:"+SecondThirdAnswerDuration[0]);
								removeIndex.add(i);
								continue;
							}
							if( (SecondThirdAnswerDuration[1] != -1) && ((Double.valueOf(answer.getElapsedTime())/1000) > SecondThirdAnswerDuration[1]))
							{
								removeIndex.add(i);
								continue;
							}
						}
					}
					if((workerScore[0] != -1) || (workerScore[1] != -1) )
					{
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
						if(worker!=null && this.workerScoreExclusionMap.containsKey(worker.getGrade().toString()) )
						{
							removeIndex.add(i);
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
							System.out.println("min IDK: "+ workerIDKPercentage[0] +" Removig worker: "+answer.getWorkerId());
							removeIndex.add(i);
							continue;
						}
						if( (workerIDKPercentage[1] != -1) && (workerIDKMap.get(answer.getWorkerId()) > workerIDKPercentage[1]))
						{
							System.out.println("max IDK: "+ workerIDKPercentage[1] +" Removig worker: "+answer.getWorkerId());
							removeIndex.add(i);
							continue;
						}
					}
					if(workerProfessionMap.size()>0)
					{
						String workerProfession = worker.getSurveyAnswer("Experience");
						if(workerProfession.contains("Other"))
							workerProfession = "Other";
						if( (workerProfessionMap.containsKey(workerProfession)))
						{
							System.out.println("profession: "+ workerProfession +" Removig worker: "+answer.getWorkerId());
							removeIndex.add(i);
							continue;
						}
						if( (workerProfessionMap.containsKey(workerProfession)))
						{
							System.out.println("profession: "+ workerProfession +" Removig worker: "+answer.getWorkerId());
							removeIndex.add(i);
							continue;
						}
					}
					//System.out.println(yearsOfExperience[0] + ": "+yearsOfExperience[1]);
					if((yearsOfExperience[0] != -1.0) || (yearsOfExperience[1] != -1.0) )
					{

						Double workerYearsOfExperience = new Double(worker.getSurveyAnswer("YearsProgramming"));
						//	System.out.println(workerYearsOfExperience);

						if(worker!=null && (yearsOfExperience[0] != -1) && ( workerYearsOfExperience.doubleValue()< yearsOfExperience[0]))
						{
							removeIndex.add(i);
							continue;
						}
						if( worker!=null && (yearsOfExperience[1] != -1) && (workerYearsOfExperience.doubleValue() > yearsOfExperience[1]))
						{
							removeIndex.add(i);
							continue;
						}
					}			
				}

			}//for


			Collections.reverse(removeIndex);
			for (Integer index : removeIndex) {
				Microtask question = content.get(questionID);
				question.getAnswerList().removeElementAt(index);
			}
			removeIndex.clear();
		}//for		
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
				if(values[1] != 0){
					percentage = (((double)(100 * values[0])) / values[1]);
					//System.out.println("IDK percentage: "+ percentage );
				}
				workerIDKMap.put(workerID, percentage);
			}
		}
	}

	public void FirstAnswerDurationCriteria(double minD, double maxD) {
		{
			if((minD > maxD) && (maxD != -1.0))
			{
				System.out.println("The minimum value of the filter cannot be greater than the maximum");
			}
			else{
				this.FirstAnswerDuration[0] = minD;
				this.FirstAnswerDuration[1] = maxD;
			}
		}
	}

	public void setSecondThirdAnswerDurationCriteria(double minD,
			double maxD) {
		{
			if((minD > maxD) && (maxD != -1.0))
			{
				System.out.println("The minimum value of the filter cannot be greater than the maximum");
			}
			else{
				this.SecondThirdAnswerDuration[0] = minD;
				this.SecondThirdAnswerDuration[1] = maxD;
			}
		}

	}


}
