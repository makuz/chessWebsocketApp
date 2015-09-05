package com.chessApp.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class WebSocketSessionHandler {

	Logger logger = Logger.getLogger(WebSocketSessionHandler.class);

	Gson gson = new Gson();

	private static long userID = 0;

	private static final Map<String, Session> sessionsMap = new ConcurrentHashMap<>();

	private static final Map<String, WebSocketGameUser> gameUsersMap = new ConcurrentHashMap<>();

	public synchronized Boolean userListNotContainsUser(String username) {

		if (gameUsersMap.containsKey(username)) {
			return false;
		}

		return true;
	}

	public synchronized void addSession(String username, Session session) {
		sessionsMap.put(username, session);
	}

	public synchronized void removeSession(String username) {
		sessionsMap.remove(username);
	}

	public synchronized void addUser(WebSocketGameUser gameUser) {
		userID++;
		gameUser.setId(userID);
		gameUsersMap.put(gameUser.getUsername(), gameUser);
		logger.info("user: " + gameUser + " added to live game repository");
	}

	public synchronized void removeUser(WebSocketGameUser gameUser) {
		gameUsersMap.remove(gameUser);
		logger.info("user: " + gameUser + " removed from live game repository");
	}

	public void sendToAllConnectedSessions(String msg) {
		for (String username : sessionsMap.keySet()) {
			Session userSession = sessionsMap.get(username);
			try {
				userSession.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				logger.info(e);
			}
		}
	}

	public void sendToAllConnectedSessionsActualParticipantList() {

		String jsonUsersList = gson.toJson(gameUsersMap.values());
		for (String username : sessionsMap.keySet()) {
			Session userSession = sessionsMap.get(username);
			try {
				userSession.getBasicRemote().sendText(jsonUsersList);
			} catch (IOException e) {
				logger.info(e);
			}
		}
	}

	public void sendToSession(String sendToUsernameName, String message) {
		logger.info("sendToSession()");

		Session userSession = sessionsMap.get(sendToUsernameName);
		if (userSession != null) {
			try {
				userSession.getBasicRemote().sendText(message);
			} catch (IOException e) {
				logger.debug(e);
			}
		}

	}

	public void printOutAllSessionsOnOpen(Session addedSession) {

		logger.info("dodano sesje: " + addedSession.getId());
		logger.info("sessionOwner: "
				+ addedSession.getUserProperties().get("sessionOwner"));

		logger.info("obecne sesje: ");
		for (String username : sessionsMap.keySet()) {
			Session session = sessionsMap.get(username);
			System.out.println(session);
		}

	}

	public Boolean isUserAllreadyConnected(String username) {
		Session userSession = sessionsMap.get(username);
		Map<String, Object> userPropertiesInSession = userSession
				.getUserProperties();
		if (userPropertiesInSession.containsValue(username)) {
			return true;
		}
		return false;
	}

	public void printOutAllSessionsOnClose(Session removedSession) {
		logger.info("usunieto sesje: " + removedSession.getId());
		logger.info("sessionOwner: "
				+ removedSession.getUserProperties().get("sessionOwner"));

		logger.info("pozostałe sesje: ");
		for (String username : sessionsMap.keySet()) {
			Session session = sessionsMap.get(username);
			System.out.println(session.getId());
		}
	}

}
