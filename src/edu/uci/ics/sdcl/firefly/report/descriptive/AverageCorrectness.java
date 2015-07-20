package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;

public class AverageCorrectness extends CorrectnessReport {
	
	String consentLogpath = "C:/var/lib/tomcat7/webapps/consent-log.log";

	@Override
	public Map<String, List<String>> generateReport(
			Map<String, List<String>> headerContent,
			Map<String, List<String>> answerReport) {
		
		SessionDTO database = new FileSessionDTO();
		Map<String, Microtask> microtasks = database.getMicrotasks();
		List<String> questionIDList = headerContent.get("Question ID"); // this is the data that came form the HeaderReport
		int truePositive = 0;
		int trueNegative = 0;
		int falsePositive = 0;
		int falseNegative = 0;
		int idk=0;
		List<String> averageTP = new ArrayList<String>();
		List<String> averageTN = new ArrayList<String>();
		List<String> averageFP = new ArrayList<String>();
		List<String> averageFN = new ArrayList<String>();


		for (String questionID : questionIDList) {
			List<String> workerIDs = microtasks.get(questionID).getWorkerIds();
			if(workerIDs != null){
				
				for (String workerID : workerIDs) {
					FileConsentDTO fc = new FileConsentDTO(consentLogpath);
					Iterator<Worker> workersIter = fc.getWorkers().values().iterator();
					while (workersIter.hasNext()) {
						Worker worker = workersIter.next();
						if(worker.getWorkerId().compareTo(workerID)==0){
							Microtask microtask = microtasks.get(questionID);
							Answer workerAnswer = microtasks.get(questionID).getAnswerByUserId(workerID);
							String answerOption = workerAnswer.getOption();
							boolean bugCovering = isBugCovering(microtask.getID());
							if(answerOption!=null){
								if (bugCovering && answerOption.equals(Answer.YES)) {
									truePositive++;
								} else if (!bugCovering && answerOption.equals(Answer.NO)) {
									trueNegative++;
								} else if (!bugCovering && answerOption.equals(Answer.YES)) {
									falsePositive++;
								} else if (bugCovering && answerOption.equals(Answer.NO)) {
									falseNegative++;
								} else {
									idk++;
								}
							}
						}
					}
				}
				averageTP.add(String.format("%.2f",((float) truePositive)/workerIDs.size()));
				averageTN.add(String.format("%.2f",((float) trueNegative)/workerIDs.size()));
				averageFP.add(String.format("%.2f",((float)falsePositive)/workerIDs.size()));
				averageFN.add(String.format("%.2f",((float) falseNegative)/workerIDs.size()));
			}
			this.correctnessContent.put("Average worker score of True Positives", averageTP);
			this.correctnessContent.put("Average worker score of True Negatives", averageTN);
			this.correctnessContent.put("Average worker score of False Positives", averageFP);
			this.correctnessContent.put("Average worker score of False Negatives", averageFN);
		}
		return correctnessContent;
	}
		
		public boolean isBugCovering(int microtaskID){
			Integer[] bugCoveringQuestions = {72,73,78,79,84,92,95,97,102,104,119,123,126};
			for(int i=0; i<bugCoveringQuestions.length; i++){
				if(microtaskID == bugCoveringQuestions[i]){
					return true;
				}
			}
			return false;
		}
		
		public String getType() {
			return "Correct/Wrong Answers";
		}

}
