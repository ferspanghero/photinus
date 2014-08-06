<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Consent form</title>

<style type="text/css" media="screen">

	#consentText {
		margin: 0 auto;
		background-color: #D1EEEE;
		width: 800px;
		text-align: justify;
	}

	#consentPage {
		margin: 10px;
		background-color: #D1EEEE;
	}
	
</style>

</head>

<body>
 
<div id=consentPage>
	<br>
	<div id='consentText' >
		<center>
			<b>University of California, Irvine<br>
			Study Information Sheet<br>
			<br>
			Programming Online Study<br>
			<br>
			Lead Researcher</b><br>
			Thomas LaToza, PhD <br>
			Department of Informatics<br>
			Donald Bren School of Information and Computer Sciences<br>
			tlatoza @uci.edu<br>
			<br>
			<b>Faculty Sponsor</b><br>
			Professor Adriaan W. van der Hoek<br>
			Department of Informatics<br>
			Donald Bren School of Information and Computer Sciences<br>
			andre@ics.uci.edu<br>
			949-824-6326<br>
		</center>
		<ul>
			<li>You are being asked to participate in a research study to perform some programming tasks.</li>
			<br>
			<li>You are eligible to participate in this study if you are at least 18 years of age or older; are fluent in English and live in the United States.</li>
			<br>
			<li>The research procedures involve using an online software development tool and will last approximately from 5 to 45 minutes.</li>
			<br>
			<li>There are no risks/discomforts associated with the study. No personal information will be collected.</li>
			<br>
			<li>There are no direct benefits from participation in the study. However, this study may help us to better understand how programmers work with tools.</li>
			<br>
			<li>You will be paid 50 cents of dollar per microtask through Amazon Mechanical Turk. At the end of the study, you will be given a code to enter in your HIT that confirms that you participated.</li> 
			<br>
			<li>All research data collected will be stored securely and confidentially in encrypted files. At the end of the study, the original answers to demographics questions will be deleted from our files.</li> 
			<br>
			<li>The research team and authorized UCI personnel may have access to your study records to protect your safety and welfare. Any information derived from this research project that personally identifies you will not be voluntarily released or disclosed by these entities without your separate consent, except as specifically required by law.</li>
			<br>
			<li>If you have any comments, concerns, or questions regarding the conduct of this research please contact the researchers listed at the top of this form.</li>
			<br>
			<li>Please contact UCI's Office of Research by phone, (949) 824-6662, by e-mail at IRB@research.uci.edu or at 5171 California Avenue, Suite 150, Irvine, CA 92617 if you are unable to reach the researchers listed at the top of the form and have general questions; have concerns or complaints about the research; have questions about your rights as a research subject; or have general comments or suggestions.</li>
			<br>
			<li>Participation in this study is voluntary.  There is no cost to you for participating.  You may refuse to participate or discontinue your involvement at any time without penalty.  You are free to withdraw from this study at any time. If you decide to withdraw from this study you should notify the research team immediately.</li>
		</ul>	
		
		<br>
	
		<form name="consentForm" action="UserId.jsp" method="get">
			<input type="checkbox" id="consentBox"><i>By checking this box I hereby state that I have read, understood and agreed with the terms above.</i>
			<br><br>
			<input type="button" value="No, thanks"		style="float: left;"	onclick="quit()"> 
			<input type="button" value="Proceed"	style="float: right;"	onclick="proceed()"> 
			<br>
		</form>
		
	</div>
	<br>
</div>

<script>
	function proceed(){
		var consented = document.getElementById('consentBox').checked;
		if (consented){
			document.forms["consentForm"].submit();
		} else{
			alert("You have to agree with the terms before proceeding");
		}
	}
	
	function quit(){
		if(confirm('Confirm quitting the study ?')){
			window.open('', '_self', ''); window.close();
		}
	}
	
</script>

</body>
</html>
 