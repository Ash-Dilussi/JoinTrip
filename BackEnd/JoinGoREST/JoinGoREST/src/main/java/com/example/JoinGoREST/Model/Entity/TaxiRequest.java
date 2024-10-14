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
@Table(name="TBL_TaxiRequest")
public class TaxiRequest{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private Integer Id;
	private String taxiReqid;
	private String Drivername; 
	private float startLat=0;
	private float startLon=0;
	private float destLat=0;
	private float destLon=0;
	private String JoinReqid;
	private int vehicletype; 
}
