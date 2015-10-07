package com.chessApp.websocket;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.chessApp.gameProtocol.GameMessageProtocol;
import com.chessApp.gameProtocol.GameMessageType;
import com.chessApp.gameProtocol.GameUserCommunicationStatus;
import com.chessApp.model.GameMessage;
import com.chessApp.model.GameUser;
import com.google.gson.Gson;

@Service
@ServerEndpoint("/chessapp-live-game/{sender}")
public class WebSocketServer {

	private final static Logger log = Logger.getLogger(WebSocketServer.class);

	private final WebSocketSessionHandler sessionHandler = new WebSocketSessionHandler();

	private final GameUsersHandler usesrHandler = new GameUsersHandler();

	private final ChessGamesHandler chessGamesHandler = new ChessGamesHandler();

	private GameMessageProtocol gameMessageProtocol = new GameMessageProtocol(
			sessionHandler, usesrHandler, chessGamesHandler);

	private Gson gson = new Gson();

	@OnMessage
	public void onMessage(String msg, Session wsSession,
			@PathParam("sender") String sender) throws IOException {

		log.info("wiadomość odebrana przez server: ");

		GameMessage message = gson.fromJson(msg, GameMessage.class);

		gameMessageProtocol.proccessMessage(message, msg);

	}

	@OnOpen
	public void onOpen(Session wsSession, EndpointConfig config,
			@PathParam("sender") String sender) {
		log.info("connection started, websocket session id: "
				+ wsSession.getId() + " " + sender + " open connection ");

		if (usesrHandler.userListNotContainsUsername(sender)) {

			GameUser gameUser = new GameUser(sender);
			gameUser.setCommunicationStatus(GameUserCommunicationStatus.WAIT_FOR_NEW_GAME);
			synchronized (this) {
				wsSession.getUserProperties().put("sessionOwner",
						gameUser.getUsername());
				sessionHandler.addSession(gameUser.getUsername(), wsSession);
				usesrHandler.addWebsocketUser(gameUser);
			}
			sessionHandler.sendToAllConnectedSessions(gameUser.getUsername());
		}

	}

	@OnClose
	public void onClose(CloseReason closeReason, Session wsSession,
			@PathParam("sender") String sender) {
		log.info("connection closed. Reason: " + closeReason.getReasonPhrase());
		log.info(sender);
		synchronized (this) {
			GameUser cloesingConnectionUser = usesrHandler
					.getWebsocketUser(sender);

			if (cloesingConnectionUser.getPlayNowWithUser() != null
					&& cloesingConnectionUser.getPlayNowWithUser() != "") {

				GameUser cloesingConnectionUserGamePartner = usesrHandler
						.getWebsocketUser(cloesingConnectionUser
								.getPlayNowWithUser());

				cloesingConnectionUserGamePartner.setPlayNowWithUser(null);
				cloesingConnectionUserGamePartner
						.setCommunicationStatus(GameUserCommunicationStatus.WAIT_FOR_NEW_GAME);

				GameMessage disconnectMsg = new GameMessage();
				disconnectMsg.setType(GameMessageType.USER_DISCONNECT);

				sessionHandler.sendToSession(
						cloesingConnectionUserGamePartner.getUsername(),
						sender, gson.toJson(disconnectMsg));
			}

			usesrHandler.removeWebsocketUser(sender);
			sessionHandler.removeSession(sender);
		}
		sessionHandler.sendToAllConnectedSessionsActualParticipantList();

	}

	@OnError
	public void onErrorReceived(Throwable t) {
		log.debug("there was an error with connection");
		log.debug(t);
	}

}
