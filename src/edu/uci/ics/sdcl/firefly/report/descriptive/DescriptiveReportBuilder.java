package edu.uci.ics.sdcl.firefly.report.descriptive;


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
public abstract class DescriptiveReportBuilder {

	/**
	 * Responsible for building the HeaderReport,
	 * the blue part of the tables. 
	 * @return: The HeaderReport containing all the necessary data.
	 */
	protected HeaderReport buildHeaderReport()
	{
		throw new UnsupportedOperationException("The method buildHeaderReport is not implemented yet");
	}
	
	/**
	 * Responsible for building the AnswerReport,
	 * the green parts of the tables.
	 * @return: The AnswerReport containing the filtered data for the report type X
	 */
	protected abstract AnswerReport buildAnswerReport();
	
	/**
	 * Responsible for building the CountReport,
	 * the yellow parts of the tables.
	 * @return: The CountReport containing the filtered data for the report type X
	 */
	protected abstract CountReport buildCountReport();
	
	/**
	 * Responsible for building the CorrectnessReport,
	 * the orange parts of the tables.
	 * @return: The CorrectnessReport containing the filtered data for the report type X
	 */
	protected abstract CorrectnessReport buildCorrectnessReport();
	
	/**
	 * This is the service in which will provide the desired
	 * DescritiveReport for a external agent.
	 * @return: The built DescriptveReport with all parts assembled.
	 */
	public abstract DescriptiveReport getDescriptiveReport();
}
