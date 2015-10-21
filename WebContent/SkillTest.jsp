<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Java Skill Test Page</title>
<link rel="stylesheet" href="jquery/jquery-ui-1.10.4.custom.min.css">
  <script src="jquery/jquery-1.10.2.js"></script>
  <script src="jquery/jquery-ui-1.10.4.custom.min.js"></script>
  <script	src="jquery/quitDialog.js"></script>
  <script	src="http://cdnjs.cloudflare.com/ajax/libs/ace/1.1.3/ace.js"></script>

<style type="text/css" media="screen">
	#container  {
		max-width: 700px;
		background-color: #D1EEEE;
		text-align: justify;
		margin: 0 auto;
	}
	
	#content  {
		background-color: #D1EEEE;
		text-align: justify;
		 margin-left: 10px;
		margin-right: 10px;
	}
	
	#sourceCode {
	position: relative;
	height: 385px;
	width: 580px;
	}
	
	
.ui-dialog-titlebar-close {
  visibility: hidden;
}
</style>
</head>

<body>
	<script>
	function isEmpty(value) {
			return (value.length === 0 || !value.trim());
		}

		function isSelected(question, number) {
			var option = -1;
			for (var i = 0; i < question.length; i++) {
				if (question[i].checked) {
					option = i;
					break;
				}
			}
			if (option == -1) {
				alert("Please answer question " + number + ".");
				return false;
			} else
				return true;
		}

		function checkAnswers() {

			if (!isSelected(document.getElementsByName("QUESTION1"), 1))
				return false;

			if (!isSelected(document.getElementsByName("QUESTION2"), 2))
				return false;

			if (!isSelected(document.getElementsByName("QUESTION3"), 3))
				return false;

			if (!isSelected(document.getElementsByName("QUESTION4"), 4))
				return false;

			//if (!isSelected(document.getElementsByName("question5"), 5))
			//	return false;

			return true;
		}

		var formAlreadyPosted = false;
		console.log("here");
		function submitAnswers() {
			//first thing is to check whether the form was already submitted
			if (formAlreadyPosted) {
				alert("Please wait. If it is taking more time than expected, please send an email to the requester.");
			} else {
				//ok, form was not submitted yet
				var checked = checkAnswers();
				if (checked) {
					var subAction = document.getElementById("subAction");
					subAction.value = "gradeAnswers";
					formAlreadyPosted = true;
					var form = document.forms["testForm"];
					form.action='skillTest';
					form.submit();
				} else {
					//nothing to do.
				}
			}
		}

		function quit() {
			if (confirm('Confirm quitting the study ?')) {
				//window.close()
			}
		}
	</script>
	

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
				 <input type="hidden"
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
	
<div id="container"><br>
	<div id="content">
		<b>Before we allow you to continue, we need to evaluate your 
		programming skills. Please answer the following questions. </b>
	
	
		<form name="testForm" method="get" action="skillTest">
			<input type="hidden" name="workerId" value=${requestScope["workerId"]}> 
			<input type="hidden" id="subAction" name="subAction" value=${requestScope["subAction"]}> 
			<input type="hidden" id="timeStamp" name="timeStamp" value=${requestScope["timeStamp"]}>
			<input type="hidden" id="isTest" name="isTest" value="false">
			<c:if test="${not empty requestScope.sourceCode}">
				<br> The source code below is used for all the questions below.<br>			
				<div id="sourceCode"><xmp>${requestScope["sourceCode"]}</xmp></div>
			</c:if>
				<script>
					//Editor for Questions 1, 2, and 3
					var editor1 = ace.edit("sourceCode");
					editor1.setReadOnly(true);
					editor1.setTheme("ace/theme/github");
					editor1.getSession().setMode("ace/mode/java");
					editor1.setBehavioursEnabled(false);
					editor1.setOption("highlightActiveLine", false); // disable highligthing on the active line
					editor1.setShowPrintMargin(false); // disable printing margin
				</script>
			
			<br>
			<c:forEach items="${requestScope['questions']}" var="question" varStatus="Counter">
					${Counter.count} - ${question}
					<br>
					<c:forEach items="${requestScope.options[Counter.index]}" var="alternative" varStatus="AltCounter">
						<input type="radio" name="QUESTION${Counter.count}" value="&#${AltCounter.index % 26 + 97};">${alternative}<br>
					</c:forEach>

					<br>
			</c:forEach>
			<input type="button"  value="Quit" onclick='quitConfirm()'>
			<INPUT TYPE="button" name="answerButton" VALUE="Submit answers" onclick="submitAnswers(event)">

		</form>
		<br>
		

	</div>
</div>

</body>
</html>