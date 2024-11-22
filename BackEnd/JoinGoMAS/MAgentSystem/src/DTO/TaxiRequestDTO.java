package DTO;

import jade.util.leap.Serializable;

public class TaxiRequestDTO implements Serializable{


	public Integer Id;
	public String taxiReqid;
  
	public double startLat=0;
	public double startLon=0;
	public double destLat=0;
	public double destLon=0;
	public String JoinReqid;
	public String destinationName;
	public int vehicletype; 
	public int tripType=0;
}