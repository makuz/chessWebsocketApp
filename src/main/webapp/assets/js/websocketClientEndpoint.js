/**
 * WEBSOCKET CLIENT ENDPOINT
 * 
 * websocket client variable name = WEBSOCKET_CLIENT_NAME
 * 
 */

// ------CONNECT TO WEBSOCKET FUNCTION, WEBSOCKET EVENTS----------------------
function connectToWebSocket() {
	console.log('connectToWebSocket()');

	// ----init websocket -------------------------------------
	var endpointUrl = "ws://" + document.location.host + "/send-fen/"
			+ WEBSOCKET_CLIENT_NAME;
	webSocket = new WebSocket(endpointUrl);

	// websocketClient events -----------------------------

	webSocket.onopen = function(event) {
		console.log("Server connected \n");
		console.log(event.data);
		webSocket.send(JSON.stringify({
			type : "welcome-msg",
			senderName : WEBSOCKET_CLIENT_NAME
		}));

	};

	// -----------------------------------

	webSocket.onmessage = function(event) {
		console.log("onmessage: ");
		console.log(event);

		if (event != null) {
			var message = JSON.parse(event.data);
			console.log("message");
			console.log(message);

			if (message.type == "chess-move") {
				var fenStr = message.fen;
				if (fenStr != null && fenStr != "") {
					// chessboard board object
					board.position(fenStr);
					// chessjs game object
					game = new Chess(fenStr);
					updateStatus();
				}
			} else if (message.type == "game-handshake-invitation") {

				showGameHandshakeModalBox(message.senderName);

			} else if (message.type == "game-handshake-agreement") {

				alert("game agreement");

			} else if (message.type == "game-handshake-refuse") {

				alert("game refused");

			} else {
				showParticipants(event.data);

				$('#participants div ul li span.username')
						.each(
								function() {
									if ($(this).text() == WEBSOCKET_CLIENT_NAME) {
										var sendMoveBtn = $(this).next()
												.children().first();
										sendMoveBtn.attr('disabled', true);
										sendMoveBtn.removeAttr('class');
										sendMoveBtn
												.attr('class',
														'btn btn-sm btn-default send-to-user-btn');
										$(this).parent().css(
												'background-color', '#B9BCBD');
									}
								});
			}
		}

	};

	// -----------------------------------

	function showGameHandshakeModalBox(sender) {

		$('#game-handshake-modal-title').html(
				"Do you want to play with: "
						+ "<span class=\"text-primary\"><b>" + sender
						+ "</b></span>");
		$('#game-handshake-msgTo').val(sender);
		$('#game-handshake-modal').modal('show');

	}

	// -----------------------------------

	webSocket.onclose = function(event) {
		webSocket.send(JSON.stringify({
			type : "goodbye-msg",
			senderName : WEBSOCKET_CLIENT_NAME
		}));
		hideParticipants();
		console.log(event);
	};

	// -----------------------------------

	webSocket.onerror = function(event) {
		webSocket.send("error: client disconnected");
		console.log("Server disconnected \n");
		console.log(event);
		webSocket.close();
	};

};

// close websocket when page reload --------------------------------------------

window.onbeforeunload = function() {
	webSocket.onclose = function() {
		webSocket.send("client disconnected");
		console.log("Server disconnected \n");
	}; // disable onclose handler first
	webSocket.close();
	window.location.reload(false);

};

// functions -------------------------------------------

function inviteUserToGame(reciever) {
	console.log("game-handshake from " + WEBSOCKET_CLIENT_NAME);
	console.log(" to " + reciever);
	var msg = "invitation";
	webSocket.send(JSON.stringify({
		type : "game-handshake-invitation",
		handshakeMsg : msg,
		senderName : WEBSOCKET_CLIENT_NAME,
		sendTo : reciever
	}));
}

// --------------------------------------------------------

