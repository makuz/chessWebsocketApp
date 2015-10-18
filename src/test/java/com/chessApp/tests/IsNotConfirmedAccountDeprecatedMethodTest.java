package com.chessApp.tests;

import static org.junit.Assert.*;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.chessApp.taskService.ScheduledTaskService;

public class IsNotConfirmedAccountDeprecatedMethodTest {

	private DateTime dateTime;
	private Date today;
	private Date twoDaysAgo;
	private Date twoWeeksAgo;

	@Before
	public void prepare() {
		today = new Date();
		dateTime = new DateTime(today);
		twoDaysAgo = dateTime.minusDays(2).toDate();
		twoWeeksAgo = dateTime.minusWeeks(2).toDate();
	}

	@Test
	public void registrationWasTodayNotConfirmedAccountIsNotDepravated() {
		// given
		Boolean expectedResultAccountIsNotDepracated = false;
		Boolean givenResult;
		// when
		givenResult = ScheduledTaskService
				.isNotConfirmedAccountDeprecated(today);
		// then
		assertEquals(expectedResultAccountIsNotDepracated, givenResult);
	}

	@Test
	public void registrationWasTwoDaysAgoNotConfirmedAccountIsNotDepravated() {
		// given
		Boolean expectedResultAccountIsNotDepracated = false;
		Boolean givenResult;
		// when
		givenResult = ScheduledTaskService
				.isNotConfirmedAccountDeprecated(twoDaysAgo);
		// then
		assertEquals(expectedResultAccountIsNotDepracated, givenResult);
	}

	@Test
	public void registrationWasTwoWeeksAgoNotConfirmedAccountIsDepravated() {
		// given
		Boolean expectedResultAccountIsDepracated = true;
		Boolean givenResult;
		// when
		givenResult = ScheduledTaskService
				.isNotConfirmedAccountDeprecated(twoWeeksAgo);
		// then
		assertEquals(expectedResultAccountIsDepracated, givenResult);
	}

}
