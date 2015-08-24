package com.chessApp.db;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.chessApp.model.UserAccount;


@Component
public class LocalAuthenticationProvider extends
		AbstractUserDetailsAuthenticationProvider {

	private final Logger logger = LoggerFactory
			.getLogger(LocalAuthenticationProvider.class);

	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private PasswordEncoder encoder;
	

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
		String hashPassword = null;
		try {
			hashPassword = usersRepository.encryptUserPassword(password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!StringUtils.hasText(password)) {
			logger.warn(username + ": no password provided", username);
			throw new BadCredentialsException("Please enter password");
		}

		if (!StringUtils.hasText(username)) {
			logger.warn(username + ": no login provided", username);
			throw new BadCredentialsException("Please enter login");
		}

		UserAccount user = usersRepository.getUserByUsername(username);

		if (user == null) {
			logger.warn(username + ": user not found", username, password);
			throw new UsernameNotFoundException("Invalid Login");
		}

		if (username.equals(user.getUsername())
				&& hashPassword.equals(user.getPassword())
				&& hashPassword != null) {

			final List<GrantedAuthority> auths = getAuthorities(user.getRole());

			return new User(user.getUsername(), user.getPassword(), true, // enabled
					true, // account not expired
					true, // credentials not expired
					true, // account not locked
					auths);
		} else {

			throw new UsernameNotFoundException("Invalid Login and password");
		}

	}

	public List<GrantedAuthority> getAuthorities(Integer role) {
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		if (role.intValue() == 1) {
			authList.add(new SimpleGrantedAuthority("ROLE_USER"));
			authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else if (role.intValue() == 2) {
			authList.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		return authList;
	}

}
