package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionFactory {
	/* public final static String FOR_LOOP = "FOR_LOOP"; 
	public final static String WHILE_LOOP = "WHILE_LOOP"; 
	public final static String IF_CONDITIONAL = "IF_CONDITIONAL"; 
	public final static String SWITCH_CONDITIONAL = "SWITCH_CONDITIONAL"; 
	public final static String METHOD_CALL = "METHOD_CALL"; 
	public final static String METHOD_PARAMETERS = "METHOD_PARAMETERS"; 
	public final static String METHOD_NAME = "METHOD_NAME";
	public static final String RETURN_STATEMENT = "RETURN_STATEMENT"; */
	/* Template Lists */
	public static ArrayList<String> templateMethodDeclaration = new ArrayList<String>();
	public static ArrayList<String> templateMethodInvocation = new ArrayList<String>();
	public static ArrayList<String> templateIf = new ArrayList<String>();
	public static ArrayList<String> templateSwitch = new ArrayList<String>();
	public static ArrayList<String> templateFor = new ArrayList<String>();
	public static ArrayList<String> templateDo = new ArrayList<String>();
	public static ArrayList<String> templateWhile = new ArrayList<String>();
	
	public Integer numberOfStatements; 
	public static Integer concreteQuestionID;
	private String questionPrompt;
	private ConcreteQuestion question;
	
//	private ArrayList<ConcreteQuestion> concreteQuestions;
	private HashMap<Integer, ConcreteQuestion> concreteQuestions;
	
	public QuestionFactory()
	{
		this.numberOfStatements = 0;
		concreteQuestionID = 0;
		this.concreteQuestions = new HashMap<Integer, ConcreteQuestion>();
		/* Method Declaration */
		templateMethodDeclaration.add("Is there maybe something wrong in the declaration of function '<F>' at line <#1> " 
				+ "(e.g., requires a parameter that is not listed, needs different parameters to produce the correct result, specifies the wrong or no return type, etc .)?");
		templateMethodDeclaration.add("Is there possibly something wrong with the body of function '<F>' between lines "
				+ "<#1> and <#2> (e.g., function produces an incorrect return value, return statement is at the wrong place, does not properly handle error situations, etc.)?");
		/* Method invocation */
		templateMethodInvocation.add("Is there perhaps something wrong with the values of the parameters received "
				+ "by function '<F>' when called by function '<G>' at line <#> (e.g., wrong variables used as "
				+ "parameters, wrong order, missing or wrong type of parameter, values of the parameters are not checked, etc .)?");
		/* Conditional */
		templateIf.add("Is it possible that the conditional clause at line <#1> has "
				+ "problems (e.g., wrong Boolean operator, wrong comparison, misplaced parentheses, etc.)?");
		templateIf.add("Is there maybe something wrong with the body of the conditional clause between lines <#1> and <#2> "
				+ "(e.g., enters the wrong branch, makes a call to a null pointer, calls a wrong type, etc.)?");
		templateSwitch.add("Is it possible that the conditional clause at line <#1> has "
				+ "problems (e.g., wrong Boolean operator, wrong comparison, misplaced parentheses, etc.)?");
		templateSwitch.add("Is there maybe something wrong with the body of the conditional clause between lines <#1> and <#2> "
				+ "(e.g., enters the wrong branch, makes a call to a null pointer, calls a wrong type, etc.)?");
		/* Loops */
		templateFor.add("Is there maybe something wrong with the '<L>-loop' construct at line <#1> "
				+ "(e.g., incorrect initialization, wrong counter increment, wrong exit condition, etc.)?");
		templateFor.add("Is the body of the '<L>-loop' between lines <#1> and <#2> "
				+ "possibly not producing what it is supposed to (e.g., does not compute the expected result, does not exit at the expected iteration, etc.)?");
		templateDo.add("Is there maybe something wrong with the '<L>-loop' construct at line <#1> "
				+ "(e.g., incorrect initialization, wrong counter increment, wrong exit condition, etc.)?");
		templateDo.add("Is the body of the '<L>-loop' between lines <#1> and <#2> "
				+ "possibly not producing what it is supposed to (e.g., does not compute the expected result, does not exit at the expected iteration, etc.)?");
		templateWhile.add("Is there maybe something wrong with the '<L>-loop' construct at line <#1> "
				+ "(e.g., incorrect initialization, wrong counter increment, wrong exit condition, etc.)?");
		templateWhile.add("Is the body of the '<L>-loop' between lines <#1> and <#2> "
				+ "possibly not producing what it is supposed to (e.g., does not compute the expected result, does not exit at the expected iteration, etc.)?");
	}
	
	public Integer getNumberOfStatements()
	{
		return this.numberOfStatements;
	}
	
	public HashMap<Integer, ConcreteQuestion> generateQuestions(ArrayList<CodeSnippet> methodsArg)
	{
		for (CodeSnippet codeSnippet : methodsArg)
		{
			for (String templateForQuestion : templateMethodDeclaration)
			{
				questionPrompt = new String(templateForQuestion);
				questionPrompt = questionPrompt.replaceAll("<F>", codeSnippet.getMethodSignature().getName());
				questionPrompt = questionPrompt.replaceAll("<#1>", codeSnippet.getMethodSignature().getLineNumber().toString());
				question = new ConcreteQuestion(CodeElement.METHOD_DECLARARION, codeSnippet, questionPrompt);
				this.concreteQuestions.put(new Integer(concreteQuestionID), question);
			}
//			this.concreteQuestions.put(new Integer(concreteQuestionID), question);	// now getting the question for the statements
//			String questionPrompt = new String("Is there maybe something wrong in the declaration of function '"
//					+ codeSnippet.getMethodSignature().getName() + "' at line " + codeSnippet.getMethodSignature().getLineNumber().toString()
//					+ " (e.g., requires a parameter that is not listed, needs different parameters to produce the correct result, specifies the wrong or no return type, etc .)?");
//			questionPrompt = questionPrompt.replaceAll("<F>", codeSnippet.getMethodSignature().getName());
//			ConcreteQuestion question = new ConcreteQuestion(CodeElement.METHOD_DECLARARION, codeSnippet, questionPrompt);
//			this.concreteQuestions.put(new Integer(concreteQuestionID), question);	
			
			ArrayList<CodeElement> statements = codeSnippet.getStatements();	// now getting the question for the statements
			
			//Method Declaration
			
			
			
			//Method Body
			
			
			
			for (CodeElement element : statements)
			{
				this.numberOfStatements++;
				switch (element.getType())
				{
				case CodeElement.METHOD_INVOCATION:
					for (String templateForQuestion : templateMethodInvocation)
					{
						myMethodCall elementCall = (myMethodCall)element;
						questionPrompt = new String(templateForQuestion);
						questionPrompt = questionPrompt.replaceAll("<F>", elementCall.getName());
						questionPrompt = questionPrompt.replaceAll("<G>", codeSnippet.getMethodSignature().getName());
						questionPrompt = questionPrompt.replaceAll("<#>", elementCall.getStartPosition().toString());
						question = new ConcreteQuestion(CodeElement.METHOD_INVOCATION, codeSnippet, questionPrompt);
						this.concreteQuestions.put(new Integer(concreteQuestionID), question);
					}
					break;
				case CodeElement.IF_CONDITIONAL:
					for (String templateForQuestion : templateIf)
					{
						questionPrompt = new String(templateForQuestion);
						questionPrompt = questionPrompt.replaceAll("<#1>", element.getStartPosition().toString());
						questionPrompt = questionPrompt.replaceAll("<#2>", element.getEndPosition().toString());
						question = new ConcreteQuestion(CodeElement.IF_CONDITIONAL, codeSnippet, questionPrompt);
						this.concreteQuestions.put(new Integer(concreteQuestionID), question);;	// now getting the question for the statements
					}
					break;
					
				case CodeElement.SWITCH_CONDITIONAL:
					for (String templateForQuestion : templateSwitch)
					{
						questionPrompt = new String(templateForQuestion);
						questionPrompt = questionPrompt.replaceAll("<#>", element.getStartPosition().toString());
						question = new ConcreteQuestion(CodeElement.SWITCH_CONDITIONAL, codeSnippet, questionPrompt);
						this.concreteQuestions.put(new Integer(concreteQuestionID), question);;	// now getting the question for the statements
					}
					break;
					
				case CodeElement.FOR_LOOP:
					for (String templateForQuestion : templateFor)
					{
						questionPrompt = new String(templateForQuestion);
						questionPrompt = questionPrompt.replaceAll("<L>", "For");
						questionPrompt = questionPrompt.replaceAll("<#1>", element.getStartPosition().toString());
						questionPrompt = questionPrompt.replaceAll("<#2>", element.getEndPosition().toString());
						question = new ConcreteQuestion(CodeElement.FOR_LOOP, codeSnippet, questionPrompt);
						this.concreteQuestions.put(new Integer(concreteQuestionID), question);;	// now getting the question for the statements
					}
					break;
					
				case CodeElement.DO_LOOP:
					for (String templateForQuestion : templateDo)
					{
						questionPrompt = new String(templateForQuestion);
						questionPrompt = questionPrompt.replaceAll("<L>", "Do-While");
						questionPrompt = questionPrompt.replaceAll("<#1>", element.getStartPosition().toString());
						questionPrompt = questionPrompt.replaceAll("<#2>", element.getEndPosition().toString());
						question = new ConcreteQuestion(CodeElement.DO_LOOP, codeSnippet, questionPrompt);
						this.concreteQuestions.put(new Integer(concreteQuestionID), question);;	// now getting the question for the statements
					}
					break;
					
				case CodeElement.WHILE_LOOP:
					for (String templateForQuestion : templateWhile)
					{
						questionPrompt = new String(templateForQuestion);
						questionPrompt = questionPrompt.replaceAll("<L>", "While");
						questionPrompt = questionPrompt.replaceAll("<#1>", element.getStartPosition().toString());
						questionPrompt = questionPrompt.replaceAll("<#2>", element.getEndPosition().toString());
						question = new ConcreteQuestion(CodeElement.WHILE_LOOP, codeSnippet, questionPrompt);
						this.concreteQuestions.put(new Integer(concreteQuestionID), question);;	// now getting the question for the statements
					}
					break;
					// Add more cases here 
					
				default:
						System.out.println("!!! Type of element did not matched: " + element.getType() + " !!!");
						break;
				} 
			}
		}
		return concreteQuestions;
	}
	
	public void addConcreteQuestion(ConcreteQuestion question, CodeSnippet method)
	{
		//TO DO
	}
	
//	public ArrayList<ConcreteQuestion> getConcreteQuestionsOfCS(CodeSnippet codeSnippetConstructor)
//	{
//		// TO DO
//		return // questions for the CodeSnippet specified
//	}
}
