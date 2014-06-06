package edu.uci.ics.sdcl.firefly;

import java.util.Map;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;


public class JavaParser {
	
	public JavaParser(StringBuffer SourcePath) {

		char[] source = SourcePath.toString().toCharArray(); // obtain an array of char from the source file = ...;
		ASTParser parser = ASTParser.newParser(AST.JLS3);  // handles JDK 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6
		parser.setSource(source);
		// In order to parse 1.5 code, some compiler options need to be set to 1.5
		Map options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_7, options);
		parser.setCompilerOptions(options);
		CompilationUnit result = (CompilationUnit) parser.createAST(null);

	}
}
