package edu.uci.ics.sdcl.firefly;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;

public class MyVisitor extends ASTVisitor {
	
	private CompilationUnit cu;
	private String className;
	private String packageName;
	private CodeSnippet newMethod;
	private CodeElement element;
	/* Element position */
	private Integer elementStartingLine;	// line number for the element beginning (not the body)
	private Integer elementStartingColumn;	// column number for the element beginning
	private Integer elementEndingLine;		// line number for the element ending
	private Integer elementEndingColumn;	// column number for the element ending
	/* Body position */
	private Integer bodyStartingLine;		// line number for the beginning of body
	private Integer bodyStartingColumn;		// column number for the body (of element in case of method invocation)
	private Integer bodyEndingLine;			// line number for the end of the body
	private Integer bodyEndingColumn;		// column number for the end of the body
	/* to find the end positions */
	private PositionFinder elementPosition;
	private PositionFinder bodyPosition;
	/* specific for the else-if case */
	private Stack<MyIfStatement> ifElements;		// line numbers of if with else-if statements 
	private CodeSnippetFactory snippetFactory;       //Factory that keeps track of the fileName and fileContent being parsed
	
	private int numberOfMethodInvocations;
	
	
	
	public MyVisitor(CompilationUnit cuArg, CodeSnippetFactory snippetFactory)
	{
		super();
		numberOfMethodInvocations = 0;
		this.snippetFactory = snippetFactory;
		this.ifElements = new Stack<MyIfStatement>();
		this.cu = cuArg;
		String nameOfThePackg = new String();
		if (null == cu.getPackage())
		{
//			System.out.println("Package: default");
			nameOfThePackg = "default";
		}
		else
		{
//			System.out.println("Package: " + cu.getPackage().getName());
			nameOfThePackg = cu.getPackage().getName().toString();
		}
		this.packageName = nameOfThePackg;
	}
	
