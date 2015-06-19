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

.ui-dialog-titlebar-close {
  visibility: hidden;
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
			
			var exp = document.getElementsByName("experience");
			var expOption = -1;
			var i = 0;

			for (i = 0; i < exp.length; i++) {
				if (exp[i].checked) {
					expOption = i;
					break;
				}
			}
			if (expOption === -1) {
				alert("Please select a level of experience .");
				return -1;
			}
			
			var languageField = document.getElementById("language");
			languageField.value = languageField.value.trim();
			if (!languageField.value) {
				alert("Please enter a programming language.");
				return -1;
			}
			
			var yearsField = document.getElementById("years");
			yearsField.value = yearsField.value.trim();
			if (!yearsField.value) {
				alert("Please enter your years of experience.");
				return -1;
			} else if (isNaN(yearsField.value)) {
				alert("Years of experience must be number.");
				return -1;
			}

		}

		var formAlreadyPosted = false;

		function submitAnswer() {
			//first thing is to check whether the form was already submitted
			if (formAlreadyPosted) {
				alert("Please wait. If it is taking more time than expected, please send an email to the requester.");
			} else {
				if ((checkAnswers()!=-1) && getCheckBoxValues()) {
					formAlreadyPosted = true;
					var form = document.forms["surveyForm"];
					form.action = "survey";
					form.submit();
				} else {
					//nothing to do.
				}
			}
		}
		
		function getCheckBoxValues(){
			var chkArray = [];
			
			/* look for all checkboes that have a parent id called 'checkboxlist' attached to it and check if it was checked */
			$("#learnedDiv input:checked").each(function() {
				chkArray.push($(this).val());
			});
			
			chkArray.push(document.getElementById("otherlearned").value);
			
			/* we join the array separated by the comma */
			var selected;
			selected = chkArray.toString();
			
			/* check if there is selected checkboxes, by default the length is 1 as it contains one single comma */
			console.log(chkArray.length<1);
			if(chkArray.length <= 1){
				console.log("ïn");
				alert("Please at least one of the checkbox");
				return false;
			}
			
			document.getElementById("hlearned").value = ''+selected;
			var values = document.getElementById("hlearned").value;
			return true;
			
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
					<INPUT TYPE="button" name="reasonButton" id="reasonButton" onclick="submitQuitAnswer()"
						VALUE="Submit">
											<INPUT TYPE="button" VALUE="Cancel"
					onclick="quitDialog()">
				</center>
				<br>
			</form>

		</div>
	</div>
	

	<div id="container">
		<br>
		<div id="content">
			<b>Thank you for your interest in CrowdDebug and for helping us localize faults in software from all over the world.<br><br>
			
			Please let us know a little bit more about you. This will help us to design future tasks.</b>




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
				<br> What is your leve of experience?</b><br>	
				<input type="radio" name="experience" value="1">Professional developer<br>
				<input type="radio" name="experience" value="2">Graduate student<br>
				<input type="radio" name="experience" value="3">Undergraduate student<br>
				<input type="radio" name="experience" value="4">Hobbyist<br>
				<input type="radio" name="experience" value="5">Other <input type="text" name="otherexperience" size="15">
				<br>
				<br>Which programming language do you use most at present?
				<input type="text" id="language" name="language" size="25"><br>
				<br>How many years have you been programming?
				<input type="text" id="years" name="years" size="2"><br>
				<div id="learnedDiv">			
   				<br>Where did you learn to code (mark all that apply)<br>
   				<input type="hidden" id="hlearned" name="hlearned" value="">
   				<input type="checkbox" name="learned" value="High School"> High School<br>
   				<input type="checkbox" name="learned" value="University"> College/University<br>
   				<input type="checkbox" name="learned" value="Web"> In the web<br>
   				<input type="checkbox" name="learned" value="Other"> Other 
   				<input type="text" id="otherlearned"name="otherlearned">
   				</div>
   				<br><br>

				<!-- Hidden fields -->
				<input type="hidden" name="sessionId"
					value=${requestScope["sessionId"]}> <input type="hidden"
					name="workerId" value=${requestScope["workerId"]}> 

				<br>
				
				<input type="button"  value="Quit" onclick='quitConfirm()'>
					&nbsp;&nbsp;
				 <INPUT TYPE="button" VALUE="Submit answer"
					onclick="submitAnswer(event)">

			</form>
		</div>
		<br>
	</div>
</body>
</html>