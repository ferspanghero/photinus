package edu.uci.ics.sdcl.firefly;

import java.lang.reflect.Modifier;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;

public class MyVisitor extends ASTVisitor {
	
	private CompilationUnit cu;
	private String className;
	private String packageName;
//	private Integer methodIndex;
	private CodeSnippet newMethod;
//	private SwitchStatement lastSwitchVisited;
//	private SwitchCase lastCaseVisited;
	
	public MyVisitor(CompilationUnit cuArg)
	{
		this.cu = cuArg;
		String nameOfThePackg = new String();
		if (null == cu.getPackage())
		{
			System.out.println("Package: default");
			nameOfThePackg = "default";
		}
		else
		{
			System.out.println("Package: " + cu.getPackage().getName());
			nameOfThePackg = cu.getPackage().getName().toString();
		}
		this.packageName = nameOfThePackg;
	}
	
	public boolean visit(TypeDeclaration node)
	{
		System.out.println("TypeDeclaration at line: " + cu.getLineNumber(node.getStartPosition()));	
		System.out.println("TypeDeclaration name: " + node.getName());
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
		Integer lnNumber;
		System.out.println("-----------");
		lnNumber = cu.getLineNumber(node.getStartPosition());
		System.out.println("Method at line: " + lnNumber);
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
			System.out.println("Method name: " + name);
		}
		
		/* For visibility */
		int modifierIdent = node.getModifiers();
		System.out.print("Visibility: ");
		if (Modifier.isPrivate(modifierIdent))
			visibility = "Private";
		else if (Modifier.isProtected(modifierIdent))
			visibility = "Protected";
		else if (Modifier.isPublic(modifierIdent))
			visibility = "Public";
		else
			visibility = "Undefined";
		System.out.println(visibility);
		
		System.out.println("Return type: " + node.getReturnType2());
		if ( null == node.getReturnType2() )
			hasReturnStatement = false;
		else if ( node.getReturnType2().toString().equalsIgnoreCase("void") )
			hasReturnStatement = false;
		else
			hasReturnStatement = true;
		System.out.println("Has return statement: " + hasReturnStatement);
		
