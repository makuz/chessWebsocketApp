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
			sendFrom : WEBSOCKET_CLIENT_NAME
		}));

		$('#connection-status').html(
				"<div class=\"alert alert-success connection-status-msg\">"
						+ "<h2>You are connected!</h2></div>");

		var disconnectBtn = $('#disconnect');
		var connectBtn = $('#connectToWebSocket');

		if (disconnectBtn.attr("disabled", true)) {
			disconnectBtn.removeAttr("class");
			disconnectBtn.attr("disabled", false);
			disconnectBtn.attr("class", "btn btn-danger pull-right");
		}

		connectBtn.removeAttr("class");
		connectBtn.attr("disabled", true);
		connectBtn.attr("class", "btn btn-default");

	};

	// -----------------------------------

	webSocket.onmessage = function(event) {
		console.log("onmessage: ");
		console.log(event);

		console.log("data: ");
		console.log(event.data);

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

				showGameHandshakeModalBox(message.sendFrom);

			} else if (message.type == "game-handshake-agreement") {

				$('#game-handshake-response-modal-title').html(
						"game agreement from user: "
								+ "<span class=\"text-primary\"><b>"
								+ message.sendFrom + "</b></span>");
				$('#game-handshake-response-modal').modal('show');

				var alertMessage = "<div class=\"alert alert-info\">"
						+ "<p>you are playing now with: <span class=\"text-info\"><b>"
						+ message.sendFrom + "</b></span></p>" + "</div>";

				$('#game-status').html(alertMessage);

				$('#quit-game-btn').data("gamePartner", message.sendFrom);

			} else if (message.type == "game-handshake-refuse") {

				$('#game-handshake-response-modal-title').html(
						"game refused from user: "
								+ "<span class=\"text-primary\"><b>"
								+ message.sendFrom + "</b></span>");
				$('#game-handshake-response-modal').modal('show');

			} else if (message.type == "quit-game"
					|| message.type == "goodbye-msg") {

				$('#game-status').html('');

			} else if (message.type == "try-later") {

				alert("user is playing now with someone else, \n or is during handshake with someone else,\n try later.");

			} else {
				showParticipants(event.data);

				$('#participants div ul li button.username')
						.each(
								function() {
									if ($(this).text().trim() == WEBSOCKET_CLIENT_NAME) {

										$(this)
												.parent()
												.find(
														'span.participants-action-btns')
												.remove();
										$(this).parent().css(
												'background-color', '#C9C9C9');
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

		$('#connection-status').html(
				"<div class=\"alert alert-warning connection-status-msg\">"
						+ "<h2>You are disconnected!</h2></div>");

		var disconnectBtn = $('#disconnect');
		var connectBtn = $('#connectToWebSocket');

		if (connectBtn.attr("disabled", true)) {
			connectBtn.removeAttr("class");
			connectBtn.removeAttr("disabled");
			connectBtn.attr("disabled", false);
			connectBtn.attr("class", "btn btn-primary");
		}
		disconnectBtn.removeAttr("class");
		disconnectBtn.attr("disabled", true);
		disconnectBtn.attr("class", "btn btn-default pull-right");

		$('#game-status').html('');

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
		sendFrom : WEBSOCKET_CLIENT_NAME,
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
		sendFrom : WEBSOCKET_CLIENT_NAME,
		sendTo : usernameToPlayWith
	}));

	$('#game-handshake-modal').modal('hide');

	var alertMessage = "<div class=\"alert alert-info\">"
			+ "<p>you are playing now with: <span class=\"text-info\"><b>"
			+ usernameToPlayWith + "</b></span></p>" + "</div>";

	$('#game-status').html(alertMessage);

	$('#quit-game-btn').data("gamePartner", usernameToPlayWith);

}

// --------------------------------------------------------

function quitGame() {

	webSocket.send(JSON.stringify({
		type : "quit-game",
		sendFrom : WEBSOCKET_CLIENT_NAME,
		sendTo : $('#quit-game-btn').data("gamePartner")
	}));

	$('#game-status').html('');

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
		sendFrom : WEBSOCKET_CLIENT_NAME,
		sendTo : usernameNotToPlayWith
	}));

	$('#game-handshake-modal').modal('hide');

}

// --------------------------------------------------------

function sendYourMoveByFenNotationToUser(reciever) {
	console.log("send-fen : " + fenFromYourMove.value);
	console.log(" to " + reciever);
	console.log(" from " + WEBSOCKET_CLIENT_NAME);
	var fenString = fenFromYourMove.value;
	webSocket.send(JSON.stringify({
		type : "chess-move",
		fen : fenString,
		sendFrom : WEBSOCKET_CLIENT_NAME,
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

	var participantsArr = JSON.parse(data);
	var participantsList = $('#participants div ul');
	participantsList.html('');

	for (var i = 0; i < participantsArr.length; i++) {

		var liElementOpening = '<li class="list-group-item game-user">';

		var participantData = '<button class="username btn"'
				+ 'onclick="showUSerInfoByAjax(' + '\''
				+ participantsArr[i].username + '\'' + ')"' + '>'
				+ '<span class="glyphicon glyphicon-user" />' + '&nbsp;'
				+ participantsArr[i].username + '</button>'
				+ '<span class="small"> ' + '&nbsp;'
				+ participantsArr[i].communicationStatus + '</span>';

		var participantPlayWithUserInfo = "";

		if (participantsArr[i].playNowWithUser != undefined
				&& participantsArr[i].playNowWithUser != '') {
			participantPlayWithUserInfo = '<span class="small text-success"> with <b>'
					+ participantsArr[i].playNowWithUser + '</b></span>';
		}

		if (participantsArr[i].communicationStatus == 'is-playing') {
			console.log('participant is playing');
			var participantActionBtns = '<span class="participants-action-btns">'
					+ '<button class="btn btn-sm btn-info send-to-user-btn" onclick="sendYourMoveByFenNotationToUser('
					+ '\''
					+ participantsArr[i].username
					+ '\''
					+ ')"'
					+ 'data-username="'
					+ participantsArr[i].username
					+ '">send-move</button>' + '</span>';
		} else {
			var participantActionBtns = '<span class="participants-action-btns">'
					+ '<button class="btn btn-sm btn-danger send-to-user-btn" onclick="inviteUserToGame('
					+ '\''
					+ participantsArr[i].username
					+ '\''
					+ ')"'
					+ 'data-username="'
					+ participantsArr[i].username
					+ '">invite</button>' + '</span>';
		}

		var liElementClosing = '</li>';

		var participantListElementContent = liElementOpening + participantData
				+ participantPlayWithUserInfo + participantActionBtns
				+ liElementClosing;

		participantsList.append(participantListElementContent);
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

