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
	@Query(value = "CALL SP_I_JoinRequest(:joinReqId, :userId, :desplaceId, :startLon, :startLat, :destLon, :destLat, :isScheduled, :deleteTime,:requestStatus,:vehicletype)", nativeQuery = true)
	void insertPassenger(String joinReqId, String userId, String desplaceId, float startLon, float startLat, float destLon, float destLat,int isScheduled, String deleteTime, int requestStatus, int vehicletype);


}
