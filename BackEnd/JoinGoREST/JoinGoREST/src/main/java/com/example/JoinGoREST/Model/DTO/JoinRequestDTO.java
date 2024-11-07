package com.example.JoinGoREST.Model.DTO;


import java.util.Date;
import java.util.List;

import com.example.JoinGoREST.Model.Entity.Passenger;

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
	public double startLon =0;
	public double startLat =0;
	public double destLon =0;
	public double destLat =0;
	 
	 
	public int requestStatus =0;
	public int reqVehicletype=0;
	
	public long scheduleTime;
	public int tripType=0;
	public int SegmentDistance=0;
	public List<Coordinate> longRoute;
	public int userType;
	
	public Passenger userInfo ;
	
	public String taxiReqid;
	
	public  Date getScheduleTimeinDate() {
		System.out.println(scheduleTime);
		
		if(scheduleTime == 0) {
			return new Date();
		}else {
		return new Date(scheduleTime);
		}
	}

}




 