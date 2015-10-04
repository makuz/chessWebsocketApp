<jsp:include page="includes/header.jsp" />
<div class="container-fluid">
	<jsp:include page="includes/menu.jsp" />
	<div class="main-wrapper">
		<%@ page import="java.util.List"%>
		<%@ page import="com.chessApp.model.UserAccount"%>
		<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<%@ page import="com.chessApp.props.ChessAppProperties"%>
		<%
			String contextURL = ChessAppProperties
					.getProperty("app.contextpath");
		%>
		<script
			src="<c:url value="${pageContext.request.contextPath}/resources/js/main.js" />"></script>
		<script
			src="<c:url value="${pageContext.request.contextPath}/resources/js/lib/jquery.canvasjs.min.js" />"></script>

		<div class="site-title">
			<h1 class="text-center">Best 10 players</h1>
		</div>
		<div id="bestChessGamersChart" style="height: 400px; width: 100%;"></div>

		<c:set var="usersJson" value="${bestPlayersJson}"></c:set>

		<script type="text/javascript">
			var USERS_JSON_ARR = JSON.parse('${usersJson}');

			var TITLE = "number of won chess games";

			var CHART_TYPE = "column";

			var DATA_INPUT = {
				title : {
					text : TITLE
				},
				data : [ {
					type : CHART_TYPE,
					dataPoints : []
				} ]
			}

			populateChartWithUsersData();

			function populateChartWithUsersData() {
				for (var i = 0; i < USERS_JSON_ARR.length; i++) {
					var userData = {
						label : USERS_JSON_ARR[i].username,
						y : USERS_JSON_ARR[i].numberOfWonChessGames
					}
					DATA_INPUT.data[0].dataPoints.push(userData);
				}
			}

			$(document).ready(
					function() {
						var chart = new CanvasJS.Chart("bestChessGamersChart",
								DATA_INPUT);

						chart.render();
					});
		</script>

	</div>
</div>
<jsp:include page="includes/footer.jsp" />