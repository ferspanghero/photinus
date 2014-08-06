<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<style>
	#container {
		margin: 10px;
		background-color: #D1EEEE;
	}
	
	#content {
		margin: 0 auto;
		background-color: #D1EEEE;
		width: 600px;
		text-align: justify;
	}
	
	p { margin:0 }
</style>

</head>
<body>

<div id='container'>
	<br>
	<div id = 'content'>
		<p style="font-size:30px"><b>Oh no! It seems we have a bug of our own!</b></p><br>
		<p style="font-size:20px">Please send the following report to: adrianoc@uci.edu</p><br><br>
		
		<p style="font-size:20px"><b>Bug report</b></p>
		<table>
			<tr><td><b>User ID:</b></td>			<td><p id="userIdField">${requestScope["userId"]}</p></td></tr>
			<tr><td><b>HIT ID:</b></td>				<td><p id="hitIdField">${requestScope["hitId"]}</p></td></tr>
			<tr><td><b>Date:</b></td>				<td><p id="dateField"/></td></tr>
			<tr><td><b>Error description: </b></td> <td><p id="errorField">${requestScope["error"]}</p></td></tr>
		</table>
		
	</div>
	<br>
</div>

<script>
	if (!document.getElementById("userIdField").innerHTML)
		document.getElementById("userIdField").innerHTML = 'none specified';
	if (!document.getElementById("hitIdField").innerHTML)
		document.getElementById("hitIdField").innerHTML = 'none specified';	
	document.getElementById("dateField").innerHTML = Date();
	if (!document.getElementById("errorField").innerHTML)
		document.getElementById("errorField").innerHTML = 'none specified';
</script>

</body>
</html>