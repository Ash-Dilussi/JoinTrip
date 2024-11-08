package com.example.JoinGoREST.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.JoinGoREST.Model.DTO.JoinReqMsgDTO;
import com.example.JoinGoREST.Model.DTO.JoinRequestDTO;
import com.example.JoinGoREST.Model.DTO.JoinResponseListDTO;
import com.example.JoinGoREST.Model.DTO.ResponsePassengerDTO;
import com.example.JoinGoREST.Model.DTO.ResponsetoFrontDTO;
import com.example.JoinGoREST.Model.Entity.JoinRequest;
import com.example.JoinGoREST.Model.Entity.LongDistanceSegment;
import com.example.JoinGoREST.Model.Entity.MasJoinList;
import com.example.JoinGoREST.Model.Entity.Passenger;

public interface Ipassenger {

	List<JoinRequest> getAllPassengers();
	CompletableFuture<ResponsetoFrontDTO> createJoinRequest(JoinRequestDTO joinrequest);
	Passenger createPassenger(Passenger passenger);
	String joinPassengerInform(String joinPassengerListData);
	String farRouteSaveInform(LongDistanceSegment farRouteSegData);
	void joinlistfromMAS(MasJoinList msg);
	String farRouteSave(List<LongDistanceSegment> farRouteSegData);
	String joinMsgManager(JoinReqMsgDTO msgJoin);


}
