package com.chessApp.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.chessApp.dao.UsersRepository;
import com.chessApp.model.UserAccount;
import com.chessApp.props.Messages;
import com.chessApp.security.PasswordEncryptor;
import com.chessApp.security.UserRoles;
import com.chessApp.validation.forms.SignUpForm;

@Controller
public class AdminPanelController {

	@Autowired
	private UsersRepository usersRepository;

	private PasswordEncryptor passwordEncrypter = new PasswordEncryptor();

	private static final Logger logger = Logger
			.getLogger(AdminPanelController.class);

	@RequestMapping(value = "admin/users", method = RequestMethod.GET)
	public ModelAndView getAllUsers() {

		logger.info("getAllUsers()");
		List<UserAccount> users = usersRepository.getUsersList();

		ModelAndView usersPage = new ModelAndView("users");
		usersPage.addObject("users", users);
		addBasicObjectsToModelAndView(usersPage);

		return usersPage;
	}

	@RequestMapping(value = "admin/users/editUser", method = RequestMethod.GET)
	public ModelAndView showEditUserForm(
			@RequestParam("username") String username, String msg) {

		logger.info("showEditUserForm()");
		UserAccount user = usersRepository.getUserByUsername(username);

		ModelAndView userDetailPage = new ModelAndView("editUser");
		userDetailPage.addObject("user", user);
		userDetailPage.addObject("msg", msg);
		addBasicObjectsToModelAndView(userDetailPage);

		return userDetailPage;
	}

	@RequestMapping(value = "admin/users/editUser", method = RequestMethod.POST)
	public ModelAndView sendEditUserData(
			@RequestParam Map<String, String> userDataMap) {

		logger.info("sendEditUserData()");

		String userLogin = userDataMap.get("j_username");
		String name = userDataMap.get("j_name");
		String lastname = userDataMap.get("j_lastname");
		String adminFlagSendedByForm = userDataMap.get("j_adminFlag");
		String email = userDataMap.get("j_email");

		UserAccount user = usersRepository.getUserByUsername(userLogin);
		user.setName(name);
		user.setLastname(lastname);

		String changePasswordFlag = userDataMap.get("j_changePasswordFlag");
		if (changePasswordFlag != null
				&& changePasswordFlag.equalsIgnoreCase("on")) {

			String pass = userDataMap.get("j_password");
			String passConfirm = userDataMap.get("j_confirm_password");

			if (!pass.equals(passConfirm)) {

				return showEditUserForm(user.getUsername(),
						"password and confirm password have to be equal");
			}
			// hash password
			String hashedPassword = null;
			try {
				hashedPassword = passwordEncrypter.encryptUserPassword(pass)
						.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (hashedPassword != null) {
				user.setPassword(hashedPassword);
			}
		}

		if (adminFlagSendedByForm != null
				&& adminFlagSendedByForm.equalsIgnoreCase("on")) {
			user.setRole(1);
		} else {
			user.setRole(2);
		}

		user.setEmail(email);
		usersRepository.updateUser(user);

		ModelAndView userDetailPage = new ModelAndView("editUser");
		userDetailPage.addObject("user", user);

		return getAllUsers();
	}

	@RequestMapping(value = "admin/users/remove", method = RequestMethod.POST)
	public ModelAndView removeUser(@RequestParam("username") String username) {
		logger.info("removeUser()");

		UserAccount user = usersRepository.getUserByUsername(username);
		usersRepository.deleteUser(user);

		return getAllUsers();
	}

	@RequestMapping(value = "/admin/users/addUser", method = RequestMethod.GET)
	public ModelAndView getAddUserForm(String errorMsg, String successMsg) {
		logger.info("getAddUserForm()");

		ModelAndView addUserPage = new ModelAndView("addUser");
		addUserPage.addObject("errorMessage", errorMsg);
		addUserPage.addObject("successMsg", successMsg);
		SignUpForm signUpForm = new SignUpForm();
		addUserPage.addObject("signUpForm", signUpForm);
		addBasicObjectsToModelAndView(addUserPage);

		return addUserPage;
	}

	@RequestMapping(value = "admin/users/addUser", method = RequestMethod.POST)
	public ModelAndView addUser(
			@Valid @ModelAttribute("signUpForm") SignUpForm signUpForm,
			BindingResult result) {
		logger.debug("addUser()");

		if (result.hasErrors()) {
			ModelAndView addUserPage = new ModelAndView("addUser");
			addUserPage.addObject("signUpForm", signUpForm);
			return addUserPage;
		}

		String userLogin = signUpForm.getUsername();
		String plaintextPassword = signUpForm.getPassword();
		String confirmPassword = signUpForm.getConfirmPassword();
		Boolean adminFlag = signUpForm.getGrantAdminAuthorities();
		String email = signUpForm.getEmail();

		// validation
		if (!plaintextPassword.equals(confirmPassword)) {

			return getAddUserForm(
					Messages.getProperty("error.passwords.notequal"), null);
		}

		UserAccount user = new UserAccount();
		user.setUsername(userLogin);

		if (adminFlag) {
			user.setRole(UserRoles.ADMIN.geNumericValue());
		} else {
			user.setRole(UserRoles.USER.geNumericValue());
		}

		try {
			String hashedPassword = passwordEncrypter.encryptUserPassword(
					plaintextPassword).toString();
			user.setPassword(hashedPassword);
		} catch (Exception e) {
			logger.debug(e);
		}

		user.setEmail(email);
		user.setIsRegistrationConfirmed(true);
		user.setRegistrationDate(new Date());

		String updateResult = usersRepository.addUser(user);

		if (updateResult.equals("ok")) {
			return getAddUserForm(null,
					Messages.getProperty("success.user.created"));
		} else {
			return getAddUserForm("login " + userLogin + " allready exists!",
					null);
		}

	}

	private void addBasicObjectsToModelAndView(ModelAndView modelAndView) {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		String userLogin = auth.getName();
		modelAndView.addObject("currentUserName", userLogin);

	}

}
