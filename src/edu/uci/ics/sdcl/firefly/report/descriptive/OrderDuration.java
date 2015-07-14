package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

public class OrderDuration extends AnswerReport {

	private final Integer QUESTIONS_PER_SESSION;
	private Map<String, Integer[]> answerOrder = new LinkedHashMap<String, Integer[]>();
	private Map<String, Integer[]> length = new LinkedHashMap<String, Integer[]>();
	
	public OrderDuration() {
		PropertyManager property = PropertyManager.initializeSingleton();
		this.QUESTIONS_PER_SESSION = property.microtasksPerSession;
	}
	
	@Override
	public String getType() {
		return "Average duration of answers agrouped by order in the session";
	}

	@Override
	protected String reportData(Answer answer) {	
		return answer.getElapsedTime();
	}
	
	@Override
	public Map<String, List<String>> generateReport( Map<String, List<String>> content) {
		List<String> questionIDList = content.get("Question ID"); // this is the data that came form the HeaderReport
		
		if(questionIDList != null) // auto defense, just to make sure I don't get a null pointer exception
		{
			obtainMicrotasksDurationByOrder();
			
			// NOW CALCULATES THE AVERAGE AND PUT ON REPORT
			for (int i = 0; i < QUESTIONS_PER_SESSION; i++) {
				List<String> column = new ArrayList<String>();
				for (String questionID : questionIDList) {
					int sum = answerOrder.get(questionID)[i];
					int size = length.get(questionID)[i];
					double average = (sum == 0) ? 0 : (sum / size);
					column.add((average == 0.0) ? "" : (average + ""));
				}
				content.put("Answer order " + (i+1), column);
			}
		}
		else
		{
			System.out.println("Invalid question ID came from the HeaderReport");
			System.exit(0);
		}
		return content;
	}
	
	private void obtainMicrotasksDurationByOrder()
	{
		SessionDTO dto = new FileSessionDTO();
		Map<String, WorkerSession> sessions = dto.getSessions();
		for (WorkerSession session : sessions.values()) {
			Vector<Microtask> questions = session.getMicrotaskList();
			for (int i = 0; i < questions.size(); i++) {
				Integer[] order = answerOrder.get(questions.get(i).getID());
				if(order == null)
				{
					order = new Integer[QUESTIONS_PER_SESSION];
					Arrays.fill(order, 0);
					String number = reportData(questions.get(i).getAnswerList().elementAt(0));
					String firstNumber = number.replaceFirst(".*?(\\d+).*", "$1");
					order[i] += Integer.valueOf(firstNumber);
					answerOrder.put(questions.get(i).getID().toString(), order);
					Integer[] size = new Integer[QUESTIONS_PER_SESSION];
					Arrays.fill(size, 0);
					size[i]++;
					length.put(questions.get(i).getID().toString(), size);
				}
				else
				{
					String number = reportData(questions.get(i).getAnswerList().elementAt(0));
					String firstNumber = number.replaceFirst(".*?(\\d+).*", "$1");
					order[i] += Integer.parseInt(firstNumber);
					Integer[] size = length.get(questions.get(i).getID());
					size[i]++;
				}
			}
		}
	}

}