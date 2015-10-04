/**
 * WEBSOCKET CLIENT ENDPOINT
 * 
 * websocket client variable name = WEBSOCKET_CLIENT_NAME
 * 
 * GAME PIECE COLOR STATUGLOBAL VAR = SENDED_CHESS_MOVE_STATUS
 * 
 */
var TIMEOUT_FOR_HANDSHAKE = 15;
var CLICK_REFUSED_FLAG = false;
var CLICK_AGREEMENT_FLAG = false;

// ------CONNECT TO WEBSOCKET FUNCTION, WEBSOCKET EVENTS----------------------
function connectToWebSocket() {
	console.log('connectToWebSocket()');

	// ----init websocket -------------------------------------
	var endpointUrl = "ws://" + document.location.host + "/chessapp-live-game/"
			+ WEBSOCKET_CLIENT_NAME;
	webSocket = new WebSocket(endpointUrl);

	// websocketClient events -----------------------------

	webSocket.onopen = function(event) {
		console.log("Server connected \n");
		console.log(event.data);

		$('#disconnect').attr("disabled", false);

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

		if (event != null) {
			var message = JSON.parse(event.data);
			console.log("message");
			console.log(message);

			if (message.type == "chess-move") {
				console.log("chess-move");

				var fenStr = message.fen;
				if (fenStr != null && fenStr != "") {
					$('#fenFromPreviousMove').val(fenStr);

					board.position(fenStr);
					game = new Chess(fenStr);
					updateStatus();
				}

				SENDED_CHESS_MOVE_STATUS = message.moveStatus;
				CHESS_MOVE_COUNTER = 0;
				SEND_MOVE_CLICK_COUNTER = 0;

				showActualMoveStatus();

			} else if (message.type == "game-handshake-invitation") {
				console.log("game-handshake-invitation");

				showGameHandshakeModalBox(message.sendFrom);
				startTimeoutForHandshakeForInvitedUser();

				setChessColorGlobalVars(message);

				var yourChessColorsInfo = "you: <span class=\"text-info\"><b>"
						+ WEBSOCKET_CLIENT_NAME + "</b></span> | color: "
						+ message.sendToObj.chessColor + " "
						+ "<span class=\"glyphicon glyphicon-hand-right\"/> ";

				$('#your-username').html(yourChessColorsInfo);

				var opponentChessColorsInfo = "opponent: <span class=\"text-info\"><b>"
						+ message.sendFrom
						+ "</b></span> | color: "
						+ message.sendFromObj.chessColor + " ";

				$('#opponent-username').html(opponentChessColorsInfo);

			} else if (message.type == "game-handshake-agreement") {
				console.log("game-handshake-agreement");

				CLICK_AGREEMENT_FLAG = true;
				SEND_MOVE_CLICK_COUNTER = 0;
				SENDED_CHESS_MOVE_STATUS = message.moveStatus;
				OPPONENT_USERNAME = message.sendFrom;

				$('#play-with-opponent-interface').attr("hidden", false);
				$('#play-with-opponent-interface-actions')
						.attr("hidden", false);
				$('#startPosBtn').hide();
				$('#fenFromPreviousMove').val(startFENPosition);
				$('#game-status').data('isPlaying', true);

				startNewGame();
				setChessColorGlobalVars(message)
				showActualMoveStatus();

				var agreementModalInfo = "game agreement from user: "
						+ "<span class=\"text-primary\"><b>" + message.sendFrom
						+ "</b></span>";

				$('#game-handshake-response-modal-title').html(
						agreementModalInfo);

				$('#game-handshake-response-modal').modal('show');

				var youArePlayingWithInfo = "<div class=\"alert alert-info\">"
						+ "<p>you are playing now with: <span class=\"text-info\"><b>"
						+ message.sendFrom + "</b></span></p>" + "</div>";

				$('#game-status').html(youArePlayingWithInfo);

				var yourChessColorsInfo = "you: <span class=\"text-info\"><b>"
						+ WEBSOCKET_CLIENT_NAME + "</b></span> | color: "
						+ message.sendToObj.chessColor + " "
						+ "<span class=\"glyphicon glyphicon-hand-right\"/> "

				$('#your-username').html(yourChessColorsInfo);

				var opponentChessColorsInfo = "opponent: <span class=\"text-info\"><b>"
						+ message.sendFrom
						+ "</b></span> | color: "
						+ message.sendFromObj.chessColor + " ";

				$('#opponent-username').html(opponentChessColorsInfo);
				$('#send-move-btn').data("opponentName", message.sendFrom);
				$('#quit-game-btn').data("gamePartner", message.sendFrom);

			} else if (message.type == "game-handshake-refuse") {
				console.log("game-handshake-refuse");

				CLICK_REFUSED_FLAG = true;
				SENDED_CHESS_MOVE_STATUS = "";

				var refuseModalInfo = "game refused from user: "
						+ "<span class=\"text-primary\"><b>" + message.sendFrom
						+ "</b></span>";

				$('#game-handshake-response-modal-title').html(refuseModalInfo);
				$('#game-handshake-response-modal').modal('show');
				$('#game-status').html('');

			} else if (message.type == "quit-game"
					|| message.type == "goodbye-msg") {

				OPPONENT_USERNAME = "";

				if (message.type == "quit-game") {
					alert("user quit game");
					CHESS_MOVE_COUNTER = 0;
				}

				$('#startPosBtn').show();
				$('#game-status').data('isPlaying', false);
				$('#play-with-opponent-interface').attr("hidden", true);
				$('#game-status').html('');
				$('#opponent-username').html('');
				$('#send-move-btn').data("opponentName", '');
				$('#quit-game-btn').data("gamePartner", '');

				clearParticipantsListView();

			} else if (message.type == "try-later") {

				alert("user is playing now with someone else, \n or is during handshake with someone else,\n try later.");

			} else if (message.type == "game-over") {

				SENDED_CHESS_MOVE_STATUS = message.moveStatus;
				OPPONENT_USERNAME = "";

				var fenStr = message.fen;
				if (fenStr != null && fenStr != "") {

					$('#fenFromPreviousMove').val(fenStr);
					board.position(fenStr);
					game = new Chess(fenStr);
					updateStatus();
				}

				showActualMoveStatus();

				if (message.checkMate == true) {
					$('#move-for').html(
							"<h1 class=\"text-danger\">YOU LOOSE !</h1>");
					alert("check mate, you loose! with " + OPPONENT_USERNAME);

					$('#startPosBtn').show();
					$('#game-status').data('isPlaying', false);
					$('#game-status').html('');
					$('#send-move-btn').data("opponentName", '');
					$('#quit-game-btn').data("gamePartner", '');
					$('#play-with-opponent-interface-actions').attr("hidden",
							true);

				}

				clearParticipantsListView();

			} else {
				showParticipants(event.data);
				clearParticipantsListView();
			}
		}

	};

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
		$('#participants div ul').html('');
		$('#disconnect').attr("disabled", true);
		$('#play-with-opponent-interface').attr("hidden", true);
		$('#startPosBtn').show();

		OPPONENT_USERNAME = "";

		console.log(event);
	};

	// -----------------------------------

	webSocket.onerror = function(event) {
		webSocket.send("error: client disconnected");
		console.log("Server disconnected \n");
		console.log(event);
		webSocket.close();
		$('#startPosBtn').show();
		$('#disconnect').attr("disabled", true);
		$('#play-with-opponent-interface').attr("hidden", true);
		OPPONENT_USERNAME = "";
	};

};

// close websocket when page reload --------------------------------------------

window.onbeforeunload = function() {
	webSocket.onclose = function() {
		webSocket.send("client disconnected");
		console.log("Server disconnected \n");
	}; // disable onclose handler first
	webSocket.close();
	$('#connectToWebSocket').attr("disabled", false);
	OPPONENT_USERNAME = "";

	window.location.reload(false);

};
