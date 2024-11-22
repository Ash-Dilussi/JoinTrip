package com.example.JoinGoREST.repo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
 
import com.example.JoinGoREST.Model.Entity.JoinRequest;
import com.example.JoinGoREST.Model.Entity.Passenger;


public interface JoinRequestRepo extends JpaRepository<JoinRequest , String>{

	@Query(value= "Select * FROM passenger ", nativeQuery = true)
	List<JoinRequest> getPassengerAll();

	@Modifying
	@Query(value = "CALL SP_I_JoinRequest(:joinReqId, :userId, :desplaceId, :startLon, :startLat, :destLon, :destLat, :requestStatus,:vehicletype, :scheduleTime, :tripType, :SegmentDistance )", nativeQuery = true)
	void insertPasReq(String joinReqId, String userId, String desplaceId, double startLon, double startLat, double destLon, double destLat, int requestStatus, int vehicletype, Date scheduleTime, int tripType, int SegmentDistance);


	JoinRequest findByJoinreqid(String joinreqId);
	
	@Query (value="SELECT t1.userid FROM tbl_join_request t1 INNER JOIN tbl_taxi_request t2 ON t1.joinreqid = t2.join_reqid WHERE t2.taxi_reqid = :taxiReqid ", nativeQuery= true)
	List<String> getPassUseridfromTaxireqId(String taxiReqid);
}
