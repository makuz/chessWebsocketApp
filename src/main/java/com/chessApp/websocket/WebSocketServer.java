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
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.google.gson.Gson;

@Service
@ServerEndpoint("/send-fen/{sender}")
public class WebSocketServer {

	private final static Logger log = Logger.getLogger(WebSocketServer.class);

	private final WebSocketSessionHandler sessionHandler = new WebSocketSessionHandler();

	private final WebsocketUsesrHandler usesrHandler = new WebsocketUsesrHandler();

	private final LiveChessTournamentsHandler chessGamesHandler = new LiveChessTournamentsHandler();

	private GameMessageExchangeProtocol gameMessageProtocol = new GameMessageExchangeProtocol(
			sessionHandler, usesrHandler, chessGamesHandler);

	private Gson gson = new Gson();

	public WebSocketServer() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@OnMessage
	public void onMessage(String msg, Session wsSession,
			@PathParam("sender") String sender) throws IOException {

		log.info("wiadomość odebrana przez server: ");

		WebSocketMessage message = gson.fromJson(msg, WebSocketMessage.class);

		gameMessageProtocol.proccessMessage(message, msg);

	}

	@OnOpen
	public void onOpen(Session wsSession, EndpointConfig config,
			@PathParam("sender") String sender) {
		log.info("connection started, websocket session id: "
				+ wsSession.getId() + " " + sender + " open connection ");

		if (usesrHandler.userListNotContainsUsername(sender)) {

			WebSocketGameUser gameUser = new WebSocketGameUser(sender);
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
			WebSocketGameUser cloesingConnectionUser = usesrHandler
					.getWebsocketUser(sender);

			if (cloesingConnectionUser.getPlayNowWithUser() != null
					&& cloesingConnectionUser.getPlayNowWithUser() != "") {

				WebSocketGameUser cloesingConnectionUserGamePartner = usesrHandler
						.getWebsocketUser(cloesingConnectionUser
								.getPlayNowWithUser());

				cloesingConnectionUserGamePartner.setPlayNowWithUser(null);
				cloesingConnectionUserGamePartner
						.setCommunicationStatus(GameUserCommunicationStatus.WAIT_FOR_NEW_GAME);

				WebSocketMessage disconnectMsg = new WebSocketMessage();
				disconnectMsg.setType(WebSocketMessageType.USER_DISCONNECT);

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
