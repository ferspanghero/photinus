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
	margin-left: 12px;
	margin-right: 12px;
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

table {
	table-layout: fixed;
	word-break: break-all;
}

table td {
	border: 0px solid;
	border-color: #C0C0C0;
	overflow: hidden;
}

.sectionTitle {
	padding-right: 0px;
	padding-left: 0px;
	font-weight: bold;
}

.box {
	width: 450px;
}

div.inner {
	display: inline-block;
	padding-top: 4px;
	width: 100%;
	height: 30px;
}

#progressbar {
	text-align: center;
	overflow: hidden;
	position: relative;
	vertical-align: middle;
	height: 15px;
}

#label {
	font-size:11px;
  margin: auto;
  position: absolute;
  top: 1px; left: 0; bottom: 0; right: 0;
}
</style>

</head>
 <link rel="stylesheet" href="jquery/jquery-ui-1.10.4.custom.min.css">
 	<!--  <script	src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script> -->
  <script src="jquery/jquery-1.10.2.js"></script>
  <script src="jquery/jquery-ui-1.10.4.custom.min.js"></script>
  <script src="jquery/quitDialog.js"></script>
  <script	src="http://cdnjs.cloudflare.com/ajax/libs/ace/1.1.3/ace.js"></script>
  
<body onload="splitReplaceTestDescription()">
  
	<script type="text/javascript">
	
	function checkDifficultyAnswers() {

		var difficulty = document.getElementsByName("difficulty");
		var difficultyOption = -1;
		for (i = 0; i < difficulty.length; i++) {
			if (difficulty[i].checked) {
				difficultyOption = i;
				break;
			}
		}
		if (difficultyOption == -1) {
			alert("Please select a level of difficulty.");
			return -1;
		}

		return 1;
	}

	var difficultyFormAlreadyPosted = false;

	function submitDifficulty() {
		//first thing is to check whether the form was already submitted
		if (difficultyFormAlreadyPosted) {
			alert("Please wait. If it is taking more time than expected, please send an email to the requester.");
		} else {
			var checked = checkDifficultyAnswers();
			if (checked != -1) {
				difficultyFormAlreadyPosted = true;
				var form = document.forms["difficultyForm"];
				var dif = $("input[name=difficulty]:checked").val();
				form.submit();
				$("#difficulty").dialog('close');
			} else {
				//nothing to do.
			}
		}
	}

  function onloadTest(){
		var description = "mymethod();hermethod();car();"
	alert("description:"+description);
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

		function showDifficultDialog() {
			//load dialog - popup
			$("#difficulty").dialog({
				autoOpen : false,
				modal : true,
				bgiframe : true,
				width : 340,
				height: 160,
				resizable : false,
				closeOnEscape : false,
				title : "How difficult was this task for you?",
				open: function (event, ui) {
				    $('#survey').css('overflow', 'hidden');
				    $(this).find('select, input, textarea').first().blur();
				}
			});
			$("#difficulty").dialog('open');
		}
		
		var formAlreadyPosted = false;
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
					$("#difficulty").on("dialogclose", function() {
						var dif = document.getElementById("hiddenDifficulty");
						dif.value = $("input[name=difficulty]:checked").val();
						form.submit();
					});
				} else {
					//nothing to do.
				}
			}
		}
		
		
		
		function splitReplaceTestDescription(){
			var description = '${requestScope["testCase"]}'; 
			//alert("description:"+description);
			var arr = description.split(';');
			var htmlContent = '';
			if(arr.length>1){
				for(var i=0; i<arr.length-1; i++) {
					var value = arr[i];
					htmlContent = htmlContent + '<code>'+ value + ';<code><br>';	
				}
			}
			else{
				htmlContent=arr[0];
			}
			//alert("htmlContent:"+ htmlContent);
			document.getElementById('testFailure').innerHTML=htmlContent;
		}

		$( document ).ready(function() {
		    $( "#progressbar" ).progressbar({
		    	max: parseInt(document.getElementById('totalTasks').value),
		    	value: parseInt(document.getElementById('currentTask').value)
		    });
	 	});
	</script>

	<!-- Hidden fields -->
	<input type="hidden" id="totalTasks" value=${requestScope["totalTasks"]}>
	<input type="hidden" id="currentTask" value=${requestScope["currentTask"]}>
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
		
	<div style="display: none;" id="difficulty">
	<center>
			<form name="difficultyForm" method="get">
			<table cellspacing='0' cellpadding='0' style="word-wrap:break-word"> 	
   				<col width="80px" />
    			<col width="80px" />
    			<col width="80px" />
    			<col width="80px" />
    			<col width="80px" />
    			<col width="80px" />
    			<tr>
						<td align="center">Very</td>
						<td></td>
						<td></td>
						<td></td>
						<td align="center">Not</td>
				</tr>
				<tr>
					<td align="center"><label><input type="radio" name="difficulty" value="5" />5</label></td>
					<td align="center"><label><input type="radio" name="difficulty" value="4" />4</label></td>
					<td align="center"><label><input type="radio" name="difficulty" value="3" />3</label></td>
					<td align="center"><label><input type="radio" name="difficulty" value="2" />2</label></td>
					<td align="center"><label><input type="radio" name="difficulty" value="1" />1</label></td>
				</tr>
			</table>
				<br>
				 <INPUT TYPE="button" VALUE="Submit" name="difficultySubmit" ID="difficultyButton" 
					onclick="submitDifficulty()">
					
			</form>
	</center>
	</div>
	<div style="display: none;" id="quit">
		<div id="internalText">
			<form name="reasonForm" method="get">
				<input type="radio" name="reason" value="difficult" />The task is too
				difficult &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; <br> <input
					type="radio" name="reason" value="boring" />The task is too boring
				&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; <br> <input
					type="radio" name="reason" value="long" />The task is too long
				&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp; <br> <input
					type="radio" name="reason" value="other" />Other&nbsp; 
					<input type="text" id="otherReason" name="otherReason" /><br><br>
				
				<!-- Hidden fields -->
				<input type="hidden"
					name="workerId" id="workerId" value=${requestScope["workerId"]}> 
				<input type="hidden" name="microtaskId"
					value=${requestScope["microtaskId"]}> <input type="hidden"
					name="timeStamp" value=${requestScope["timeStamp"]}>
					
					
				<center>
					<INPUT TYPE="button" name="reasonButton" id="reasonButton" onclick="submitQuitAnswer()"
						VALUE="Submit">
					<INPUT TYPE="button" VALUE="Cancel"
					onclick="quitDialog()">
				</center>
				<br>
			</form>

		</div>
	</div>

	<!-- src=" http://cdnjs.cloudflare.com/ajax/libs/ace/1.1.3/ace.js" https://rawgithub.com/ajaxorg/ace-builds/master/src-noconflict/ace.js-->
	<div id="failurePrompt">
	<div id="internalText">
	<br>

	<div id="progressbar"><div id="label">${requestScope["currentTask"]} / ${requestScope["totalTasks"]}</div></div>
		<br>
			<input type="button"  value="Quit" onclick='quitConfirm()' style="float:right">
			<span class="sectionTitle"><b>Please take a look at the following problem, the code below it, and answer the questions.</span></b><br><br>
			<div id="internalText">
			<table class="fixed" CELLPADDING="4px" align="center"  style="word-wrap: break-word;">
				<col width="210px">
				<col width="410px">
				<tr>
					<td><b>We ran the following <u>test</u>:</b></td>
					<td><div id="testFailure"> </div></td>
				</tr>
				<tr height="20px"></tr>
				<tr>
					<td><b> But we received this <u>failure:</u></b></td>
					<td><code>${requestScope["bugReport"]}</code></td>
				</tr>
			</table>
	<br>
	
	</div>
	</div>


	
	<div id="questionPrompt">
	<div id="internalText">
	<br>
			<span class="sectionTitle"><b>${requestScope["question"]}</b></span>
			<form name="answerForm" method="get">
	
			<table cellspacing='0' cellpadding='0'  align="center"> 	 	
				<col width="220px" />
				<col width="220px" />
				<col width="220px" />
			<tr>
			<td style="text-align: left"><label><input type="radio" name="answer" value="2" />I don't know</label></td>
			<td><label><input type="radio" name="answer" value="1" />Yes, there is an issue </label></td>
			<td style="text-align: right"><label><input type="radio" name="answer" value="3" />No, there is not an issue</label></td>
		</tr>
			</table>
			
		<br>
	
			<table cellspacing='0' cellpadding='0' align="center"> 	
   				<col width="270px" />
    			<col width="70px" />
    			<col width="70px" />
    			<col width="70px" />
    			<col width="70px" />
    			<col width="70px" />
    			<tr>
						<td><b>How confident you are in your answer?</b></td>
						<td style="text-align: right">Very</td>
						<td></td>
						<td></td>
						<td></td>
						<td style="text-align: right">Not</td>
				</tr>
				<tr>
					<td></td>

					<td style="text-align: right"><label><input type="radio" name="confidence"value="5" />5</label></td>
					<td style="text-align: right"><label><input type="radio" name="confidence" value="4" />4</label></td>
					<td style="text-align: right"><label><input type="radio" name="confidence" value="3" />3</label></td>
					<td style="text-align: right"><label><input type="radio" name="confidence" value="2" />2</label></td>
					<td style="text-align: right"><label><input type="radio"	name="confidence" value="1" />1</label></td>
							
				</tr>
			</table>
			
			
				<!-- Hidden fields -->
				<input type="hidden" id="hiddenDifficulty" name="difficulty" value="">
				<input type="hidden"
					name="workerId" id="workerId" value=${requestScope["workerId"]}> 
				<input type="hidden" name="microtaskId"
					value=${requestScope["microtaskId"]}> <input type="hidden"
					name="timeStamp" value=${requestScope["timeStamp"]}>
				
			
			<table cellspacing='0' cellpadding='0'  align="center"> 	
				<col width="620px" />
			<tr>
				<td align="left"><b>Please provide an explanation:</b></td>
			</tr>
			<tr>
				<td align="center"><textarea name="explanation" id="explanation" rows="4" cols="77" style="margin: 0px; width: 614px; height: 45px;"></textarea></td>
			</tr>
			</table>
	<br>
			<table cellspacing='0' cellpadding='0'  align="center"> 		
				<col width="310px" />
				<col width="310px" />
				<tr>
					<td align="center"><input TYPE="button" name="answerButton" id="answerButton" VALUE="Submit" onclick="submitAnswer()"></td>
				</tr>
			</table>				
			<br>
			</form>	

</div>
	</div>
	
	
   
	<div id="questionCode">
		<div id="internalText">
		<br>
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