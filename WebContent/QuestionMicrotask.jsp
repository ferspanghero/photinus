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
</head>
<body bgcolor="#FFFFFF">

<script>
function submitAnswer(){
	
	var option = document.getElementById("answerOption").value;
	if(( option== 1) || (option ==2))
		if(document.getElementById("explanation").value == '')
	    	alert("Please provide an explanation for your answer.");
		else
			
	else
			
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
					<h2>${requestScope["source"]} </h2>

					<form name="answerForm" action=MicrotaskServlet method="get">
						<p>
							<input type="radio" name="answer" value="1">Yes <br>
							<input type="radio" name="answer" value="2">Probably yes<br> 
							<input type="radio" name="answer" value="3">I can't tell<br>
							<input type="radio" name="answer" value="4">Probably not <br>
							<input type="radio" name="answer" value="5">No<br>
							
							<br> <br> <font size="5">Explanation:
							</font><br>
							<textarea name="explanation" rows="4" cols="80">...</textarea>
						</p>
						
							<INPUT TYPE="submit" VALUE="Cancel">
							<INPUT TYPE="submit" VALUE="Submit">
						
							<!-- Hidden fields -->
							<input type="hidden" name="fileName" value=${requestScope["fileName"]} >
							<input type="hidden" name="id" value=${requestScope["id"]} >
							
							
					</form>
				</div>

				<div id="editor">
					${requestScope["source"]} 
				</div> <br> <script>
					var editor = ace.edit('editor');
					//var textarea = $('textarea[name="editor"]').hide();
					var Range = ace.require('ace/range').Range;
					editor.setReadOnly(true);
					editor.setTheme("ace/theme/github");
					editor.getSession().setMode("ace/mode/java");
					setTimeout(function() {
						//editor.gotoLine(30);
						editor.session.addMarker(new Range(3, 0, 15, 0),
								"ace_active-line", "fullLine");
						editor.session.addMarker(new Range(4, 5, 8, 5), "foo",
								"line");
						editor.session.addMarker(new Range(17, 5, 19, 8),
								"bar", "text");
					}, 100);
				</script>


		</tr>


	</table>




</body>
</html>