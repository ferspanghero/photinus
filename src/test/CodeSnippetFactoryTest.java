package test;

import java.util.ArrayList;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.CodeElement;
import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.CodeSnippetFactory;
import edu.uci.ics.sdcl.firefly.MethodParameter;
import edu.uci.ics.sdcl.firefly.MethodSignature;
import edu.uci.ics.sdcl.firefly.SourceFileReader;

public class CodeSnippetFactoryTest {

	private CodeSnippet codeSnippetFactorial;
	private CodeSnippet codeSnippetConstructor;
	
	@Before
	public void setUp()  {
					
		MethodSignature signature = new MethodSignature("factorial", "public");
		MethodParameter arg1, arg2;
		arg1 = new MethodParameter("Integer", "Seed");
		arg2 = new MethodParameter("Integer", "Iterations");
		signature.addMethodParameters(arg1);
		signature.addMethodParameters(arg2);
			
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
		
		String body = buffer.toString().substring(buffer.toString().indexOf('{'));
		
		this.codeSnippetFactorial=new CodeSnippet("sample", "SimpleSampleCode", signature, body, true,
				12, 0, 12, 58, 12, 59, 21, 0);
			
		CodeElement element = new CodeElement(CodeElement.IF_CONDITIONAL, 14, 0, 14, 15, 14, 15, 20, 17);
		codeSnippetFactorial.addElement(element);
		element = new CodeElement(CodeElement.FOR_LOOP, 15, 0, 15, 42, 15, 42, 17, 0);
		codeSnippetFactorial.addElement(element);
		element = new CodeElement(CodeElement.METHOD_INVOCATION, 15, 26, 15, 36);
		codeSnippetFactorial.addElement(element);
		
		//Second CodeSnippet
		signature = new MethodSignature("SimpleSampleCode", "public");
		arg1 = new MethodParameter("Integer", "seedValue");
		signature.addMethodParameters(arg1);
		
		buffer = new StringBuffer("public SimpleSampleCode(Integer seedValue) {");
		buffer.append("\n");
		buffer.append("if(SeedLimit == null)");
		buffer.append("\n");
		buffer.append("this.SeedLimit = new Integer(seedValue);");
		buffer.append("\n");
		buffer.append("}");
		
		body = buffer.toString().substring(buffer.toString().indexOf('{'));
		
		this.codeSnippetConstructor=new CodeSnippet("sample", "SimpleSampleCode", signature, body, true, 
				7, 0, 10, 27, 7, 27, 10, 0);
		
		element = new CodeElement(CodeElement.IF_CONDITIONAL, 8, 0, 8, 11, 9, 0, 9, 15);
		codeSnippetConstructor.addElement(element);
		
	}

	@Test
	public void test() {
		
		String folderPath = "C:/Users/Christian Adriano/Documents/GitHub/crowd-debug-firefly/src/sample/JustOneSample/";
		String fileName = "SimpleSampleCode.java";
		String fileContent = SourceFileReader.readFileToString(folderPath+fileName);
		CodeSnippetFactory factory = new CodeSnippetFactory(fileName,fileContent);

		Vector<CodeSnippet> list = factory.generateSnippetsForFile();
		if((list ==null ) || (list.size()!=3))
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
