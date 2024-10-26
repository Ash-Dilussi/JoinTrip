package com.example.JoinGoREST.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class ResponsePassengerDTO{

	public String userid; 
	public String firstName;
	public String lastName;
	public String phone;
	public char gender;
	public String town;

}