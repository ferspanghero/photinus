<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Results</title>
		<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

</head>
<body> 

	<table border="0">
		<tr valign="bottom">
			<center>
				<td><img src="./images/Firefly-2.jpg" width=112 height=46 />
					&nbsp;&nbsp;&nbsp;</td>  
					
					<td><form method="POST" action='./FileUpload.jsp'
					name="fileUpload">
					<input type="image" src="./images/UploadsButton.jpg"
						value="Upload Files" name="upload">
				</form></td>
					
					<td>
						<form method="POST" action="microtask" name="openMicrotask">
							<input type="image" src="./images/MicrotasksButton.jpg"
								value="Open Microtask" name="openMicrotask">
						</form>
					</td>
					
					<td>
				
					<img src="./images/ResultsButton-blue.jpg"></td>
 
			</center>  
		</tr>

	</table>
	<br>
      <div id="result" style="background-color:#FFFAEB;">${requestScope["results"]}</div>
    <br>
</body>
</html>
