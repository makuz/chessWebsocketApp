<jsp:include page="includes/header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%@ page import="com.chessApp.model.UserAccount"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container-fluid">
	<jsp:include page="includes/menu.jsp" />
		<div class="main-wrapper">
			<h3 class="text-left">Play Chess with us</h3>
			<br />
			<script
				src="<c:url value="${pageContext.request.contextPath}/resources/js/lib/chess.js" />"></script>

			<img id="home-img" class="img-responsive"
				src="<c:url value="${pageContext.request.contextPath}/resources/images/chess2.jpg" />" />

			<div id="chess-board-home">
				<div class="game-stats-home">
					<p class="text-danger">
						Status: <span id="status"></span>
					</p>
					<small class="text-info"> FEN: <br /> <span id="fen"></span>
					</small>
				</div>
				<br />
				<div id="board"></div>
			</div>
		</div>
</div>
<!-- IMPORT CHESS SCRIPT -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/chessComputerVsComputer.js"></script>

<jsp:include page="includes/footer.jsp" />

