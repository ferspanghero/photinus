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
	private Integer NUMBER_OF_ANSWERS;
	private final String reportPath;
	private final String reportFileName = "firefly_report.xlsx";
	
	public ExcelExporter() {
		PropertyManager property = PropertyManager.initializeSingleton();
		this.NUMBER_OF_ANSWERS = 0;
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
		Cell cell = row.createCell(cellNum);
		workersSheet.addMergedRegion(CellRangeAddress.valueOf("A1:D1"));
		cell.setCellValue("1 - DATA IDENTIFIERS");
		cell.setCellStyle(dataIdentifierHeaderColor(wb));
		cell = row.createCell(4); //next cell starts at position 4
		
		int columnNumber = 0; // Controls the column number for example A1 or A2
		for(int i = (4 + NUMBER_OF_ANSWERS);i > 0;) {
			columnNumber++;
			i -= 26;
		}
		
		cell.setCellValue("2 - DATA VALUES "+ report.reportType());
		int endOfAnswers = 68 + NUMBER_OF_ANSWERS; // D1+NUMBER_OF_ANSWERS
		String letter = Character.toString((char) endOfAnswers);
		workersSheet.addMergedRegion(CellRangeAddress.valueOf("E1:"+letter+columnNumber));
		cell.setCellStyle(answerHeaderColor(wb));
		
		cell = row.createCell(4+NUMBER_OF_ANSWERS);
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
				cell.setCellStyle(dataIdentifierDataColor(wb));
			else
			{
				cell.setCellStyle(answerDataColor(wb));
				NUMBER_OF_ANSWERS++;
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
					cell.setCellStyle(dataIdentifierDataColor(wb));
				else
					cell.setCellStyle(answerDataColor(wb));
			}
		}
	}
	
	private CellStyle dataIdentifierHeaderColor(Workbook wb)
	{
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(155,194,230)));
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		XSSFFont font = (XSSFFont) wb.createFont();
		font.setBold(true);
		style.setFont(font);
		
		return style;
	}
	
	private CellStyle answerHeaderColor(Workbook wb)
	{
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(169, 208, 142)));
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		XSSFFont font = (XSSFFont) wb.createFont();
		font.setBold(true);
		style.setFont(font);
		
		return style;
	}
	
	private CellStyle dataIdentifierDataColor(Workbook wb)
	{
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(189,215,238)));
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
	
	private CellStyle answerDataColor(Workbook wb)
	{
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(198, 224, 180)));
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

}
