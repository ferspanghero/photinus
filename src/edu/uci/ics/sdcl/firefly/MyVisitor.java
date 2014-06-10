package edu.uci.ics.sdcl.firefly;
//package edu.uci.ics.sdcl.firefly;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class MyVisitor extends ASTVisitor {
	
	private CompilationUnit cu;
	
	public MyVisitor (CompilationUnit cuArg)
	{
		this.cu = cuArg;
	}
	
	public boolean visit(MethodDeclaration node)
	{
//		SimpleName methodName = node.getName();
//		this.names.add(methodName.getIdentifier());
		System.out.println("-----------");
		System.out.println("Method at line: " + cu.getLineNumber(node.getStartPosition()));
		System.out.println("Method name: " + node.getName());
		System.out.println("Return type: " + node.getReturnType2());
		System.out.println("Parameters: " + node.parameters());
		System.out.println("Body: " + node.getBody().toString());
		return true;
	}
	
	/*Statements */
	public boolean visit(IfStatement node)
	{
		System.out.println("If at line: " + cu.getLineNumber(node.getStartPosition()));	
		return true;
	}
	
	public boolean visit(WhileStatement node)
	{
		System.out.println("While at line: " + cu.getLineNumber(node.getStartPosition()));	
		return true;
	}
	
	public boolean visit(ForStatement node)
	{
		System.out.println("For at line: " + cu.getLineNumber(node.getStartPosition()));	
		return true;
	}
	
	public boolean visit(SwitchStatement node)
	{
		System.out.println("Switch at line: " + cu.getLineNumber(node.getStartPosition()));	
		return true;
	}
}
