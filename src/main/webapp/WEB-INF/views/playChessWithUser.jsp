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
	<div id="chess-board-play-with-user" class="main-wrapper">
		<br />
		<!-- DECLARE VARIABLE isLoggedIn -->
		<security:authorize access="hasRole('ROLE_USER')" var="isLoggedIn" />
		<security:authorize access="hasRole('ROLE_USER')">
			<script
				src="<c:url value="${pageContext.request.contextPath}/resources/js/lib/chess.js" />"></script>


			<div class="site-title">
				<span class="text-left lead">Play chess with others</span> <span
					id="startPosBtn" class="btn btn-danger
					btn-sm">start
					new game</span>
			</div>
			<article id="aside-board">
				<div id="board" class="row-fluid"></div>
			</article>
			<!-- -------------------------- -->
			<article class="game-actions">
				<div class="stats">
					Game status: <span class=" text-info" id="status"></span>
				</div>
				<input type="text" id="fenFromPreviousMove" hidden="true" /> <input
					type="text" id="fenFromYourMove" hidden="true" />
				<section id="onlineUsersSection">
					<div id="connection-status"></div>
					<div id="game-status" data-isPlaying=""></div>

					<div id="play-with-opponent-interface" hidden="true">
						<span id="your-username"></span><span id="opponent-username"></span>
						<div id="move-for"></div>

						<div id="play-with-opponent-interface-actions">
							<button id="send-move-btn" class="btn btn-info"
								data-opponentName="">send-move</button>
							<button id="undo-move-btn" class="btn btn-default">undo-move</button>
							<button class="btn btn-warning pull-right" data-gamePartner=""
								id="quit-game-btn">quit game</button>
						</div>
					</div>

					<button class="btn btn-primary" id="connectToWebSocket">connect</button>
					<button class="btn btn-danger pull-right" id="disconnect">disconnect</button>
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

<jsp:include
	page="includes/modal_boxes/game_handshake_invitaion_modal.jsp" />
<jsp:include
	page="includes/modal_boxes/game_handshake_response_modal.jsp" />
<jsp:include page="includes/modal_boxes/user_info_modal.jsp" />

<!-- JAVASCRIPTS -->
<!-- tylko zalogowany uzytkownik ma zalaczone pliki javascript dotyczace gry w sieci -->
<security:authorize access="hasRole('ROLE_USER')">

	<!-- set variable websocket username for js -->
	<c:set var="sender" value="${currentUserName}" />
	<script>
		var WEBSOCKET_CLIENT_NAME = '${sender}';
	</script>

	<!-- Main -->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/playChessWithUserMain.js"></script>

	<!-- IMPORT WEBSOCKET CLIENT ENDPOINT SCRIPT -->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/websocketClientEndpoint.js">
		
	</script>

	<!-- IMPORT WEBSOCKET CLIENT MESSAGE PROTOCOL -->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/wsClientMessageExchangeProtocol.js">
		
	</script>

	<!-- IMPORT WEBSOCKET CLIENT ENDPOINT FUNCTIONS -->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/wsClientEndpointFunctions.js">
		
	</script>

	<!-- IMPORT CHESS LOGIC SCRIPT -->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/chessUserVsUser.js">
		
	</script>
</security:authorize>
<jsp:include page="includes/footer.jsp" />

