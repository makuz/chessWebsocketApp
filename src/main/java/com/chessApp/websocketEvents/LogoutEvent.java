package com.chessApp.websocketEvents;

/*
 * narazie klasa nie używana
 */
public class LogoutEvent {

	private String username;

	public LogoutEvent(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
