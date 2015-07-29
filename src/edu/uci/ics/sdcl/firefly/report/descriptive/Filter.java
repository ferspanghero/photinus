package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;

/**
 * Responsible for filter a collection of Sessions.
 * 
 * @author igMoreira
 *
 */
public class Filter {
	
	private int[] explanationSize = new int[2];
	private boolean explanationFlag = false;
	private int[] confidence = new int[2];
	private boolean confidenceFlag = false;
	private int[] difficulty = new int[2];
	private boolean difficultyFlag = false;
	private double[] answerDuration = new double[2];
	private boolean answerFlag = false;
	
	public Filter() {
		Arrays.fill(explanationSize, -1);
		Arrays.fill(confidence, -1);
		Arrays.fill(difficulty, -1);
		Arrays.fill(answerDuration, -1);
	}
	
	public void setExplanationSizeCriteria(int minimum, int maximum)
	{
		if(minimum > maximum)
		{
			System.out.println("The minimum value of the filter cannot be greater than the maximum");
		}
		else{
			this.explanationSize[0] = minimum;
			this.explanationSize[1] = maximum;
			this.explanationFlag = true;
		}
	}

	public void setConfidenceCriteria(int minimum, int maximum)
	{
		if(minimum > maximum)
		{
			System.out.println("The minimum value of the filter cannot be greater than the maximum");
		}
		else{
			this.confidence[0] = minimum;
			this.confidence[1] = maximum;
			this.confidenceFlag = true;
		}
	}

	public void setDifficultyCriteria(int minimum, int maximum)
	{
		if(minimum > maximum)
		{
			System.out.println("The minimum value of the filter cannot be greater than the maximum");
		}
		else{
			this.difficulty[0] = minimum;
			this.difficulty[1] = maximum;
			this.difficultyFlag = true;
		}
	}

	public void setAnswerDurationCriteria(double minimum, double maximum)
	{
		if(minimum > maximum)
		{
			System.out.println("The minimum value of the filter cannot be greater than the maximum");
		}
		else{
			this.answerDuration[0] = minimum;
			this.answerDuration[1] = maximum;
			this.answerFlag = true;
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
		
		for (String questionID : aux.keySet()) {
			Vector<Answer> answerList = aux.get(questionID).getAnswerList();
			for (int i = 0; i < answerList.size(); i++) {				
				Answer answer = answerList.get(i);
				if(explanationFlag)
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
				if(confidenceFlag)
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
				if(difficultyFlag)
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
				if(answerFlag)
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
}
