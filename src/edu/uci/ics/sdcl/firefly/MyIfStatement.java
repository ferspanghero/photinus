package edu.uci.ics.sdcl.firefly;

public class MyIfStatement extends CodeElement{
	public final static Integer NO_ELSE_STATEMENT = -1;
	private boolean thereIsElse;
	private Integer elseStartingLine;
	private Integer elseStartingColumn;
	private Integer elseEndingLine;
	private Integer elseEndingColumn;
	
	/* Construct without else statement */
	public MyIfStatement(Integer elementStartingLineArg, Integer elementStartingColumnArg, 
			Integer elementEndingLineArg, Integer elementEndingColumnArg,
			Integer bodyStartingLineArg, Integer bodyStartingColumnArg,
			Integer bodyEndingLineArg, Integer bodyEndingColumnArg)
	{
		super(CodeElement.IF_CONDITIONAL, elementStartingLineArg, elementStartingColumnArg,
				elementEndingLineArg, elementEndingColumnArg,
				bodyStartingLineArg, bodyStartingColumnArg,
				bodyEndingLineArg, bodyEndingColumnArg);
		this.thereIsElse = false;
		this.elseStartingLine = MyIfStatement.NO_ELSE_STATEMENT;
		this.elseStartingColumn = MyIfStatement.NO_ELSE_STATEMENT;
		this.elseEndingLine = MyIfStatement.NO_ELSE_STATEMENT;
		this.elseEndingColumn = MyIfStatement.NO_ELSE_STATEMENT;
	}
	
	/* Construct with else statement */
	public MyIfStatement(Integer elementStartingLineArg, Integer elementStartingColumnArg, 
			Integer elementEndingLineArg, Integer elementEndingColumnArg,
			Integer bodyStartingLineArg, Integer bodyStartingColumnArg,
			Integer bodyEndingLineArg, Integer bodyEndingColumnArg,
			Integer elseStartingLineArg, Integer elseStartingColumnArg, 
			Integer elseEndingLineArg, Integer elseEndingColumnArg)
	{
		super(CodeElement.IF_CONDITIONAL, elementStartingLineArg, elementStartingColumnArg,
				elementEndingLineArg, elementEndingColumnArg,
				bodyStartingLineArg, bodyStartingColumnArg,
				bodyEndingLineArg, bodyStartingColumnArg); 
		this.thereIsElse = true;
		this.elseStartingLine = elseStartingLineArg;
		this.elseStartingColumn = elseStartingColumnArg;
		this.elseEndingLine = elseEndingLineArg;
		this.elseEndingColumn = elseEndingColumnArg;
	}

	public boolean isThereIsElse() {
		return thereIsElse;
	}
	/* getting positions */
	public Integer getElseStartingLine() {
		return elseStartingLine;
	}

	public Integer getElseStartingColumn() {
		return elseStartingColumn;
	}

	public Integer getElseEndingLine() {
		return elseEndingLine;
	}

	public Integer getElseEndingColumn() {
		return elseEndingColumn;
	}
	/* setting positions */
	public void setElseStartingLine(Integer elseStartingLine) {
		this.elseStartingLine = elseStartingLine;
	}
}
