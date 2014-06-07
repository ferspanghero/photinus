package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;

public class MethodSignature {

	protected String Name;
	protected String Modifier;
	protected ArrayList<String> Parameters;
	protected Integer LineNumber;

	public MethodDeclaration(String name, String modifier,
			ArrayList<String> parameters, Integer lineNumber) {
		super();
		Name = name;
		Modifier = modifier;
		Parameters = parameters;
		LineNumber = lineNumber;
	}

	/**
	 * Compare the current method against a provided one.
	 * @return true is mehods are the same (name and parameters match), otherwise false
	 */
	public boolean compareMethods(MethodDeclaration method){
		//TO DO
		return false;
	}

}
