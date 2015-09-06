package com.chessApp.controllers;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.servlet.ModelAndView;

import com.chessApp.db.UsersRepository;

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


	private void addBasicObjectsToModelAndView(ModelAndView modelAndView) {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		String userLogin = auth.getName();
		modelAndView.addObject("currentUserName", userLogin);

	}

}
