package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This holds the complete descriptive report table,
 * whit all parts assembled on it. 
 * 
 * @author igMoreira
 *
 */
public class DescriptiveReport {

	
	/**
	 * Holds all the contents of the report.
	 */
	private Map<String, List<String>> table = new LinkedHashMap<String, List<String>>();
	
	private HeaderReport headerReport = null;
	private AnswerReport answerReport = null;
	private CountReport countReport = null;
	private CorrectnessReport correctnessReport = null;
	private DescriptiveReportWriter exporter;
	private String[] type;
	
	public DescriptiveReport( HeaderReport headerReport, AnswerReport answerReport,
			CountReport countReport, CorrectnessReport correctnessReport,
			DescriptiveReportWriter exporter) {
		this.headerReport = headerReport;
		this.answerReport = answerReport;
		this.countReport = countReport;
		this.correctnessReport = correctnessReport;
		this.exporter = exporter;
		this.type = new String[]{answerReport.getType(), null};
		
		Map<String, List<String>> content = new LinkedHashMap<String, List<String>>();
		content.putAll(headerReport.getContent());
		content.putAll(answerReport.getContent());
		content.putAll(correctnessReport.getContent());
		this.table = content;
	}
	
	public Map<String, List<String>> getTable() {
		return table;
	}
	
	/**
	 * Calls the ReportWrite so the content
	 * of this report will be written in an
	 * external source, an excel sheet for instance
	 */
	public void exportReport()
	{
		exporter.exportReport(this);
	}

	
	public String[] reportTypes()
	{
		return type;
	}
	
	public int getHeaderReportSize()
	{
		return this.headerReport.getContent().size();
	}
	
	public int getAnswerReportSize()
	{
		return this.answerReport.getContent().size();
	}
	
	public int getCountReportSize()
	{
		return this.countReport.getContent().size();
	}
	
	public int getCorrectnessSize()
	{
		return this.correctnessReport.getContent().size();
	}
}
