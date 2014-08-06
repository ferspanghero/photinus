<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
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


</body>
</html>