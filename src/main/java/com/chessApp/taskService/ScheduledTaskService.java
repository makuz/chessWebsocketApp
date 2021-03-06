package com.chessApp.taskService;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.chessApp.dao.UsersRepository;
import com.chessApp.model.UserAccount;

@Service
public class ScheduledTaskService {

	private static final Logger logger = Logger
			.getLogger(ScheduledTaskService.class);

	private final long ONE_HOUR_MS = 3600000;
	private final long ONE_DAY_MS = ONE_HOUR_MS * 24;
	private final long THREE_DAYS_MS = ONE_DAY_MS * 3;

	@Autowired
	private UsersRepository usersRpository;

	@Scheduled(fixedRate = THREE_DAYS_MS)
	public void doScheduledTask() {

		System.out
				.println("-------------ScheduledTaskService-----------------");
		cleanUsersFromNotConfirmedByOneWeek();

	}

	private void cleanUsersFromNotConfirmedByOneWeek() {
		logger.info("cleanUsersFromNotConfirmedByOneWeek()");
		logger.info("Method executed at time : " + new Date() + " ");

		List<UserAccount> allUsers = usersRpository.getUsersList();

		for (UserAccount userAccount : allUsers) {
			Date userRegistrationDate = userAccount.getRegistrationDate();
			Boolean isUserConfirmed = userAccount.getIsRegistrationConfirmed();
			String registrationToken = userAccount.getRegistrationHashString();
			Boolean registrationTokenIsNotEmpty = !StringUtils
					.isEmpty(registrationToken);

			if (registrationToken != null && registrationTokenIsNotEmpty) {
				if (isUserConfirmed == false) {
					if (isNotConfirmedAccountDeprecated(userRegistrationDate)) {

						usersRpository.deleteUser(userAccount);

						logger.debug("User: " + userAccount.getUsername()
								+ " was deprecated");
						logger.debug(userAccount.getUsername()
								+ " was removed from db");

					}
				}
			}

		}
	}

	public static boolean isNotConfirmedAccountDeprecated(Date registrationDate) {
		
		Integer ONE_WEEK_PLUS_ONE_DAY = 8;

		DateTime currentDate = new DateTime(new Date());
		DateTime userRegistrationDateTime = new DateTime(registrationDate);

		Integer numberOfDays = Days.daysBetween(userRegistrationDateTime,
				currentDate).getDays();

		if (numberOfDays > ONE_WEEK_PLUS_ONE_DAY) {
			return true;
		} else {
			return false;
		}

	}

}
