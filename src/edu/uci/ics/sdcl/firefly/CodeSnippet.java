package edu.uci.ics.sdcl.firefly;
import java.util.ArrayList;

public class CodeSnippet
{
	protected String packageName;					// package name
	protected String className; 					// file
//	protected String methodName; 					// name of method
//	protected String implementationType; 			// concrete or abstract
	protected String methodBody;					// whole content of method
	protected Boolean returnStatement;				// true if there is a return value
	protected MethodSignature methodSignature;		// parsed method declaration
	protected ArrayList<CodeElement> statements;	// list of statements
	
	
	public CodeSnippet(String packageName, String className, 
			String methodBody, Boolean returnStatement, 
			MethodSignature methodSignature)
	{
		this.packageName = packageName;
		this.className = className;
//		this.methodName = methodName;
//		this.implementationType = implementationType;
		this.methodBody = methodBody;
		this.returnStatement = returnStatement;
		this.methodSignature = methodSignature;
		this.statements = new ArrayList<CodeElement>();
	}
	
	public CodeSnippet(String packageName2, String className2,
			StringBuffer buffer,
			Boolean returnStatement2, MethodSignature signature)
	{
		this.packageName = packageName2;
		this.className = className2;
//		this.methodName = name;
//		this.implementationType = implementationType;
		this.methodBody = buffer.toString();
		this.returnStatement = returnStatement2;
		this.methodSignature = signature;
		this.statements = new ArrayList<CodeElement>();
	}

	@Override
	public String toString() {
		return "CodeSnippet [package name=" + packageName + "className="+ className+ 
//				" methodName= "+methodName +", implementationType=" + implementationType
				 ", methodBody=" + methodBody + ", returnStatment=" + returnStatement
				+ ", methodSignature=" + methodSignature + ", statements="
				+ statements + "]";
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
}