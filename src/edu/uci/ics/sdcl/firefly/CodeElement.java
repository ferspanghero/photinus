package edu.uci.ics.sdcl.firefly;

public class CodeElement {

	public final static String FOR_LOOP = "FOR_LOOP"; 
	public final static String WHILE_LOOP = "WHILE_LOOP"; 
	public final static String DO_LOOP = "DO_LOOP";
	public final static String IF_CONDITIONAL = "IF_CONDITIONAL"; 
	public final static String SWITCH_CONDITIONAL = "SWITCH_CONDITIONAL"; 
	public final static String METHOD_CALL = "METHOD_CALL"; 
	public final static String METHOD_PARAMETERS = "METHOD_PARAMETERS"; 
	public final static String METHOD_NAME = "METHOD_NAME";
	public static final String RETURN_STATEMENT = "RETURN_STATEMENT";
	public static final String COMMENT_STATEMENT = "COMMENT_STATEMENT";

	private String Type;
	private Integer startPosition;
	private Integer endPosition;
	
	public CodeElement(String Type, Integer LineNumber){
		this.Type = Type;
		this.startPosition = LineNumber;
		this.endPosition = LineNumber;
	}
	
	public CodeElement(String Type, Integer LineNumber, Integer endPositionArg){
		this.Type = Type;
		this.startPosition = LineNumber;
		this.endPosition = endPositionArg;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}
	
	@Override
	public String toString()
	{
		return this.getType() + " @ line " + this.startPosition.toString();
	}

	public Integer getStartPosition()
	{
		return startPosition;
	}

	public void setStartPosition(Integer startPosition)
	{
		this.startPosition = startPosition;
	}

	public Integer getEndPosition()
	{
		return endPosition;
	}

	public void setEndPosition(Integer endPosition)
	{
		this.endPosition = endPosition;
	}
}