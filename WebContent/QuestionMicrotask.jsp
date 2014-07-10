<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Firefly - Question-based Crowd Debugging</title>
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

<body>
<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script
		src="https://rawgithub.com/ajaxorg/ace-builds/master/src-noconflict/ace.js"></script>
		

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
			alert("Please select an answer");
			return -1;
		} else {
			if ((radios[0].checked) || (radios[1].checked)) {//yes and probably yes must provide an explanation
				if (document.getElementById("explanation").value == '') {
					alert("Please provide an explanation for your answer.");
					return -1;
				} else
					return option;
			}
			else
				return option;
		}
	}

	function submitAnswer() {
		var checked = checkAnswer();
		alert("checked: "+checked);
		
		if (checked != -1) {
			 
			var jsonDataObject = new Object();
			jsonDataObject.fileName = document.getElementById("fileName").value;
			alert("jason fileName "+document.getElementById("fileName").value );
			jsonDataObject.id = document.getElementById("id").value;
			alert("jason id "+ jsonDataObject.id);
			jsonDataObject.answerOption = checked;
			alert("checked = "+ checked);
			jsonDataObject.explanation = document.getElementById("explanation").value;
			alert("explanation = "+ jsonDataObject.explanation);
			// turn the jsonData object into a string so it can be passed to the servlet
			var jsonData = JSON.stringify(jsonDataObject);

			$.getJSON("MicrotaskServlet", {
				action : "addAnswer",
				json : jsonData
			}, function(data) {
			});

			return false; // prevents the page from refreshing before JSON is read from server response
		} else {
		}

	}

	function cancel() {
		//user skipped. Ask whether she wants new microtask or wants to close the tab.
	}
</script>


	<table border="0">
		<tr valign="bottom">
			<center>
				<td><img src="./images/Firefly-2.jpg" width=112 height=46 />
					&nbsp;&nbsp;&nbsp;</td>

				<td><form method="POST" action='./FileUpload.jsp'
						name="fileUpload">
						<input type="image" src="./images/UploadsButton.jpg"
							value="Upload Files" name="upload">
					</form></td>

				<td><img src="./images/MicrotasksButton-blue.jpg"></td>

				<td><img src="./images/ResultsButton-blue.jpg"></td>

			</center>
		</tr>

	</table>


	<table cellspacing="0">

		<tr bgcolor="#FFFAEB">
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td>&nbsp;&nbsp;&nbsp;</td>

			<td><script
					src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
				<script
					src="https://rawgithub.com/ajaxorg/ace-builds/master/src-noconflict/ace.js"></script>
				<div style="position: relative">
					<b>${requestScope["question"]}</b>

					<form name="answerForm" action=MicrotaskServlet method="get">
						<p>
							<input type="radio" name="answer" value="1">Yes <br>
							<input type="radio" name="answer" value="2">Probably yes<br>
							<input type="radio" name="answer" value="3">I can't tell<br>
							<input type="radio" name="answer" value="4">Probably not<br> 
							<input type="radio" name="answer" value="5">No<br>

							<br>   <b>Explanation: </b><br>
							<textarea id="explanation" rows="3" cols="80">t</textarea>
						</p>

						<INPUT TYPE="button" VALUE="Cancel" onclick="cancel()"> 
						<INPUT TYPE="button" VALUE="Submit" onclick="submitAnswer()">
						<br>

						<!-- Hidden fields -->
						<input type="hidden" id="fileName" value=${requestScope["fileName"]}> 
						<input type="hidden" id="id" value=${requestScope["id"]}>
						<input type="hidden" id="startLine" value=${requestScope["startLine"]}> 
						<input type="hidden" id="startColumn" value=${requestScope["startColumn"]}>
						<input type="hidden" id="endLine" value=${requestScope["endLine"]}>
						<input type="hidden" id="endColumn" value=${requestScope["endColumn"]}>
					</form>
				</div>
			 
	 

	 
				 <div id="editor">${requestScope["source"]}</div>
				</td>
				 <br>
		
		
	<script>
    	var editor = ace.edit('editor');
   	    var Range = ace.require('ace/range').Range;
    	editor.setReadOnly(true);
    	editor.setTheme("ace/theme/github");
    	editor.getSession().setMode("ace/mode/java");
    
    	var startLine = document.getElementById("startLine").value;
    	var startColumn = document.getElementById("startColumn").value;
    	var endLine = document.getElementById("endLine").value;
    	var endColumn = document.getElementById("endColumn").value;
    
	    editor.setBehavioursEnabled(false);
    
    	setTimeout(function() {
      		editor.session.addMarker(new Range( startLine -1 , startColumn, 
    		  endLine -1 , endColumn), "foo", "line");
      		editor.gotoLine(startLine);
    	}, 100);
    
    	document.write(startLine, ", ", startColumn, " C ");
    	document.write(endLine, ", ", endColumn);
   </script>
		
	 </tr>
	</table>	

</body>
</html>