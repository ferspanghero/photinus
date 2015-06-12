<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Microtask Page</title>
<style type="text/css" media="screen">
#mainEditor {
	position: relative;
}

.callers {
	position: absolute;
	background: rgba(27, 132, 249, 0.3);
	z-index: 20
}

.callees {
	position: absolute;
	background: rgba(27, 132, 249, 0.3);
	z-index: 20
}

#failurePrompt {
	max-width: 800px;
	background-color: #D1EEEE;
	text-align: justify;
	margin: 0 auto;
	text-justify: distribute-all-lines;
}

#internalText {
	margin-left: 10px;
	margin-right: 15px;
	text-justify: distribute-all-lines;
}

#questionPrompt {
	max-width: 800px;
	background-color: #B4CDCD;
	margin: 0 auto;
	text-align: justify;
	text-justify: distribute-all-lines;
}

#questionCode {
	max-width: 800px;
	background-color: #D1EEEE;
	margin: 0 auto;
	text-align: justify;
}

#buttons {
	background-color: #B4CDCD;
	margin: 0 auto;
	max-width: 800px;
}

#thumbs {
	width: 800px;
	background-color: #B4CDCD;
	text-align: justify;
	text-justify: distribute-all-lines;
	margin: 0 auto;
}

#thumbs a {
	vertical-align: top;
	display: inline-block;
	*display: inline;
	zoom: 1;
}

.ui-dialog-titlebar-close {
  visibility: hidden;
}

</style>

</head>
 <link rel="stylesheet" href="jquery/jquery-ui-1.10.4.custom.min.css">
 	<!--  <script	src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script> -->
  <script src="jquery/jquery-1.10.2.js"></script>
  <script src="jquery/jquery-ui-1.10.4.custom.min.js"></script>
  <script	src="http://cdnjs.cloudflare.com/ajax/libs/ace/1.1.3/ace.js"></script>
