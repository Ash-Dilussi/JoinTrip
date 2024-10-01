package com.example.JoinGoREST.Model.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class ReturnPasstoSBDTO {


	private PassengerDTO currentPassenger;

	private List<PassengerDTO> joinPassengerList = new ArrayList<>();
}