package com.chessApp.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.chessApp.db.UsersRepository;
import com.chessApp.model.UserAccount;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = Logger.getLogger(HomeController.class);
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry;

	// home page
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home() {
		logger.info("homePage()");

		ModelAndView homePageModel = new ModelAndView("home");
		addBasicObjectsToModelAndView(homePageModel);

		return homePageModel;
	}

	// play chess
	@RequestMapping(value = "/play-chess-with-computer", method = RequestMethod.GET)
	public ModelAndView playChessWithComputer() {
		logger.info("playChessWithComputer()");

		ModelAndView playChessWithCompPageModel = new ModelAndView("playChess");
		addBasicObjectsToModelAndView(playChessWithCompPageModel);

		return playChessWithCompPageModel;
	}

	// play chess
	@RequestMapping(value = "/play-chess-with-user", method = RequestMethod.GET)
	public ModelAndView playChessWithUser(HttpSession sessionObj) {
		logger.info("playChessWithUser()");
		
		logger.info("session id");
		logger.info(sessionObj.getId());
		logger.info("session creation time");
		logger.info(sessionObj.getCreationTime());

		ModelAndView playChessWithUserPageModel = new ModelAndView("playChessWithUser");
		addBasicObjectsToModelAndView(playChessWithUserPageModel);
		
		List<Object> principals = sessionRegistry.getAllPrincipals();
		
		logger.info(principals);
		Integer usersCount = principals.size();
		List<String> usersNamesList = new ArrayList<String>();

		for (Object principal: principals) {
		    if (principal instanceof User) {
		        usersNamesList.add(((User) principal).getUsername());
		    }
		}
		logger.info(usersNamesList);
		if (usersNamesList.isEmpty()) {
			usersNamesList = null;
		}
		playChessWithUserPageModel.addObject("onlineUsers", usersNamesList);
		playChessWithUserPageModel.addObject("usersCount", usersCount);

		return playChessWithUserPageModel;
	}

	// log in
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView logIn() {
		logger.info("logIn()");
		ModelAndView loginPageModel = new ModelAndView("login");
		addBasicObjectsToModelAndView(loginPageModel);
		
		return loginPageModel;
	}

	// invalid login
	@RequestMapping("/fail")
	public ModelAndView getFailPage() {

		ModelAndView errorPage = new ModelAndView("error");
		errorPage.addObject("errorMessage", "login fail");

		return errorPage;

	}

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

	@RequestMapping("/signin/create/account")
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
			hashPassword = usersRepository.encryptUserPassword(userPassword);
		} catch (Exception e) {
			e.printStackTrace();
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
