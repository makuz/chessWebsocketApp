<%@ page import="com.chessApp.props.ChessAppProperties"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
	String contextURL = ChessAppProperties
			.getProperty("app.contextpath");
%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form:form method="POST" commandName="signUpForm"
	class="form-horizontal" name="addUserForm">

	<div class="form-group">
		<label class="control-label col-sm-2">login</label>
		<div class="col-sm-8">
			<form:input path="username" type="text" class="form-control" />
			<form:errors path="username" cssClass="alert-danger danger" />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">email</label>
		<div class="col-sm-8">
			<form:input path="email" type="email" class="form-control" />
			<form:errors path="email" cssClass="alert-danger danger" />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">password</label>
		<div class="col-sm-8">
			<form:input path="password" type="password" class="form-control" />
			<form:errors path="password" cssClass="alert-danger danger" />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">confirm password</label>
		<div class="col-sm-8">
			<form:input path="confirmPassword" type="password"
				class="form-control" />
			<form:errors path="confirmPassword" cssClass="alert-danger danger" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-8 col-sm-offset-2"> <form:checkbox
				path="grantAdminAuthorities" /> grant admin authorities
		</label>
	</div>

	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-8">
			<input class="btn btn-success btn-block" type="submit"
				value="add account" />
		</div>
	</div>

	<c:if test="${errorMessage ne null}">
		<h2 class="alert alert-danger text-center">${errorMessage}</h2>
	</c:if>
	<c:if test="${successMsg ne null}">
		<h2 class="alert alert-success text-center">${successMsg}</h2>
	</c:if>
</form:form>