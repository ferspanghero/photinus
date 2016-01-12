package edu.uci.ics.sdcl.firefly.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import edu.uci.ics.sdcl.firefly.SkillTest;
import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

public class SkillTestStorage {
	
	PropertyManager manager = PropertyManager.initializeSingleton();
	private String testsPath = manager.skillTestUploadPath;

	// Provides the exams
	private static Hashtable<String, SkillTest> skillTests = new Hashtable<>();
	
	//Holds the relationship between files and skillTests
	private static Hashtable<String, List<SkillTest>> skillFileTable = new Hashtable<String, List<SkillTest>>();
	
	private static Hashtable<String, SkillTest> skillTestWorkerTable = new Hashtable<>();
	
	public SkillTestStorage(){
		if(skillTests.size() == 0)
		{
			loadSkillTests();

			bindSkillTest("AR", "FindTop");
			bindSkillTest("AR", "PositionFinder");
			bindSkillTest("AR", "WordFinder");
			bindSkillTest("UI", "UI1");
			bindSkillTest("UI", "UI2");
			bindSkillTest("UI", "UI3");
			bindSkillTest("UI", "UI4");
			
			// THIS IS A HARDCODED BIND OF FILES
//			bindSkillTest("HIT01_8.java");
//			bindSkillTest("HIT02_24.java");
//			bindSkillTest("HIT03_6.java");
//			bindSkillTest("HIT04_7.java");
//			bindSkillTest("HIT05_35.java");
//			bindSkillTest("HIT06_51.java");
//			bindSkillTest("HIT07_33.java");
//			bindSkillTest("HIT08_54.java");
//			bindSkillTest("HIT10_59.java");
		}
	}


	private void loadSkillTests() {
		skillTests.put("FindTop", createSkillTest("FindTop.java", "FindTopRubrics.txt", "FindTop"));
		skillTests.put("PositionFinder", createSkillTest("PositionFinder.java", "PositionFinderRubrics.txt", "PositionFinder"));
		skillTests.put("WordFinder", createSkillTest("WordFinder.java", "WordFinderRubrics.txt", "WordFinder"));
		skillTests.put("UI1", createSkillTest("", "UI1Rubrics.txt", "UI1"));
		skillTests.put("UI2", createSkillTest("", "UI2Rubrics.txt", "UI2"));
		skillTests.put("UI3", createSkillTest("", "UI3Rubrics.txt", "UI3"));
		skillTests.put("UI4", createSkillTest("", "UI4Rubrics.txt", "UI4"));
	}
	
	private SkillTest createSkillTest(String sourceCodeFile, String rubricsFile, String name){
		SkillTest test = new SkillTest(name);
		loadSourceCode(test, sourceCodeFile);
		loadQuestions(test, rubricsFile);
		loadOptions(test, rubricsFile);
		loadAnswers(test, rubricsFile);
		return test;
	}
	
	
	private void populateBuffer(StringBuffer buff, String fileName){
		try {
			Scanner scanner = new Scanner(new File(testsPath+"/"+fileName));
			String content = scanner.useDelimiter("\\Z").next();
			buff.append(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void loadSourceCode(SkillTest test, String fileName)
	{
		if (fileName != null && !fileName.isEmpty()) {		
			StringBuffer buff = new StringBuffer();
			populateBuffer(buff,fileName);
			//System.out.println("In SkillTestSource, fileName= "+fileName);
			buff.delete(0, buff.indexOf("public class"));
			test.setSourceCode(buff);
		}
	}
	
	private void loadQuestions(SkillTest test, String fileName)
	{
		StringBuffer buff = new StringBuffer();
		populateBuffer(buff, fileName);
		String[] lines = buff.toString().split("\\n");
		List<String> questions = new ArrayList<String>();
		for (int i = 1; i < lines.length; i+=4) {
			if(i<= lines.length) {
				String questionLine = lines[i].substring(lines[i].indexOf("Q:")+3, lines[i].length()-1);
				
				questions.add(questionLine); 
			}
		}
		test.setQuestions((questions.toArray(new String[questions.size()])));
	}
	
	private void loadAnswers(SkillTest test, String fileName)
	{
		StringBuffer buff = new StringBuffer();
		populateBuffer(buff, fileName);
		String[] lines = buff.toString().split("\\n");
		List<String> answers = new ArrayList<String>();
		for (int i = 3; i < lines.length; i+=4) {
			if(i<= lines.length)
			{
				lines[i] = lines[i].substring(lines[i].indexOf("A:")+3, lines[i].length());
				answers.add(lines[i].replace("\n", "").replace("\r", ""));
			}
		}
		test.setAnswers((answers.toArray(new String[answers.size()])));
	}
	
	private void loadOptions(SkillTest test, String fileName)
	{
		StringBuffer buff = new StringBuffer();
		populateBuffer(buff, fileName);
		String[] lines = buff.toString().split("\\n");
		List<String[]> options = new ArrayList<String[]>();
		for (int i = 2; i < lines.length; i+=4) {
			if(i<= lines.length)
			{
				lines[i] = lines[i].substring(lines[i].indexOf("O:")+3, lines[i].length());
				lines[i] = lines[i].replace("\n", "").replace("\r", "");
				String[] aux = lines[i].split("; ");
				options.add(aux);
			}
		}
		test.setOptions((options.toArray(new String[options.size()][])));
	}

	/**
	 * Binds a file with a skill test.
	 * @param fileName: The desired file to be binded
	 */
	public void bindSkillTest(String fileName, String testName)
	{
		int extensionIndex = fileName.indexOf('.');
		if(extensionIndex != -1)
			fileName = (String) fileName.subSequence(0, extensionIndex); // Avoid file extension
					
		if (!skillFileTable.containsKey(fileName)) {
			skillFileTable.put(fileName, new ArrayList<SkillTest>());
		}
		
		skillFileTable.get(fileName).add(skillTests.get(testName));
	}
	
	/**
	 * Gets the skill test related to a worker
	 * 
	 * @param worker: The worker
	 * @return: A StringBuffer containing the skill test or null if no skill is related to the worker
	 */
	public SkillTest getSource(Worker worker)
	{
		if (!skillTestWorkerTable.containsKey(worker.getWorkerId())) {
			int testIndex = 0;
			
			if (skillFileTable.get(worker.getCurrentFileName()).size() > 1)
				testIndex = ThreadLocalRandom.current().nextInt(0, skillFileTable.get(worker.getCurrentFileName()).size()); // upper bound is exclusive
						
			skillTestWorkerTable.put(worker.getWorkerId(), skillFileTable.get(worker.getCurrentFileName()).get(testIndex));
		}
		
		return skillTestWorkerTable.get(worker.getWorkerId());
	}
}
