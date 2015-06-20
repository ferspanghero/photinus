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
			<form name="feedback" action="feedback" method="get">
				<center>
					<b>Great! Almost done! Could you please give us some additional feedback?</b><br><br>
					<textarea name="feedback" id="feedback" rows="6" cols="45"></textarea>
					<input type="hidden"
					name="key" id="key" value=${requestScope["key"]}> 
					<input type="hidden"
					name="workerId" id="workerId" value=${requestScope["workerId"]}> 
					<br><br>
					<input type="submit" value="Submit">
					<br><br>
			</form>
			</center>
		</div>
	</div>
</body>
</html>