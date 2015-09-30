package com.chessApp.websocket;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.chessApp.model.ChessGame;
import com.google.gson.Gson;

public class GameMessageExchangeProtocol {

	private final static Logger log = Logger
			.getLogger(GameMessageExchangeProtocol.class);

	private WebSocketSessionHandler sessionHandler;

	private WebsocketUsesrHandler usersHandler;

	private LiveChessTournamentsHandler chessGamesHandler;

	private Gson gson;

	public GameMessageExchangeProtocol(WebSocketSessionHandler sessionHandler,
			WebsocketUsesrHandler usesrHandler,
			LiveChessTournamentsHandler chessGamesHandler) {
		this.sessionHandler = sessionHandler;
		this.usersHandler = usesrHandler;
		this.chessGamesHandler = chessGamesHandler;
		gson = new Gson();
	}

	public synchronized void proccessMessage(WebSocketMessage messageObj,
			String messageJsonString) {

		String messageType = messageObj.getType();

		if (messageType.equals(WebSocketMessageType.GAME_HANDSHAKE_INVITATION)) {

			setUserComStatusIsDuringHandshakeSendMsgAndRefresh(messageObj,
					messageJsonString);

		} else if (messageType
				.equals(WebSocketMessageType.GAME_HANDSHAKE_AGREEMENT)) {

			setUserComStatusIsPlayingAndRefresh(messageObj);

		} else if (messageType
				.equals(WebSocketMessageType.GAME_HANDSHAKE_REFUSE)) {

			sendMessageToOneUser(messageObj, messageJsonString);
			setUserComStatusWaitForNewGameAndRefresh(messageObj);

		} else if (messageType.equals(WebSocketMessageType.CHESS_MOVE)) {

			String fromUsername = messageObj.getSendFrom();
			WebSocketGameUser fromUser = usersHandler
					.getWebsocketUser(fromUsername);

			if (isUserPlayingWithAnyUser(fromUser)) {

				String toUsername = messageObj.getSendTo();
				WebSocketGameUser toUser = usersHandler
						.getWebsocketUser(toUsername);

				if (toUser.getCommunicationStatus().equals(
						GameUserCommunicationStatus.IS_PLAYING)) {

					if (userONEPlayWithUserTWO(fromUser, toUser)) {

						chessGamesHandler.incrementNumberOfMoves(toUser
								.getUniqueActualGameHash());

						sessionHandler.sendToSession(toUsername, fromUsername,
								messageJsonString);
					} else {
						log.debug(messageObj.getSendFrom()
								+ " send message to user which he does not play with , ( to user: "
								+ toUsername + " )");
					}
				}
			} else {
				log.debug(messageObj.getSendFrom()
						+ " send chess-move but he his not playing with anyone");
			}

		} else if (messageType.equals(WebSocketMessageType.GAME_OVER)
				|| messageType.equals(WebSocketMessageType.QUIT_GAME)
				|| messageType.equals(WebSocketMessageType.USER_DISCONNECT)) {

			if (messageType.equals(WebSocketMessageType.QUIT_GAME)
					|| messageType.equals(WebSocketMessageType.GAME_OVER)) {

				WebSocketGameUser gUser = usersHandler
						.getWebsocketUser(messageObj.getSendFrom());
				ChessGame game = chessGamesHandler.getGameByUniqueHashId(gUser
						.getUniqueActualGameHash());
				game.setEndDate(new Date());
				game.setEndingGameFENString(messageObj.getFen());
				LiveChessTournamentsHandler
						.calculateAndSetTimeDurationBeetwenGameBeginAndEnd(game);

				if (messageObj.getCheckMate() != null
						&& messageObj.getCheckMate() == true) {
					game.setCheckMate(true);
				} else {
					game.setCheckMate(false);
				}

				System.out
						.println("------------------------------------------");
				System.out
						.println("------------------------------------------");
				System.out.println("--------CHESS GAME TO SAVE--------");
				System.out
						.println("------------------------------------------");

				System.out.println(game);
				// save game to db

			}

			sendMessageToOneUser(messageObj, messageJsonString);
			setUserComStatusWaitForNewGameAndRefresh(messageObj);

		} else if (messageType.equals(WebSocketMessageType.USER_CONNECT)) {

			log.debug("user " + messageObj.getSendFrom()
					+ " join to participants");

			sessionHandler.sendToAllConnectedSessionsActualParticipantList();
		}

	}

	private synchronized Boolean userONEPlayWithUserTWO(
			WebSocketGameUser fromUser, WebSocketGameUser toUser) {
		log.debug("userONEPlayWithUserTWO()");

		if (fromUser != null && toUser != null
				&& fromUser.getPlayNowWithUser().equals(toUser.getUsername())
				&& toUser.getPlayNowWithUser().equals(fromUser.getUsername())) {
			return true;
		} else {
			return false;
		}

	}

