package com.chessApp.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
@ServerEndpoint("/send-fen/{username1}/{username2}")
public class ChessAppServerEndpoint {

	Logger log = Logger.getLogger(ChessAppServerEndpoint.class);

	private static Set<Session> usersSessions = Collections
			.synchronizedSet(new HashSet<Session>());

	@OnMessage
	public void onMessage(String msg, Session wsSession,
			@PathParam("username1") String username1,
			@PathParam("username2") String username2) throws IOException {

		log.info("message recived on serverEndpoint: " + msg + " from "
				+ username1 + " to " + username2);
		wsSession.getBasicRemote().sendText(msg);

	}

	@OnOpen
	public void onOpen(Session wsSession,
			@PathParam("username1") String username1,
			@PathParam("username2") String username2) {
		log.info("connection started, websocket session id: "
				+ wsSession.getId() + " from " + username1 + " to " + username2);
		wsSession.getUserProperties().put(username1, username1);
		wsSession.getUserProperties().put(username2, username2);
		usersSessions.add(wsSession);
	}

	@OnClose
	public void onClose(CloseReason closeReason, Session wsSession,
			@PathParam("username1") String username1,
			@PathParam("username2") String username2) {
		log.info("connection closed. Reason: " + closeReason.getReasonPhrase());
		usersSessions.remove(wsSession);
	}

	@OnError
	public void onErrorReceived(Throwable t) {
		log.debug("there was an error with connection");
	}

}
