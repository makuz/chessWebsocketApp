/**
 * WEBSOCKET CLIENT
 */

// main ------------------------------------------------

$(function() {

	$('#sendYourMoveBtn').click(function() {
		sendYourMoveByFenNotation();
	});

	$('#disconnect').click(function() {
		closeWsConnection();
	});

	$('.connectToUserBtn').click(function(event) {
		var reciever = $(this).data('username');
		connectToUser(reciever);
		event.target.style = "color: red;";

	});

	$('#sendFen').click(function() {
		sendFen();
	});

});

// functions -------------------------------------------

	function sendYourMoveByFenNotation() {
		console.log("send fen : " + fenFromYourMove.value);
		webSocket.send(fenFromYourMove.value);

	};

	function closeWsConnection() {
		console.log('closeWsConnection()');
		$('.connectToUserBtn').css("color", "white");
		webSocket.close();

	};

	function connectToUser(reciever) {
		console.log('connectToUser()');
		
	// init websocket -------------------------------------
		
		var endpointUrl = "ws://" + document.location.host
				+ "/send-fen/${sender}/" + reciever;
		webSocket = new WebSocket(endpointUrl);
		$('#connect').disabled = false;
		
	// websocketClient events -----------------------------
		
		webSocket.onopen = function(msg) {
			console.log("Server connected \n");

		};

		webSocket.onmessage = function(msg) {
			console.log("onmessage: ");
			if (msg != null) {
				console.log("message: " + msg.data);
				// to wywoluje echo: onmessage
				var fenStr = msg.data;
				if (fenStr != null && fenStr != "") {
					// chessboard board object
					board.position(fenStr);
					// chessjs game object
					game = new Chess(fenStr);
					updateStatus();
				}
			}

		};

		webSocket.onclose = function(msg) {
			webSocket.send("client disconnected");
			console.log("Server disconnected \n");
		};

		webSocket.onerror = function(msg) {
		webSocket.send("error: client disconnected");
			console.log("Server disconnected \n");
		};

	};