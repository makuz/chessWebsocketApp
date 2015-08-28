package com.chessApp.websocket;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

@ApplicationScoped
@ServerEndpoint("/send-fen/{sender}/{reciever}")
public class WebSocketServer {

	private final static Logger log = Logger.getLogger(WebSocketServer.class);

	WebSocketSessionHandler webSocketSessionHandler = new WebSocketSessionHandler();

	@OnMessage
	public void onMessage(String msg, Session wsSession,
			@PathParam("sender") String sender,
			@PathParam("reciever") String reciever) throws IOException {

		log.info("wiadomość odebrana przez server: " + msg + " from " + sender
				+ " to " + reciever);

		webSocketSessionHandler.sendToAllConnectedSessions(msg);

	}

	@OnOpen
	public void onOpen(Session wsSession, @PathParam("sender") String sender,
			@PathParam("reciever") String reciever) {
		log.info("connection started, websocket session id: "
				+ wsSession.getId() + " from " + sender + " to " + reciever);

		webSocketSessionHandler.sendToAllConnectedSessions(sender
				+ " has connected");

		wsSession.getUserProperties().put("sender", sender);
		wsSession.getUserProperties().put("reciever", reciever);
		log.info("session path parameters");
		log.info(wsSession.getPathParameters());

		webSocketSessionHandler.addSession(wsSession);
		webSocketSessionHandler.printOutAllSessionsOnOpen(wsSession);

	}

	@OnClose
	public void onClose(CloseReason closeReason, Session wsSession,
			@PathParam("sender") String sender,
			@PathParam("reciever") String reciever) {
		log.info("connection closed. Reason: " + closeReason.getReasonPhrase());

		webSocketSessionHandler.sendToAllConnectedSessions(sender
				+ " has connected");
		webSocketSessionHandler.removeSession(wsSession);
		webSocketSessionHandler.printOutAllSessionsOnClose(wsSession);

	}

	@OnError
	public void onErrorReceived(Throwable t) {
		log.debug("there was an error with connection");
	}

}
