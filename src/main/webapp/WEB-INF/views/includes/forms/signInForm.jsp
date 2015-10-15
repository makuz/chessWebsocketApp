<%@ page import="com.chessApp.props.ChessAppProperties"%>
<%
	String contextURL = ChessAppProperties
			.getProperty("app.contextpath");
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
		<label class="control-label col-sm-2">login</label>
		<div class="col-sm-8">
			<input type="text" class="form-control" name="j_username"
				pattern=".{5,10}" required title="5 to 10 characters minimum" />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">email</label>
		<div class="col-sm-8">
			<input type="email" class="form-control" name="j_email" required />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">password</label>
		<div class="col-sm-8">
			<input type="password" class="form-control" name="j_password"
				pattern=".{5,30}" required title="5 to 30 characters minimum" />
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
		<div class="col-sm-offset-2 col-sm-8">
			<input class="btn btn-success btn-block" type="submit" value="create account" />
		</div>
	</div>

	<c:if test="${errorMessage ne null}">
		<h2 class="alert alert-danger text-center">${errorMessage}</h2>
	</c:if>
</form>