<%@ page import="com.chessApp.props.ChessAppProperties"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="table-responsive">
	<table
		class="table table-condensed table-striped table-bordered table-accounts">
		<thead>
			<tr>
				<td class="text-center">place</td>
				<td class="text-center">user since</td>
				<td class="text-center">user name</td>
				<td class="text-center">number of chess games played</td>
				<td class="text-center">number of won chess games</td>
				<td class="text-center">number of lost chess games</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="user" items="${bestPlayingUsers}">
				<tr>
					<td><c:out value="${counter}" /></td>
					<td class="text-center"><fmt:formatDate pattern="dd-MM-yyyy"
							value="${user.registrationDate}" /></td>
					<td class="text-center">${user.username}</td>
					<td class="text-center">${user.numberOfGamesPlayed}</td>
					<td class="text-center">${user.numberOfWonChessGames}</td>
					<td class="text-center">${user.numberOfLostChessGames}</td>
				</tr>
			</c:forEach>

		</tbody>
	</table>
</div>