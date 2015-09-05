package com.chessApp.taskService;

import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/*
 * narazie klasa nie używana
 */
@Component
public class ScheduledTaskService {

	private static final Logger logger = Logger
			.getLogger(ScheduledTaskService.class);
	
	//metoda wykonujaca sie raz na godzine
	//moze się przydac pozniej do tworzenia danych statystycznych
	@Scheduled(fixedRate = 3600000)
	public void doScheduledTask() {
		
		logger.info("---------------------------------------------------");
		logger.info("Method executed at time : " + new Date() + " ");
		


	}




}
