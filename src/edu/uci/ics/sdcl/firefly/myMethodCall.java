package edu.uci.ics.sdcl.firefly;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;

public class myMethodCall extends CodeElement
{
	private String name;
	private String expression;
	private List<Expression> parameterList;
	
	public myMethodCall(String nameArg, String expressionArg, List<Expression> parametersArg, Integer LineNumber)
	{
		super(CodeElement.METHOD_CALL, LineNumber);
		this.setName(nameArg);
		this.setExpression(expressionArg);
		this.setParameterList(parametersArg);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getExpression()
	{
		return expression;
	}

	public void setExpression(String expression)
	{
		this.expression = expression;
	}

	public List<Expression> getParameterList()
	{
		return parameterList;
	}

	public void setParameterList(List<Expression> parametersArg)
	{
		this.parameterList = parametersArg;
	}

}
