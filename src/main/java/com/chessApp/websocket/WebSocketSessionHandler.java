package com.chessApp.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.chessApp.model.UserAccount;

@ApplicationScoped
public class WebSocketSessionHandler {

	Logger logger = Logger.getLogger(WebSocketSessionHandler.class);

	private static long userID = 0;

	private static Set<Session> sessions = Collections
			.synchronizedSet(new HashSet<Session>());
	private final Set<UserAccount> users = new HashSet<>();

	public void addSession(Session session) {
		sessions.add(session);
	}

	public void removeSession(Session session) {
		sessions.remove(session);
	}

	public void addUser(UserAccount user) {
		userID++;
		user.setUserId(userID);
		users.add(user);
	}

	public void removeUser(long id) {

	}

	public void toggleUser(long id) {

	}

	public UserAccount getUserById(long id) {
		return null;
	}

	public String createAddMessage(UserAccount user) {
		return null;
	}

	public void sendToAllConnectedSessions(String msg) {
		for (Session userSession : sessions) {
			try {
				userSession.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				logger.info(e + "");
			}
		}
	}

	public void sendToSession(Session session, String message) {
	}

	public void printOutAllSessionsOnOpen(Session addedSession) {

		logger.info("dodano sesje: " + addedSession.getId());
		logger.info("sender: " + addedSession.getUserProperties().get("sender"));
		logger.info("reciever: "
				+ addedSession.getUserProperties().get("reciever"));
		logger.info("sesje: ");
		for (Session session : sessions) {
			System.out.println(session);
		}

	}

	public Boolean userIsAllreadyConnected(String username) {
		for (Session userSession : sessions) {

			Map<String, Object> userPropertiesInSession = userSession
					.getUserProperties();
			if (userPropertiesInSession.containsValue(username)) {
				return true;
			}
		}
		return false;
	}

	public void printOutAllSessionsOnClose(Session removedSession) {
		logger.info("usunieto sesje: " + removedSession.getId());
		logger.info("sender: "
				+ removedSession.getUserProperties().get("sender"));
		logger.info("reciever: "
				+ removedSession.getUserProperties().get("reciever"));
		logger.info("pozostałe sesje: ");
		for (Session session : sessions) {
			System.out.println(session.getId());
		}
	}

}
