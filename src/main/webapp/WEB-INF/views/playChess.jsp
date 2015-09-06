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
			<h3 class="text-left">Play Chess</h3>
			<script
				src="<c:url value="${pageContext.request.contextPath}/resources/js/chess.js" />"></script>
			<div id="chess-board-play-with-computer">
				<div id="board"></div>
				<br />

			</div>

			<div class="game-stats-with-computer">
				<p class="text-danger">
					Status: <span id="status"></span>
				</p>
				<small class="text-info"> FEN: <br /> <span id="fen"></span>
				</small> <br /> <small class="text-warning"> PGN: <span id="pgn"></span>
				</small>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/chessUserVsComputer.js">
</script>
<jsp:include page="includes/footer.jsp" />

