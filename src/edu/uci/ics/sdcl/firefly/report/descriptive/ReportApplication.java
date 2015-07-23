package edu.uci.ics.sdcl.firefly.report.descriptive;


public class ReportApplication {

	private static DescriptiveReportBuilder builder;
	
	public static void main(String[] args) {
		difficultyLevelReport(new ExcelExporter());
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
//		builder = new DescriptiveReportBuilder(new AnswerDuration(), new , correctness, exporter)
	}
	
}
