package com.chessApp.dao;

import java.util.List;

import com.chessApp.model.UserAccount;

public interface UsersRepository {

	public String addUser(UserAccount user);

	public List<UserAccount> getUsersList();

	public UserAccount getUserByUsername(String username);

	public UserAccount getUserByRegistrationHashString(
			String registrationHashString);

	public UserAccount getUserById(long userId);

	public void deleteUser(UserAccount user);

	public void updateUser(UserAccount user);

}
