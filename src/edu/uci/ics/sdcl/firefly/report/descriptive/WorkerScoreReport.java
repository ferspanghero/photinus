package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.Iterator;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Worker;

public class WorkerScoreReport extends AnswerReport{
	
	String consentLogpath = "C:/Users/igMoreira/Desktop/Dropbox/1.CrowdDebug-Summer2015/sampleDatalogs/pilot2/consent-Pilot2-log.txt";
	
	@Override
	public String getType() {
		return "Workers Scores for each answer";
	}

	@Override
	protected String reportData(Answer answer) {
		FileConsentDTO fc = new FileConsentDTO(consentLogpath);

		Iterator<Worker> workersIter = fc.getWorkers().values().iterator();
		while(workersIter.hasNext()){
			Worker worker = workersIter.next();
			if(worker.getWorkerId().compareTo(answer.getWorkerId())==0){
				return worker.getGrade().toString();
			}
		}
		return null;
	}

}
