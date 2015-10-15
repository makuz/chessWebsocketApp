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
	<br />
		<div class="outerDiv">
			<div class="wrapperForKeepCenterPosition">
				<div class="site-title">
					<h4>Play board games online</h4>
				</div>
				<article id="home-img-article">
					<img id="home-img" class="img-responsive"
						src="<c:url value="${pageContext.request.contextPath}/resources/images/chess.jpg" />" />
				</article>

				<article id="chess-board-home">
					<div id="board"></div>
				</article>

				<div class="game-stats">
					<p class="nice-blue-font-color">
						Status: <span id="status"></span>
					</p>
					<small class="white"> FEN: <br /> <span id="fen"></span>
					</small>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- IMPORT CHESS SCRIPT -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/chessComputerVsComputer.js"></script>

<jsp:include page="includes/footer.jsp" />

