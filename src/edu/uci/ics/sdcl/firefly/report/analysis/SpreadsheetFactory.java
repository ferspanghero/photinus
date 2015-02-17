package edu.uci.ics.sdcl.firefly.report.analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.uci.ics.sdcl.firefly.report.AnalysisPath;
import edu.uci.ics.sdcl.firefly.report.Result;


/**
 * Create spreadsheets that calculate multiple results. The whole process consist of two steps:
 * 1- instantiate new spreadsheet based on a template
 * 2- copy the cut/filtered data from data files to a worksheet in the spreadsheet
 * 
 * @author adrianoc
 *
 */
public class SpreadsheetFactory {

	String templateName = "MultipleAnalyzes_template.xlsx";
	String templatePath;
	String templateDataWorksheet = "data";

	Double minPrecision = new Double(0.0);
	Double minRecall = new Double(70.0);
	AnalysisPath analysisPath;
	
	public SpreadsheetFactory(){
		this.analysisPath = AnalysisPath.getInstance(); 
		this.templatePath = analysisPath.analysisTypePath;
	}


	private Path copyFile(String templatePath, String templateName, String copyPath, String copyName){

		Path baseExcelPath = FileSystems.getDefault().getPath(templatePath, templateName);
		Path newFilePath = FileSystems.getDefault().getPath(copyPath,copyName);

		try {
			Files.copy(baseExcelPath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
			return newFilePath.getFileName();
		} catch (IOException e) {
			e.printStackTrace();
			return null;			
		}
	}

	private int populateWorksheet(HashMap<String,ArrayList<String>> contentMap, String spreadsheetFileNamePath,String worksheetName){
		try{
			//Open workbook
			FileInputStream file = new FileInputStream(new File(spreadsheetFileNamePath));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheet(worksheetName);

			int answerCount=0;
			Iterator<String> iter = contentMap.keySet().iterator();
			int rowInt=1; //skips the first row, because it has the titles.
			while(iter.hasNext()){
				String taskId = iter.next();
				ArrayList<String> answerList = contentMap.get(taskId);
				Row row = sheet.getRow(rowInt);
				if(answerList!=null && answerList.size()>0){
					int columnIndex=1;//skips the first column, because it has the titles.
					for(String answer: answerList){
						Cell cell = row.getCell(columnIndex);
						cell.setCellValue(answer);

						//open worksheet
						//write to worksheet
						columnIndex++;
					}
					answerCount = answerCount + answerList.size(); 
				}

				//update and close workbook
				FileOutputStream out = new FileOutputStream(new File(spreadsheetFileNamePath));
				workbook.write(out);
				out.flush();
				out.close();
			}
			return answerCount;
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	/** Reads the Panel worksheet from the template spreadsheet */
	public Result readStrengthSignalResults(String spreadsheetFileNamePath,String worksheetName){

		try{
			//columnList = {"E", "J", "O", "T", "Y", "AD", "AI", "AN", "AS", "AX"};
			Integer[] columnList = {4, 9, 14, 19, 24, 29, 34, 39, 44, 49}; 
			Integer[] recallRowList = {3,6,9,12};

			Double precision=0.0;
			Double recall=0.0;
			Double auxPrecision=0.0;
			Double auxRecall=0.0;

			for(Integer column: columnList){
				for(Integer row: recallRowList){
					auxRecall = getCellValue(column, row, spreadsheetFileNamePath, worksheetName);
					if(auxRecall>=this.minRecall){
						auxPrecision = getCellValue(column, row-1, spreadsheetFileNamePath, worksheetName);
						if((auxPrecision>precision) || (auxPrecision==precision && auxRecall>recall)){
							precision = auxPrecision;
							recall = auxRecall;
						}
					}
				}
			}

			Result result = new Result();
			result.precision = precision;
			result.recall = recall;
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/** Reads the MajorityVoting worksheet from the template spreadsheet */
	public Result readMajorityVotingResults(String spreadsheetFileNamePath,String worksheetName){

		Integer column = 5;
		Integer precisionRow = 2;
		Integer recallRow = 3;

		Result result = new Result();
		result.precision = getCellValue(column, precisionRow, spreadsheetFileNamePath, worksheetName);;
		result.recall = getCellValue(column, recallRow, spreadsheetFileNamePath, worksheetName);;
		return result;		
	}


	private Double getCellValue(Integer columnInt, Integer rowInt, String fileNamePath, String worksheetName){

		try{
			Double cellValue = 0.0;
			//Open workbook
			FileInputStream file = new FileInputStream(new File(fileNamePath));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheet(worksheetName);

			Row row = sheet.getRow(rowInt.intValue());
			Cell cell = row.getCell(columnInt.intValue());
			String cellValueStr = cell.getStringCellValue();
			cellValue = new Double(cellValueStr);

			//close
			file.close();

			return cellValue;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public boolean writeResult(String reportFileNamePath, String worksheetName, Integer initialRow, Integer initialColumn, ArrayList<ArrayList<String>> resultMatrix){

		try{
			//Open workbook
			FileInputStream file = new FileInputStream(new File(reportFileNamePath));      
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheet(worksheetName);

			int rowIndex = initialRow.intValue();
			for(ArrayList<String> line: resultMatrix){
				int columnIndex = initialColumn.intValue();
				for(String result: line){
					Row row = sheet.getRow(rowIndex);
					Cell cell = row.getCell(columnIndex);
					cell.setCellValue(result);
					columnIndex++;
				}
				rowIndex++;
			}       

			//update and close workbook
			FileOutputStream out = new FileOutputStream(new File(reportFileNamePath));
			workbook.write(out);
			out.flush();
			out.close();

			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public void calculateResults(String fileName, FilterContent content){
		Path path = this.copyFile(this.templatePath, this.templateName, analysisPath.calculationsPath, fileName);
		int answerCount = this.populateWorksheet(content.answerMap, analysisPath.calculationsPath, this.templateDataWorksheet);
		if(answerCount!=content.validAnswers){
			System.out.println("ERROR: wrong number of anwers written in spreadsheet "+fileName);
			System.out.println("Expected:"+content.validAnswers+", actual: "+answerCount);
		}
	}

	public static void main(String[] args){

		
		

	}

}
