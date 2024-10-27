package com.example.JoinGoREST.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.JoinGoREST.Model.DTO.JoinPassengerInfo;
import com.example.JoinGoREST.Model.DTO.ResponsePassengerDTO;
import com.example.JoinGoREST.Model.Entity.Passenger;


public interface PassengerRepo extends JpaRepository<Passenger , Integer>{



	@Query (value="Select Id from tbl_passenger where userid = :joinuserid", nativeQuery= true)
	Integer getPassengerbyUserid(String joinuserid);
	
	
	@Query (value="SELECT t1.* FROM tbl_passenger t1 INNER JOIN tbl_join_request t2 ON t1.userid = t2.userid WHERE t2.joinreqid = :joinreqId ", nativeQuery= true)
	Passenger getResponsePassforreqId(String joinreqId);
	
	
	
//	@Query (value="SELECT t1.userid , t1.username, t1.phone, t1.town  FROM tbl_passenger t1 INNER JOIN tbl_join_request t2 ON t1.userid = t2.userid WHERE t2.joinreqid = :joinreqId ", nativeQuery= true)
//	JoinPassengerInfo getResponsePassforreqId(String joinreqId);

 
}