package edu.uci.ics.sdcl.firefly;
import java.io.Serializable;
import java.util.ArrayList;

public class CodeSnippet implements Serializable
{
	protected String packageName;					// package name
	protected String className; 					// file
//	protected String methodName; 					// name of method
//	protected String implementationType; 			// concrete or abstract
	protected String methodBody;					// whole content of method
	protected Integer bodyStartsAt;					// line where body starts
	protected Integer bodyEndsAt;					// line where body ends
	protected Integer elseStartingLine;				// else startingLine case if statement
	protected Integer bodyStartingColumn;			// column where body starts
	protected Integer bodyEndingColumn;				// column where body ends
	protected Boolean returnStatement;				// true if there is a return value
	protected MethodSignature methodSignature;		// parsed method declaration
	protected ArrayList<CodeElement> statements;	// list of statements
	protected ArrayList<MethodSignature> methodCalss;	// list of method calls
	private final static String newline = System.getProperty("line.separator");	// Just to jump line @toString
	
	public CodeSnippet(String packageName, String className, 
			String methodBody, Integer bodyStartingLine, Boolean returnStatement, 
			MethodSignature methodSignature)
	{
		this.packageName = packageName;
		this.className = className;
		this.bodyStartsAt = bodyStartingLine;
		this.bodyStartingColumn = 0;
		this.bodyEndingColumn = 2000;
//		this.methodName = methodName;
//		this.implementationType = implementationType;
		this.methodBody = methodBody;
		this.returnStatement = returnStatement;
		this.methodSignature = methodSignature;
		if (null != this.methodBody)
		{
			String contentPerLines[] = CodeSnippetFactory.getFileContentePerLine();
			Integer curlyBracesTrack = 0;	// reference to find the end of the body
			int currentLine = (this.bodyStartsAt -1);
			do {	// looping lines to find the end of the body
				for (int i=0; i < contentPerLines[currentLine].length(); i++) // looping chars to find brackets 
				{
					switch ( contentPerLines[currentLine].charAt(i) ) {
					case '{':
//						System.out.println("FOUND A BRACKET!");
						curlyBracesTrack++;
						break;
					case '}':
						curlyBracesTrack--;
						break;	
					}
				}
				currentLine++;
			}
			while (0 < curlyBracesTrack);
			
			this.bodyEndsAt = currentLine;
			this.bodyEndingColumn = contentPerLines[currentLine-1].length() + 2; // to highlight the last char
		}
		else
			this.bodyEndsAt = this.bodyStartsAt;
		this.statements = new ArrayList<CodeElement>();
		this.methodCalss = new ArrayList<MethodSignature>();
	}

	@Override
	public String toString() {
		return "CodeSnippet" 
				+ newline + "package name: " + packageName 
				+ newline + "className: " + className 
				+ newline + "methodSignature: " + methodSignature 
				+ newline + "returnStatment: " + returnStatement 
				+ newline + "methodBody: " + newline + methodBody
				+ "statements = "	+ statements;
	}

	
	public void addElement(CodeElement element){
		this.statements.add(element);
	}

	public boolean isEqualTo(CodeSnippet targetSnippet) {
		MethodSignature targetMethod = targetSnippet.getMethodSignature();
		return this.methodSignature.isEqualTo(targetMethod);
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

//	public String getMethodName() {
//		return methodName;
//	}
//
//	public void setMethodName(String methodName) {
//		this.methodName = methodName;
//	}

//	public String getImplementationType() {
//		return implementationType;
//	}
//
//	public void setImplementationType(String implementationType) {
//		this.implementationType = implementationType;
//	}

	public String getMethodBody() {
		return methodBody;
	}

	public void setMethodBody(String methodBody) {
		this.methodBody = methodBody;
	}

	public Boolean getReturnStatement() {
		return returnStatement;
	}

	public void setReturnStatement(Boolean returnStatement) {
		this.returnStatement = returnStatement;
	}

	public MethodSignature getMethodSignature() {
		return methodSignature;
	}

	public void setMethodSignature(MethodSignature methodSignature) {
		this.methodSignature = methodSignature;
	}

	public ArrayList<CodeElement> getStatements() {
		return statements;
	}

	public void setStatements(ArrayList<CodeElement> statements) {
		this.statements = statements;
	}
	public Integer getBodyStartsAt() {
		return bodyStartsAt;
	}

	public void setBodyStartsAt(Integer bodyStartsAt) {
		this.bodyStartsAt = bodyStartsAt;
	}
	public Integer getBodyEndsAt() {
		return bodyEndsAt;
	}

	public void setBodyEndsAt(Integer bodyEndsAt) {
		this.bodyEndsAt = bodyEndsAt;
	}
	public Integer getBodyStartingColumn() {
		return bodyStartingColumn;
	}

	public void setBodyStartingColumn(Integer bodyStartingColumn) {
		this.bodyStartingColumn = bodyStartingColumn;
	}
	public Integer getBodyEndingColumn() {
		return bodyEndingColumn;
	}

	public void setBodyEndingColumn(Integer bodyEndingColumn) {
		this.bodyEndingColumn = bodyEndingColumn;
	}

}