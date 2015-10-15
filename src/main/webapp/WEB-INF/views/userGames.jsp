<jsp:include page="includes/header.jsp" />
<div class="container-fluid">
	<jsp:include page="includes/menu.jsp" />
	<div class="main-wrapper">
		<%@ page import="java.util.List"%>
		<%@ page import="com.chessApp.model.UserAccount"%>
		<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<%@ page import="com.chessApp.props.ChessAppProperties"%>
		<%
			String contextURL = ChessAppProperties
					.getProperty("app.contextpath");
		%>
		<script
			src="<c:url value="${pageContext.request.contextPath}/resources/js/userGamesHistory.js" />"></script>

		<div id="user-games-stats" class="text-center nice-green-backgroud">
			<c:if test="${user.numberOfGamesPlayed ne null}">
				<div>
					<span class="white text-uppercase">number of games palyed : </span>
					${user.numberOfGamesPlayed}
				</div>
			</c:if>
			<c:if test="${user.numberOfWonChessGames ne null}">
				<div>
					<span class="nice-blue-font-color text-uppercase">number of won games : </span>
					${user.numberOfWonChessGames}
				</div>
			</c:if>
			<c:if test="${user.numberOfLostChessGames ne null}">
				<div>
					<span class="text-danger text-uppercase">number of lost games : </span>
					${user.numberOfLostChessGames}
				</div>
			</c:if>
		</div>

		<div id="gamesTable">
			<h3 class="text-center">Your chess games</h3>
			<jsp:include page="includes/tables/userGamesTable.jsp"></jsp:include>
		</div>
	</div>
</div>
<jsp:include
	page="includes/modal_boxes/user_end_chess_game_pos_modal.jsp" />

<jsp:include page="includes/footer.jsp" />