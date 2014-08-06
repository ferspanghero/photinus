<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Survey</title>

<style type="text/css" media="screen">

#myDiv  {
	max-width: 700px;
	background-color: #D1EEEE;
	 text-align: justify;
	 margin: 0 auto;
	 text-justify: distribute-all-lines;
}
</style>

</head>


<body>

	<script>
	
		function isEmpty(value) {
	 	   return (value.length === 0 || !value.trim());
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
			if (genderOption == -1) {
				alert("Please select a gender option.");
				return -1;
			}
			
			if(isEmpty(document.getElementsByName("age"))){
				alert("Please enter your age.");
				return -1;
			}
			
			if(isEmpty(document.getElementsByName("experience"))){
				alert("Please enter your years of experience.");
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
	
<div id="myDiv">
	<b>Your final task is to answer the following survey. After that the HIT will be considered completed.</b>


	<form name="surveyForm" method="get" action="survey">
		
		<br>
		
		What is your gender?
		<ul>
		<li><a id="Female"> <input type="radio" name="gender" value="1">Female</a></li> 
		<li><a id="Male"> <input type="radio" name="gender" value="2">Male</a></li>
		<li><a id="Other"> <input type="radio" name="gender" value="3">Other</a> </li>
		<li><a id="PreferNotTell"> <input type="radio" name="gender" value="4">Prefer not to tell</a> </li>
		</ul>
		
		What is your age?
		<input type=text name="age" id="age" />
		
		How many years of programming experience do you have?
		<input type=text name="experience" id="experience" />
		
		<br> 
		
		On a scale of difficulty from 1(easy) to 7(hard), how would you rank this task?
		<ul>
		<li><a id="difficulty"> <input type="radio" name="difficulty" value="1">1</a></li> 
		<li><a id="difficulty"> <input type="radio" name="difficulty" value="2">2</a></li>
		<li><a id="difficulty"> <input type="radio" name="difficulty" value="3">3</a> </li>
		<li><a id="difficulty"> <input type="radio" name="difficulty" value="4">4</a> </li>
		<li><a id="difficulty"> <input type="radio" name="difficulty" value="5">5</a> </li>
		<li><a id="difficulty"> <input type="radio" name="difficulty" value="6">6</a> </li>
		<li><a id="difficulty"> <input type="radio" name="difficulty" value="7">7</a> </li>
		</ul>
		

		<br>
		Please provide any additional feedback:
		<textarea name="feedback" id="feedback" rows="6" cols="50"></textarea> 

 		<!-- Hidden fields -->
 		<input type="hidden" name="sessionId" value=${requestScope["sessionId"]}> 
		<input type="hidden" name="userId" value=${requestScope["userId"]}> 
		<input type="hidden" name="hitId" value=${requestScope["hitId"]}> 
 
 		<br> 
		<INPUT TYPE="button" VALUE="Submit Answer" onclick="submitAnswer(event)">
 
	</form>


</div>
</body>
</html>