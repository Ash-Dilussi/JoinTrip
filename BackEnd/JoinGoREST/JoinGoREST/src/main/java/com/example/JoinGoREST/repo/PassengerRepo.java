package com.example.JoinGoREST.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.JoinGoREST.Model.Entity.Passenger;


public interface PassengerRepo extends JpaRepository<Passenger , String>{

    @Query(value= "Select * FROM passenger ", nativeQuery = true)
    List<Passenger> getPassengerAll();
    
    
    
	
}
