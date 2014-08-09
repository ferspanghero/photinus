<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Java Skill Test</title>

<style type="text/css" media="screen">
	#container  {
		max-width: 700px;
		background-color: #D1EEEE;
		text-align: justify;
		margin: 0 auto;
		
	}
	
	#content  {
		background-color: #D1EEEE;
		text-align: justify;
		 margin-left: 10px;
		margin-right: 10px;
	}
	
	#editor1 {
	position: relative;
	height: 280px;
	width: 580px;
	}
	
	#editor2 {
	position: relative;
	height: 500px;
	width: 580px;
	}
	
}
</style>
</head>

<body>
	<script>
		function isEmpty(value) {
			return (value.length === 0 || !value.trim());
		}

		function isSelected(question, number) {
			var option = -1;
			for (var i = 0; i < question.length; i++) {
				if (question[i].checked) {
					option = i;
					break;
				}
			}
			if (option == -1) {
				alert("Please answer question " + number + ".");
				return false;
			} else
				return true;
		}

		function checkAnswers() {

			if (!isSelected(document.getElementsByName("question1"), 1))
				return false;

			if (!isSelected(document.getElementsByName("question2"), 2))
				return false;

			if (!isSelected(document.getElementsByName("question3"), 3))
				return false;

			if (!isSelected(document.getElementsByName("question4"), 4))
				return false;

			if (!isSelected(document.getElementsByName("question5"), 5))
				return false;

			return true;
		}

		function submitAnswers() {
			var checked = checkAnswers(); 
			if (checked) {
				var subAction = document.getElementById("subAction");
				subAction.value = "gradeAnswers";
				document.forms["testForm"].submit();
			} else {
				//nothing to do.
			}
		}

		function quit() {
			if (confirm('Confirm quitting the study ?')) {
				window.open('', '_self', '');
				window.close();
			}
		}
	</script>
	
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	<script
		src="https://rawgithub.com/ajaxorg/ace-builds/master/src-noconflict/ace.js"></script>
		
<div id="container"><br>
	<div id="content">
		<b>Thank you for interest to participate the study. We need to evaluate your 
		programming skills before we allow you to continue. Please answer the following questions. </b>
	
	
		<form name="testForm" method="get" action="skillTest">
			<input type="hidden" name="userId" value=${requestScope["userId"]}> 
			<input type="hidden" name="hitId" value=${requestScope["hitId"]}> 
			<input type="hidden" id="subAction" name="subAction" value=${requestScope["subAction"]}> 
			
			<br> The source code below is used in the questions 1, 2, and 3<br>
			<div id="editor1"><xmp>${requestScope["editor1"]}</xmp></div>

				<script>
					//Editor for Questions 1, 2, and 3
					var editor1 = ace.edit("editor1");
					editor1.setReadOnly(true);
					editor1.setSelection(false);
					editor1.setTheme("ace/theme/github");
					editor1.getSession().setMode("ace/mode/java");
					editor1.setBehavioursEnabled(false);
					editor1.setOption("highlightActiveLine", false); // disable highligthing on the active line
					editor1.setShowPrintMargin(false); // disable printing margin
				</script>

			<br>
			1- What is the output of the code?
			<br>
			<input type="radio" name="question1" value="a">0<br>
			<input type="radio" name="question1" value="b">1<br>
			<input type="radio" name="question1" value="c">2<br>
			<input type="radio" name="question1" value="d">3<br>
			
			<br>
			2- At line 20, if we substitute "Ciao" for "Hola", what would be the output? 
			<br>
			<input type="radio" name="question2" value="a">0<br>
			<input type="radio" name="question2" value="b">1<br>
			<input type="radio" name="question2" value="c">2<br>
			<input type="radio" name="question2" value="d">3<br>

			<br>
			3- At line 20, if we substitute "Ciao" for an empty String (e.g., " "), what would be the output? 
			<br>
			<input type="radio" name="question3" value="a">0<br>
			<input type="radio" name="question3" value="b">1<br>
			<input type="radio" name="question3" value="c">2<br>
			<input type="radio" name="question3" value="d">3<br>
			
			<br> The source code below is used in the questions 4 and 5<br>
			<div id="editor2"><xmp>${requestScope["editor2"]}</xmp></div>

				<script>
					//Editor for Questions 4 and 5
					var editor2 = ace.edit("editor2");
					editor2.setReadOnly(true);
					editor2.setSelection(false);
					editor2.setTheme("ace/theme/github");
					editor2.getSession().setMode("ace/mode/java");
					editor2.setBehavioursEnabled(false);
					editor2.setOption("highlightActiveLine", false); // disable highligthing on the active line
					editor2.setShowPrintMargin(false); // disable printing margin
				</script>

			<br>
			4- What is the output of the code?
			<br>
			<input type="radio" name="question4" value="a">2!1!0BA;<br>
			<input type="radio" name="question4" value="b">2!1!0AB;<br>
			<input type="radio" name="question4" value="c">012!!AB;<br>
			<input type="radio" name="question4" value="d">OAB1!2!;<br>
			
			<br>
			5- If we remove the "else" clause at line 28, what would be the new output of the code?
			<br>
			<input type="radio" name="question5" value="a">Exception<br>
			<input type="radio" name="question5" value="b">;<br>
			<input type="radio" name="question5" value="c">2!1Exception<br>
			<input type="radio" name="question5" value="d">OAB1!2Exception<br><br>

			<INPUT TYPE="button" VALUE="Quit"	onclick="quit()"> 
			<INPUT TYPE="button" VALUE="Submit answers" onclick="submitAnswers(event)">

		</form>
		<br>
	</div>
</div>

</body>
</html>