package com.chessApp.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.chessApp.dao.UsersRepository;
import com.chessApp.props.Messages;
import com.chessApp.validation.forms.LoginForm;

@Controller
public class LoginController {

	@Autowired
	private UsersRepository repository;

	private static final Logger logger = Logger
			.getLogger(LoginController.class);

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView getLoginPage() {
		logger.debug("getLoginPage()");
		ModelAndView loginPageModel = new ModelAndView("login");
		addBasicObjectsToModelAndView(loginPageModel);

		return loginPageModel;
	}

	@RequestMapping("/fail")
	public ModelAndView getFailPage() {
		logger.debug("getFailPage()");

		ModelAndView errorPage = new ModelAndView("error");
		errorPage.addObject("errorMessage",
				Messages.getProperty("error.loginfailed"));

		return errorPage;

	}

	private void addBasicObjectsToModelAndView(ModelAndView modelAndView) {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		String userLogin = auth.getName();
		modelAndView.addObject("currentUserName", userLogin);
		LoginForm loginForm = new LoginForm();
		modelAndView.addObject("loginForm", loginForm);

	}

}
