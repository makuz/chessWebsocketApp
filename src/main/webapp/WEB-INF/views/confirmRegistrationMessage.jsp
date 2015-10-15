<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.chessApp.props.ChessAppProperties"%>
<%
	String contextURL = ChessAppProperties
			.getProperty("app.contextpath");
%>

<jsp:include page="includes/header.jsp" />
<div class="container-fluid">
	<jsp:include page="includes/menu.jsp"></jsp:include>
	<div class="main-wrapper">

		<br />
		<div class="message-box">

			<h2 class="alert alert-success text-center user-create-msg">
				<b>${username}</b> Twoje Konto zostało potwierdzone
			</h2>

			<p class="text-center">Teraz wystarczy się zalogować</p>
			<p class="text-center">i zacząć grać</p>
			<p class="text-center">powodzenia!</p>

		</div>
	</div>

</div>
<jsp:include page="includes/footer.jsp" />