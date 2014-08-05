<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Identification</title>

<style>
	#container {
		margin: 10px;
		background-color: #FFFAEB;
	}
	
	#content {
		margin: 0 auto;
		background-color: #FFFAEB;
		width: 400px;
		text-align: justify;
	}
</style>

</head>
<body>

<div id='container'>
	<br>
	<div id='content'>
	
		<p style="font-size:20px"><b>Please enter the following information</b></p>
		<form name="userIdForm" action="UserIdServlet" method="get">
			<table>
				<tr><td>User ID: </td><td><input type="text" id="userId" name="userId" size="40"/></td></tr>
				<tr><td>HIT ID: </td><td><input type="text" id="hitId" name="hitId" size="40"/></td></tr>
				<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>	<!-- blank line -->
				<tr><td align=left><input type="button" value="Quit"	onclick="window.open('', '_self', ''); window.close();"></td>
					<td align=right><input type="button" value="Proceed"	onclick="checkIds()"></td>
			</table>
			<br>
		</form>
		
	</div>
	<br><br>
</div>

<script type="text/javascript">
	function checkIds(){
		
		var userId = document.getElementById('userId');
		userId.value = userId.value.replace(/\s+/g, '');	// removing white spaces
		var hitId = document.getElementById('hitId');
		hitId.value = hitId.value.replace(/\s+/g, '');		// removing white spaces

		if (userId.value && hitId.value){
			document.forms["userIdForm"].submit();
		} else if (userId.value){
			alert("Please enter a valid HIT ID");
		} else {
			alert("Please enter a valid User ID");
		}
	}
</script>

</body>
</html>