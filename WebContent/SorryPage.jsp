<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sorry Page</title>
<style type="text/css" media="screen">
#base {
	max-width: 700px;
	background-color: #D1EEEE;
	text-align: justify;
	margin: 0 auto;
	text-justify: distribute-all-lines;
}

#internalText {
	margin-left: 10px;
	margin-right: 10px;
}
</style>
</head>

<body>
	<div id="base">
		<div id="internalText">
			<br> <br>
			<center>			
				<b>${requestScope["message"]}</b> <br>
				<br> <input type="button" value="Close"
					onclick="window.open('', '_self', ''); window.close();"> <br>
				<br>
			</center>
		</div>
	</div>
	<form name="sorryForm">
	<input type="hidden" name="message"  value=${requestScope["message"]}>
	</form>
</body>
</html>