package com.example.JoinGoREST.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.JoinGoREST.Model.DTO.ResDriverMatchDTO;
import com.example.JoinGoREST.Model.Entity.Passenger;
import com.example.JoinGoREST.Model.Entity.TaxiDriverStatus;
import com.example.JoinGoREST.Model.Entity.TaxiRequest;
import com.example.JoinGoREST.controllers.WebSocketController;
import com.example.JoinGoREST.repo.TaxiDriverStatusRepo;
import com.example.JoinGoREST.repo.JoinRequestRepo;
import com.example.JoinGoREST.repo.PassengerRepo;
import com.google.gson.Gson;

import jakarta.transaction.Transactional;

@Service
public class driverService implements Idriver {

	@Autowired
	private WebSocketController _socketWithFront;
	@Autowired
	private TaxiDriverStatusRepo _taxidriverrepo;
	@Autowired
	private PassengerRepo _passengerepo;
	@Autowired
	private JoinRequestRepo _joinRequestRepo;
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public void tripMatchCallsSendtoDriver(String matchStringFromMAS) {

		Gson gson = new Gson();
		try {
			ResDriverMatchDTO matchFromMAS = gson.fromJson(matchStringFromMAS, ResDriverMatchDTO.class);
			Thread.sleep(1000);
			_socketWithFront.sendTripToDriver(matchFromMAS.taxiRequest, matchFromMAS.taxiStatust.getDriverid());

			// get from the taxirqeuest
			System.out.println("taxi req id: " + matchFromMAS.taxiRequest.getTaxiReqid() + " -- joinreqid:- "
					+ matchFromMAS.taxiRequest.getJoinReqid());
			List<String> passUId = _joinRequestRepo.getPassUseridfromTaxireqId(matchFromMAS.taxiRequest.getTaxiReqid());

			for (String userid : passUId) {

				Thread.sleep(500);
				System.out.println("the pass user in driver srvice: " + passUId + " :- "
						+ matchFromMAS.taxiRequest.getJoinReqid());
				_socketWithFront.sendRiderToPassenger(matchFromMAS.taxiStatust, userid);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	@Transactional
	public void setDriverStatus(TaxiDriverStatus driverStatus) {
		if (_taxidriverrepo.findByDriverid(driverStatus.getDriverid()).map(driver -> {
			driver.setTaxiStatus(driverStatus.getTaxiStatus());
			driver.setOnService(driverStatus.getOnService());
			driver.setCurrentLat(driverStatus.getCurrentLat());
			driver.setCurrentLon(driverStatus.getCurrentLon());

			return _taxidriverrepo.save(driverStatus);
		}).orElse(null) == null) {
			_taxidriverrepo.save(driverStatus);
		}

		this.notifyJadeMAS(driverStatus);
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