package edu.uci.ics.sdcl.firefly.storage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class SkillTestSource {

	private StringBuffer sourceOne;
	private StringBuffer sourceTwo;
	
	// Provides the exams
	private static List<StringBuffer> skillTests = new ArrayList<StringBuffer>();
	
	//Holds the relationship between files and skillTests
	private static Hashtable<String, StringBuffer> skillFileTable = new Hashtable<String, StringBuffer>();
	
	public SkillTestSource(){
		if(skillTests.size() == 0)
		{
			skillTests.add(createSourceOne());
			skillTests.add(createSourceTwo());
		}
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

	public StringBuffer getSourceOne(){
		return this.sourceOne;
	}

	public StringBuffer getSourceTwo(){
		return this.sourceTwo;
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
			if(extensionIndex != 1)
				fileName = (String) fileName.subSequence(0, extensionIndex); // Avoid file extension
			
			int index = (skillFileTable.size() % skillTests.size()); // A circular queue
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
