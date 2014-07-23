<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@ page
	import="edu.uci.ics.sdcl.firefly.*, java.util.*, java.util.Map.Entry"
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Firefly - Question-based Crowd Debugging</title>
<style type="text/css" media="screen">
#editor {
	position: relative;
	height: 200px;
	width: 700px;
}

#editorCaller {
	position: relative;
	height: 200px;
	width: 700px;
}

#editorCallee {
	position: relative;
	height: 200px;
	width: 700px;
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

#myDiv {
	max-width: 700px;
	background-color: #FFFAEB
}

#thumbs {
	width: 700px;
	margin-top: 0px;
	margin-left: 0px;
	margin-right: 10px;
	background-color: #FFFAEB text-align: justify;
	-ms-text-justify: distribute-all-lines;
	text-justify: distribute-all-lines;
}

#thumbs a {
	vertical-align: top;
	display: inline-block;
	*display: inline;
	zoom: 1;
}

.stretch {
	width: 100%;
	display: inline-block;
	font-size: 12;
	line-height: 0
}
</style>
</head>

<body>



	<script>
		function checkAnswer() {

			var radios = document.getElementsByName('answer');

			var option = -1;
			var i = 0;

			for (i = 0; i < radios.length; i++) {
				if (radios[i].checked) {
					option = i;
					break;
				}
			}

			if (option == -1) {
				alert("Please select an answer.");
				return -1;
			} else {
				if ((radios[0].checked) || (radios[1].checked)) {//yes and probably yes must provide an explanation
					if (document.getElementById("explanation").value == '') {
						alert("Please provide an explanation for your answer.");
						return -1;
					} else
						return option;
				} else
					return option;
			}
		}

		function submitAnswer() {
			var checked = checkAnswer();
			if (checked != -1) {
				document.forms["answerForm"].submit();
			} else {
				//nothing to do.
			}
		}

		function skipAnswer() {
			document.forms["skipForm"].submit();
		}
	</script>

	<div id="myDiv">

		<form name="skipForm" action="microtask" value="skip" method="post"></form>
		<br>
		<!-- Hidden fields -->
		<input type="hidden" id="startLine" value=${requestScope["startLine"]}>
		<input type="hidden" id="startColumn" value=${requestScope["startColumn"]}> 
		<input type="hidden" id="endLine" value=${requestScope["endLine"]}> 
		<input type="hidden" id="endColumn" value=${requestScope["endColumn"]}>
		<input type="hidden" id="methodStartingLine" value=${requestScope["methodStartingLine"]}>
	</div>

	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	<script
		src="https://rawgithub.com/ajaxorg/ace-builds/master/src-noconflict/ace.js"></script>

	<div id="myDiv">
		<b>${requestScope["question"]}</b><br> <br>
	</div>

	<div id="editor"><xmp>${requestScope["source"]}</xmp></div>

	<div id="thumbs" style="background-color: #FFFAEB">
		<br>
		<form name="answerForm" action="microtask" method="get">

			<a id="option1"> <input type="radio" name="answer" value="1">Yes
			</a> <a id="option2"> <input type="radio" name="answer" value="2">Probably yes
			</a> <a id="option3"> <input type="radio" name="answer" value="3">I can't tell
			</a> <a id="option4"> <input type="radio" name="answer" value="4">Probably not
			</a> <a id="option5"> <input type="radio" name="answer" value="5">No
			</a> <span class="stretch"></span> 
			<input type="hidden" name="fileName" value=${requestScope["fileName"]}> 
			<input type="hidden" name="id" value=${requestScope["id"]}> 
			<br> Please	provide an explanation for your answer: <br>
			<textarea name="explanation" id="explanation" rows="3" cols="72"></textarea>
			
			<br>
			
		</form>
	</div>

	<div id=myDiv>
		<br> <INPUT TYPE="button" VALUE="Skip this"	onclick="skipAnswer()"> 
		<INPUT TYPE="button" VALUE="Submit Answer" onclick="submitAnswer(event)">
	

		<br><br>
		
		<div id="editorCaller"><xmp>${requestScope["caller"]}</xmp></div>
		
		<br><br>
		
		<div id="editorCallee"><xmp>${requestScope["callee"]}</xmp></div>
				
		<script>
			/* First and main ACE Editor */
			var editor = ace.edit('editor');
			editor.setReadOnly(true);
			editor.setTheme("ace/theme/github");
			editor.getSession().setMode("ace/mode/java");
			editor.setBehavioursEnabled(false);
			editor.setOption("highlightActiveLine", false); // disable highligthing on the active line
			editor.setShowPrintMargin(false); 				// disable printing margin
	
			var startLine = document.getElementById("startLine").value;
			var startColumn = document.getElementById("startColumn").value;
			var endLine = document.getElementById("endLine").value;
			var endColumn = document.getElementById("endColumn").value;
			var Range = ace.require("ace/range").Range;
	
			var codeSnippetStartingLine = parseInt(document.getElementById("methodStartingLine").value);
			editor.setOption("firstLineNumber", codeSnippetStartingLine); // set the starting line to <second parameter>
	
			/* Second and caller ACE Editor */
			var editorCaller = ace.edit('editorCaller');
			editorCaller.setReadOnly(true);
			editorCaller.setTheme("ace/theme/github");
			editorCaller.getSession().setMode("ace/mode/java"); 
			editorCaller.setBehavioursEnabled(false);
			editorCaller.setOption("highlightActiveLine", false); 	// disable highligthing on the active line
			editorCaller.setShowPrintMargin(false);					// disable printing margin
			
			/* Third and callee ACE Editor */
			var editorCallee = ace.edit('editorCallee');
			editorCallee.setReadOnly(true);
			editorCallee.setTheme("ace/theme/github");
			editorCallee.getSession().setMode("ace/mode/java"); 
			editorCallee.setBehavioursEnabled(false);
			editorCallee.setOption("highlightActiveLine", false); 	// disable highligthing on the active line
			editorCallee.setShowPrintMargin(false);					// disable printing margin
			
			setTimeout(function() {
				editor.session.addMarker(new Range(startLine - codeSnippetStartingLine, startColumn, 
						endLine	- codeSnippetStartingLine, endColumn), "ace_active-line", "line");
				editor.gotoLine(startLine - codeSnippetStartingLine + 1);
			}, 100); 
		</script>

	</div>
</body>
</html>