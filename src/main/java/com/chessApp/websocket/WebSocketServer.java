package com.chessApp.websocket;

import java.io.IOException;

//import javax.enterprise.context.ApplicationScoped;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
// @ApplicationScoped
@ServerEndpoint("/send-fen/{sender}")
public class WebSocketServer {

	private final static Logger log = Logger.getLogger(WebSocketServer.class);

	WebSocketSessionHandler webSocketSessionHandler = new WebSocketSessionHandler();

	Gson gson = new Gson();

	@OnMessage
	public void onMessage(String msg, Session wsSession,
			@PathParam("sender") String sender) throws IOException {

		log.info("wiadomość odebrana przez server: ");
		log.info("string msg");
		log.info(msg);

		Message message = gson.fromJson(msg, Message.class);

		log.info("obiekt message");
		log.info(message);

		if (message.getType().equals("chess-move")) {
			log.info("od usera " + message.getSenderName());
			log.info("fen " + message.getFen());

			if (message.getSendTo() != null
					&& StringUtils.isNotEmpty(message.getSendTo())) {

				log.info("do usera " + message.getSendTo());

				webSocketSessionHandler.sendToSession(message.getSendTo(), msg);

			} else {

				log.info("broadcast: wiadomość dla wszystkich ");
				webSocketSessionHandler.sendToAllConnectedSessions(msg);
			}

		} else if (message.getType().equals("welcome-msg")) {
			log.info("welcome message from user " + message.getSenderName());
			webSocketSessionHandler
					.sendToAllConnectedSessionsActualParticipantList();
		}

	}

	@OnOpen
	public void onOpen(Session wsSession, EndpointConfig config,
			@PathParam("sender") String sender) {
		log.info("connection started, websocket session id: "
				+ wsSession.getId() + " " + sender + " open connection ");

		webSocketSessionHandler.sendToAllConnectedSessions(sender);

		WebSocketGameUser gameUser = new WebSocketGameUser(sender);
		webSocketSessionHandler.addUser(gameUser);
		wsSession.getUserProperties().put("sessionOwner",
				gameUser.getUsername());
		webSocketSessionHandler.addSession(wsSession);
		webSocketSessionHandler.printOutAllSessionsOnOpen(wsSession);

	}

	@OnClose
	public void onClose(CloseReason closeReason, Session wsSession,
			@PathParam("sender") String sender) {
		log.info("connection closed. Reason: " + closeReason.getReasonPhrase());

		WebSocketGameUser gameUser = new WebSocketGameUser(sender);
		webSocketSessionHandler.removeUser(gameUser);
		webSocketSessionHandler.sendToAllConnectedSessions(sender
				+ " closed connection");
		webSocketSessionHandler.removeSession(wsSession);
		webSocketSessionHandler.printOutAllSessionsOnClose(wsSession);

	}

	@OnError
	public void onErrorReceived(Throwable t) {
		log.debug("there was an error with connection");
	}

}
