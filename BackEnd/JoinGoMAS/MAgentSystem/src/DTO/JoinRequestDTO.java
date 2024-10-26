package DTO;
import java.util.Date;
import java.util.List;



import java.text.ParseException;
import java.text.SimpleDateFormat;

import jade.util.leap.Serializable;

public class JoinRequestDTO implements Serializable {

	//private String pasName;
	private String userId;
	private String joinReqId;
	private double startLon; 
	private double startLat; 
	private double destLon;  
	private double destLat;
	private String desplace_id;
	
	private long scheduleTime ; 
	private int isRequestDone =0;
	
	private int tripType=0;
	private int SegmentDistance=0;
	private RouteDTO longRoute;
	private int userType;
	
 

	public String getJoinReqId() {
		return joinReqId;
	}

	public void setJoinReqId(String pasId) {
		this.joinReqId = pasId;
	}

	public double getStartLon() {
		return startLon;
	}

	public void setStartLon(double startLon) {
		this.startLon = startLon;
	}

	public double getStartLat() {
		return startLat;
	}

	public void setStartLat(double startLat) {
		this.startLat = startLat;
	}

	public double getDestLon() {
		return destLon;
	}

	public void setDestLon(double destLon) {
		this.destLon = destLon;
	}

	public double getDestLat() {
		return destLat;
	}

	public void setDestLat(double destLat) {
		this.destLat = destLat;
	}

	public String getDesplace_id() {
		return desplace_id;
	}

	public void setDesplace_id(String desplace_id) {
		this.desplace_id = desplace_id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getDelelteTime() throws ParseException {
	    //System.out.println("this is del time:"+scheduleTime);
		 //SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy'T'HH:mm:ss");
		// Date ScheduleTime = dateFormat.parse(scheduleTime);
		 Date ScheduleTime = new Date(scheduleTime);
		 return ScheduleTime;
	}

	public void setDelelteTime(long delelteTime) {
		this.scheduleTime = delelteTime;
	}

 

	public int getIsRequestDone() {
		return isRequestDone;
	}

	public void setIsRequestDone(int isRequestDone) {
		this.isRequestDone = isRequestDone;
	}

	public int getTripType() {
		return tripType;
	}

	public void setTripType(int tripType) {
		this.tripType = tripType;
	}

	public int getSegmentDistance() {
		return SegmentDistance;
	}

	public void setSegmentDistance(int segmentDistance) {
		SegmentDistance = segmentDistance;
	}

	public RouteDTO getLongRoute() {
		return longRoute;
	}

	public void setLongRoute(RouteDTO longRoute) {
		this.longRoute = longRoute;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}


}




class Coordinate {
    public double latitude;
    public double longitude;
    }

class RouteDTO { 
    public List<Coordinate> routeCoordinates; 
    }
