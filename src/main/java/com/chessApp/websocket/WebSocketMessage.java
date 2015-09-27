package com.chessApp.websocket;

import org.springframework.stereotype.Component;

@Component
public class WebSocketMessage {

	private String type;

	private String fen;

	private String sendTo;

	private String sendFrom;

	private String moveBelongsToColor;

	private WebSocketGameUser messageAuthorObject;

	public WebSocketMessage() {

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMoveBelongsToColor() {
		return moveBelongsToColor;
	}

	public void setMoveBelongsToColor(String moveBelongsToColor) {
		this.moveBelongsToColor = moveBelongsToColor;
	}

	public WebSocketGameUser getMessageAuthorObject() {
		return messageAuthorObject;
	}

	public void setMessageAuthorObject(WebSocketGameUser messageAuthorObject) {
		this.messageAuthorObject = messageAuthorObject;
	}

	public String getSendFrom() {
		return sendFrom;
	}

	public void setSendFrom(String sendFrom) {
		this.sendFrom = sendFrom;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public String getFen() {
		return fen;
	}

	public void setFen(String fen) {
		this.fen = fen;
	}

	@Override
	public String toString() {
		return "WebSocketMessage [type=" + type + ", fen=" + fen + ", sendTo="
				+ sendTo + ", sendFrom=" + sendFrom + "]";
	}

}
