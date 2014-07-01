package test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.CodeElement;
import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.MethodParameter;
import edu.uci.ics.sdcl.firefly.MethodSignature;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.memento.MicrotaskMemento;

public class MicrotaskMementoTest {

	private  HashMap<String, HashMap<Integer, Microtask>> debugSessionMicrotaskMap = new HashMap<String, HashMap<Integer,Microtask>>();
	private HashMap<Integer, Microtask> microtaskMap;

	@Before
	public void setUp() throws Exception {
		MethodSignature signature = new MethodSignature("factorial", "public", new Integer(12));
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

		String questionArg = "Is there maybe something wrong in the declaration of function 'factorial' at line 12 " 
				+ "(e.g., requires a parameter that is not listed, needs different parameters to produce the correct result, specifies the wrong or no return type, etc .)?";
		
		CodeSnippet codeSnippetFactorial=new CodeSnippet("sample","SimpleSampleCode", buffer.toString(), new Integer (12),
				new Boolean (true), signature);
		Microtask mtask = new Microtask(CodeElement.METHOD_DECLARARION, codeSnippetFactorial, questionArg, new Integer(12));

		//Create the data structure
		this.microtaskMap =  new HashMap<Integer,Microtask>();
		microtaskMap.put(new Integer(1),mtask);

		this.debugSessionMicrotaskMap.put("SimpleSampleCode.java", microtaskMap);
	}

	@Test
	public void testCreateNewPersistentFile() {

		MicrotaskMemento memento = new MicrotaskMemento();
		memento.insert("SimpleSampleCode.java", this.debugSessionMicrotaskMap.get("SimpleSampleCode.java"));

		HashMap<Integer, Microtask> mMap = memento.read("SimpleSampleCode.java");
		if (mMap!=null){
			Integer key = new Integer (1);
			Microtask expectedTask = this.microtaskMap.get(key);
			Microtask actualTask = this.microtaskMap.get(key);
			
			assertEquals(expectedTask.getMethod().getClassName(),actualTask.getMethod().getClassName().toString());
		}
		else
			fail("review test setup");
	}

}
