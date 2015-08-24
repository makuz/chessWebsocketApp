<%@ page import="com.chessApp.configs.Config"%>
<%
	String contextURL = new Config().getContextUrl();
%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<h1 class="text-center">add user</h1>


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
			<input type="text" class="form-control" name="j_username" required />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">password</label>
		<div class="col-sm-8">
			<input type="password" class="form-control" name="j_password"
				required />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">confirm password</label>
		<div class="col-sm-8">
			<input type="password" class="form-control" name="j_confirm_password"
				required />
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
		<label class="control-label col-sm-2">grant admin authorities</label>
		<div class="col-sm-8">
			<input type="checkbox" class="form-control" name="j_adminFlag" />
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-10">
			<input class="btn btn-success" type="submit" value="add" />
		</div>
	</div>

</form>