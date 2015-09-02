package com.chessApp.websocket;

public class Message {

	private String type;

	private String fen;

	private String senderName;

	private String sendTo;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	@Override
	public String toString() {
		return "Message [type=" + type + ", fen=" + fen + ", senderName="
				+ senderName + ", sendTo=" + sendTo + "]";
	}
	
	

}
