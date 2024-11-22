package com.example.JoinGoREST.controllers;

import javax.management.Notification;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.JoinGoREST.Model.DTO.ResDriverMatchDTO;
import com.example.JoinGoREST.Model.DTO.ResponsePassengerDTO;
import com.example.JoinGoREST.Model.DTO.JoinReqMsgDTO;
import com.example.JoinGoREST.Model.DTO.JoinRequestDTO;
import com.example.JoinGoREST.Model.Entity.TaxiDriverStatus;
import com.example.JoinGoREST.Model.Entity.TaxiRequest;

@Controller
public class WebSocketController {

	private final SimpMessagingTemplate messagingTemplate;

	public WebSocketController(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	// Send join request to passengers
	public void sendRideRequestToPassenger(JoinReqMsgDTO request, String userId) {
		System.out.println("To the Mbile: "+userId);
		messagingTemplate.convertAndSend("/specific/passenger/requests/"+ userId, request);
	}
	
	public void sendJoinListToPassenger(ResponsePassengerDTO listItem, String userId) {
		System.out.println("To the Mbile: "+userId);
		messagingTemplate.convertAndSend("/specific/passenger/joinList/"+ userId, listItem);
	}

	// Send new ride notifications to passengers
	public void sendRiderToPassenger(TaxiDriverStatus driverdata, String userId) {
	
		System.out.println("rider info sending to: "+ userId);
		try {
		messagingTemplate.convertAndSend("/specific/passenger/driverInfo/"+ userId, driverdata);
		} catch (Exception e) {
	        // Log the exception or handle it accordingly
	        System.err.println("Error while sending rider info to passenger: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	// Send updates to drivers
	public void sendTripToDriver(TaxiRequest update, String userId) {
		messagingTemplate.convertAndSend("/specific/driver/updates/"+ userId, update);
	}

	// Send status updates to drivers
	public void sendStatusUpdateToDriver(JoinRequestDTO status) {
		messagingTemplate.convertAndSend("/topic/driver/status", status);
	}
}
