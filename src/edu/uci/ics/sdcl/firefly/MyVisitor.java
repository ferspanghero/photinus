package edu.uci.ics.sdcl.firefly;

import java.lang.reflect.Modifier;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;

public class MyVisitor extends ASTVisitor {
	
	private CompilationUnit cu;
	private String className;
	private String packageName;
//	private Integer methodIndex;
	private CodeSnippet newMethod;
	
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
	
	/*Statements */
	public boolean visit(IfStatement node)
	{
		System.out.println("If at line: " + cu.getLineNumber(node.getStartPosition()));	
		CodeElement element = new CodeElement(CodeElement.IF_CONDITIONAL, 
				cu.getLineNumber(node.getStartPosition()));
		newMethod.addElement(element);
		return true;
	}
	
	public boolean visit(WhileStatement node)
	{
		System.out.println("While at line: " + cu.getLineNumber(node.getStartPosition()));
		CodeElement element = new CodeElement(CodeElement.WHILE_LOOP, 
				cu.getLineNumber(node.getStartPosition()));
		newMethod.addElement(element);
		return true;
	}
	
	public boolean visit(ForStatement node)
	{
		System.out.println("For at line: " + cu.getLineNumber(node.getStartPosition()));
		CodeElement element = new CodeElement(CodeElement.FOR_LOOP, 
				cu.getLineNumber(node.getStartPosition()));
		newMethod.addElement(element);
		return true;
	}
	
	public boolean visit(SwitchStatement node)
	{
		System.out.println("Switch at line: " + cu.getLineNumber(node.getStartPosition()));	
		CodeElement element = new CodeElement(CodeElement.SWITCH_CONDITIONAL, 
				cu.getLineNumber(node.getStartPosition()));
		newMethod.addElement(element);
		return true;
	}
}
