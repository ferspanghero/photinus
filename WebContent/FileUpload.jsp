<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload Files for Debugging</title>

<style type="text/css" media="screen">
	#external {
		max-width: 700px;
		text-align: justify;
		margin: 0 auto;	
	}

	#container  {
		max-width: 700px;
		background-color: #D1EEEE;
		text-align: justify;
		margin: 0 auto;
		
	}
	
	#content  {
		background-color: #D1EEEE;
		text-align: justify;
		 margin-left: 10px;
		margin-right: 10px;
	}
	
</style>

</head>


<body>

<script>
		function openMicrotask() {
			var subAction = document.getElementById("subAction");
			subAction.value = "loadFirst";
			document.forms["openMicrotaskForm"].submit();
		}
	
		
		function generateWorkerSessions(){
			var subAction = document.getElementById("subAction");
			subAction.value = "generateWorkerSessions";
			document.forms["sessionForm"].submit();
		}
		
		
		function deleteAll(){
			if (confirm('Confirm deleting all data?')) {
				var subAction = document.getElementById("subAction");
				subAction.value = "delete";
				document.forms["deleteForm"].submit();
			}
		}
		
		function runReports(){
			var subAction = document.getElementById("subAction");
			subAction.value = "generateReports";
			document.forms["reportsForm"].submit();
		}
		
	</script>
	
	
<div id="external">

	<table border="0">
		<tr>
		
						<td><img src="./images/Firefly-2.jpg" width=112 height=46 />
				&nbsp;&nbsp;&nbsp;</td>
				
					<td><img src="./images/UploadsButton-blue.jpg"></td>
					
					<td>
						<form method="GET" action="microtask" name="openMicrotaskForm">
							<input type="image" src="./images/MicrotasksButton.jpg"
								value="Open Microtask" name="openMicrotask" align="bottom" onclick="openMicrotask()">
							<input type="hidden" id="subAction" name="subAction" value="loadFirst"> 	
							<input type="hidden" id="userId" name="userId" value="researcher"> 	
							<input type="hidden" id="hitId" name="hitId" value="researchSession"> 	
						</form>
					</td>
					
					<td>	
							<input type="image" src="./images/ReportsButton.jpg"
								value="Generate Report" name="generateReports" align="bottom" onclick="runReports()">
					</td>
					
					<td><form method="POST" action="results" name="results">
					<input type="image" src="./images/ResultsButton.jpg"
						value="results" name="results">
				</form></td>
 
		
		</tr>

	</table>
	
<div id='container'>
	<br>
	<div id = 'content'>
 
	<table cellspacing="0">

		<tr>
              <td>&nbsp;&nbsp;&nbsp; </td>
              <td>&nbsp;&nbsp;&nbsp; </td>
		
				 	<td>
				<form action="upload" method="post" enctype="multipart/form-data" name="uploadForm">
					<table border="0">
						<tr>
							<td align="right">Suspicious file:</td>
							<td><input type="file" name="fileUploaded" size="40" src="./images/Browse.jpg"/></td>
						</tr>
						<tr>
							<td align="right">Failure description:</td>
							<td><input type="text" name="bugReport" size="54"/></td>			 
						</tr>
						<tr>
							<td align="right">Method Name:</td>
							<td><input type="text" name="targetMethod" size="54"/></td>		 
						</tr>
						<tr>
							<td><input type="submit" value="Generate Microtasks"/></td>	
							<td>${requestScope["microtasks_message"]}</td>
						</tr>
					</table>
							<input type="hidden" id="userId" name="userId" value="researcher"> 	
							<input type="hidden" id="hitId" name="hitId" value="researchSession"> 	
					<br>
				</form>
			
			
			</td>
			 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  </td>
			  
			 
		</tr>
	</table>
	<table>
		<tr>
		  <td>&nbsp;&nbsp;&nbsp; </td>
          <td>&nbsp;&nbsp;&nbsp; </td>
		  <td>
				<form action ="upload" method="get" name="sessionForm">
			 		<input type="button" value="Generate Worker Sessions" onclick="generateWorkerSessions()" />	
		 			<input type="hidden" id="subAction" name="subAction" value="generateWorkerSessions" /> 	
				 </form>
				 </td>
				 <td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ${requestScope["workerSessions_message"]}
		  </td>		
		  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  </td>
		 </tr>
	</table>
	
	<br>
	<br>
	
	<table>
		<tr>
		  <td>&nbsp;&nbsp;&nbsp; </td>
          <td>&nbsp;&nbsp;&nbsp; </td>
		  <td>
				<form action ="upload" method="get" name="deleteForm">
			 		<input type="button" value="Delete All Data" onclick="deleteAll()" />	
			 		<input type="hidden" id="subAction" name="subAction" value="delete" /> 	
				 </form>
				 </td>
	      <td>&nbsp;&nbsp;&nbsp; ${requestScope["reports_message"]}</td>		
		  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  </td>
		  
		 </tr>
	</table>

	<form action ="upload" method="get" name="reportsForm">
		<input type="hidden" id="subAction" name="subAction" value="generateReports" /> 
	</form>

	
</div>
<br>

</div>
</div>

</body>
</html>