	public boolean visit(TypeDeclaration node)
	{
//		System.out.println("TypeDeclaration at line: " + cu.getLineNumber(node.getStartPosition()));	
//		System.out.println("TypeDeclaration name: " + node.getName());
		this.className = node.getName().toString();
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public boolean visit(MethodDeclaration node)
	{
		String name;
		String body; 
//		String returnType;
		Boolean hasReturnStatement; 
		MethodSignature signature;
		String visibility;
		List<Object> parameters;
		System.out.println("-----------");
		this.elementStartingLine = cu.getLineNumber(node.getStartPosition());
		this.elementStartingColumn = cu.getColumnNumber(node.getStartPosition());
//		System.out.println("Method at line: " + lnNumber);
//		System.out.println("Method name full: " + node.getName().getFullyQualifiedName()); //FullName?
		if ( null == node.getName() )
		{
			name = null;
			System.out.println("Could not get the method name!");
			System.exit(1);
		}
		else
		{
			name = node.getName().toString();
//			System.out.println("Method name: " + name);
		}
		
		/* For visibility */
		int modifierIdent = node.getModifiers();
//		System.out.print("Visibility: ");
		if (Modifier.isPrivate(modifierIdent))
			visibility = "Private";
		else if (Modifier.isProtected(modifierIdent))
			visibility = "Protected";
		else if (Modifier.isPublic(modifierIdent))
			visibility = "Public";
		else
			visibility = "Undefined";
//		System.out.println(visibility);
		
//		System.out.println("Return type: " + node.getReturnType2());
		if ( null == node.getReturnType2() )
			hasReturnStatement = false;
		else if ( node.getReturnType2().toString().equalsIgnoreCase("void") )
			hasReturnStatement = false;
		else
			hasReturnStatement = true;
//		System.out.println("Has return statement: " + hasReturnStatement);
		
		signature = new MethodSignature(name, visibility, this.elementStartingLine);
		parameters = node.parameters();
		if (null != parameters)
		{
			for (Object parameter : parameters)
			{
				SingleVariableDeclaration parameterCasted = (SingleVariableDeclaration)parameter;
				String parameterName = parameterCasted.getName().toString();
				String parameterType = parameterCasted.getType().toString();
				MethodParameter parameterReady = new MethodParameter(parameterType, parameterName);
//				System.out.println("Parameter name: " + parameterName + " type: " + parameterType);
				signature.addMethodParameters(parameterReady);
			}
		}
//		System.out.println("Parameters: " + parameters);
		
		setupElementEndPosition();
		if (null == node.getBody())
		{
			body = null;
			setupElementEndPosition();
			/* Creating new object without body */
			this.newMethod = new CodeSnippet(this.packageName, this.className, signature, body, hasReturnStatement,
					this.elementStartingLine, this.elementStartingColumn,
					this.elementEndingLine, this.elementEndingColumn);
		}	
		else
		{
			body = node.getBody().toString();
			this.bodyStartingLine = cu.getLineNumber(node.getBody().getStartPosition());
			this.bodyStartingColumn = cu.getColumnNumber(node.getBody().getStartPosition());
			setupBodyEndPostition();
			/* Creating new object with body */
			this.newMethod = new CodeSnippet(this.packageName, this.className, signature, body, hasReturnStatement,
					this.elementStartingLine, this.elementStartingColumn,
					this.elementEndingLine, this.elementEndingColumn,
					this.bodyStartingLine, this.bodyStartingColumn,
					this.bodyEndingLine, this.bodyEndingColumn);
		}
		

		this.snippetFactory.addToCodeSnippetList(this.newMethod);
		
		return true;
	}
	
	/* Method Calls */
	public boolean visit(MethodInvocation node)
	{
		if(invalidCall(node)) 
			return true;
		else{
			this.elementStartingLine = cu.getLineNumber(node.getStartPosition());
			this.elementStartingColumn = cu.getColumnNumber(node.getStartPosition());
			
			/* to avoid null pointers */
			String name;
			String expression;
			String arguments;
			if (null == node.getName())
				name = "";
			else
				name = node.getName().toString();
			
			if (null == node.getExpression())
				expression = "";
			else
				expression = node.getExpression().toString();
			
			if (null == node.arguments())
				arguments = "";
			else
				arguments = node.arguments().toString();
			
			System.out.println(this.elementStartingLine + " - " + expression + "." + name + 
					arguments.replace('[', '(').replace(']', ')'));	
//			System.out.println("Method name: " + node.getName().toString());
//			System.out.println("Method expression package: " + node.getExpression().toString());
//			System.out.println("Method parameters " + node.arguments().toString());
			
			setupElementEndPosition();

			MyMethodCall methodCall = new MyMethodCall(name, expression, arguments, 
					this.elementStartingLine, this.elementStartingColumn,
					this.elementEndingLine, this.elementEndingColumn);

			this.newMethod.addElement(methodCall);
		}
		numberOfMethodInvocations++;
		System.out.println("# of Method invocations: " + numberOfMethodInvocations);
		System.out.println();
		return true;
	}
	
	/*Statements */
	public boolean visit(IfStatement node)
	{
		this.elementStartingLine = cu.getLineNumber(node.getStartPosition());
		this.elementStartingColumn = cu.getColumnNumber(node.getStartPosition());
		setupElementEndPosition();
		this.bodyStartingLine = cu.getLineNumber(node.getThenStatement().getStartPosition());
		this.bodyStartingColumn = cu.getColumnNumber(node.getThenStatement().getStartPosition());
		setupBodyEndPostition();
		
//		System.out.println(":::If at line: " + this.elementStartingLine);	
		
		MyIfStatement ifCreated = (MyIfStatement)element;
		if (null == node.getElseStatement()) // There is no else statement (body = then statement)
		{	// just create the element
			ifCreated = new MyIfStatement(this.elementStartingLine, this.elementStartingColumn, 
					this.elementEndingLine, this.elementEndingColumn,
					this.bodyStartingLine, this.bodyStartingColumn,
					this.bodyEndingLine, this.bodyEndingColumn);
			
			// checking if belongs to an else-if statement
			if (MyIfStatement.isIfofAnElseIfCase(this.snippetFactory.getFileContentPerLine(), this.elementStartingLine-1))
			{	// match this if with its else-if case on hold onto stack
//				System.out.println("!:This if w NO else is part of an Else-If case");
				ifCreated.setIfOfAnElse(true);
				
				MyIfStatement ifElementOnStack;
				do
				{
					ifElementOnStack = this.ifElements.pop();
					ifElementOnStack.setElseEndingLine(this.bodyEndingLine);
					ifElementOnStack.setElseEndingColumn(this.bodyEndingColumn);
					this.newMethod.addElement(ifElementOnStack);
				}while( ifElementOnStack.isIfOfAnElse() );
				
			}
			this.newMethod.addElement(ifCreated);
		}
		else	// there is an else statement 
		{			
			Integer elseStartingLine = cu.getLineNumber(node.getElseStatement().getStartPosition());
			Integer elseStartingColumn = cu.getColumnNumber(node.getElseStatement().getStartPosition());
			if ( node.getElseStatement().toString().substring(0, 2).equals("if") )
			{	// else-if case
				// setting the element as unknown end position
				ifCreated = new MyIfStatement(this.elementStartingLine, this.elementStartingColumn, 
						this.elementEndingLine, this.elementEndingColumn,
						this.bodyStartingLine, this.bodyStartingColumn,
						this.bodyEndingLine, this.bodyEndingColumn,
						elseStartingLine, elseStartingColumn,
						CodeElement.NO_NUMBER_ASSOCIATED, CodeElement.NO_NUMBER_ASSOCIATED);
				
				if (MyIfStatement.isIfofAnElseIfCase(snippetFactory.getFileContentPerLine(), this.elementStartingLine-1))
				{	// match this if with its else-if case on hold onto stack
//					System.out.println("!:This if w NO else is part of an Else-If case");
					ifCreated.setIfOfAnElse(true);
				}
				// adding element to the stack, not added on NewMethod yet
				this.ifElements.push(ifCreated);
//				System.out.println("This if is now onto Stack!");
				
			} 
			else
			{	/* Finding the end position for the else [not the else-if case] */
				this.bodyPosition = new PositionFinder(elseStartingLine, elseStartingColumn, 
						snippetFactory.getFileContentPerLine(), '{', '}');
				Integer elseEndingLine = this.bodyPosition.getEndingLineNumber();
				Integer elseEndingColumn = this.bodyPosition.getEndingColumnNumber();
				ifCreated = new MyIfStatement(this.elementStartingLine, this.elementStartingColumn, 
						this.elementEndingLine, this.elementEndingColumn,
						this.bodyStartingLine, this.bodyStartingColumn,
						this.bodyEndingLine, this.bodyEndingColumn,
						elseStartingLine, elseStartingColumn,
						elseEndingLine, elseEndingColumn);
				
				// checking if belongs to an else-if statement
				if (MyIfStatement.isIfofAnElseIfCase(snippetFactory.getFileContentPerLine(), this.elementStartingLine-1))
				{	// match this if with its else-if case on hold onto stack
//					System.out.println("::This if w else is part of an Else-If case");
					ifCreated.setIfOfAnElse(true);
					
					MyIfStatement ifElementOnStack;
					do
					{
						ifElementOnStack = this.ifElements.pop();
						ifElementOnStack.setElseEndingLine(elseEndingLine);
						ifElementOnStack.setElseEndingColumn(elseEndingColumn);
						this.newMethod.addElement(ifElementOnStack);
					}while( ifElementOnStack.isIfOfAnElse() );
				}
				this.newMethod.addElement(ifCreated);
			}
		}
		
//		System.out.println(" $$$ CS = " + element.getColumnStart());
//		System.out.println(" $$$ CE = " + element.getBodyEndingColumn());
		return true;
	}
	
	public boolean visit(SwitchStatement node)
	{
		this.elementStartingLine = cu.getLineNumber(node.getStartPosition());
		this.elementStartingColumn = cu.getColumnNumber(node.getStartPosition());
		setupElementEndPosition();

		this.bodyStartingLine = this.elementEndingLine;
		this.bodyStartingColumn = this.elementEndingColumn; // Not much precision here!
		setupBodyEndPostition();
		/* starting line updated just because of the switch statement body */
		this.bodyStartingLine = this.bodyPosition.getStartingLineNumber();
		this.bodyStartingColumn = this.bodyPosition.getStartingColumnNumber();
		
//		System.out.println("Switch at line: " + this.elementStartingLine);	
		CodeElement element = new CodeElement(CodeElement.SWITCH_CONDITIONAL, 
				this.elementStartingLine, this.elementStartingColumn, 
				this.elementEndingLine, this.elementEndingColumn,
				this.bodyStartingLine, this.bodyStartingColumn,
				this.bodyEndingLine, this.bodyEndingColumn);
		this.newMethod.addElement(element);
		return true;
	}
	
	public boolean visit(SwitchCase node)
	{
//		this.lastCaseVisited = node;
//		System.out.println("Case at line: " + cu.getLineNumber(node.getStartPosition()));	
		return true;
	}
	
	public boolean visit(ForStatement node)
	{
		this.elementStartingLine = cu.getLineNumber(node.getStartPosition());
		this.elementStartingColumn = cu.getColumnNumber(node.getStartPosition());
		setupElementEndPosition();

		this.bodyStartingLine = cu.getLineNumber(node.getBody().getStartPosition());
		this.bodyStartingColumn = cu.getColumnNumber(node.getBody().getStartPosition());
		setupBodyEndPostition();
		
//		System.out.println("For at line: " + cu.getLineNumber(node.getStartPosition()));
		CodeElement element = new CodeElement(CodeElement.FOR_LOOP, 
				this.elementStartingLine, this.elementStartingColumn, 
				this.elementEndingLine, this.elementEndingColumn,
				this.bodyStartingLine, this.bodyStartingColumn,
				this.bodyEndingLine, this.bodyEndingColumn);
//		System.out.println("For-body column starting point: " + element.getColumnStart());
		this.newMethod.addElement(element);

//		System.out.println("For statement: " + node.getBody().toString());
		return true;
	}
	
	public boolean visit(EnhancedForStatement node)
	{
		this.elementStartingLine = cu.getLineNumber(node.getStartPosition());
		this.elementStartingColumn = cu.getColumnNumber(node.getStartPosition());
		setupElementEndPosition();

		this.bodyStartingLine = cu.getLineNumber(node.getBody().getStartPosition());
		this.bodyStartingColumn = cu.getColumnNumber(node.getBody().getStartPosition());
		setupBodyEndPostition();
		
//		System.out.println("ForEach at line: " + cu.getLineNumber(node.getStartPosition()));
		CodeElement element = new CodeElement(CodeElement.FOR_LOOP, 
				this.elementStartingLine, this.elementStartingColumn, 
				this.elementEndingLine, this.elementEndingColumn,
				this.bodyStartingLine, this.bodyStartingColumn,
				this.bodyEndingLine, this.bodyEndingColumn);
		
		this.newMethod.addElement(element);
//		System.out.println("For statement: " + node.getBody().toString());
		return true;
	}
	
	public boolean visit(DoStatement node)
	{
		
		this.elementStartingLine = cu.getLineNumber(node.getExpression().getStartPosition());
		/* Finding where the 'while'-word start */
		String[] fileInLines = snippetFactory.getFileContentPerLine();
		this.elementStartingColumn =  fileInLines[this.elementStartingLine-1].indexOf("while");
		/* setting up the end position */
		setupElementEndPosition();

		this.bodyStartingLine = cu.getLineNumber(node.getBody().getStartPosition());
		this.bodyStartingColumn = cu.getColumnNumber(node.getBody().getStartPosition());
		setupBodyEndPostition();
		
//		System.out.println("Do-While at line: " + cu.getLineNumber(node.getStartPosition()));
		CodeElement element = new CodeElement(CodeElement.DO_LOOP, 
				this.elementStartingLine, this.elementStartingColumn, 
				this.elementEndingLine, this.elementEndingColumn,
				this.bodyStartingLine, this.bodyStartingColumn,
				this.bodyEndingLine, this.bodyEndingColumn);
		
		this.newMethod.addElement(element);
//		System.out.println("Do-While statement: " + node.getBody().toString());
		return true;
	}
	
	public boolean visit(WhileStatement node)
	{
		this.elementStartingLine = cu.getLineNumber(node.getStartPosition());
		this.elementStartingColumn = cu.getColumnNumber(node.getStartPosition());
		setupElementEndPosition();

		this.bodyStartingLine = cu.getLineNumber(node.getBody().getStartPosition());
		this.bodyStartingColumn = cu.getColumnNumber(node.getBody().getStartPosition());
		setupBodyEndPostition();
		
//		System.out.println("While at line: " + cu.getLineNumber(node.getStartPosition()));
		CodeElement element = new CodeElement(CodeElement.WHILE_LOOP, 
				this.elementStartingLine, this.elementStartingColumn, 
				this.elementEndingLine, this.elementEndingColumn,
				this.bodyStartingLine, this.bodyStartingColumn,
				this.bodyEndingLine, this.bodyEndingColumn);
		
		this.newMethod.addElement(element);
//		System.out.println("While statement: " + node.getBody().toString());
		return true;
	}
	
	/* public boolean visit(ReturnStatement node)
	{
		System.out.println("Return at line: " + cu.getLineNumber(node.getStartPosition()));
		CodeElement element = new CodeElement(CodeElement.RETURN_STATEMENT, 
				cu.getLineNumber(node.getStartPosition()));
		newMethod.addElement(element);
		return true;
	} */
	
	/**
	 * Check whether a method invocation is an invalid method call. Two types of invalid calls, 
	 * when the node is null or it is an API call.
	 * @param node
	 * @return true if calls System.out, otherwise false. Also returns false if a null pointer is provided
	 */
	private boolean invalidCall(MethodInvocation node){
		if((node==null))
			return true;
		else
			if((node.getExpression()!=null) && (node.getExpression().toString().equalsIgnoreCase("System.out")))	// ignoring System.out calls
					return true;
			else 
				return false;
	}
	
	/* assuming the correct values for the starting position */
	private void setupElementEndPosition()
	{
		/* Finding the end position for the element */
		this.elementPosition = new PositionFinder(this.elementStartingLine, this.elementStartingColumn, 
				snippetFactory.getFileContentPerLine(), '(', ')');
		this.elementEndingLine = this.elementPosition.getEndingLineNumber();
		this.elementEndingColumn = this.elementPosition.getEndingColumnNumber();
	}
	
	private void setupBodyEndPostition()
	{
		/* Finding the end position for the body */
		this.bodyPosition = new PositionFinder(this.bodyStartingLine, this.bodyStartingColumn, 
				snippetFactory.getFileContentPerLine(), '{', '}');
		this.bodyEndingLine = this.bodyPosition.getEndingLineNumber();
		this.bodyEndingColumn = this.bodyPosition.getEndingColumnNumber();
	}
}
