package edu.uci.ics.sdcl.firefly.report.descriptive;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.Worker;

public class FileConsentDTO extends ConsentDTO{

	/**
	 * Will hold the data loaded from the database.
	 * Necessary not to keep connecting to the database.
	 */
	protected HashMap<String, Worker> workers = new HashMap<String, Worker>();
	
	/**
	 * Line containing the path of the log file
	 */
	private final String logPath;
	
	/**
	 * CONSTRUCTOR
	 * @param logPath: Should be path+fileName. Ex. "myFolder/myFile"
	 */
	public FileConsentDTO(String logPath) {
		this.logPath = logPath;
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
				String event = line.split("%")[1];
				switch (event.toUpperCase()) {
					case "CONSENT":
						loadConsent(line);
						break;
					case "SKILLTEST":
						loadSkillTest(line);
						break;
					case "SURVEY":
						loadSurvey(line);
						break;
					default:
						System.out.println("DATABASE ERROR: Invalid event on log");
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
		String workerId = result[3];
		Worker worker = this.workers.get(workerId);
		if(worker != null)
		{
			for(int i=0; i< result.length; i++)
			{
				switch(result[i])
				{
				case "sessionId":
					worker.setSessionId(result[i+1]);
					break;
				case "Feedback":
					worker.setFeedback(result[i+1]);
					break;
				case "Gender":
					worker.setGender(result[i+1]);
					break;
				case "Years progr.":
					worker.setYearsProgramming(Integer.parseInt(result[i+1]));
					break;
				case "Difficulty":
					worker.setDifficulty(Integer.parseInt(result[i+1]));
					break;
				case "Country":
					worker.setCountry(result[i+1]);
					break;
				case "Age":
					worker.setAge(Integer.parseInt(result[i+1]));
					break;
				}
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
		String workerId = result[3];
		Worker worker = this.workers.get(workerId);
		if ((worker != null)) {
			boolean[] gradeMap = new boolean[5];
			for (int i=0, j=0; i < result.length; i++) {
				if(result[i].equalsIgnoreCase("false") || result[i].equalsIgnoreCase("true"))
				{
					gradeMap[j++] = Boolean.valueOf(result[i]);
				}
				else if(result[i].equalsIgnoreCase("grade"))
					worker.setGrade(Integer.parseInt(result[i+1]));
				else if(result[i].equalsIgnoreCase("testDuration"))
					worker.setSkillTestDuration(result[i+1]);
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
		Worker worker = new Worker(result[3], result[5], null);
		if(!(this.workers.containsKey(worker.getWorkerId())))
		{
			this.workers.put(worker.getWorkerId(), worker);
		}
	}

}
