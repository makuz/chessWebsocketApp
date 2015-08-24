<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="includes/header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%@ page import="com.chessApp.model.UserAccount"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container-fluid">
	<jsp:include page="includes/menu.jsp" />
	<div class="col-md-10 col-md-offset-1 ">
		<br />
		<div class="row contentWrapper">

			<section id="playSection">
				<h3 class="text-left">Play Chess</h3>
				<script
					src="<c:url value="${pageContext.request.contextPath}/resources/js/chess.js" />"></script>
				<div id="chess-board-play-with-user">
					<div id="board"></div>
					<div class="game-actions-user">
						<br /> <label>current fen</label><br /> <input type="text"
							id="fenFromYourMove" /> <br /> <br />
						<button id="sendYourMoveBtn" class="btn btn-info pull-right">send
							your move</button>
						<br /> <br /> <br />
						<div class="stats">
							<p class="text-danger">
								Status: <span id="status"></span>
							</p>
							<small class="text-info"> FEN: <br /> <span id="fen"></span>
							</small><br /> <small class="text-warning"> PGN: <span id="pgn"></span>
							</small>
						</div>
						<br />
						<p class="text-info">example FEN:</p>
						<small>rnbqkbnr/pppppppp/8/8/8/2P5/PP1PPPPP/RNBQKBNR b
							KQkq - 0 1</small> <label>construct game by FEN</label> <input
							type="text" id="fenToSet" /><br />
						<button id="setFenBtn" class="btn btn-info pull-right">construct
							game</button>
						<br /> <br />
						<hr />
						<button id="startPosBtn" class="btn btn-info pull-right">start
							new game</button>
					</div>
				</div>
			</section>

			<section id="onlineUsersSection">
				<br />
				<hr />
				<h3>online users: ${usersCount}</h3>
				<c:if test="${onlineUsers != null}">
					<ul class="list-group">
						<c:forEach items="${onlineUsers}" var="onlineUser">
							<li class="list-group-item"><span
								class="glyphicon glyphicon-user"></span>&nbsp&nbsp<span
								class="text-info">${onlineUser}</span></li>
						</c:forEach>
					</ul>
				</c:if>

			</section>

			<section id="testWsResponse">
				<br />
				<hr />
				<button id="connect">Połącz</button>
				<button id="disconnect">Rozłącz</button>
				<br />
				<h3>Testowa odpowiedź echo:</h3>
				<br />

				<div id="boardEcho" style="width: 400px"></div>

			</section>

		</div>
	</div>
</div>
<!-- set variable for js -->
<c:set var="username1" value="${currentUserName}" />
<script>
	var boardEcho = ChessBoard('boardEcho', fenFromYourMove.value);
</script>
<!-- WebSocket -->
<script>
	var endpointUrl = "ws://localhost:8080/send-fen/${username1}/${username1}";
	var webSocket = new WebSocket(endpointUrl);
	$(function() {

		// disconnect();

		$('#sendYourMoveBtn').click(function() {
			sendYourMoveByFenNotation();
		});

		$('#connect').click(function() {
			connectToUser("user");
		});

		$('#disconnect').click(function() {
			// disconnect();
		});

		$('#sendFen').click(function() {
			sendFen();
		});

		if (webSocket != undefined) {
			webSocket.onclose = function(msg) {
				webSocket.send("client disconnected");
				console.log("Server disconnected \n");
			}

			webSocket.onerror = function(msg) {
				webSocket.send("error: client disconnected");
				console.log("Server disconnected \n");
			}
		}

		function sendYourMoveByFenNotation() {

			if (fenFromYourMove.value != "close") {
				console.log("sended");

				// wysyla na server endpoint
				webSocket.send(fenFromYourMove.value);

			} else {
				webSocket.close();
			}

		}

		function connectToUser(user2) {
			// open the connection if one does not exist
			//if (webSocket !== undefined
			//		&& webSocket.readyState !== WebSocket.CLOSED) {
			//	return;
			//}

			var endpointUrl = "ws://localhost:8080/send-fen/${username1}/"
					+ user2;
			webSocket = new WebSocket(endpointUrl);

			$('#connect').disabled = false;

			webSocket.onopen = function(msg) {
				console.log("Server connected \n");

			}

			webSocket.onmessage = function(msg) {

				if (msg != null) {
					console.log("message: " + msg.data);
					// to wywoluje echo: onmessage
					var boardEcho = ChessBoard('boardEcho', msg.data);
				}

			}

		}

	});
</script>

<!-- CHESS JS -->
<script>
	var startFENPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	// load game object with fen
	$('#setFenBtn').click(function() {
		var fenStr = $('#fenToSet').val().trim();
		console.log("fenStr " + fenStr);
		board.position(fenStr);
		game = new Chess(fenStr);
		updateStatus();

	});

	//start new game
	$('#startPosBtn').click(function() {
		board.position(startFENPosition);
		game = new Chess(startFENPosition);
		updateStatus();
	});

	// chess script ---------------------------------
	var board, game = new Chess(), statusEl = $('#status'), fenEl = $('#fen'), pgnEl = $('#pgn');

	//do not pick up pieces if the game is over
	//only pick up pieces for the side to move
	var onDragStart = function(source, piece, position, orientation) {
		if (game.game_over() === true
				|| (game.turn() === 'w' && piece.search(/^b/) !== -1)
				|| (game.turn() === 'b' && piece.search(/^w/) !== -1)) {
			return false;
		}
	};

	var onDrop = function(source, target) {
		// see if the move is legal
		var move = game.move({
			from : source,
			to : target,
			promotion : 'q' // NOTE: always promote to a queen for example simplicity
		});

		// illegal move
		if (move === null)
			return 'snapback';

		updateStatus();
	};

	//update the board position after the piece snap 
	//for castling, en passant, pawn promotion
	var onSnapEnd = function() {
		board.position(game.fen());
	};

	var updateStatus = function() {
		var status = '';

		var moveColor = 'White';
		if (game.turn() === 'b') {
			moveColor = 'Black';
		}

		// checkmate?
		if (game.in_checkmate() === true) {
			status = 'Game over, ' + moveColor + ' is in checkmate.';
		}

		// draw?
		else if (game.in_draw() === true) {
			status = 'Game over, drawn position';
		}

		// game still on
		else {
			status = moveColor + ' to move';

			// check?
			if (game.in_check() === true) {
				status += ', ' + moveColor + ' is in check';
			}
		}

		statusEl.html(status);
		fenEl.html(game.fen());
		$('#fenFromYourMove').val(game.fen());
		pgnEl.html(game.pgn());
	};

	var cfg = {
		draggable : true,
		position : 'start',
		onDragStart : onDragStart,
		onDrop : onDrop,
		onSnapEnd : onSnapEnd
	};
	board = ChessBoard('board', cfg);

	updateStatus();
</script>

<jsp:include page="includes/footer.jsp" />

