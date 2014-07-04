package edu.uci.ics.sdcl.firefly;

public class PositionFinder {
	private Integer startingLineNumber;
	private Integer startingColumnNumber;
	private Integer endingLineNumber;
	private Integer endingColumnNumber;
	private char openingBrace;
	private char closingBrace;
	private String fileInLines[];
	
	public PositionFinder(Integer startingLineNumberArg, Integer startingColumnNumberArg, 
			String fileInLinesArg[], char openingBraceArg, char closingBraceArg)
	{
		this.startingLineNumber = startingLineNumberArg;
		this.startingColumnNumber = startingColumnNumberArg;
		this.fileInLines = fileInLinesArg;
		this.openingBrace = openingBraceArg;
		this.closingBrace = closingBraceArg;
		
		setEndPosition();
	}
	
	public void setEndPosition()
	{
		Integer bracesTrack = 0;	// reference to find the end counting the braces
		int currentLine =  (this.startingLineNumber-1);	// because the vector starts at 0
		
		System.out.print("---> Checking if this is the right starting line: " + this.startingLineNumber + "-");
		System.out.println(this.fileInLines[currentLine]);
		System.out.print("---> Checking if this is the right starting column: " + this.startingColumnNumber + "-");
		System.out.println(this.fileInLines[currentLine].charAt(this.startingColumnNumber));
		
		/* Flags */
		boolean openingBraceFound = false;		// assuming the OPEN bracket was not yet found
		boolean semiColonFound = false;			// assuming end of an instruction was not yet found
		boolean firstIterationFlag = true;		// to not reset the column
		
		this.endingColumnNumber = this.startingColumnNumber; // setting the start position of the column  
		do {	// looping lines to find the ending line
			if (!firstIterationFlag) this.endingColumnNumber = 0;	// do not reset if it is first iteration
			for (; this.endingColumnNumber < this.fileInLines[currentLine].length(); 
					this.endingColumnNumber++) // looping chars to find braces 
			{
				if ( this.openingBrace == this.fileInLines[currentLine].charAt(this.endingColumnNumber) )
				{
					if (!openingBraceFound)
					{	// first opening brace occurrence 
						this.startingLineNumber = currentLine +1;	// to remain consistent (Switch Statement)
						this.startingColumnNumber = this.endingColumnNumber; 
					}
					System.out.println(this.openingBrace + " found at " + this.fileInLines[currentLine]);
					bracesTrack++;
					openingBraceFound = true;
				} else if (this.closingBrace == this.fileInLines[currentLine].charAt(this.endingColumnNumber))
					bracesTrack--;
				else if (';' == this.fileInLines[currentLine].charAt(this.endingColumnNumber))
					semiColonFound = true;
				
				if( openingBraceFound && (0 >= bracesTrack) ) break;	// found the end already
				if( !openingBraceFound && semiColonFound) 
				{
					System.out.println("Found body with one single instruction!");
					break;		// body has one line w/o braces
				}
			}
			currentLine++;
			firstIterationFlag = false;
			System.out.println("Brace track value: " + bracesTrack);
		}
		// First OR: continue if braces did not close or if did not find the opening brace
		// Second AND: for the case of one single instruction without braces
		while ( (0 < bracesTrack || (!openingBraceFound) ) && !( !openingBraceFound && semiColonFound ) );
		
		this.endingLineNumber = currentLine;
		this.endingColumnNumber++; // to set the highlight until the last character
		
		System.out.print("---> Checking if this is the right ending line: " + this.endingLineNumber + "-");
		System.out.println(this.fileInLines[currentLine-1]);
		System.out.print("---> Checking if this is the right ending column: " + this.endingColumnNumber + "-");
		System.out.println(this.fileInLines[currentLine-1].charAt(this.endingColumnNumber-1));
		System.out.println("------------");
	}
	
	public Integer getStartingLineNumber() {
		return this.startingLineNumber;
	}
	
	public Integer getStartingColumnNumber() {
		return this.startingColumnNumber;
	}

	public Integer getEndingLineNumber() {
		return this.endingLineNumber;
	}
	
	public Integer getEndingColumnNumber() {
		return this.endingColumnNumber;
	}
	
}
