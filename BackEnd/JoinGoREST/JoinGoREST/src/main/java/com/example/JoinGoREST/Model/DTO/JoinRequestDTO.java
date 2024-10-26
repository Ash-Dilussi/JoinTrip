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
	public float startLon =0;
	public float startLat =0;
	public float destLon =0;
	public float destLat =0;
	 
	 
	public int requestStatus =0;
	public int reqVehicletype=0;
	
	public long scheduleTime;
	public int tripType=0;
	public int SegmentDistance=0;
	public RouteDTO longRoute;
	public int userType;
	
	public Passenger userInfo ;
	
	public  Date getScheduleTimeinDate() {
		return new Date(scheduleTime);
	}

}


@Data
@AllArgsConstructor
@NoArgsConstructor
 class Coordinate {
    public double latitude;
    public double longitude;
    }


@Data
@AllArgsConstructor
@NoArgsConstructor
class RouteDTO { 
    public List<Coordinate> routeCoordinates; 
    }