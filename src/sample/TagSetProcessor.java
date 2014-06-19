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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.sdcl.porchlight.antlr.BTLLexer;
import edu.uci.ics.sdcl.porchlight.antlr.BTLParser;
import edu.uci.ics.sdcl.porchlight.graph.BugGraph;
import edu.uci.ics.sdcl.porchlight.graph.Neo4jBugGraphImpl;
import edu.uci.ics.sdcl.porchlight.model.Bug;
import edu.uci.ics.sdcl.porchlight.model.BugTagSet;

public class TagSetProcessor {
	final static Logger logger = LoggerFactory.getLogger(TagSetProcessor.class);
	private BugGraph bugGraph = null;

	public TagSetProcessor(BugGraph bugGraph) {
		this.bugGraph = bugGraph;
	}

	public static void main(String[] args) {
		StopWatch watch = new StopWatch();

		BugGraph bugGraph = new Neo4jBugGraphImpl();
		bugGraph.connect(args[0]);
		TagSetProcessor tsp = new TagSetProcessor(bugGraph);

		boolean quit = false;
		String statement = "";
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

		do {
			try {
				System.out.print("tsp> ");
				statement = buffer.readLine();

				if (!"quit".equalsIgnoreCase(statement)) {
					try {
						watch.reset();
						watch.start();
						BugTagSet tagSet = tsp.evaluateStatement(statement);
						watch.stop();

						if (tagSet != null) {
							for (Bug bug : tagSet.getBugs()) {
								System.out.println("[" + bug.getKey() + "] " + bug.getSummary() + " (Assigned to " + bug.getAssignee() + ")");
							}

							System.out.println("Tag set '" + tagSet.getName() + "' returned with " + tagSet.getBugs().size() + " bugs.");
						}

						logger.info("Evaluation duration: " + watch.getTime() + " ms.");
					} catch (Exception e) {
						logger.error("Error evaluation statement: " + statement, e);
					}
				} else {
					quit = true;
				}

			} catch (IOException e) {
				logger.error("Error reading input.");
			}
		} while (!quit);

		logger.info("Exited due to user quit.");
		bugGraph.disconnect();
	}
	
	public BugTagSet evaluateStatement(String tag, String clauses) throws Exception {
		return evaluateStatement("TAG " + tag + " WHERE " + clauses);
	}

	public BugTagSet evaluateStatement(String statement) throws Exception {
		InputStream is = IOUtils.toInputStream(statement);

		ANTLRInputStream input = new ANTLRInputStream(is);
		BTLLexer lexer = new BTLLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		BTLParser parser = new BTLParser(tokens);
		CommonTree ast = (CommonTree) parser.statement().getTree();

		Evaluator evaluator = new Evaluator(bugGraph);
		BugTagSet tagSet = evaluator.evaluate(ast);

		return tagSet;
	}
}
