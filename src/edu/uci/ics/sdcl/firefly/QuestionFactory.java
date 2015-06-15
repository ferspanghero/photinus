package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class QuestionFactory {

	/* Template Lists */
	public ArrayList<String> templateMethodInvocation = new ArrayList<String>();
	public ArrayList<String> templateConditional = new ArrayList<String>();
	public ArrayList<String> templateLoop = new ArrayList<String>();
	public ArrayList<String> templateVariable = new ArrayList<String>();

	private String questionPrompt;
	private Microtask question;

	/* for the Range-values (ACE Editor) */
	private Integer startingLine;
	private Integer startingColumn;
	private Integer endingLine;
	private Integer endingColumn;

	private Hashtable<Integer, Microtask> microtaskMap;

	public QuestionFactory()
	{
		this.microtaskMap = new Hashtable<Integer, Microtask>();
		/* Method invocation */
		templateMethodInvocation.add("Is there any issue with the  method invocation(s) \"<M>\"  at line  <#> that might be related to the failure?");
		/* Conditional */
		templateConditional.add("Is there any issue with the conditional clause between lines <#1> and <#2> that might be related to the failure?");
		/* Loops */
		templateLoop.add("Is there any issue with the <L>-loop between lines <#1> and <#2> that might be related to the failure?");
		/* Variables */
		templateVariable.add("Is there any issue with the definition OR the use of  variable \"<#>\" THAT might be related to the failure?");
	}


	/**
	 * 
	 * @param methodsParsed only the code snippets for which microtasks will be generated
	 * @param i 
	 * @param bugReport 
	 * @return the map of microtasks
	 */
	public Hashtable<Integer, Microtask> generateMicrotasks(Vector<CodeSnippet> methodsParsed, String bugReport, String testCase, int numberOfExistingMicrotasks)
	{
		Integer microtaskId = numberOfExistingMicrotasks;
		this.microtaskMap = new  Hashtable<Integer, Microtask>();
		for (CodeSnippet codeSnippet : methodsParsed)
		{
			Vector<CodeElement> statements = codeSnippet.getStatements();	// now getting the question for the statements

			for (CodeElement element : statements)
			{
				System.out.println(element.getType());
				switch (element.getType())
				{
				case CodeElement.METHOD_INVOCATION:
					for (String templateForQuestion : templateMethodInvocation)
					{
						boolean addQuestion = false; 		// assuming question is NOT good formed 
						MyMethodCall elementCall = (MyMethodCall)element;
						/* setting up the position for the element */
						
						StringBuilder methodNames = new StringBuilder();
						methodNames.append(elementCall.getName());
						if(elementCall.getNestedMethods().size() > 0)
						{
							List<String> nestedMethods = elementCall.getNestedMethods();
							for(int i=0; i<nestedMethods.size();i++)
							{
								methodNames.append(", ");
								methodNames.append(nestedMethods.get(i));
							}
						}
						questionPrompt = new String(templateForQuestion);

						if ( -1 != questionPrompt.indexOf("parameters"))	// question about parameters
						{	// are there parameters? ('2' for the brackets) 
							if (1 < elementCall.getNumberOfParameters())	
								addQuestion = true;	// question is all set
						}
						else
							addQuestion = true;		// other questions are always good formed

						if (addQuestion)
						{	// then add question!
							this.startingLine = elementCall.getElementStartingLine();
							this.startingColumn = elementCall.getElementStartingColumn();
							this.endingLine = elementCall.getElementEndingLine();
							this.endingColumn = elementCall.getElementEndingColumn();

							questionPrompt = questionPrompt.replaceAll("<M>", methodNames.toString());
							//questionPrompt = questionPrompt.replaceAll("<G>", codeSnippet.getMethodSignature().getName());
							questionPrompt = questionPrompt.replaceAll("<#>", this.startingLine.toString());

							question = new Microtask(CodeElement.METHOD_INVOCATION, codeSnippet, elementCall, 
									questionPrompt, this.startingLine, this.startingColumn, this.endingLine, this.endingColumn, microtaskId, bugReport, testCase);
							this.microtaskMap.put(question.getID(),question);
							microtaskId++;
						}
					}
					break;

				case CodeElement.IF_CONDITIONAL:
					for (String templateForQuestion : templateConditional)
					{
						MyIfStatement elementIf = (MyIfStatement)element;
						questionPrompt = new String(templateForQuestion);
						
						// the starting line of the body
						this.startingLine = elementIf.getElementStartingLine();
						this.startingColumn = elementIf.getElementStartingColumn();
						
						// Verifies existence of else clause
						if (elementIf.isThereIsElse())
						{	// get Else statement end
							this.endingLine = elementIf.getElseEndingLine();
							this.endingColumn = elementIf.getElseEndingColumn();
						} else 
						{	// no Else then get just body  end (then statement end)
							this.endingLine = elementIf.getBodyEndingLine();
							this.endingColumn = elementIf.getBodyEndingColumn();
						}
						
						// Changes the template according to the line structure of the statement
						if ( !(this.startingLine.equals(this.endingLine)))
						{
							questionPrompt = questionPrompt.replaceAll("<#1>", this.startingLine.toString());
							questionPrompt = questionPrompt.replaceAll("<#2>", this.endingLine.toString());
						}
						else
						{
							questionPrompt = questionPrompt.substring(0, questionPrompt.indexOf("between")) + "at line " +
									this.startingLine + questionPrompt.substring(questionPrompt.indexOf("<#2>")+4);
						}
						question = new Microtask(CodeElement.IF_CONDITIONAL, codeSnippet, elementIf, 
								questionPrompt, this.startingLine, this.startingColumn, this.endingLine, this.endingColumn, microtaskId, bugReport, testCase);

						this.microtaskMap.put(question.getID(),question);
						microtaskId++;

					}
					break;

				case CodeElement.SWITCH_CONDITIONAL:
					for (String templateForQuestion : templateConditional)
					{
						questionPrompt = new String(templateForQuestion);
						questionPrompt = this.setUpQuestionPrompt(questionPrompt, element);
						question = new Microtask(CodeElement.SWITCH_CONDITIONAL, codeSnippet, element, 
								questionPrompt, element.getElementStartingLine(), element.getElementStartingColumn()
								, element.getBodyEndingLine(),element.getBodyEndingColumn()
								, microtaskId, bugReport, testCase);

						this.microtaskMap.put(question.getID(),question);
						microtaskId++;
					}
					break;

				case CodeElement.FOR_LOOP:
				case CodeElement.DO_LOOP:
				case CodeElement.WHILE_LOOP:
					for (String templateLoopQuestion : templateLoop)
					{
						questionPrompt = new String(templateLoopQuestion);
						questionPrompt = this.setUpQuestionPrompt(questionPrompt, element);
						questionPrompt = questionPrompt.replaceAll("<#1>", element.getElementStartingLine().toString());
						questionPrompt = questionPrompt.replaceAll("<#2>", element.getElementEndingLine().toString());
						this.startingLine = element.getElementStartingLine();
						this.startingColumn = element.getElementStartingColumn();
						/*this.endingLine = element.getElementEndingLine();
						this.endingColumn = element.getElementEndingColumn();*/
						switch (element.getType()) {
						case CodeElement.FOR_LOOP:
							questionPrompt = questionPrompt.replaceAll("<L>", "For");
							question = new Microtask(CodeElement.FOR_LOOP, codeSnippet, element, 
									questionPrompt, this.startingLine, this.startingColumn, this.endingLine, this.endingColumn, microtaskId, bugReport, testCase);
							break;
						case CodeElement.DO_LOOP:
							this.startingLine = element.getBodyStartingLine();
							this.startingColumn = element.getBodyStartingColumn();
							this.endingColumn = element.getBodyEndingColumn();
							this.endingLine = element.getBodyEndingLine();
							questionPrompt = questionPrompt.replaceAll("<L>", "Do");
							question = new Microtask(CodeElement.DO_LOOP, codeSnippet, element, 
									questionPrompt, this.startingLine, this.startingColumn, this.endingLine,this.endingColumn, microtaskId, bugReport, testCase);
							break;
						case CodeElement.WHILE_LOOP:
							questionPrompt = questionPrompt.replaceAll("<L>", "While");
							question = new Microtask(CodeElement.WHILE_LOOP, codeSnippet, element, 
									questionPrompt, this.startingLine, this.startingColumn, this.endingLine,this.endingColumn, microtaskId, bugReport, testCase);
							break;
						}
						this.microtaskMap.put(question.getID(),question);
						microtaskId++;
					}
					break;
					// Variable declarations 
				case CodeElement.VARIABLE_DECLARATION:
					this.startingLine = element.getElementStartingLine();
					this.startingColumn = element.getElementStartingColumn();
					this.endingLine = element.getElementEndingLine();
					this.endingColumn = element.getElementEndingColumn();
					
					for (String templateVariableQuestion : templateVariable) {
						questionPrompt = new String(templateVariableQuestion);
						questionPrompt = questionPrompt.replaceAll("<#>", ((MyVariable)element).getName() );
						question = new Microtask(CodeElement.VARIABLE_DECLARATION, codeSnippet, element,
								questionPrompt, this.startingLine, this.startingColumn, this.endingLine, this.endingColumn, microtaskId, bugReport, testCase);
						this.microtaskMap.put(question.getID(), question);
						microtaskId++;
					}
					break;
					// Add more cases here
				default:
					System.out.println("!!! Type of element did not matched: " + element.getType() + " !!!");
					break;
				} 
			}
		}
		return this.microtaskMap;
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

			//			System.out.println("Starting and ending line: " + this.startingLine + ", " + this.endingLine);
			if ( this.startingLine != this.endingLine )
			{
				questionPromptArg = questionPromptArg.replaceAll("<#1>", this.startingLine.toString());
				questionPromptArg = questionPromptArg.replaceAll("<#2>", this.endingLine.toString());
			}
			else
			{
				//				System.out.println("Old question: " + questionPromptArg);
				questionPromptArg = questionPromptArg.substring(0, questionPromptArg.indexOf("between")) + "at line " +
						this.startingLine + questionPromptArg.substring(questionPromptArg.indexOf("<#2>")+4);
				//				System.out.println("New question: " + questionPromptArg);
			}
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


	public Hashtable<Integer, Microtask> getConcreteQuestions() {
		return microtaskMap;
	}
}
