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
	max-width: 800px;
	background-color: #D1EEEE;
	text-align: justify;
	margin: 0 auto;
}

#content {
	background-color: #D1EEEE;
	text-align: justify;
	margin-left: 80px;
	margin-right: 80px;
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

			var exp = document.getElementsByName("experience");
			var expOption = -1;
			var i = 0;

			for (i = 0; i < exp.length; i++) {
				if (exp[i].checked) {
					expOption = i;
					break;
				}
			}
			
			if (expOption===4){//if user selected other
				if(document.getElementById("otherexperience").value==""){
					alert("Please fill the experience field!")
					return false;
				}
			}
			if (expOption === -1) {
				alert("Please select a level of experience .");
				return false;
			}				
			
			var yearsField = document.getElementById("years");
			yearsField.value = yearsField.value.trim();
			if (!yearsField.value) {
				alert("Please enter your years of experience.");
				return false;
			} else if (isNaN(yearsField.value)) {
				alert("Years of experience must be number.");
				return false;
			}
			
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
				return false;
			}

			var ageField = document.getElementById("age");
			ageField.value = ageField.value.trim();
			if (!ageField.value) {
				alert("Please enter your age.");
				return false;
			} else if (isNaN(ageField.value)) {
				alert("Age must be number.");
				return false;
			}

			var countryField = document.getElementById("country");
			countryField.value = countryField.value.trim();
			if (!countryField.value) {
				alert("Please enter your country of residence.");
				return false;
			}
			return true;
		}

		var formAlreadyPosted = false;

		function submitAnswer() {
			//first thing is to check whether the form was already submitted
			if (formAlreadyPosted) {
				alert("Please wait. If it is taking more time than expected, please send an email to the requester.");
			} else {
				if (checkAnswers() && getCheckBoxValues()) {
					formAlreadyPosted = true;
					return true;
					//var form = document.forms["surveyForm"];
					//form.action = "survey";
					//form.submit();
				} else {
					return false;
					//nothing to do.
				}
			}
		}
		
		function getCheckBoxValues(){
			var chkArray = [];
			
			/* look for all checkboes that have a parent id called 'checkboxlist' attached to it and check if it was checked */
			$("#learnedDiv input:checked").each(function() {
				if($(this).val()=="Other"){
					chkArray.push("Other");
					if(document.getElementById("otherlearned").value == ""){
						//alert("Please fill all the fields!");
						return false;
					}else{
						var index = chkArray.indexOf("Other");
						chkArray[index]="Other "+document.getElementById("otherlearned").value;
					}
				}else{
					chkArray.push($(this).val());
				}
			});
						
			/* we join the array separated by the comma */
			var selected;
			selected = chkArray.toString();
			if(selected == "Other"){
				alert("Please fill where did you learn how to code!");
				return false;
			}
			/* check if there is selected checkboxes, by default the length is 1 as it contains one single comma */
			if(chkArray.length < 1){
				alert("Please select at least one of the checkbox");
				return false;
			}
			
			document.getElementById("hlearned").value = ''+selected;
			return true;
			
		}
	</script>	

	<div id="container">
		<br>
		<div id="content">
			<b>Thank you for your interest in CrowdDebug and for helping us localize faults in software from all over the world.<br><br>
			
			Please let us know a little bit more about you. This will help us to design future tasks.</b>




			<form name="surveyForm" method="get" action="survey" onsubmit="return submitAnswer();">
 
				 <!--  EXPERIENCE QUESTIONS -->
		
				<br> I am currently a:</b><br>	
				<input type="radio" name="experience" value="1">Professional developer<br>
				<input type="radio" name="experience" value="2">Graduate student<br>
				<input type="radio" name="experience" value="3">Undergraduate student<br>
				<input type="radio" name="experience" value="4">Hobbyist<br>
				<input type="radio" name="experience" value="5">Other <input type="text" id="otherexperience" name="otherexperience" size="15">
				<br>				
				<br>How many years have you been developing software?
				<input type="text" id="years" name="years" size="2"><br>
				<div id="learnedDiv">			
   				<br>Where did you learn to develop software (mark all that apply)?<br>
   				<input type="hidden" id="hlearned" name="hlearned" value="">
   				<input type="checkbox" name="learned" value="High School" /> High school<br>
   				<input type="checkbox" name="learned" value="University"/> College/University<br>
   				<input type="checkbox" name="learned" value="Web" /> On the web<br>
   				<input type="checkbox" name="learned" value="Other" /> Other 
   				<input type="text" id="otherlearned" name="otherlearned" />
   				</div>
   				<!--  DEMOGRAPHIC QUESTIONS  -->
   				
   					<br><br> What is your gender?<br> &nbsp;&nbsp;<input
					type="radio" name="gender" value="Female">Female<br>
				&nbsp;&nbsp;<input type="radio" name="gender" value="Male">Male<br>
				&nbsp;&nbsp;<input type="radio" name="gender" value="Other">Other<br>
				&nbsp;&nbsp;<input type="radio" name="gender"
					value="Prefer not to tell">Prefer not to tell<br>
				<br> What is your age?</b> <input type=text name="age" id="age"
					size="2" /><br>
				<br> What is your country of residence?</b> <input type=text
					name="country" id="country" size="25" /><br>
   				

   				<br><br>

				<!-- Hidden fields -->
			    <input type="hidden"
					name="workerId" value=${requestScope["workerId"]}> 

				<br>
				
				<center> <INPUT TYPE="submit" name="surveySubmit" VALUE="Submit answer">
				</center>
			</form>
		</div>
		<br>
	</div>
</body>
</html>