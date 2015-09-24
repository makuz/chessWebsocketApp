<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chessApp.configs.Config"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

				<h2 class="alert alert-success text-center user-create-msg">
					Twoje Konto <b>${userEmail}</b> zostało potwierdzone
				</h2>

				<p class="text-center">Teraz wystarczy się zalogować</p>
				<p class="text-center">i zacząć grać</p>
				<p class="text-center">powodzenia!</p>

			</div>
		</div>

	</div>
</div>
<jsp:include page="includes/footer.jsp" />