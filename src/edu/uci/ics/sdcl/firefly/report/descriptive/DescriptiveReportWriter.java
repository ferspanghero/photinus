package edu.uci.ics.sdcl.firefly.report.descriptive;

/**
 * Gets a descriptive report an writes that to 
 * an external source, a file, an excel sheet for
 * instance.
 * 
 * @author igMoreira
 *
 */
public interface DescriptiveReportWriter {

	/**
	 * Exports the Descriptive Report to an
	 * external source.
	 */
	public void exportReport(DescriptiveReport report);
	
	public void setReportName(String reportName);

}
