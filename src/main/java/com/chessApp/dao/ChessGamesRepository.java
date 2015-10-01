package com.chessApp.dao;

import java.util.List;

import com.chessApp.model.ChessGame;

public interface ChessGamesRepository {

	public String saveGame(ChessGame game);

	public String removeGame(ChessGame game);

	public List<ChessGame> getUserChessGames(String username);

	public List<ChessGame> getAllChessGames();
	
	public ChessGame getBychessGameId(long id);
	
	public ChessGame getByUniqueGameHash(String uniqueGameHash);

}
