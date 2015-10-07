package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.*;

public class QuestionFactoryTest 
{
	/* private ArrayList<String> templateIf = new ArrayList<String>();
	private ArrayList<String> templateFor = new ArrayList<String>();
	private ArrayList<String> templateWhile = new ArrayList<String>();
	private ArrayList<String> templateSwitch = new ArrayList<String>();
	private ArrayList<String> templateMethodCall = new ArrayList<String>(); */
	private CodeSnippet codeSnippetFactorial;
	private CodeSnippet codeSnippetConstructor;
	private Microtask constructorBody;
	private Microtask factorialBody;
	private Microtask questionIf11;
	private Microtask questionIf12;
	private Microtask questionIf21;
	private Microtask questionIf22;
	private Microtask questionFor1;
	private Microtask questionFor2;
	private Microtask questionInvocation1;
	private Microtask questionMethodCallFactorial;
	private Microtask questionMethodCallConstructor;
	private QuestionFactory questionFactory;

	@Before
	public void setupTestData()
	{
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

	//	this.questionFactory = new QuestionFactory();

		questionMethodCallConstructor = new Microtask(CodeElement.METHOD_INVOCATION, 
				this.codeSnippetConstructor, null, "Is there maybe something wrong in the declaration of function "
								+ "'SimpleSampleCode' at line 7 (e.g., requires a parameter that is not listed, needs "
								+ "different parameters to produce the correct result, specifies the wrong or no return type, "
								+ "etc .)?", 7, 0, 7,27, 1,"failure description");
//		this.questionFactory.addConcreteQuestion(questionMethodCallConstructor, this.codeSnippetConstructor);

		constructorBody = new Microtask(CodeElement.METHOD_DECLARATION, this.codeSnippetConstructor, null , "Is there possibly "
						+ "something wrong with the body of function 'SimpleSampleCode' between lines 7 and 9 (e.g., "
						+ "function produces an incorrect return value, return statement is at the wrong place, does not "
						+ "properly handle error situations, etc.)?", 7, 27, 10,1, 2,"failure description");
//		this.questionFactory.addConcreteQuestion(constructorBody, this.codeSnippetConstructor);

		questionIf11 = new Microtask(CodeElement.IF_CONDITIONAL, this.codeSnippetConstructor, 
				null, "Is it possible that the conditional clause at line 8 has problems (e.g., wrong Boolean operator, "
								+ "wrong comparison, misplaced parentheses, etc.)?", 8, 0, 8,11, 3,"failure description");
//		this.questionFactory.addConcreteQuestion(questionIf11, this.codeSnippetConstructor);

		questionIf12 = new Microtask(CodeElement.IF_CONDITIONAL, this.codeSnippetConstructor, 
				null, "Is there maybe something wrong with the body of the conditional clause at line 8 (e.g., enters the "
								+ "wrong branch, makes a call to a null pointer, calls a wrong type, etc.)?", 9, 0, 9,15, 4,"failure description");
//		this.questionFactory.addConcreteQuestion(questionIf12, this.codeSnippetConstructor);

		questionMethodCallFactorial = new Microtask(CodeElement.METHOD_INVOCATION, 
				this.codeSnippetFactorial, null, "Is there maybe something wrong in the declaration of function 'factorial' "
								+ "at line 20 (e.g., requires a parameter that is not listed, needs different parameters to "
								+ "produce the correct result, specifies the wrong or no return type, etc .)?", 20, 0, 20,58, 5,"failure description");
//		this.questionFactory.addConcreteQuestion(questionMethodCallFactorial, this.codeSnippetFactorial);

		factorialBody = new Microtask(CodeElement.METHOD_DECLARATION, this.codeSnippetFactorial, null, "Is there possibly "
						+ "something wrong with the body of function 'factorial' between lines 20 and 30 (e.g., function "
						+ "produces an incorrect return value, return statement is at the wrong place, does not properly "
						+ "handle error situations, etc.)?", 20, 58, 30,1, 6,"failure description");
//		this.questionFactory.addConcreteQuestion(questionMethodCallFactorial, this.codeSnippetFactorial);

		questionIf21 = new Microtask(CodeElement.IF_CONDITIONAL, this.codeSnippetFactorial, 
				null, "Is it possible that the conditional clause at line 21 has problems (e.g., wrong Boolean operator, "
								+ "wrong comparison, misplaced parentheses, etc.)?", 21, 0, 21,15, 7,"failure description");
//		this.questionFactory.addConcreteQuestion(questionIf21, this.codeSnippetFactorial);

		questionIf22 = new Microtask(CodeElement.IF_CONDITIONAL, this.codeSnippetFactorial, 
				null, "Is there maybe something wrong with the body of the conditional clause between lines 22 and 29 "
				+ "(e.g., enters the wrong branch, makes a call to a null pointer, "
				+ "calls a wrong type, etc.)?", 22, 3, 22,17, 8,"failure description");
	//	this.questionFactory.addConcreteQuestion(questionIf22, this.codeSnippetFactorial);

		questionFor1 = new Microtask(CodeElement.FOR_LOOP, this.codeSnippetFactorial, 
				null, "Is there maybe something wrong with the 'For-loop' construct at line 23 (e.g., incorrect "
								+ "initialization, wrong counter increment, wrong exit condition, etc.)?", 23, 0, 23,42, 9,"failure description");
	//	this.questionFactory.addConcreteQuestion(questionFor1, this.codeSnippetFactorial);

		questionFor2 = new Microtask(CodeElement.FOR_LOOP, this.codeSnippetFactorial, 
				null, "Is the body of the 'For-loop' between lines 23 and 25 possibly not producing what it is supposed to "
								+ "(e.g., does not compute the expected result, does not exit at the expected iteration, etc.)"
								+ "?", 23, 42, 25,1, 10,"failure description");
	//	this.questionFactory.addConcreteQuestion(questionFor2, this.codeSnippetFactorial);

		questionInvocation1 = new Microtask(CodeElement.METHOD_INVOCATION, this.codeSnippetFactorial, null, "Is there perhaps something wrong with the "
						+ "values of the parameters received by function 'intValue' when called by function 'factorial' at "
						+ "line 23 (e.g., wrong variables used as parameters, wrong order, missing or wrong type of parameter, "
						+ "values of the parameters are not checked, etc .)?", 23, 26, 23,36, 11,"failure description");
	}

