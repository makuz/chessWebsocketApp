<jsp:include page="includes/header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%@ page import="com.chessApp.model.UserAccount"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script
	src="<c:url value="${pageContext.request.contextPath}/resources/js/lib/chess.js" />"></script>

<div class="container-fluid">
	<jsp:include page="includes/menu.jsp" />
	<div class="main-wrapper">

		<div class="wrapperForKeepCenterPosition">

			<article id="home-img-article">
				<div class="site-title">
					<h3 class="text-left">Play Chess with us</h3>
				</div>
				<img id="home-img" class="img-responsive"
					src="<c:url value="${pageContext.request.contextPath}/resources/images/chess.jpg" />" />
			</article>

			<article id="chess-board-home">
				<div id="board"></div>
			</article>

			<div class="game-stats-home">
				<p class="text-danger">
					Status: <span id="status"></span>
				</p>
				<small class="text-info"> FEN: <br /> <span id="fen"></span>
				</small>
			</div>

		</div>
	</div>
</div>
<!-- IMPORT CHESS SCRIPT -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/chessComputerVsComputer.js"></script>

<jsp:include page="includes/footer.jsp" />

