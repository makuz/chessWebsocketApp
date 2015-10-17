<%@ page import="com.chessApp.props.ChessAppProperties"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
	String contextURL = ChessAppProperties
			.getProperty("app.contextpath");
%>
<form:form method="POST" commandName="loginForm"
	cssClass="form-horizontal">
	<div class="form-group">
		<label class="control-label col-sm-3">login</label>
		<div class="col-sm-6">
			<form:input path="username" type="text" cssClass="form-control"
				pattern=".{5,10}" required="true" title="5 to 10 characters minimum" />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-3">password</label>
		<div class="col-sm-6">
			<form:input path="password" type="password" cssClass="form-control"
				pattern=".{6,30}" required="true" title="6 to 30 characters minimum" />
		</div>
	</div>

	<form:errors path="*" cssStyle="color: #ff0000;" />

	<div class="form-group">
		<div class="col-sm-offset-3 col-sm-6">
			<input class="btn btn-info btn-block" type="submit" value="log in" />
			<a class="btn btn-success btn-block" href="<%=contextURL%>/signup">sign
				up</a>
		</div>
	</div>


</form:form>