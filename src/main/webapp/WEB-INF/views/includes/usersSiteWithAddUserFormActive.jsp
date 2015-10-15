<%@ page import="com.chessApp.props.ChessAppProperties"%>
<%
	String contextURL = ChessAppProperties
			.getProperty("app.contextpath");
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="navPilsDivWrapper">
	<ul class="nav nav-pills pull-right responsive">
		<li><a href="/admin/users">users</a></li>
		<li class="active"><a href="#addUserForm" data-toggle="tab">add
				user</a></li>
	</ul>
</div>
<div class="tab-content">
	<div id="usersTable" class="tab-pane fade">
		<h3 class="text-center">Users</h3>
		<jsp:include page="tables/usersTable.jsp"></jsp:include>
	</div>

	<div id="addUserForm" class="tab-pane fade in active user-profile-form">
		<h3 class="text-center">add user</h3>
		<jsp:include page="forms/addUserForm.jsp"></jsp:include>
	</div>
</div>