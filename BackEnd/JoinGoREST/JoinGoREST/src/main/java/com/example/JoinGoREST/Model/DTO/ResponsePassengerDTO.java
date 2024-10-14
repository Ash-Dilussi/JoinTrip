package com.example.JoinGoREST.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ResponsePassengerDTO{

	public String Id; 
	public String Username;
	public Integer Phone;
	public String Town;

}