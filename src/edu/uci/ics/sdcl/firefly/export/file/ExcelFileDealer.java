package edu.uci.ics.sdcl.firefly.export.file;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.uci.ics.sdcl.firefly.FileDebugSession;


public class ExcelFileDealer
{
	public static void writeToXlsx(FileDebugSession debugSessionFile)
	{
		String fileNameWithoutDotJava = new String(debugSessionFile.getFileName().
				substring(0, debugSessionFile.getFileName().indexOf('.')));
		//Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		//Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("Sumary");

		//This data needs to be written (Object[])
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("1", new Object[] {"File name: ", fileNameWithoutDotJava});
		data.put("2", new Object[] {"Number of Snippets: ", "put number here"});
		data.put("3", new Object[] {"Number of questions: ", "put number here"});
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
