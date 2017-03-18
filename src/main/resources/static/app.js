var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
}

function connect() {
    var socket = new SockJS('/weight-reader-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
//        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/weight', function (resp) {
        	updateWeight(JSON.parse(resp.body).weightString);
        	setZeroIndicator(JSON.parse(resp.body).zeroIndicator);
        });
        stompClient.subscribe('/topic/driverRecord', function (resp) {
        	updateDriverRecord(JSON.parse(resp.body));
        });
        stompClient.send("/app/read", {}, null);
    });
}

function updateWeight(message) {
//	console.log(message);
    odometer.innerHTML = message;
}

function updateDriverRecord(messageObject) {
//	console.log(message);
    driverRecord.innerHTML = "สวัสดีค่ะ ผู้ถือบัตร "+messageObject.identifier;
}

function setZeroIndicator(message) {
    if(strcmp("true", message) == 0) {
    	$(".zeroIndicator").css("background-color", "#33fa35" );
    	driverRecord.innerHTML = "-";
    } else {
    	$(".zeroIndicator").css("background-color", "#ff6666" );
    }
}

function setDisconnect() {
    $.ajax({
        url : '/disconnect',
        type: 'GET'
    });
}

function strcmp(a, b) {
    if (a.toString() < b.toString()) return -1;
    if (a.toString() > b.toString()) return 1;
    return 0;
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    setDisconnect();
    console.log("Disconnected");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
});

