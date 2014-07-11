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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.Microtask;


public class ExcelFileDealer
{
	public static void writeToXlsx(FileDebugSession debugSessionFile)
	{
		String fileNameWithoutDotJava = new String(debugSessionFile.getFileName().
				substring(0, debugSessionFile.getFileName().indexOf('.')));
		int numberOfSnippets = 0;
		int numberOfQuestions = 0;
		int numberOfStatements = 0;
		/* reading microtasks and obtaining information regarding its file*/
		HashMap<Integer, Microtask> concreteQuestionsMade = debugSessionFile.getMicrotaskMap();
		Set<Map.Entry<Integer, Microtask>> set = concreteQuestionsMade.entrySet();
		Iterator<Entry<Integer, Microtask>> i = set.iterator();
		while(i.hasNext()) 
		{
			numberOfQuestions++;
			Map.Entry<Integer, Microtask> me = (Map.Entry<Integer, Microtask>)i.next();
			

		}

		//Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		//Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("Sumary");

		//This data needs to be written (Object[])
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("1", new Object[] {"File name: ", fileNameWithoutDotJava});
		data.put("2", new Object[] {"Number of Snippets: ", "put number here"});
		data.put("3", new Object[] {"Number of questions: ", numberOfQuestions});
		data.put("4", new Object[] {"Number of statements: ", "put number here"});

		//Iterate over data and write to sheet
		Set<String> keyset = data.keySet();
		int rownum = 0;
		for (String key : keyset)
		{
			Row row = sheet.createRow(rownum++);
			Object [] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr)
			{
				Cell cell = row.createCell(cellnum++);
				if(obj instanceof String)
					cell.setCellValue((String)obj);
				else if(obj instanceof Integer)
					cell.setCellValue((Integer)obj);
			}
		}
		try
		{
			//Write the workbook in file system

			FileOutputStream out = new FileOutputStream(new File(fileNameWithoutDotJava + ".xlsx"));
			workbook.write(out);
			out.close();

			System.out.println(fileNameWithoutDotJava + ".xlsx written successfully on disk.");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
