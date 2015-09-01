package com.chessApp.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class UserAccount implements Comparable<UserAccount> {

	@Id
	private String id;
	private long userId;
	private String username;
	private String password;
	private String name;
	private String lastname;
	private int role;
	private String email;

	// for user sign in
	public UserAccount(String username, String password, int role, String email) {

		this.username = username;
		this.password = password;
		this.role = role;
		this.email = email;

	}

	public UserAccount(long userId, String username, String password,
			String name, String lastname, int role, List<String> trackTermsList) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastname = lastname;
		this.role = role;
	}

	public UserAccount(String username, String password, String name,
			String lastname, int role, String email) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastname = lastname;
		this.role = role;
		this.email = email;
	}

	public UserAccount(String username, String password, String name,
			String lastname) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastname = lastname;
	}

	public UserAccount(String username, String name, String lastname) {
		this.username = username;
		this.name = name;
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserAccount() {
	}

	public UserAccount(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "UserAccount [id=" + id + ", userId=" + userId + ", username="
				+ username + ", password=" + password + ", name=" + name
				+ ", lastname=" + lastname + ", role=" + role + ", email="
				+ email + "]";
	}

	@Override
	public int compareTo(UserAccount userAcc) {
		
		long compareUserId = userAcc.getUserId(); 
		 
		return (int) (this.userId - compareUserId);
 
	}

}
