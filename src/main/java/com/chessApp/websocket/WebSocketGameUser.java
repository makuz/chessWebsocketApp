package com.chessApp.websocket;

import org.springframework.stereotype.Component;

@Component
public class WebSocketGameUser {

	private long id;
	private String username;
	private String playNowWithUser;
	private long numberOfScores;
	private String chessColor;
	private String communicationStatus;

	public WebSocketGameUser() {
	}

	public WebSocketGameUser(String username) {
		this.username = username;
	}

	public String getChessColor() {
		return chessColor;
	}

	public String getPlayNowWithUser() {
		return playNowWithUser;
	}

	public void setPlayNowWithUser(String playNowWithUser) {
		this.playNowWithUser = playNowWithUser;
	}

	public void setChessColor(String chessColor) {
		this.chessColor = chessColor;
	}

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

	public String getCommunicationStatus() {
		return communicationStatus;
	}

	public void setCommunicationStatus(String communicationStatus) {
		this.communicationStatus = communicationStatus;
	}

	@Override
	public String toString() {
		return "WebSocketGameUser [id=" + id + ", username=" + username
				+ ", playNowWithUser=" + playNowWithUser + ", numberOfScores="
				+ numberOfScores + ", chessColor=" + chessColor
				+ ", communicationStatus=" + communicationStatus + "]";
	}

}
