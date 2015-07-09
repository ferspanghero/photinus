package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

public class AnswerOption extends AnswerReport {
	
	private final int NUMBER_OF_ANSWERS;
	
	 public AnswerOption() {
		PropertyManager properties = PropertyManager.initializeSingleton();
		this.NUMBER_OF_ANSWERS = properties.answersPerMicrotask;
	}

	@Override
	public HashMap<String, List<String>> generateReport( HashMap<String, List<String>> content,	HashMap<String, Microtask> microtasks) {
		
		List<String> questionIDList = content.get("Question ID"); // this is the data that came form the HeaderReport
		
		if(questionIDList != null) // auto defense, just to make sure I don't get a null pointer exception
		{
			List<String> answers = null;
			for (int i = 0; i < NUMBER_OF_ANSWERS; i++) { // The maximum number of answers showed on the report
				answers = new ArrayList<String>();
				for (String questionID : questionIDList) { // for each microtask get the answer number i
					Microtask microtask = microtasks.get(questionID);
					List<Answer> answerList = microtask.getAnswerList();
					if((answerList.size()-1) >= i)
					{
						Answer answer = microtask.getAnswerList().get(i);
						answers.add(answer.getOption());
					}
					else
					{
						answers.add("");
					}
				}
				content.put("Answer "+(i+1), answers);
			}
		}
		else
		{
			System.out.println("Invalid question ID came from the HeaderReport");
			System.exit(0);
		}
		return content;
	}

}
