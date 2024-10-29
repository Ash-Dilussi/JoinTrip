package com.example.JoinGoREST.controllers;

import javax.management.Notification;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.JoinGoREST.Model.DTO.DriverMatchResMAS;
import com.example.JoinGoREST.Model.DTO.JoinRequestDTO;
import com.example.JoinGoREST.Model.Entity.TaxiRequest;

@Controller
public class WebSocketController {

	private final SimpMessagingTemplate messagingTemplate;

	public WebSocketController(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	// Send ride request to passengers
	public void sendRideRequestToPassenger(JoinRequestDTO request) {
		messagingTemplate.convertAndSend("/topic/passenger/requests", request);
	}

	// Send notifications to passengers
	public void sendNotificationToPassenger(JoinRequestDTO notification) {
		messagingTemplate.convertAndSend("/topic/passenger/notifications", notification);
	}

	// Send updates to drivers
	public void sendTripToDriver(DriverMatchResMAS update, String userId) {
		messagingTemplate.convertAndSend("/topic/driver/updates"+ userId, update);
	}

	// Send status updates to drivers
	public void sendStatusUpdateToDriver(JoinRequestDTO status) {
		messagingTemplate.convertAndSend("/topic/driver/status", status);
	}
}
