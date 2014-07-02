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
	public static ArrayList<String> templateLoop = new ArrayList<String>();
	
	public Integer numberOfStatements; 
	private Integer lineNumber1, lineNumber2, col1, col2;
	public static Integer concreteQuestionID;
	private String questionPrompt;
	private Microtask question;
	
//	private ArrayList<ConcreteQuestion> concreteQuestions;
	private HashMap<Integer, Microtask> concreteQuestions;
	
	public QuestionFactory()
	{
		this.numberOfStatements = 0;
		concreteQuestionID = 0;
		this.concreteQuestions = new HashMap<Integer, Microtask>();
		/* Method Declaration */
		templateMethodDeclaration.add("Is there maybe something wrong in the declaration of function '<F>' at line <#> " 
				+ "(e.g., requires a parameter that is not listed, needs different parameters to produce the correct result, specifies the wrong or no return type, etc .)?");
		templateMethodDeclaration.add("Is there possibly something wrong with the body of function '<F>' between lines "
				+ "<#1> and <#2> (e.g., function produces an incorrect return value, return statement is at the wrong place, does not properly handle error situations, etc.)?");
		/* Method invocation */
		templateMethodInvocation.add("Is there perhaps something wrong with the values of the parameters received "
				+ "by function '<F>' when called by function '<G>' at line <#> (e.g., wrong variables used as "
				+ "parameters, wrong order, missing or wrong type of parameter, values of the parameters are not checked, etc .)?");
		/* Conditional */
		templateIf.add("Is it possible that the conditional clause at line <#> has "
				+ "problems (e.g., wrong Boolean operator, wrong comparison, misplaced parentheses, etc.)?");
		templateIf.add("Is there maybe something wrong with the body of the conditional clause between lines <#1> and <#2> "
				+ "(e.g., enters the wrong branch, makes a call to a null pointer, calls a wrong type, etc.)?");
		templateSwitch.add("Is it possible that the conditional clause at line <#> has "
				+ "problems (e.g., wrong Boolean operator, wrong comparison, misplaced parentheses, etc.)?");
		templateSwitch.add("Is there maybe something wrong with the body of the conditional clause between lines <#1> and <#2> "
				+ "(e.g., enters the wrong branch, makes a call to a null pointer, calls a wrong type, etc.)?");
		/* Loops */
		templateLoop.add("Is there maybe something wrong with the '<L>-loop' construct at line <#> "
				+ "(e.g., incorrect initialization, wrong counter increment, wrong exit condition, etc.)?");
		templateLoop.add("Is the body of the '<L>-loop' between lines <#1> and <#2> "
				+ "possibly not producing what it is supposed to (e.g., does not compute the expected result, does not exit at the expected iteration, etc.)?");
	}
	
	public Integer getNumberOfStatements()
	{
		return this.numberOfStatements;
	}
	
	public HashMap<Integer, Microtask> generateQuestions(ArrayList<CodeSnippet> methodsArg)
	{
		for (CodeSnippet codeSnippet : methodsArg)
		{
			for (String templateForQuestion : templateMethodDeclaration)
			{
				questionPrompt = new String(templateForQuestion);
				questionPrompt = questionPrompt.replaceAll("<F>", codeSnippet.getMethodSignature().getName());
				/* assuming it will talk just about the construct */
				lineNumber1 = codeSnippet.getMethodSignature().getLineNumber();
				lineNumber2 = lineNumber1;
				col1 = 0;
				col2 = 2000;
				if (questionPrompt.indexOf("<#1>") > 0)	//it means it will ask about the body
				{
					lineNumber1 = codeSnippet.getBodyStartsAt();
					lineNumber2 = codeSnippet.getBodyEndsAt();
					col1 = codeSnippet.getBodyStartingColumn();
					col2 = codeSnippet.getBodyEndingColumn();
					questionPrompt = questionPrompt.replaceAll("<#1>", lineNumber1.toString());
					questionPrompt = questionPrompt.replaceAll("<#2>", lineNumber2.toString());
				} else {
					questionPrompt = questionPrompt.replaceAll("<#>", codeSnippet.getMethodSignature().getLineNumber().toString());
					if (codeSnippet.getBodyStartsAt() == lineNumber1) //if body starts at the constructor line
						this.col2 = codeSnippet.getBodyStartingColumn();
				}
				question = new Microtask(CodeElement.METHOD_DECLARARION, codeSnippet, 
						questionPrompt, codeSnippet.getMethodSignature().getLineNumber(), lineNumber1, lineNumber2);
				question.setBodyStartingColumn(col1);
				question.setBodyEndingColumn(col2);
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
				/* assuming it will ask just about the construct */
				lineNumber1 = element.getElementStartPosition();
				lineNumber2 = lineNumber1;
				col1 = 0;
				col2 = 2000;
				
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
						questionPrompt = questionPrompt.replaceAll("<#>", elementCall.getElementStartPosition().toString());
						question = new Microtask(CodeElement.METHOD_INVOCATION, codeSnippet, questionPrompt, 
								lineNumber1);
						this.concreteQuestions.put(new Integer(concreteQuestionID), question);
					}
					break;
				case CodeElement.IF_CONDITIONAL:
					for (String templateForQuestion : templateIf)
					{
						questionPrompt = new String(templateForQuestion);
						questionPrompt = this.setUpQuestionPrompt(questionPrompt, element);
						question = new Microtask(CodeElement.IF_CONDITIONAL, codeSnippet, questionPrompt, 
								this.lineNumber1, this.lineNumber1, this.lineNumber2);
						this.setColumnsAndPutQuestion(question);
					}
					break;
					
				case CodeElement.SWITCH_CONDITIONAL:
					for (String templateForQuestion : templateSwitch)
					{
						questionPrompt = new String(templateForQuestion);
						questionPrompt = this.setUpQuestionPrompt(questionPrompt, element);
						question = new Microtask(CodeElement.SWITCH_CONDITIONAL, codeSnippet, questionPrompt, 
								element.getElementStartPosition(), element.getBodyStartPosition(), element.getBodyEndPosition());
						this.setColumnsAndPutQuestion(question);
					}
					break;
					
				case CodeElement.FOR_LOOP:
				case CodeElement.DO_LOOP:
				case CodeElement.WHILE_LOOP:
					for (String templateLoopQuestion : templateLoop)
					{
						questionPrompt = new String(templateLoopQuestion);
						questionPrompt = this.setUpQuestionPrompt(questionPrompt, element);
						switch (element.getType()) {
						case CodeElement.FOR_LOOP:
							questionPrompt = questionPrompt.replaceAll("<L>", "For");
							question = new Microtask(CodeElement.FOR_LOOP, codeSnippet, questionPrompt, 
									element.getElementStartPosition(), element.getBodyStartPosition(), element.getBodyEndPosition());
							break;
						case CodeElement.DO_LOOP:
							questionPrompt = questionPrompt.replaceAll("<L>", "Do");
							question = new Microtask(CodeElement.DO_LOOP, codeSnippet, questionPrompt, 
									element.getElementStartPosition(), element.getBodyStartPosition(), element.getBodyEndPosition());
							break;
						case CodeElement.WHILE_LOOP:
							questionPrompt = questionPrompt.replaceAll("<L>", "While");
							question = new Microtask(CodeElement.WHILE_LOOP, codeSnippet, questionPrompt, 
									element.getElementStartPosition(), element.getBodyStartPosition(), element.getBodyEndPosition());
							break;
						}
						this.setColumnsAndPutQuestion(question);
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
	
	public void addConcreteQuestion(Microtask question, CodeSnippet method)
	{
		//TO DO
	}
	
	private String setUpQuestionPrompt(String questionPromptArg, CodeElement elementArg)
	{
		if (questionPromptArg.indexOf("<#1>") > 0)	//it means it will ask about the body
		{
			this.lineNumber1 = elementArg.getBodyStartPosition();
			this.lineNumber2 = elementArg.getBodyEndPosition();
			this.col1 = elementArg.getColumnStart();
			this.col2 = elementArg.getColumnEnd();
			questionPromptArg = questionPromptArg.replaceAll("<#1>", this.lineNumber1.toString());
			questionPromptArg = questionPromptArg.replaceAll("<#2>", this.lineNumber2.toString());
		} else {
			questionPromptArg = questionPromptArg.replaceAll("<#>", elementArg.getElementStartPosition().toString());
			if (elementArg.getBodyStartPosition() == lineNumber1) //if body starts at the constructor line
				this.col2 = elementArg.getColumnStart();
		}
			
		return questionPromptArg;
	}
	
	private void setColumnsAndPutQuestion(Microtask questionArg)
	{
		questionArg.setBodyStartingColumn(this.col1);
		questionArg.setBodyEndingColumn(this.col2);
		this.concreteQuestions.put(new Integer(concreteQuestionID), question);
	}
	
//	public ArrayList<ConcreteQuestion> getConcreteQuestionsOfCS(CodeSnippet codeSnippetConstructor)
//	{
//		// TO DO
//		return // questions for the CodeSnippet specified
//	}
}
