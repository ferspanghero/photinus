
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

function onload(){
	var diffForm = document.forms["difficultyForm"];
	diffForm.getElementById("difficultyButton").focus;
}


</script>


<body onload="onload();">
<!-- Dialog Content -->
<input type="hidden" autofocus="autofocus" />
		<br>
</body>
	