package edu.uci.ics.sdcl.firefly;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;

import edu.uci.ics.sdcl.firefly.util.PositionFinder;

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
	private PositionFinder positionFinder;
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
		//System.out.println("TypeDeclaration at line: " + cu.getLineNumber(node.getStartPosition()));	
	//	System.out.println("TypeDeclaration name: " + node.getName());
		this.className = node.getName().toString();
		return true;
	}

	public boolean visit(FieldDeclaration node){
		//Ignore field declarations and don't allow that 
		//methodInvocations and ClassInstanceCreation nodes be added to previous methods.
		this.newMethod = null;
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean visit(MethodDeclaration node)
	{
		String name;
		String body; 
		Boolean hasReturnStatement; 
		MethodSignature signature;
		String visibility;
		List<Object> parameters;
		//System.out.println("-----------");
		this.elementStartingLine = cu.getLineNumber(node.getStartPosition());	
		this.elementStartingColumn = cu.getColumnNumber(node.getStartPosition());
		//System.out.println("Method at starting line: " + this.elementStartingLine + ", starting column: "+ this.elementStartingColumn);

		//System.out.println("Method declaration name: " + node.getName().getFullyQualifiedName()); 
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
			visibility = "Package";
		//		System.out.println(visibility);

		//		System.out.println("Return type: " + node.getReturnType2());
		if ( null == node.getReturnType2() )
			hasReturnStatement = false;
		else if ( node.getReturnType2().toString().equalsIgnoreCase("void") )
			hasReturnStatement = false;
		else
			hasReturnStatement = true;
		//		System.out.println("Has return statement: " + hasReturnStatement);

		signature = new MethodSignature(name, visibility);
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

		int nameStartingLine = cu.getLineNumber(node.getName().getStartPosition());
		if (this.elementStartingLine !=  nameStartingLine)
		{	// that means that are comment(s) before the method which is(are) misleading the starting line
			this.elementStartingLine = nameStartingLine;	// but now I do not know the column start
			//System.out.println("[MV] New line: " + this.elementStartingLine);

			/* Finding the column start and the end position for the element */
			this.positionFinder = new PositionFinder(this.elementStartingLine,
					snippetFactory.getFileContentPerLine(), '(', ')');
			this.positionFinder.computeStartEndPosition();
			this.elementEndingLine = this.positionFinder.getEndingLineNumber();
			this.elementEndingColumn = this.positionFinder.getEndingColumnNumber();
		}
		setupElementEndPosition();
		if (null == node.getBody())
		{
			body = null;
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


	public boolean visit(ClassInstanceCreation node)
	{
		if(isInvalidClassInstantiation(node))
			return true;
		else
			if(node.arguments().size()==0) //Discard constructors that take no parameters.
				return true;
			else{
				Integer numberOfArguments = node.arguments().size();
				//System.out.println("Class instantiation..."+node.getExpression()+" type:"+node.getType());
				String name = node.getType().toString();
				String expression =""; //There is no expression value in ConstructionInvocation node
				String arguments = node.arguments().toString();
				this.elementStartingLine = cu.getLineNumber(node.getStartPosition());
				this.elementStartingColumn = cu.getColumnNumber(node.getStartPosition());
				this.elementEndingColumn = this.elementStartingColumn+ node.getLength();
				this.elementEndingLine = this.elementStartingLine;

				MyMethodCall methodCall = new MyMethodCall(name, expression, arguments,numberOfArguments, 
						this.elementStartingLine, this.elementStartingColumn,
						this.elementEndingLine, this.elementEndingColumn);

				this.newMethod.addElement(methodCall);
			}

		return true;
	}

	/* Method Calls */
	public boolean visit(MethodInvocation node)
	{

		if(isInvalidMethodInvocation(node)) 
			return true;
		else{
			this.elementStartingLine = cu.getLineNumber(node.getStartPosition());
			this.elementStartingColumn = cu.getColumnNumber(node.getStartPosition());

			String name = node.getName().toString();
		   // System.out.println("node.getName: " +name+ " expression= "+node.getExpression());
			String expression = node.getExpression()==null ? "" : node.getExpression().toString(); 
			String arguments = node.arguments()==null ? "" : node.arguments().toString();
			Integer numberOfArguments = node.arguments()==null ? 0 : node.arguments().size();
			
			String line = this.snippetFactory.getFileContentPerLine()[this.elementStartingLine-1];
			this.elementStartingColumn = line.indexOf(node.getName().toString()); 
			this.elementEndingColumn = this.elementStartingColumn+ node.getName().toString().length();
			this.elementEndingLine = this.elementStartingLine;

			MyMethodCall methodCall = new MyMethodCall(name, expression, arguments, numberOfArguments,
					this.elementStartingLine, this.elementStartingColumn,
					this.elementEndingLine, this.elementEndingColumn);

			//System.out.println(methodCall.toString());
			//System.out.println("# of Method invocations: " + ++numberOfMethodInvocations+ "\n");

			/*	System.out.println("Adding methodCall "+methodCall.getName()+"(" +
							methodCall.getParameters() +")"+ 
								" under method "+
									newMethod.getMethodSignature().getName()+"(" +
									newMethod.getMethodSignature().getParameterList().size() +" parameters )");
									*/	

			this.newMethod.addElement(methodCall);

		}
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
				this.bodyPosition.computeEndPosition();
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
	private boolean isInvalidCall(int nodeType, String methodName, Expression expression){

		if(this.newMethod==null) //ignore method calls within a field declaration
			return true;
		else
			if((expression!=null) && ( //Ignore print statements
					(expression.toString().equalsIgnoreCase("System.out")) ||
					(expression.toString().equalsIgnoreCase("System.err"))
					))	
				return true;
			else //Ignore loggers (Log4J)
				if(methodName.equalsIgnoreCase("trace") || 
						methodName.equalsIgnoreCase("log")||
						methodName.equalsIgnoreCase("debug")||
						methodName.equalsIgnoreCase("info") )
					return true;
				else 
					return false;
	}

	private boolean isInvalidMethodInvocation(MethodInvocation node){
		if(node==null)
			return true;
		else
			return isInvalidCall(node.getParent().getNodeType(),node.getName().toString(), node.getExpression());
	}

	private boolean isInvalidClassInstantiation(ClassInstanceCreation node){
		if(node==null)
			return true;
		else
			return isInvalidCall(node.getParent().getNodeType(),node.getType().toString(),node.getExpression());
	}

	/* assuming the correct values for the starting position */
	private void setupElementEndPosition()
	{
		/* Finding the end position for the element */
		this.positionFinder = new PositionFinder(this.elementStartingLine, this.elementStartingColumn, 
				snippetFactory.getFileContentPerLine(), '(', ')');
		this.positionFinder.computeEndPosition();
		this.elementEndingLine = this.positionFinder.getEndingLineNumber();
		this.elementEndingColumn = this.positionFinder.getEndingColumnNumber();
	}

	private void setupBodyEndPostition()
	{
		/* Finding the end position for the body */
		this.bodyPosition = new PositionFinder(this.bodyStartingLine, this.bodyStartingColumn, 
				snippetFactory.getFileContentPerLine(), '{', '}');
		this.bodyPosition.computeEndPosition();
		this.bodyEndingLine = this.bodyPosition.getEndingLineNumber();
		this.bodyEndingColumn = this.bodyPosition.getEndingColumnNumber();
	}
}
