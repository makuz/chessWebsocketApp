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

// functions -------------------------------------------

function testCheckMateSaveToDb() {

	var checkMatePos = "4kb2/1pp1ppp1/5n2/r4K1b/3q4/PPP5/R2P1PP1/2B1r3 w - - 7 20";
	fenFromYourMove.value = checkMatePos;
	board.position(checkMatePos);
	game = new Chess(checkMatePos);

}
// -------------------------------------------------------

function undoMove() {
	console.log('undoMove()');

	CHESS_MOVE_COUNTER = 0;
	var prevoiusPos = fenFromPreviousMove.value;
	board.position(prevoiusPos);
	game = new Chess(prevoiusPos);
	updateStatus();

}

// -----------------------------------------------------

function setChessColorGlobalVars(msgObj) {
	console.log('setChessColorGlobalVars()');

	if (msgObj.sendToObj.chessColor == 'white') {
		WHITE_COLOR_USERNAME = msgObj.sendToObj.username;
		BLACK_COLOR_USERNAME = msgObj.sendFromObj.username;
	} else {
		WHITE_COLOR_USERNAME = msgObj.sendFromObj.username;
		BLACK_COLOR_USERNAME = msgObj.sendToObj.username;
	}

}

// -----------------------------------------------------

function showGameHandshakeModalBox(sender) {

	var modalBoxMsg = "Do you want to play with: "
			+ "<span class=\"text-primary\"><b>" + sender + "</b></span>";

	$('#game-handshake-modal-title').html(modalBoxMsg);
	$('#game-handshake-msgTo').val(sender);
	$('#game-handshake-modal').modal('show');

}

function showTimerForInviter(recieverName) {
	console.log('showTimerForInviter()');

	if (TIMEOUT_FOR_HANDSHAKE == 0 || CLICK_REFUSED_FLAG == true
			|| CLICK_AGREEMENT_FLAG == true) {

		setTimeOutForHandshake_RefuseFlag_AgreementFlag_ForStartValues();
		clearUserInetrfaceForTimer();
		clearTimeout(inviterTimer);
		return;
	} else {
		TIMEOUT_FOR_HANDSHAKE--;

		var timerInfo = '<div class="alert alert-info">' + '<span><strong>'
				+ recieverName + '</strong> considering... <strong>'
				+ TIMEOUT_FOR_HANDSHAKE + '</strong></span></div>';

		$('#game-status').html(timerInfo);
	}

	var inviterTimer = setTimeout(function() {
		showTimerForInviter(recieverName)
	}, 1000);

}

function setTimeOutForHandshake_RefuseFlag_AgreementFlag_ForStartValues() {
	console
			.log("setTimeOutForHandshake_RefuseFlag_AgreementFlag_ForStartValues()");

	TIMEOUT_FOR_HANDSHAKE = 15;
	CLICK_REFUSED_FLAG = false;
	CLICK_AGREEMENT_FLAG = false;
}

function startTimeoutForHandshakeForInvitedUser() {
	console.log('setChessColorGlobalVars()');

	if (CLICK_REFUSED_FLAG == true || CLICK_AGREEMENT_FLAG == true) {

		setTimeOutForHandshake_RefuseFlag_AgreementFlag_ForStartValues();

		clearTimeout(reciverTimer);
		return;
	}

	if (TIMEOUT_FOR_HANDSHAKE == 0) {

		refusedToPlay();
		setTimeOutForHandshake_RefuseFlag_AgreementFlag_ForStartValues();
		clearTimeout(reciverTimer);
		return;
	} else {
		TIMEOUT_FOR_HANDSHAKE--;
		$('#game-handshake-timer').html(TIMEOUT_FOR_HANDSHAKE);
	}

	var reciverTimer = setTimeout(function() {
		startTimeoutForHandshakeForInvitedUser()
	}, 1000);

}

// -----------------------------------

function clearParticipantsListView() {
	console.log("clearParticipantsListView()");

	var participantListUsernameBtns = $('#participants div ul li button.username');

	participantListUsernameBtns.each(function() {

		if ($(this).text().trim() == WEBSOCKET_CLIENT_NAME) {

			$(this).parent().find('span.participants-action-btns').remove();
			$(this).parent().css('background-color', '#C9C9C9');
		}

		if ($('#game-status').data('isPlaying') == true) {
			$(this).parent().find('span.participants-action-btns').attr(
					'hidden', true);
		} else {
			$(this).parent().find('span.participants-action-btns').attr(
					'hidden', false);
		}

	});
}

// -----------------------------------

function inviteUserToGame(reciever) {
	console.log("inviteUserToGame()");
	console.log("game-handshake from " + WEBSOCKET_CLIENT_NAME);
	console.log(" to " + reciever);

	CHESS_MOVE_COUNTER = 0;
	webSocket.send(JSON.stringify({
		type : "game-handshake-invitation",
		sendFrom : WEBSOCKET_CLIENT_NAME,
		sendTo : reciever
	}));

	showTimerForInviter(reciever);

}

// --------------------------------------------------------

function showActualMoveStatus() {
	console.log("showActualMoveStatus");

	if (SENDED_CHESS_MOVE_STATUS == $('#status').text().trim()) {
		$('#move-for').html('<p class=\"text-success\">Your move</p>');
	} else {
		$('#move-for').html('<p class=\"text-danger\">Move for opponent</p>');
	}

}

// --------------------------------------------------------

