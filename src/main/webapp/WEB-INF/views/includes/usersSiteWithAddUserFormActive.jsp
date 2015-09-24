<%@ page import="com.chessApp.props.ChessAppProperties"%>
<%
	String contextURL = ChessAppProperties
			.getProperty("app.contextpath");
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<ul class="nav nav-pills pull-right">
	<li><a href="/admin/users">users</a></li>
	<li class="active"><a href="#addUserForm" data-toggle="tab">add
			user</a></li>
</ul>
<div class="col-sm-11">
	<div class="tab-content">
		<div id="usersTable" class="tab-pane fade">
			<h1 class="text-center">Users</h1>
			<jsp:include page="tables/usersTable.jsp"></jsp:include>
		</div>

		<div id="addUserForm" class="tab-pane fade in active">
			<jsp:include page="forms/addUserForm.jsp"></jsp:include>
		</div>
	</div>
</div>