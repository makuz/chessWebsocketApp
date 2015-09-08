package com.chessApp.controllers;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.chessApp.db.UsersRepository;
import com.chessApp.helper.PasswordEncrypter;
import com.chessApp.model.UserAccount;

@Controller
public class SignInController {

	@Autowired
	private UsersRepository usersRepository;
	
	private PasswordEncrypter passwordEncrypter = new PasswordEncrypter();

	private static final Logger logger = Logger
			.getLogger(SignInController.class);

	// sign in
	@RequestMapping("/signin")
	public ModelAndView getSignInForm() {

		ModelAndView signInSite = new ModelAndView("signIn");
		addBasicObjectsToModelAndView(signInSite);

		return signInSite;
	}

	@RequestMapping("/signin/account/creation")
	public ModelAndView getSiteAccountCreationInfo(String userCreationMsg,
			boolean created, String userLogin, String userPassword) {

		ModelAndView accountCreationInfo = new ModelAndView(
				"creatAccountMessage");
		accountCreationInfo.addObject("msg", userCreationMsg);
		accountCreationInfo.addObject("created", created);
		if (created) {
			accountCreationInfo.addObject("userLogin", userLogin);
			accountCreationInfo.addObject("userPassword", userPassword);
		}
		addBasicObjectsToModelAndView(accountCreationInfo);

		return accountCreationInfo;

	}

	@RequestMapping(value = "/signin/create/account", method = RequestMethod.POST)
	public ModelAndView addUserAction(@RequestParam Map<String, String> reqMap) {

		String userLogin = reqMap.get("j_username");
		String userPassword = reqMap.get("j_password");
		String confirmPassword = reqMap.get("j_confirm_password");

		// validation
		if (!userPassword.equals(confirmPassword)) {

			return getSiteAccountCreationInfo(
					"password and confirm password have to be equal", false,
					userLogin, userPassword);
		}

		String hashPassword = null;
		try {
			hashPassword = passwordEncrypter.encryptUserPassword(userPassword);
		} catch (Exception e) {
			logger.debug(e);
		}

		UserAccount newUser = new UserAccount(userLogin, hashPassword, 2,
				userLogin);
		logger.info("newUser-----");
		logger.info(newUser);
		usersRepository.addUser(newUser);

		return getSiteAccountCreationInfo("user created successfull", true,
				userLogin, userPassword);

	}

	private void addBasicObjectsToModelAndView(ModelAndView modelAndView) {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		String userLogin = auth.getName();
		modelAndView.addObject("currentUserName", userLogin);

	}

}
