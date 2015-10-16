package com.chessApp.validation.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginForm {

	@NotNull(message = "please enter a login")
	@Size(min = 5, max = 10, message = "your login should be between 5 - 10 characters")
	private String username;

	@NotNull(message = "please enter a password")
	@Size(min = 6, max = 30, message = "password should be between 6 - 30 characters")
	private String password;

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

	@Override
	public String toString() {
		return "LoginForm [username=" + username + ", password=" + password
				+ "]";
	}

}
