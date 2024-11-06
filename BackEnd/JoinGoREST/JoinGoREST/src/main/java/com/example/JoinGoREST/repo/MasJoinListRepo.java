package com.example.JoinGoREST.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.JoinGoREST.Model.Entity.MasJoinList;
import com.example.JoinGoREST.Model.Entity.Passenger;

public interface MasJoinListRepo extends JpaRepository<MasJoinList,Integer>{

	@Query (value="Select * from TBL_Masjoinlist where asking_reqid = :askingreqid", nativeQuery= true)
	MasJoinList matchcall(String askingreqid);

	Optional<MasJoinList> findByAskingReqid(String askingReqid);

}