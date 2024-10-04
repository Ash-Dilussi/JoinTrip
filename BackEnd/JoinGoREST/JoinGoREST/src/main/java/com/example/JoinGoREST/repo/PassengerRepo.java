package com.example.JoinGoREST.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.JoinGoREST.Model.Entity.Passenger;


public interface PassengerRepo extends JpaRepository<Passenger , Integer>{



	@Query (value="Select * from tbl_passenger where userid = :joinuserid", nativeQuery= true)
	Passenger getjoinReponseUserInfo(String joinuserid);


}