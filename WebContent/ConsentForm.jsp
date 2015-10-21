<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Consent Form Page</title>

<style type="text/css" media="screen">
#container {
	max-width: 700px;
	background-color: #D1EEEE;
	text-align: justify;
	margin: 0 auto;
}

#content {
	background-color: #D1EEEE;
	text-align: justify;
	margin-left: 10px;
	margin-right: 15px;
}

.ui-dialog-titlebar-close {
  visibility: hidden;
}

.alertJS{
	font-size: 20px;
	background-color: #FF0000;
	widtgh: 100%;
	text-align: center;
}
</style>

</head>

<link rel="stylesheet" href="jquery/jquery-ui-1.10.4.custom.min.css">
  <script src="jquery/jquery-1.10.2.js"></script>
  <script src="jquery/jquery-ui-1.10.4.custom.min.js"></script>
  <script	src="http://cdnjs.cloudflare.com/ajax/libs/ace/1.1.3/ace.js"></script>
  <script	src="jquery/quitDialog.js"></script>
	<script>

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
					$("#quit").dialog("close");
					var workerId = document.getElementById("workerId").value;
					quitFormAlreadyPosted = true;
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

			
		var formAlreadyPosted = false;

	
	
	
		function proceed() {

			var loc = window.location.toString();
			var consented = document.getElementById('consentBox').checked;

			//first thing is to check whether the form was already submitted
			if (formAlreadyPosted) {
				alert("Please wait. If it is taking more time than expected, please send an email to the requester.");
			} else if (loc.split('?').length < 2) {
				alert("The URL used is not valid. The file name is missing. Please obtain a valid URL.");
			} else if (consented) {//ok, form was not submitted yet	
				formAlreadyPosted = true;
				var subAction = document.getElementById("subAction");
				subAction.value = "loadQuestions";

				var consentForm = document.forms["consentForm"];
				consentForm.action = "ConsentServlet";

				var fileNameInput = document.getElementById("fileName");
				fileNameInput.value = loc.split('?')[1];

				consentForm.submit();
			} else {
				formAlreadyPosted = false;
				alert("You have to agree with the terms before proceeding");
			}
		}

		function quit() {
			if (confirm('Confirm quitting the study ?')) {
				window.close();
			}
		}
		
		$(document).ready(function(){
			var cookieEnabled=(navigator.cookieEnabled)? true : false					 
			//if not IE4+ nor NS6+
			if (typeof navigator.cookieEnabled=="undefined" && !cookieEnabled){ 
				document.cookie="testcookie"
				cookieEnabled=(document.cookie.indexOf("testcookie")!=-1)? true : false
			}
			
			if (!cookieEnabled){
				alert("Please enable your cookies and refresh the page!");
			}
		});
	</script>
	
	<noscript><div class="alertJS">JavaScript is off. Please enable to view full site.</div></noscript>
<body>



	<div id="container">
		<br>
		<div id="content">
			<center>
				<b>University of California, Irvine<br> Study Information
					Sheet<br> <br> Programming Online Study<br> <br>
					Faculty Sponsor and Lead Researcher
				</b><br> Professor Adriaan W. van der Hoek <br> Department of Informatics<br>
				Donald Bren School of Information and Computer Sciences<br>
				andre@ics.uci.edu<br> 949-824-6326<br>
			</center>
			<ul>
				<li>You are being asked to participate in a research study to perform some 
					programming tasks related to software design, coding, debugging, 
					and testing.</li>
				<br>
				<li>Programming tasks will be performed in an online tool that consists of 
					an external website accessible via a link in a Mechanical Turk task 
					(HIT - Human Intelligent Task).</li>
				<br>
				<li>The purpose of the study is to better understand the challenges 
					developers face in using tools to answer their questions about code 
					and to help inform the design of new tools that help developers to work 
					more effectively.</li>
				<br>
				<li>You are eligible to participate in this study if you are at least 18 
					years of age or older; are fluent in English; and have at least minimal 
					programming skills.</li>
				<br>
				<li>The research procedures involve using an online software development tool 
					and will last approximately from 5 to 45 minutes.</li>
				<br>
				<li>There are no risks/discomforts associated with the study. No personal 
					information will be collected.</li>
				<br>
				<li>There are no direct benefits from participation in the study. However, 
					this study may help us to better understand how programmers work with 
					tools.</li>
				<br>
				<li>You will be paid the equivalent of 9  dollars per hour, which is 
					California minimal wage, prorated by the expected length of the task 
					to be completed. You will be paid through Amazon Mechanical Turk. 
					At the end of the study, you will be given a code to enter in your HIT 
					(Human Intelligent Task) that confirms that you participated.</li>
				<br>
				<li>All research data collected will be stored securely and
					confidentially in encrypted files. At the end of the study, the
					original answers to demographics questions will be deleted from our
					files.</li>
				<br>
				<li>The research team and authorized UCI personnel may have
					access to your study records to protect your safety and welfare.
					Any information derived from this research project that personally
					identifies you will not be voluntarily released or disclosed by
					these entities without your separate consent, except as
					specifically required by law.</li>
				<br>
				<li>If you have any comments, concerns, or questions regarding
					the conduct of this research please contact the researchers listed
					at the top of this form.</li>
				<br>
				<li>Please contact UCI's Office of Research by phone, (949)
					824-6662, by e-mail at IRB@research.uci.edu or at 5171 California
					Avenue, Suite 150, Irvine, CA 92617 if you are unable to reach the
					researchers listed at the top of the form and have general
					questions; have concerns or complaints about the research; have
					questions about your rights as a research subject; or have general
					comments or suggestions.</li>
				<br>
				<li>Participation in this study is voluntary. There is no cost
					to you for participating. You may refuse to participate or
					discontinue your involvement at any time without penalty. You are
					free to withdraw from this study at any time. If you decide to
					withdraw from this study you should notify the research team
					immediately by clicking on the "No, thanks" button below.</li>
			</ul>

			<br>

			<form name="consentForm" action="ConsentServlet" method="get">
				<input type="hidden" id="subAction" name="subAction" value="loadConsentForm"> 
				<input type="hidden" id="workerId" name="workerId" value="consentForm">	
				<input type="hidden" id="fileName" name="fileName">	
					<input type="checkbox" name="consentBox" id="consentBox"><i> By checking this box I hereby state 
					that "I have read the study information sheet and want to proceed with this study".</i> <br>
					
					<br> 
					<input type="button" style="float: left;" value="No, thanks" onclick='quitConfirm()'>
					<input type="button" name="yesButton" id="yesButton" 
					value="Yes, I want to participate" style="float: right;"
					onclick="proceed()"> 
			
					<br>
			</form>

		</div>
		<div style="display: none;" id="quit">
		<div id="internalText">
			<form name="reasonForm" method="get">
				<input type="radio" name="reason" value="difficult" />The task is too
				difficult &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; <br> <input
					type="radio" name="reason" value="boring" />The task is too boring
				&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; <br> <input
					type="radio" name="reason" value="long" />The task is too long
				&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp; <br> <input
					type="radio" name="reason" value="other" />Other&nbsp;
					<input type="text" id="otherReason" name="otherReason" /><br><br>

				
				<!-- Hidden fields -->
				<input type="hidden"
					name="workerId" id="workerId" value="consentForm"> 
				<center>
					<INPUT TYPE="button" name="reasonButton" id="reasonButton" onclick="submitQuitAnswer()"
						VALUE="Submit">
					<INPUT TYPE="button" VALUE="Cancel"
					onclick="quitDialog()">
				</center>
				<br>
			</form>

		</div>
	</div>
		
		
		<br>
	</div>
 
</body>
</html>
