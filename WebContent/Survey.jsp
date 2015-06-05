<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Survey Page</title>

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


<body>

	<script>
		function checkAnswers() {

// 			var gender = document.getElementsByName("gender");
// 			var genderOption = -1;
// 			var i = 0;

// 			for (i = 0; i < gender.length; i++) {
// 				if (gender[i].checked) {
// 					genderOption = i;
// 					break;
// 				}
// 			}
// 			if (genderOption === -1) {
// 				alert("Please select a gender option.");
// 				return -1;
// 			}

// 			var ageField = document.getElementById("age");
// 			ageField.value = ageField.value.trim();
// 			if (!ageField.value) {
// 				alert("Please enter your age.");
// 				return -1;
// 			} else if (isNaN(ageField.value)) {
// 				alert("Age must be number.");
// 				return -1;
// 			}

// 			var countryField = document.getElementById("country");
// 			countryField.value = countryField.value.trim();
// 			if (!countryField.value) {
// 				alert("Please enter your country of residence.");
// 				return -1;
// 			}

// 			var experienceField = document.getElementById("experience");
// 			experienceField.value = experienceField.value.trim();
// 			if (!experienceField.value) {
// 				alert("Please enter your years of programming experience.");
// 				return -1;
// 			} else if (isNaN(experienceField.value)) {
// 				alert("Years of experience must be a number.");
// 				return -1;
// 			}

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

		var formAlreadyPosted = false;

		function submitAnswer() {
			//first thing is to check whether the form was already submitted
			if (formAlreadyPosted) {
				alert("Please wait. If it is taking more time than expected, please send an email to the requester.");
			} else {
				var checked = checkAnswers();
				if (checked != -1) {
					formAlreadyPosted = true;
					document.forms["surveyForm"].submit();
				} else {
					//nothing to do.
				}
			}
		}
	</script>

	<div id="container">
		<br>
		<div id="content">
			<b>Your final task is to answer the following survey. After that
				the HIT will be considered completed.</b>


			<form name="surveyForm" method="get" action="survey">
<!-- 
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
					type=text name="experience" id="experience" size="2" /><br> -->
				<br> From 1(easy) to 7(hard), how do you rank this task? <br>
				&nbsp;&nbsp;<input type="radio" name="difficulty" value="1">1
				&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;<input type="radio"
					name="difficulty" value="2">2 &nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;<input type="radio" name="difficulty" value="3">3
				&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;<input type="radio"
					name="difficulty" value="4">4 &nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;<input type="radio" name="difficulty" value="5">5
				&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;<input type="radio"
					name="difficulty" value="6">6 &nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;<input type="radio" name="difficulty" value="7">7
				<br>
				<br> Please provide any additional feedback: <br>
				&nbsp;&nbsp;
				<textarea name="feedback" id="feedback" rows="6" cols="45"></textarea>
				<br>

				<!-- Hidden fields -->
				<input type="hidden" name="sessionId"
					value=${requestScope["sessionId"]}> <input type="hidden"
					name="workerId" value=${requestScope["workerId"]}> <input
					type="hidden" name="hitId" value=${requestScope["hitId"]}>

				<br> <INPUT TYPE="button" VALUE="Submit Answer"
					onclick="submitAnswer(event)">

			</form>
		</div>
		<br>
	</div>
</body>
</html>