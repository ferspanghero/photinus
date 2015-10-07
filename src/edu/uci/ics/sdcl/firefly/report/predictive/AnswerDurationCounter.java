package edu.uci.ics.sdcl.firefly.report.predictive;

import java.util.HashMap;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileSessionDTO;

/**
 * Prints the duration of answers and categorized them as first,second, or third in each worker session.
 * Also prints the answers option (Yes, No, IDK).
 * 
 * The goal is to investigate whether the duration of answers are different depending on answer option.
 * 
 * @author adrianoc
 *
 */
public class AnswerDurationCounter {

		
	public void printCompletedSessions(){
		FileSessionDTO sessionDTO = new FileSessionDTO();
		HashMap<String, WorkerSession> sessionMap = (HashMap<String, WorkerSession>) sessionDTO.getSessions();


		int completeSessions=0;
		int oneAnswer=0;
		int twoAnswer=0;
		int zeroAnswer=0;
		for(WorkerSession session: sessionMap.values()){
			int answers=0;
			Vector<Microtask> taskList = session.getMicrotaskList();
			for(Microtask task:taskList){
				answers = answers + task.getNumberOfAnswers();
			}

			if(answers==3)
				completeSessions++;
			else
				if(answers==1)
					oneAnswer++;
				else
					if(answers==2)
						twoAnswer++;
					else
						if(answers==0)
							zeroAnswer++;

		}

		System.out.println("CompleteSession: "+completeSessions+", "
				+ "oneAnswer: "+oneAnswer+", "
				+ "twoAnswer: "+twoAnswer+", "
				+ "zeroAnswer: "+zeroAnswer);
	}


	public static void main (String args[]){

	}
	
}
