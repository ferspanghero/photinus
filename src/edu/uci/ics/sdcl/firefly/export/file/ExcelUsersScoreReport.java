package edu.uci.ics.sdcl.firefly.export.file;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
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

import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.servlet.SurveyServlet;

public class ExcelUsersScoreReport {
	public static void writeToXlsx(HashMap<String, Worker> workers)
	{
		/* creating excel workbook */
		//Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		//Create a blank sheet (for the scores)
		XSSFSheet scoreSheet = workbook.createSheet("Scores");

		int key = 0;	// for the 'data' below
		int rownum = 0;	
		Row row;

		/* creating first header line */
		row = scoreSheet.createRow(rownum++);
		int cellNum = 0;
		Cell cell = row.createCell(cellNum++);
		cell.setCellValue("User ID");
		cell = row.createCell(cellNum++);
		cell.setCellValue("HIT ID");
		cell = row.createCell(cellNum++);
		cell.setCellValue("Skill Score");
		cell = row.createCell(cellNum++);
		// creating 5 new columns for the skill questions
		for (Integer k=1; k<=5; k++){
			cell.setCellValue("Q" + k.toString());
			cell = row.createCell(cellNum++);
		}	// now for the survey
		for (int i=0; i<SurveyServlet.question.length; i++){
			cell.setCellValue(SurveyServlet.question[i]);
			cell = row.createCell(cellNum++);
		}
		
		// populating new data structure to later fill score sheet
		Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
		// iterating users
		Set<Map.Entry<String, Worker>> setWorkers = workers.entrySet();
		Iterator<Entry<String, Worker>> iterateWorkers = setWorkers.iterator();
		while(iterateWorkers.hasNext())
		{
			Map.Entry<String, Worker> mapEntryWorker = (Map.Entry<String, Worker>)iterateWorkers.next();
			
			// preparing line (object), which index is a cell
			Object[] lineContent = new Object[13]; 		// 13 columns
			lineContent[0] = mapEntryWorker.getKey();	// user ID (cell 0)
			lineContent[1] = mapEntryWorker.getValue().getHitId();	// hit ID (cell 1)
			lineContent[2] = mapEntryWorker.getValue().getGrade();	// Skill score (cell 2)
			// iterating over the skill questions
			int j = 3;
			if (null != mapEntryWorker.getValue().getGradeMap()){
				Set<Map.Entry<String, Boolean>> setSkillTest = mapEntryWorker.getValue().getGradeMap().entrySet();
				Iterator<Entry<String, Boolean>> iterateSkillTest = setSkillTest.iterator();
				while(iterateSkillTest.hasNext()){
					Map.Entry<String, Boolean> mapEntrySkillTest = (Map.Entry<String, Boolean>)iterateSkillTest.next();
					lineContent[j++] = mapEntrySkillTest.getValue() ? "OK" : "Not";
				}
			} else j = 8;
			// iterating over the Survey questions
			HashMap<String, String> survey = mapEntryWorker.getValue().getSurveyAnswers();
			for (int i=0; i<SurveyServlet.question.length; i++){
				if (survey.containsKey(SurveyServlet.question[i])){
					lineContent[j++] = survey.get(SurveyServlet.question[i]);
				} else
					lineContent[j++] = "No Answer";
			}

			// putting customized line
			data.put(new Integer(key++), lineContent); 
		}
		
		/* filling the score sheet */
		// Iterate over data and write to score sheet
		Set<Integer> keyset = data.keySet();
		for (Integer singleKey : keyset)
		{	// new row for each entry
			row = scoreSheet.createRow(rownum++);
			Object [] objArr = data.get(singleKey);
			int cellnum = 0;
			for (Object obj : objArr)
			{	// new cell for each object on the object Array
				cell = row.createCell(cellnum++);
				if(obj instanceof String)
				{
					String text = (String)obj;
					if (text.length() > 30)	// wraping text
					{
						CellStyle style = workbook.createCellStyle(); //Create new style
						style.setWrapText(true); 	//Set wordwrap
						cell.setCellStyle(style); 	//Apply style to cell
					}
					cell.setCellValue(text);
				}
				else if(obj instanceof Integer)
					cell.setCellValue((Integer)obj);
			}
		}
		// sizing columns for method sheet
		for (short c=0; c<12; c++){
			scoreSheet.autoSizeColumn(c);
		}
		scoreSheet.setColumnWidth(12, 20000);	// the one that can have a long text (wrapped)

		try
		{
			//Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File("WorkersReport.xlsx"));
			workbook.write(out);
			out.flush();
			out.close();

			System.out.println("WorkersReport.xlsx written successfully on disk.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}