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
		this.type = new String[]{
				((answerReport == null) ? "" : answerReport.getType()),
				((countReport == null) ? "" : countReport.getType()),
				((correctnessReport == null) ? "" :correctnessReport.getType())
				};
		
		Map<String, List<String>> content = new LinkedHashMap<String, List<String>>();
		if(headerReport != null)
			content.putAll(headerReport.getContent());
		if(answerReport != null)
			content.putAll(answerReport.getContent());
		if(countReport != null)
			content.putAll(countReport.getContent());
		if(correctnessReport != null)
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
		int size = 0;
		if(this.headerReport != null)
			size = this.headerReport.getContent().size();
		return size;
	}
	
	public int getAnswerReportSize()
	{
		int size = 0;
		if(this.answerReport != null)
			size = this.answerReport.getContent().size();
		return size;
	}
	
	public int getCountReportSize()
	{
		int size = 0;
		if(this.countReport != null)
			size = this.countReport.getContent().size();
		return size;
	}
	
	public int getCorrectnessSize()
	{
		int size = 0;
		if(correctnessReport != null)
			size = this.correctnessReport.getContent().size();
		return size;
	}
}
