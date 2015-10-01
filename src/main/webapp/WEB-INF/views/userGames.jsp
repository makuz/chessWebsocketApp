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
			src="<c:url value="${pageContext.request.contextPath}/resources/js/main.js" />"></script>
		<div id="gamesTable">
			<h1 class="text-center">Your chess games</h1>
			<jsp:include page="includes/tables/userGamesTable.jsp"></jsp:include>
		</div>
	</div>
</div>
<jsp:include page="includes/modal_boxes/user_end_chess_game_pos_modal.jsp" />

<jsp:include page="includes/footer.jsp" />