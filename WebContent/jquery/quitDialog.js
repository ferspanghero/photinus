function checkQuitAnswer() {

	var radios = document.getElementsByName("reason");

	var option = -1;

	var i = 0;

	for (i = 0; i < radios.length; i++) {
		if (radios[i].checked) {
			option = i;
			break;
		}
	}

	if (option == -1) {
		alert("Please select an answer.");
		return -1;
	}
}

var quitFormAlreadyPosted = false;

function submitQuitAnswer() {
	//first thing is to check whether the form was already submitted
	if (quitFormAlreadyPosted) {
		alert("Please wait. If it is taking more time than expected, please send an email to the requester.");
	} else {
		//ok, form was not submitted yet
		var checked = checkQuitAnswer();
		if (checked != -1) {
			//$("#quit").on( "dialogclose", function() { 
			var form = document.forms["reasonForm"];
			form.action = "quit";
			form.submit();
			$('#reasonForm').submit( function() {
				$("#quit").dialog("close");
				quitFormAlreadyPosted = true;
		    });

		} else {
			//nothing to do.
		}
	}
}

function showQuitDialog() {
	//load dialog - popup
	$("#quit").dialog({
		autoOpen : false,
		modal : true,
		bgiframe : true,
		width : 485,
		resizable : false,
		closeOnEscape : false,
		title : "Why did you decide to quit ?"
	});
	$("#quit").dialog('open');
}

function quitDialog(){
	$("#quit").dialog("close");
}

function quitConfirm() {
    var x;
    if (confirm("Do you really want to quit") == true) {
        showQuitDialog();
    } else {
        //nothing
    }
    //document.getElementById("demo").innerHTML = x;
}
