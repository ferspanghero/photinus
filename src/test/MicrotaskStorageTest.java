package test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.CodeElement;
import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;

public class MicrotaskStorageTest {

	FileDebugSession session1;
	FileDebugSession session2;
	String fileName = "SimpleSampleCode";
	private String path = ".";
	
	@Before
	public void setup(){
		
		StringBuffer buffer = new StringBuffer("public SimpleSampleCode(Integer seedValue) {");
		buffer.append("\n");
		buffer.append("if(SeedLimit == null)");
		buffer.append("\n");
		buffer.append("this.SeedLimit = new Integer(seedValue);");
		buffer.append("\n");
		buffer.append("}");
		
		String fileContent= buffer.toString();
		
		CodeSnippet dummySnippet = new CodeSnippet("sample", "SimpleSampleCode", null, "body", true, 
				7, 0, 10, 27, 7, 27, 10, 0);
		
		Microtask questionMethodCallConstructor = new Microtask(CodeElement.METHOD_INVOCATION, 
				dummySnippet, "Is there maybe something wrong in the declaration of function "
						+ "'SimpleSampleCode' at line 7 (e.g., requires a parameter that is not listed, needs "
						+ "different parameters to produce the correct result, specifies the wrong or no return type, "
						+ "etc .)?", 7, 0, 7, 27,1);

		HashMap<Integer,Microtask> microtaskMap = new HashMap<Integer,Microtask>();
		microtaskMap.put(new Integer(0),  questionMethodCallConstructor);
		session1 =  new FileDebugSession( fileName,  fileContent,microtaskMap );
		

		Microtask constructorBody = new Microtask(CodeElement.METHOD_DECLARATION, dummySnippet, "Is there possibly "
				+ "something wrong with the body of function 'SimpleSampleCode' between lines 7 and 9 (e.g., "
				+ "function produces an incorrect return value, return statement is at the wrong place, does not "
				+ "properly handle error situations, etc.)?" , 7, 27, 10, 1,2);
		
		microtaskMap = new HashMap<Integer,Microtask>();
		microtaskMap.put(new Integer(1),  constructorBody);
		session2 =  new FileDebugSession( fileName,  fileContent,microtaskMap);
		
		
	}
	@Test
	public void test() {
		
		//Clean up the storage
		MicrotaskStorage storage = new MicrotaskStorage(path);
		storage.remove(fileName);
		
		storage.insert(fileName, session1);
		storage.insert(fileName, session2);
		
		FileDebugSession session = storage.read(fileName);
		
		assertEquals(2, session.getNumberOfMicrotasks());
		
	}

}
