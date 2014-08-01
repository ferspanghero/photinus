<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Survey</title>

<style type="text/css" media="screen">
#myDiv {
	max-width: 700px;
	background-color: #FFFAEB
}
</style>

</head>


<body>

	<script>
		function checkAnswers() {
			var radiosLearnedHow = document.getElementsByName('learnedHow');

			var learnedOption = -1;
			var i = 0;

			for (i = 0; i < radiosLearnedHow.length; i++) {
				if (radiosLearnedHow[i].checked) {
					learnedOption = i;
					break;
				}
			}

			var radiosCodingYears = document.getElementsByName('codingYears');
			var codingYearsOption = -1;
			for (i = 0; i < radiosCodingYears.length; i++) {
				if (radiosCodingYears[i].checked) {
					codingYearsOption = i;
					break;
				}
			}

			var radiosWorking = document
					.getElementsByName('workingAsProgrammer');
			var workingOption = -1;
			for (i = 0; i < radiosWorking.length; i++) {
				if (radiosWorking[i].checked) {
					workingOption = i;
					break;
				}
			}

			if (learnedOption == -1) {
				alert("Please select the option you used to learn computer programming.");
				return -1;
			} else if (codingYearsOption == -1) {
				alert("Please select the option of number of years of coding.");
				return -1;
			} else if (workingOption == -1) {
				alert("Please select the option of whether you are working as a programmer.");
				return -1;
			} else
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
	<b>Your final task is to answer the following survey. After
		that the HIT will be considered completed.</b>


	<form name="surveyForm" method="get" action="survey">
		
		<br>
		
		How many years of computer programming do you have?
		<ul>
		<li><a id="codingYears1"> <input type="radio" name="codingYears" value="1">Less than 1 year</a></li> 
		<li><a id="codingYears1"> <input type="radio" name="codingYears" value="2">Between 1 and 2 years</a></li>
		<li><a id="codingYears1"> <input type="radio" name="codingYears" value="2">Between 2 and 3 years</a> </li>
		<li><a id="codingYears1"> <input type="radio" name="codingYears" value="3">More than 3 years</a> </li>
		</ul>

		<br> 
		
		How did you learn how to program?   
		<ul>
			<li><a id="learned1"> <input type="radio" name="learnedHow" value="1">High school course</a></li> 
			<li><a id="learned2"> <input type="radio" name="learnedHow" value="2">University course</a> </li>
			<li><a id="learned3"> <input type="radio" name="learnedHow" value="3">Web-based course</a> </li>
			<li><a id="learned4"> <input type="radio" name="learnedHow" value="4"> Books and Web references</a></li>  
		</ul>
		<br>

 		Do you currently work as a computer programmer?
 		<ul>
 		<li><a id="workYes"> <input type="radio" name="workingAsProgrammer" value="1">Yes</a></li>
 		<li><a id="workDont"> <input type="radio" name="workingAsProgrammer" value="2">No</a></li>
 		</ul>
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