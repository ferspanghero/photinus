package edu.uci.ics.sdcl.firefly;

public class MyIfStatement extends CodeElement{
	public final static Integer NO_ELSE_STATEMENT = -1;
	private Integer elseStartingLine;
	
	public MyIfStatement(Integer statementLine, Integer bodyStartingAt, Integer elseStartingAt)
	{
		super(CodeElement.IF_CONDITIONAL, statementLine, (NO_ELSE_STATEMENT == elseStartingAt) ? bodyStartingAt : elseStartingAt); 
		this.setElseStartingLine(elseStartingAt);
		this.setBodyStartPosition(bodyStartingAt); // changing the line to the real beginning (then)
	}

	public Integer getElseStartingLine() {
		return elseStartingLine;
	}

	public void setElseStartingLine(Integer elseStartingLine) {
		this.elseStartingLine = elseStartingLine;
	}
	
	

}
