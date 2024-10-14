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
@Table(name="TBL_TaxiDriver")
public class TaxiDriver{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private Integer Id;
	private String Driverid;
	private String Drivername; 
	private float homeLat=0;
	private float homeLon=0;
	private int vehicletype; 
}
