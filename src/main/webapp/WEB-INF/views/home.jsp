<jsp:include page="includes/header.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%@ page import="com.chessApp.model.UserAccount"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container-fluid">
	<jsp:include page="includes/menu.jsp" />
	<div class="col-md-10 col-md-offset-1 ">
		<br />
		<div class="row contentWrapper">
			<h3 class="text-left">Play Chess with us</h3>
			<br />
			<script
				src="<c:url value="${pageContext.request.contextPath}/resources/js/chess.js" />"></script>

			<img id="home-img" class="img-responsive"
				src="<c:url value="${pageContext.request.contextPath}/resources/images/chess2.jpg" />" />

			<div id="chess-board-home">
				<div class="game-stats">
					<p class="text-danger">
						Status: <span id="status"></span>
					</p>
					<small class="text-info"> FEN: <br /> <span id="fen"></span>
					</small> 
				</div>
				<br />
				<div id="board"></div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	var board, game = new Chess();
	statusEl = $('#status'), fenEl = $('#fen'), pgnEl = $('#pgn');

	var makeRandomMove = function() {
		var possibleMoves = game.moves();

		// exit if the game is over
		if (game.game_over() === true || game.in_draw() === true
				|| possibleMoves.length === 0)
			return;

		var randomIndex = Math.floor(Math.random() * possibleMoves.length);
		game.move(possibleMoves[randomIndex]);
		board.position(game.fen());

		window.setTimeout(makeRandomMove, 2000);
		updateStatus();
	};

	board = ChessBoard('board', 'start');

	window.setTimeout(makeRandomMove, 2000);

	var updateStatus = function() {
		var status = '';

		var moveColor = 'White';
		if (game.turn() === 'b') {
			moveColor = 'Black';
		}

		// checkmate?
		if (game.in_checkmate() === true) {
			status = 'Game over, ' + moveColor + ' is in checkmate.';
		}

		// draw?
		else if (game.in_draw() === true) {
			status = 'Game over, drawn position';
		}

		// game still on
		else {
			status = moveColor + ' to move';

			// check?
			if (game.in_check() === true) {
				status += ', ' + moveColor + ' is in check';
			}
		}

		statusEl.html(status);
		fenEl.html(game.fen());
		pgnEl.html(game.pgn());
	};

	updateStatus();
</script>

<jsp:include page="includes/footer.jsp" />

