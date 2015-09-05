<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="includes/header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%@ page import="com.chessApp.model.UserAccount"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<div class="container-fluid">
	<jsp:include page="includes/menu.jsp" />
	<div class="col-md-10 col-md-offset-1 ">
		<br />
		<div class="row contentWrapper">

			<security:authorize access="hasRole('ROLE_USER')" var="isLoggedIn" />
			<security:authorize access="hasRole('ROLE_USER')">
				<section id="playSection">
					<h3 class="text-left">Play Chess</h3>
					<script
						src="<c:url value="${pageContext.request.contextPath}/resources/js/chess.js" />"></script>
					<div id="chess-board-play-with-user">
						<div id="board"></div>
						<div class="game-actions-user">
							<br /> <br />
							<div class="stats">
								<p class="text-danger">
									Status: <span id="status"></span>
								</p>
								<small class="text-info"> FEN: <br /> <span id="fen"></span>
								</small><br /> <small class="text-warning"> PGN: <span id="pgn"></span>
								</small>
							</div>
							<hr />
							<br /> <input hidden="true" type="text" id="fenFromYourMove" />
							<br />
							<button id="sendYourMoveBtn" class="btn btn-success pull-right">send
								your move to all</button>
							<br />
							<hr />
							<button id="startPosBtn" class="btn btn-danger pull-right">start
								new game</button>
							<br />
							<section id="onlineUsersSection">
								<br />


								<hr />
								<h3>online users: ${usersCount}</h3>
								<c:if test="${onlineUsers != null}">
									<ul class="list-group">
										<c:forEach items="${onlineUsers}" var="onlineUser">
											<li class="list-group-item"><span
												class="glyphicon glyphicon-user"></span>&nbsp&nbsp<span
												class="text-info">${onlineUser}</span> <c:choose>
													<c:when test="${currentUserName == onlineUser }">
														<button class="btn btn-default sendToUserBtn"
															data-username="${onlineUser}" disabled="true">send
															move</button>
													</c:when>
													<c:otherwise>
														<button class="btn btn-info sendToUserBtn"
															data-username="${onlineUser}">send move</button>
													</c:otherwise>
												</c:choose>
												<button data-username="${onlineUser}"
													class="btn btn-warning btn-sm user-info-btn">user
													info</button></li>
										</c:forEach>
									</ul>
								</c:if>


								<hr />
								<button class="btn btn-warning pull-right"
									id="connectToWebSocket">Połącz</button>
								<br /> <br />
								<button class="btn btn-danger pull-right" id="disconnect">Rozłącz</button>
								<br />
								<hr />
								<div id="participants">
									<h3>Participants :</h3>
									<br />
									<div id="participants-user-names"></div>

								</div>
							</section>
						</div>
					</div>

				</section>

			</security:authorize>
			<c:if test="${!isLoggedIn}">
				<div class="alert alert-warning" id="play-chess-not-loggedin-alert"
					role="alert">
					<h3>You have to be logged in to play chess with other users</h3>
				</div>
				<a class="btn btn-success pull-right" href="/signin">sign in</a>
			</c:if>
		</div>
	</div>
</div>

<jsp:include page="includes/modal_boxes/user_info_modal.jsp" />

