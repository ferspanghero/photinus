<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@ page import="edu.uci.ics.sdcl.firefly.*, java.util.*, java.util.Map.Entry" language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset=utf-8 />
<title>GUI</title>
<style type="text/css" media="screen">
#editor {
	position: relative;
	height: 300px;
}

.foo {
	position: absolute;
	background: rgba(100, 200, 100, 0.5);
	z-index: 20
}

.bar {
	position: absolute;
	background: rgba(100, 100, 200, 0.5);
	z-index: 20
}
</style>
</head>

<%! String question = new String("Sou uma questão dentro de uma variável! Concorda?"); 
	String example = new String("\npublic SimpleSampleCode(Integer seedValue) {\n" +
		"	if(SeedLimit == null)\n" +
			"		this.SeedLimit = new Integer(seedValue);\n" +
        "	Integer testes = 3;" +
        "	if(testes == 5) this.SeedLimit = new Integer(testes);\n" + 
        "	if(testes == 8) { this.SeedLimit = new Integer(testes); } // If with curly braces at the same line\n" +
        "	if(testes == 10) {\n" +
                "		this.SeedLimit = new Integer(testes); // If with curly braces\n" +
        "	}\n" +
	"}"); 
	Microtask lastQuestion; 
	CodeSnippetFactory codeSnippets = new CodeSnippetFactory(
			"C:\\Users\\Danilo\\Documents\\GitHub\\crowd-debug-firefly\\src\\sample\\JustOneSample");
	ArrayList<CodeSnippet> methodsParsed = codeSnippets.generateSnippets();
	QuestionFactory questionFactory = new QuestionFactory();
	HashMap<Integer, Microtask> concreteQuestionsMade = questionFactory.generateQuestions(methodsParsed);
	Set<Map.Entry<Integer, Microtask>> set = concreteQuestionsMade.entrySet();
	Iterator<Entry<Integer, Microtask>> i = set.iterator();
	String method = new String();
%>
	
	<%
	while(i.hasNext()) 
	{
         Map.Entry<Integer, Microtask> me = (Map.Entry<Integer, Microtask>)i.next();
         System.out.print("ID = " + me.getKey() + ": ");
         System.out.println(me.getValue().getQuestion());
         lastQuestion = me.getValue();
        if (36 == me.getKey()) break;
    }
	%>

<body bgcolor="#F0F8FF">
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	<script
		src="https://rawgithub.com/ajaxorg/ace-builds/master/src-noconflict/ace.js"></script>
		
	<div style="position: relative">
		<h1>Question</h1>
		<h2><%= lastQuestion.getQuestion() %></h2>

		<form name="input" action="demo_form_action.asp" method="get">
			<p>
				Would you like to answer? <input type="radio" name="PreAnswer"
					value="yes">Yes <input type="radio" name="PreAnswer"
					value="no">No <input type="radio" name="PreAnswer"
					value="no">Maybe<br>
				<br> <font size="5">Explanation: </font><br>
				<textarea id="textArea1" rows="4" cols="80">Firefly</textarea>
			</p>
		</form>
	</div>

	<div id="editor"><%= CodeSnippetFactory.getFileContent() %></div>

	<br>
	
	<script>
    var editor = ace.edit('editor');
    //var textarea = $('textarea[name="editor"]').hide();
    var Range = ace.require('ace/range').Range;
    editor.setReadOnly(true);
    editor.setTheme("ace/theme/github");
    editor.getSession().setMode("ace/mode/java");
    
    editor.setBehavioursEnabled(false);
    //editor.setOption("firstLineNumber", %= lastQuestion.getMethod().getMethodSignature().getLineNumber() %>);
    
    setTimeout(function() {
      editor.session.addMarker(new Range(<%= lastQuestion.getStartingLine() -1 %>, <%= lastQuestion.getStartingColumn() %>, 
      		<%= lastQuestion.getEndingLine() -1 %>, <%= lastQuestion.getEndingColumn() %>), "foo", "line");
      //editor.session.addMarker(new Range(6, 0 , 6, 2000), "foo", "line");
      //editor.session.addMarker(new Range(17, 5, 19, 8), "bar", "text");
      editor.gotoLine(<%= lastQuestion.getStartingLine() %>);
    }, 100);
    
    document.write(<%= lastQuestion.getStartingLine() %>, ", ", <%= lastQuestion.getStartingColumn() %>, " C ");
    document.write(<%= lastQuestion.getEndingLine() %>, ", ", <%= lastQuestion.getEndingColumn() %>);
  </script>


	<div style="position: relative">
		<TABLE BORDER="0">
			<TR>

				<TD ALIGN=left><FORM METHOD="LINK" ACTION="http://cancel.com">
						<INPUT TYPE="submit" VALUE="Cancel">
					</FORM></TD>
				<TD></TD>

				<TD align=right><FORM METHOD="LINK" ACTION="http://submit.com">
						<INPUT TYPE="submit" VALUE="Submit" onclick="">
					</FORM></TD>

			</TR>
		</TABLE>
	</div>
</body>
</html>