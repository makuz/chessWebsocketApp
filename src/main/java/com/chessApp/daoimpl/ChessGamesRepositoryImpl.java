package com.chessApp.daoimpl;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.chessApp.dao.ChessGamesRepository;
import com.chessApp.model.ChessGame;
import com.chessApp.model.UserAccount;

@Repository
public class ChessGamesRepositoryImpl implements ChessGamesRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	public static final String COLLECTION_NAME = "chessGames";

	private final Logger logger = Logger
			.getLogger(ChessGamesRepositoryImpl.class);

	@Override
	public String saveGame(ChessGame game) {
		logger.debug("saveGame()");

		if (!mongoTemplate.collectionExists(UserAccount.class)) {
			mongoTemplate.createCollection(UserAccount.class);
		}

		ChessGame testGame = getByUniqueGameHash(game.getUniqueGameHash());

		System.out.println("mongoTemplate");
		System.out.println(mongoTemplate);

		if (testGame == null) {
			autoIncrementChessGameId(game);
			mongoTemplate.insert(game, COLLECTION_NAME);

			return "ok";
		} else {
			return "faild";
		}

	}

	private void autoIncrementChessGameId(ChessGame gameToUpdate) {
		logger.debug("incrementChessGameId()");

		Query query = new Query();
		ChessGame game = mongoTemplate.findOne(query, ChessGame.class);
		if (game == null) {
			gameToUpdate.setChessGameId(0);
		} else {
			List<ChessGame> games = mongoTemplate.findAll(ChessGame.class,
					COLLECTION_NAME);

			Collections.sort(games);
			ChessGame lastGame = games.get(games.size() - 1);
			long lastGameId = lastGame.getChessGameId();
			gameToUpdate.setChessGameId(lastGameId + 1);

		}
	}

	@Override
	public String removeGame(ChessGame game) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ChessGame> getUserChessGames(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ChessGame> getAllChessGames() {
		logger.debug("getAllChessGames()");
		return mongoTemplate.findAll(ChessGame.class, COLLECTION_NAME);
	}

	@Override
	public ChessGame getBychessGameId(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChessGame getByUniqueGameHash(String uniqueGameHash) {
		logger.debug("getByUniqueGameHash()");

		Query query = new Query();
		query.addCriteria(Criteria.where("uniqueGameHash").is(uniqueGameHash));
		ChessGame fondedGame = mongoTemplate.findOne(query, ChessGame.class,
				COLLECTION_NAME);
		return fondedGame;
	}

}
