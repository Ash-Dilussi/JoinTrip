package com.example.JoinGoREST.Model.DTO;

import com.example.JoinGoREST.Model.Entity.Passenger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PassengerDTO {
	
	private String pasName;
	private String padId;
	private float startLon;
	private float startLat;
	private float destLon;
	private float destLat;

}
