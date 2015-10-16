package com.chessApp.security;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.chessApp.dao.UsersRepository;
import com.chessApp.exceptions.LogInValidationException;
import com.chessApp.exceptions.UserNotConfirmedException;
import com.chessApp.model.UserAccount;
import com.chessApp.validation.forms.LoginForm;

@Component
public class LocalAuthenticationProvider extends
		AbstractUserDetailsAuthenticationProvider {

	private final Logger logger = Logger
			.getLogger(LocalAuthenticationProvider.class);

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private LoginForm loginForm;

	private PasswordEncryptor passwordEncrypter = new PasswordEncryptor();

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {

	}

	@Override
	public UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {

		String password = (String) authentication.getCredentials();

		loginForm.setUsername(username);
		loginForm.setPassword(password);
		Validator validation = Validation.buildDefaultValidatorFactory()
				.getValidator();
		Set<ConstraintViolation<LoginForm>> constraintViolations = validation
				.validate(loginForm);

		if (constraintViolations.size() != 0) {

			String validationMessages = null;
			for (ConstraintViolation<LoginForm> constraintViolation : constraintViolations) {
				validationMessages += constraintViolation.getMessage();
				validationMessages += " ";
			}

			throw new LogInValidationException(validationMessages);
		}

		String hashPassword = null;
		try {
			hashPassword = passwordEncrypter.encryptUserPassword(password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!StringUtils.hasText(password)) {
			logger.warn(username + ": no password provided");
			throw new BadCredentialsException("Please enter password");
		}

		if (!StringUtils.hasText(username)) {
			logger.warn(username + ": no login provided");
			throw new BadCredentialsException("Please enter login");
		}

		UserAccount user = usersRepository.getUserByUsername(username);

		if (user == null) {
			logger.warn(username + ": user not found");
			throw new UsernameNotFoundException("Invalid Login");
		}

		if (user.getIsRegistrationConfirmed() == false) {
			logger.warn(username + ": not confirmed");
			throw new UserNotConfirmedException(username + ": not confirmed");
		}

		if (username.equals(user.getUsername())
				&& hashPassword.equals(user.getPassword())
				&& hashPassword != null) {

			final List<GrantedAuthority> auths = UserRoles.getUserRoles(user
					.getRole());

			return new User(user.getUsername(), user.getPassword(), true, // enabled
					true, // account not expired
					true, // credentials not expired
					true, // account not locked
					auths);
		} else {

			throw new UsernameNotFoundException("Invalid Login and password");
		}

	}

}
