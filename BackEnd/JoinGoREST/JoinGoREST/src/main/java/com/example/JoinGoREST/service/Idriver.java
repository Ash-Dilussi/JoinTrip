package com.example.JoinGoREST.service;

import com.example.JoinGoREST.Model.Entity.TaxiDriverStatus;

public interface Idriver {

	void tripMatchCallsSendtoDriver(String StringFromMAS);

	void setDriverStatus(TaxiDriverStatus driverStatus);
}