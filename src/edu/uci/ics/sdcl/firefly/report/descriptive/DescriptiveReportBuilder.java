package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.HashMap;
import java.util.List;


/**
 * Provides an interface for different builders.
 * The reports have similarities on the structure,
 * but their content is different, so the parts construction
 * are separated and permitted to vary on concrete implementations
 * of this class.
 *
 * @author igMoreira
 *
 */
public class DescriptiveReportBuilder {

	private AnswerReport answers;
	private CountReport counters;
	private CorrectnessReport correctness;
	
	public DescriptiveReportBuilder(AnswerReport answer, CountReport counter, CorrectnessReport correctness) {
		this.answers = answer;
		this.counters = counter;
		this.correctness = correctness;
	}
	
	public DescriptiveReportBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	public void setAnswerReport(AnswerReport answers) {
		this.answers = answers;
	}

	public void setCounterReport(CountReport counters) {
		this.counters = counters;
	}

	public void setCorrectnessReport(CorrectnessReport correctness) {
		this.correctness = correctness;
	}

	/**
	 * Responsible for building the HeaderReport,
	 * the blue part of the tables. 
	 * @return: The HeaderReport containing all the necessary data.
	 */
	private HashMap<String, List<String>> buildHeaderReport()
	{
		SessionDTO database = new FileSessionDTO();
		HeaderReport header = new HeaderReport(database.getMicrotasks());
		return header.generateReport();
	}
	
	/**
	 * Responsible for building the AnswerReport,
	 * the green parts of the tables.
	 * @return: The AnswerReport containing the filtered data for the report type X
	 */
	private HashMap<String, List<String>> buildAnswerReport(HashMap<String, List<String>> content)
	{
		SessionDTO database = new FileSessionDTO();
		return answers.generateReport(content, database.getMicrotasks());
	}
	
	/**
	 * Responsible for building the CountReport,
	 * the yellow parts of the tables.
	 * @return: The CountReport containing the filtered data for the report type X
	 */
	private HashMap<String, List<String>> buildCountReport()
	{
		throw new UnsupportedOperationException("The method buildCountReport is not implemented yet");
	}
	
	/**
	 * Responsible for building the CorrectnessReport,
	 * the orange parts of the tables.
	 * @return: The CorrectnessReport containing the filtered data for the report type X
	 */
	private HashMap<String, List<String>> buildCorrectnessReport()
	{
		throw new UnsupportedOperationException("The method buildCorrectnessReport is not implemented yet");
	}
	
	/**
	 * This is the service in which will provide the desired
	 * DescritiveReport for a external agent.
	 * @return: The built DescriptveReport with all parts assembled.
	 */
	public DescriptiveReport generateDescriptiveReport()
	{
		HashMap<String, List<String>> content = buildHeaderReport();
		content = buildAnswerReport(content);
		DescriptiveReport report = new DescriptiveReport(content);
		return report;
	}
}
