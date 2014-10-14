package edu.uci.ics.sdcl.firefly.lite;


public class MicrotaskLite {

	public int microtaskId;
	public String question; 
	public String failureDescription;
	public String snippetHightlights;
	public String callerHightlights;
	public String calleeHightlights;
	public int startingColumn;
	public int endingLine;
	public int endingColumn;
	public String calleeFileContent;
	public int calleeLOCS;
	public String callerFileContent;
	public int callerLOCS;

	public MicrotaskLite(int microtaskId, String question,
			String failureDescription, String snippetHightlights,
			String callerHightlights, String calleeHightlights,
			int startingColumn, int endingLine, int endingColumn,
			String calleeFileContent, int calleeLOCS, String callerFileContent,
			int callerLOCS) {
		
		this.microtaskId = microtaskId;
		this.question = question;
		this.failureDescription = failureDescription;
		this.snippetHightlights = snippetHightlights;
		this.callerHightlights = callerHightlights;
		this.calleeHightlights = calleeHightlights;
		this.startingColumn = startingColumn;
		this.endingLine = endingLine;
		this.endingColumn = endingColumn;
		this.calleeFileContent = calleeFileContent;
		this.calleeLOCS = calleeLOCS;
		this.callerFileContent = callerFileContent;
		this.callerLOCS = callerLOCS;
	}
	
	
	
}
