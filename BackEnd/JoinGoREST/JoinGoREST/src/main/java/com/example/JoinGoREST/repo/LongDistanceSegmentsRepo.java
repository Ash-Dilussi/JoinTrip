package com.example.JoinGoREST.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.JoinGoREST.Model.Entity.LongDistanceSegment;
import com.example.JoinGoREST.Model.Entity.MasJoinList;

public interface LongDistanceSegmentsRepo extends JpaRepository<LongDistanceSegment,Integer>{
	
	
	@Query (value="Select * from TBL_Farroute_Segment where asking_reqid = :askingreqid", nativeQuery= true)
	List<LongDistanceSegment> getsegsbyTripid(String askingreqid);

	
	
}