package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.MethodParameter;
import edu.uci.ics.sdcl.firefly.MethodSignature;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.Question;
import edu.uci.ics.sdcl.firefly.memento.MicrotaskMemento;

public class MicrotaskMementoTest {

	private  HashMap<String,ArrayList<Microtask>> debugSessionMicrotaskMap = new HashMap<String, ArrayList<Microtask>>();
	private ArrayList<Microtask> microtaskList;
	
	@Before
	public void setUp() throws Exception {
		Question question = new Question();
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
		
		CodeSnippet codeSnippetFactorial=new CodeSnippet("sample","SimpleSampleCode", buffer, 
				new Boolean (true), signature);
		Microtask mtask = new Microtask(question, codeSnippetFactorial);
		
		//Create the data structure
		this.microtaskList =  new ArrayList<Microtask>();
		microtaskList.add(mtask);
		
		this.debugSessionMicrotaskMap.put("SimpleSampleCode.java", microtaskList);
	}

	@Test
	public void testCreateNewPersistentFile() {
		
		MicrotaskMemento memento = new MicrotaskMemento();
		memento.insert("SimpleSampleCode.java", this.debugSessionMicrotaskMap.get("SimpleSampleCode"));
		
		ArrayList<Microtask>mlist = memento.read("SimpleSampleCode");
		assertSame(this.microtaskList.get(0).getCode().getClassName(),mlist.get(0).getCode().getClassName());
	}

}
