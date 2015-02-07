package edu.uci.ics.sdcl.firefly.report;

import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.report.LogAnalysis.Counter;

public class FirstAnswersAnalysis {

	LogData data;
	
	public FirstAnswersAnalysis(LogData data){
		this.data = data;
		//counterMap = new HashMap<String,Counter>(); 
		//bugReportResultMap = new HashMap<String, Result>();
	}
	
	//Compute first answers maps
	public void computeFirstAnswersFromWorkers(){
		
	}

	
	private static FirstAnswersAnalysis initializeLogs(){
		boolean rejectNoisyWorkers = false;
		double minimalDuration = 0;
		LogData data = new LogData(false, 0);	
		
		String samsungPath = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\RawDataLogs\\";
		String dellPath = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\RawDataLogs\\";
		String path = samsungPath;
		
		data.processLogProduction1(path);
		data.processLogProduction2(path);

		data.printSummary();
		
		FirstAnswersAnalysis firstData = new FirstAnswersAnalysis(data);
		return firstData;
	}
	
	
	public static void main(String[] args){

		FirstAnswersAnalysis firstData = initializeLogs();

		String path = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\DataAnalysis\\BaseDataInTime\\questionType\\";
			// "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\1.Fall2014-Experiments\\DataAnalysis\\BaseDataInTime\\combined123\\";

		Double minDuration = new Double(0.00);
		Double maxDuration = new Double(10000.00);
		int minScore;
		int maxScore;
		int maxICT;
		int minICT;
		String fileName;
		int firstAnswe;
		
		//firstData.printToFile(path+fileName, firstData.writeAnswerLabels_Filtered_by_DURATION_GRADE_IDK( minDration, maxDuration, minScore,maxScore,maxICT,minICT,firstAnswers));
	}
	
}
