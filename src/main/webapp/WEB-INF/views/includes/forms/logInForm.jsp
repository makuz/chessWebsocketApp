<%@ page import="com.chessApp.props.ChessAppProperties"%>
<%
	String contextURL = ChessAppProperties
			.getProperty("app.contextpath");
%>
<form method="POST" action="<%=contextURL%>/login"
	class="form-horizontal" autocomplete="on">
	<div class="form-group">
		<label class="control-label col-sm-3">login</label>
		<div class="col-sm-6">
			<input type="text" class="form-control" name="username"
				pattern=".{5,10}" required title="5 to 10 characters minimum" />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-3">password</label>
		<div class="col-sm-6">
			<input type="password" class="form-control" name="password"
				pattern=".{5,30}" required title="5 to 30 characters minimum" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-offset-3 col-sm-6">
			<input class="btn btn-info btn-block" type="submit" value="log in" />
			<a class="btn btn-success btn-block" href="<%=contextURL%>/signin">sign
				up</a>
		</div>
	</div>


</form>