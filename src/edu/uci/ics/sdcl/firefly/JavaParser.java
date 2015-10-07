package edu.uci.ics.sdcl.firefly;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

/**
 * Parses one Java file at a time and produces a list of code snippets.
 * 
 * @author adrianoc
 *
 */
public class JavaParser {

	private CompilationUnit unit = null;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JavaParser(CodeSnippetFactory snippetFactory) {
		
		char[] source = snippetFactory.getFileContent().toCharArray(); // obtain an array of char from the source file = ...;
		ASTParser parser = ASTParser.newParser(AST.JLS4);  // handles JDK 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6
		parser.setSource(source);
		
		// In order to parse 1.5 code, some compiler options need to be set to 1.5
		Map options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_7, options);
		parser.setCompilerOptions(options);
		parser.setBindingsRecovery(true);
		
		this.unit  = (CompilationUnit) parser.createAST(null);
		
		this.unit.accept(new MyVisitor(this.unit, snippetFactory));
		List<Comment> commentsList = (List<Comment>)this.unit.getCommentList();
		for (Comment comment : commentsList) {
			comment.accept(new CommentVisitor(this.unit, snippetFactory.getFileContent().split("\n")));
		}
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
	
	  
}
