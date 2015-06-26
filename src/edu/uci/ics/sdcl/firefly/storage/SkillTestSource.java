package edu.uci.ics.sdcl.firefly.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import edu.uci.ics.sdcl.firefly.util.PropertyManager;

import java.lang.StringBuffer;

public class SkillTestSource {
	
	PropertyManager manager = PropertyManager.initializeSingleton();
	private String testsPath = manager.skillTestUploadFolder;

	// Provides the exams
	private static List<StringBuffer> skillTests = new ArrayList<StringBuffer>();
	
	//Holds the relationship between files and skillTests
	private static Hashtable<String, StringBuffer> skillFileTable = new Hashtable<String, StringBuffer>();
	
	public SkillTestSource(){
		if(skillTests.size() == 0)
		{
			addSourcesToSkillTest(skillTests);
		}
	}


	private void addSourcesToSkillTest(List<StringBuffer> skillTests) {
		skillTests.add(createSourceOne());
		skillTests.add(createSourceTwo());
		skillTests.add(createSourceThree());
		skillTests.add(createSourceFour());
		skillTests.add(createSourceFive());
	}


	private StringBuffer createSourceOne(){
		StringBuffer buffer = new StringBuffer("public class PositionFinder {");
		buffer.append("\n\n");
		buffer.append("	public static void findPosition(String[] NameArray, String term){");
		buffer.append("\n");
		buffer.append("		if (NameArray.length==0 && NameArray==null)");
		buffer.append("\n");
		buffer.append("			System.out.print(\"Empty array\");");
		buffer.append("\n");
		buffer.append("		else{ ");
		buffer.append("\n");
		buffer.append("			int position=-1;");
		buffer.append("\n");
		buffer.append("			for (String name: NameArray){");
		buffer.append("\n");
		buffer.append("				if(name.compareTo(term)==0)");
		buffer.append("\n");
		buffer.append("					break;	");
		buffer.append("\n");
		buffer.append("				position++;");
		buffer.append("\n");
		buffer.append("			}");
		buffer.append("\n");
		buffer.append("			System.out.print(position > 0? position: 0);");
		buffer.append("\n");
		buffer.append("		}");
		buffer.append("\n");
		buffer.append("	}");
		buffer.append("\n");
		buffer.append("	public static void main(String[] args){");
		buffer.append("\n");
		buffer.append("		String[] NameArray = {\"Hola\", \"Kumusta\", \"Hello\", \"Ciao\"};");
		buffer.append("\n");
		buffer.append("		findPosition(NameArray, \"Ciao\");");
		buffer.append("\n");
		buffer.append("	}");
		buffer.append("\n");
		buffer.append("}");

		return buffer;
	}


	private StringBuffer createSourceTwo(){
		StringBuffer buffer = new StringBuffer();		buffer.append("\n\n");
		buffer.append("import java.util.*;");		buffer.append("\n\n");

		buffer.append("public class PrintDatastructure {");		buffer.append("\n\n");

		buffer.append("	public static void main(String[] args){");		buffer.append("\n\n");

		buffer.append("		HashMap<String, String[]> rootMap =  new HashMap<String, String[]>();");		buffer.append("\n");
		buffer.append("		String[] list1 = { \"A\",\"B\"} ;");		buffer.append("\n");
		buffer.append("		String[] list2 = {};");		buffer.append("\n\n");

		buffer.append("		rootMap.put(\"0\", list1);");		buffer.append("\n");
		buffer.append("		rootMap.put(\"1\", null);");		buffer.append("\n");
		buffer.append("		rootMap.put(\"2\", list2);");		buffer.append("\n\n");

		buffer.append("		printData(rootMap);");		buffer.append("\n");
		buffer.append("	}");		buffer.append("\n\n");

		buffer.append("	public static void printData(HashMap<String, String[]> map){");		buffer.append("\n\n");

		buffer.append("		Iterator<String> keySet = map.keySet().iterator();");		buffer.append("\n");
		buffer.append("		while(keySet.hasNext()){");		buffer.append("\n");
		buffer.append("			String key =  keySet.next();");		buffer.append("\n");
		buffer.append("			System.out.print(key);");		buffer.append("\n");
		buffer.append("			String[] list = map.get(key);");		buffer.append("\n");
		buffer.append("			if((list==null) || list.length==0)");		buffer.append("\n");
		buffer.append("				System.out.print(\"!\");");		buffer.append("\n");
		buffer.append("			else	");		buffer.append("\n");
		buffer.append("				for(int i= list.length-1;i>=0;i--)");		buffer.append("\n");
		buffer.append("					System.out.print(list[i]);");		buffer.append("\n");
		buffer.append("		}");		buffer.append("\n");
		buffer.append("		System.out.println(\";\");");				buffer.append("\n");
		buffer.append("	}");		buffer.append("\n");
		buffer.append("}");		


		return buffer;
	}
	
	private StringBuffer createSourceThree(){
		StringBuffer buff = new StringBuffer();
		populateBuffer(buff,"FindTop.java");
		return buff;
	}
	
	private StringBuffer createSourceFour(){
		StringBuffer buff = new StringBuffer();
		populateBuffer(buff,"PositionFinder.java");
		return buff;
	}
	
	private StringBuffer createSourceFive(){
		StringBuffer buff = new StringBuffer();
		populateBuffer(buff,"WordFinder.java");
		return buff;
	}
	
	
	private void populateBuffer(StringBuffer buff, String fileName){
		try {
			Scanner scanner = new Scanner(new File(testsPath+"\\"+fileName));
			String content = scanner.useDelimiter("\\Z").next();
			buff.append(content);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch bloc
			e.printStackTrace();
		}
	}

	/**
	 * Binds a file with a skill test.
	 * @param fileName: The desired file to be binded
	 */
	public void bindSkillTest(String fileName)
	{
		if(skillFileTable.get(fileName) == null)
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
	public StringBuffer getSource(String fileName)
	{
		return skillFileTable.get(fileName);
	}
}
