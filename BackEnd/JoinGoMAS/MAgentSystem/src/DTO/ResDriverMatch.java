package DTO;

import jade.util.leap.Serializable;

public class ResDriverMatch implements Serializable{
	
	private TaxiStatusDTO taxiStatust;
	private TaxiRequestDTO taxiRequest;
	
	
	
	 
	
	public TaxiStatusDTO getTaxiStatust() {
		return taxiStatust;
	}
	public void setTaxiStatust(TaxiStatusDTO taxiStatust) {
		this.taxiStatust = taxiStatust;
	}
	public TaxiRequestDTO getTaxiRequest() {
		return taxiRequest;
	}
	public void setTaxiRequest(TaxiRequestDTO taxiRequest) {
		this.taxiRequest = taxiRequest;
	}
}