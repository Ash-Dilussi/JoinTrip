package com.example.JoinGoREST.service;

import java.util.List;

import com.example.JoinGoREST.Model.Entity.Passenger;

public interface Ipassenger {
	
	List<Passenger> getAllPassengers();
	Passenger createPassenger(Passenger passenger);
	

}
