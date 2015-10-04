package com.chessApp.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.chessApp.dao.UsersRepository;
import com.chessApp.model.UserAccount;

@Controller
public class RegistrationConfirmationController {

	@Autowired
	private UsersRepository usersRepository;

	private Logger logger = Logger
			.getLogger(RegistrationConfirmationController.class);

	@RequestMapping(value = "registration/confirm/{hash}", method = RequestMethod.GET)
	public ModelAndView confirmEmailAccount(@PathVariable("hash") String hash) {
		logger.debug("confirmEmailAccount()");

		UserAccount user = usersRepository
				.getUserByRegistrationHashString(hash);

		if (user != null) {
			user.setIsRegistrationConfirmed(true);
			user.setRegistrationHashString("");
			usersRepository.updateUser(user);
		} else {
			ModelAndView error = new ModelAndView("error");
			error.addObject("errorMessage", "SOMETHINGS GOES WRONG");
			return error;
		}

		ModelAndView confirmRegistrationMsg = new ModelAndView(
				"confirmRegistrationMessage");
		confirmRegistrationMsg.addObject("username", user.getUsername());
		return confirmRegistrationMsg;
	}

}
