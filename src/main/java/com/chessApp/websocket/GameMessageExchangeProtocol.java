package com.chessApp.websocket;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class GameMessageExchangeProtocol {

	private final static Logger log = Logger
			.getLogger(GameMessageExchangeProtocol.class);

	private WebSocketSessionHandler sessionHandler;

	private WebsocketUsesrHandler usesrHandler;

	public GameMessageExchangeProtocol(WebSocketSessionHandler sessionHandler,
			WebsocketUsesrHandler usesrHandler) {
		this.sessionHandler = sessionHandler;
		this.usesrHandler = usesrHandler;
	}

	public void proccessMessage(WebSocketMessage message,
			String messageJsonString) {

		String messageType = message.getType();

		if (messageType.equals(WebSocketMessageType.GAME_HANDSHAKE_INVITATION)) {
			log.debug(WebSocketMessageType.GAME_HANDSHAKE_INVITATION);

			sendMessageToOneUser(message, messageJsonString);
			setUserComStatusIsDuringHandshakeAndRefresh(message);

		} else if (messageType
				.equals(WebSocketMessageType.GAME_HANDSHAKE_AGREEMENT)) {
			log.debug(WebSocketMessageType.GAME_HANDSHAKE_AGREEMENT);

			sendMessageToOneUser(message, messageJsonString);
			setUserComStatusIsPlayingAndRefresh(message);

		} else if (messageType
				.equals(WebSocketMessageType.GAME_HANDSHAKE_REFUSE)) {
			log.debug(WebSocketMessageType.GAME_HANDSHAKE_REFUSE);

			sendMessageToOneUser(message, messageJsonString);
			setUserComStatusWaitForNewGameAndRefresh(message);

		} else if (messageType.equals(WebSocketMessageType.CHESS_MOVE)) {
			log.debug(WebSocketMessageType.CHESS_MOVE);

			sendMessageToOneUser(message, messageJsonString);

		} else if (messageType.equals(WebSocketMessageType.GAME_OVER)) {
			log.debug(WebSocketMessageType.GAME_OVER);

			setUserComStatusWaitForNewGameAndRefresh(message);

		} else if (messageType.equals(WebSocketMessageType.USER_CONNECT)) {
			log.debug(WebSocketMessageType.USER_CONNECT);

			log.info("user " + message.getSendFrom() + " join ");

			sessionHandler.sendToAllConnectedSessionsActualParticipantList();
		}

	}

	private void printIfNull(Object object) {
		if (object == null) {
			System.out.println("there was null");
		}
	}

	private void setUserComStatusIsDuringHandshakeAndRefresh(
			WebSocketMessage message) {
		log.debug("setUserComStatusIsDuringHandshakeAndRefresh()");

		printIfNull(message);

		usesrHandler.setComStatusIsDuringHandshake(message.getSendFrom());
		usesrHandler.setComStatusIsDuringHandshake(message.getSendTo());
		sessionHandler.sendToAllConnectedSessionsActualParticipantList();
	}

	private void setUserComStatusIsPlayingAndRefresh(WebSocketMessage message) {
		log.debug("setUserComStatusIsPlayingAndRefresh()");
		printIfNull(message);

		usesrHandler.setComStatusIsPlaying(message.getSendTo(),
				message.getSendFrom());
		usesrHandler.setComStatusIsPlaying(message.getSendFrom(),
				message.getSendTo());
		sessionHandler.sendToAllConnectedSessionsActualParticipantList();

	}

	private void setUserComStatusWaitForNewGameAndRefresh(
			WebSocketMessage message) {
		log.debug("setUserComStatusWaitForNewGameAndRefresh()");
		printIfNull(message);

		usesrHandler.setComStatusWaitForNewGame(message.getSendFrom());
		usesrHandler.setComStatusWaitForNewGame(message.getSendTo());
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
