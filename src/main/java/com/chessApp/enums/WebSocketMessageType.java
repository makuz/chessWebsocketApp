package com.chessApp.enums;

public enum WebSocketMessageType {

	GAME_HANDSHAKE_INVITATION("game-handshake-invitation"), GAME_HANDSHAKE_AGREEMENT(
			"game-handshake-agreement"), GAME_HANDSHAKE_REFUSE(
			"game-handshake-refuse"), CHESS_MOVE("chess-move"), USER_CONNECT(
			"welcome-msg"), USER_DISCONNECT("goodbye-msg");

	private String msg;

	public String message() {
		return msg;
	}

	private WebSocketMessageType(String msg) {
		this.msg = msg;
	}

}
