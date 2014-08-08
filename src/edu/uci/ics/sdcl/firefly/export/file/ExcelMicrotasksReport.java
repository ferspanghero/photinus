package edu.uci.ics.sdcl.firefly.export.file;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
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

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.Features;


public class ExcelMicrotasksReport
{
	public static void writeToXlsx(FileDebugSession microtasksPerFile)
	{
		int numberOfQuestions = 0;
		int numberOfAnswers = 0;
		
		String fileName = Features.removePath(microtasksPerFile.getFileName(), true);

		/* creating excel workbook (per file) */
		//Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		//Create a blank sheet for the summary
		XSSFSheet summarySheet = workbook.createSheet("Summary");

		// converting microtasks per file into microtasks per method
		HashMap<String, ArrayList<Microtask>> microtasksPerMethod = Features.convertToMicrotasksPerMethod(microtasksPerFile.getMicrotaskMap());
		// iterating methods
		Set<Map.Entry<String, ArrayList<Microtask>>> set = microtasksPerMethod.entrySet();
		Iterator<Entry<String, ArrayList<Microtask>>> i = set.iterator();
		while(i.hasNext())
		{
			Map.Entry<String, ArrayList<Microtask>> me = (Map.Entry<String, ArrayList<Microtask>>)i.next();
			
			numberOfQuestions += me.getValue().size();
			
			//Create a blank sheet for the method
			XSSFSheet methodSheet = workbook.createSheet(me.getKey());

			int key = 0;	// for the 'data' below
			int rownum = 0;	
			Row row;
			/* creating first header line */
			row = methodSheet.createRow(rownum++);
			int cellnum2 = 0;
			Cell cell = row.createCell(cellnum2++);
			cell.setCellValue("ID");
			cell = row.createCell(cellnum2++);
			cell.setCellValue("Questions");
			cell = row.createCell(cellnum2);
			cell.setCellValue("Answers");

			// iterating questions (per method)
			for (Microtask microtask : me.getValue())
			{	
				numberOfAnswers += microtask.getNumberOfAnswers();

				/* filling the method sheet */
				Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
				// preparing line (object), which index is a cell
				Object[] lineContent = new Object[microtask.getNumberOfAnswers()+3]; // ID(1) + question(1) + explanations(1) + answers(size)
				lineContent[0] = microtask.getID();			// ID (cell 0)
				lineContent[1] = microtask.getQuestion();	// Question (cell 1)
				String cellOne = "";
				int k = 3;
				for (Answer singleAnswer : microtask.getAnswerList()) {
					cellOne += singleAnswer.getOption() + "{" + singleAnswer.getExplanation() + "}; ";
					lineContent[k++] = singleAnswer.getOption();	// adding answers per question
				}
				lineContent[2] = cellOne;					// setting cell at index 2
				data.put(new Integer(key++), lineContent);	// putting customized line 

				//Iterate over data and write to method sheet
				Set<Integer> keyset = data.keySet();
				for (Integer singleKey : keyset)
				{	// new row for each entry
					row = methodSheet.createRow(rownum++);
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
				//row = methodSheet.createRow(rownum++);	// blank row
			}
			// sizing columns for method sheet
			methodSheet.autoSizeColumn(0);
			methodSheet.setColumnWidth(1, 30000);
			methodSheet.autoSizeColumn(2);
			methodSheet.autoSizeColumn(3);
			/*
				CellStyle cs = workbook.createCellStyle();
				XSSFFont f = workbook.createFont();
				f.setBoldweight((short) Font.BOLD);
				cs.setFont(f);
				methodSheet.setDefaultColumnStyle(1,cs); //set bold for column 1 */
		}

		/* filling the summary sheet */
		//This data needs to be written (Object[])
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("1", new Object[] {"File name: ", fileName});
		data.put("2", new Object[] {"Number of Snippets: ", microtasksPerMethod.size()});
		data.put("3", new Object[] {"Total number of questions: ", numberOfQuestions});
		data.put("4", new Object[] {"Total number of answers: ", numberOfAnswers});

		//Iterate over data and write to the Summary sheet
		Set<String> keyset = data.keySet();
		int rownum = 0;
		for (String key : keyset)
		{
			Row row = summarySheet.createRow(rownum++);
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
		// auto-sizing columns
		for (int columnPosition = 0; columnPosition< 5; columnPosition++) {
			summarySheet.autoSizeColumn((short) (columnPosition));
		}
		try
		{
			//Write the workbook in file system

			FileOutputStream out = new FileOutputStream(new File(fileName + ".xlsx"));
			workbook.write(out);
			out.flush();
			out.close();

			System.out.println(fileName + ".xlsx written successfully on disk.");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
