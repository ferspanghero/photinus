package edu.uci.ics.sdcl.firefly.report.descriptive;

public class ReportApplication {

	public static void main(String[] args) {
		DescriptiveReportBuilder builder = new DescriptiveReportBuilder();
		builder.setAnswerReport(new AnswerDifficulty());
		DescriptiveReport report = builder.generateDescriptiveReport();
		for (String key : report.getTable().keySet()) {
			System.out.println(key);
		}
		report.exportReport(); // THIS SHOULD EXPORT ON AN EXCEL SPREADSHEET
	}
	
}
