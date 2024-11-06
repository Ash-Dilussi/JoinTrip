package com.example.JoinGoREST.repo;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
 
import com.example.JoinGoREST.Model.Entity.TaxiDriverStatus;


public interface TaxiDriverStatusRepo extends JpaRepository<TaxiDriverStatus , Integer>{



	@Query (value="Select Id from TBL_Driver_Status where driverid = :taxiid", nativeQuery= true)
	Integer getDriverbyUserid(String taxiid);
	
	Optional<TaxiDriverStatus> findByDriverid(String driverid);
	
}