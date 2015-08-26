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
@ServerEndpoint("/send-fen/{sender}/{reciever}")
public class ChessAppServerEndpoint {

	Logger log = Logger.getLogger(ChessAppServerEndpoint.class);

	private static Set<Session> usersSessions = Collections
			.synchronizedSet(new HashSet<Session>());

	private void sendMessageToAll(String msg) {
		for (Session userSession : usersSessions) {
			try {
				userSession.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				log.info(e + "");
			}
		}
	}

	@OnMessage
	public void onMessage(String msg, Session wsSession,
			@PathParam("sender") String sender,
			@PathParam("reciever") String reciever) throws IOException {

		log.info("message recived on serverEndpoint: " + msg + " from "
				+ sender + " to " + reciever);

		// wsSession.getBasicRemote().sendText(msg);
		sendMessageToAll(msg);

	}

	@OnOpen
	public void onOpen(Session wsSession, @PathParam("sender") String sender,
			@PathParam("reciever") String reciever) {
		log.info("connection started, websocket session id: "
				+ wsSession.getId() + " from " + sender + " to " + reciever);

		sendMessageToAll(sender + " has connected");

		wsSession.getUserProperties().put("sender", sender);
		wsSession.getUserProperties().put("reciever", reciever);
		System.out.println("session path parameters");
		System.out.println(wsSession.getPathParameters());
		usersSessions.add(wsSession);

		log.info("dodano sesje: " + wsSession.getId());
		log.info("sender: " + wsSession.getUserProperties().get("sender"));
		log.info("reciever: " + wsSession.getUserProperties().get("reciever"));
		log.info("sesje: ");
		for (Session session : usersSessions) {
			System.out.println(session);
		}
	}

	@OnClose
	public void onClose(CloseReason closeReason, Session wsSession,
			@PathParam("sender") String sender,
			@PathParam("reciever") String reciever) {
		log.info("connection closed. Reason: " + closeReason.getReasonPhrase());
		sendMessageToAll(sender + " has disconnected");
		usersSessions.remove(wsSession);
		log.info("usunieto sesje: " + wsSession.getId());
		log.info("sender: " + wsSession.getUserProperties().get("sender"));
		log.info("reciever: " + wsSession.getUserProperties().get("reciever"));
		log.info("pozostałe sesje: ");
		for (Session session : usersSessions) {
			System.out.println(session.getId());
		}
	}

	@OnError
	public void onErrorReceived(Throwable t) {
		log.debug("there was an error with connection");
	}

}
