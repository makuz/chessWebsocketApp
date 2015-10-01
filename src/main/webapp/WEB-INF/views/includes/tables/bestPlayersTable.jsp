<%@ page import="com.chessApp.props.ChessAppProperties"%>
<%
	String contextURL = ChessAppProperties
			.getProperty("app.contextpath");
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="table-responsive">
	<table
		class="table table-condensed table-striped table-bordered table-accounts">
		<thead>
			<tr>
				<td class="text-center">user since</td>
				<td class="text-center">user name</td>
				<td class="text-center">number of chess games played</td>
				<td class="text-center">number of won chess games</td>
				<td class="text-center">last played game</td>
			</tr>
		</thead>
		<tbody>
			
		</tbody>
	</table>
</div>