<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Survey Page</title>
<link rel="stylesheet" href="jquery/jquery-ui-1.10.4.custom.min.css">
<style type="text/css" media="screen">
#container {
	max-width: 700px;
	background-color: #D1EEEE;
	text-align: justify;
	margin: 0 auto;
}

#content {
	background-color: #D1EEEE;
	text-align: justify;
	margin-left: 100px;
	margin-right: 100px;
}
</style>

</head>
  <script src="jquery/jquery-1.10.2.js"></script>
  <script src="jquery/jquery-ui-1.10.4.custom.min.js"></script>
  <script	src="jquery/quitDialog.js"></script>

<body>

	<script>
		function quit() {
			if (confirm('Confirm quitting the study ?')) {
				formAlreadyPosted=true;
				window.close();
			}
		}
		function checkAnswers() {

			var gender = document.getElementsByName("gender");
			var genderOption = -1;
			var i = 0;

			for (i = 0; i < gender.length; i++) {
				if (gender[i].checked) {
					genderOption = i;
					break;
				}
			}
			if (genderOption === -1) {
				alert("Please select a gender option.");
				return -1;
			}

			var ageField = document.getElementById("age");
			ageField.value = ageField.value.trim();
			if (!ageField.value) {
				alert("Please enter your age.");
				return -1;
			} else if (isNaN(ageField.value)) {
				alert("Age must be number.");
				return -1;
			}

			var countryField = document.getElementById("country");
			countryField.value = countryField.value.trim();
			if (!countryField.value) {
				alert("Please enter your country of residence.");
				return -1;
			}

			var experienceField = document.getElementById("experience");
			experienceField.value = experienceField.value.trim();
			if (!experienceField.value) {
				alert("Please enter your years of programming experience.");
				return -1;
			} else if (isNaN(experienceField.value)) {
				alert("Years of experience must be a number.");
				return -1;
			}

		}

		var formAlreadyPosted = false;

		function submitAnswer() {
			//first thing is to check whether the form was already submitted
			if (formAlreadyPosted) {
				alert("Please wait. If it is taking more time than expected, please send an email to the requester.");
			} else {
				var checked = checkAnswers();
				if (checked != -1) {
					formAlreadyPosted = true;
					var form = document.forms["surveyForm"];
					form.action = "survey";
					form.submit();
				} else {
					//nothing to do.
				}
			}
		}
	</script>

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
	

	<div id="container">
		<br>
		<div id="content">
			<b>Your final task is to answer the following survey. After that
				the HIT will be considered completed.</b>


			<form name="surveyForm" method="get" action="survey">
 
				<br> What is your gender?<br> &nbsp;&nbsp;<input
					type="radio" name="gender" value="Female">Female<br>
				&nbsp;&nbsp;<input type="radio" name="gender" value="Male">Male<br>
				&nbsp;&nbsp;<input type="radio" name="gender" value="Other">Other<br>
				&nbsp;&nbsp;<input type="radio" name="gender"
					value="Prefer not to tell">Prefer not to tell<br>
				<br> What is your age?</b> <input type=text name="age" id="age"
					size="2" /><br>
				<br> What is your country of residence?</b> <input type=text
					name="country" id="country" size="25" /><br>
				<br> How many years of programming experience do you have?</b> <input
					type=text name="experience" id="experience" size="2" /><br>


				<!-- Hidden fields -->
				<input type="hidden" name="sessionId"
					value=${requestScope["sessionId"]}> <input type="hidden"
					name="workerId" value=${requestScope["workerId"]}> <input
					type="hidden" name="hitId" value=${requestScope["hitId"]}>

				<br>
				
				<input type="button"  value="Quit" onclick='showQuitDialog()'>
					&nbsp;&nbsp;
				 <INPUT TYPE="button" VALUE="Submit Answer"
					onclick="submitAnswer(event)">

			</form>
		</div>
		<br>
	</div>
</body>
</html>