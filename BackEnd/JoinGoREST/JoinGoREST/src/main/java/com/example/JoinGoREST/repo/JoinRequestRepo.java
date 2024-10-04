package com.example.JoinGoREST.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.JoinGoREST.Model.Entity.JoinRequest;


public interface JoinRequestRepo extends JpaRepository<JoinRequest , String>{

	@Query(value= "Select * FROM passenger ", nativeQuery = true)
	List<JoinRequest> getPassengerAll();

	@Modifying
	@Query(value = "CALL SP_I_JoinRequest(:joinReqId, :pasName, :desplaceId, :startLon, :startLat, :destLon, :destLat)", nativeQuery = true)
	void insertPassenger(String joinReqId, String pasName, String desplaceId, float startLon, float startLat, float destLon, float destLat);


}