<body>
  
	<script type="text/javascript">
	function checkQuitAnswer() {

		var radios = document.getElementsByName("reason");
		
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
		}
	}

	var quitFormAlreadyPosted = false;


		function submitQuitAnswer() {
			//first thing is to check whether the form was already submitted
			if (quitFormAlreadyPosted) {
				alert("Please wait. If it is taking more time than expected, please send an email to the requester.");
			} else {
				//ok, form was not submitted yet
				var checked = checkQuitAnswer();
				if (checked != -1) {
					//$("#quit").on( "dialogclose", function() { 
					var form = document.forms["reasonForm"];
					form.action = "quit";
					form.submit();
					$("#quit").dialog("close");
					var workerId = document.getElementById("workerId").value;
					quitFormAlreadyPosted = true;
				} else {
					//nothing to do.
				}
			}
		}

		function checkAnswer() {

			var radios = document.getElementsByName("answer");
			var confidenceRadios = document.getElementsByName("confidence");

			var option = -1;
			var option2 = -1;

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
				//yes, probably yes, I can't tell must provide an explanation
				if ((radios[0].checked) || (radios[1].checked)
						|| (radios[2].checked)) {
					//check the confidence answer
					for (i = 0; i < confidenceRadios.length; i++) {
						if (confidenceRadios[i].checked) {
							option2 = i;
						}
					}

					if (option2 == -1) {
						alert("Please select a confidence level.");
						return -1;
					} else {
						if (document.getElementById("explanation").value == '') {
							alert("Please provide an explanation for your answer.");
							return -1;
						} else {
							return option;
						}
					}
				} else
					return option;
			}
		}

		var formAlreadyPosted = false;
		function showDifficultDialog() {
			//load dialog - popup
			$("#survey").load("difficultyDialog.jsp").dialog({
				autoOpen : false,
				modal : true,
				bgiframe : true,
				width : 485,
				resizable : false,
				closeOnEscape : false,
				title : "How difficult was this question for you to answer?"
			});
			$("#survey").dialog('open');
		}

		function showQuitDialog() {
			//load dialog - popup
			$("#quit").dialog({
				autoOpen : false,
				modal : true,
				bgiframe : true,
				width : 485,
				resizable : false,
				closeOnEscape : false,
				title : "Why did you decide to quit ?"
			});
			$("#quit").dialog('open');
		}

		function submitAnswer() {
			//first thing is to check whether the form was already submitted
			if (formAlreadyPosted) {
				alert("Please wait. If it is taking more time than expected, please send an email to the requester.");
			} else {
				//ok, form was not submitted yet
				var checked = checkAnswer();
				if (checked != -1) {
					formAlreadyPosted = true;
					var form = document.forms["answerForm"];
					form.action = 'microtask';
					showDifficultDialog();
					$("#survey").on("dialogclose", function() {
						form.submit();
					});
				} else {
					//nothing to do.
				}
			}
		}
	</script>

	<!-- Hidden fields -->
	<input type="hidden" id="startLine" value=${requestScope["startLine"]}>
	<input type="hidden" id="startColumn"
		value=${requestScope["startColumn"]}>
	<input type="hidden" id="endLine" value=${requestScope["endLine"]}>
	<input type="hidden" id="endColumn" value=${requestScope["endColumn"]}>
	<input type="hidden" id="methodStartingLine"
		value=${requestScope["methodStartingLine"]}>
	<input type="hidden" id="positionsCaller"
		value=${requestScope["positionsCaller"]}>
	<input type="hidden" id="positionsCallee"
		value=${requestScope["positionsCallee"]}>
	<input type="hidden" id="calleesInMain"
		value=${requestScope["calleesInMain"]}>
	<input type="hidden" id="sourceLOCS"
		value=${requestScope["sourceLOCS"]}>
	<input type="hidden" id="callerLOCS"
		value=${requestScope["callerLOCS"]}>
	<input type="hidden" id="calleeLOCS"
		value=${requestScope["calleeLOCS"]}>
		
	<div style="display: none;" id="survey"></div>
	<div style="display: none;" id="quit">
		<div id="internalText">
			<form name="reasonForm" method="get" onsubmit="submitQuitAnswer()">
				<input type="radio" name="reason" value="hard" />The task is too
				hard &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; <br> <input
					type="radio" name="reason" value="boring" />The task is too boring
				&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; <br> <input
					type="radio" name="reason" value="long" />The task is too long
				&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp; <br> <input
					type="radio" name="reason" value="other" />Other&nbsp; &nbsp;
				&nbsp; &nbsp;&nbsp; &nbsp;
				
				<!-- Hidden fields -->
				<input type="hidden"
					name="workerId" id="workerId" value=${requestScope["workerId"]}> 
				<input type="hidden" name="microtaskId"
					value=${requestScope["microtaskId"]}> <input type="hidden"
					name="timeStamp" value=${requestScope["timeStamp"]}>
					<input type="hidden"
					name="sessionId" value=${requestScope["sesionId"]}> 
					
				<center>
					<INPUT TYPE="submit" name="reasonButton" id="reasonButton"
						VALUE="Submit">
				</center>
				<br>
			</form>

		</div>
	</div>

	<!-- src=" http://cdnjs.cloudflare.com/ajax/libs/ace/1.1.3/ace.js" https://rawgithub.com/ajaxorg/ace-builds/master/src-noconflict/ace.js-->
	<div id="failurePrompt">
		<div id="internalText">
			Thanks for using <b>Crowd Debug!</b> and for helping us debug
			software from all over the world. 
			<br><b> We are executing the following test:</b>
				<pre><code>${requestScope["testCase"]}</code></pre>			
			<br><b> But we are receiving the following failure:</b>
			   <pre><code>${requestScope["bugReport"]}</code></pre>
		</div>
	</div>


	<div id="questionPrompt">
		<div id="internalText">
			<b><br> ${requestScope["question"]}</b>
		</div>
	</div>


	<div id="thumbs">
		<div id="internalText">
			<form name="answerForm" method="get">

				<center>
					<br> <a>
					<input type="radio" name="answer" value="1" />Yes, there is an issue</a> &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; <a>
					<input type="radio" name="answer" value="2" />I don't know</a> &nbsp;	&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; <a>
					<input type="radio" name="answer" value="3" />No, there is not an issue</a> &nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp; <a>
				</center>
				<br>
				<label>
					<b>How confident are you with your answer?</b>
				</label>
				<br>
				<center>
					<br>
					<input type="radio" name="confidence" value="1" />Very confident &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp;
					<input type="radio" name="confidence" value="2" />Confident &nbsp;	&nbsp; &nbsp; &nbsp;&nbsp; &nbsp;
					<input type="radio" name="confidence" value="3" />Somewhat confident &nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;
					<input type="radio" name="confidence" value="2" />Unsure &nbsp;	&nbsp; &nbsp; &nbsp;&nbsp; &nbsp;
					<input type="radio" name="confidence" value="3" />Not at all &nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;
				</center>


				<!-- Hidden fields -->
				<input type="hidden"
					name="workerId" id="workerId" value=${requestScope["workerId"]}> 
				<input type="hidden" name="microtaskId"
					value=${requestScope["microtaskId"]}> <input type="hidden"
					name="timeStamp" value=${requestScope["timeStamp"]}>
					<input type="hidden"
					name="sessionId" value=${requestScope["sesionId"]}> 
				<center>
					<br>Please provide an explanation for your answer: <br>
					<textarea name="explanation" id="explanation" rows="3" cols="82"></textarea>
				</center>
				<center>				<br>
				<INPUT TYPE="button" name="answerButton" id="answerButton" VALUE="Submit"
					onclick="submitAnswer(event)">
					<input type="button"  value="Quit" onclick='showQuitDialog()'>
				</center>
										
				<br><br>
			</form>
			
		</div>
	</div>

	<div id="questionCode">
		<div id="internalText">

		<b>The source code:</b> 
		<div id="mainEditor"><xmp>${requestScope["source"]}</xmp></div>
		
		<br>
		
		<div id="context"></div>
		
		<div id="editorCaller"><xmp>${requestScope["caller"]}</xmp></div>
		
		<div id="space"></div>
		
		<div id="editorCallee"><xmp>${requestScope["callee"]}</xmp></div>

			<script>
				function computeHeight(linespan) {

					if (linespan <= 10) {
						var pixels = linespan * 20;
						return pixels + 'px';
					} else if (linespan > 35)
						return '450px';
					else {
						var pixels = 150 + (linespan - 10) * 90 / 5
						return pixels + 'px';
					}
				}

				/* First and main ACE Editor */
				/* setting properties of main editor */
				var divMainEditor = document.getElementById('mainEditor');
				divMainEditor.style.position = 'relative';
				var sourceLinespan = document.getElementById("sourceLOCS").value;
				divMainEditor.style.height = computeHeight(sourceLinespan);
				divMainEditor.style.width = '760px';

				var mainEditor = ace.edit("mainEditor");
				mainEditor.setReadOnly(true);
				mainEditor.setTheme("ace/theme/github");
				mainEditor.getSession().setMode("ace/mode/java");
				mainEditor.setBehavioursEnabled(false);
				mainEditor.setOption("highlightActiveLine", false); // disable highligthing on the active line
				mainEditor.setShowPrintMargin(false); // disable printing margin

				var startLine = document.getElementById("startLine").value;
				var startColumn = document.getElementById("startColumn").value;
				var endLine = document.getElementById("endLine").value;
				var endColumn = document.getElementById("endColumn").value;
				var Range = ace.require("ace/range").Range;

				var codeSnippetStartingLine = parseInt(document
						.getElementById("methodStartingLine").value);
				mainEditor
						.setOption("firstLineNumber", codeSnippetStartingLine); // set the starting line to <second parameter>	

				// parameters for the others AceEditor
				var highlightCaller = document
						.getElementById("positionsCaller").value;
				var highlightCallee = document
						.getElementById("positionsCallee").value;
				var calleesInMain = document.getElementById("calleesInMain").value;

				setTimeout(
						function() {
							// highlight regarding main method
							mainEditor.session.addMarker(new Range(startLine
									- codeSnippetStartingLine, startColumn,
									endLine - codeSnippetStartingLine,
									endColumn), "ace_active-line", "line");

							mainEditor.gotoLine(startLine
									- codeSnippetStartingLine + 1);
							if (calleesInMain) { // highlighting callees
								var numbersCalleesInMain = calleesInMain
										.split("#");
								var lnStart = 0.0;
								var clStart = 0.0;
								var lnEnd = 0.0;
								var clEnd = 0.0;
								//document.write("Callee length: " + numbersCallee.length + "<br>");
								for (i = 0; i < numbersCalleesInMain.length; i += 4) {
									lnStart = numbersCalleesInMain[i] - 1;
									clStart = numbersCalleesInMain[i + 1];
									lnEnd = numbersCalleesInMain[i + 2] - 1;
									clEnd = numbersCalleesInMain[i + 3];
									mainEditor.session.addMarker(new Range(
											lnStart - codeSnippetStartingLine
													+ 1, clStart, lnEnd
													- codeSnippetStartingLine
													+ 1, clEnd), "callees",
											"line");
									//alert("main: " + lnStart + ", " + clStart + ", " + lnEnd + ", " + clEnd +"<br>");
								}
							}

							// other ACE Editor highlights
							if (highlightCaller) {
								/* setting properties of the div caller */
								var divCaller = document
										.getElementById('editorCaller');
								divCaller.style.position = 'relative';
								var sourceLinespan = document
										.getElementById("callerLOCS").value;
								divCaller.style.height = computeHeight(sourceLinespan);
								divCaller.style.width = '760px';

								/* Second and caller ACE Editor */
								var editorCaller = ace.edit('editorCaller');
								editorCaller.setReadOnly(true);
								editorCaller.setTheme("ace/theme/github");
								editorCaller.getSession().setMode(
										"ace/mode/java");
								editorCaller.setBehavioursEnabled(false);
								editorCaller.setOption("highlightActiveLine",
										false); // disable highligthing on the active line
								editorCaller.setShowPrintMargin(false); // disable printing margin

								var numbersCaller = highlightCaller.split("#");
								var lnStart = 0.0;
								var clStart = 0.0;
								var lnEnd = 0.0;
								var clEnd = 0.0;
								//document.write("Caller length: " + numbersCaller.length + "<br>");
								for (i = 0; i < numbersCaller.length; i += 4) {
									lnStart = numbersCaller[i] - 1;
									clStart = numbersCaller[i + 1];
									lnEnd = numbersCaller[i + 2] - 1;
									clEnd = numbersCaller[i + 3];
									editorCaller.session.addMarker(new Range(
											lnStart, clStart, lnEnd, clEnd),
											"ace_active-line", "line");
									//document.write("positions: " + lnStart + ", " + clStart + ", " + lnEnd + ", " + clEnd +"<br>");
								}
								//document.write("<br>");
							}

							if (highlightCallee) {
								/* setting properties of the div caller */
								var divCallee = document
										.getElementById('editorCallee');
								divCallee.style.position = 'relative';
								var sourceLinespan = document
										.getElementById("calleeLOCS").value;
								divCallee.style.height = computeHeight(sourceLinespan);
								divCallee.style.width = '760px';

								/* Third and callee ACE Editor */
								var editorCallee = ace.edit('editorCallee');
								editorCallee.setReadOnly(true);
								editorCallee.setTheme("ace/theme/github");
								editorCallee.getSession().setMode(
										"ace/mode/java");
								editorCallee.setBehavioursEnabled(false);
								editorCallee.setOption("highlightActiveLine",
										false); // disable highligthing on the active line
								editorCallee.setShowPrintMargin(false); // disable printing margin

								var numbersCallee = highlightCallee.split("#");
								var lnStart = 0.0;
								var clStart = 0.0;
								var lnEnd = 0.0;
								var clEnd = 0.0;
								//document.write("Callee length: " + numbersCallee.length + "<br>");
								for (i = 0; i < numbersCallee.length; i += 4) {
									lnStart = numbersCallee[i] - 1;
									clStart = numbersCallee[i + 1];
									lnEnd = numbersCallee[i + 2] - 1;
									clEnd = numbersCallee[i + 3];
									editorCallee.session.addMarker(new Range(
											lnStart, clStart, lnEnd, clEnd),
											"callees", "line");
									//alert("callee positions: " + lnStart + ", " + clStart + ", " + lnEnd + ", " + clEnd +"<br>");
								}
							}
							// just do make a space between Editors
							if (highlightCaller && highlightCallee)
								document.getElementById('space').innerHTML = '<br>';
							// just to fill the label about the Editors
							if (highlightCaller || highlightCallee)
								document.getElementById('context').innerHTML = '<b>Functions that call or are called by the source code above:</b>';

						}, 100);
				
				function quit() {
					if (confirm('Confirm quitting the study ?')) {
						document.forms["quitForm"].submit();
						formAlreadyPosted=true;
					}
				}
			</script>
			<br>
		</div>

	</div>
</body>
</html>