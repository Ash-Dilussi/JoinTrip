package com.example.JoinGoREST.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.JoinGoREST.Model.Entity.JoinRequest;


public interface JoinRequestRepo extends JpaRepository<JoinRequest , String>{

	@Query(value= "Select * FROM passenger ", nativeQuery = true)
	List<JoinRequest> getPassengerAll();

	@Modifying
	@Query(value = "CALL SP_I_JoinRequest(:joinReqId, :userId, :desplaceId, :startLon, :startLat, :destLon, :destLat, :requestStatus,:vehicletype, :scheduleTime, :tripType, :SegmentDistance )", nativeQuery = true)
	void insertPasReq(String joinReqId, String userId, String desplaceId, float startLon, float startLat, float destLon, float destLat, int requestStatus, int vehicletype, Date scheduleTime, int tripType, int SegmentDistance);


}
