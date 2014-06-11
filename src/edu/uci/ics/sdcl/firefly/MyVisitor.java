package edu.uci.ics.sdcl.firefly;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
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
		String returnType;
		Boolean hasReturnStatement; 
		MethodSignature signature;
		String visibility;
		ArrayList<SingleVariableDeclaration> parameters;
		Integer lnNumber;
		System.out.println("-----------");
		System.out.println("Method at line: " + cu.getLineNumber(node.getStartPosition()));
		lnNumber = cu.getLineNumber(node.getStartPosition());
//		System.out.println("Method name full: " + node.getName().getFullyQualifiedName()); //FullName?
		System.out.println("Method name: " + node.getName());
		name = node.getName().toString();
		/* For visibility */
		System.out.print("Visibility: ");
		int modifierIdent = node.getModifiers();
		if (Modifier.isPrivate(modifierIdent))
		{
			System.out.println("Private");
			visibility = "Private";
		}
		else if (Modifier.isProtected(modifierIdent))
		{
			System.out.println("Protected");
			visibility = "Protected";
		}
		else if (Modifier.isPublic(modifierIdent))
		{
			System.out.println("Public");
			visibility = "Public";
		}
		else
		{
			System.out.println("Undefined");
			visibility = "Undefined";
		}
		System.out.println("Return type: " + node.getReturnType2());
		returnType = node.getReturnType2().toString();
		if ( (null == returnType) || (returnType.equalsIgnoreCase("void")) )
			hasReturnStatement = false;
		else
			hasReturnStatement = true;
		System.out.println("Parameters: " + node.parameters());
		parameters = (ArrayList<SingleVariableDeclaration>)node.parameters();
		System.out.println("Body: " + node.getBody().toString());
		body = node.getBody().toString();
		signature = new MethodSignature(name, visibility, lnNumber);
		signature.setParameterList(parameters);
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
