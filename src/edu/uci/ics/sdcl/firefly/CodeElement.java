package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;

public class CodeElement implements Serializable {

	public final static String FOR_LOOP = "FOR_LOOP"; 
	public final static String WHILE_LOOP = "WHILE_LOOP"; 
	public final static String DO_LOOP = "DO_LOOP";
	public final static String IF_CONDITIONAL = "IF_CONDITIONAL"; 
	public final static String SWITCH_CONDITIONAL = "SWITCH_CONDITIONAL"; 
//	public final static String METHOD_CALL = "METHOD_CALL"; 
	public final static String METHOD_DECLARARION = "METHOD_DECLARATION"; 
	public final static String METHOD_INVOCATION = "METHOD_INVOCATION"; 
//	public final static String METHOD_PARAMETERS = "METHOD_PARAMETERS"; 
//	public final static String METHOD_NAME = "METHOD_NAME";
//	public static final String RETURN_STATEMENT = "RETURN_STATEMENT";
	public static final String COMMENT_STATEMENT = "COMMENT_STATEMENT";

	private String Type;
	private Integer elementStartPosition;
	private Integer bodyStartPosition;
	private Integer bodyEndPosition;
	private Integer columnStart;
	private Integer columnEnd;
	
	/* constructor for elements without body */
	public CodeElement(String Type, Integer statementLine){
		this.Type = Type;
		this.elementStartPosition = statementLine;
		this.bodyStartPosition = statementLine;
		this.bodyStartPosition = statementLine;
		this.columnStart = 0;
		this.columnEnd = 2000;
	}
	/* constructor for elements with body */
	public CodeElement(String Type, Integer statementLine, Integer bodyStartingAt){
		this.Type = Type;
		this.elementStartPosition = statementLine;
		this.bodyStartPosition = bodyStartingAt;
		this.columnStart = 0;
		this.columnEnd = 2000;
		/* finding the end of the body */
		String contentPerLines[] = CodeSnippetFactory.getFileContentePerLine();
		Integer curlyBracesTrack = 0;	// reference to find the end of the body
		int currentLine = (this.bodyStartPosition-1);
		boolean openBracketNotFound = true;				// assuming the OPEN bracket was not yet found
		do {	// looping lines to find and of body
			for (int i=0; i < contentPerLines[currentLine].length(); i++) // looping chars to find brackets 
			{
				switch ( contentPerLines[currentLine].charAt(i) ) {
				case '{':
					curlyBracesTrack++;
					openBracketNotFound = false;
					if (SWITCH_CONDITIONAL == this.Type) this.columnStart = currentLine++;
					break;
				case '}':
					curlyBracesTrack--;
					break;	
				}
			}
			currentLine++;
		}
		while ( (0 < curlyBracesTrack) || (openBracketNotFound && (SWITCH_CONDITIONAL == this.Type)) );
		
		this.bodyEndPosition = currentLine;
		this.columnEnd = contentPerLines[currentLine-1].length() + 2; // to highlight the last char
		System.out.println("body ends at: " + contentPerLines[currentLine-1]);
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
		return this.getType() + " @ line " + this.bodyStartPosition.toString();
	}

	public Integer getBodyStartPosition()
	{
		return bodyStartPosition;
	}

	public void setBodyStartPosition(Integer startPosition)
	{
		this.bodyStartPosition = startPosition;
	}

	public Integer getBodyEndPosition()
	{
		return bodyEndPosition;
	}

	public void setBodyEndPosition(Integer endPosition)
	{
		this.bodyEndPosition = endPosition;
	}

	public Integer getElementStartPosition() {
		return elementStartPosition;
	}

	public void setElementStartPosition(Integer elementStartPosition) {
		this.elementStartPosition = elementStartPosition;
	}
	public Integer getColumnStart() {
		return columnStart;
	}
	public void setColumnStart(Integer columnStart) {
		this.columnStart = columnStart;
	}
	public Integer getColumnEnd() {
		return columnEnd;
	}
	public void setColumnEnd(Integer columnEnd) {
		this.columnEnd = columnEnd;
	}
}