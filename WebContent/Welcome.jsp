<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome</title>
</head>
<body>
 
	<div>
		<img src="./images/Firefly-2.jpg" width=112 height=46 />
	</div>

	<div style="background-color: #FFFAEB;">
		<h1>What would you like to do today?</h1>

		<table>
			<tr>
				<td><b>Ask the crowd to help me find a bug: </b></td>
				<td><form method="POST" action='./FileUpload.jsp'
						name="fileUpload">
						<input type="image" src="./images/UploadsButton.jpg"
							value="Upload Files" name="upload">
					</form></td>
			</tr>

			<tr>
				<td><b>Help find some bugs by answering questions: </b></td>

				<td>
					<form method="POST" action="microtask" name="openMicrotask">
						<input type="image" src="./images/MicrotasksButton.jpg"
							value="Open Microtask" name="openMicrotask">
					</form>
				</td>

			</tr>

		</table>
	</div>

</body>
</html>
 