package com.example.JoinGoREST.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.JoinGoREST.Model.DTO.ResDriverMatchDTO;
import com.example.JoinGoREST.Model.Entity.TaxiDriverStatus;
import com.example.JoinGoREST.service.Idriver;

@RestController
@RequestMapping(value= "api/v1/driver")
@CrossOrigin
public class DriverController {

	@Autowired
	private Idriver _driver;

	@PostMapping("/masReponseTaxiMatch")
	public String respondfromMASaboutReqMatches(@RequestBody String StringfromMAS) {
		try {

		System.out.println("hit to driver from MAS"+StringfromMAS);
			_driver.tripMatchCallsSendtoDriver(StringfromMAS);
			return "all gud";

		}catch(Exception ex) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bad match reponse"+ ex);}
	}
	
	

	@PostMapping("/driverStatus")
	public String setDriverStatus(@RequestBody TaxiDriverStatus driverStatus) {
		try {

			_driver.setDriverStatus(driverStatus);
			return "starus updated";

		}catch(Exception ex) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bad match reponse"+ ex);}
	}







}