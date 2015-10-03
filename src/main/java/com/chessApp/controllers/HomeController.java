package com.chessApp.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.chessApp.dao.UsersRepository;
import com.chessApp.model.UserAccount;

@Controller
public class HomeController {

	private static final Logger logger = Logger.getLogger(HomeController.class);

	@Autowired
	private UsersRepository repository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home() {
		logger.info("homePage()");

		ModelAndView homePageModel = new ModelAndView("home");
		addBasicObjectsToModelAndView(homePageModel);

		return homePageModel;
	}

	@RequestMapping(value = "/home/best-players", method = RequestMethod.GET)
	public ModelAndView bestPlayersSite() {
		logger.info("bestPlayersSite()");

		ModelAndView bestPlayers = new ModelAndView("bestPlayers");
		addBasicObjectsToModelAndView(bestPlayers);
		List<UserAccount> bestPlayingUsers = repository.getBestPlaying10Users();
		bestPlayers.addObject("bestPlayingUsers", bestPlayingUsers);

		return bestPlayers;
	}

	private void addBasicObjectsToModelAndView(ModelAndView modelAndView) {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		String userLogin = auth.getName();
		modelAndView.addObject("currentUserName", userLogin);

	}

}
