package com.chessApp.websocket;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class GameMessageExchangeProtocol {

	private final static Logger log = Logger
			.getLogger(GameMessageExchangeProtocol.class);

	private WebSocketSessionHandler sessionHandler;

	public GameMessageExchangeProtocol(WebSocketSessionHandler sessionHandler) {
		this.sessionHandler = sessionHandler;
	}

	public void proccessMessage(WebSocketMessage message,
			String messageJsonString) {

		String messageType = message.getType();

		if (messageType.equals(WebSocketMessageType.GAME_HANDSHAKE_INVITATION)) {

			sendMessageToOneUser(message, messageJsonString);

		} else if (messageType
				.equals(WebSocketMessageType.GAME_HANDSHAKE_AGREEMENT)) {

			sendMessageToOneUser(message, messageJsonString);

		} else if (messageType
				.equals(WebSocketMessageType.GAME_HANDSHAKE_REFUSE)) {

			sendMessageToOneUser(message, messageJsonString);

		} else if (messageType.equals(WebSocketMessageType.CHESS_MOVE)) {

			sendMessageToOneUser(message, messageJsonString);

		} else if (messageType.equals(WebSocketMessageType.USER_CONNECT)) {

			log.info("user " + message.getSenderName() + " join ");

			sessionHandler.sendToAllConnectedSessionsActualParticipantList();
		}

	}

	private void sendMessageToOneUser(WebSocketMessage message, String content) {
		log.debug("sendMessageToOneUser()");
		log.debug("typ wiadomosci : " + message.getType());
		log.debug("od usera " + message.getSenderName() + " do usera "
				+ message.getSendTo());

		String toUsername = message.getSendTo();
		if (toUsername != null && StringUtils.isNotEmpty(toUsername)) {

			sessionHandler.sendToSession(toUsername, content);
		}
	}

}
