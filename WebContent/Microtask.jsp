<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@ page
	import="edu.uci.ics.sdcl.firefly.*, java.util.*, java.util.Map.Entry"
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset=utf-8 />
<title>Firefly - Question-based Crowd Debugging</title>
<style type="text/css" media="screen">
#editor {
	position: relative;
	height: 200px;
	width: 660px;
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
							  
				$.get("microtask", {
					action: "microtask",
					fileName : document.getElementById("fileName").value,
					answerOption: checked,
					explanation: document.getElementById("explanation").value,
					id:document.getElementById("id").value,
				}, function(data) { 
					//window.location.reload(false);
				});

				//return false; // prevents the page from refreshing before JSON is read from server response
			} else {
				//nothing to do.
			}
		}

		function cancel() {
		 	//Ask the use if she want's a new microtask or want's to close the tab.
		}
	</script>



	<table border="0">
		<tr valign="bottom">

			<td><img src="./images/Firefly-2.jpg" width=112 height=46 />
				&nbsp;&nbsp;&nbsp;</td>

			<td><form method="POST" action='./FileUpload.jsp'
					name="fileUpload">
					<input type="image" src="./images/UploadsButton.jpg"
						value="Upload Files" name="upload">
				</form></td>

			<td><img src="./images/MicrotasksButton-blue.jpg"></td>

			<td><img src="./images/ResultsButton-blue.jpg"></td>


		</tr>

	</table>


	<table cellspacing="0" bgcolor="#FFFAEB">

		<tr bgcolor="#FFFAEB">


			<td><script
					src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
				<script
					src="https://rawgithub.com/ajaxorg/ace-builds/master/src-noconflict/ace.js"></script>
				<b>${requestScope["question"]}</b></td>
		</tr>
	</table>



	<table bgcolor="#FFFAEB">
		<tr>
			<td>
				<form name="answerForm" action=MicrotaskServlet method="get">
					 
						<input type="radio" name="answer" value="1">Yes <br>
						<input type="radio" name="answer" value="2">Probably yes<br>
						<input type="radio" name="answer" value="3">I can't tell<br>
						<input type="radio" name="answer" value="4">Probably not<br>
						<input type="radio" name="answer" value="5">No<br>
				 
					<!-- Hidden fields -->
					<input type="hidden" id="fileName"
						value=${requestScope["fileName"]}> <input type="hidden"
						id="id" value=${requestScope["id"]}> <input type="hidden"
						id="startLine" value=${requestScope["startLine"]}> <input
						type="hidden" id="startColumn"
						value=${requestScope["startColumn"]}> <input type="hidden"
						id="endLine" value=${requestScope["endLine"]}> <input
						type="hidden" id="endColumn" value=${requestScope["endColumn"]}>
				</form>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="left"><br>  Please provide an
				explanation for your answer:  <br> <textarea id="explanation"
					rows="6" cols="50"></textarea></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="right"><INPUT TYPE="button" VALUE="Cancel"
				onclick="cancel()"> <INPUT TYPE="button"
				VALUE="Submit Answer" onclick="submitAnswer()"></td>
		<tr>
	</table>


	<table bgcolor="#FFFAEB">
		<tr>
			<td align="left">
				<div id="editor">${requestScope["source"]}</div> <script>
			var editor = ace.edit('editor');
			editor.setReadOnly(true);
			editor.setTheme("ace/theme/github");
			editor.getSession().setMode("ace/mode/java");
		    editor.setBehavioursEnabled(false);
			var startLine = document.getElementById("startLine").value;
			var startColumn =  document.getElementById("startColumn").value;
			var endLine = document.getElementById("endLine").value;
			var endColumn =  document.getElementById("endColumn").value;  
		    var Range = ace.require("ace/range").Range;
		    
		    setTimeout(function() {
		    	editor.session.addMarker(new Range(startLine - 1, startColumn,
						endLine - 1, endColumn), "foo", "line");
				editor.gotoLine(startLine);
			}, 100);
		  
		    //alert("here 2");
			//document.write(startLine, ", ", startColumn, " C ");
			//document.write(endLine, ", ", endColumn);
		</script>

			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		</tr>
		<tr bgcolor="#FFFAEB">
			<td><br>
			<br>
			<br></td>
			<td></td>
		</tr>
	</table>
</body>
</html>