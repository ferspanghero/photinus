package edu.uci.ics.sdcl.firefly.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import edu.uci.ics.sdcl.firefly.SkillTest;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

import java.lang.StringBuffer;

public class SkillTestStorage {
	
	PropertyManager manager = PropertyManager.initializeSingleton();
	private String testsPath = manager.skillTestUploadPath;

	// Provides the exams
	private static List<SkillTest> skillTests = new ArrayList<SkillTest>();
	
	//Holds the relationship between files and skillTests
	private static Hashtable<String, SkillTest> skillFileTable = new Hashtable<String, SkillTest>();
	
	public SkillTestStorage(){
		if(skillTests.size() == 0)
		{
			loadSkillTests();

			// THIS IS A HARDCODED BIND OF FILES
			bindSkillTest("HIT01_8.java");
			bindSkillTest("HIT02_24.java");
			bindSkillTest("HIT03_6.java");
			bindSkillTest("HIT04_7.java");
			bindSkillTest("HIT05_35.java");
			bindSkillTest("HIT06_51.java");
			bindSkillTest("HIT07_33.java");
			bindSkillTest("HIT08_54.java");
			bindSkillTest("HIT10_59.java");
		}
	}


	private void loadSkillTests() {
		skillTests.add(createSkillTest("FindTop.java", "FindTopRubrics.txt"));
		skillTests.add(createSkillTest("PositionFinder.java", "PositionFinderRubrics.txt"));
		skillTests.add(createSkillTest("WordFinder.java", "WordFinderRubrics.txt"));
	}
	
	private SkillTest createSkillTest(String sourceCodeFile, String rubricsFile){
		SkillTest test = new SkillTest();
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
		StringBuffer buff = new StringBuffer();
		populateBuffer(buff,fileName);
		//System.out.println("In SkillTestSource, fileName= "+fileName);
		buff.delete(0, buff.indexOf("public class"));
		test.setSourceCode(buff);
	}
	
	private void loadQuestions(SkillTest test, String fileName)
	{
		StringBuffer buff = new StringBuffer();
		populateBuffer(buff, fileName);
		String[] lines = buff.toString().split("\\n");
		List<String> questions = new ArrayList<String>();
		for (int i = 1; i < lines.length; i+=4) {
			if(i<= lines.length)
				questions.add(lines[i].substring(lines[i].indexOf("Q:")+3, lines[i].length()-1));
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
				String[] aux = lines[i].split(", ");
				options.add(aux);
			}
		}
		test.setOptions((options.toArray(new String[options.size()][])));
	}

	/**
	 * Binds a file with a skill test.
	 * @param fileName: The desired file to be binded
	 */
	public void bindSkillTest(String fileName)
	{
		if(skillFileTable.get(fileName) == null) // avoid duplicated bind on the same file
		{
			int extensionIndex = fileName.indexOf('.');
			if(extensionIndex != -1)
				fileName = (String) fileName.subSequence(0, extensionIndex); // Avoid file extension
			
			int index = (skillFileTable.size() % skillTests.size()); // A circular queue
			System.out.println(fileName + "--" + index);
			skillFileTable.put(fileName, skillTests.get( index ));
		}
	}
	
	/**
	 * Gets the skill test related to a file
	 * 
	 * @param fileName: File name without the extension
	 * @return: A StringBuffer containing the skill test or null if no skill is related to the file
	 */
	public SkillTest getSource(String fileName)
	{
		return skillFileTable.get(fileName);
	}
}
