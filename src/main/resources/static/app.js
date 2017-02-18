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
        });
        stompClient.send("/app/read", {}, null);
    });
}

function updateWeight(message) {
//	console.log(message);
    odometer.innerHTML = message;
}

function setDisconnect() {
    $.ajax({
        url : '/disconnect',
        type: 'GET'
    });
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

