package com.example.JoinGoREST.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class JoinRequestDTO {

	public String Id;
	public String userId; 
	public String joinReqId;
	public String desplace_id;
	public float startLon =0;
	public float startLat =0;
	public float destLon =0;
	public float destLat =0;
	public int isScheduled = 0;
	public String deleteTime = null; 
	public int requestStatus =0;
	public int reqVehicletype=0;

}
