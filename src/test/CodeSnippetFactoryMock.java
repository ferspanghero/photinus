package test;

import java.util.ArrayList;

import edu.uci.ics.sdcl.firefly.CodeElement;
import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.MethodSignature;

public class CodeSnippetFactoryMock {

	private ArrayList<CodeSnippet> list = new ArrayList<CodeSnippet>();
	private CodeSnippet codeSnippetFactorial;
	private CodeSnippet codeSnippetConstructor;
	
	public CodeSnippetFactoryMock(){
		MethodSignature signature = new MethodSignature("factorial", "public", new Integer(12));
		
		StringBuffer buffer = new StringBuffer("public Integer factorial(Integer Seed, Integer Iterations){");
		buffer.append("\n");
		buffer.append("if(Seed!=null){");
		buffer.append("\n");
		buffer.append("int aux=1;");
		buffer.append("\n");
		buffer.append("for (int i=0;i<Iterations.intValue();i++){");
		buffer.append("\n");
		buffer.append("aux =  aux * Seed;");
		buffer.append("\n");
		buffer.append("	}");
		buffer.append("\n");
		buffer.append("	return new Integer(aux);");
		buffer.append("\n");
		buffer.append("}");
		buffer.append("\n");
		buffer.append("else return null;");
		buffer.append("\n");
		buffer.append("}");
		
		this.codeSnippetFactorial=new CodeSnippet("sample","SimpleSampleCode", buffer, 
				new Boolean (true), signature);
		
		CodeElement element = new CodeElement(CodeElement.METHOD_NAME,new Integer(12));
		codeSnippetFactorial.addElement(element);
		element = new CodeElement(CodeElement.IF_CONDITIONAL,new Integer(14));
		codeSnippetFactorial.addElement(element);
		element = new CodeElement(CodeElement.FOR_LOOP,new Integer(16));
		codeSnippetFactorial.addElement(element);
		element = new CodeElement(CodeElement.RETURN_STATEMENT,new Integer(19));
		codeSnippetFactorial.addElement(element);
		element = new CodeElement(CodeElement.RETURN_STATEMENT,new Integer(21));
		codeSnippetFactorial.addElement(element);
		
		this.list.add(codeSnippetFactorial);
		
		//Second CodeSnippet
		signature = new MethodSignature("SimpleSampleCode", "public", new Integer(7));
		
		buffer = new StringBuffer("public SimpleSampleCode() {");
		buffer.append("\n");
		buffer.append("if(SeedLimit == null)");
		buffer.append("\n");
		buffer.append("this.SeedLimit = new Integer(10);");
		buffer.append("\n");
		buffer.append("}");
		
		this.codeSnippetConstructor=new CodeSnippet("sample","SimpleSampleCode", buffer, 
				new Boolean (true), signature);
		
		element = new CodeElement(CodeElement.METHOD_NAME,new Integer(7));
		codeSnippetConstructor.addElement(element);
		
		this.list.add(codeSnippetConstructor);
		
	}
	
	public ArrayList<CodeSnippet> generateSnippets(String folderPath){
		return this.list;
	}
	
	
}
