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
	<li><a href="<%=contextURL%>/play-chess-with-computer">play-with-computer</a></li>
	<li><a href="<%=contextURL%>/play-chess-with-user">play-with-others</a></li>

	<security:authorize access="hasRole('ROLE_ADMIN')">
		<li><a href="<%=contextURL%>/admin/users">all-users</a></li>
	</security:authorize>

	<security:authorize access="hasRole('ROLE_ADMIN')" var="isAdmin" />

	<security:authorize access="hasRole('ROLE_USER')">
		<li><a href="<%=contextURL%>/user/your-account">your-profile</a></li>
	</security:authorize>

	<security:authorize access="hasRole('ROLE_USER')">
		<li><a href="<%=contextURL%>/user/your-chessgames">your-games-history</a></li>
	</security:authorize>
	<li><a href="<%=contextURL%>/home/best-players">best-players</a></li>

	<li class="pull-right logOutBtn "><a href="<%=contextURL%>/logout">log
			out</a></li>
	<li class="pull-right logInBtn "><a href="<%=contextURL%>/login">log
			in</a></li>
</ul>
<div class="welcome-user-line">
	<div class="col-md-8">
		<h3 class="text-left bright-font-color header-title">
			<span class="glyphicon glyphicon glyphicon-king"></span> Iboard games
		</h3>
	</div>
	<security:authorize access="hasAnyRole('ROLE_ADMIN, ROLE_USER')">
		<div class="col-md-4">
			<h4 class="text-right logged-user-name">
				Welcome: <span class="main-active-font-color"><b>${currentUserName}</b></span>
			</h4>
		</div>

	</security:authorize>
</div>