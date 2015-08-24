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
import com.chessApp.db.UsersRepository;
import com.chessApp.model.UserAccount;

@Controller
public class AdminPanelController {

	@Autowired
	private UsersRepository repository;
	private static final Logger logger = Logger
			.getLogger(AdminPanelController.class);

	@RequestMapping("admin/panel")
	public ModelAndView getAdminSite() {
		logger.info("getAdminSite()");
		ModelAndView adminPage = new ModelAndView("admin");
		
		addBasicObjectsToModelAndView(adminPage);

		return adminPage;

	}

	// users table
	@RequestMapping("admin/users")
	public ModelAndView getAllUsers() {
		
		logger.info("getAllUsers()");
		List<UserAccount> users = repository.getUsersList();

		ModelAndView usersPage = new ModelAndView("users");
		usersPage.addObject("users", users);
		addBasicObjectsToModelAndView(usersPage);

		return usersPage;
	}

	// edit user form -----GET
	@RequestMapping("admin/users/editUser")
	public ModelAndView showEditUserForm(
			@RequestParam("username") String username, String msg) {
		
		logger.info("showEditUserForm()");
		UserAccount user = repository.getUserByUsername(username);


		ModelAndView userDetailPage = new ModelAndView("editUser");
		userDetailPage.addObject("user", user);
		userDetailPage.addObject("msg", msg);
		addBasicObjectsToModelAndView(userDetailPage);

		return userDetailPage;
	}

	// edit user ------ POST
	@RequestMapping(value = "admin/users/editUser", method = RequestMethod.POST)
	public ModelAndView sendEditUserData(
			@RequestParam Map<String, String> userDataMap) {
		
		logger.info("sendEditUserData()");

		String userLogin = userDataMap.get("j_username");
		String name = userDataMap.get("j_name");
		String lastname = userDataMap.get("j_lastname");
		String adminFlagSendedByForm = userDataMap.get("j_adminFlag");
		String email = userDataMap.get("j_email");

		UserAccount user = new UserAccount(userLogin, name, lastname);

		String changePasswordFlag = userDataMap.get("j_changePasswordFlag");
		if (changePasswordFlag != null
				&& changePasswordFlag.equalsIgnoreCase("on")) {

			String pass = userDataMap.get("j_password");
			String passConfirm = userDataMap.get("j_confirm_password");

			if (!pass.equals(passConfirm)) {

				return showEditUserForm(user.getUsername(),
						"password and confirm password have to be equal");
			}
			// hash password
			String hashedPassword = null;
			try {
				hashedPassword = repository.encryptUserPassword(pass)
						.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (hashedPassword != null) {
				user.setPassword(hashedPassword);
			}
		}

		if (adminFlagSendedByForm != null
				&& adminFlagSendedByForm.equalsIgnoreCase("on")) {
			user.setRole(1);
		} else {
			user.setRole(2);
		}

		user.setEmail(email);
		repository.updateUser(user);

		ModelAndView userDetailPage = new ModelAndView("editUser");
		userDetailPage.addObject("user", user);

		return getAllUsers();
	}

	// user account details for the user
	@RequestMapping("/user/your-account")
	public ModelAndView getLoggedInUserDetails(String msg) {
		
		logger.info("getLoggedInUserDetails()");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String currentUserLogin = auth.getName();

		UserAccount user = repository.getUserByUsername(currentUserLogin);

		ModelAndView yourAccount = new ModelAndView("yourAccount");
		yourAccount.addObject("user", user);
		yourAccount.addObject("msg", msg);
		addBasicObjectsToModelAndView(yourAccount);

		return yourAccount;
	}
	
	@RequestMapping("/admin/your-account")
	public ModelAndView getLoggedInAdminDetails(String msg) {
		
		logger.info("getLoggedInUserDetails()");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String currentUserLogin = auth.getName();

		UserAccount user = repository.getUserByUsername(currentUserLogin);

		ModelAndView yourAccount = new ModelAndView("adminAccount");
		yourAccount.addObject("user", user);
		yourAccount.addObject("msg", msg);
		addBasicObjectsToModelAndView(yourAccount);

		return yourAccount;
	}

	// edit user for user-------- POST
	@RequestMapping(value = "/user/your-account", method = RequestMethod.POST)
	public ModelAndView sendEditUserDataForUserAccount(
			@RequestParam Map<String, String> userDataMap) {

		logger.info("sendEditUserDataForUserAccount()");
		String userLogin = userDataMap.get("j_username");
		String name = userDataMap.get("j_name");
		String lastname = userDataMap.get("j_lastname");
		String adminFlagSendedByForm = userDataMap.get("j_adminFlag");
		String email = userDataMap.get("j_email");

		logger.info("adminFlag");
		logger.info(adminFlagSendedByForm);

		UserAccount user = new UserAccount(userLogin, name, lastname);

		String changePasswordFlag = userDataMap.get("j_changePasswordFlag");
		if (changePasswordFlag != null
				&& changePasswordFlag.equalsIgnoreCase("on")) {

			String pass = userDataMap.get("j_password");
			String passConfirm = userDataMap.get("j_confirm_password");

			if (!pass.equals(passConfirm)) {

				return getLoggedInUserDetails("password and confirm password have to be equal");
			}
			// hash password
			String hashedPassword = null;
			try {
				hashedPassword = repository.encryptUserPassword(pass)
						.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (hashedPassword != null) {
				user.setPassword(hashedPassword);
			}

		}
		user.setEmail(email);
		user.setRole(2);
		repository.updateUser(user);

		return getLoggedInUserDetails(null);
	}

	// remove user
	@RequestMapping(value = "admin/users/remove", method = RequestMethod.POST)
	public ModelAndView removeUser(@RequestParam("username") String username) {
		logger.info("removeUser()");
		
		UserAccount user = repository.getUserByUsername(username);
		repository.deleteUser(user);

		return getAllUsers();
	}

	// add user form---- GET
	@RequestMapping("/admin/users/addUser#addUserForm")
	public ModelAndView getAddUserForm(String msg) {
		logger.info("getAddUserForm()");
		
		ModelAndView addUserPage = new ModelAndView("users");
		addUserPage.addObject("msg", msg);
		addBasicObjectsToModelAndView(addUserPage);

		return addUserPage;
	}

	// add user into DB action--- POST
	@RequestMapping(value = "admin/users/addUser", method = RequestMethod.POST)
	public ModelAndView addUser(@RequestParam Map<String, String> userParams) {

		String plaintextPassword = userParams.get("j_password");
		String userLogin = userParams.get("j_username");
		String name = userParams.get("j_name");
		String lastname = userParams.get("j_lastname");
		String adminFlag = userParams.get("j_adminFlag");
		String confirmPassword = userParams.get("j_confirm_password");
		String email = userParams.get("j_email");

		// validation
		if (!plaintextPassword.equals(confirmPassword)) {

			return getAddUserForm("password and confirm password have to be equal");
		}
		int role;
		if (adminFlag != null) {
			role = 1;
		} else {
			role = 2;
		}

		// hash password
		String hashedPassword = null;
		try {
			hashedPassword = repository.encryptUserPassword(plaintextPassword)
					.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		UserAccount user = new UserAccount(userLogin, hashedPassword, name,
				lastname, role, email);

		String result = repository.addUser(user);

		if (result.equals("ok")) {
			return getAllUsers();
		} else {
			return getAddUserForm("login " + userLogin + " allready exists!");
		}

	}
	
private void addBasicObjectsToModelAndView(ModelAndView modelAndView) {
		
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		String userLogin = auth.getName();
		modelAndView.addObject("currentUserName", userLogin);
		
	}

}
