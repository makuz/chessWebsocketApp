<jsp:include page="includes/header.jsp" />
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%@ page import="com.chessApp.model.UserAccount"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript"
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
				<div id="home-img-div">
					<img id="home-img" class="img-responsive" alt="iboard home image"
						src="<c:url value="${pageContext.request.contextPath}/resources/images/chess.jpg" />" />
				</div>

				<div id="chess-board-home">
					<div id="board"></div>
				</div>

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

