<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/resources/icons/myicon.png">
<link rel="stylesheet"
	href="<c:url value="${pageContext.request.contextPath}/resources/css/style.css" />" />

<link rel="stylesheet"
	href="<c:url value="${pageContext.request.contextPath}/resources/css/chessboard-0.3.0.css" />" />

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">

<link rel="stylesheet"
	href="https://cdn.datatables.net/1.10.7/css/jquery.dataTables.min.css">

<script src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script
	src="https://cdn.datatables.net/1.10.7/js/jquery.dataTables.min.js"></script>

<script
	src="<c:url value="${pageContext.request.contextPath}/resources/js/chessboard-0.3.0.js" />"></script>

<title>Chess application</title>
</head>

<body>