package edu.uci.ics.sdcl.firefly.report.descriptive;

public class ReportApplication {

	public static void main(String[] args) {
		DescriptiveReportBuilder builder = new DescriptiveReportBuilder();
		builder.setAnswerReport(new AnswerOption());
		DescriptiveReport report = builder.generateDescriptiveReport();
		report.exportReport(); // THIS SHOULD EXPORT ON AN EXCEL SPREADSHEET
	}
	
}
