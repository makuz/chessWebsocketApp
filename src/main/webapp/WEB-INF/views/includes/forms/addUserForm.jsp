<%@ page import="com.chessApp.props.ChessAppProperties"%>
<%
	String contextURL = ChessAppProperties
			.getProperty("app.contextpath");
%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form method="POST"
	action="<%=contextURL%>/admin/users/addUser#addUserForm"
	class="form-horizontal">

	<div class="form-group col-sm-12">
		<c:choose>
			<c:when test="${ msg != null }">
				<div class="alert alert-danger">${msg}</div>
			</c:when>
		</c:choose>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">username</label>
		<div class="col-sm-8">
			<input type="text" class="form-control" name="j_username"
				pattern=".{5,10}" required title="5 to 10 characters minimum" />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">password</label>
		<div class="col-sm-8">
			<input type="password" class="form-control" name="j_password"
				pattern=".{6,30}" required title="6 to 30 characters minimum" />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">confirm password</label>
		<div class="col-sm-8">
			<input type="password" class="form-control" name="j_confirm_password"
				pattern=".{6,30}" required title="6 to 30 characters minimum" />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">name</label>
		<div class="col-sm-8">
			<input type="text" class="form-control" name="j_name" />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">lastname</label>
		<div class="col-sm-8">
			<input type="text" class="form-control" name="j_lastname" />
		</div>
	</div>
	<div class="form-group">
		<label class="control-label col-sm-2">email</label>
		<div class="col-sm-8">
			<input type="email" class="form-control" name="j_email" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-8 col-sm-offset-2"> <input
			type="checkbox" name="j_adminFlag" /> grant admin authorities
		</label>
	</div>
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-8">
			<input class="btn btn-success btn-block" type="submit"
				value="add account" />
		</div>
	</div>

</form>