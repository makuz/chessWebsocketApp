package com.chessApp.controllers;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.chessApp.websocketEvents.LoginEvent;
import com.chessApp.websocketEvents.ParticipantRepository;

/*
 * narazie klasa nie używana
 */

@Controller
public class LiveGameController {
	
	Logger logger = Logger.getLogger(LiveGameController.class);
	
	@Autowired 
	private ParticipantRepository participantRepository;
	
	@SubscribeMapping("/topic.participants")
	public Collection<LoginEvent> retrieveParticipants() {
		logger.info("retrieveParticipants()");
		
		return participantRepository.getActiveSessions().values();
	}
	
	@MessageMapping("/send-fen")
    @SendTo("/topic/position")
    public String greeting(String message) throws Exception {
        Thread.sleep(3000); // simulated delay
        return "fen: , " + message;
    }

}
