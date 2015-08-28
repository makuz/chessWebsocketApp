package com.chessApp.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

import org.apache.log4j.Logger;


@ApplicationScoped
public class WebSocketSessionHandler {

	Logger logger = Logger.getLogger(WebSocketSessionHandler.class);

	private static long userID = 0;

	private static Set<Session> sessions = Collections
			.synchronizedSet(new HashSet<Session>());
	private final Set<WebSocketGameUser> gameUsers = new HashSet<>();

	public void addSession(Session session) {
		sessions.add(session);
	}

	public void removeSession(Session session) {
		sessions.remove(session);
	}

	public void addUser(WebSocketGameUser gameUser) {
		userID++;
		gameUser.setId(userID);
		gameUsers.add(gameUser);
		logger.info("user: " + gameUser + " added to live game repository");
	}

	public void removeUser(WebSocketGameUser gameUser) {
		gameUsers.remove(gameUser);
		logger.info("user: " + gameUser + " removed from live game repository");
	}
	
	public void removeUserById(long id) {
		for (WebSocketGameUser webSocketGameUser : gameUsers) {
			if(webSocketGameUser.getId() == id) {
				gameUsers.remove(webSocketGameUser);
				
			}
		}
	}

	public void toggleUser(long id) {

	}

	public WebSocketGameUser getUserById(long id) {
		
		for (WebSocketGameUser webSocketGameUser : gameUsers) {
			if(webSocketGameUser.getId() == id) {
				return webSocketGameUser;
			}
		}
		return null;
	}

	public String createAddMessage(WebSocketGameUser user) {
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
		logger.info("sessionOwner: " + addedSession.getUserProperties().get("sessionOwner"));
		
		logger.info("obecne sesje: ");
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
		logger.info("sessionOwner: "
				+ removedSession.getUserProperties().get("sessionOwner"));
		
		logger.info("pozostałe sesje: ");
		for (Session session : sessions) {
			System.out.println(session.getId());
		}
	}

}
