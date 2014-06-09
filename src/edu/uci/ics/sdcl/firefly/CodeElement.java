package edu.uci.ics.sdcl.firefly;

public class CodeElement {

	public final static String FOR_LOOP = "FOR_LOOP"; 
	public final static String WHILE_LOOP = "WHILE_LOOP"; 
	public final static String IF_CONDITIONAL = "IF_CONDITIONAL"; 
	public final static String SWITCH_CONDITIONAL = "SWITCH_CONDITIONAL"; 
	public final static String METHOD_CALL = "METHOD_CALL"; 
	public final static String METHOD_PARAMETERS = "METHOD_PARAMETERS"; 
	public final static String METHOD_NAME = "METHOD_NAME";
	public static final String RETURN_STATEMENT = "RETURN_STATEMENT";

	private String Type;
	private Integer LineNumber;
	
	public CodeElement(String Type, Integer LineNumber){
		this.Type = Type;
		this.LineNumber = LineNumber;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public Integer getLineNumber() {
		return LineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		LineNumber = lineNumber;
	}

	
	
}
