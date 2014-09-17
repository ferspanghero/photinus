package test;

import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.CodeElement;
import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.MethodParameter;
import edu.uci.ics.sdcl.firefly.MethodSignature;
import edu.uci.ics.sdcl.firefly.Microtask;

/** 
 * Generates a map of microtasks for testing
 * @author Christian Adriano
 *
 */
public class QuestionFactoryMock {

	
	public static HashMap<Integer, Microtask> generateQuestions(){	
		HashMap<Integer, Microtask> microtaskMap = new HashMap<Integer, Microtask>();
		
		MethodSignature signature = new MethodSignature("seedCounter", "public", new Integer(12));
		MethodParameter arg1, arg2;
		arg1 = new MethodParameter("Integer", "Seed");
		arg2 = new MethodParameter("Integer", "Iterations");
		signature.addMethodParameters(arg1);
		signature.addMethodParameters(arg2);
		
		//--------------------------------------------
		//First code Snippet
		StringBuffer buffer = new StringBuffer("public Integer seedCounter(Integer Seed, Integer Iterations){");
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

		CodeSnippet codeSnippeSeedCounter=new CodeSnippet("sample.tiny", "TinySample", signature, body, true,
				12, 0, 12, 58, 12, 59, 21, 0);

		CodeElement element = new CodeElement(CodeElement.IF_CONDITIONAL, 14, 0, 14, 15, 14, 15, 20, 17);
		codeSnippeSeedCounter.addElement(element);
		element = new CodeElement(CodeElement.FOR_LOOP, 15, 0, 15, 42, 15, 42, 17, 0);
		codeSnippeSeedCounter.addElement(element);
		element = new CodeElement(CodeElement.METHOD_INVOCATION, 15, 26, 15, 36);
		codeSnippeSeedCounter.addElement(element);

		//---------------------------------------------
		//Second CodeSnippet
		signature = new MethodSignature("TinySample", "public", new Integer(7));
		arg1 = new MethodParameter("Integer", "seedValue");
		signature.addMethodParameters(arg1);

		buffer = new StringBuffer("public TinySample(Integer seedValue) {");
		buffer.append("\n");
		buffer.append("if(SeedLimit == null)");
		buffer.append("\n");
		buffer.append("this.SeedLimit = new Integer(seedValue);");
		buffer.append("\n");
		buffer.append("}");

		body = buffer.toString().substring(buffer.toString().indexOf('{'));

		CodeSnippet codeSnippetConstructor=new CodeSnippet("sample", "TinySample", signature, body, true, 
				7, 0, 10, 27, 7, 27, 10, 0);

		element = new CodeElement(CodeElement.IF_CONDITIONAL, 8, 0, 8, 11, 9, 0, 9, 15);
		codeSnippetConstructor.addElement(element);

	
		//----------------------------------------------
		//Generate all microtasks for both code snippets

		Microtask microtask = new Microtask(CodeElement.METHOD_INVOCATION, 
				codeSnippetConstructor, null, "Is there maybe something wrong in the declaration of function "
								+ "'TinySample' at line 7 (e.g., requires a parameter that is not listed, needs "
								+ "different parameters to produce the correct result, specifies the wrong or no return type, "
								+ "etc .)?", 7, 0, 7,27, 0);
		microtaskMap.put(microtask.getID(), microtask);

		microtask = new Microtask(CodeElement.METHOD_DECLARATION, codeSnippetConstructor, null , "Is there possibly "
						+ "something wrong with the body of function 'TinySample' between lines 7 and 9 (e.g., "
						+ "function produces an incorrect return value, return statement is at the wrong place, does not "
						+ "properly handle error situations, etc.)?", 7, 27, 10,1, 1);
		microtaskMap.put(microtask.getID(), microtask);

		microtask = new Microtask(CodeElement.IF_CONDITIONAL,  codeSnippetConstructor, 
				null, "Is it possible that the conditional clause at line 8 has problems (e.g., wrong Boolean operator, "
								+ "wrong comparison, misplaced parentheses, etc.)?", 8, 0, 8,11, 2);
		microtaskMap.put(microtask.getID(), microtask);

		microtask = new Microtask(CodeElement.IF_CONDITIONAL, codeSnippetConstructor, 
				null, "Is there maybe something wrong with the body of the conditional clause at line 8 (e.g., enters the "
								+ "wrong branch, makes a call to a null pointer, calls a wrong type, etc.)?", 9, 0, 9,15, 3);
		microtaskMap.put(microtask.getID(), microtask);

		microtask = new Microtask(CodeElement.METHOD_INVOCATION, 
				codeSnippeSeedCounter, null, "Is there maybe something wrong in the declaration of function 'seedCounter' "
								+ "at line 20 (e.g., requires a parameter that is not listed, needs different parameters to "
								+ "produce the correct result, specifies the wrong or no return type, etc .)?", 20, 0, 20,58, 4);
		microtaskMap.put(microtask.getID(), microtask);

		microtask = new Microtask(CodeElement.METHOD_DECLARATION, codeSnippeSeedCounter, null, "Is there possibly "
						+ "something wrong with the body of function 'seedCounter' between lines 20 and 30 (e.g., function "
						+ "produces an incorrect return value, return statement is at the wrong place, does not properly "
						+ "handle error situations, etc.)?", 20, 58, 30,1, 5);
		microtaskMap.put(microtask.getID(), microtask);

		microtask = new Microtask(CodeElement.IF_CONDITIONAL, codeSnippeSeedCounter, 
				null, "Is it possible that the conditional clause at line 21 has problems (e.g., wrong Boolean operator, "
								+ "wrong comparison, misplaced parentheses, etc.)?", 21, 0, 21,15, 6);
		microtaskMap.put(microtask.getID(), microtask);

		microtask = new Microtask(CodeElement.IF_CONDITIONAL, codeSnippeSeedCounter, 
				null, "Is there maybe something wrong with the body of the conditional clause between lines 22 and 29 "
				+ "(e.g., enters the wrong branch, makes a call to a null pointer, "
				+ "calls a wrong type, etc.)?", 22, 3, 22,17, 7);
		microtaskMap.put(microtask.getID(), microtask);

		microtask = new Microtask(CodeElement.FOR_LOOP, codeSnippeSeedCounter, 
				null, "Is there maybe something wrong with the 'For-loop' construct at line 23 (e.g., incorrect "
								+ "initialization, wrong counter increment, wrong exit condition, etc.)?", 23, 0, 23,42, 8);
		microtaskMap.put(microtask.getID(), microtask);

		microtask = new Microtask(CodeElement.FOR_LOOP, codeSnippeSeedCounter, 
				null, "Is the body of the 'For-loop' between lines 23 and 25 possibly not producing what it is supposed to "
								+ "(e.g., does not compute the expected result, does not exit at the expected iteration, etc.)"
								+ "?", 23, 42, 25,1, 9);
		microtaskMap.put(microtask.getID(), microtask);

	microtask = new Microtask(CodeElement.METHOD_INVOCATION, codeSnippetConstructor, null, "Is there perhaps something wrong with the "
							+ "values of the parameters received by function 'intValue' when called by function 'seedCounter' at "
							+ "line 23 (e.g., wrong variables used as parameters, wrong order, missing or wrong type of parameter, "
							+ "values of the parameters are not checked, etc .)?", 23, 26, 23,36, 10);
		microtaskMap.put(microtask.getID(), microtask);
	
		return microtaskMap;
	}
	
	

}
