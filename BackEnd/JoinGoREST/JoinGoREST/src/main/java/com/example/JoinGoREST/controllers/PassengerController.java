package com.example.JoinGoREST.controllers;

import java.io.Console;
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

import com.example.JoinGoREST.Model.DTO.JoinReqMsgDTO;
import com.example.JoinGoREST.Model.DTO.JoinRequestDTO;
import com.example.JoinGoREST.Model.DTO.JoinResponseListDTO;
import com.example.JoinGoREST.Model.DTO.ResponsePassengerDTO;
import com.example.JoinGoREST.Model.DTO.ResponsetoFrontDTO;
import com.example.JoinGoREST.Model.Entity.JoinRequest;
import com.example.JoinGoREST.Model.Entity.LongDistanceSegment;
import com.example.JoinGoREST.Model.Entity.MasJoinList;
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

	@PostMapping("/createRideRequest")
	public CompletableFuture<ResponsetoFrontDTO> createRideRequest(@RequestBody JoinRequestDTO joinrequest){
		try { 
 
		System.out.println("hit to createRideRequest:   ");
		return _passenger.createJoinRequest(joinrequest);
		
		
		}
		catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot create passenger"+ ex);
		}
	}

	@PostMapping("/masReponseJoin")
	public String  fromMas(@RequestBody String joinPassengerListJSON) {
		try { 

			System.out.println("Conrtoller: hit from mas: ");  

			return _passenger.joinPassengerInform(joinPassengerListJSON);

		}catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bad match reponse"+ ex);
		}
	}
	
	@PostMapping("/masReponseFarRoute")
	public String  fromMasFarRoute(@RequestBody List<LongDistanceSegment> joinPassengerSegmentsJSON) {
		try { 

			 System.out.println("Conrtoller: hit from mas: ");  

			return _passenger.farRouteSave(joinPassengerSegmentsJSON);

		}catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bad match reponse"+ ex);
		}
	}

	@PostMapping("/msgJoinControl")
	public String  MSGJoinControl(@RequestBody JoinReqMsgDTO msgJoin) {
		try { 

			 System.out.println("front msg :"+ msgJoin.receiverUserId);  

			return _passenger.joinMsgManager(msgJoin);

		}catch(Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bad match reponse"+ ex);
		}
	}
}
