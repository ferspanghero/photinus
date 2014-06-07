package edu.uci.ics.sdcl.firefly;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;


public class JavaParser {

	private CompilationUnit unit = null;
	private MethodVisitor methodVisitor = null;
	//private WhileVisitor whileVisitor= null;
	//private ForVisitor forVisitor = null;
	//private SwitchVisitor switchVisitor = null; 
	//private IfVisitor ifVisitor = null;

	public JavaParser(StringBuffer SourcePath) {

		char[] source = SourcePath.toString().toCharArray(); // obtain an array of char from the source file = ...;
		ASTParser parser = ASTParser.newParser(AST.JLS3);  // handles JDK 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6
		parser.setSource(source);
		// In order to parse 1.5 code, some compiler options need to be set to 1.5
		Map options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_7, options);
		parser.setCompilerOptions(options);
		
		this.unit  = (CompilationUnit) parser.createAST(null);
		
		//Add a Method Visitor
		this.methodVisitor = new MethodVisitor();
		this.unit.accept(methodVisitor);
		
	}


	public void printICompilationUnitInfo(){
		if(this.unit!=null){
			List<AbstractTypeDeclaration> list = this.unit.types();
			for(AbstractTypeDeclaration declaration: list){
			System.out.println("Name : "+ declaration.getName().toString());
		}
			}
		else
			System.err.println("No Compilation Unit to print");
	}
	
	public void printMethods(){
		
		for (MethodDeclaration method : methodVisitor.getMethods()) {
			System.out.println("-----------");
			System.out.print("Method name: " + method.getName());
			System.out.print(" Return type: " + method.getReturnType2());
			System.out.print(" Body: " + method.getBody().toString());
			System.out.print(" Line: " + method.getStartPosition());
			System.out.println("-----------");
		}
	}
	  
	
	/*
	private void printCompilationUnitDetails( CompilationUnit unit)
			throws JavaModelException {
		//System.out.println("Source file " + unit.getElementName());
		//DocumentCommand doc = new DocumentCommand(unit.getSource());
		// System.out.println("Has number of lines: " + doc.getNumberOfLines());
		printIMethods();
	}

	private void  printIMethods() throws JavaModelException {
		List allTypes = this.unit.types();
		for (IType type : allTypes) {
			printIMethodDetails(type);
		}
	}



	private void printIMethodDetails(IType type) throws JavaModelException {
		IMethod[] methods = type.getMethods();
		for (IMethod method : methods) {

			System.out.println("Method name " + method.getElementName());
			System.out.println("Signature " + method.getSignature());
			System.out.println("Return Type " + method.getReturnType());
			System.out.println("Source " + method.getSource());
			System.out.println("Compilation Unit Source " + method.getCompilationUnit().getSource());
		}

	}
	*/
}
