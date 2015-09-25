package com.chessApp.websocket;

import org.springframework.stereotype.Component;

@Component
public class WebSocketGameUser {

	private long id;
	private String username;
	private Boolean isPlayingNow;
	private String playNowWithUser;
	private long numberOfScores;
	private String chessColor;
	private Boolean isDuringHandShake;

	public WebSocketGameUser() {
	}

	public WebSocketGameUser(String username) {
		this.username = username;
	}

	public Boolean getIsDuringHandShake() {
		return isDuringHandShake;
	}

	public void setIsDuringHandShake(Boolean isDuringHandShake) {
		this.isDuringHandShake = isDuringHandShake;
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

	public Boolean getIsPlayingNow() {
		return isPlayingNow;
	}

	public void setIsPlayingNow(Boolean isPlayingNow) {
		this.isPlayingNow = isPlayingNow;
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
		return "WebSocketGameUser [id=" + id + ", username=" + username
				+ ", isPlayingNow=" + isPlayingNow + ", numberOfScores="
				+ numberOfScores + "]";
	}

}
