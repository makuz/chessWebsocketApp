package com.chessApp.controllers;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.chessApp.dao.UsersRepository;
import com.chessApp.mailService.MailService;
import com.chessApp.model.UserAccount;
import com.chessApp.security.PasswordEncryptor;
import com.chessApp.security.UserRoles;

@Controller
public class SignInController {

	@Autowired
	private UsersRepository usersRepository;

	private PasswordEncryptor passwordEncrypter = new PasswordEncryptor();

	private static final Logger logger = Logger
			.getLogger(SignInController.class);

	@Autowired
	private MailService mailService;

	// sign in
	@RequestMapping("/signin")
	public ModelAndView getSignInForm(String msg) {

		ModelAndView signInSite = new ModelAndView("signIn");
		if (msg != null) {
			signInSite.addObject("errorMessage", msg);
		}
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
			accountCreationInfo.addObject("userMail", userLogin);
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

			return getSignInForm("password and confirm password have to be equal");
		}

		String hashPassword = null;
		try {
			hashPassword = passwordEncrypter.encryptUserPassword(userPassword);
		} catch (Exception e) {
			logger.debug(e);
		}

		UserAccount newUser = new UserAccount();
		newUser.setUsername(userLogin);
		newUser.setPassword(hashPassword);
		newUser.setRole(UserRoles.USER.geNumericValue());
		newUser.setEmail(userLogin);

		String randomHashString = UUID.randomUUID().toString();

		newUser.setRegistrationHashString(randomHashString);
		newUser.setIsRegistrationConfirmed(false);
		newUser.setRegistrationDate(new Date());

		String creationMessage = usersRepository.addUser(newUser);

		if (creationMessage.equals("fail")) {

			return getSignInForm("taki login już istnieje, wybierz inny");

		}

		mailService.sendRegistrationMail(userLogin, randomHashString);

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
