package com.chessApp.websocket;

import org.springframework.stereotype.Component;

import com.chessApp.model.ChessMove;

@Component
public class WebSocketMessage {

	private String type;

	private String fen;

	private String sendTo;

	private String sendFrom;

	private String moveStatus;

	private WebSocketGameUser sendToObj;

	private WebSocketGameUser sendFromObj;

	private Boolean checkMate;

	private ChessMove chessMove;

	public WebSocketMessage() {

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public WebSocketGameUser getSendToObj() {
		return sendToObj;
	}

	public void setSendToObj(WebSocketGameUser sendToObj) {
		this.sendToObj = sendToObj;
	}

	public WebSocketGameUser getSendFromObj() {
		return sendFromObj;
	}

	public void setSendFromObj(WebSocketGameUser sendFromObj) {
		this.sendFromObj = sendFromObj;
	}

	public Boolean getCheckMate() {
		return checkMate;
	}

	public void setCheckMate(Boolean checkMate) {
		this.checkMate = checkMate;
	}

	public String getMoveStatus() {
		return moveStatus;
	}

	public void setMoveStatus(String moveStatus) {
		this.moveStatus = moveStatus;
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

	public ChessMove getChessMove() {
		return chessMove;
	}

	public void setChessMove(ChessMove chessMove) {
		this.chessMove = chessMove;
	}

	@Override
	public String toString() {
		return "WebSocketMessage [type=" + type + ", fen=" + fen + ", sendTo="
				+ sendTo + ", sendFrom=" + sendFrom + ", moveStatus="
				+ moveStatus + ", sendToObj=" + sendToObj + ", sendFromObj="
				+ sendFromObj + ", checkMate=" + checkMate + ", chessMove="
				+ chessMove + "]";
	}

}
