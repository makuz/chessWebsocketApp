<%@ page import="com.chessApp.props.ChessAppProperties"%>
<%
	String contextURL = ChessAppProperties
			.getProperty("app.contextpath");
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="navPilsDivWrapper">
	<ul class="nav nav-pills pull-right responsive">
		<li class="active"><a href="#usersTable" data-toggle="tab">users</a></li>
		<li><a href="#addUserForm" data-toggle="tab">add user</a></li>
	</ul>
</div>
<div class="tab-content">
	<div id="usersTable" class="tab-pane fade in active">

		<h3 class="text-center">
			<span class="glyphicon glyphicon-user"></span> USERS <span
				class="glyphicon glyphicon-user"></span>
		</h3>
		<jsp:include page="tables/usersTable.jsp"></jsp:include>
	</div>

	<div id="addUserForm" class="tab-pane fade user-profile-form">
		<h3 class="text-center">add user</h3>
		<jsp:include page="forms/addUserForm.jsp"></jsp:include>
	</div>
</div>
