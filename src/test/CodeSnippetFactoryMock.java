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
		MethodSignature signature = new MethodSignature("factorial", "public");
		
		StringBuffer buffer = new StringBuffer("public Integer factorial(Integer Seed, Integer Iterations){"); //12
		buffer.append("\n");				
		buffer.append("if(Seed!=null){");	// 13
		buffer.append("\n");				
		buffer.append("int aux=1;");		// 14
		buffer.append("\n");	
		buffer.append("for (int i=0;i<Iterations.intValue();i++){"); // 15
		buffer.append("\n");
		buffer.append("aux =  aux * Seed;"); // 16
		buffer.append("\n");
		buffer.append("	}");				 // 17
		buffer.append("\n");
		buffer.append("	return new Integer(aux);"); // 18
		buffer.append("\n");
		buffer.append("}");					// 19
		buffer.append("\n");
		buffer.append("else return null;");	// 20
		buffer.append("\n");
		buffer.append("}");					// 21
		
		String body = buffer.toString().substring(buffer.toString().indexOf('{'));
		
		this.codeSnippetFactorial=new CodeSnippet("sample", "SimpleSampleCode", signature, body, true,
				12, 0, 12, 58, 12, 59, 21, 0);
		
//		CodeElement element = new CodeElement(CodeElement.METHOD_DECLARARION, 12, 0, 12, 58);
//		codeSnippetFactorial.addElement(element);
		CodeElement element = new CodeElement(CodeElement.IF_CONDITIONAL, 14, 0, 14, 15, 14, 15, 20, 17);
		codeSnippetFactorial.addElement(element);
		element = new CodeElement(CodeElement.FOR_LOOP, 15, 0, 15, 42, 15, 42, 17, 0);
		codeSnippetFactorial.addElement(element);
		element = new CodeElement(CodeElement.METHOD_INVOCATION, 15, 26, 15, 36);
		codeSnippetFactorial.addElement(element);
		
		this.list.add(codeSnippetFactorial);
		
		//Second CodeSnippet
		signature = new MethodSignature("SimpleSampleCode", "public");
		
		buffer = new StringBuffer("public SimpleSampleCode() {"); 	// 7
		buffer.append("\n");	
		buffer.append("if(SeedLimit == null)");						// 8
		buffer.append("\n");
		buffer.append("this.SeedLimit = new Integer(10);");			// 9
		buffer.append("\n");
		buffer.append("}");											// 10
		
		body = buffer.toString().substring(buffer.toString().indexOf('{'));
		
		this.codeSnippetConstructor=new CodeSnippet("sample", "SimpleSampleCode", signature, body, true, 
				7, 0, 7, 27, 7, 27, 10, 0);
		
		element = new CodeElement(CodeElement.IF_CONDITIONAL, 8, 0, 8, 11, 9, 0, 9, 15);
		codeSnippetConstructor.addElement(element);
		
		this.list.add(codeSnippetConstructor);
		
	}
	
	public ArrayList<CodeSnippet> generateSnippets(String folderPath){
		return this.list;
	}
	
	
}
