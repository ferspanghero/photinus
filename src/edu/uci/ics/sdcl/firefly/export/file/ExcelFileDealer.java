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
import edu.uci.ics.sdcl.firefly.servlet.MethodData;


public class ExcelFileDealer
{
	public static void writeToXlsx(HashMap<String,HashMap<String,MethodData>> resultMapArg)
	{
		/* reading microtasks and obtaining information regarding its file*/
		Set<Map.Entry<String,HashMap<String,MethodData>>> set = resultMapArg.entrySet();
		Iterator<Entry<String,HashMap<String,MethodData>>> i = set.iterator();
		while(i.hasNext()) 
		{	// for each file it will be written an xlsx
			int numberOfSnippets = 0;
			int numberOfQuestions = 0;
			int numberOfStatements = 0;
			
			Map.Entry<String,HashMap<String,MethodData>> me = (Map.Entry<String,HashMap<String,MethodData>>)i.next();
			String filePathWithoutDotJava = new String(me.getKey().substring(0, me.getKey().indexOf('.')));
			String fileName = filePathWithoutDotJava.substring(filePathWithoutDotJava.lastIndexOf('\\')+1);
				
			/* creating excel file */
			//Blank workbook
			XSSFWorkbook workbook = new XSSFWorkbook();

			//Create a blank sheet
			XSSFSheet sumarySheet = workbook.createSheet("Sumary");

			
			// iterating methods
			Set<Map.Entry<String,MethodData>> set2 = me.getValue().entrySet();
			Iterator<Entry<String,MethodData>> i2 = set2.iterator();
			while(i2.hasNext())
			{
				numberOfSnippets++;
				Map.Entry<String,MethodData> me2 = (Map.Entry<String,MethodData>)i2.next();
			}
			
			/* setting the summary sheet */
			//This data needs to be written (Object[])
			Map<String, Object[]> data = new TreeMap<String, Object[]>();
			data.put("1", new Object[] {"File name: ", fileName});
			data.put("2", new Object[] {"Number of Snippets: ", numberOfSnippets});
			data.put("3", new Object[] {"Number of questions: ", numberOfQuestions});
			data.put("4", new Object[] {"Number of statements: ", numberOfStatements});

			//Iterate over data and write to sheet
			Set<String> keyset = data.keySet();
			int rownum = 0;
			for (String key : keyset)
			{
				Row row = sumarySheet.createRow(rownum++);
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

				FileOutputStream out = new FileOutputStream(new File(filePathWithoutDotJava + ".xlsx"));
				workbook.write(out);
				out.flush();
				out.close();

				System.out.println(filePathWithoutDotJava + ".xlsx written successfully on disk.");

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		
	}
}
