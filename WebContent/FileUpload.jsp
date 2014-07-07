<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload Suspicious File</title>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>

</head>
<body>

	<script>
		//Uploads a file. It is the starting point for generating microtasks
		function uploadFile(thisObj, thisEvent) {
			alert("uploadFile");
			var fileName = $('#fileName').val(); // file name in an input field
			alert(fileName);
			var jsonDataObject = new Object();
			jsonDataObject.fileName = fileName;

			// turn the jsonData object into a string so it can be passed to the servlet
			var jsonData = JSON.stringify(jsonDataObject);

			$.getJSON("FileUploadController", {
				action : "uploadFile",
				json : jsonData
			}, function(data) {
				alert(data.message);
				$('#return_message').html(data.message);
			});

			return false; // prevents the page from refreshing before JSON is read from server response
		}

		//Opens a page with one of the microtasks generated for the previously uploaded file
		function openMicrotask(thisObj, thisEvent) {
			alert("openMicrotask");
			var fileName = $('#fileName').val(); // file name in an input field
			var jsonDataObject = new Object();
			jsonDataObject.fileName = fileName;

			// turn the jsonData object into a string so it can be passed to the servlet
			var jsonData = JSON.stringify(jsonDataObject);

			//$.post("MicrotaskController", {
			///		url:"Edit.jsp",
			//		action : "openMicrotask",
			//		json : jsonData}, function (response){},'json'
			//	);

			return true; // prevents the page from refreshing before JSON is read from server response
		}
	</script>

	<h1>Upload Suspicious File</h1>
	<ul>
		<li>Allowed file types are: .java</li>
	</ul>

	<form action="upload" method="post" enctype="multipart/form-data">
		<table cellspacing="2" cellpadding="2" border="0">
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
	
 
 <div id="result">
            <h4>${requestScope["return_message"]}</h4>
        </div>
	
	<form method="POST" action='MicrotaskServlet' name="openMicrotask">
		<input type="submit" value="Give me a Microtask!" name="openMicrotask">

	</form>
	<br>
	
	



</body>
</html>
