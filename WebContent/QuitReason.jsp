<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Exit Page</title>
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
	text-align: left;
	margin: 0 auto;
}

#thumbs a {
	vertical-align: top;
	display: inline-block;
	*display: inline;
	zoom: 1;
}
</style>
</head>

<body>

	<script>
		function checkAnswer() {

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
					document.forms["reasonForm"].submit();
					open("QuitReason.jsp", '_self').close();

				} else {
					//nothing to do.
				}
			}
		}
	</script>

		<script	src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		
		<script	src="http://cdnjs.cloudflare.com/ajax/libs/ace/1.1.3/ace.js"></script>

<!-- src=" http://cdnjs.cloudflare.com/ajax/libs/ace/1.1.3/ace.js" https://rawgithub.com/ajaxorg/ace-builds/master/src-noconflict/ace.js-->

	<div id="thumbs">
		<div id="internalText">
			<form name="reasonForm" action="quit" method="get">

				<label>
					<b>Why did you decide to quit ?</b>
				</label>
				<br>
					<br>
					<input type="radio" name="reason" value="hard" />The task is too hard &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp;
					<br>
					<input type="radio" name="reason" value="boring" />The task is too boring &nbsp;	&nbsp; &nbsp; &nbsp;&nbsp; &nbsp;
					<br>
					<input type="radio" name="reason" value="long" />The task is too long &nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;
					<br>					
					<input type="radio" name="reason" value="other" />Other&nbsp;	&nbsp; &nbsp; &nbsp;&nbsp; &nbsp;
					<br>


				<!-- Hidden fields -->
				<input type="hidden" id="sessionId" name="sessionId"
					value=${requestScope["sessionId"]}>
				<input type="hidden"
					name="workerId" value=${requestScope["workerId"]}>
				<input type="hidden" name="microtaskId"
					value=${requestScope["microtaskId"]}>
				<input type="hidden"
					name="timeStamp" value=${requestScope["timeStamp"]}> 
				<center>
					<INPUT TYPE="button" name="reasonButton" id="reasonButton" VALUE="Submit answer"
					onclick="submitAnswer(event)">
				</center>
				<br>
			</form>
			
		</div>
	</div>
</body>
</html>