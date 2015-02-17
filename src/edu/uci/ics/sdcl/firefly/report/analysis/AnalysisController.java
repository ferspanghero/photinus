package edu.uci.ics.sdcl.firefly.report.analysis;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.report.AnalysisPath;
import edu.uci.ics.sdcl.firefly.report.Result;

public class AnalysisController {

	ArrayList<ArrayList<String>> resultsMatix; 
	CutFilter cutFilter;

	ArrayList<FilterContent> filterContentList;
	AnalysisPath analysisPath;

	public AnalysisController(){
		this.analysisPath = AnalysisPath.getInstance();
		resultsMatix = new ArrayList<ArrayList<String>>();
		filterContentList = new ArrayList<FilterContent>(); 
		cutFilter = new CutFilter();
	}

	public void run(){	
		Double maxDuration = new Double(Double.MAX_VALUE);
		Integer[] durationList = {0};//,15,20,30,45,60,120}; //Minimal duration to be considered
		Integer[] scoreList = {2};//,4};  //Minimal Score to be considered		
		Integer[] idkList = {11};// 2,4,6,8,10}; //I Can't Tell answer count that would eliminate workers
		Integer lowerCut_idk = -1;  //Worker that has an equal amount below will be cut out of the set.
		Integer maxScore=5; //Worker has to have grade below that.
		int i=0;

		filterContentList = new ArrayList<FilterContent>(); 		
		HashMap<String, Microtask> cutMap = cutFilter.filterCutMicrotasks(0,1);
		SpreadsheetFactory factory = new SpreadsheetFactory();

		while(i<durationList.length){
			int j=0;
			String durationStr = durationList[i].toString();
			Double duration = new Double(durationList[i].doubleValue()*1000);
			while(j<scoreList.length){
				String scoreStr = scoreList[j].toString();
				int k=0;
				while(k<idkList.length){
					String idkStr = idkList[k].toString();

					FilterContent content = cutFilter.writeAnswerLabels_Filtered_by_DURATION_GRADE_IDK(cutMap, duration, maxDuration, scoreList[j],maxScore,idkList[k],lowerCut_idk);
					String fileName = content.getFilterName() + ".xlsx";
					content.fileName = fileName;
					factory.calculateResults(fileName, content);
					filterContentList.add(content);
					k++;
				}
				j++;
			}
			i++;
		}
		System.out.println("files written, look at: "+analysisPath.calculationsPath);
	}

	public void generateReport(){
		if(this.filterContentList==null || this.filterContentList.size()==0)
			System.out.println("ERROR: there aren't spreadsheets to process the report. Check path: "+analysisPath.calculationsPath);

		SpreadsheetFactory factory = new SpreadsheetFactory();

		//
		for(int i=0; i<this.filterContentList.size();i++){
			FilterContent content = this.filterContentList.get(i);
			content.majorityResult = factory.readMajorityVotingResults(analysisPath.calculationsPath, "MajorityVoting");
			content.strengthSignalResult = factory.readStrengthSignalResults(analysisPath.calculationsPath, "MajorityVoting");	
			this.filterContentList.set(i,content);
		}
		
		ArrayList<ArrayList<String>> reportTable = generateReportTable();
		factory.writeResult(analysisPath.analysisTypePath, "Results", new Integer(3), new Integer (3), reportTable);

	}

	private ArrayList<ArrayList<String>> generateReportTable(){
		ArrayList<ArrayList<String>> reportTable = new ArrayList<ArrayList<String>>();
		
		for(FilterContent content : this.filterContentList){
			
			ArrayList<String> line = new ArrayList<String>();
			String durationStr = content.minimalDuration.toString();
			line.add(durationStr);
			String workerScoreStr = content.convertWorkerScore();
			line.add(workerScoreStr);
			String ictStr = content.maxICantTell.toString();
			line.add(ictStr);
			
			line.add(content.validAnswers.toString());
			line.add(content.activeWorkers.toString());
			
			line.add(content.majorityResult.precision.toString());
			line.add(content.majorityResult.recall.toString());
			line.add(content.strengthSignalResult.precision.toString());
			line.add(content.strengthSignalResult.recall.toString());
			
			reportTable.add(line);
		}
		
		return reportTable;
	}
	

	
	public static void main(String[] args){
		AnalysisController controller = new AnalysisController();
		controller.run();
		controller.generateReport();
	}


}
