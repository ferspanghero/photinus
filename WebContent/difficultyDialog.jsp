<script type="text/javascript">


function checkAnswers() {

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

var surveyFormAlreadyPosted = false;

function submitSurveyAnswer() {
	//first thing is to check whether the form was already submitted
	if (surveyFormAlreadyPosted) {
		alert("Please wait. If it is taking more time than expected, please send an email to the requester.");
	} else {
		var checked = checkAnswers();
		if (checked != -1) {
			surveyFormAlreadyPosted = true;
			var form = document.forms["surveyForm"];
			form.action = "survey";
			form.submit();
			$("#survey").dialog('close');
		} else {
			//nothing to do.
		}
	}
}
</script>


<body>
<!-- Dialog Content -->
	<div id="survey">
		<div id="content">

			<form name="surveyForm" method="get" action="survey">
			<center>
				<table>
					<tr>
					<td><center>Very difficult</center></td>
					<td></td>
					<td></td>
					<td></td>
					<td><center>Not difficult</center></td>
					</tr>
					
					<tr>
					<td><a><center><input type="radio" name="difficulty" value="5" />5</center></a></td>
					<td><a><center><input type="radio" name="difficulty" value="4" />4</center></a></td>
					<td><a><center> <input type="radio" name="difficulty" value="3" />3</center></a></td>
					<td><a><center><input type="radio" name="difficulty" value="2" />2</center></a></td>
					<td><a><center><input type="radio" name="difficulty" value="1" />1</center></a></td>
					</tr>
					</table> 
			</center>
			
				<!-- Hidden fields -->
				<input type="hidden" name="sessionId"
					value=${requestScope["sessionId"]}> <input type="hidden"
					name="workerId" value=${requestScope["workerId"]}> <input
					type="hidden" name="hitId" value=${requestScope["hitId"]}>

				<br>
				<center>
				 <INPUT TYPE="button" VALUE="Submit answer"
					onclick="submitSurveyAnswer(event)">
				</center>
			</form>
		</div>
		<br>
	</div>
</body>
	