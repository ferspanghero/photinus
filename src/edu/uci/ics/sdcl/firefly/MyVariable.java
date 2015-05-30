package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;

public class MyVariable extends CodeElement implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String name;
	private String type;
	
	public MyVariable(String nameArg, 
			Integer elementStartingLineArg, Integer elementStartingColumnArg,
			Integer elementEndingLineArg, Integer elementEndingColumnArg)
	{
		super(CodeElement.VARIABLE_DECLARATION, elementStartingLineArg, elementStartingColumnArg,
				elementEndingLineArg, elementEndingColumnArg);
		name = nameArg;
	}

	public String getName()
	{
		return name;
	}

} 
