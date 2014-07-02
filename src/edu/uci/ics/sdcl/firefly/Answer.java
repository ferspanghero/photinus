package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;

/** Represents an answer to a microtask */
public class Answer implements Serializable{

	public static String YES = "Yes";
	public static String PROBABLY_YES = "Probably yes";
	public static String I_CANNOT_TELL = "I cannot tell";
	public static String PROBABLY_NOT = "Probably not";
	public static String NO = "No";
	
	private String option;
	
	public Answer(String option){
		this.option=option;
	}
	
}
