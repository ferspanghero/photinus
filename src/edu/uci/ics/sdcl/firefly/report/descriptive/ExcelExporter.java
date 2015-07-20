package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.uci.ics.sdcl.firefly.util.PropertyManager;

public class ExcelExporter implements DescriptiveReportWriter {
	private final String reportPath;
	private final String reportFileName = "firefly_report.xlsx";
	
	public ExcelExporter() {
		PropertyManager property = PropertyManager.initializeSingleton();
		this.reportPath = property.reportPath;
	}
	
	@Override
	public void exportReport(DescriptiveReport report) {
		
		/* creating excel workbook */
		//Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		//Create a blank sheet (for the scores)
		XSSFSheet workersSheet = workbook.createSheet("Answers Report");
		populateSheet(workersSheet, report, workbook);
		createHeaders(workersSheet, workbook, report);
		try{
			//Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File(this.reportPath+this.reportFileName));
			workbook.write(out);
			out.flush();
			out.close();
			System.out.println("Answers Report.xlsx written successfully on disk.");
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void createHeaders(XSSFSheet workersSheet, Workbook wb, DescriptiveReport report)
	{
		Row row = workersSheet.createRow(0);
		int cellNum = 0;
		/* creating first header line */
		
		// HEADER REPORT
		Cell cell = row.createCell(cellNum);
		workersSheet.addMergedRegion(CellRangeAddress.valueOf("A1:D1"));
		cell.setCellValue("1 - DATA IDENTIFIERS");
		cell.setCellStyle(headerColor(wb, 155,194,230));
		cell = row.createCell(4); //next cell starts at position 4
		
		//ANSWER REPORT
		int startColumnNumber = 1;
		int endColumnNumber = 0; // Controls the column number for example A1 or A2
		for(int i = (4 + report.getAnswerReportSize() );i > 0;) {
			endColumnNumber++;
			i -= 26;
		}
		cell.setCellValue("2 - "+ report.reportTypes()[0]);
		int start = (65 + report.getHeaderReportSize());
		String startLetter = Character.toString((char) start);
		int end = (64 + ( (report.getHeaderReportSize() + report.getAnswerReportSize()) %26)); // D1+NUMBER_OF_ANSWERS
		String endLetter = Character.toString((char) end);
		workersSheet.addMergedRegion(CellRangeAddress.valueOf(buildMergedCellsString(startColumnNumber, startLetter, endColumnNumber, endLetter)));
		cell.setCellStyle(headerColor(wb, 169, 208, 142));
		
		// COUNT REPORT
		if(report.getCountReportSize() != 0)
		{
			startColumnNumber = endColumnNumber;
			endColumnNumber = 0; // Controls the column number for example A1 or A2
			for(int i = (4 + report.getAnswerReportSize() + report.getCountReportSize() );i > 0;) {
				endColumnNumber++;
				i -= 26;
			}
			start  = end + 1;
			end = (64 + ((report.getHeaderReportSize() + report.getAnswerReportSize() + report.getCountReportSize()) %26));
			cell = row.createCell(4+report.getAnswerReportSize());
			cell.setCellValue("3 - "+ report.reportTypes()[1]);
			startLetter = Character.toString((char) start);
			endLetter = Character.toString((char) end);
			workersSheet.addMergedRegion(CellRangeAddress.valueOf(buildMergedCellsString(startColumnNumber, startLetter, endColumnNumber, endLetter)));
			cell.setCellStyle(headerColor(wb, 255, 230, 153));
		}
		// CORRECTNESS REPORT
		if(report.getCorrectnessSize() != 0)
		{
			startColumnNumber = endColumnNumber;
			endColumnNumber = 0; // Controls the column number for example A1 or A2
			for(int i = (4 + report.getAnswerReportSize() + report.getCountReportSize() + report.getCorrectnessSize() );i > 0;) {
				endColumnNumber++;
				i -= 26;
			}
			start  = end + 1;
			end = (64 + ((report.getHeaderReportSize() + report.getAnswerReportSize() + report.getCountReportSize() + report.getCorrectnessSize()) %26));
			cell = row.createCell(4+report.getAnswerReportSize()+report.getCountReportSize());
			cell.setCellValue(report.reportTypes()[2]);
			startLetter = Character.toString((char) start);
			endLetter = Character.toString((char) end);
			workersSheet.addMergedRegion(CellRangeAddress.valueOf(buildMergedCellsString(startColumnNumber, startLetter, endColumnNumber, endLetter)));
			cell.setCellStyle(headerColor(wb, 255, 230, 153));
		}
	}
	
	private void populateSheet(XSSFSheet workersSheet, DescriptiveReport report, Workbook wb)
	{
		XSSFFont font = (XSSFFont) wb.createFont();
		font.setBold(true);
		
		int rowNumber = 1;
		int cellNumber = 0;
		Row row = workersSheet.createRow(rowNumber++);
		Cell cell = null;
		
		Map<String, List<String>> table = report.getTable();
		List<String> keys = new ArrayList<String>(table.keySet());
		
		// SUBTITLES
		for (String subTitle : keys) {
			cell = row.createCell(cellNumber);
			cell.setCellValue(subTitle);
			if((cellNumber) < 4)
				cell.setCellStyle(dataReportColor(wb,189,215,238));
			else if(cellNumber < report.getAnswerReportSize() + 4)
			{
				cell.setCellStyle(dataReportColor(wb,198, 224, 180));
			}else if(cellNumber < report.getCountReportSize() + report.getAnswerReportSize() +4){
				cell.setCellStyle(dataReportColor(wb, 255, 230, 153));
			}
			else
			{
				cell.setCellStyle(dataReportColor(wb, 255, 250, 205));
			}
			cell.getCellStyle().setFont(font);
			cellNumber++;
		}
		
		//DATA VALUES
		for (int i = 0; i < table.get(keys.get(0)).size(); i++) {
			row = workersSheet.createRow(rowNumber++);
			cellNumber = 0;
			for (String key : keys) {
				cell = row.createCell((cellNumber%table.size()));
				cellNumber++;
				cell.setCellValue(table.get(key).get(i));
				if((cellNumber) <= 4)
					cell.setCellStyle(dataReportColor(wb,189,215,238));
				else if (cellNumber <= report.getAnswerReportSize() + 4){
					cell.setCellStyle(dataReportColor(wb,198, 224, 180));
				}else if(cellNumber <= report.getCountReportSize() + report.getAnswerReportSize() +4){
					cell.setCellStyle(dataReportColor(wb, 255, 230, 153));
				}
				else{
					cell.setCellStyle(dataReportColor(wb, 255, 250, 205));
				}
			}
		}
	}
	

	private CellStyle headerColor(Workbook wb, int red, int green, int blue)
	{
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(red,green,blue)));
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		XSSFFont font = (XSSFFont) wb.createFont();
		font.setBold(true);
		style.setFont(font);
		
		return style;
	}
	
	private CellStyle dataReportColor(Workbook wb, int red, int green, int blue) {
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(red, green, blue)));
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		
		return style;
	}
	
	private String buildMergedCellsString(int startColumnNumber, String startLetter, int endColumnNumber, String endLetter )
	{
		StringBuilder mergeCells = new StringBuilder();
		mergeCells.append((startColumnNumber != 1) ? Character.toString((char) (64 + (startColumnNumber -1))) : "" );
		mergeCells.append(startLetter);
		mergeCells.append(1);
		mergeCells.append(":");
		mergeCells.append((endColumnNumber != 1) ? Character.toString((char) (64 + (endColumnNumber -1))) : "" );
		mergeCells.append(endLetter);
		mergeCells.append(1);
		return mergeCells.toString();
	}

}
