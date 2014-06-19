package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IfStatement;

public class IfVisitor extends ASTVisitor {

	List<IfStatement> statements = new ArrayList<IfStatement>();

	@Override
	public boolean visit(IfStatement node) {
		statements.add(node);
		System.out.println("Visiting Ifs");
		return super.visit(node);
	}

	public List<IfStatement> getStatements() {
		return statements;
	}
}
