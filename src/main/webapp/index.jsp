<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.2/jquery.min.js"></script>
<script type="text/javascript" src="requests.js">
	
</script>


</head>


<body onload="onloadfunction()">
	<div class="jumbotron">
		<div class="container">
			<h1>Welcome to Stock Monitor</h1>

		</div>
	</div>
	<div class="container">
		<div class="row">

			<div class="col-md-6">
				<h2>Add new Symbol</h2>
				<form onsubmit="addSymbol()" action="javascript:void(0);"
					id="addform">
					Enter Symbol to upload to database <input type="text"
						id="symbolname"> <input type="submit" value="Upload"></input>
					<label id="response"></label>
				</form>
			</div>
			<div class="col-md-6">
				<h2>Delete Symbol</h2>
				<form onsubmit="deleteSymbol()" action="javascript:void(0);"
					id="addform">
					Enter Symbol to delete from database <input type="text"
						id="symbolnamedelete"> <input type="submit" value="Delete"></input>
					<label id="responsed"></label>
				</form>
			</div>
		</div>
		<br>
		<br>
		<div class="row">

			<div class="col-md-6">
				<h2>Get Symbol List</h2>
				<form onsubmit="getSymbolList()" action="javascript:void(0);"
					id="listform">
					<input type="submit" value="Get Symbol List"></input>
				</form>
				<table id="symbolList"></table>
			</div>
			<div class="col-md-6">
				<h2>Get Stock Information</h2>
				<form onsubmit="getSymbolData()" action="javascript:void(0);"
					id="history">

					<input type="radio" name="selection" value="complete"
						checked="checked" id="complete"> get complete data <input
						type="radio" name="selection" value="range" id="range">
					get data in time range <br> <br> Start Date <input
						type="date" id="startdate"></input> <input type="time"
						id="starttime"></input> <br> <br> End Date <input
						type="date" id="enddate"></input> <input type="time" id="endtime"></input>
					<br> <br> Enter symbol name <input type="text"
						id="symname"></input> <input type="submit" value="Get Data"></input>
				</form>
				<table id="stockdata" class="table table-bordered"></table>
			</div>
			<div class="col-md-5"></div>

		</div>

	</div>

</body>
</html>
