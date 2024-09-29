package com.example.JoinGoREST.Model.Entity;


import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Passenger {

	private String pasName;
	@Id
	private String pasId;
	private float startLon =0;
	private float startLat =0;
	private float destLon =0;
	private float destLat =0;

	
	
	
	
}
