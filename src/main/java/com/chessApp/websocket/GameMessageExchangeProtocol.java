package com.chessApp.websocket;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class GameMessageExchangeProtocol {

	private final static Logger log = Logger
			.getLogger(GameMessageExchangeProtocol.class);

	private WebSocketSessionHandler sessionHandler;

	private WebsocketUsesrHandler usersHandler;

	private Gson gson = new Gson();

	public GameMessageExchangeProtocol(WebSocketSessionHandler sessionHandler,
			WebsocketUsesrHandler usesrHandler) {
		this.sessionHandler = sessionHandler;
		this.usersHandler = usesrHandler;
	}

	public void proccessMessage(WebSocketMessage messageObj,
			String messageJsonString) {

		String messageType = messageObj.getType();

		if (messageType.equals(WebSocketMessageType.GAME_HANDSHAKE_INVITATION)) {

			setUserComStatusIsDuringHandshakeSendMsgAndRefresh(messageObj,
					messageJsonString);

		} else if (messageType
				.equals(WebSocketMessageType.GAME_HANDSHAKE_AGREEMENT)) {

			sendMessageToOneUser(messageObj, messageJsonString);
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

			sendMessageToOneUser(messageObj, messageJsonString);
			setUserComStatusWaitForNewGameAndRefresh(messageObj);

		} else if (messageType.equals(WebSocketMessageType.USER_CONNECT)) {

			log.debug("user " + messageObj.getSendFrom()
					+ " join to participants");

			sessionHandler.sendToAllConnectedSessionsActualParticipantList();
		}

	}

	private Boolean userONEPlayWithUserTWO(WebSocketGameUser fromUser,
			WebSocketGameUser toUser) {
		log.debug("userONEPlayWithUserTWO()");

		if (fromUser != null && toUser != null
				&& fromUser.getPlayNowWithUser().equals(toUser.getUsername())
				&& toUser.getPlayNowWithUser().equals(fromUser.getUsername())) {
			return true;
		} else {
			return false;
		}

	}

	private Boolean isUserPlayingWithAnyUser(WebSocketGameUser user) {
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

	private void printIfNull(Object object) {
		if (object == null) {
			System.out.println("there was null");
		}
	}

	private void setUserComStatusIsDuringHandshakeSendMsgAndRefresh(
			WebSocketMessage messageObj, String messageJsonString) {
		log.debug("setUserComStatusIsDuringHandshakeAndRefresh()");

		WebSocketGameUser invitedUser = usersHandler
				.getWebsocketUser(messageObj.getSendTo());

		if (invitedUser != null
				&& !invitedUser.getCommunicationStatus().equals(
						GameUserCommunicationStatus.IS_DURING_HANDSHAKE)
				&& !invitedUser.getCommunicationStatus().equals(
						GameUserCommunicationStatus.IS_PLAYING)) {

			sendMessageToOneUser(messageObj, messageJsonString);

			usersHandler
					.setComStatusIsDuringHandshake(messageObj.getSendFrom());
			usersHandler.setComStatusIsDuringHandshake(messageObj.getSendTo());
			sessionHandler.sendToAllConnectedSessionsActualParticipantList();
		} else {
			log.debug("invited user is already playing, is during handshake or is null");

			WebSocketMessage tryLaterMsg = new WebSocketMessage();
			tryLaterMsg.setType(WebSocketMessageType.TRY_LATER);

			sessionHandler.sendToSession(messageObj.getSendFrom(), "server",
					gson.toJson(tryLaterMsg));
		}

	}

	private void setUserComStatusIsPlayingAndRefresh(WebSocketMessage message) {
		log.debug("setUserComStatusIsPlayingAndRefresh()");
		printIfNull(message);

		usersHandler.setComStatusIsPlaying(message.getSendTo(),
				message.getSendFrom());
		usersHandler.setComStatusIsPlaying(message.getSendFrom(),
				message.getSendTo());

		usersHandler.setChessPiecesColorForGamers(message.getSendTo(),
				message.getSendFrom());

		sessionHandler.sendToAllConnectedSessionsActualParticipantList();

	}

	private void setUserComStatusWaitForNewGameAndRefresh(
			WebSocketMessage message) {
		log.debug("setUserComStatusWaitForNewGameAndRefresh()");
		printIfNull(message);

		usersHandler.setComStatusWaitForNewGame(message.getSendFrom());
		usersHandler.setComStatusWaitForNewGame(message.getSendTo());
		sessionHandler.sendToAllConnectedSessionsActualParticipantList();
	}

	private void sendMessageToOneUser(WebSocketMessage message, String content) {
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
