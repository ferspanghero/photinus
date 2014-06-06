package edu.uci.ics.sdcl.firefly;

import java.util.Map;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;


public class JavaParser {

	private ICompilationUnit unit = null;

	public JavaParser(StringBuffer SourcePath) {

		char[] source = SourcePath.toString().toCharArray(); // obtain an array of char from the source file = ...;
		ASTParser parser = ASTParser.newParser(AST.JLS3);  // handles JDK 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6
		parser.setSource(source);
		// In order to parse 1.5 code, some compiler options need to be set to 1.5
		Map options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_7, options);
		parser.setCompilerOptions(options);
		ICompilationUnit result = (ICompilationUnit) parser.createAST(null);

	}


	public void printICompilationUnitInfo()
			throws JavaModelException {
		if(this.unit!=null)
			printCompilationUnitDetails(this.unit);
		else
			System.err.println("No Compilation Unit to print");
	}

	private void printCompilationUnitDetails(ICompilationUnit unit)
			throws JavaModelException {
		System.out.println("Source file " + unit.getElementName());
		//  DocumentCommand doc = new DocumentCommand(unit.getSource());
		// System.out.println("Has number of lines: " + doc.getNumberOfLines());
		printIMethods();
	}

	private void  printIMethods() throws JavaModelException {
		IType[] allTypes = this.unit.getAllTypes();
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
}
