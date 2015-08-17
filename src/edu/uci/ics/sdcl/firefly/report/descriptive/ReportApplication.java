package edu.uci.ics.sdcl.firefly.report.descriptive;


public class ReportApplication {

	private static DescriptiveReportBuilder builder;
	
	public static void main(String[] args) {
		/*Filter filter = new Filter();
		filter.setIDKPercentageCriteria(-1, 50);
		SessionDTO dto = new FileSessionDTO();
		*/
		//answerOptionReport(new ExcelExporter());
		//workerScoreReport(new ExcelExporter());
		workerDemographicsReport();
		//builder.generateDescriptiveReport().exportReport();
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
		builder = new DescriptiveReportBuilder(new AnswerDuration(), new AverageCount(), new AverageCorrectness(), exporter);
	}
	
	private static void sizeOfExplanationReport(DescriptiveReportWriter exporter)
	{
		builder = new DescriptiveReportBuilder(new AnswerExplanationSize(), new AverageCount(), new AverageCorrectness(), exporter);
	}
	
	private static void durationOfAnswersAgroupedByOrder(DescriptiveReportWriter exporter)
	{
		builder = new DescriptiveReportBuilder(new OrderDuration(), new AverageCount(), new AverageCorrectness(), exporter);
	}
	
	private static void sessionDurationReport(DescriptiveReportWriter exporter)
	{
		builder = new DescriptiveReportBuilder(new SessionDuration(), new AverageCount(), new AverageCorrectness(), exporter);
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
