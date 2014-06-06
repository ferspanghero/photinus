/*
 * Copyright (c) 2012 Gerald Bortis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package edu.uci.ics.sdcl.porchlight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.tree.Tree;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.sdcl.porchlight.antlr.BTLParser;
import edu.uci.ics.sdcl.porchlight.function.Commented;
import edu.uci.ics.sdcl.porchlight.function.Commited;
import edu.uci.ics.sdcl.porchlight.function.Frequency;
import edu.uci.ics.sdcl.porchlight.function.Function;
import edu.uci.ics.sdcl.porchlight.function.Has;
import edu.uci.ics.sdcl.porchlight.graph.BugGraph;
import edu.uci.ics.sdcl.porchlight.model.Bug;
import edu.uci.ics.sdcl.porchlight.model.BugTagSet;

public class Evaluator {
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	private BugGraph bugGraph = null;
	private Map<String, Function> functions = new HashMap<String, Function>();

	public   Evaluator(BugGraph bugGraph) {
		this.bugGraph = bugGraph;
		initFunctions();
	}

	private void initFunctions() {
		functions.put("FREQUENCY", new Frequency());
		functions.put("HAS", new Has());
		functions.put("COMMITTED", new Commited());
		functions.put("COMMENTED", new Commented());
	}
	
	public BugTagSet evaluate(Tree ast) throws Exception {
		logger.info("Evaluating AST: " + ast.toStringTree());
		String tag = ast.getChild(0).getText();

		if (ast.getType() == BTLParser.TAG) {
			List<Bug> bugs = new ArrayList<Bug>();

			for (Bug bug : bugGraph.getBugs()) {
				Tree clause = ast.getChild(1);

				if (evaluateClause(clause, bug)) {
					bugs.add(bug);
				}
			}

			bugGraph.tagBugs(bugs, tag, ast.getChild(1).toStringTree());
			return bugGraph.getBugTagSet(tag);
		} else if (ast.getType() == BTLParser.SHOW) {
			return bugGraph.getBugTagSet(tag);
		} else if (ast.getType() == BTLParser.DELETE) {
			bugGraph.removeBugTagSet(tag);
		}

		return null;
	}

	public boolean evaluateClause(Tree ast, Bug bug) {
		switch (ast.getType()) {
		case BTLParser.BOOLEAN:
			if ("AND".equals(ast.getText())) {
				return evaluateClause(ast.getChild(0), bug) && evaluateClause(ast.getChild(1), bug);
			} else {
				return evaluateClause(ast.getChild(0), bug) || evaluateClause(ast.getChild(1), bug);
			}
		case BTLParser.RELATION:
			String relation = ast.getText();
			String variable = ast.getChild(0).getText();
			String value = ast.getChild(1).getText();

			// TODO: Use BeanUtils to access bean properties
			// http://commons.apache.org/beanutils/apidocs/org/apache/commons/beanutils/package-summary.html#standard.basic

			if ("key".equals(variable)) {
				return evaluatePredicate(relation, bug.getKey(), value);
			} else if ("assignee".equals(variable)) {
				return evaluatePredicate(relation, bug.getAssignee(), value);
			} else if ("summary".equals(variable)) {
				return evaluatePredicate(relation, bug.getSummary(), value);
			} else if ("description".equals(variable)) {
				return evaluatePredicate(relation, bug.getDescription(), value);
			} else if ("component".equals(variable)) {
				return true;
			} else if ("priority".equals(variable)) {
				return evaluatePredicate(relation, bug.getPriority(), value);
			} else if ("resolution".equals(variable)) {
				return true;
			} else if ("fixversion".equalsIgnoreCase(variable)) {
				return evaluatePredicate(relation, bug.getFixVersions(), value);
			} else if ("affectedversion".equalsIgnoreCase(value)) {
				return true;
			} else if ("reporter".equals(variable)) {
				return evaluatePredicate(relation, bug.getReporter(), value);
			} else if ("age".equals(variable)) {
				return true;
			}

			return false;
		case BTLParser.WINDOW:
			return evaluateWindow(Integer.valueOf(ast.getChild(0).getText()), ast.getChild(1).getText(), bug);
		case BTLParser.FUNCTION:
			String result = evaluateFunction(ast.getChild(1).getText(), ast.getChild(2).getText(), bug);
			return evaluatePredicate(ast.getChild(0).getText(), result, ast.getChild(3).getText());
		default:
			return false;
		}
	}

	private String evaluateFunction(String fn, String param, Bug bug) {
		Function function = functions.get(fn);

		if (function != null) {
			return function.evaluate(bugGraph, bug, param);
		} else {
			logger.error("Function '" + fn + "' could not be found.");
		}

		return null;
	}

	private boolean evaluatePredicate(String relation, String left, String right) {
		if (relation.equals("=")) {
			return StringUtils.equals(left, right);
		} else if (relation.equals(">")) {
			return left.compareTo(right) > 0;
		} else if (relation.equals("<")) {
			return left.compareTo(right) < 0;
		} else if (relation.equals("~")) {
			return left.contains(right);
		}

		return false;
	}

	private boolean evaluatePredicate(String relation, String[] left, String right) {
		if (relation.equals("=")) {
			return ArrayUtils.contains(left, right);
		}

		return false;
	}

	private boolean evaluateWindow(Integer value, String period, Bug bug) {
		Period window = null;

		if (StringUtils.equalsIgnoreCase(period, "DAYS")) {
			window = new Period().withDays(value);
		} else if (StringUtils.equalsIgnoreCase(period, "WEEKS")) {
			window = new Period().withWeeks(value);
		} else if (StringUtils.equalsIgnoreCase(period, "MONTHS")) {
			window = new Period().withMonths(value);
		} else if (StringUtils.equalsIgnoreCase(period, "YEARS")) {
			window = new Period().withYears(value);
		}

		DateTime lastUpdated = new DateTime(bug.getUpdated());
		DateTime nowMinusWindow = DateTime.now().minus(window);

		return lastUpdated.isAfter(nowMinusWindow);
	}
}
