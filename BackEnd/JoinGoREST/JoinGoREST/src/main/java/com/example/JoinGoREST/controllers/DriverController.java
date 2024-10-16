package com.example.JoinGoREST.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.JoinGoREST.service.Idriver;

@RestController
@RequestMapping(value= "api/v1/driver")
@CrossOrigin
public class DriverController {

	@Autowired
	private Idriver _driver;

	@GetMapping("/masReponseJoin")
	public String respondfromMASaboutReqMatches(@RequestBody String StringfromMAS) {
		try {

			_driver.tripMatchCallsSendtoDriver(StringfromMAS);
			return "all gud";

		}catch(Exception ex) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bad match reponse"+ ex);}
	}









}