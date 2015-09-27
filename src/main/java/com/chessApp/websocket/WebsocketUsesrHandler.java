package com.chessApp.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class WebsocketUsesrHandler {

	private static final Logger logger = Logger
			.getLogger(WebsocketUsesrHandler.class);

	private volatile static long userID = 0;

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

	public synchronized void setUserComunicationStatus(String username,
			String status) {
		WebSocketGameUser gameUser = gameUsersMap.get(username);
		gameUser.setCommunicationStatus(status);
	}

	public synchronized void setComStatusIsDuringHandshake(String username) {
		logger.debug("setComStatusIsPlaying()");

		setUserComunicationStatus(username,
				GameUserCommunicationStatus.IS_DURING_HANDSHAKE);
	}

	public synchronized void setComStatusWaitForNewGame(String username) {
		logger.debug("setComStatusWaitForNewGame()");

		WebSocketGameUser gameUser = gameUsersMap.get(username);
		gameUser.setCommunicationStatus(GameUserCommunicationStatus.WAIT_FOR_NEW_GAME);
		gameUser.setPlayNowWithUser(null);
	}

	public synchronized void setComStatusIsPlaying(String toUsername,
			String fromUsername) {
		logger.debug("setComStatusIsPlaying()");

		WebSocketGameUser gameUser = gameUsersMap.get(toUsername);
		gameUser.setCommunicationStatus(GameUserCommunicationStatus.IS_PLAYING);
		gameUser.setPlayNowWithUser(fromUsername);

	}

	public synchronized void setChessPiecesColorForGamers(String toUsername,
			String fromUsername) {

		WebSocketGameUser invitingUser = gameUsersMap.get(fromUsername);
		WebSocketGameUser recievingUser = gameUsersMap.get(fromUsername);

		invitingUser.setChessColor(ChessPieces.WHITE);
		recievingUser.setChessColor(ChessPieces.BLACK);

	}

	public void printOutUsersList() {
		logger.info("printOutUsersList()");
		for (String key : gameUsersMap.keySet()) {
			System.out.println(gameUsersMap.get(key));
		}

	}

}
