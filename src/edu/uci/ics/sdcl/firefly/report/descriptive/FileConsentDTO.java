package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.Worker;

public class FileConsentDTO extends ConsentDTO{

	//logPath: Should be path+fileName. Ex. "myFolder/myFile"
	private String logPath = "C:/Users/igMoreira/Desktop/Dropbox/1.CrowdDebug-Summer2015/sampleDatalogs/consent-log-TestSample.log";
	
	/**
	 * Will hold the data loaded from the database.
	 * Necessary not to keep connecting to the database.
	 */
	protected HashMap<String, Worker> workers = new HashMap<String, Worker>();
	
	public FileConsentDTO() {
		
	}
	
	@Override
	public HashMap<String, Worker> getWorkers() {
		if((this.workers == null) || (this.workers.isEmpty()))
			loadWorkers();
		return this.workers;
	}

	@Override
	protected void loadWorkers() {
		BufferedReader log = null;
		try {
			log = new BufferedReader(new FileReader(logPath));
			String line = null;
			while((line = log.readLine()) != null)
			{
				if(line.equals(""))
					continue;
				String event = line.split("%")[1].replaceAll("\\s", "");
				switch (event.toUpperCase()) {
					case "CONSENT":loadConsent(line);break;
					case "SKILLTEST": loadSkillTest(line);break;
					case "SURVEY": loadSurvey(line); break;
					case "FEEDBACK": loadFeedback(line); break;
					case "QUIT": loadQuit(line); break;
					case "ERROR": break;
					default:
						System.out.println("DATABASE ERROR: Invalid event on log. EVENT = "+event);
						System.exit(0);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("DATABASE ERROR: File could not be opened");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("DATABASE ERROR: Couldn't read file");
			e.printStackTrace();
			System.exit(0);
		}
		finally
		{
			try {
				log.close();
			} catch (IOException e) {
				System.out.println("DATABASE ERROR: Could not close file");
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	/**
	 * Load the survey info and sets the
	 * content to the corresponding worker on
	 * the hashMap
	 * @param line: line containing the survey info
	 */
	private void loadSurvey(String line)
	{
		String result[] = line.split("%");
		String workerId = result[3].trim();
		Worker worker = this.workers.get(workerId);
		if(worker != null)
		{
			for(int i=6; i< result.length-1; i+=2)
			{
				worker.addSurveyAnswer(result[i].trim(), result[i+1].trim());
			}
		}
	}
	
	/**
	 * Load the skill test info and sets the
	 * content to the corresponding worker on
	 * the hashMap
	 * @param line: line containing the skill test info
	 */
	private void loadSkillTest(String line)
	{
		String[] result = line.split("%");
		String workerId = result[3].trim();
		Worker worker = this.workers.get(workerId);
		if ((worker != null)) {
			boolean[] gradeMap = new boolean[5];
			for (int i=0, j=0; i < result.length; i++) {
				if(result[i].trim().equalsIgnoreCase("false") || result[i].trim().equalsIgnoreCase("true"))
				{
					gradeMap[j++] = Boolean.valueOf(result[i].trim());
				}
				else if(result[i].trim().equalsIgnoreCase("grade"))
					worker.setGrade(Integer.parseInt(result[i+1].trim()));
				else if(result[i].trim().equalsIgnoreCase("testDuration"))
					worker.setSkillTestDuration(result[i+1].trim());
			}
			worker.setGradeMap(gradeMap);
		}
	}
	
	/**
	 * Load the worker consent info and
	 * add in the workers hashMap
	 * @param line: The line of the log containing the consent info
	 */
	private void loadConsent(String line)
	{
		String[] result = line.split("%");
		Worker worker = new Worker(result[3].trim(), result[5].trim(), null);
		worker.setConsentDate(result[7].trim());
		worker.setCurrentFileName(result[5].trim());
		if(!(this.workers.containsKey(worker.getWorkerId())))
		{
			this.workers.put(worker.getWorkerId(), worker);
		}
	}
	
	/**
	 * Load the worker feedback info and
	 * add in the designated worker.
	 * @param line: line containing the feedback data
	 */
	private void loadFeedback(String line)
	{
		String[] result = line.split("%");
		String workerId = result[3].trim();
		Worker worker = this.workers.get(workerId);
		if(worker != null)
		{
			for (int i = 0; i < result.length; i++) {
				if(result[i].trim().equalsIgnoreCase("feedback"))
					worker.addSurveyAnswer("feedback", (i == (result.length -1)) ? "" : result[i+1]);
			}
		}
	}
	
	/**
	 * Load the worker quit info and
	 * add in the designated worker.
	 * @param line: line containing the quit data
	 */
	private void loadQuit(String line)
	{
		String[] result = line.split("%");
		String workerId = result[3];
		Worker worker = this.workers.get(workerId);
		if(worker != null)
		{
			//for (int i = 0; i < result.length; i++) {
			//	if(result[i].trim().equalsIgnoreCase("answer"))
			//		worker.addQuitReason((i == (result.length -1)) ? "" : result[i+1].trim());
			//}
			worker.addQuitFileList(result[5]);
			worker.addQuitReason(result[7]);		
		}
		
		
	}

}
