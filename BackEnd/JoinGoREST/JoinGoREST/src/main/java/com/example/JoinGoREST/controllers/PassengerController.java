package com.example.JoinGoREST.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.JoinGoREST.Model.DTO.ReturnPasstoSBDTO;
import com.example.JoinGoREST.Model.Entity.Passenger;
import com.example.JoinGoREST.service.Ipassenger;

@RestController
@RequestMapping(value= "api/v1/passenger")
@CrossOrigin
public class PassengerController {

	@Autowired
	private Ipassenger _passenger;


	@GetMapping("/getallpassengers")
	public List<Passenger> getAll(){
		try {
			return _passenger.getAllPassengers();

		}catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot retrieve  passengers"+ ex);

		}

	}

	@PostMapping("/createpassenger")
	public Passenger createPassenger(@RequestBody Passenger passenger){
		try {
			return _passenger.createPassenger(passenger);
		}
		catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot create passenger"+ ex);
		}
	}

	@PostMapping("/fromMAS")
	public void fromMas(@RequestBody ReturnPasstoSBDTO joinPassengerListData) {
		try {
			System.out.print("hit from mas"); 
			System.out.println (joinPassengerListData);
			System.out.println (joinPassengerListData.getJoinPassengerList());

		}catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot create passenger"+ ex);
		}
	}

}
