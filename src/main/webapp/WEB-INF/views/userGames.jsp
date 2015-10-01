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

		your games <br /> ${userChessGames}

		<!--<jsp:include page="includes/usersSiteWithUsersTableActive.jsp"></jsp:include> -->
	</div>
</div>
<jsp:include page="includes/modal_boxes/removeUserModal.jsp"></jsp:include>

<jsp:include page="includes/footer.jsp" />