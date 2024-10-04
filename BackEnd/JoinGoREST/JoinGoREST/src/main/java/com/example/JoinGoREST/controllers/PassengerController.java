package com.example.JoinGoREST.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.example.JoinGoREST.Model.DTO.JoinRequestDTO;
import com.example.JoinGoREST.Model.DTO.JoinResponseListDTO;
import com.example.JoinGoREST.Model.Entity.JoinRequest;
import com.example.JoinGoREST.Model.Entity.Passenger;
import com.example.JoinGoREST.service.Ipassenger;

@RestController
@RequestMapping(value= "api/v1/passenger")
@CrossOrigin
public class PassengerController {

	@Autowired
	private Ipassenger _passenger;


	@GetMapping("/getallpassengers")
	public List<JoinRequest> getAll(){
		try {
			return _passenger.getAllPassengers();

		}catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot retrieve  passengers"+ ex);

		}

	}

	@PostMapping("/createPassUser")
	public Passenger createPassengerUser(@RequestBody Passenger user) {
		try {


			return _passenger.createPassenger(user);

		}catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot retrieve  passengers"+ ex);

		}

	}

	@GetMapping("/createJoinRequest")
	public CompletableFuture<String> createJoinRequest(@RequestBody JoinRequestDTO joinrequest){
		try {RestTemplate restTemplate = new RestTemplate();

		//CompletableFuture<List<Passenger>> backendBResponse = restTemplate.postForObject();
		return _passenger.createJoinRequest(joinrequest);
		}
		catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot create passenger"+ ex);
		}
	}

	@PostMapping("/masReponseJoin")
	public CompletableFuture<List<Passenger>>  fromMas(@RequestBody JoinResponseListDTO joinPassengerListData) {
		try {


			System.out.print("hit from mas"); 
			System.out.println (joinPassengerListData.getCurrentPassenger().joinReqId);
			for(JoinRequestDTO joinreq: joinPassengerListData.getJoinPassengerList() ) {
				System.out.println (joinreq.joinReqId+" : "+ joinreq.desplace_id);
			}

			return _passenger.joinPassengerInform(joinPassengerListData);

		}catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bad match reponse"+ ex);
		}
	}

}
