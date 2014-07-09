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
		<tr>
<center>	
			<td> <img src="./images/Firefly-2.jpg" width=112 height=46/>
					 &nbsp;&nbsp;&nbsp;
	
		<i>
		 <a href="./FileUpload.jsp&action=load"><img src="./images/HomeButton.jpg"></a>
	    <a href="./FileUpload.jsp&action=load"><img src="./images/UploadsButton.jpg"></a>
	     <a href="./FileUpload.jsp&action=load"><img src="./images/MicrotasksButton.jpg"></a>
	   <a href="./FileUpload.jsp&action=load"><img src="./images/ResultsButton.jpg"></a>
	     
	 </i>
	 </center>
		 </td>
				
			 
				
			
			
		</tr>

		<tr>	
	
	
			<td bgcolor ="#FFFAEB">
			<h1><i>Upload Suspicious File</i></h1>
				<form action="upload" method="post" enctype="multipart/form-data">
					<table  border="0">
						<tr>
							<td align="right">File Name:</td>
							<td><input type="file" name="fileUploaded"></td>
						</tr>
						<tr>
							<td align="right"></td>
							<td><input type="submit" value="upload"></td>
						</tr>
					</table>

					<br>
				</form>
<form method="POST" action='MicrotaskServlet' name="openMicrotask">
					<input type="submit" value="Upload File"
						name="openMicrotask"> <br>
					
						
				</form> <br>

				<div id="result">
					<h4>${requestScope["return_message"]}</h4>
				</div>

				
			<td>
 
			
		</tr>
	</table>



</body>
</html>