function agreementToPlay() {
	console.log("agreementToPlay()");

	CLICK_AGREEMENT_FLAG = true;

	var usernameToPlayWith = $('#game-handshake-msgTo').val();
	OPPONENT_USERNAME = usernameToPlayWith;
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

	$('#game-status').data('isPlaying', true);
	$('#startPosBtn').hide();
	$('#fenFromPreviousMove').val(startFENPosition);

	$('#play-with-opponent-interface').attr("hidden", false);
	$('#play-with-opponent-interface-actions').attr("hidden", false);

	var alertMessage = "<div class=\"alert alert-info\">"
			+ "<p>you are playing now with: <span class=\"text-info\"><b>"
			+ usernameToPlayWith + "</b></span></p>" + "</div>";

	$('#game-status').html(alertMessage);
	$('#send-move-btn').data("opponentName", usernameToPlayWith);
	$('#quit-game-btn').data("gamePartner", usernameToPlayWith);

	showActualMoveStatus();
	startNewGame();

}

// --------------------------------------------------------

function quitGame() {

	var endFen = fenFromYourMove.value;
	CHESS_MOVE_COUNTER = 0;

	webSocket.send(JSON.stringify({
		type : "quit-game",
		whiteColUsername : WHITE_COLOR_USERNAME,
		blackColUsername : BLACK_COLOR_USERNAME,
		fen : endFen,
		sendFrom : WEBSOCKET_CLIENT_NAME,
		sendTo : $('#quit-game-btn').data("gamePartner")
	}));

	$('#game-status').data('isPlaying', false);
	$('#startPosBtn').show();
	$('#play-with-opponent-interface').attr("hidden", true);
	$('#game-status').html('');
	$('#opponent-username').html('');
	$('#send-move-btn').data("opponentName", '');
	$('#quit-game-btn').data("gamePartner", '');

	OPPONENT_USERNAME = "";

}

// --------------------------------------------------------

function refusedToPlay() {
	console.log("refusedToPlay()");

	CLICK_REFUSED_FLAG = true;
	var usernameNotToPlayWith = $('#game-handshake-msgTo').val();
	var myUserName = WEBSOCKET_CLIENT_NAME;

	console.log("game-handshake-refuse from");
	console.log(myUserName + " to " + usernameNotToPlayWith);

	webSocket.send(JSON.stringify({
		type : "game-handshake-refuse",
		sendFrom : WEBSOCKET_CLIENT_NAME,
		sendTo : usernameNotToPlayWith
	}));

	clearUserInetrfaceForTimer();

}

// -------------------------------------------------------

function clearUserInetrfaceForTimer() {
	$('#game-status').html('');
	$('#your-username').html('');
	$('#opponent-username').html('');
	$('#game-handshake-modal').modal('hide');
}

// --------------------------------------------------------

function sendYourMoveByFenNotationToUser() {

	SEND_MOVE_CLICK_COUNTER = 1;
	console.log("sendYourMoveByFenNotationToUser()");
	console.log("current chess move");
	console.log(CURRENT_CHESS_MOVE);

	var reciever = $('#send-move-btn').data('opponentName');

	var chessMoveStatus = $('#status').text().trim();

	console.log("send-fen : " + fenFromYourMove.value);
	console.log(" to " + reciever);
	console.log(" from " + WEBSOCKET_CLIENT_NAME);
	var fenString = fenFromYourMove.value;
	webSocket.send(JSON.stringify({
		type : "chess-move",
		fen : fenString,
		moveStatus : chessMoveStatus,
		chessMove : CURRENT_CHESS_MOVE,
		sendFrom : WEBSOCKET_CLIENT_NAME,
		sendTo : reciever
	}));

};

// -----------------------------------------------
function closeWsConnection() {
	console.log('closeWsConnection()');
	$('.connectToUserBtn').css("color", "white");
	$('#game-status').data('isPlaying', false);
	OPPONENT_USERNAME = "";
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
		var participantData = "";

		if (participantsArr[i].communicationStatus == 'is-playing') {

			participantData = '<button class="username btn"'
					+ 'onclick="showUSerInfoByAjax(' + '\''
					+ participantsArr[i].username + '\'' + ')"' + '>'
					+ '<span class="glyphicon glyphicon-user text-danger" />'
					+ '&nbsp;' + participantsArr[i].username + '</button>'
					+ '<span class="small"> ' + '&nbsp;'
					+ participantsArr[i].communicationStatus + ' </span>'
					+ '<span class="participant-timer"></span>';
		} else {
			participantData = '<button class="username btn"'
					+ 'onclick="showUSerInfoByAjax(' + '\''
					+ participantsArr[i].username + '\'' + ')"' + '>'
					+ '<span class="glyphicon glyphicon-user text-success" />'
					+ '&nbsp;' + participantsArr[i].username + '</button>'
					+ '<span class="small text-info"> ' + '&nbsp;'
					+ participantsArr[i].communicationStatus + ' </span>'
					+ '<span class="participant-timer"></span>';
		}

		var participantPlayWithUserInfo = "";

		if (participantsArr[i].playNowWithUser != undefined
				&& participantsArr[i].playNowWithUser != '') {
			participantPlayWithUserInfo = '<span class="small text-success"> with <b>'
					+ participantsArr[i].playNowWithUser + '</b></span>';
		}

		var participantActionBtns = "";

		if (participantsArr[i].communicationStatus == 'wait-for-new-game') {

			participantActionBtns = '<span class="participants-action-btns">'
					+ '<button class="btn btn-sm btn-danger send-to-user-btn" onclick="inviteUserToGame('
					+ '\'' + participantsArr[i].username + '\'' + ')"'
					+ 'data-username="' + participantsArr[i].username
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

