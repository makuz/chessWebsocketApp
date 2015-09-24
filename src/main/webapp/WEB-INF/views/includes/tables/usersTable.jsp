<%@ page import="com.chessApp.props.ChessAppProperties"%>
<%
	String contextURL = ChessAppProperties
			.getProperty("app.contextpath");
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table id="usersTableForDataTableJS"
	class="table table-condensed table-striped table-bordered table-accounts">
	<thead>
		<tr>
			<td>user id</td>
			<td>login</td>
			<td>role</td>
			<td>name</td>
			<td>lastname</td>
			<td>email</td>
			<td>actions</td>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="user" items="${users}">
			<tr>
				<td class="facebookBlue text-center"><c:out
						value="${user.userId}" /></td>
				<td class="text-center"><c:out value="${user.username}" /></td>
				<td><c:choose>
						<c:when test="${user.role == 1}">
							<p class="text-success text-center">admin</p>
						</c:when>
						<c:otherwise>

							<p class="text-danger text-center">user</p>
						</c:otherwise>

					</c:choose></td>
				<td class="text-center"><c:out value="${user.name}" /></td>
				<td class="text-center"><c:out value="${user.lastname}" /></td>
				<td class="text-center"><c:out value="${user.email}" /></td>
				<td class="text-center"><a class="btn btn-success"
					href="<%=contextURL%>/admin/users/editUser?username=<c:out value="${user.username}"/>">edit</a>
					<a data-toggle="modal" data-target="#removeUser"
					class="btn btn-danger" data-id="<c:out value="${user.username}" />">remove</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
