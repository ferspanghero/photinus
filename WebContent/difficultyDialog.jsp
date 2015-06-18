
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


function checkDifficultyAnswers() {

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

var difficultyFormAlreadyPosted = false;

function submitDifficulty() {
	//first thing is to check whether the form was already submitted
	if (difficultyFormAlreadyPosted) {
		alert("Please wait. If it is taking more time than expected, please send an email to the requester.");
	} else {
		var checked = checkDifficultyAnswers();
		if (checked != -1) {
			difficultyFormAlreadyPosted = true;
			var form = document.forms["difficultyForm"];
			var dif = $("input[name=difficulty]:checked").val();
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

		<br>
</body>
	