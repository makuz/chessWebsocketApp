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

			<!-- DECLARE VARIABLE isLoggedIn -->
			<security:authorize access="hasRole('ROLE_USER')" var="isLoggedIn" />
			<security:authorize access="hasRole('ROLE_USER')">

				<span class="text-left lead">Play Chess</span>
				<span id="startPosBtn" class="btn btn-danger
					btn-sm">start
					new game</span>
				<script
					src="<c:url value="${pageContext.request.contextPath}/resources/js/lib/chess.js" />"></script>
				<div id="chess-board-play-with-user">
					<article id="aside-board">
						<div id="board"></div>
					</article>
					<!-- -------------------------- -->
					<article class="game-actions">
						<div class="stats">
							<p class="text-danger">
								Status: <span id="status"></span>
							</p>
							<small class="text-info"> FEN: <br /> <span id="fen"></span>
							</small><br /> <small class="text-warning"> PGN: <span id="pgn"></span>
							</small>
						</div>
						<input hidden="true" type="text" id="fenFromYourMove" />
						<section id="onlineUsersSection">
							<div id="connection-status"></div>
							<div id="game-status" data-isPlaying=""></div>
							<hr />

							<div id="play-with-opponent-interface">
								<span id="your-username">you: </span><span
									id="opponent-username">oppeonent: </span>
								<div id="move-for">move for:</div>

								<button id="send-move-btn" class="btn btn-info"
									data-opponentName="">send-move</button>
								<button class="btn btn-warning pull-right" data-gamePartner=""
									id="quit-game-btn">quit game</button>

							</div>

							<hr />
							<button class="btn btn-primary" id="connectToWebSocket">Połącz
								mnie</button>
							<button class="btn btn-danger pull-right" id="disconnect">Rozłącz
								mnie</button>

							<hr />

							<div id="participants">
								<h3>Participants :</h3>
								<br />
								<div id="participants-user-names">
									<ul class="list-group"></ul>

								</div>

							</div>
						</section>
					</article>
				</div>



			</security:authorize>
			<c:if test="${!isLoggedIn}">
				<div class="alert-log-in-to-play">
					<div class="alert alert-warning" id="play-chess-not-loggedin-alert"
						role="alert">
						<h3>You have to be logged in to play chess with other users</h3>
					</div>
					<div id="play-chess-not-loggedin-btns" class="pull-right">
						<a class="btn btn-info" href="/login">log in</a> <a
							class="btn btn-success" href="/signin">sign in</a>
					</div>
				</div>
			</c:if>
		</div>
	</div>
</div>

<jsp:include
	page="includes/modal_boxes/game_handshake_invitaion_modal.jsp" />
<jsp:include
	page="includes/modal_boxes/game_handshake_response_modal.jsp" />
<jsp:include page="includes/modal_boxes/user_info_modal.jsp" />

<!-- JAVASCRIPTS -->
<!-- tylko zalogowany uzytkownik ma wlaczony poniższy javascript -->
<security:authorize access="hasRole('ROLE_USER')">

	<!-- set variable websocket username for js -->
	<c:set var="sender" value="${currentUserName}" />

	<!-- WebSocket -->
	<script>
		// main ------------------------------------------------
		var WEBSOCKET_CLIENT_NAME = '${sender}';

		$(function() {

			$('#game-status').data('isPlaying', false);

			$('#connectToWebSocket').click(function(event) {
				connectToWebSocket();
			});

			$('#disconnect').click(function() {
				closeWsConnection();
			});

			$('#quit-game-btn').click(function() {
				quitGame();
			});

			$('#send-move-btn').click(function() {
				sendYourMoveByFenNotationToUser();
			});

		});
	</script>
	<!-- IMPORT WEBSOCKET CLIENT ENDPOINT SCRIPT -->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/websocketClientEndpoint.js">
		
	</script>

	<!-- IMPORT CHESS SCRIPT -->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/chessUserVsUser.js">
		
	</script>
</security:authorize>
<jsp:include page="includes/footer.jsp" />

