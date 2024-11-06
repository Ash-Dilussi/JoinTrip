package com.example.JoinGoREST.Model.DTO;

import java.util.List;

import com.example.JoinGoREST.Model.Entity.LongDistanceSegment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsetoFrontDTO {
	
public String joinReqId;
 public List<ResponsePassengerDTO> joinList;
 public List<LongDistanceSegment> farRouteSegs;
 private String other;
 
  
 
}