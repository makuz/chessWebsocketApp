package com.chessApp.db;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.chessApp.model.UserAccount;

@Repository
public class UsersRepository {

	@Autowired
	private MongoTemplate mongoTemplate;
	public static final String COLLECTION_NAME = "users";
	private final Logger logger = LoggerFactory
			.getLogger(UsersRepository.class);

	// CREATE
	public String addUser(UserAccount user) {
		logger.info("addUser()");

		if (!mongoTemplate.collectionExists(UserAccount.class)) {
			mongoTemplate.createCollection(UserAccount.class);
		}

		UserAccount testUser = getUserByUsername(user.getUsername());

		if (testUser == null) {
			incrementUserId(user);
			mongoTemplate.insert(user, COLLECTION_NAME);
			logger.info("user added");
			return "ok";
		} else {
			logger.info("login allready exist");
			return "fail";
		}

	}

	// autoIncrement function
	public void incrementUserId(UserAccount usertoUpdate) {

		logger.info("incrementUserId()");
		Query query = new Query();

		UserAccount user = mongoTemplate.findOne(query, UserAccount.class);
		// query.with(new Sort(new Order(Direction.DESC, "id")));
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

	// READ ALL
	public List<UserAccount> getUsersList() {

		logger.info("getUsersList()");

		return mongoTemplate.findAll(UserAccount.class, COLLECTION_NAME);
	}

	// READ ONE BY NAME
	public UserAccount getUserByUsername(String username) {

		logger.info("getUserByUsername()");
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username));
		UserAccount user = mongoTemplate.findOne(query, UserAccount.class);

		return user;
	}

	// READ ONE BY ID
	public UserAccount getUserById(long userId) {

		logger.info("getUserById()");
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		UserAccount user = mongoTemplate.findOne(query, UserAccount.class);

		return user;
	}

	// DELETE----------------------------
	public void deleteUser(UserAccount user) {
		logger.info("deleteUser()");
		mongoTemplate.remove(user, COLLECTION_NAME);
	}

	// UPDATE
	public void updateUser(UserAccount user) {
		logger.info("updateUser()");

		Update updateUserData = new Update();
		updateUserData.set("name", user.getName());
		updateUserData.set("lastname", user.getLastname());
		updateUserData.set("role", user.getRole());
		updateUserData.set("email", user.getEmail());

		if (user.getPassword() != null
				&& !user.getPassword().equalsIgnoreCase("")) {
			updateUserData.set("password", user.getPassword());
		}

		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(user.getUsername()));

		logger.info("user to update------------");
		UserAccount thisUser = getUserByUsername(user.getUsername());
		logger.info(thisUser + "");

		mongoTemplate.updateFirst(query, updateUserData, COLLECTION_NAME);

	}

}