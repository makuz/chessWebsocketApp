package com.chessApp.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chessGames")
public class ChessGame {

	@Id
	private String id;
	private Date beginDate;
	private Date endDate;
	private String endingGameFENString;
	private long chessGameId;
	private String winnerUsername;
	private String loserUsername;
	private String winnerColor;
	private int numberOfMoves;

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getEndingGameFENString() {
		return endingGameFENString;
	}

	public void setEndingGameFENString(String endingGameFENString) {
		this.endingGameFENString = endingGameFENString;
	}

	public long getChessGameId() {
		return chessGameId;
	}

	public void setChessGameId(long chessGameId) {
		this.chessGameId = chessGameId;
	}

	public String getWinnerUsername() {
		return winnerUsername;
	}

	public void setWinnerUsername(String winnerUsername) {
		this.winnerUsername = winnerUsername;
	}

	public String getWinnerColor() {
		return winnerColor;
	}

	public void setWinnerColor(String winnerColor) {
		this.winnerColor = winnerColor;
	}

	public int getNumberOfMoves() {
		return numberOfMoves;
	}

	public void setNumberOfMoves(int numberOfMoves) {
		this.numberOfMoves = numberOfMoves;
	}

	public String getLoserUsername() {
		return loserUsername;
	}

	public void setLoserUsername(String loserUsername) {
		this.loserUsername = loserUsername;
	}

}
