package edu.uci.ics.sdcl.firefly.storage;

public class SkillTestSource {

	private StringBuffer sourceOne;
	private StringBuffer sourceTwo;

	public SkillTestSource(){
		this.sourceOne = createSourceOne();
		this.sourceTwo = createSourceTwo();
	}


	private StringBuffer createSourceOne(){
		StringBuffer buffer = new StringBuffer("package pretask.test;");
		buffer.append("\n");
		buffer.append("	public static void findPosition(String[] NameArray, String term){");
		buffer.append("\n");
		buffer.append("		if (NameArray.length==0)");
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
		buffer.append("		findPosition(NameArray, \" \");");
		buffer.append("\n");
		buffer.append("	}");
		buffer.append("\n");
		buffer.append("}");

		return buffer;
	}


	private StringBuffer createSourceTwo(){
		StringBuffer buffer = new StringBuffer("package pretask.test;");		buffer.append("\n\n");
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
		buffer.append("}");		buffer.append("\n");


		return buffer;
	}

	public StringBuffer getSourceOne(){
		return this.sourceOne;
	}

	public StringBuffer getSourceTwo(){
		return this.sourceTwo;
	}





}
