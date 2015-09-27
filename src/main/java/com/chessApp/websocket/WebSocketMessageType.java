package com.chessApp.websocket;

public class WebSocketMessageType {

	public final static String USER_CONNECT = "welcome-msg";

	public final static String GAME_HANDSHAKE_INVITATION = "game-handshake-invitation";

	public final static String GAME_HANDSHAKE_AGREEMENT = "game-handshake-agreement";

	public final static String GAME_HANDSHAKE_REFUSE = "game-handshake-refuse";

	public final static String CHESS_MOVE = "chess-move";
	
	public final static String GAME_OVER = "game-over";
	
	public final static String QUIT_GAME = "quit-game";

	public final static String USER_DISCONNECT = "goodbye-msg";

}
