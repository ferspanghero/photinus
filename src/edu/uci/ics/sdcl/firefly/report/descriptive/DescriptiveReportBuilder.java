package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.List;
import java.util.Map;


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

	private HeaderReport header;
	private AnswerReport answers;
	private CountReport counters;
	private CorrectnessReport correctness;
	private DescriptiveReportWriter exporter;
	String reportName = "";
	
	public DescriptiveReportBuilder(AnswerReport answer, CountReport counter, CorrectnessReport correctness, DescriptiveReportWriter exporter) {
		this.answers = answer;
		this.counters = counter;
		this.correctness = correctness;
		this.exporter = exporter;
	}
	
	public void setFilter(Filter filter)
	{
		FileSessionDTO dto = new FileSessionDTO();
		dto.setFilter(filter);
		StringBuilder filterName = new StringBuilder();
		filterName.append("answerDuration[ "+ filter.getAnswerDuration()[0] + " " + filter.getAnswerDuration()[1]+ " ] ");
		filterName.append("explanationSize[ "+ filter.getExplanationSize()[0] + " " + filter.getExplanationSize()[1]+ " ] ");
		filterName.append("confidence[ "+ filter.getConfidence()[0] + " " + filter.getConfidence()[1]+ " ] ");
		filterName.append("difficulty[ "+ filter.getDifficulty()[0] + " " + filter.getDifficulty()[1]+ " ] ");
		filterName.append("workerScore[ "+ filter.getWorkerScore()[0] + " " + filter.getWorkerScore()[1]+ " ] ");
		filterName.append("sessionDuration[ "+ filter.getSessionDuration()[0] + " " + filter.getSessionDuration()[1]+ " ] ");
		this.reportName = filterName.toString();
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
	private Map<String, List<String>> buildHeaderReport()
	{
		SessionDTO database = new FileSessionDTO();
		this.header = new HeaderReport(database.getMicrotasks());
		return header.generateReport();
	}
	
	/**
	 * Responsible for building the AnswerReport,
	 * the green parts of the tables.
	 * @return: The AnswerReport containing the filtered data for the report type X
	 */
	private Map<String, List<String>> buildAnswerReport(Map<String, List<String>> content)
	{
		return answers.generateReport(content);
	}
	
	/**
	 * Responsible for building the CountReport,
	 * the yellow parts of the tables.
	 * @return: The CountReport containing the filtered data for the report type X
	 */
	private Map<String, List<String>> buildCountReport(HeaderReport header, AnswerReport answers) {
		return counters.generateReport(header,answers);
	}
	
	/**
	 * Responsible for building the CorrectnessReport,
	 * the orange parts of the tables.
	 * @return: The CorrectnessReport containing the filtered data for the report type X
	 */
	private Map<String, List<String>> buildCorrectnessReport(HeaderReport headerReport, AnswerReport answerReport)
	{
		return this.correctness.generateReport(headerReport, answerReport);
	}
	
	/**
	 * This is the service in which will provide the desired
	 * DescritiveReport for a external agent.
	 * @return: The built DescriptveReport with all parts assembled.
	 */
	public DescriptiveReport generateDescriptiveReport()
	{
		Map<String, List<String>> headerContent = buildHeaderReport();
		buildAnswerReport(headerContent);
		if(this.counters != null)
		{
			buildCountReport(header,answers);
		}
		buildCorrectnessReport(header,answers);
		exporter.setReportName(reportName);
		DescriptiveReport report = new DescriptiveReport(this.header, this.answers, this.counters, this.correctness, exporter);
		return report;
	}

}
