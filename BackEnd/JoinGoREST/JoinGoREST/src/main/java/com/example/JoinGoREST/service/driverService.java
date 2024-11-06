package com.example.JoinGoREST.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.JoinGoREST.Model.DTO.DriverMatchResMAS;
import com.example.JoinGoREST.Model.Entity.TaxiDriverStatus;
import com.example.JoinGoREST.Model.Entity.TaxiRequest;
import com.example.JoinGoREST.controllers.WebSocketController;
import com.example.JoinGoREST.repo.TaxiDriverStatusRepo;
import com.google.gson.Gson;

import jakarta.transaction.Transactional;

@Service
public class driverService implements Idriver{

	@Autowired
	private WebSocketController _socketWithFront;
	@Autowired
	private TaxiDriverStatusRepo _taxidriverrepo;
	@Autowired
    private RestTemplate restTemplate;
	

	@Override
	public void tripMatchCallsSendtoDriver(String StringFromMAS) {
		Gson gson = new Gson();
		DriverMatchResMAS tripReqCall = gson.fromJson(StringFromMAS,DriverMatchResMAS.class);
		_socketWithFront.sendTripToDriver(tripReqCall,tripReqCall.driverStatusInfo.getDriverid());


	}

	@Override
	@Transactional
	public void setDriverStatus(TaxiDriverStatus driverStatus) {
		if(_taxidriverrepo.findByDriverid(driverStatus.getDriverid()).map(driver ->{
	driver.setTaxiStatus(driverStatus.getTaxiStatus());
	driver.setOnService(driverStatus.getOnService());
	driver.setCurrentLat(driverStatus.getCurrentLat());
	driver.setCurrentLon(driverStatus.getCurrentLon());
	
	 return _taxidriverrepo.save(driverStatus);
		}).orElse(null) == null) {
		_taxidriverrepo.save(driverStatus);
		}
		
		
	}
	
	private void notifyJadeMAS(TaxiDriverStatus driverStatus) {
		
		String url = "http://localhost:8888/taxiStatusUpdate";
		
		Gson gson = new Gson();
		String jsonMessage = gson.toJson(driverStatus);
		
		
		 ResponseEntity<String> response = restTemplate.postForEntity(url, jsonMessage, String.class);
		 
		   if (response.getStatusCode().is2xxSuccessful()) {
	            System.out.println("Response from JADE: " + response.getBody());
	        } else {
	            System.out.println("Failed to update status: " + response.getStatusCode());
	        }
		   
	        
	}

}