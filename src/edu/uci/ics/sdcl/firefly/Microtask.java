package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import edu.uci.ics.sdcl.firefly.lite.MicrotaskLite;

public class Microtask implements Serializable
{	
	private static final long serialVersionUID = 1L;
	private String question;
	private String failureDescription;
	private String testCase;
	private CodeSnippet method;
	private CodeElement codeElement;
	private String codeElementType;
	private String snippetHightlights;
	private String callerHightlights;
	private String calleeHightlights;

	private Vector<Answer> answerList;

	private Integer startingLine;
	private Integer startingColumn;
	private Integer endingLine;
	private Integer endingColumn;
	private Integer ID;
	private String calleeFileContent;
	private Integer calleeLOCS;
	private String callerFileContent;
	private Integer callerLOCS;
	private String fileName;
	
	private String questionType;


	/**
	 * 
	 * Represents a question about one element (see CodeElement class) in a code snippet. The element is 
	 * localized by the line and column parameters provided.
	 * 
	 * @param codeElementTypeArg
	 * @param methodArg
	 * @param codeElement
	 * @param questionArg
	 * @param startingLineNumber 
	 * @param startingColumnNumber
	 * @param endingLineNumber
	 * @param endingColumnNumber
	 * @param ID
	 */
	public Microtask(String codeElementType, CodeSnippet method, CodeElement codeElement, String question, Integer startingLineNumber, 
			Integer startingColumnNumber, Integer endingLineNumber, Integer endingColumnNumber, Integer ID, String failureDescription, String testCase)
	{
		this.setCodeSnippet(method);
		this.setQuestion(question);
		this.setCodeElementType(codeElementType);
		this.codeElement = codeElement;
		this.startingLine = startingLineNumber;
		this.startingColumn = startingColumnNumber;
		this.endingLine = endingLineNumber;
		this.endingColumn = endingColumnNumber;
		this.answerList = new Vector<Answer>();
		this.ID = ID;
		this.failureDescription = failureDescription;
		this.testCase = testCase;
		this.fileName = method.getFileName();
	}

	/** Simplified version with only the data needed to write a Session Report */
	public Microtask(String question, Integer ID, Vector<Answer> answerList, String fileName)	{
		this.answerList = answerList;
		this.ID = ID;
		this.question = question;
		this.fileName = fileName;
	}
	
	
	/** Light version used during microtask execution */
	public Microtask(int microtaskId, String question,
			String failureDescription, String testCase, String snippetHightlights,
			String callerHightlights, String calleeHightlights,
			int startingColumn, int endingLine, int endingColumn,
			String calleeFileContent, int calleeLOCS, String callerFileContent,
			int callerLOCS, String fileName) {
		
		this.ID = new Integer(microtaskId);
		this.question = question;
		this.failureDescription = failureDescription;
		this.testCase = testCase;
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
		this.fileName = fileName;
	}
	
	public Microtask getSimpleVersion(){
		Vector<Answer> answerListCopy = new Vector<Answer>();
		for(Answer answer: this.getAnswerList()){
			answerListCopy.add(answer);
		}
		return new Microtask(this.getQuestion(),this.getID(),answerListCopy,this.getFileName());
	}

	public Integer getID(){
		return this.ID;
	}
	
	public String getQuestion()
	{
		return question;
	}

	public void setQuestion(String question)
	{
		this.question = question;
	}
	
	public String getCodeElementType()
	{
		return codeElementType;
	}

	public void setCodeElementType(String type)
	{
		this.codeElementType = type;
	}

	public CodeSnippet getCodeSnippet()
	{
		return method;
	}

	public CodeElement getCodeElement() {
		return codeElement;
	}
	
	public void setCodeSnippet(CodeSnippet method)
	{
		this.method = method;
	}

	public Vector<Answer> getAnswerList()
	{
		return answerList;
	}

	public void setAnswerList(Vector<Answer> answerList)
	{
		this.answerList = answerList;
	}
	
	public void addAnswer(Answer answer){
		this.answerList.add(answer);
	}
	
	/* getters for the position */
	public Integer getStartingLine() {
		return startingLine;
	}

	public Integer getStartingColumn() {
		return startingColumn;
	}

	public Integer getEndingLine() {
		return endingLine;
	}

	public Integer getEndingColumn() {
		return endingColumn;
	}

	public int getNumberOfAnswers() {
		return this.answerList.size();
	}

	public String getFailureDescription() {
		return failureDescription;
	}
	
	public String getTestCase(){
		return testCase;
	}

	public String getSnippetHightlights() {
		return snippetHightlights;
	}

	public void setSnippetHightlights(String snippetHightlights) {
		this.snippetHightlights = snippetHightlights;
	}

	public String getCallerHightlights() {
		return callerHightlights;
	}

	public void setCallerHightlights(String callerHightlights) {
		this.callerHightlights = callerHightlights;
	}


	public String getCalleeHightlights() {
		return calleeHightlights;
	}

	public void setCalleeHightlights(String calleeHightlights) {
		this.calleeHightlights = calleeHightlights;
	}

	public void setCallerFileContent(String fileContent){
		this.callerFileContent = fileContent;
		String contentLines[] = callerFileContent.toString().split("\r\n|\r|\n");
		this.callerLOCS = new Integer(contentLines.length);		 
	}

	public String getCallerFileContent(){
		return this.callerFileContent;
	}
	
	public Integer getCallerLOCS(){
		return this.callerLOCS;
	}
	
	public void setCalleeFileContent(String fileContent){
		this.calleeFileContent = fileContent;
		String contentLines[] = calleeFileContent.toString().split("\r\n|\r|\n");
		this.calleeLOCS = new Integer(contentLines.length);		 
	}

	public String getCalleeFileContent(){
		return this.calleeFileContent;
	}
	
	public Integer getCalleeLOCS(){
		return this.calleeLOCS;
	}

	public Answer getAnswerByUserId(String workerId) {
		Answer foundAnswer=null;
		for(Answer answer: answerList){
			if(answer!=null && answer.getWorkerId().matches(workerId)){
				foundAnswer = answer;
				break;
			}
		}
		return foundAnswer;
	}
	
	public List<String> getWorkerIds(){
		List<String> workerIDs = new ArrayList<String>();
		for (Answer answer : answerList) {
			if(answer != null){
				workerIDs.add(answer.getWorkerId());
			}
		}
		return workerIDs;
	}
	
	public Microtask getLiteVersion(){
		return new Microtask(
				this.ID,
				this.question,
				this.failureDescription,
				this.testCase,
				this.snippetHightlights,
				this.callerHightlights,
				this.calleeHightlights,
				this.startingColumn,
				this.endingLine,
				this.endingColumn,
				this.calleeFileContent,
				this.calleeLOCS,
				this.callerFileContent,
				this.callerLOCS,
				this.fileName
				);
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	
	public ArrayList<String> getAnswerOptions(){
		ArrayList<String> list = new ArrayList<String>();
		for(Answer answer : this.answerList){
			list.add(answer.getOption());
		}
		return list;
	}
	
}