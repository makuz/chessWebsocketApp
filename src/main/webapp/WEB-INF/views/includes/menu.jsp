<%@ page import="com.chessApp.props.ChessAppProperties"%>
<%
	String contextURL = ChessAppProperties
			.getProperty("app.contextpath");
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<ul id="navButtons" class="nav nav-tabs navView">
	<li><a href="<%=contextURL%>/">home</a></li>
	<li><a href="<%=contextURL%>/play-chess-with-computer">play
			with computer</a></li>
	<li><a href="<%=contextURL%>/play-chess-with-user">play with
			users</a></li>

	<security:authorize access="hasRole('ROLE_ADMIN')">
		<li><a href="<%=contextURL%>/admin/users">Accounts</a></li>
	</security:authorize>

	<security:authorize access="hasRole('ROLE_ADMIN')" var="isAdmin" />
	<c:choose>
		<c:when test="${isAdmin}">
			<li><a href="<%=contextURL%>/admin/your-account">Your
					Account</a></li>
		</c:when>
		<c:otherwise>
			<security:authorize access="hasRole('ROLE_USER')">
				<li><a href="<%=contextURL%>/user/your-account">Your
						Account</a></li>
			</security:authorize>
		</c:otherwise>
	</c:choose>

	<li class="pull-right text-uppercase"><a
		href="<%=contextURL%>/logout">log out</a></li>
	<li class="pull-right text-uppercase"><a
		href="<%=contextURL%>/login">log in</a></li>
</ul>

<security:authorize access="hasAnyRole('ROLE_ADMIN, ROLE_USER')">
	<h4 class="text-right logged-user-name">
		Welcome: <span class="text-primary"><b>${currentUserName}</b></span>
	</h4>
</security:authorize>