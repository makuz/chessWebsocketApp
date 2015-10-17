package com.chessApp.validation.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SignUpForm {

	@NotNull(message = "please enter a login")
	@Size(min = 5, max = 10, message = "your login should be between 5 - 10 characters")
	private String username;

	@NotNull(message = "please enter your email")
	@Pattern(regexp = ".+@.+\\..+", message = "wrong email!")
	private String email;

	@NotNull(message = "please enter a password")
	@Size(min = 6, max = 30, message = "password should be between 6 - 30 characters")
	private String password;

	@NotNull(message = "please enter a password")
	@Size(min = 6, max = 30, message = "password should be between 6 - 30 characters")
	private String confirmPassword;

	private Boolean grantAdminAuthorities;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Boolean getGrantAdminAuthorities() {
		return grantAdminAuthorities;
	}

	public void setGrantAdminAuthorities(Boolean grantAdminAuthorities) {
		this.grantAdminAuthorities = grantAdminAuthorities;
	}

	@Override
	public String toString() {
		return "SignUpForm [username=" + username + ", email=" + email
				+ ", password=" + password + ", confirmPassword="
				+ confirmPassword + ", grantAdminAuthorities="
				+ grantAdminAuthorities + "]";
	}

}
