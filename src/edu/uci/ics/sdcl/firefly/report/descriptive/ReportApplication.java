package edu.uci.ics.sdcl.firefly.report.descriptive;

public class ReportApplication {

	public static void main(String[] args) {
		DescriptiveReportBuilder builder = new DescriptiveReportBuilder(new AnswerConfidence(), null, null, new CSVExporter());
		DescriptiveReport report = builder.generateDescriptiveReport();
		report.exportReport(); // THIS SHOULD EXPORT ON AN EXCEL SPREADSHEET
	}
	
}