<!-- JAVASCRIPTS -->
<!-- tylko zalogowany uzytkownik ma wlaczony poniższy javascript -->
<security:authorize access="hasRole('ROLE_USER')">
	<!-- set variable for js -->
	<c:set var="sender" value="${currentUserName}" />

	<!-- WebSocket CLIENT ENDPOINT SCRIPT -->
	<script>
		/**
		 * WEBSOCKET CLIENT
		 */

		// main ------------------------------------------------
		$(function() {

			$('.user-info-btn').click(
					function() {
						console.log("$('.user-info-btn').click()");
						$('#user-info-modal').modal('show');
						var login = $(this).data('username');

						$.ajax({
							url : "user/get-user-info-by-username",
							data : {
								username : login
							},
							success : function(response) {
								$('#usernameResponse').text(
										"login: " + response.username);
								$('#userIdResponse').text(
										"id: " + response.userId);
								$('#userEmailResponse').text(
										"email: " + response.email);
								$('#userNameResponse').text(
										"name: " + response.name);
								$('#userLastnameResponse').text(
										"lastname: " + response.lastname);
							}
						}

						);

					});

			$('#sendYourMoveBtn').click(function() {
				sendYourMoveByFenNotation();
			});

			$('#connectToWebSocket').click(function(event) {
				connectToWebSocket();
				event.target = "color: blue;";
			});

			$('#disconnect').click(function() {
				closeWsConnection();
			});

			$('.sendToUserBtn').click(function(event) {
				console.log("sendToUserBtn.click()");
				var reciever = $(this).data('username');
				console.log(reciever);
				sendYourMoveByFenNotationToUser(reciever);
				event.target.style = "color: red;";

			});

			$('#sendFen').click(function() {
				sendFen();
			});

		});

		// functions -------------------------------------------

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
					$('#userLastnameResponse').text(
							"lastname: " + response.lastname);
				}
			});
		}

		function sendYourMoveByFenNotation() {
			console.log("send fen : " + fenFromYourMove.value);
			var fenString = fenFromYourMove.value;
			webSocket.send(JSON.stringify({
				type : "chess-move",
				fen : fenString,
				senderName : '${sender}'
			}));

		};

		function sendYourMoveByFenNotationToUser(reciever) {
			console.log("send-fen : " + fenFromYourMove.value);
			console.log(" to " + reciever);
			console.log(" from " + '${sender}');
			var fenString = fenFromYourMove.value;
			webSocket.send(JSON.stringify({
				type : "chess-move",
				fen : fenString,
				senderName : '${sender}',
				sendTo : reciever
			}));

		};

		function closeWsConnection() {
			console.log('closeWsConnection()');
			$('.connectToUserBtn').css("color", "white");
			webSocket.close();

		};

		function connectToWebSocket() {
			console.log('connectToWebSocket()');

			// init websocket -------------------------------------

			var endpointUrl = "ws://" + document.location.host
					+ "/send-fen/${sender}";
			webSocket = new WebSocket(endpointUrl);
			$('#connect').disabled = false;

			// websocketClient events -----------------------------

			webSocket.onopen = function(event) {
				console.log("Server connected \n");
				console.log(event.data);
				webSocket.send(JSON.stringify({
					type : "welcome-msg",
					senderName : '${sender}'
				}));

			};

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
					} else {

						showParticipants(event.data, $('#participants div'));

					}
				}

			};

			function showParticipants(data, htmlObject) {
				var usersArr = JSON.parse(data);
				var usernames = new Array();
				for (var i = 0; i < usersArr.length; i++) {
					usernames.push(usersArr[i].username);
				}

				var usersPre = htmlObject;
				usersPre.html('');
				var allText = "";
				usersPre.append('<ul class="list-group">');
				for (var i = 0; i < usernames.length; i++) {
					usersPre.append('<li class="list-group-item game-user">');

					var userData = '<span class="glyphicon glyphicon-user"></span>&nbsp&nbsp'
							+ '<span class="text-info username">'
							+ usernames[i]
							+ '</span>'
							+ '<button class="btn btn-info sendToUserBtn" onclick="sendYourMoveByFenNotationToUser('
							+ '\''
							+ usernames[i]
							+ '\''
							+ ')"'
							+ 'data-username="'
							+ usernames[i]
							+ '">send move</button>'
							+ '<button data-username="'
							+ usernames[i]
							+ '"'
							+ 'onclick="showUSerInfoByAjax('
							+ '\''
							+ usernames[i]
							+ '\''
							+ ')"'
							+ 'class="btn btn-warning btn-sm user-info-btn">user'
							+ 'info</button>';

					usersPre.append(userData);
					usersPre.append('</li>');
				}
				usersPre.append('</ul>');
			}

			webSocket.onclose = function(event) {
				webSocket.send("client disconnected");
				console.log("Server disconnected \n");
				console.log(event);
			};

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
</security:authorize>
<jsp:include page="includes/footer.jsp" />

