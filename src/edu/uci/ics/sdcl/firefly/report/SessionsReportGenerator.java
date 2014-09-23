package edu.uci.ics.sdcl.firefly.report;

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
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

public class SessionsReportGenerator {
	private HashMap<Integer, Integer> questionsInSheet;	// HashMap with the microtask's ID and respective column number
	private Integer columnNumber, key;
	private Map<Integer, Object[]> data;
	private Object[] lineContent;
		
	private String fileName = "SessionsReport.xlsx";
	
	public SessionsReportGenerator() {
		PropertyManager manager = new PropertyManager();
		String path = manager.reportPath;
		this.fileName = path + this.fileName;
		this.questionsInSheet = new HashMap<Integer, Integer>();
		this.columnNumber = 2;
		this.key = 0;	// for the 'data' map
		data = new TreeMap<Integer, Object[]>();
	}
	
	@SuppressWarnings("unchecked")
	public boolean writeToXlsx(HashMap<String, Object> allWorkerSessions)
	{
		/* setting a HashMap with the microtask's ID and the respective column number  */
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
		this.lineContent = new Object[this.questionsInSheet.size()+3];	// represents the lines on the sheet
		/* creating first header line */								// +2 for the 2 IDs before questions
		this.lineContent[0] = "User ID";
		this.lineContent[1] = "Session ID";
		
		Set<Map.Entry<Integer, Integer>> setQIS = this.questionsInSheet.entrySet();	// QIS same as question in sheet
		Iterator<Entry<Integer, Integer>> iterateQIS = setQIS.iterator();
		while(iterateQIS.hasNext())
		{
			Map.Entry<Integer, Integer> mapEntryQIS = (Map.Entry<Integer, Integer>)iterateQIS.next();
			Integer currentColumn = mapEntryQIS.getValue();
			if (null == currentColumn)	// critical error
				return false;
			this.lineContent[currentColumn] = mapEntryQIS.getKey();	
		}
		this.data.put(new Integer(this.key++), lineContent);
		/* getting data from the closed sessions */								
		if (!this.populateDataLines(closedSessions))
			return false;
		/* now from the active sessions */								
		if (!this.populateDataLines(activeSessionsAL))
			return false;
		/* now for the new sessions implement the not answered counter */
		this.lineContent = new Object[questionsInSheet.size()+3];
		Arrays.fill(this.lineContent, (Integer)0);	// populating the array with 0's
		this.lineContent[0] = "Unanswered";
		this.lineContent[1] = "-";
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
		this.data.put(new Integer(this.key++), this.lineContent);
		
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
					if (text.length() > 30)	// wrapping text
					{
						CellStyle style = workbook.createCellStyle(); //Create new style
						style.setWrapText(true); 	//Set word wrap
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
			FileOutputStream out = new FileOutputStream(new File(this.fileName));
			workbook.write(out);
			out.flush();
			out.close();
			System.out.println("SessionReport.xlsx written successfully on disk.");
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean populateDataLines(ArrayList<WorkerSession> workerSessions){
		for (WorkerSession workerSession : workerSessions) {
			this.lineContent = new Object[this.questionsInSheet.size()+2]; 
			this.lineContent[0] = workerSession.getUserId();			// worker ID
			this.lineContent[1] = workerSession.getId();				// session ID
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
			this.data.put(new Integer(this.key++), this.lineContent);
			/* for debug purposes 
			Set<Map.Entry<Integer, Object[]>> setActiveSessions = data.entrySet();
			Iterator<Entry<Integer, Object[]>> iterateActiveSessions = setActiveSessions.iterator();
			while(iterateActiveSessions.hasNext())
			{
				Map.Entry<Integer, Object[]> me = (Map.Entry<Integer, Object[]>)iterateActiveSessions.next();
				System.out.print(me.getKey() + " | ");
				for(Object obj : me.getValue()){
					System.out.print(obj + " | ");
				}
				System.out.println();
				
			}
			System.out.println();
			System.out.println(); */
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
