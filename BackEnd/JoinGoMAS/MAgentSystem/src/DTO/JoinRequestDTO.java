package DTO;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import jade.util.leap.Serializable;

public class JoinRequestDTO implements Serializable {

	//private String pasName;
	private String userId;
	private String joinReqId;
	private double startLon; // Use double for decimal values
	private double startLat; // Use double for decimal values
	private double destLon;  // Use double for decimal values
	private double destLat;
	private String desplace_id;
	private int isScheduled = 0;
	private String deleteTime = null; 
	private int isRequestDone =0;
 

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
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		 Date ScheduleTime = dateFormat.parse(deleteTime);
		return ScheduleTime;
	}

	public void setDelelteTime(String delelteTime) {
		this.deleteTime = delelteTime;
	}

	public int getIsScheduled() {
		return isScheduled;
	}

	public void setIsScheduled(int isScheduled) {
		this.isScheduled = isScheduled;
	}

	public int getIsRequestDone() {
		return isRequestDone;
	}

	public void setIsRequestDone(int isRequestDone) {
		this.isRequestDone = isRequestDone;
	}


}
