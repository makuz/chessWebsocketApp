package com.chessApp.tests;

import static org.junit.Assert.*;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.chessApp.model.ChessGame;
import com.chessApp.websocket.ChessGamesHandler;

public class CalculateTimeDurationBeetwenGameBeginAndEndMethodTest {

	private ChessGame game;
	private DateTime dateTime;

	@Before
	public void prepare() {
		game = new ChessGame();
	}

	@Test
	public void durationIs10MinnutesTest() {

		// given
		long duration10MinutesExpected = 6000000;
		Date beginDate = new Date();
		dateTime = new DateTime(beginDate);
		Date dateAfterTenMinnutes = dateTime.plus(duration10MinutesExpected)
				.toDate();

		game.setBeginDate(beginDate);
		game.setEndDate(dateAfterTenMinnutes);
		// when
		ChessGamesHandler
				.calculateAndSetTimeDurationBeetwenGameBeginAndEnd(game);

		long durationResult = game.getGameDurationMillis();
		// then
		assertEquals(duration10MinutesExpected, durationResult);

	}

	@Test
	public void durationIs1HouerTest() {

		// given
		long duration1HouerExpected = 3600000;
		Date beginDate = new Date();
		dateTime = new DateTime(beginDate);
		Date dateAfterTenMinnutes = dateTime.plus(duration1HouerExpected)
				.toDate();

		game.setBeginDate(beginDate);
		game.setEndDate(dateAfterTenMinnutes);
		// when
		ChessGamesHandler
				.calculateAndSetTimeDurationBeetwenGameBeginAndEnd(game);

		long durationResult = game.getGameDurationMillis();
		// then
		assertEquals(duration1HouerExpected, durationResult);

	}

}
