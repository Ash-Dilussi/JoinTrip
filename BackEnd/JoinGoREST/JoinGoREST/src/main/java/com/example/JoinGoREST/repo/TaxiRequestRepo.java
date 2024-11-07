package com.example.JoinGoREST.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.JoinGoREST.Model.Entity.TaxiDriverStatus;
import com.example.JoinGoREST.Model.Entity.TaxiRequest;

public interface TaxiRequestRepo extends JpaRepository<TaxiRequest , Integer>{
	
	
}