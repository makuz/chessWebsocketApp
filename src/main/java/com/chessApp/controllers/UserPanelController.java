package com.chessApp.controllers;

import java.util.List;
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

import com.chessApp.dao.ChessGamesRepository;
import com.chessApp.dao.UsersRepository;
import com.chessApp.model.ChessGame;
import com.chessApp.model.UserAccount;
import com.chessApp.security.PasswordEncryptor;

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
	public ModelAndView getLoggedInUserDetails(String msg) {

		logger.debug("getLoggedInUserDetails()");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String currentUserLogin = auth.getName();

		UserAccount user = usersRepository.getUserByUsername(currentUserLogin);

		ModelAndView yourAccount = new ModelAndView("yourAccount");
		yourAccount.addObject("user", user);
		yourAccount.addObject("msg", msg);
		addBasicObjectsToModelAndView(yourAccount);

		return yourAccount;
	}

	@RequestMapping(value = "/user/your-account", method = RequestMethod.POST)
	public ModelAndView sendEditUserDataForUserAccount(
			@RequestParam Map<String, String> userDataMap) {
		logger.debug("sendEditUserDataForUserAccount()");

		String userLogin = userDataMap.get("j_username");
		String name = userDataMap.get("j_name");
		String lastname = userDataMap.get("j_lastname");
		String adminFlagSendedByForm = userDataMap.get("j_adminFlag");
		String email = userDataMap.get("j_email");

		logger.debug("adminFlagSendedByForm");
		logger.debug(adminFlagSendedByForm);

		UserAccount user = usersRepository.getUserByUsername(userLogin);
		user.setName(name);
		user.setLastname(lastname);

		String changePasswordFlag = userDataMap.get("j_changePasswordFlag");
		if (changePasswordFlag != null
				&& changePasswordFlag.equalsIgnoreCase("on")) {

			String pass = userDataMap.get("j_password");
			String passConfirm = userDataMap.get("j_confirm_password");

			if (!pass.equals(passConfirm)) {

				return getLoggedInUserDetails("password and confirm password have to be equal");
			}

			String hashedPassword = null;
			try {
				hashedPassword = passwordEncrypter.encryptUserPassword(pass)
						.toString();
			} catch (Exception e) {
				logger.debug(e);
			}

			if (hashedPassword != null) {
				user.setPassword(hashedPassword);
			}

		}
		user.setEmail(email);
		user.setRole(2);
		usersRepository.updateUser(user);

		return getLoggedInUserDetails(null);
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

		userGamesSite.addObject("userChessGames", userChessGames);

		return userGamesSite;
	}

	private void addBasicObjectsToModelAndView(ModelAndView modelAndView) {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		String userLogin = auth.getName();
		modelAndView.addObject("currentUserName", userLogin);

	}

}
