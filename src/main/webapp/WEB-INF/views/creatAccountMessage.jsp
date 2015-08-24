<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chessApp.configs.Config"%>
<%
	String contextURL = new Config().getContextUrl();
%>

<jsp:include page="includes/header.jsp" />
<div class="container-fluid">
	<jsp:include page="includes/menu.jsp"></jsp:include>
	<div class="col-md-10 col-md-offset-1 ">

		<br />
		<div class="row contentWrapper">
			<div class="row">

				<c:choose>
					<c:when test="${created}">

						<h2 class="alert alert-success text-center user-create-msg">${msg}</h2>
						<jsp:include
							page="includes/forms/logInFormAfterNewUserCreated.jsp"></jsp:include>
					</c:when>
					<c:otherwise>
						<h2 class="alert alert-danger text-center user-create-msg">${msg}</h2>
						<a class="btn btn-success" href="<%=contextURL%>/signin">sign
							in</a>
					</c:otherwise>

				</c:choose>

			</div>
		</div>

	</div>
</div>
<jsp:include page="includes/footer.jsp" />