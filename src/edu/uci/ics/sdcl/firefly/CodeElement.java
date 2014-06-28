package edu.uci.ics.sdcl.firefly;

public class CodeElement implements java.io.Serializable{

	public  static String FOR_LOOP = "FOR_LOOP"; 
	public  static String WHILE_LOOP = "WHILE_LOOP"; 
	public  static String DO_LOOP = "DO_LOOP";
	public  static String IF_CONDITIONAL = "IF_CONDITIONAL"; 
	public  static String SWITCH_CONDITIONAL = "SWITCH_CONDITIONAL"; 
	public  static String METHOD_DECLARARION = "METHOD_DECLARATION"; 
	public  static String METHOD_INVOCATION = "METHOD_INVOCATION"; 

	public  final String COMMENT_STATEMENT = "COMMENT_STATEMENT";

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