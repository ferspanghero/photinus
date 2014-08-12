package edu.uci.ics.sdcl.firefly.export.file;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;

public class ExcelAnswersReport {
	private HashMap<Integer, Integer> questionsInSheet;	// HashMap with the microtask's ID and respective column number
	private Integer columnNumber, key;
	private Map<Integer, Object[]> data;
	private Object[] lineContent;
	
	public ExcelAnswersReport() {
		this.questionsInSheet = new HashMap<Integer, Integer>();
		this.columnNumber = 2;
		this.key = 0;	// for the 'data' map
		data = new TreeMap<Integer, Object[]>();
	}
	
	@SuppressWarnings("unchecked")
	public boolean writeToXlsx(HashMap<String, Object> allWorkerSessions)
	{
		/* setting a HashMap with the microtask's ID and respective column number */
		// reading closed session's questions
		ArrayList<WorkerSession> closedSessions = (ArrayList<WorkerSession>)allWorkerSessions.get(WorkerSessionStorage.CLOSED);
		for (WorkerSession workerSession : closedSessions) {
			this.populateQuestionMap(workerSession.getMicrotaskList());
		}
		// reading active sessions questions
		HashMap<Integer, WorkerSession> activeSessions = (HashMap<Integer, WorkerSession>)allWorkerSessions.get(WorkerSessionStorage.ACTIVE);
		ArrayList<WorkerSession> activeSessionsAL = new ArrayList<WorkerSession>();	// to cache all the workerSessions
		Set<Map.Entry<Integer, WorkerSession>> setActiveSessions = activeSessions.entrySet();
		Iterator<Entry<Integer, WorkerSession>> iterateActiveSessions = setActiveSessions.iterator();
		while(iterateActiveSessions.hasNext())
		{
			Map.Entry<Integer, WorkerSession> mapEntryActiveSessions = (Map.Entry<Integer, WorkerSession>)iterateActiveSessions.next();
			this.populateQuestionMap(mapEntryActiveSessions.getValue().getMicrotaskList());
			activeSessionsAL.add(mapEntryActiveSessions.getValue());	// storing worker sessions to not loop again
		}
		// reading new session's questions
		Stack<WorkerSession> newCopiesSessions = (Stack<WorkerSession>)allWorkerSessions.get(WorkerSessionStorage.NEW_COPIES);
		ArrayList<WorkerSession> newSessionsAL = new ArrayList<WorkerSession>();
		for (WorkerSession workerSession : newCopiesSessions) {
			this.populateQuestionMap(workerSession.getMicrotaskList());
			newSessionsAL.add(workerSession);		// converting to array list (AL)
		}
		Stack<WorkerSession> newSessions = (Stack<WorkerSession>)allWorkerSessions.get(WorkerSessionStorage.NEW);
		for (WorkerSession workerSession : newSessions) {
			this.populateQuestionMap(workerSession.getMicrotaskList());
			newSessionsAL.add(workerSession);		// converting to array list (AL)
		}
		
		/* preparing the data structure */
		this.lineContent = new Object[this.questionsInSheet.size()+2];	// represents the lines on the sheet
		/* creating first header line */								// +2 for the 2 IDs before questions
		this.lineContent[0] = "Worker ID";
		this.lineContent[1] = "Orig. ID";
		Set<Map.Entry<Integer, Integer>> setQIS = this.questionsInSheet.entrySet();
		Iterator<Entry<Integer, Integer>> iterateQIS = setQIS.iterator();
		while(iterateQIS.hasNext())
		{
			Map.Entry<Integer, Integer> mapEntryQIS = (Map.Entry<Integer, Integer>)iterateQIS.next();
			Integer currentColumn = mapEntryQIS.getValue();
			if (null == currentColumn)	// critical error
				return false;
			this.lineContent[currentColumn] = mapEntryQIS.getKey();	
		}
		this.data.put(this.key++, lineContent);
		/* getting data from the closed sessions */								
		if (!this.populateDataLines(closedSessions))
			return false;
		/* now from the active sessions */								
		if (!this.populateDataLines(activeSessionsAL))
			return false;
		/* now for the new sessions implement the not answered counter */
		this.lineContent = new Object[questionsInSheet.size()+2];
		Arrays.fill(this.lineContent, (Integer)0);	// populating the array with 0's
		this.lineContent[0] = "Unanswered";
		for (WorkerSession workerSession : newSessionsAL) {
			// counting unanswered questions 
			ArrayList<Microtask> microtasks = workerSession.getMicrotaskList();
			for (Microtask microtask : microtasks) {
				Integer currentColumn = this.questionsInSheet.get(microtask.getID());
				if (null == currentColumn)
					return false;
				if (microtask.getAnswerList().isEmpty())
					this.lineContent[currentColumn] = (Integer)this.lineContent[currentColumn] + 1;	
			}
		}
		this.data.put(this.key++, this.lineContent);
		
		/* creating excel workbook */
		//Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		//Create a blank sheet (for the scores)
		XSSFSheet answerSheet = workbook.createSheet("Answers");

		int rownum = 0;	
		Row row;
		Cell cell;

		/* filling the Answer sheet */
		// Iterate over data and write to score sheet
		Set<Integer> keyset = data.keySet();
		for (Integer singleKey : keyset)
		{	// new row for each entry
			row = answerSheet.createRow(rownum++);
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
		for (short c=0; c<questionsInSheet.size(); c++){
			answerSheet.autoSizeColumn(c);
		}

		try
		{	//Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File("AnswersReport.xlsx"));
			workbook.write(out);
			out.flush();
			out.close();
			System.out.println("AnswersReport.xlsx written successfully on disk.");
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean populateDataLines(ArrayList<WorkerSession> workerSessions){
		this.lineContent = new Object[this.questionsInSheet.size()+2]; 
		for (WorkerSession workerSession : workerSessions) {
			this.lineContent[0] = workerSession.getId();				// first column
			this.lineContent[1] = workerSession.getOriginalId();		// second column
			ArrayList<Microtask> microtasks = workerSession.getMicrotaskList();
			for (Microtask microtask : microtasks) {
				Integer currentColumn = this.questionsInSheet.get(microtask.getID());
				if (null == currentColumn)
					return false;
				if (microtask.getAnswerList().isEmpty())
					this.lineContent[currentColumn] = "?";	
				else
					this.lineContent[currentColumn] = microtask.getAnswerList().get(0).getOption();	// the one single answer
			}
			this.data.put(this.key++, this.lineContent);
		}
		return true;
	}
	
	private void populateQuestionMap(ArrayList<Microtask> microtasksPerWorker){
		for (Microtask microtask : microtasksPerWorker) {
			if ( !this.questionsInSheet.containsKey(microtask.getID()) ){	
				this.questionsInSheet.put(microtask.getID(), this.columnNumber++);
			}	// if contains the question, do nothing
		}
	}
}