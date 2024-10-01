package com.example.JoinGoREST.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PassengerDTO {

	private String pasName;
	private String pasId;
	private String desplace_id;
	private float startLon;
	private float startLat;
	private float destLon;
	private float destLat;

}
