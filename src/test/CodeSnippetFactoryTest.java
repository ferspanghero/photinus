package test;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.CodeElement;
import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.CodeSnippetFactory;
import edu.uci.ics.sdcl.firefly.MethodSignature;

public class CodeSnippetFactoryTest {

	private CodeSnippet codeSnippetFactorial;
	private CodeSnippet codeSnippetConstructor;
	
	@Before
	public void setUp()  {
					
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
		
		//Second CodeSnippet
		signature = new MethodSignature("SimpleSampleCode", "public", new Integer(7));
		
		buffer = new StringBuffer("public SimpleSampleCode() {");
		buffer.append("\n");
		buffer.append("if(SeedLimit == null)");
		buffer.append("\n");
		buffer.append("this.SeedLimit = new Integer(10);");
		buffer.append("\n");
		buffer.append("}");
		
		this.codeSnippetConstructor=new CodeSnippet("sample","SimpleSampleCode",buffer,
				new Boolean (true), signature);
		
		element = new CodeElement(CodeElement.METHOD_NAME,new Integer(7));
		codeSnippetConstructor.addElement(element);
		
	}

	@Test
	public void test() {
		CodeSnippetFactory factory = new CodeSnippetFactory(
				"C:/Users/Danilo/Documents/GitHub/crowd-debug-firefly/src/sample/JustOneSample");
		ArrayList<CodeSnippet> list = factory.generateSnippets();
		if((list ==null ) || (list.size()!=2))
			Assert.fail("Null list of snippets or file does not match test data");
		else{
			boolean match=false;
			CodeSnippet snippet1 = list.get(0);
			CodeSnippet snippet2 = list.get(1);
			if((snippet1.isEqualTo(codeSnippetConstructor)) || (snippet1.isEqualTo(codeSnippetFactorial)) && 
				(snippet2.isEqualTo(codeSnippetConstructor)) || (snippet2.isEqualTo(codeSnippetFactorial))){
					match=true;
				}
			Assert.assertTrue("Snippet for Factorial Method does match", match);
			}
	}

}
