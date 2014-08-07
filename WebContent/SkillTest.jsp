<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Java Skill Test</title>

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
		function isEmpty(value) {
	 	   return (value.length === 0 || !value.trim());
		}
		
		function isSelected(question, number){
			var option = -1; 
			for (var i = 0; i < question.length; i++) {
				if (question[i].checked) {
					option = i;
					break;
				}
			}
			if (option == -1) {
				alert("Please answer question "+ number +".");
				return -1;
			}
			else
				return 1;
		}
	
		
		function checkAnswers() {
			
			if(!isSelected(document.getElementsByName("question1"),1))
				return -1;
			else
				if(!isSelected(document.getElementsByName("question2"),2))
					return -1;
				else
					if(!isSelected(document.getElementsByName("question3"),3))
						return -1;
					else
						if(!isSelected(document.getElementsByName("question4"),4))
							return -1;
						else
							if(!isSelected(document.getElementsByName("question5"),5))
								return -1;
							else
								return 1;
		}
		
		
		function submitAnswers() {
			var checked = checkAnswers();
			if (checked != -1) {
				document.forms["testForm"].submit();
			} else {
				//nothing to do.
			}
		}
		
		
		function quit(){
			if(confirm('Confirm quitting the study ?')){
				window.open('', '_self', ''); window.close();
			}
		}
		
		
	</script>

<div id="container"><br>
	<div id="content">
		<b>Thank you for interest to participate the study. We need to evaluate your 
		programming skills before we allow you to continue. Please answer the following questions. </b>
	
	
		<form name="testForm" method="get" action="skillTest">
			<input type="hidden" name="userId" value=${requestScope["userId"]}> 
			<input type="hidden" name="hitId" value=${requestScope["hitId"]}> 
			<input type="hidden" id="subAction" name="subAction" value=${requestScope["subAction"]}> 
			
			<br> The source code below is used in the questions 1 and 2<br>
			<i> SOURCE CODE </i>
			<br>
			Question-1
			<br><br>
			<input type="radio" name="question1" value="a">a<br>
			<input type="radio" name="question1" value="b">b<br>
			<input type="radio" name="question1" value="c">d<br>
			<input type="radio" name="question1" value="d">I don't know<br><br>
			
			<br>
			<br>
			Question-2
			<br><br>
			<input type="radio" name="question2" value="a">a<br>
			<input type="radio" name="question2" value="b">b<br>
			<input type="radio" name="question2" value="c">d<br>
			<input type="radio" name="question2" value="d">I don't know<br><br>

			<br> The source code below is used in the questions 3, 4, and 5<br>
			<i> SOURCE CODE </i>
			
			<br>
			Question-3
			<br><br>
			<input type="radio" name="question3" value="a">a<br>
			<input type="radio" name="question3" value="b">b<br>
			<input type="radio" name="question3" value="c">d<br>
			<input type="radio" name="question3" value="d">I don't know<br><br>
			
			
			<br>
			Question-4
			<br><br>
			<input type="radio" name="question4" value="a">a<br>
			<input type="radio" name="question4" value="b">b<br>
			<input type="radio" name="question4" value="c">d<br>
			<input type="radio" name="question4" value="d">I don't know<br><br>
			
			<br>
			Question-5
			<br><br>
			<input type="radio" name="question5" value="a">a<br>
			<input type="radio" name="question5" value="b">b<br>
			<input type="radio" name="question5" value="c">d<br>
			<input type="radio" name="question5" value="d">I don't know<br><br>

			<INPUT TYPE="button" VALUE="Quit"	onclick="quit()"> 
			<INPUT TYPE="button" VALUE="Submit answers" onclick="submitAnswers(event)">

		</form>
</div>
</div>

</body>
</html>