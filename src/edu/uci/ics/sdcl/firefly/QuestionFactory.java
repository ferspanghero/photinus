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
	public static Integer concreteQuestionID = 0;
	private String questionPrompt;
	private Microtask question;
	
	/* for the Range-values (ACE Editor) */
	private Integer startingLine;
	private Integer startingColumn;
	private Integer endingLine;
	private Integer endingColumn;
	
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
		this.numberOfStatements = 0;	// initializing variable
		for (CodeSnippet codeSnippet : methodsArg)
		{
			for (String templateForQuestion : templateMethodDeclaration)
			{
				questionPrompt = new String(templateForQuestion);
				questionPrompt = questionPrompt.replaceAll("<F>", codeSnippet.getMethodSignature().getName());

				if (questionPrompt.indexOf("<#1>") > 0)	//it means it will ask about the body
				{
					/* setting up the position for the body */
					this.startingLine = codeSnippet.getBodyStartingLine();
					this.startingColumn = codeSnippet.getBodyStartingColumn();
					this.endingLine = codeSnippet.getBodyEndingLine();
					this.endingColumn = codeSnippet.getBodyEndingColumn();
					
					questionPrompt = questionPrompt.replaceAll("<#1>", this.startingLine.toString());
					questionPrompt = questionPrompt.replaceAll("<#2>", this.endingLine.toString());
				} else
				{
					/* setting up the position for the element */
					this.startingLine = codeSnippet.getElementStartingLine();
					this.startingColumn = codeSnippet.getElementStartingColumn();
					this.endingLine = codeSnippet.getElementEndingLine();
					this.endingColumn = codeSnippet.getElementEndingColumn();
					
					questionPrompt = questionPrompt.replaceAll("<#>", this.startingLine.toString());
				}
				/* setting and adding a concrete question */
				question = new Microtask(CodeElement.METHOD_DECLARARION, codeSnippet, questionPrompt,
						this.startingLine, this.startingColumn, this.endingLine, this.endingColumn);
				this.concreteQuestions.put(new Integer(concreteQuestionID), question);
			}
			
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
						MyMethodCall elementCall = (MyMethodCall)element;
						/* setting up the position for the element */
						this.startingLine = elementCall.getElementStartingLine();
						this.startingColumn = elementCall.getElementStartingColumn();
						this.endingLine = elementCall.getElementEndingLine();
						this.endingColumn = elementCall.getElementEndingColumn();
						
						questionPrompt = new String(templateForQuestion);
						questionPrompt = questionPrompt.replaceAll("<F>", elementCall.getName());
						questionPrompt = questionPrompt.replaceAll("<G>", codeSnippet.getMethodSignature().getName());
						questionPrompt = questionPrompt.replaceAll("<#>", this.startingLine.toString());
						
						question = new Microtask(CodeElement.METHOD_INVOCATION, codeSnippet, questionPrompt, 
								this.startingLine, this.startingColumn, this.endingLine, this.endingColumn);
						this.concreteQuestions.put(new Integer(concreteQuestionID), question);
					}
					break;
					
				case CodeElement.IF_CONDITIONAL:
					for (String templateForQuestion : templateIf)
					{
						MyIfStatement elementIf = (MyIfStatement)element;
						questionPrompt = new String(templateForQuestion);
						/* setting up question prompt */
						if (questionPrompt.indexOf("<#1>") > 0)	//it means it will ask about the body
						{
							// the starting line of the body
							this.startingLine = elementIf.getBodyStartingLine();
							this.startingColumn = elementIf.getBodyStartingColumn();
							if (elementIf.isThereIsElse())
							{	// get Else statement end
								this.endingLine = elementIf.getElseEndingLine();
								this.endingColumn = elementIf.getElseEndingColumn();
							} else 
							{	// no Else then get just body  end (then statement end)
								this.endingLine = elementIf.getBodyEndingLine();
								this.endingColumn = elementIf.getBodyEndingColumn();
							}
							questionPrompt = questionPrompt.replaceAll("<#1>", this.startingLine.toString());
							questionPrompt = questionPrompt.replaceAll("<#2>", this.endingLine.toString());
						} else
						{	/* setting up the position for the element */
							this.startingLine = elementIf.getElementStartingLine();
							this.startingColumn = elementIf.getElementStartingColumn();
							this.endingLine = elementIf.getElementEndingLine();
							this.endingColumn = elementIf.getElementEndingColumn();
							
							questionPrompt = questionPrompt.replaceAll("<#>", this.startingLine.toString());
						}
						question = new Microtask(CodeElement.IF_CONDITIONAL, codeSnippet, questionPrompt, 
								this.startingLine, this.startingColumn, this.endingLine, this.endingColumn);
						
						this.concreteQuestions.put(new Integer(concreteQuestionID), question);
					}
					break;
					
				case CodeElement.SWITCH_CONDITIONAL:
					for (String templateForQuestion : templateSwitch)
					{
						questionPrompt = new String(templateForQuestion);
						questionPrompt = this.setUpQuestionPrompt(questionPrompt, element);
						question = new Microtask(CodeElement.SWITCH_CONDITIONAL, codeSnippet, questionPrompt, 
								this.startingLine, this.startingColumn, this.endingLine, this.endingColumn);

						this.concreteQuestions.put(new Integer(concreteQuestionID), question);
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
									this.startingLine, this.startingColumn, this.endingLine, this.endingColumn);
							break;
						case CodeElement.DO_LOOP:
							questionPrompt = questionPrompt.replaceAll("<L>", "Do");
							question = new Microtask(CodeElement.DO_LOOP, codeSnippet, questionPrompt, 
									this.startingLine, this.startingColumn, this.endingLine, this.endingColumn);
							break;
						case CodeElement.WHILE_LOOP:
							questionPrompt = questionPrompt.replaceAll("<L>", "While");
							question = new Microtask(CodeElement.WHILE_LOOP, codeSnippet, questionPrompt, 
									this.startingLine, this.startingColumn, this.endingLine, this.endingColumn);
							break;
						}
						this.concreteQuestions.put(new Integer(concreteQuestionID), question);
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
			/* setting up the position for the body */
			this.startingLine = elementArg.getBodyStartingLine();
			this.startingColumn = elementArg.getBodyStartingColumn();
			this.endingLine = elementArg.getBodyEndingLine();
			this.endingColumn = elementArg.getBodyEndingColumn();
			
			questionPromptArg = questionPromptArg.replaceAll("<#1>", this.startingLine.toString());
			questionPromptArg = questionPromptArg.replaceAll("<#2>", this.endingLine.toString());
		} else
		{
			/* setting up the position for the element */
			this.startingLine = elementArg.getElementStartingLine();
			this.startingColumn = elementArg.getElementStartingColumn();
			this.endingLine = elementArg.getElementEndingLine();
			this.endingColumn = elementArg.getElementEndingColumn();
			
			questionPromptArg = questionPromptArg.replaceAll("<#>", this.startingLine.toString());
		}
		return questionPromptArg;
	}
	
//	public ArrayList<ConcreteQuestion> getConcreteQuestionsOfCS(CodeSnippet codeSnippetConstructor)
//	{
//		// TO DO
//		return // questions for the CodeSnippet specified
//	}
}