		signature = new MethodSignature(name, visibility, lnNumber);
		parameters = node.parameters();
		if (null != parameters)
		{
			for (Object parameter : parameters)
			{
				SingleVariableDeclaration parameterCasted = (SingleVariableDeclaration)parameter;
				String parameterName = parameterCasted.getName().toString();
				String parameterType = parameterCasted.getType().toString();
				MethodParameter parameterReady = new MethodParameter(parameterType, parameterName);
				System.out.println("Parameter name: " + parameterName + " type: " + parameterType);
				signature.addMethodParameters(parameterReady);
			}
		}
		System.out.println("Parameters: " + parameters);
		
		
		if (null == node.getBody())
			body = null;
		else
			body = node.getBody().toString();
		System.out.println("Body: " + body);
		/* Creating new object */
		newMethod = new CodeSnippet(this.packageName, this.className, body,
				hasReturnStatement, signature);
		CodeSnippetFactory.addToCodeSnippetList(newMethod);
//		this.methodIndex = CodeSnippetFactory.getIndexCSList(newMethod);
		return true;
	}
	
	/* Method Calls */
	public boolean visit(MethodInvocation node)
	{
		if (!node.getExpression().toString().equalsIgnoreCase("System.out"))	// ignoring System.out calls
		{
			System.out.println("Method invocation at line: " + cu.getLineNumber(node.getStartPosition()));	
			System.out.println("Method name: " + node.getName().toString());
			System.out.println("Method expression: " + node.getExpression().toString());
			System.out.println("Method parameters " + node.arguments());	
			@SuppressWarnings("unchecked")
			myMethodCall methodCall = new myMethodCall(node.getName().toString(), node.getExpression().toString(),
					node.arguments(), cu.getLineNumber(node.getStartPosition()));
			newMethod.addElement(methodCall);
		}
		return true;
	}
	
	/*Statements */
	public boolean visit(IfStatement node)
	{
		System.out.println("If at line: " + cu.getLineNumber(node.getStartPosition()));	
		String str = new String(node.getThenStatement().toString());
		String[] lines = str.split("\r\n|\r|\n");
		Integer endPosition =  cu.getLineNumber(node.getThenStatement().getStartPosition()) + lines.length -1;
//		System.out.println("Then statement: " + node.getThenStatement().toString());
		if (null != node.getElseStatement())
		{	
//			System.out.println("---> Start for the else estatement " + cu.getLineNumber((node.getElseStatement().getStartPosition())));
			str = new String(node.getElseStatement().toString());
			String[] lines2 = str.split("\r\n|\r|\n");
			endPosition = cu.getLineNumber(node.getElseStatement().getStartPosition()) + lines2.length -1;
//			System.out.println("Else statement: " + node.getElseStatement().toString());
		}
		CodeElement element = new CodeElement(CodeElement.IF_CONDITIONAL, 
				cu.getLineNumber(node.getStartPosition()), endPosition);
		newMethod.addElement(element);
		return true;
	}
	
	public boolean visit(SwitchStatement node)
	{
//		this.lastSwitchVisited = node;
		System.out.println("Switch at line: " + cu.getLineNumber(node.getStartPosition()));	
		CodeElement element = new CodeElement(CodeElement.SWITCH_CONDITIONAL, 
				cu.getLineNumber(node.getStartPosition()));
		newMethod.addElement(element);
		return true;
	}
	
	public boolean visit(SwitchCase node)
	{
//		this.lastCaseVisited = node;
		System.out.println("Case at line: " + cu.getLineNumber(node.getStartPosition()));	
		return true;
	}
	
	public boolean visit(ForStatement node)
	{
		System.out.println("For at line: " + cu.getLineNumber(node.getStartPosition()));
		String str = node.getBody().toString();
		String[] lines = str.split("\r\n|\r|\n");
		Integer endPosition = cu.getLineNumber(node.getBody().getStartPosition()) + lines.length -1;
		CodeElement element = new CodeElement(CodeElement.FOR_LOOP, 
				cu.getLineNumber(node.getStartPosition()), endPosition);
		newMethod.addElement(element);
		System.out.println("For statement: " + node.getBody().toString());
		return true;
	}
	
	public boolean visit(EnhancedForStatement node)
	{
		System.out.println("ForEach at line: " + cu.getLineNumber(node.getStartPosition()));
		String str = node.getBody().toString();
		String[] lines = str.split("\r\n|\r|\n");
		Integer endPosition = cu.getLineNumber(node.getBody().getStartPosition()) + lines.length -1;
		CodeElement element = new CodeElement(CodeElement.FOR_LOOP, 
				cu.getLineNumber(node.getStartPosition()), endPosition);
		newMethod.addElement(element);
		System.out.println("For statement: " + node.getBody().toString());
		return true;
	}
	
	public boolean visit(DoStatement node)
	{
		System.out.println("Do-While at line: " + cu.getLineNumber(node.getStartPosition()));
		String str = node.getBody().toString();
		String[] lines = str.split("\r\n|\r|\n");
		Integer endPosition = cu.getLineNumber(node.getBody().getStartPosition()) + lines.length -1;
		CodeElement element = new CodeElement(CodeElement.DO_LOOP, 
				cu.getLineNumber(node.getStartPosition()), endPosition);
		newMethod.addElement(element);
		System.out.println("Do-While statement: " + node.getBody().toString());
		return true;
	}
	
	public boolean visit(WhileStatement node)
	{
		System.out.println("While at line: " + cu.getLineNumber(node.getStartPosition()));
		String str = node.getBody().toString();
		String[] lines = str.split("\r\n|\r|\n");
		Integer endPosition = cu.getLineNumber(node.getBody().getStartPosition()) + lines.length -1;
		CodeElement element = new CodeElement(CodeElement.WHILE_LOOP, 
				cu.getLineNumber(node.getStartPosition()), endPosition);
		newMethod.addElement(element);
		System.out.println("While statement: " + node.getBody().toString());
		return true;
	}
	
	public boolean visit(ReturnStatement node)
	{
		System.out.println("Return at line: " + cu.getLineNumber(node.getStartPosition()));
		CodeElement element = new CodeElement(CodeElement.RETURN_STATEMENT, 
				cu.getLineNumber(node.getStartPosition()));
		newMethod.addElement(element);
		return true;
	}
	
}
