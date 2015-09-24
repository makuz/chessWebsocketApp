package com.chessApp.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chessApp.dao.UsersRepository;
import com.chessApp.model.UserAccount;

@Controller
public class UsersAjaxController {

	@Autowired
	private UsersRepository usersRepository;

	private static final Logger logger = Logger
			.getLogger(UsersAjaxController.class);

	@RequestMapping(value = "user/get-user-info-by-username", method = RequestMethod.GET)
	public @ResponseBody UserAccount getUserInfoByUsername(
			@RequestParam("username") String username) {
		logger.debug("getUserInfoById()");

		UserAccount user = usersRepository.getUserByUsername(username);

		return user;
	}

}