	@Test
	public void test() {

		String folderPath = "C:/Users/Christian Adriano/Documents/GitHub/crowd-debug-firefly/src/sample/JustOneSample/";
		String fileName = "SimpleSampleCode.java";
		String fileContent = SourceFileReader.readFileToString(folderPath+fileName);
		CodeSnippetFactory factory = new CodeSnippetFactory(fileName,fileContent);
		
		Vector<CodeSnippet> list = factory.generateSnippetsForFile();
		if((list ==null ) || (list.size()!=2))
			Assert.fail("Null list of snippets or file does not match test data");
		else{
			boolean match = false;
			CodeSnippet snippet1 = list.get(0);
			CodeSnippet snippet2 = list.get(1);
			if((snippet1.isEqualTo(codeSnippetConstructor)) || (snippet1.isEqualTo(codeSnippetFactorial)) && 
					(snippet2.isEqualTo(codeSnippetConstructor)) || (snippet2.isEqualTo(codeSnippetFactorial))){
				/* Methods OK, now checking questions */
				this.questionFactory.generateMicrotasks(list,"Failure message",0);
				Hashtable<Integer, Microtask> allQuestions = this.questionFactory.getConcreteQuestions();
				
				if( (null == allQuestions) || (allQuestions.size()!= 11) )
					Assert.fail("Null list of questions or questions do not match test data");
				else
				{
					match = true;			// Assuming is true and testing for a false scenario
					Set<Integer> keySet = allQuestions.keySet();
					for (Integer key: keySet)
					{
						Microtask microtask = allQuestions.get(key);
						//							System.out.println(concreteQuestion.getQuestion());
						if ( !microtask.getQuestion().equalsIgnoreCase(questionIf11.getQuestion())
								&& !microtask.getQuestion().equalsIgnoreCase(questionIf12.getQuestion())
								&& !microtask.getQuestion().equalsIgnoreCase(questionIf21.getQuestion()) 
								&& !microtask.getQuestion().equalsIgnoreCase(questionIf22.getQuestion())
								&& !microtask.getQuestion().equalsIgnoreCase(questionFor1.getQuestion()) 
								&& !microtask.getQuestion().equalsIgnoreCase(questionFor2.getQuestion()) 
								&& !microtask.getQuestion().equalsIgnoreCase(questionMethodCallConstructor.getQuestion()) 
								&& !microtask.getQuestion().equalsIgnoreCase(constructorBody.getQuestion()) 
								&& !microtask.getQuestion().equalsIgnoreCase(questionMethodCallFactorial.getQuestion())
								&& !microtask.getQuestion().equalsIgnoreCase(factorialBody.getQuestion())
								&& !microtask.getQuestion().equalsIgnoreCase(questionInvocation1.getQuestion()) 
								)
						{
							match = false;
							System.out.println("Did not match this question: " + microtask.getQuestion());
							break;
						}
					}
				}
			}
			Assert.assertTrue("Snippet for Factorial Method does match", match);
		}
	}

}
