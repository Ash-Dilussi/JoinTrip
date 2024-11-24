package DTO;

import jade.util.leap.Serializable;

public class TaxiStatusDTO implements Serializable{

	private String driverid;
	private String drivername;
	private double currentLon=0;
	private double currentLat=0;
	private double headingLat=0;
	private double headingLon=0;
	private int vehicletype;
	private int taxiStatus;
	private int onService =1;

	private String phone;
	private String nic;
	
	
	
	public String getDriverid() {
		return driverid;
	}
	public void setDriverid(String driverid) {
		driverid = driverid;
	}
	public double getHeadingLat() {
		return headingLat;
	}
	public void setHeadingLat(double headingLat) {
		this.headingLat = headingLat;
	}
	public String getDrivername() {
		return drivername;
	}
	public void setDrivername(String drivername) {
		drivername = drivername;
	}
	public double getCurrentLon() {
		return currentLon;
	}
	public void setCurrentLon(double currentLon) {
		this.currentLon = currentLon;
	}
	public double getCurrentLat() {
		return currentLat;
	}
	public void setCurrentLat(double currentLat) {
		this.currentLat = currentLat;
	}
	public double getHeadingLon() {
		return headingLon;
	}
	public void setHeadingLon(double headingLon) {
		this.headingLon = headingLon;
	}
	public int getVehicletype() {
		return vehicletype;
	}
	public void setVehicletype(int vehicletype) {
		this.vehicletype = vehicletype;
	}
	public int getTaxiStatus() {
		return taxiStatus;
	}
	public void setTaxiStatus(int taxiStatus) {
		this.taxiStatus = taxiStatus;
	}
	public int getOnService() {
		return onService;
	}
	public void setOnService(int onService) {
		this.onService = onService;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNic() {
		return nic;
	}
	public void setNic(String nic) {
		this.nic = nic;
	}

}