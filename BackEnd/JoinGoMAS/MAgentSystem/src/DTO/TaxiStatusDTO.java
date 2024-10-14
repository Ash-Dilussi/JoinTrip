package DTO;


public class TaxiStatusDTO{

	private String Driverid;
	private String Drivername;
	private float currentLon=0;
	private float currentLat=0;
	private float headingLat=0;
	private float headingLon=0;
	private int vehicletype;
	private int taxiStatus;
	private int onService =1;

	public String getDriverid() {
		return Driverid;
	}
	public void setDriverid(String driverid) {
		Driverid = driverid;
	}
	public float getHeadingLat() {
		return headingLat;
	}
	public void setHeadingLat(float headingLat) {
		this.headingLat = headingLat;
	}
	public String getDrivername() {
		return Drivername;
	}
	public void setDrivername(String drivername) {
		Drivername = drivername;
	}
	public float getCurrentLon() {
		return currentLon;
	}
	public void setCurrentLon(float currentLon) {
		this.currentLon = currentLon;
	}
	public float getCurrentLat() {
		return currentLat;
	}
	public void setCurrentLat(float currentLat) {
		this.currentLat = currentLat;
	}
	public float getHeadingLon() {
		return headingLon;
	}
	public void setHeadingLon(float headingLon) {
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

}