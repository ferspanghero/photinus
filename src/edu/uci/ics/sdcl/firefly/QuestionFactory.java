package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;

public class QuestionFactory {

	/* Template Lists */
	public ArrayList<String> templateMethodDeclaration = new ArrayList<String>();
	public ArrayList<String> templateMethodInvocation = new ArrayList<String>();
	public ArrayList<String> templateConditional = new ArrayList<String>();
	public ArrayList<String> templateLoop = new ArrayList<String>();

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
		/* Method Declaration */
		templateMethodDeclaration.add("Is there maybe something wrong in the declaration of function '<F>' at line <#> " 
				+ "(e.g., requires a parameter that is not listed, needs different parameters to produce the correct result, specifies the wrong or no return type, etc .)?");
		templateMethodDeclaration.add("Is there possibly something wrong with the body of function '<F>' between lines "
				+ "<#1> and <#2> (e.g., function produces an incorrect return value, return statement is at the wrong place, does not properly handle error situations, etc.)?");
		/* Method invocation */
		templateMethodInvocation.add("Is there maybe something wrong with the invocation of function '<F>' in function "
				+ "'<G>' at line <#> (e.g., should be at a different place in the code, should invoke a different "
				+ "function, has unanticipated side effects, return value is improperly used, etc.)");
		templateMethodInvocation.add("Is there perhaps something wrong with the values of the parameters received "
				+ "by function '<F>' when called by function '<G>' at line <#> (e.g., wrong variables used as "
				+ "parameters, wrong order, missing or wrong type of parameter, values of the parameters are not checked, etc .)?");
		/* Conditional */
		templateConditional.add("Is there any issue with the conditional clause between lines <#1> and <#2> that might be related to the failure?");
		/* Loops */
		templateLoop.add("Is there any issue with the <L>-loop between lines <#1> and <#2> that might be related to the failure?");
	}


	/**
	 * 
	 * @param methodsParsed only the code snippets for which microtasks will be generated
	 * @param i 
	 * @param bugReport 
	 * @return the map of microtasks
	 */
	public Hashtable<Integer, Microtask> generateMicrotasks(Vector<CodeSnippet> methodsParsed, String bugReport, int numberOfExistingMicrotasks)
	{
		Integer microtaskId = numberOfExistingMicrotasks;
		this.microtaskMap = new  Hashtable<Integer, Microtask>();
		for (CodeSnippet codeSnippet : methodsParsed)
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

					if ( this.startingLine != this.endingLine )
					{	// different lines, so between is OK
						questionPrompt = questionPrompt.replaceAll("<#1>", this.startingLine.toString());
						questionPrompt = questionPrompt.replaceAll("<#2>", this.endingLine.toString());
					}
					else
					{	// same lines, so between is has to be replaced
						questionPrompt = questionPrompt.substring(0, questionPrompt.indexOf("between")) + "at line " +
								this.startingLine + questionPrompt.substring(questionPrompt.indexOf("<#2>")+4); 
					}
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
				question = new Microtask(CodeElement.METHOD_DECLARATION, codeSnippet, null,
						questionPrompt, this.startingLine, this.startingColumn, this.endingLine,this.endingColumn, microtaskId, bugReport);

				this.microtaskMap.put(question.getID(),question);
				microtaskId++;

			}

			Vector<CodeElement> statements = codeSnippet.getStatements();	// now getting the question for the statements


			for (CodeElement element : statements)
			{
				switch (element.getType())
				{
				case CodeElement.METHOD_INVOCATION:
					for (String templateForQuestion : templateMethodInvocation)
					{
						boolean addQuestion = false; 		// assuming question is NOT good formed 
						MyMethodCall elementCall = (MyMethodCall)element;
						/* setting up the position for the element */

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

							questionPrompt = questionPrompt.replaceAll("<F>", elementCall.getName());
							questionPrompt = questionPrompt.replaceAll("<G>", codeSnippet.getMethodSignature().getName());
							questionPrompt = questionPrompt.replaceAll("<#>", this.startingLine.toString());

							question = new Microtask(CodeElement.METHOD_INVOCATION, codeSnippet, elementCall, 
									questionPrompt, this.startingLine, this.startingColumn, this.endingLine, this.endingColumn, microtaskId, bugReport);
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
						if ( this.startingLine != this.endingLine )
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
								questionPrompt, this.startingLine, this.startingColumn, this.endingLine, this.endingColumn, microtaskId, bugReport);

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
								questionPrompt, this.startingLine, this.startingColumn, this.endingLine,this.endingColumn, microtaskId, bugReport);

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
						switch (element.getType()) {
						case CodeElement.FOR_LOOP:
							questionPrompt = questionPrompt.replaceAll("<L>", "For");
							question = new Microtask(CodeElement.FOR_LOOP, codeSnippet, element, 
									questionPrompt, this.startingLine, this.startingColumn, this.endingLine, this.endingColumn, microtaskId, bugReport);
							break;
						case CodeElement.DO_LOOP:
							questionPrompt = questionPrompt.replaceAll("<L>", "Do");
							question = new Microtask(CodeElement.DO_LOOP, codeSnippet, element, 
									questionPrompt, this.startingLine, this.startingColumn, this.endingLine,this.endingColumn, microtaskId, bugReport);
							break;
						case CodeElement.WHILE_LOOP:
							questionPrompt = questionPrompt.replaceAll("<L>", "While");
							question = new Microtask(CodeElement.WHILE_LOOP, codeSnippet, element, 
									questionPrompt, this.startingLine, this.startingColumn, this.endingLine,this.endingColumn, microtaskId, bugReport);
							break;
						}
						this.microtaskMap.put(question.getID(),question);
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
