package com.example.JoinGoREST.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ResponsePassengerDTO{

	public String userid; 
	public String username;
	public Integer phone;
	public String town;

}