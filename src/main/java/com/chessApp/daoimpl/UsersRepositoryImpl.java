package com.chessApp.daoimpl;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.chessApp.dao.UsersRepository;
import com.chessApp.model.UserAccount;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	public static final String COLLECTION_NAME = "users";

	private final Logger logger = Logger.getLogger(UsersRepositoryImpl.class);

	public String addUser(UserAccount user) {
		logger.debug("addUser()");

		if (!mongoTemplate.collectionExists(UserAccount.class)) {
			mongoTemplate.createCollection(UserAccount.class);
		}

		UserAccount testUser = getUserByUsername(user.getUsername());

		if (testUser == null) {
			autoIncrementUserId(user);
			mongoTemplate.insert(user, COLLECTION_NAME);
			logger.info("user added");
			return "ok";
		} else {
			logger.info("login allready exist");
			return "fail";
		}

	}

	private void autoIncrementUserId(UserAccount usertoUpdate) {
		logger.debug("incrementUserId()");

		Query query = new Query();
		UserAccount user = mongoTemplate.findOne(query, UserAccount.class,
				COLLECTION_NAME);
		if (user == null) {
			usertoUpdate.setUserId(0);
		} else {
			List<UserAccount> users = mongoTemplate.findAll(UserAccount.class,
					COLLECTION_NAME);
			UserAccount lastUserBefore = users.get(users.size() - 1);
			long lastUserBeforeId = lastUserBefore.getUserId();
			logger.info("lastUser id before sorting");
			logger.info(lastUserBeforeId + "");

			Collections.sort(users);
			UserAccount lastUser = users.get(users.size() - 1);
			long lastUserId = lastUser.getUserId();
			logger.info("lastUser id after sorting");
			logger.info(lastUserId + "");
			usertoUpdate.setUserId(lastUserId + 1);

		}
	}

	public List<UserAccount> getUsersList() {

		logger.debug("getUsersList()");

		return mongoTemplate.findAll(UserAccount.class, COLLECTION_NAME);
	}

	public UserAccount getUserByUsername(String username) {

		logger.debug("getUserByUsername()");
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username));
		UserAccount user = mongoTemplate.findOne(query, UserAccount.class,
				COLLECTION_NAME);

		return user;
	}

	public UserAccount getUserByRegistrationHashString(
			String registrationHashString) {
		logger.debug("getUserByRegistrationHashString()");

		Query query = new Query();
		query.addCriteria(Criteria.where("registrationHashString").is(
				registrationHashString));
		UserAccount user = mongoTemplate.findOne(query, UserAccount.class,
				COLLECTION_NAME);

		return user;
	}

	public UserAccount getUserById(long userId) {

		logger.debug("getUserById()");
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		UserAccount user = mongoTemplate.findOne(query, UserAccount.class,
				COLLECTION_NAME);

		return user;
	}

	public void deleteUser(UserAccount user) {
		logger.debug("deleteUser()");
		mongoTemplate.remove(user, COLLECTION_NAME);
	}

	// UPDATE
	public void updateUser(UserAccount user) {
		logger.debug("updateUser()");

		Update updateUserData = new Update();
		updateUserData.set("name", user.getName());
		updateUserData.set("lastname", user.getLastname());
		updateUserData.set("role", user.getRole());
		updateUserData.set("email", user.getEmail());
		updateUserData.set("isRegistrationConfirmed",
				user.getIsRegistrationConfirmed());
		updateUserData.set("registrationDate", user.getRegistrationDate());
		updateUserData.set("registrationHashString",
				user.getRegistrationHashString());
		if (user.getNumberOfGamesPlayed() != null) {
			updateUserData.set("numberOfGamesPlayed",
					user.getNumberOfGamesPlayed());
		}
		if (user.getNumberOfWonChessGames() != null) {
			updateUserData.set("numberOfWonChessGames",
					user.getNumberOfWonChessGames());
		}
		if (user.getNumberOfLostChessGames() != null) {
			updateUserData.set("numberOfLostChessGames",
					user.getNumberOfLostChessGames());
		}

		if (user.getPassword() != null
				&& !user.getPassword().equalsIgnoreCase("")) {
			updateUserData.set("password", user.getPassword());
		}

		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(user.getUsername()));

		mongoTemplate.updateFirst(query, updateUserData, COLLECTION_NAME);

	}
}