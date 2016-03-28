function getSymbolList() {

	request = $.ajax({
		url : "resources/stocks/symbols",
		type : "get",
		dataType : "json",
		contentType : 'application/json'
	});
	request.done(function(response, textStatus, jqXHR) {
		console.log(response);
		$('#symbolList tr').remove();
		var cnt = 0;
		for ( var i in response) {
			var id = response[i].symbol;
			tr = $('<tr/>');
			var cnt = cnt + 1;
			tr.append("<td>" + cnt + "</td>");
			tr.append("<td>" + response[i].symbol + "</td>");
			$('#symbolList').append(tr);
		}
	});

	request.fail(function(jqXHR, textStatus, errorThrown) {
		console.error("The following error occured: " + textStatus);
	});

	request.always(function() {
	});
}
function addSymbol() {
	$("#response").text("");
	var val = $('#symbolname').val().trim();
	if (val) {
		jsondata = "{\"symbol\":\"" + $('#symbolname').val() + "\"}";

		request = $.ajax({
			url : "resources/stocks/",
			type : "post",
			data : jsondata,
			contentType : 'application/json'
		});
		request.done(function(response, textStatus, jqXHR) {
			console.log(response);
			if (typeof (response) === "undefined")
				$("#response").text("Symbol already present or is invalid");
			else
				$("#response").text("Symbol uploaded successfully");
		});

		request.fail(function(jqXHR, textStatus, errorThrown) {
			console.error("The following error occured: " + textStatus);
		});

		request.always(function() {
		});
	} else {
		$("#response").text("Enter some data ");
	}
}
function getSymbolData() {
	var qurl;
	if ($('#complete').is(':checked'))
		qurl = "resources/stocks/history/" + $('#symname').val().trim();
	else {
		var startDate = document.getElementById("startdate").value;
		var startTime = document.getElementById("starttime").value;

		var endDate = document.getElementById("enddate").value;
		var endTime = document.getElementById("endtime").value;
		console.log(startTime);

		var sdate = getDate(startDate, startTime).getTime();
		var edate = getDate(endDate, endTime).getTime();
		if (isNaN(sdate) || isNaN(edate)) {
			$("#errorhistory").text("Invalid date and time values");
			return;
		} else {
			qurl = "resources/stocks/history/" + $('#symname').val().trim()
					+ "/?startDate=" + sdate + "&endDate=" + edate;
			console.log(qurl);
		}
	}
	request = $.ajax({
		url : qurl,
		type : "get",
		dataType : "json",
		contentType : 'application/json'
	});
	request.done(function(response, textStatus, jqXHR) {
		$('#stockdata tr').remove();
		tr = $('<tr/>');
		tr.append("<td>Name</td>");
		tr.append("<td>Price</td>");
		tr.append("<td>Date</td>");
		tr.append("<td>Volume</td>");
		$('#stockdata').append(tr);
		for ( var i in response) {
			tr = $('<tr/>');
			tr.append("<td>" + response[i].name + "</td>");
			tr.append("<td>" + response[i].price + "</td>");
			tr.append("<td>" + getStringDate(new Date(response[i].ts * 1000))
					+ " " + getStringTime(new Date(response[i].ts * 1000))
					+ "</td>");
			tr.append("<td>" + response[i].volume + "</td>");
			$('#stockdata').append(tr);
		}
	});

	request.fail(function(jqXHR, textStatus, errorThrown) {
		console.error("The following error occured: " + textStatus);
	});

	request.always(function() {
	});
}
function getDate(date, time) {
	var b = date.split(/\D/);
	var hours = time.split(":")[0];
	var minutes = time.split(":")[1];
	return new Date(b[0], --b[1], b[2], hours, minutes);
}
function onloadfunction() {
	document.getElementById('startdate').value = getStringDate(new Date());
	document.getElementById('enddate').value = getStringDate(new Date());

	document.getElementById('starttime').value = getStringTime(new Date());
	document.getElementById('endtime').value = getStringTime(new Date());
}
function getStringTime(d) {
	h = d.getHours(), m = d.getMinutes();
	if (h < 10)
		h = '0' + h;
	if (m < 10)
		m = '0' + m;
	return h + ":" + m
}

function getStringDate(date) {
	var day = date.getDate();
	var month = date.getMonth() + 1;
	var year = date.getFullYear();
	if (month < 10)
		month = "0" + month;
	if (day < 10)
		day = "0" + day;
	var today = year + "-" + month + "-" + day;
	return today;
}
function deleteSymbol() {
	qurl = "resources/stocks/" + $('#symbolnamedelete').val().trim();
	$("#responsed").text("");
	request = $.ajax({
		url : qurl,
		type : "delete",
		dataType : "json",
		contentType : 'application/json'
	});
	request.done(function(response, textStatus, jqXHR) {
		console.log(response);
		if (response.result == true)
			$("#responsed").text("Symbol deleted successfully");
		else
			$("#responsed").text("Symbol not found");
	});

	request.fail(function(jqXHR, textStatus, errorThrown) {
		console.error("The following error occured: " + textStatus);
		$("#responsed").text("Some error has occurred");
	});

	request.always(function() {
	});
}