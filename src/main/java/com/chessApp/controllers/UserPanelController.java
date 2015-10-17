package com.chessApp.controllers;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.chessApp.dao.ChessGamesRepository;
import com.chessApp.dao.UsersRepository;
import com.chessApp.model.ChessGame;
import com.chessApp.model.UserAccount;
import com.chessApp.props.Messages;
import com.chessApp.security.PasswordEncryptor;
import com.chessApp.validation.forms.EditForm;

@Controller
public class UserPanelController {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private ChessGamesRepository chessGamesRepository;

	private PasswordEncryptor passwordEncrypter = new PasswordEncryptor();

	private static final Logger logger = Logger
			.getLogger(UserPanelController.class);

	@RequestMapping("/user/your-account")
	public ModelAndView getLoggedInUserDetails(String errorrMessage,
			String successMessage) {

		logger.debug("getLoggedInUserDetails()");

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String currentUserLogin = auth.getName();

		UserAccount user = usersRepository.getUserByUsername(currentUserLogin);

		ModelAndView yourAccount = new ModelAndView("yourAccount");
		EditForm editForm = new EditForm();
		yourAccount.addObject("editForm", editForm);
		yourAccount.addObject("user", user);
		yourAccount.addObject("errorrMessage", errorrMessage);
		yourAccount.addObject("successMessage", successMessage);
		addBasicObjectsToModelAndView(yourAccount);

		return yourAccount;
	}

	@RequestMapping(value = "/user/your-account", method = RequestMethod.POST)
	public ModelAndView sendEditUserDataForUserAccount(
			@Valid @ModelAttribute("editForm") EditForm editForm,
			BindingResult result) {
		logger.debug("sendEditUserDataForUserAccount()");

		Boolean changePasswordFlag = editForm.getChangePasswordFlag();
		Boolean changePasswordCheckBoxIsUnchecked = !changePasswordFlag;
		if (changePasswordCheckBoxIsUnchecked) {
			if (result.hasFieldErrors("email") || result.hasFieldErrors("name")
					|| result.hasFieldErrors("lastname")) {
				ModelAndView editFormSite = new ModelAndView("yourAccount");
				editFormSite.addObject("changePasswordCheckBoxIsChecked",
						changePasswordFlag);
				editFormSite.addObject("editForm", editForm);
				return editFormSite;
			}
		} else {
			if (result.hasErrors()) {
				ModelAndView editFormSite = new ModelAndView("yourAccount");
				editFormSite.addObject("changePasswordCheckBoxIsChecked",
						changePasswordFlag);
				editFormSite.addObject("editForm", editForm);
				return editFormSite;
			}
		}

		String userLogin = editForm.getUsername();
		String name = editForm.getName();
		String lastname = editForm.getLastname();

		String email = editForm.getEmail();
		String password = editForm.getPassword();
		String confirmPassword = editForm.getConfirmPassword();

		if (!password.equals(confirmPassword)) {

			return getLoggedInUserDetails(
					Messages.getProperty("error.passwords.notequal"), null);
		}

		UserAccount user = usersRepository.getUserByUsername(userLogin);

		if (!StringUtils.isBlank(name)) {
			user.setName(name);
		}
		if (!StringUtils.isBlank(lastname)) {
			user.setLastname(lastname);
		}

		if (changePasswordFlag) {
			try {
				String hashedPassword = passwordEncrypter.encryptUserPassword(
						password).toString();
				user.setPassword(hashedPassword);
			} catch (Exception e) {
				logger.debug(e);
			}
		}
		user.setEmail(email);
		user.setRole(2);
		usersRepository.updateUser(user);

		return getLoggedInUserDetails(null,
				Messages.getProperty("success.user.edit"));
	}

	@RequestMapping(value = "/user/your-chessgames", method = RequestMethod.GET)
	public ModelAndView userGamesSite() {
		logger.debug("userGamesSite()");

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String userLogin = auth.getName();

		ModelAndView userGamesSite = new ModelAndView("userGames");
		addBasicObjectsToModelAndView(userGamesSite);
		List<ChessGame> userChessGames = chessGamesRepository
				.getUserChessGames(userLogin);

		UserAccount userInfo = usersRepository.getUserByUsername(userLogin);

		userGamesSite.addObject("userChessGames", userChessGames);
		userGamesSite.addObject("user", userInfo);

		return userGamesSite;
	}

	private void addBasicObjectsToModelAndView(ModelAndView modelAndView) {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		String userLogin = auth.getName();
		modelAndView.addObject("currentUserName", userLogin);

	}

}
