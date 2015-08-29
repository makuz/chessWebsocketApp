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

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
// @ApplicationScoped
@ServerEndpoint("/send-fen/{sender}/{reciever}")
public class WebSocketServer {

	private final static Logger log = Logger.getLogger(WebSocketServer.class);

	WebSocketSessionHandler webSocketSessionHandler = new WebSocketSessionHandler();

	Gson gson = new Gson();

	@OnMessage
	public void onMessage(String msg, Session wsSession,
			@PathParam("sender") String sender,
			@PathParam("reciever") String reciever) throws IOException {

		log.info("wiadomość odebrana przez server: ");

		Message message = gson.fromJson(msg, Message.class);

		log.info("od usera " + message.getSenderName());
		log.info("fen " + message.getFen());

		webSocketSessionHandler.sendToAllConnectedSessions(msg);

	}

	@OnOpen
	public void onOpen(Session wsSession, EndpointConfig config,
			@PathParam("sender") String sender,
			@PathParam("reciever") String reciever) {
		log.info("connection started, websocket session id: "
				+ wsSession.getId() + " from " + sender + " to " + reciever);

		webSocketSessionHandler.sendToAllConnectedSessions(sender
				+ " has connected");

		WebSocketGameUser gameUser = new WebSocketGameUser(sender);
		webSocketSessionHandler.addUser(gameUser);
		wsSession.getUserProperties().put("sessionOwner",
				gameUser.getUsername());
		webSocketSessionHandler.addSession(wsSession);
		webSocketSessionHandler.printOutAllSessionsOnOpen(wsSession);

	}

	@OnClose
	public void onClose(CloseReason closeReason, Session wsSession,
			@PathParam("sender") String sender,
			@PathParam("reciever") String reciever) {
		log.info("connection closed. Reason: " + closeReason.getReasonPhrase());

		WebSocketGameUser gameUser = new WebSocketGameUser(sender);
		webSocketSessionHandler.removeUser(gameUser);
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
