package com.example.JoinGoREST.service;

import com.example.JoinGoREST.Model.DTO.ResDriverMatchDTO;
import com.example.JoinGoREST.Model.Entity.TaxiDriverStatus;

public interface Idriver {

	void tripMatchCallsSendtoDriver(String stringfromMAS);

	void setDriverStatus(TaxiDriverStatus driverStatus);
}