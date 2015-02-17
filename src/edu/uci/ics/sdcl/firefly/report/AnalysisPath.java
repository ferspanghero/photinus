package edu.uci.ics.sdcl.firefly.report;

public class AnalysisPath {

	public  String samsungPath = "C:\\Users\\adrianoc\\Dropbox (PE-C)\\3.Research\\2.Fall2014-Experiments\\";
	public  String dellPath = "C:\\Users\\Christian Adriano\\Dropbox (PE-C)\\3.Research\\2.Fall2014-Experiments\\";
	public  String currentPath = samsungPath;
	
	public  String rawDataLogPath;
	public  String calculationsPath;
	public  String analysisTypePath;
	
	private static AnalysisPath analysisPath;
	
	private AnalysisPath(){
		this.rawDataLogPath = currentPath + "\\RawDataLogs\\";
		this.calculationsPath = currentPath + "\\Calculations\\";
		this.analysisTypePath = currentPath + "\\1.DataAnalysis(CutFirst)\\";
	}
	
	public static AnalysisPath getInstance(){
		if(analysisPath==null)
			analysisPath =  new AnalysisPath();
		return analysisPath;
	}
	
}
