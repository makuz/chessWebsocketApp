package com.chessApp.validation.forms;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditForm {

	private String username;

	@Size(max = 15, message = "name should be maximum 10 characters")
	private String name;

	@Size(max = 15, message = "lastname should be maximum 15 characters")
	private String lastname;

	@Pattern(regexp = ".+@.+\\..+", message = "wrong email!")
	private String email;

	@Size(min = 6, max = 30, message = "password should be between 6 - 30 characters")
	private String password;

	@Size(min = 6, max = 30, message = "password should be between 6 - 30 characters")
	private String confirmPassword;

	private Boolean changePasswordFlag;

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

	public Boolean getChangePasswordFlag() {
		return changePasswordFlag;
	}

	public void setChangePasswordFlag(Boolean changePasswordFlag) {
		this.changePasswordFlag = changePasswordFlag;
	}

	@Override
	public String toString() {
		return "EditForm [username=" + username + ", name=" + name
				+ ", lastname=" + lastname + ", email=" + email + ", password="
				+ password + ", confirmPassword=" + confirmPassword
				+ ", changePasswordFlag=" + changePasswordFlag + "]";
	}

}
