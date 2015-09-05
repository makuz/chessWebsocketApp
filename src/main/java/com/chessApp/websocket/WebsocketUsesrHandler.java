package com.chessApp.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class WebsocketUsesrHandler {

	Logger logger = Logger.getLogger(WebsocketUsesrHandler.class);

	private static long userID = 0;

	protected static final Map<String, WebSocketGameUser> gameUsersMap = new ConcurrentHashMap<>();

	public synchronized Boolean userListNotContainsUsername(String username) {
		if (gameUsersMap.containsKey(username)) {
			return false;
		}
		return true;
	}

	public synchronized void addWebsocketUser(WebSocketGameUser gameUser) {
		logger.debug("");
		
		userID++;
		gameUser.setId(userID);
		gameUsersMap.put(gameUser.getUsername(), gameUser);
		logger.info("user: " + gameUser + " added to live game repository");
	}

	public synchronized WebSocketGameUser getWebsocketUser(String username) {
		WebSocketGameUser gameUser = gameUsersMap.get(username);
		return gameUser;
	}

	public synchronized void removeWebsocketUser(String username) {
		WebSocketGameUser gameUser = gameUsersMap.remove(username);
		logger.info("user: " + gameUser + " removed from live game repository");
	}

	public void printOutUsersList() {
		logger.info("printOutUsersList()");
		for (String key : gameUsersMap.keySet()) {
			System.out.println(gameUsersMap.get(key));
		}

	}

}
