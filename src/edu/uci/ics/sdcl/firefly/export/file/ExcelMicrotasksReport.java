package edu.uci.ics.sdcl.firefly.export.file;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
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
	public boolean writeToXlsx(HashMap<String, FileDebugSession> microtasksMappedPerFile)
	{
		int numberOfQuestions = 0;
		int numberOfAnswers = 0;
		int numberOfFiles = microtasksMappedPerFile.size();

		/* creating excel workbook */
		//Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		//Create a blank sheet for the summary
		XSSFSheet summarySheet = workbook.createSheet("Summary");

		Integer mapKey = 0;
		// creating a map containing all the microtasks obtained from all files uploaded
		HashMap<Integer, Microtask> allMicrotasksMap = new HashMap<>();
		// keep in mind that each file is a key for a HashMap and I want to read the microtasks Map inside it
		Set<Map.Entry<String, FileDebugSession>> setMPF = microtasksMappedPerFile.entrySet();	// MPF = microtasks per File
		Iterator<Entry<String, FileDebugSession>> iMPF = setMPF.iterator();
		while(iMPF.hasNext())
		{
			Map.Entry<String, FileDebugSession> meMPF = (Map.Entry<String, FileDebugSession>)iMPF.next();
			// iterating microtasks per file
			Set<Map.Entry<Integer, Microtask>> setMPF2 = meMPF.getValue().getMicrotaskMap().entrySet();
			Iterator<Entry<Integer, Microtask>> iMPF2 = setMPF2.iterator();
			while(iMPF2.hasNext())
			{
				Map.Entry<Integer, Microtask> meMPF2 = (Map.Entry<Integer, Microtask>)iMPF2.next();
				allMicrotasksMap.put(new Integer(mapKey++), meMPF2.getValue());
			}
		}
		numberOfQuestions = allMicrotasksMap.size();

		// converting microtasks per file into microtasks per method
		HashMap<String, ArrayList<Microtask>> microtasksPerMethod = Features.convertToMicrotasksPerMethod(allMicrotasksMap);
		// iterating methods
		Set<Map.Entry<String, ArrayList<Microtask>>> set = microtasksPerMethod.entrySet();
		Iterator<Entry<String, ArrayList<Microtask>>> i = set.iterator();
		while(i.hasNext())
		{
			Map.Entry<String, ArrayList<Microtask>> me = (Map.Entry<String, ArrayList<Microtask>>)i.next();

			//Create a blank sheet for the method
			XSSFSheet methodSheet = workbook.createSheet(me.getKey());

			int dataKey = 0;	// for the 'data' below
			int rownum = 0;	
			Row row;
			/* creating first header line */
			row = methodSheet.createRow(rownum++);
			int cellnum2 = 0;
			Cell cell = row.createCell(cellnum2++);
			cell.setCellValue("File Name");
			cell = row.createCell(cellnum2++);
			cell.setCellValue("ID");
			cell = row.createCell(cellnum2++);
			cell.setCellValue("Questions");
			cell = row.createCell(cellnum2);
			cell.setCellValue("Answers - options");
			// preparing the TreeMap for later fill the method sheet
			Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
			int lastOptionColumn = 3;			// for auto sizing later
			int lastExplanationColumn = 4;
			// iterating questions (per method)
			for (Microtask microtask : me.getValue())
			{	
				numberOfAnswers += microtask.getNumberOfAnswers();

				// preparing line (object), which index is a cell
				Object[] lineContent = new Object[(microtask.getNumberOfAnswers()*2)+3]; // FileName(1) + ID(1) + question(1) + explanations(size) + answers(size)
				lineContent[0] = Features.removePath(microtask.getMethod().getFileName(), true);	// FileName (cell 0)
				lineContent[1] = microtask.getID();						// ID (cell 1)
				lineContent[2] = microtask.getQuestion();				// Question (cell 2)
				int k = 3;
				for (Answer singleAnswer : microtask.getAnswerList()) {
					lineContent[k++] = singleAnswer.getOption();		// adding answers per question
				}
				lastOptionColumn = lastOptionColumn < k ? k : lastOptionColumn;
				if (k > 3){	// got some answers, now the explanation:
					cell = row.createCell(k);
					cell.setCellValue("Explanations");

					for (Answer singleAnswer : microtask.getAnswerList()) {
						lineContent[k++] = singleAnswer.getExplanation();	// adding explanation per question
					}
					lastExplanationColumn = lastExplanationColumn < k ? k : lastExplanationColumn;		
				}
				data.put(new Integer(dataKey++), lineContent);	// putting customized line 
				
			}
			/* filling the method sheet */
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

			// sizing columns for method sheet
			for (int j=0; j < lastOptionColumn; j++){
				methodSheet.autoSizeColumn(j);
			}
			methodSheet.setColumnWidth(2, 30000);
			if (lastExplanationColumn > 4){
				for (int j=4; j < lastExplanationColumn; j++){
					methodSheet.setColumnWidth(j, 15000);
				}
			}
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
		data.put("1", new Object[] {"Number of files: ", numberOfFiles});
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

			FileOutputStream out = new FileOutputStream(new File("MicrotasksReport.xlsx"));
			workbook.write(out);
			out.flush();
			out.close();

			System.out.println("MicrotasksReport.xlsx written successfully on disk.");
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
