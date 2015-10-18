<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.chessApp.props.ChessAppProperties"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String version = ChessAppProperties.getProperty("chessApp.version");
%>
<br />
<footer class="footer">
	<span class="author">author: Marcin Kużdowicz</span> <span
		class="appTitle">chess application</span> <span class="pull-right">version:
		<%=version%></span>
</footer>
<script type="text/javascript"
	src="<c:url value="${pageContext.request.contextPath}/resources/js/main.js" />"></script>
</body>
</html>