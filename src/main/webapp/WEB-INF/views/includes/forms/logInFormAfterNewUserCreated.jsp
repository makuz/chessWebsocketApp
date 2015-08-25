<%@ page import="com.chessApp.configs.Config"%>
<%
	String contextURL = new Config().getContextUrl();
%>
<form method="POST" action="<%=contextURL%>/login"
	class="form-horizontal">

	<div class="form-group">
		<label class="control-label col-sm-2">Name</label>
		<div class="col-sm-8">
			<input type="text" class="form-control" value="${userLogin}"
				name="j_username" required />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">password</label>
		<div class="col-sm-8">
			<input type="password" class="form-control" value="${userPassword}"
				name="j_password" required />
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-10">
			<input class="btn btn-success" type="submit" value="log in" /> <a
				class="btn btn-success" href="<%=contextURL%>/signin">sign in</a>
		</div>
	</div>


</form>