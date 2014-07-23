package edu.uci.ics.sdcl.firefly;
import java.io.Serializable;
import java.util.ArrayList;

public class CodeSnippet implements Serializable
{
	protected String packageName;					// package name
	protected String className; 					// file
	protected ArrayList<CodeSnippet> callers;		// all callers of this method within the file
	protected ArrayList<CodeSnippet> callees;		// all callees of this method within the file
	
	protected String methodBody;					// whole content of method
	/* finding starting and ending position */
	/* Element position */
	protected Integer elementStartingLine;	// line number for the element beginning (not the body)
	protected Integer elementStartingColumn;// column number for the element beginning
	protected Integer elementEndingLine;	// line number for the element ending
	protected Integer elementEndingColumn;	// column number for the element ending
	/* Body position */
	protected Integer bodyStartingLine;		// line number for the beginning of body
	protected Integer bodyStartingColumn;	// column number for the body (of element in case of method invocation)
	protected Integer bodyEndingLine;		// line number for the end of the body
	protected Integer bodyEndingColumn;		// column number for the end of the body
	
	protected Boolean returnStatement;				// true if there is a return value
	protected MethodSignature methodSignature;		// parsed method declaration
	protected ArrayList<CodeElement> statements;	// list of statements
	
	protected String codeSnippetFromFileContent;	// the string that has the whole body of the method

	private final static String newline = System.getProperty("line.separator");	// Just to jump line @toString
	
	/* constructor for methods without body */
	public CodeSnippet(String packageName, String className, MethodSignature methodSignature, 
			String methodBody, Boolean returnStatement,
			Integer elementStartingLineArg, Integer elementStartingColumnArg, 
			Integer elementEndingLineArg, Integer elementEndingColumnArg)
	{
		this.packageName = packageName;
		this.className = className;
		this.methodSignature = methodSignature;
		this.methodBody = methodBody;
		this.returnStatement = returnStatement;
		/* setting element position */
		this.elementStartingLine = elementStartingLineArg;
		this.elementStartingColumn = elementStartingColumnArg;
		this.elementEndingLine = elementEndingLineArg;
		this.elementEndingColumn = elementEndingColumnArg;
		/* setting body position */
		this.bodyStartingLine = CodeElement.NO_NUMBER_ASSOCIATED;		
		this.bodyStartingColumn = CodeElement.NO_NUMBER_ASSOCIATED;	
		this.bodyEndingLine = CodeElement.NO_NUMBER_ASSOCIATED;		
		this.bodyEndingColumn = CodeElement.NO_NUMBER_ASSOCIATED;
		
		this.statements = new ArrayList<CodeElement>();
		this.callers = new ArrayList<CodeSnippet>();
		this.callees = new ArrayList<CodeSnippet>();
	}
	
	/* constructor for methods with body */
	public CodeSnippet(String packageName, String className, MethodSignature methodSignature, 
			String methodBody, Boolean returnStatement, 
			Integer elementStartingLineArg, Integer elementStartingColumnArg, 
			Integer elementEndingLineArg, Integer elementEndingColumnArg,
			Integer bodyStartingLineArg, Integer bodyStartingColumnArg,
			Integer bodyEndingLineArg, Integer bodyEndingColumnArg)
	{
		this.packageName = packageName;
		this.className = className;
		this.methodSignature = methodSignature;
		this.methodBody = methodBody;
		this.returnStatement = returnStatement;
		/* setting element position */
		this.elementStartingLine = elementStartingLineArg;
		this.elementStartingColumn = elementStartingColumnArg;
		this.elementEndingLine = elementEndingLineArg;
		this.elementEndingColumn = elementEndingColumnArg;
		/* setting body position */
		this.bodyStartingLine = bodyStartingLineArg;		
		this.bodyStartingColumn = bodyStartingColumnArg;	
		this.bodyEndingLine = bodyEndingLineArg;		
		this.bodyEndingColumn = bodyEndingColumnArg;	
		
		this.statements = new ArrayList<CodeElement>();
		this.callers = new ArrayList<CodeSnippet>();
		this.callees = new ArrayList<CodeSnippet>();
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
	
	public void addCaller(CodeSnippet callerSnippet)
	{
		this.callers.add(callerSnippet);
	}
	
	public void addCallee(CodeSnippet calleeSnippet)
	{
		this.callees.add(calleeSnippet);
	}
	
	public ArrayList<CodeSnippet> getCallers()
	{
		return this.callers;
	}
	
	public ArrayList<CodeSnippet> getCallees()
	{
		return this.callees;
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
	/* Getters for the position */
	public Integer getElementStartingLine() {
		return elementStartingLine;
	}

	public Integer getElementStartingColumn() {
		return elementStartingColumn;
	}

	public Integer getElementEndingLine() {
		return elementEndingLine;
	}

	public Integer getElementEndingColumn() {
		return elementEndingColumn;
	}

	public Integer getBodyStartingLine() {
		return bodyStartingLine;
	}

	public Integer getBodyStartingColumn() {
		return bodyStartingColumn;
	}

	public Integer getBodyEndingLine() {
		return bodyEndingLine;
	}

	public Integer getBodyEndingColumn() {
		return bodyEndingColumn;
	}
	// getting codeSnippet from file content
	public String getCodeSnippetFromFileContent() {
		return codeSnippetFromFileContent;
	}

	public void setCodeSnippetFromFileContent(String codeSnippetFromFileContent) {
		this.codeSnippetFromFileContent = codeSnippetFromFileContent;
	}
}