
<style>
label {
	padding-right: 138px;
}

span {
	padding-right: 8px;
	padding-left: 8px;
}

.box {
	width: 450px;
}
</style>
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
			<form name="surveyForm" method="get" action="survey">
				<table>
					<tr>
						<td><label>Very
							difficult</label> <label>Not
							difficult</label></td>
					</tr>
					<tr align="center">
						<td><span><input type="radio" name="difficulty"
							value="5" />5</span><span><input type="radio" name="difficulty" value="4" />4</span>
							<span><input type="radio" name="difficulty" value="3" />3</span><span><input
							type="radio" name="difficulty" value="2" />2</span><span><input type="radio"
							name="difficulty" value="1" />1</span></td>
					</tr>
				</table>

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
		<br>
</body>
	