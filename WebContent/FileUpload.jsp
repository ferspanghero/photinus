<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload Suspicious File</title>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>

</head>
<body> 

	<table border="0">
		<tr valign="bottom">
			<center>
				<td><img src="./images/Firefly-2.jpg" width=112 height=46 />
					&nbsp;&nbsp;&nbsp;</td>  
					
					<td><img src="./images/UploadsButton-blue.jpg"></td>
					
					<td>
						<form method="POST" action="MicrotaskServlet" name="openMicrotask">
							<input type="image" src="./images/MicrotasksButton.jpg"
								value="Open Microtask" name="openMicrotask">
						</form>
					</td>
					
					<td><img src="./images/ResultsButton-blue.jpg"></td>
 
			</center>  
		</tr>

	</table>

 
	<table cellspacing="0">

		<tr bgcolor="#FFFAEB">
              <td>&nbsp;&nbsp;&nbsp; </td>
              <td>&nbsp;&nbsp;&nbsp; </td>


		
				 	<td bgcolor="#FFFAEB">
				<form action="upload" method="post" enctype="multipart/form-data">
					<table border="0">
						<tr>
							<td align="right">Suspicious file:</td>
							<td><input name="fileUploaded" type="file" size="40" src="./images/Browse.jpg"></td>
						</tr>
						<tr>
							<td align="right"></td>
							<td><input type="submit" value="Generate Microtasks"> 
							</td>
								 
						</tr>
					</table>

					<br>
				</form>
			
			<form action="upload" method="get"><br>
							<input type="hidden" name="testField" value=${requestScope["test"]} > 
					 <input type="submit" value="testSubmit" ></form>

				<div id="result"> 
					 ${requestScope["return_message"]} 
				</div>
			<td>
			 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  </td> 
		</tr>
	</table>



</body>
</html>
