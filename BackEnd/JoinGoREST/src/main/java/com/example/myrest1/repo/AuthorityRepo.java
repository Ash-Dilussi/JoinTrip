package com.example.myrest1.repo;


import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.myrest1.entity.Authority;



public interface AuthorityRepo extends JpaRepository<Authority ,Integer>{

    // @Query(value= "Select * FROM AUTHORITY", nativeQuery= true)
    // Authority getAuthorities();

    @Query(value= "Select * FROM AUTHORITY WHERE AID= ?1", nativeQuery = true)
    Authority getAuthorityByAuthID(Integer aId);

     @Query(value= "Select * FROM AUTHORITY WHERE aclearance liek :clearance", nativeQuery = true)
     List<Authority> getAuthorityByAuthPOST(@Param("clearance") String clearance);

     @Query(value= "Select * FROM AUTHORITY WHERE aclearance= ?1", nativeQuery = true)
     List<Authority> getAuthorityByAuthPOSTURL(String aclearance);

     @Query(value= "Select * FROM AUTHORITY WHERE emps in :nums", nativeQuery = true)
     List<Authority> getAuthorityByAuthemps(@Param("nums") List<Integer> nums);


    @Query(value = "select * FROM AUTHORITY WHERE aname=?1 OR emps=?2 AND aclearance= ?3", nativeQuery= true)
     List<Authority> getsearch2( String names, Integer nums, String clearance);
    // Iterable<T>


}
