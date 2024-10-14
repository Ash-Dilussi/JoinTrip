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
@Table(name="TBL_Driver_Status")
public class TaxiDriverStatus{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private Integer Id;
	private String Driverid;
	private String Drivername;
	private float currentLon=0;
	private float currentLat=0;
	private float headingLat=0;
	private float headingLon=0;
	private int vehicletype;
	private int taxiStatus;
	private int onService=1;

}