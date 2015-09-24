<%@ page import="com.chessApp.configs.Config"%>
<%
	String contextURL = new Config().getContextUrl();
%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:choose>
	<c:when test="${ msg != null }">
		<div class="alert alert-danger">${msg}</div>
	</c:when>
</c:choose>
<form method="POST" action="<%=contextURL%>/signin/create/account"
	class="form-horizontal" autocomplete="on">

	<div class="form-group">
		<label class="control-label col-sm-2">email</label>
		<div class="col-sm-8">
			<input type="email" class="form-control" name="j_username" required />
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
		<div class="col-sm-offset-2 col-sm-10">
			<input class="btn btn-success" type="submit" value="create" />
		</div>
	</div>

	<c:if test="${errorMessage ne null}">
		<h2 class="alert alert-danger text-center">${errorMessage}</h2>
	</c:if>
</form>