package edu.uci.ics.sdcl.firefly;
import java.util.ArrayList;

public class CodeSnippet
{
	protected String uniqueName; 			// path + name of method
	protected String visibilityType;		// public, protected or private
	protected String implementationType; 	// concrete or abstract
	protected String methodBody;			// whole content of method
	protected String methodInterface;		// method declaration
	protected Boolean returnStatment;		// true if there is a return value
	protected MethodSignature methodSignature;	// parsed method declaration
	protected ArrayList<CodeElement> statments;		// flow control statments
	
	
	public CodeSnippet(String uniqueName, String visibilityType, String implementationType, 
			String methodBody, String methodInterface, Boolean returnStatment, 
			MethodSignature methodSignature, ArrayList<CodeElement> statments)
	{
		this.uniqueName = uniqueName;
		this.visibilityType = visibilityType;
		this.implementationType = implementationType;
		this.methodBody = methodBody;
		this.methodInterface = methodInterface;
		this.returnStatment = returnStatment;
		this.methodSignature = methodSignature;
		this.statments = statments;
	}
	
	@Override
	public String toString() {
		return "CodeSnippet [uniqueName=" + uniqueName + ", visibilityType="
				+ visibilityType + ", implementationType=" + implementationType
				+ ", methodBody=" + methodBody + ", methodInterface="
				+ methodInterface + ", returnStatment=" + returnStatment
				+ ", methodDeclaration=" + methodSignature + ", statments="
				+ statments + "]";
	}

	public void print()
	{
		//TO DO
	}
	
}