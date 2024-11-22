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
import com.example.JoinGoREST.Model.Entity.TaxiRequest;

public interface Ipassenger {

	List<JoinRequest> getAllPassengers();

	String createJoinRequest(JoinRequestDTO joinrequest);

	Passenger createPassenger(Passenger passenger);

	String joinPassengerInform(String joinPassengerListData);

	String farRouteSaveInform(LongDistanceSegment farRouteSegData);

	void joinlistfromMAS(MasJoinList msg);

	String farRouteSave(List<LongDistanceSegment> farRouteSegData);

	String joinMsgManager(JoinReqMsgDTO msgJoin);

	String longDisTaxiReq(TaxiRequest msgLong);

}
