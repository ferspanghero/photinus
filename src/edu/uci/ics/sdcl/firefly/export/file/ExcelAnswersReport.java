package edu.uci.ics.sdcl.firefly.export.file;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
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
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;

public class ExcelAnswersReport {
	private HashMap<Integer, Integer> questionsInSheet;
	private Integer columnNumber;
	
	public ExcelAnswersReport() {
		this.questionsInSheet = new HashMap<Integer, Integer>();
		this.columnNumber = 0;
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
		Set<Map.Entry<Integer, WorkerSession>> setActiveSessions = activeSessions.entrySet();
		Iterator<Entry<Integer, WorkerSession>> iterateActiveSessions = setActiveSessions.iterator();
		while(iterateActiveSessions.hasNext())
		{
			Map.Entry<Integer, WorkerSession> mapEntryActiveSessions = (Map.Entry<Integer, WorkerSession>)iterateActiveSessions.next();
			this.populateQuestionMap(mapEntryActiveSessions.getValue().getMicrotaskList());
		}
		// reading new session's questions
		Stack<WorkerSession> newCopiesSessions = (Stack<WorkerSession>)allWorkerSessions.get(WorkerSessionStorage.NEW_COPIES);
		for (WorkerSession workerSession : newCopiesSessions) {
			this.populateQuestionMap(workerSession.getMicrotaskList());
		}
		Stack<WorkerSession> newSessions = (Stack<WorkerSession>)allWorkerSessions.get(WorkerSessionStorage.NEW);
		for (WorkerSession workerSession : newSessions) {
			this.populateQuestionMap(workerSession.getMicrotaskList());
		}
		
		/* preparing the data structure */
		int key = 0;	// for the 'data' below
		Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
		Object[] lineContent = new Object[this.questionsInSheet.size()+2];	// represents the lines on the sheet
		/* starting by the closed sessions */								// +2 for the 2 IDs before questions
		for (WorkerSession workerSession : closedSessions) {
			lineContent[0] = workerSession.getId();				// first column
			lineContent[1] = workerSession.getOriginalId();		// second column
			ArrayList<Microtask> microtasks = workerSession.getMicrotaskList();
			for (Microtask microtask : microtasks) {
				Integer currentColumn = this.questionsInSheet.get(microtask.getID());
				if (null == currentColumn)
					return false;
				if (microtask.getAnswerList().isEmpty())
					lineContent[currentColumn] = "?";	// getting the one single answer
				else
					lineContent[currentColumn] = microtask.getAnswerList().get(0);	// the one single answer
			}
		}
		
		/* creating excel workbook */
		//Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		//Create a blank sheet (for the scores)
		XSSFSheet answerSheet = workbook.createSheet("Answers");

		
		int rownum = 0;	
		Row row;
		
		

		/* creating first header line */
		row = answerSheet.createRow(rownum++);
		int cellNum = 0;
		Cell cell = row.createCell(cellNum++);
		cell.setCellValue("Worker ID");
		cell = row.createCell(cellNum++);
		cell.setCellValue("HIT ID");
		cell = row.createCell(cellNum++);
		cell.setCellValue("ID");
		cell = row.createCell(cellNum++);
		
		
		// creating new columns for the questions
		for (Integer k=1; k<=250; k++){				// this number should be a variable
			cell.setCellValue("Q" + k.toString());
			cell = row.createCell(cellNum++);
		}
		
		/*
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
		
		/* filling the score sheet /
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
		} */
		// sizing columns for method sheet
		for (short c=0; c<253; c++){
			answerSheet.autoSizeColumn(c);
		}

		try
		{
			//Write the workbook in file system
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
	
	private void populateQuestionMap(ArrayList<Microtask> microtasksPerWorker){
		for (Microtask microtask : microtasksPerWorker) {
			if ( !this.questionsInSheet.containsKey(microtask.getID()) ){	
				this.questionsInSheet.put(microtask.getID(), this.columnNumber++);
			}	// if contains the question, do nothing
		}
	}
}
