package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyMethodCall extends CodeElement implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String expression;
	private String parameters;
	private Integer numberOfParameters;
	// handles the nested methods cases methodA().methodB() or methodA(methodB())
	private List<String> nestedMethods = new ArrayList<String>(); 
	
	public MyMethodCall(String nameArg, String expressionArg, String parametersArg, Integer numberOfParameters, 
			Integer elementStartingLineArg, Integer elementStartingColumnArg,
			Integer elementEndingLineArg, Integer elementEndingColumnArg)
	{
		super(CodeElement.METHOD_INVOCATION, elementStartingLineArg, elementStartingColumnArg,
				elementEndingLineArg, elementEndingColumnArg);
		name = nameArg;
		expression = expressionArg;
		parameters = parametersArg;
		this.numberOfParameters = numberOfParameters;
	}
	
	public String getName()
	{
		return name;
	}

	public String getExpression()
	{
		return expression;
	}

	public String getParameters()
	{
		return parameters;
	}

	public Integer getNumberOfParameters(){
		return this.numberOfParameters;
	}
	
	public void setNestedMethods(List<String> nestedMethods)
	{
		this.nestedMethods = nestedMethods;
	}
	
	public List<String> getNestedMethods()
	{
		return this.nestedMethods;
	}
	
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("Method name: " + name+" at line "+elementStartingLine + " expression: " + expression + "." + name + 
				this.parameters.replace('[', '(').replace(']', ')'));
		buffer.append("\n");
		buffer.append("Method invocation at starting line: " + elementStartingLine + ", starting column: "+ elementStartingColumn);
		buffer.append("\n");
		buffer.append("Method invocation at ending line : " + elementEndingLine + ", ending column: "+ elementEndingColumn);
		buffer.append("\n");
		return buffer.toString();
	}

} // test
