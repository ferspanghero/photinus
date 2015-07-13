package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;

public class WorkerDemographicsReport {

private String fileName = "WorkersDemographicsReport.xlsx";
	
	String consentLogpath = "C:/var/lib/tomcat7/webapps/consent-Pilot2-log.txt";
	String sessionLogpath = "C:/var/lib/tomcat7/webapps/session-log.log";

	public WorkerDemographicsReport(){
		writeToXlsx();
	}
	
	public void writeToXlsx(){
		/* creating excel workbook */
		//Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		//Create a blank sheet (for the scores)
		XSSFSheet workersSheet = workbook.createSheet("Workers");
		//first header
		createFirstHeader(workersSheet);
		//second header
		createSecondHeader(workersSheet);
		
		populateTable(workersSheet);
		try{
			//Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File(this.fileName));
			workbook.write(out);
			out.flush();
			out.close();

			System.out.println("WorkerDemographicsReport.xlsx written successfully on disk.");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public void createFirstHeader(XSSFSheet workersSheet) {
		Row row = workersSheet.createRow(0);
		int cellNum = 0;
		/* creating first header line */
		Cell cell = row.createCell(cellNum);
		workersSheet.addMergedRegion(CellRangeAddress.valueOf("A1:H1"));
		cell.setCellValue("1 - Data Identifiers - (Worker Demographics)");
		cell = row.createCell(8); //next cell starts at position 8

		cell.setCellValue("2 - Load Factor");
		workersSheet.addMergedRegion(CellRangeAddress.valueOf("I1:P1"));
		cell = row.createCell(16);//next cell starts at position 13

		cell.setCellValue("3 - Optimism Analisys");
		workersSheet.addMergedRegion(CellRangeAddress.valueOf("Q1:T1"));
		cell = row.createCell(20);//next cell starts at position 17

		cell.setCellValue("4 - Quality");
		workersSheet.addMergedRegion(CellRangeAddress.valueOf("U1:Z1"));
		
	}
	
	public void createSecondHeader(XSSFSheet workersSheet) {
		Row row = workersSheet.createRow(1);
		int cellNum = 0;
		//Data identifiers
		Cell cell;
		cell = row.createCell(cellNum++);
		cell.setCellValue("Worker Id");
		cell = row.createCell(cellNum++);

		cell.setCellValue("Worker Score");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("Worker Experience");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("Years Programming");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("Worker Gender");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("Worker Country");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("Prog Language");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("Learned on");
		cell = row.createCell(cellNum++);
		//-----------------------------------------//
		//Load Factor
		cell.setCellValue("#HITs");
		cell = row.createCell(cellNum++);

		cell.setCellValue("#Questions Answered");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("#Yes");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("#No's");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("#IDK");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("Difficulty Average");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("Confidence Average");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("Answer Duration Average");
		cell = row.createCell(cellNum++);
		
		
		//------------------------------------------//
		//Optimism Analisys
		cell.setCellValue("#Expected Yes");
		cell = row.createCell(cellNum++);

		cell.setCellValue("#Expected No's");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("Optimism(Yes'/Expected Yes's)");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("Pessimism(No'/Expected No's)");
		cell = row.createCell(cellNum++);
		//-------------------------------------------//
		//Quality
		cell.setCellValue("#True Positives");
		cell = row.createCell(cellNum++);

		cell.setCellValue("#True Negatives");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("#False Positives");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("#False Negatives");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("#IDK");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("Total");
		cell = row.createCell(cellNum++);
	}
	
	public void populateTable(XSSFSheet workersSheet){
		FileConsentDTO fc = new FileConsentDTO(consentLogpath);
		
		int rownum = 2;
		Row row = workersSheet.createRow(rownum);
		int cellNum = 0;
		Iterator<Worker> workersIter = fc.getWorkers().values().iterator();
		while (workersIter.hasNext()) {
			Worker worker = workersIter.next();
			populateDataIdentifiers(row, cellNum, worker);
			rownum++;
			row = workersSheet.createRow(rownum);
			cellNum = 0;
		}

	}

	public void populateDataIdentifiers(Row row, int cellNum, Worker worker) {

		Cell cell;
		cell = row.createCell(cellNum++);
		cell.setCellValue(worker.getWorkerId());
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(worker.getGrade());
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(worker.getSurveyAnswer(" Experience"));
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(worker.getSurveyAnswer(" YearsProgramming"));
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(worker.getSurveyAnswer(" Gender"));
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(worker.getSurveyAnswer(" Country"));
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(worker.getSurveyAnswer(" Language"));
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(worker.getSurveyAnswer(" Learned"));
		
		populateLoadFactor(row, cellNum, worker);

	}
	
	public void populateLoadFactor(Row row, int cellNum, Worker worker) {
		FileSessionDTO fs = new FileSessionDTO(sessionLogpath);
		ArrayList<WorkerSession> workerSessions = fs.getSessionsByWorkerID(worker.getWorkerId());
		
		int numberOfAnswers = 0;
		int numberOfYes =0;
		int numberOfNo = 0;
		int numberOfIDK = 0;
		int difAverage = getDifficultyAverage(workerSessions, worker);
		int confidencyAverage = getConfidenceAverage(workerSessions, worker);
		double durationAverage = getDurationAverage(workerSessions, worker);
		
		for (WorkerSession workerSession : workerSessions) {
			Iterator<Microtask> iterMicrotask = workerSession.getMicrotaskList().iterator();
			while(iterMicrotask.hasNext()){
				Microtask microtask = iterMicrotask.next();
				Answer answer = microtask.getAnswerByUserId(worker.getWorkerId());
				if(answer!=null){
					numberOfAnswers++;
				};
				int answerOption = Answer.mapOptionNumber(answer.getOption());
				if(answerOption == 1){
					numberOfYes ++;
				}else if(answerOption == 2){
					numberOfIDK++;
				}else{
					numberOfNo++;
				}
				//72;73;78;79;84;92;95;97;102;104;119;123;126			}
			}
		}
		int numberOfTasks = workerSessions.size();
		
		Cell cell;
		cell = row.createCell(cellNum++);
		cell.setCellValue(numberOfTasks);
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(numberOfAnswers);
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(numberOfYes);
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(numberOfNo);	
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(numberOfIDK);
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(difAverage);	
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(confidencyAverage);
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(durationAverage);
		
		populateOptimismAnalysis(row,cellNum, worker, numberOfYes, numberOfNo);
	}
	
	public void populateOptimismAnalysis(Row row, int cellNum, Worker worker, int numberOfYes, int numberOfNo){
		FileSessionDTO fs = new FileSessionDTO(sessionLogpath);
		ArrayList<WorkerSession> workerSessions = fs.getSessionsByWorkerID(worker.getWorkerId());
		int totalAnswers= numberOfYes+numberOfNo;
		int expectedYes = 0;
		int expectedNo = 0;
		for (WorkerSession workerSession : workerSessions) {
			Iterator<Microtask> iterMicrotask = workerSession.getMicrotaskList().iterator();
			while(iterMicrotask.hasNext()){
				Microtask microtask = iterMicrotask.next();
				Answer answer = microtask.getAnswerByUserId(worker.getWorkerId());
				if(isBugCovering(microtask.getID())){
					expectedYes++; 
				}
			}
		}
		expectedNo = totalAnswers - expectedYes;
		
		Cell cell;
		cell = row.createCell(cellNum++);
		cell.setCellValue(expectedYes);
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(expectedNo);
		
		if(expectedYes!=0){
			cell = row.createCell(cellNum++);
			cell.setCellValue(numberOfYes/Double.parseDouble(expectedYes+""));
		}else{
			cell = row.createCell(cellNum++);
			cell.setCellValue(0);
		}
		
		if(expectedNo != 0){
			cell = row.createCell(cellNum++);
			cell.setCellValue(numberOfNo/Double.parseDouble(expectedNo+""));
		}else{
			cell = row.createCell(cellNum++);
			cell.setCellValue(0);
		}
		
		populateQuality(row, cellNum, worker);
	}
	
	private void populateQuality(Row row, int cellNum, Worker worker) {
		FileSessionDTO fs = new FileSessionDTO(sessionLogpath);
		ArrayList<WorkerSession> workerSessions = fs.getSessionsByWorkerID(worker.getWorkerId());
		int truePositive = 0;
		int trueNegative = 0;
		int falsePositive = 0;
		int falseNegative = 0;
		int idk=0; 
		for (WorkerSession workerSession : workerSessions) {
			Iterator<Microtask> iterMicrotask = workerSession.getMicrotaskList().iterator();
			while (iterMicrotask.hasNext()) {
				Microtask microtask = iterMicrotask.next();
				Answer answer = microtask.getAnswerByUserId(worker.getWorkerId());
				String answerOption = answer.getOption();
				boolean bugCovering = isBugCovering(microtask.getID());
				if(answer!=null){
					if (bugCovering && answerOption.equals(Answer.YES)) {
						truePositive++;
					} else if (!bugCovering && answerOption.equals(Answer.NO)) {
						trueNegative++;
					} else if (!bugCovering && answerOption.equals(Answer.YES)) {
						falsePositive++;
					} else if (bugCovering && answerOption.equals(Answer.NO)) {
						falseNegative++;
					} else {
						idk++;
					}
				}
			}
		}
		int total = truePositive+trueNegative+falseNegative+falsePositive+idk;
		Cell cell;
		cell = row.createCell(cellNum++);
		cell.setCellValue(truePositive);
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(trueNegative);
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(falsePositive);
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(falseNegative);
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(idk);
		
		cell = row.createCell(cellNum++);
		cell.setCellValue(total);
	}

	public boolean isBugCovering(int microtaskID){
		Integer[] bugCoveringQuestions = {72,73,78,79,84,92,95,97,102,104,119,123,126};
		for(int i=0; i<bugCoveringQuestions.length; i++){
			if(microtaskID == bugCoveringQuestions[i]){
				return true;
			}
		}
		return false;
	}
	
	public int getDifficultyAverage(ArrayList<WorkerSession> workerSessions, Worker worker) {
		int numberOfAnswers = 0;
		int difficulties = 0;
		for (WorkerSession workerSession : workerSessions) {
			Iterator<Microtask> iterMicrotask = workerSession.getMicrotaskList().iterator();
			while(iterMicrotask.hasNext()){
				Microtask microtask = iterMicrotask.next();
				Answer answer = microtask.getAnswerByUserId(worker.getWorkerId());
				if (answer != null){
					difficulties += answer.getDifficulty();
					numberOfAnswers++;
				}
			}
		}
		if(numberOfAnswers == 0){
			return 0;
		}
		return difficulties/numberOfAnswers;
	}
	
	public int getConfidenceAverage(ArrayList<WorkerSession> workerSessions, Worker worker) {
		int numberOfAnswers = 0;
		int confidencies = 0;
		for (WorkerSession workerSession : workerSessions) {
			Iterator<Microtask> iterMicrotask = workerSession.getMicrotaskList().iterator();
			while(iterMicrotask.hasNext()){
				Microtask microtask = iterMicrotask.next();
				Answer answer = microtask.getAnswerByUserId(worker.getWorkerId());
				if (answer != null){
					confidencies += answer.getConfidenceOption();
					numberOfAnswers++;
				}
			}
		}
		if(numberOfAnswers == 0){
			return 0;
		}
		return confidencies/numberOfAnswers;
	}
	
	public double getDurationAverage(ArrayList<WorkerSession> workerSessions, Worker worker) {
		int numberOfAnswers = 0;
		double durations = 0;
		for (WorkerSession workerSession : workerSessions) {
			Iterator<Microtask> iterMicrotask = workerSession.getMicrotaskList().iterator();
			while(iterMicrotask.hasNext()){
				Microtask microtask = iterMicrotask.next();
				Answer answer = microtask.getAnswerByUserId(worker.getWorkerId());
				if (answer != null){
					double time = Double.parseDouble(answer.getElapsedTime());
					durations += time;
					numberOfAnswers++;
				}
			}
		}
		if(numberOfAnswers == 0){
			return 0;
		}
		return (durations/numberOfAnswers)/1000;
	}
	
	public static void main(String[] args) {
		WorkerDemographicsReport wdr = new WorkerDemographicsReport();
	}
}
