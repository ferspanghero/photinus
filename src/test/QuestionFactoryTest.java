package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
	private ConcreteQuestion questionIf11;
	private ConcreteQuestion questionIf12;
	private ConcreteQuestion questionIf21;
	private ConcreteQuestion questionIf22;
	private ConcreteQuestion questionFor1;
	private ConcreteQuestion questionFor2;
	private ConcreteQuestion questionMethodCallFactorial;
	private ConcreteQuestion questionMethodCallConstructor;
	private QuestionFactory questionFactory;
	
	@Before
	public void setupTestData()
	{
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
		
		this.codeSnippetFactorial=new CodeSnippet("sample","SimpleSampleCode", buffer, 
				new Boolean (true), signature);
		
		CodeElement element = new CodeElement(CodeElement.METHOD_DECLARARION,new Integer(12));
		codeSnippetFactorial.addElement(element);
		element = new CodeElement(CodeElement.IF_CONDITIONAL,new Integer(14));
		codeSnippetFactorial.addElement(element);
		element = new CodeElement(CodeElement.FOR_LOOP,new Integer(16));
		codeSnippetFactorial.addElement(element);
		element = new CodeElement(CodeElement.METHOD_INVOCATION,new Integer(19));
		codeSnippetFactorial.addElement(element);
		element = new CodeElement(CodeElement.METHOD_INVOCATION,new Integer(21));
		codeSnippetFactorial.addElement(element);
		
		//Second CodeSnippet
		signature = new MethodSignature("SimpleSampleCode", "public", new Integer(7));
		arg1 = new MethodParameter("Integer", "seedValue");
		signature.addMethodParameters(arg1);
		
		buffer = new StringBuffer("public SimpleSampleCode(Integer seedValue) {");
		buffer.append("\n");
		buffer.append("if(SeedLimit == null)");
		buffer.append("\n");
		buffer.append("this.SeedLimit = new Integer(seedValue);");
		buffer.append("\n");
		buffer.append("}");
		
		this.codeSnippetConstructor=new CodeSnippet("sample", "SimpleSampleCode", buffer,
				new Boolean(false), signature);
		
		element = new CodeElement(CodeElement.METHOD_DECLARARION, new Integer(7));
		codeSnippetConstructor.addElement(element);
		
		this.questionFactory = new QuestionFactory();
		
		questionMethodCallConstructor = new ConcreteQuestion(CodeElement.METHOD_INVOCATION, 
				this.codeSnippetConstructor, "Is there perhaps something wrong with the parameters received "
				+ "by function 'SimpleSampleCode' (e.g. wrong order, missing parameter, wrong type of parameter, "
						+ "parameters that are not checked, etc.)?");
		this.questionFactory.addConcreteQuestion(questionMethodCallConstructor, this.codeSnippetConstructor);
		
		questionIf11 = new ConcreteQuestion(CodeElement.IF_CONDITIONAL, this.codeSnippetConstructor, 
				"Is if possible that the construct of the conditional clause at line 8 has any problems"
				+ " (e.g., wrong boolean operator, wrong comparison, misplaced parentheses, etc.)?");
		this.questionFactory.addConcreteQuestion(questionIf11, this.codeSnippetConstructor);
		
		questionIf12 = new ConcreteQuestion(CodeElement.IF_CONDITIONAL, this.codeSnippetConstructor, 
				"Is it possible that the conditional clause at line 8 is causing the wrong code " + 
				"to be executed (e.g. enter the wrong branch, makes a call to a null pointer, " +
				"calls a wrong type, etc.)?");
		this.questionFactory.addConcreteQuestion(questionIf12, this.codeSnippetConstructor);
		
		questionMethodCallFactorial = new ConcreteQuestion(CodeElement.METHOD_INVOCATION, 
				this.codeSnippetFactorial, "Is there perhaps something wrong with the parameters received "
				+ "by function 'factorial' (e.g. wrong order, missing parameter, wrong type of parameter, "
						+ "parameters that are not checked, etc.)?");
		this.questionFactory.addConcreteQuestion(questionMethodCallFactorial, this.codeSnippetFactorial);
		
		questionIf21 = new ConcreteQuestion(CodeElement.IF_CONDITIONAL, this.codeSnippetFactorial, 
				"Is if possible that the construct of the conditional clause at line 14 has any problems"
				+ " (e.g., wrong boolean operator, wrong comparison, misplaced parentheses, etc.)?");
		this.questionFactory.addConcreteQuestion(questionIf21, this.codeSnippetFactorial);
		
		questionIf22 = new ConcreteQuestion(CodeElement.IF_CONDITIONAL, this.codeSnippetFactorial, 
				"Is it possible that the conditional clause at line 14 is causing the wrong code " + 
				"to be executed (e.g. enter the wrong branch, makes a call to a null pointer, " +
				"calls a wrong type, etc.)?");
		this.questionFactory.addConcreteQuestion(questionIf22, this.codeSnippetFactorial);
		
		questionFor1 = new ConcreteQuestion(CodeElement.FOR_LOOP, this.codeSnippetFactorial, 
				"Is there maybe something wrong with the construct of the loop at line 16 (e.g. " +
				"incorrect initialization, wrong counter increment, wrong exit condition, etc.)?");
		this.questionFactory.addConcreteQuestion(questionFor1, this.codeSnippetFactorial);
		
		questionFor2 = new ConcreteQuestion(CodeElement.FOR_LOOP, this.codeSnippetFactorial, 
				"Is the loop at line 16 possibly not producing what it is supposed to (e.g. does not " +
				"generate the expected result from iteration, does not exit at the expected iteration, etc.)?");
		this.questionFactory.addConcreteQuestion(questionFor2, this.codeSnippetFactorial);
	
	}
	
	@Test
	public void test() {
		CodeSnippetFactory factory = new CodeSnippetFactory(
				"C:/Users/Danilo/Documents/GitHub/crowd-debug-firefly/src/sample/JustOneSample");
		ArrayList<CodeSnippet> list = factory.generateSnippets();
		if((list ==null ) || (list.size()!=2))
			Assert.fail("Null list of snippets or file does not match test data");
		else{
			boolean match = false;
			CodeSnippet snippet1 = list.get(0);
			CodeSnippet snippet2 = list.get(1);
			if((snippet1.isEqualTo(codeSnippetConstructor)) || (snippet1.isEqualTo(codeSnippetFactorial)) && 
				(snippet2.isEqualTo(codeSnippetConstructor)) || (snippet2.isEqualTo(codeSnippetFactorial))){
					/* Methods OK, now checking questions */
					HashMap<Integer, ConcreteQuestion> allQuestions = this.questionFactory.generateQuestions(list);
					if( (null == allQuestions) || (allQuestions.size()!= 8) )
						Assert.fail("Null list of questions or questions do not match test data");
					else
					{
						match = true;			// Assuming is true and testing for a false scenario
						Set<Integer> keySet = allQuestions.keySet();
						for (Integer key: keySet)
						{
							ConcreteQuestion concreteQuestion = allQuestions.get(key);
//							System.out.println(concreteQuestion.getQuestion());
							if ( !concreteQuestion.getQuestion().equalsIgnoreCase(questionIf11.getQuestion())
								&& !concreteQuestion.getQuestion().equalsIgnoreCase(questionIf12.getQuestion())
								&& !concreteQuestion.getQuestion().equalsIgnoreCase(questionIf21.getQuestion()) 
								&& !concreteQuestion.getQuestion().equalsIgnoreCase(questionIf22.getQuestion())
								&& !concreteQuestion.getQuestion().equalsIgnoreCase(questionFor1.getQuestion()) 
								&& !concreteQuestion.getQuestion().equalsIgnoreCase(questionFor2.getQuestion()) 
								&& !concreteQuestion.getQuestion().equalsIgnoreCase(questionMethodCallConstructor.getQuestion()) 
								&& !concreteQuestion.getQuestion().equalsIgnoreCase(questionMethodCallFactorial.getQuestion())
									)
							{
								match = false;
								System.out.println("Did not match this question");
								break;
							}
						}
					}
					
				}
			Assert.assertTrue("Snippet for Factorial Method does match", match);
			}
	}
	
}
