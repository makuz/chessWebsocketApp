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
		<div id="bestPlayersTable">
			<h1 class="text-center">Best 20 players</h1>
			<jsp:include page="includes/tables/bestPlayersTable.jsp"></jsp:include>
		</div>
	</div>
</div>
<jsp:include page="includes/footer.jsp" />