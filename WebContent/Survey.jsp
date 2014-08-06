<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Survey</title>

<style type="text/css" media="screen">

	#content  {
		max-width: 700px;
		background-color: #D1EEEE;
		text-align: justify;
		margin: 0 auto;
	}
	
	#container  {
		background-color: #D1EEEE;
		text-align: justify;
	}

</style>

</head>


<body>

	<script>
	
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
			if(!ageField.value){
				alert("Please enter your age.");
				return -1;
			}
			
			var experienceField = document.getElementById("experience");
			experienceField.value = experienceField.value.trim();
			if(!experienceField.value){
				alert("Please enter your years of programming experience.");
				return -1;
			}
		 
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

		function submitAnswer() {
			var checked = checkAnswers();
			if (checked != -1) {
				document.forms["surveyForm"].submit();
			} else {
				//nothing to do.
			}
		}
	</script>
	
<div id="container"><br>
	<div id="content">
		<b>Your final task is to answer the following survey. After that the HIT will be considered completed.</b>
	
	
		<form name="surveyForm" method="get" action="survey">
			
			<br>
			
			What is your gender? <br><br>
			<input type="radio" name="gender" value="Female">Female<br>
			<input type="radio" name="gender" value="Male">Male<br>
			<input type="radio" name="gender" value="Other">Other<br>
			<input type="radio" name="gender" value="Prefer not to tell">Prefer not to tell<br><br>
			
			
			What is your age?<br>
			<input type=text name="age" id="age" /><br><br>
			
			How many years of programming experience do you have?<br>
			<input type=text name="experience" id="experience" /><br>
			
			<br> 
			
			On a scale of difficulty from 1(easy) to 7(hard), how would you rank this task?<br>
			<input type="radio" name="difficulty" value="1">1 
			<input type="radio" name="difficulty" value="2">2
			<input type="radio" name="difficulty" value="3">3
			<input type="radio" name="difficulty" value="4">4
			<input type="radio" name="difficulty" value="5">5
			<input type="radio" name="difficulty" value="6">6
			<input type="radio" name="difficulty" value="7">7<br>
	
			<br>
			Please provide any additional feedback: <br>
			<textarea name="feedback" id="feedback" rows="6" cols="50"></textarea><br>
	
	 		<!-- Hidden fields -->
	 		<input type="hidden" name="sessionId" value=${requestScope["sessionId"]}> 
			<input type="hidden" name="userId" value=${requestScope["userId"]}> 
			<input type="hidden" name="hitId" value=${requestScope["hitId"]}> 
	 
	 		<br> 
			<INPUT TYPE="button" VALUE="Submit Answer" onclick="submitAnswer(event)">
	 
		</form>

	</div><br>
</div>
</body>
</html>