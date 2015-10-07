<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Thanks Page</title>

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
		 margin-left: 100px;
		margin-right: 100px;
	}

#key{
background-color: #FFFFFF;
}
</style>
</head>


<body>
<div id="container">
<br>
	<div id="content">
		<br>
		<center><b>Please copy the code below and paste in your HIT at Mechanical Turk.<br><br>
			<div id=key>${requestScope["key"]} </div>
			<form name="thanksForm">
				<input type="hidden" name="sessionId" value=${requestScope["key"]} >
				<br>
			</form>
		<br>
		<br>
		
		Thank you for your contribution! </b>
			<br><br>
			<br> 
		</center>
	</div>
</div>
</body>
</html>