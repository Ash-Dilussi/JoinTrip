package com.example.JoinGoREST.Model.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_TaxiRequest")
public class TaxiRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer Id;
	private String taxiReqid;

	private double startLat = 0;
	private double startLon = 0;
	private double destLat = 0;
	private double destLon = 0;
	private String JoinReqid;
	public String destinationName;
	private int vehicletype;
	private int tripType;
}
