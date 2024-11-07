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
	private String driverid;
	private String Drivername;
	private double currentLon=0;
	private double currentLat=0;
	private double headingLat=0;
	private double headingLon=0; 
	private int vehicletype;
	private int taxiStatus;
	private int onService=1;
	
	private String town;
	private String phone;
	private String nic;

}