package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;

public class MyIfStatement extends CodeElement implements Serializable {
	public final static Integer NO_ELSE_STATEMENT = -1;
	public final static Integer DOES_NOT_BELONG_TO_ANY_IF = -2;
	private boolean thereIsElse;
	private boolean thereIsElseIf;
	private boolean isIfOfAnElse;
//	private Integer belongsToIfAt;
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
		this.setIfOfAnElse(false);
		this.setThereIsElseIf(false);
//		this.belongsToIfAt = MyIfStatement.DOES_NOT_BELONG_TO_ANY_IF;
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
		this.setIfOfAnElse(false);
		this.thereIsElse = true;
//		this.belongsToIfAt = MyIfStatement.DOES_NOT_BELONG_TO_ANY_IF;
		this.elseStartingLine = elseStartingLineArg;
		this.elseStartingColumn = elseStartingColumnArg;
		this.elseEndingLine = elseEndingLineArg;
		this.elseEndingColumn = elseEndingColumnArg;
		if ( (elseEndingLineArg == CodeElement.NO_NUMBER_ASSOCIATED) 
				&& (elseEndingColumnArg == CodeElement.NO_NUMBER_ASSOCIATED) )
			this.setThereIsElseIf(true);
		else
			this.setThereIsElseIf(false);
		
	}
	
//	/* IF of ELSE-IF case */
//	/* Construct without else statement */
//	public MyIfStatement(Integer ifFatherLineNumber,
//			Integer elementStartingLineArg, Integer elementStartingColumnArg, 
//			Integer elementEndingLineArg, Integer elementEndingColumnArg,
//			Integer bodyStartingLineArg, Integer bodyStartingColumnArg,
//			Integer bodyEndingLineArg, Integer bodyEndingColumnArg)
//	{
//		super(CodeElement.IF_CONDITIONAL, elementStartingLineArg, elementStartingColumnArg,
//				elementEndingLineArg, elementEndingColumnArg,
//				bodyStartingLineArg, bodyStartingColumnArg,
//				bodyEndingLineArg, bodyEndingColumnArg);
//		this.isElseIfCase = true;
//		this.belongsToIfAt = ifFatherLineNumber;
//		this.thereIsElse = false;
//		this.elseStartingLine = MyIfStatement.NO_ELSE_STATEMENT;
//		this.elseStartingColumn = MyIfStatement.NO_ELSE_STATEMENT;
//		this.elseEndingLine = MyIfStatement.NO_ELSE_STATEMENT;
//		this.elseEndingColumn = MyIfStatement.NO_ELSE_STATEMENT;
//	}
//	
//	/* Construct with else statement */
//	public MyIfStatement(Integer ifFatherLineNumber,
//			Integer elementStartingLineArg, Integer elementStartingColumnArg, 
//			Integer elementEndingLineArg, Integer elementEndingColumnArg,
//			Integer bodyStartingLineArg, Integer bodyStartingColumnArg,
//			Integer bodyEndingLineArg, Integer bodyEndingColumnArg,
//			Integer elseStartingLineArg, Integer elseStartingColumnArg, 
//			Integer elseEndingLineArg, Integer elseEndingColumnArg)
//	{
//		super(CodeElement.IF_CONDITIONAL, elementStartingLineArg, elementStartingColumnArg,
//				elementEndingLineArg, elementEndingColumnArg,
//				bodyStartingLineArg, bodyStartingColumnArg,
//				bodyEndingLineArg, bodyStartingColumnArg); 
//		this.isElseIfCase = true;
//		this.belongsToIfAt = ifFatherLineNumber;
//		this.thereIsElse = true;
//		this.elseStartingLine = elseStartingLineArg;
//		this.elseStartingColumn = elseStartingColumnArg;
//		this.elseEndingLine = elseEndingLineArg;
//		this.elseEndingColumn = elseEndingColumnArg;
//	}
	
	public static boolean isIfofAnElseIfCase(String[] fileContentInLines, Integer ifLineNumber)
	{
		boolean flagThereAreWords = false;
//		System.out.println("=================Trying to find the lost Else(if)================");
		for (int l = ifLineNumber; l > -1; l--) 
		{	// going backwards on the line number
			while ( fileContentInLines[l].startsWith("\t") ) {	// taking off tabs
				fileContentInLines[l] = fileContentInLines[l].substring(1, fileContentInLines[l].length());
			}
//			System.out.println("On line " + l + ": " + fileContentInLines[l]);
			String[] words = fileContentInLines[l].split(" ");
//			System.out.print("words: ");
			for (int i = 0; i < words.length; i++) {	// looking for 'else' word
//				System.out.print(words[i] + "--");
				if (null != words[i])
				{
					flagThereAreWords = true;
					if ( words[i].equals("else") )
					{	// Found an else-word
//						System.out.println("Found an Else!");
						if ( (words.length-1) == i)	// is the last word on this line
						{
//							System.out.println("Else is the last word");
							// verifying "if" as 1st word on the next line
							int j = 1;	// just to make sure the line is not empty
							while ( fileContentInLines[l+j].isEmpty() ) j++;
							String wordsNextLine[] = fileContentInLines[l+j].split(" ");
//							System.out.println("Line after else: " + fileContentInLines[l+j]);
							int k = 0; // just to make sure the first word is not a null char
							while ( null == wordsNextLine[k] ) k++; 
							if (wordsNextLine[k].equals("if"))
								return true;	// if after else! Else-if case
							else
								return false;	// something else, not an Else-if case
						}
						else 
						{
//							System.out.println("Else is NOT the last word");
							int p = 1;	// just to make sure the next word is not a null char
							while ( words[i+p].isEmpty() ) p++;
							if ( words[i+p].equals("if") )
								return true;		// if after else! Else-if case
							else
								return false;		// something else, not an Else-if case
						}
							
					}
				}
				else
					flagThereAreWords = false;
			}
//			System.out.println(); // another line will begin
			if (l<ifLineNumber && flagThereAreWords) break;	// no else found,  no point in continue 
		}
		return false;
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
	public void setElseEndingLine(Integer elseEndingLine) {
		this.elseEndingLine = elseEndingLine;
	}
	public void setElseEndingColumn(Integer elseEndingColumn) {
		this.elseEndingColumn = elseEndingColumn;
	}

	public boolean thereIsElseIf() {
		return thereIsElseIf;
	}

	public void setThereIsElseIf(boolean thereIsElseIf) {
//		System.out.println("Setting to " + thereIsElseIf);
		this.thereIsElseIf = thereIsElseIf;
	}

	public boolean isIfOfAnElse() {
		return isIfOfAnElse;
	}

	public void setIfOfAnElse(boolean isIfOfAnElse) {
		this.isIfOfAnElse = isIfOfAnElse;
	}
}
