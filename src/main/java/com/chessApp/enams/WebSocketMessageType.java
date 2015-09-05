package com.chessApp.enams;

public enum WebSocketMessageType {

	CHESS_MOVE("chess-move"), USER_CONNECT("welcome-msg"), USER_DISCONNECT(
			"goodbye-msg");

	private String msg;

	public String message() {
		return msg;
	}

	private WebSocketMessageType(String msg) {
		this.msg = msg;
	}

}
