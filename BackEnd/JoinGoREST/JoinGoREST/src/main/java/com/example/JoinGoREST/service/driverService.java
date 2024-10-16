package com.example.JoinGoREST.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.JoinGoREST.Model.Entity.TaxiRequest;
import com.example.JoinGoREST.controllers.WebSocketController;
import com.google.gson.Gson;

@Service
public class driverService implements Idriver{

	@Autowired
	private WebSocketController _socketWithFront;

	@Override
	public void tripMatchCallsSendtoDriver(String StringFromMAS) {
		Gson gson = new Gson();
		TaxiRequest tripReqCall = gson.fromJson(StringFromMAS,TaxiRequest.class);
		_socketWithFront.sendUpdateToDriver(tripReqCall);


	}

}