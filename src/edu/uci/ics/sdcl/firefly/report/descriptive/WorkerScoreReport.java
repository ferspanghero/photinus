package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.Iterator;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Worker;

public class WorkerScoreReport extends AnswerReport{
	
	
	FileConsentDTO fc;
	
	public WorkerScoreReport(){
		super();
		fc = new FileConsentDTO();
	}
	
	@Override
	public String getType() {
		return "Workers Scores for each answer";
	}

	@Override
	protected String reportData(Answer answer) {
		
		Iterator<Worker> workersIter = fc.getWorkers().values().iterator();
		while(workersIter.hasNext()){
			Worker worker = workersIter.next();
			if(worker.getWorkerId().compareTo(answer.getWorkerId())==0){
				return worker.getGrade().toString();
			}
		}
		return null;
	}

	@Override
	public String getDataNature() {
		return "Worker Score";
	}

}
