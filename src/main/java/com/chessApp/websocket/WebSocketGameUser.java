package com.chessApp.websocket;

public class WebSocketGameUser {
	
	private long id;
	private String username;
	
	public WebSocketGameUser(String username) {
		this.username = username;
	}
	
	private long numberOfScores;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getNumberOfScores() {
		return numberOfScores;
	}

	public void setNumberOfScores(long numberOfScores) {
		this.numberOfScores = numberOfScores;
	}

	@Override
	public String toString() {
		return "WebSocketGameUser [id=" + id + ", username=" + username + "]";
	}
	
	

}