function agreementToPlay() {

	var usernameToPlayWith = $('#game-handshake-msgTo').val();
	var myUserName = WEBSOCKET_CLIENT_NAME;
	console.log("game-handshake-agreement from");
	console.log(myUserName + " with " + usernameToPlayWith);
	var msg = "agreement";
	webSocket.send(JSON.stringify({
		type : "game-handshake-agreement",
		handshakeMsg : msg,
		senderName : WEBSOCKET_CLIENT_NAME,
		sendTo : usernameToPlayWith
	}));

	$('#game-handshake-modal').modal('hide');

}

// --------------------------------------------------------

function refusedToPlay() {

	var usernameNotToPlayWith = $('#game-handshake-msgTo').val();
	var myUserName = WEBSOCKET_CLIENT_NAME;
	console.log("game-handshake-refuse from");
	console.log(myUserName + " to " + usernameNotToPlayWith);
	var msg = "refuse";
	webSocket.send(JSON.stringify({
		type : "game-handshake-refuse",
		handshakeMsg : msg,
		senderName : WEBSOCKET_CLIENT_NAME,
		sendTo : usernameNotToPlayWith
	}));
	
	$('#game-handshake-modal').modal('hide');

}

function sendYourMoveByFenNotationToUser(reciever) {
	console.log("send-fen : " + fenFromYourMove.value);
	console.log(" to " + reciever);
	console.log(" from " + WEBSOCKET_CLIENT_NAME);
	var fenString = fenFromYourMove.value;
	webSocket.send(JSON.stringify({
		type : "chess-move",
		fen : fenString,
		senderName : WEBSOCKET_CLIENT_NAME,
		sendTo : reciever
	}));

};

// -----------------------------------------------
function closeWsConnection() {
	console.log('closeWsConnection()');
	$('.connectToUserBtn').css("color", "white");
	webSocket.close();
};

// -----------------------------------------------------------

function showParticipants(data) {
	console.log("showParticipants()");

	var usersArr = JSON.parse(data);
	var usernames = new Array();
	for (var i = 0; i < usersArr.length; i++) {
		usernames.push(usersArr[i].username);
	}

	var participantsList = $('#participants div ul');
	participantsList.html('');
	var allText = "";
	for (var i = 0; i < usernames.length; i++) {

		var userData = '<li class="list-group-item game-user">'
				+ '<span class="username">'
				+ '<span class="glyphicon glyphicon-user"></span>'
				+ usernames[i]
				+ '</span>'
				+ '<span class="participants-action-btns">'
				+ '<button class="btn btn-sm btn-danger send-to-user-btn" onclick="inviteUserToGame('
				+ '\''
				+ usernames[i]
				+ '\''
				+ ')"'
				+ 'data-username="'
				+ usernames[i]
				+ '">invite</button>'
				+ '<button class="btn btn-sm btn-info send-to-user-btn" onclick="sendYourMoveByFenNotationToUser('
				+ '\''
				+ usernames[i]
				+ '\''
				+ ')"'
				+ 'data-username="'
				+ usernames[i]
				+ '">send-move</button>'
				+ '&nbsp;'
				+ '<button data-username="'
				+ usernames[i]
				+ '"'
				+ 'onclick="showUSerInfoByAjax('
				+ '\''
				+ usernames[i]
				+ '\''
				+ ')"'
				+ 'class="btn btn-warning btn-sm user-info-btn">info'
				+ '</button>' + '</span>' + '</li>';

		participantsList.append(userData);
	}
};

// ----------------------------------------------

function hideParticipants() {
	$('#participants div ul').html('');
};

// ----------------------------------------------

function showUSerInfoByAjax(login) {
	$('#user-info-modal').modal('show');
	$.ajax({
		url : "user/get-user-info-by-username",
		data : {
			username : login
		},
		success : function(response) {
			$('#usernameResponse').text("login: " + response.username);
			$('#userIdResponse').text("id: " + response.userId);
			$('#userEmailResponse').text("email: " + response.email);
			$('#userNameResponse').text("name: " + response.name);
			$('#userLastnameResponse').text("lastname: " + response.lastname);
		}
	});
};