	private synchronized Boolean isUserPlayingWithAnyUser(WebSocketGameUser user) {
		log.debug("isUserPlayingWithAnyUser()");

		if (user != null
				&& user.getCommunicationStatus().equals(
						GameUserCommunicationStatus.IS_PLAYING)
				&& user.getPlayNowWithUser() != null
				&& !user.getPlayNowWithUser().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	private synchronized void setUserComStatusIsDuringHandshakeSendMsgAndRefresh(
			WebSocketMessage messageObj, String messageJsonString) {
		log.debug("setUserComStatusIsDuringHandshakeAndRefresh()");

		WebSocketGameUser invitedUser = usersHandler
				.getWebsocketUser(messageObj.getSendTo());

		if (invitedUser != null
				&& !invitedUser.getCommunicationStatus().equals(
						GameUserCommunicationStatus.IS_DURING_HANDSHAKE)
				&& !invitedUser.getCommunicationStatus().equals(
						GameUserCommunicationStatus.IS_PLAYING)) {

			usersHandler
					.setComStatusIsDuringHandshake(messageObj.getSendFrom());
			usersHandler.setComStatusIsDuringHandshake(messageObj.getSendTo());

			usersHandler.setChessPiecesColorForGamers(messageObj.getSendTo(),
					messageObj.getSendFrom());

			WebSocketGameUser sendToObj = usersHandler
					.getWebsocketUser(messageObj.getSendTo());

			messageObj.setSendToObj(sendToObj);

			WebSocketGameUser sendFromObj = usersHandler
					.getWebsocketUser(messageObj.getSendFrom());

			messageObj.setSendFromObj(sendFromObj);

			sendMessageToOneUser(messageObj, gson.toJson(messageObj));

			sessionHandler.sendToAllConnectedSessionsActualParticipantList();
		} else {
			log.debug("invited user is already playing, is during handshake or is null");

			WebSocketMessage tryLaterMsg = new WebSocketMessage();
			tryLaterMsg.setType(WebSocketMessageType.TRY_LATER);

			sessionHandler.sendToSession(messageObj.getSendFrom(), "server",
					gson.toJson(tryLaterMsg));
		}

	}

	private synchronized void setUserComStatusIsPlayingAndRefresh(
			WebSocketMessage messageObj) {
		log.debug("setUserComStatusIsPlayingAndRefresh()");

		String actualChessGameUUID = UUID.randomUUID().toString();

		usersHandler.setComStatusIsPlaying(messageObj.getSendTo(),
				messageObj.getSendFrom());
		usersHandler.setComStatusIsPlaying(messageObj.getSendFrom(),
				messageObj.getSendTo());

		WebSocketGameUser sendToObj = usersHandler.getWebsocketUser(messageObj
				.getSendTo());

		sendToObj.setUniqueActualGameHash(actualChessGameUUID);
		messageObj.setSendToObj(sendToObj);

		WebSocketGameUser sendFromObj = usersHandler
				.getWebsocketUser(messageObj.getSendFrom());

		sendFromObj.setUniqueActualGameHash(actualChessGameUUID);
		messageObj.setSendFromObj(sendFromObj);
		messageObj.setMoveStatus(ChessMoveStatus.WHITE_TO_MOVE);

		ChessGame chessGame = prepareAndReturnChessGameObjectAtGameStart(
				actualChessGameUUID, sendToObj, sendFromObj, messageObj);

		chessGamesHandler.addNewGame(chessGame);

		sendMessageToOneUser(messageObj, gson.toJson(messageObj));

		sessionHandler.sendToAllConnectedSessionsActualParticipantList();

	}

	private synchronized ChessGame prepareAndReturnChessGameObjectAtGameStart(
			String actualChessGameUUID, WebSocketGameUser sendToObj,
			WebSocketGameUser sendFromObj, WebSocketMessage messageObj) {
		log.debug("prepareAndReturnChessGameObjectAtGameStart()");

		ChessGame chessGame = new ChessGame();
		chessGame.setUniqueGameHash(actualChessGameUUID);
		chessGame.setBeginDate(new Date());
		chessGame.setNumberOfMoves(0);

		if (sendToObj.getChessColor().equals("white")) {
			chessGame.setWhiteColUsername(sendToObj.getUsername());
			chessGame.setBlackColUsername(sendFromObj.getUsername());
		} else {
			chessGame.setWhiteColUsername(sendFromObj.getUsername());
			chessGame.setBlackColUsername(sendToObj.getUsername());
		}

		chessGame.setEndingGameFENString(messageObj.getFen());

		return chessGame;
	}

	private synchronized void setUserComStatusWaitForNewGameAndRefresh(
			WebSocketMessage messageObj) {
		log.debug("setUserComStatusWaitForNewGameAndRefresh()");

		usersHandler.setComStatusWaitForNewGame(messageObj.getSendFrom());
		usersHandler.setComStatusWaitForNewGame(messageObj.getSendTo());
		usersHandler.setChessPiecesColorForGamers(messageObj.getSendTo(),
				messageObj.getSendFrom());

		sessionHandler.sendToAllConnectedSessionsActualParticipantList();
	}

	private synchronized void sendMessageToOneUser(WebSocketMessage message,
			String content) {
		log.debug("sendMessageToOneUser()");
		log.debug("typ wiadomosci : " + message.getType());
		log.debug("od usera " + message.getSendFrom() + " do usera "
				+ message.getSendTo());

		String toUsername = message.getSendTo();
		String fromUsername = message.getSendFrom();
		if (toUsername != null && StringUtils.isNotEmpty(toUsername)) {

			sessionHandler.sendToSession(toUsername, fromUsername, content);
		}
	}

}
