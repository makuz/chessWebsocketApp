<jsp:include page="includes/header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%@ page import="com.chessApp.model.UserAccount"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container-fluid">
	<jsp:include page="includes/menu.jsp" />
	<div class="main-wrapper">

		<div class="wrapperForKeepCenterPosition">
			<div class="site-title">
				<h2 class="text-center main-font-color">Play chess with computer</h2>
			</div>
			<script
				src="<c:url value="${pageContext.request.contextPath}/resources/js/lib/chess.js" />"></script>
			<article id="chess-board-play-with-computer">
				<div id="board"></div>
				<br />
			</article>
			<article class="game-stats-with-computer">
				<p class="text-danger">
					Status: <span id="status"></span>
				</p>
				<small class="text-info"> FEN: <br /> <span id="fen"></span>
				</small> <br /> <small class="text-warning"> PGN: <span id="pgn"></span>
				</small>
			</article>

		</div>

	</div>
</div>
<!-- IMPORT CHESS SCRIPT -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/chessUserVsComputer.js">
	
</script>
<jsp:include page="includes/footer.jsp" />

