<%@ page import="com.chessApp.props.ChessAppProperties"%>
<%
	String contextURL = ChessAppProperties
			.getProperty("app.contextpath");
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<form method="POST" action="<%=contextURL%>/admin/users/editUser"
	class="form-horizontal">

	<div class="form-group col-sm-12">
		<c:choose>
			<c:when test="${ msg != null }">
				<div class="alert alert-danger">${msg}</div>
			</c:when>
		</c:choose>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">login</label>
		<div class="col-sm-8">
			<input type="text" class="form-control" name="j_username"
				value="${user.username}" readonly />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">name</label>
		<div class="col-sm-8">
			<input type="text" class="form-control" name="j_name"
				value="${user.name}" />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">lastname</label>
		<div class="col-sm-8">
			<input type="text" class="form-control" name="j_lastname"
				value="${user.lastname}" />
		</div>
	</div>

	<div class="form-group">
		<label class="control-label col-sm-2">email</label>
		<div class="col-sm-8">
			<input type="email" class="form-control" name="j_email"
				value="${user.email}" />
		</div>
	</div>
	<div id="passwordChangeInputs">
		<div class="form-group">
			<label class="col-sm-2 control-label"> userPassword: </label>
			<div class="col-sm-8">
				<input class="form-control" type="password" name="j_password" />
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label">Confirm password: </label>
			<div class="col-sm-8">
				<input class="form-control" type="password"
					name="j_confirm_password" />

			</div>
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-8 col-sm-offset-2"> <input
			id="adminCheckBox2" type="checkbox" name="j_changePasswordFlag">
			change password
		</label>
	</div>
	<!-- admin authorities checkbox -->
	<security:authorize access="hasRole('ROLE_ADMIN')">
		<c:choose>
			<c:when test="${user.role==1}">
				<div class="form-group">
					<label class="control-label col-sm-2">grant admin
						authorities</label>
					<div class="col-sm-8">
						<input type="checkbox" class="form-control" name="j_adminFlag"
							checked />
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="form-group">
					<label class="control-label col-sm-2">grant admin
						authorities</label>
					<div class="col-sm-8">
						<input type="checkbox" class="form-control" name="j_adminFlag" />
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</security:authorize>
	<!--  -->
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-10">
			<input class="btn btn-success" type="submit" value="change data" />
			<security:authorize access="hasRole('ROLE_ADMIN')">
				<a class="btn btn-default" href="/admin/users">back</a>
			</security:authorize>
		</div>
	</div>

</form>
<script>
	$(document).ready(function() {

		$('#passwordChangeInputs').hide();
		$('#adminCheckBox2').attr('checked', false);

	});

	$('#adminCheckBox2').change(function() {
		if (this.checked) {
			$('#passwordChangeInputs').show();
		} else {

			$('#passwordChangeInputs').hide();
		}
	});
</script>