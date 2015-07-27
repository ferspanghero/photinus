package edu.uci.ics.sdcl.firefly.report.descriptive;


public class ReportApplication {

	private static DescriptiveReportBuilder builder;
	
	public static void main(String[] args) {
		answerDurationReport(new ExcelExporter());
		DescriptiveReport report = builder.generateDescriptiveReport();
		report.exportReport(); // THIS SHOULD EXPORT ON AN EXCEL SPREADSHEET
	}
	
	private static void answerOptionReport(DescriptiveReportWriter exporter)
	{
		builder = new DescriptiveReportBuilder(new AnswerOption(), null , new RegularCorrectness(), exporter);
	}
	
	private static void confidenceLevelReport(DescriptiveReportWriter exporter)
	{
		builder = new DescriptiveReportBuilder(new AnswerConfidence(), new ConfidenceCounting(), new RegularCorrectness(), exporter);
	}
	
	private static void difficultyLevelReport(DescriptiveReportWriter exporter)
	{
		builder = new DescriptiveReportBuilder(new AnswerDifficulty(), new ConfidenceCounting(), new RegularCorrectness(), exporter);
	}
	
	private static void answerDurationReport(DescriptiveReportWriter exporter)
	{
		//TODO: the count report and the correctness report are different
//		throw new UnsupportedOperationException("The answerDuration report is not implemented yet");
		builder = new DescriptiveReportBuilder(new AnswerDuration(), new AverageCount(), new RegularCorrectness(), exporter);
	}
	
	private static void sizeOfExplanationReport(DescriptiveReportWriter exporter)
	{
		//TODO: the count report and the correctness report are different
		throw new UnsupportedOperationException("The sizeOfExplanation report is not implemented yet");
//		builder = new DescriptiveReportBuilder(new AnswerExplanationSize(), counter, correctness, exporter);
	}
	
	private static void durationOfAnswersAgroupedByOrder(DescriptiveReportWriter exporter)
	{
		//TODO: Verify the count report and the correctness details
		throw new UnsupportedOperationException("The durationByOrder report is not implemented yet");
//		builder = new DescriptiveReportBuilder(new OrderDuration(), counter, correctness, exporter);
	}
	
	private static void sessionDurationReport(DescriptiveReportWriter exporter)
	{
		//TODO: Verify the count report and the correctness details
		throw new UnsupportedOperationException("The sessionDuration report is not implemented yet");
//		builder = new DescriptiveReportBuilder(new SessionDuration(), counter, correctness, exporter);
	}
	
	private static void workerScoreReport(DescriptiveReportWriter exporter)
	{
		builder = new DescriptiveReportBuilder(new WorkerScoreReport(), new AverageWorkerProfileReport(), new AverageCorrectness(), exporter);
	}
	
	private static void workerDemographicsReport()
	{
		new WorkerDemographicsReport();
	}
	
}
