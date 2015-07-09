package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;


import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.servlet.SurveyServlet;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

public class WorkerDemographicsReport {

private String fileName = "WorkersDemographicsReport.xlsx";

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
		workersSheet.addMergedRegion(CellRangeAddress.valueOf("I1:M1"));
		cell = row.createCell(13);//next cell starts at position 13

		cell.setCellValue("3 - Optimism Analisys");
		workersSheet.addMergedRegion(CellRangeAddress.valueOf("N1:Q1"));
		cell = row.createCell(17);//next cell starts at position 17

		cell.setCellValue("4 - Quality");
		workersSheet.addMergedRegion(CellRangeAddress.valueOf("R1:W1"));
		
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
		cell.setCellValue("#Tasks Taken");
		cell = row.createCell(cellNum++);

		cell.setCellValue("#Answers given (average)");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("#Yes");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("#No's");
		cell = row.createCell(cellNum++);
		
		cell.setCellValue("#IDK");
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
		FileConsentDTO fc = new FileConsentDTO("C:/var/lib/tomcat7/webapps/consent-Pilot2-log.txt");
		
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
	}
	
	public static void main(String[] args) {
		WorkerDemographicsReport wdr = new WorkerDemographicsReport();
	}
}
