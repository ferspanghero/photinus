package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;

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
	public static ArrayList<String> templateIf = new ArrayList<String>();
	public static ArrayList<String> templateFor = new ArrayList<String>();
	public static ArrayList<String> templateWhile = new ArrayList<String>();
	public static ArrayList<String> templateSwitch = new ArrayList<String>();
	public static ArrayList<String> templateMethodCall = new ArrayList<String>();
	
	private ArrayList<ConcreteQuestion> concreteQuestions;
	
	public QuestionFactory()
	{
		this.concreteQuestions = new ArrayList<ConcreteQuestion>();
		templateIf.add("Is if possible that the construct of the conditional clause at line <#> has "
				+ "any problems (e.g., wrong boolean operator, wrong comparison, misplaced parentheses, etc.)?");
		templateIf.add("Is it possible that the conditional clause at line <#> is causing the wrong code " + 
				"to be executed (e.g. enter the wrong branch, makes a call to a null pointer, " +
				"calls a wrong type, etc.)?");
		templateFor.add("Is there maybe something wrong with the construct of the loop at line <#> (e.g. " +
				"incorrect initialization, wrong counter increment, wrong exit condition, etc.)?");
		templateFor.add("Is the loop at line <#> possibly not producing what it is supposed to (e.g. does not " +
				"generate the expected result from iteration, does not exit at the expected iteration, etc.)?");
		templateMethodCall.add("Is there perhaps something wrong with the parameters received "
				+ "by function <F> (e.g. wrong order, missing parameter, wrong type of parameter, "
				+ "parameters that are not checked, etc.)?");
	}
	
	public ArrayList<ConcreteQuestion> generateQuestions(ArrayList<CodeSnippet> methodsArg)
	{
		for (CodeSnippet codeSnippet : methodsArg)
		{
			String questionPrompt = new String(templateMethodCall.get(0)); // 0 = first question of this template
			questionPrompt = questionPrompt.replaceAll("<F>", "'" + codeSnippet.getMethodSignature().getName() + "'");
			ConcreteQuestion question = new ConcreteQuestion(CodeElement.METHOD_CALL, codeSnippet, questionPrompt);
			this.concreteQuestions.add(question);	// now getting the question for the statements
			ArrayList<CodeElement> statements = codeSnippet.getStatements();
			for (CodeElement element : statements)
			{
				switch (element.getType())
				{
				case CodeElement.FOR_LOOP:
					for (String templateForQuestion : templateFor)
					{
						questionPrompt = new String(templateForQuestion);
						questionPrompt = questionPrompt.replaceAll("<#>", statements.get(statements.indexOf(element)).
								getLineNumber().toString());
						question = new ConcreteQuestion(CodeElement.FOR_LOOP, codeSnippet, questionPrompt);
						this.concreteQuestions.add(question);	// now getting the question for the statements
					}
					break;
					
				case CodeElement.IF_CONDITIONAL:
					for (String templateForQuestion : templateIf)
					{
						questionPrompt = new String(templateForQuestion);
						questionPrompt = questionPrompt.replaceAll("<#>", statements.get(statements.indexOf(element)).
								getLineNumber().toString());
						question = new ConcreteQuestion(CodeElement.IF_CONDITIONAL, codeSnippet, questionPrompt);
						this.concreteQuestions.add(question);	// now getting the question for the statements
					}
					break;
					
					// Add more cases here
					
				default:
					System.out.println("!!! Type of element did not matched !!!");
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
