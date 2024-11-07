package com.example.JoinGoREST.service;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.example.JoinGoREST.Model.Enum.JoinReqStatus;
import com.example.JoinGoREST.controllers.WebSocketController;
import com.example.JoinGoREST.repo.JoinRequestRepo;
import com.example.JoinGoREST.repo.LongDistanceSegmentsRepo;
import com.example.JoinGoREST.repo.MasJoinListRepo;
import com.example.JoinGoREST.repo.PassengerRepo;
import com.example.JoinGoREST.repo.TaxiRequestRepo;
import com.google.gson.Gson;




@Service
public class passengerServiece implements Ipassenger {

	@Autowired
	private   JoinRequestRepo _joinrequestrepo;
	@Autowired
	private   PassengerRepo _passengerepo;
	@Autowired
	private MasJoinListRepo _maslistrepo; 
	@Autowired
	private LongDistanceSegmentsRepo _longSegrepo;
	@Autowired
	private TaxiRequestRepo _taxiRequestRepo;
	@Autowired
	private WebSocketController _webSocketController;

	private final int TIMEOUT_MILLIS = 19000; 
	private final int CHECK_INTERVAL = 6000; // Check every x seconds




	@Override
	public  List<JoinRequest> getAllPassengers(){

		return _joinrequestrepo.getPassengerAll();

	}


	@Override
	@Transactional
	public CompletableFuture<ResponsetoFrontDTO> createJoinRequest(JoinRequestDTO joinrequest) {
		CompletableFuture<ResponsetoFrontDTO> jointlsit=null;
		try {
			joinrequest.setJoinReqId(generatepasId(joinrequest.userId));
			 System.out.println(joinrequest);
			if(joinrequest.userType ==2 ) {
				
				if(_passengerepo.getPassengerbyUserid(joinrequest.userInfo.getUserid()) == null) {
					
					
					Passenger guestPassenger = new Passenger(
							0,
							joinrequest.userInfo.getUserid(),
							joinrequest.userType,
							joinrequest.userInfo.getFirstName(),
							joinrequest.userInfo.getLastName(),
							joinrequest.userInfo.getAddressline1(),
							joinrequest.userInfo.getAddressline2(),
							joinrequest.userInfo.getTown(),
							joinrequest.userInfo.getEmail(),
							joinrequest.userInfo.getGender(),
							joinrequest.userInfo.getNic(),
							joinrequest.userInfo.getPhone());
					
					this.createPassenger(guestPassenger);
					
				}
				
				 
			}
 
			_joinrequestrepo.insertPasReq(joinrequest.joinReqId, joinrequest.userId, joinrequest.desplace_id, (float)joinrequest.startLon, (float)joinrequest.startLat, (float)joinrequest.destLon, (float)joinrequest.destLat, joinrequest.requestStatus, joinrequest.reqVehicletype, joinrequest.getScheduleTimeinDate(), joinrequest.tripType, joinrequest.SegmentDistance);

		if(joinrequest.tripType == 1) {
			joinrequest.setTaxiReqid(generateTaxiReqId(joinrequest.userId,(float)joinrequest.destLat));
			TaxiRequest taxiReq  =new TaxiRequest(0,joinrequest.taxiReqid,joinrequest.startLat,joinrequest.startLon,joinrequest.destLat,joinrequest.destLon,joinrequest.joinReqId,joinrequest.desplace_id,joinrequest.reqVehicletype);
			_taxiRequestRepo.save(taxiReq);
			
		}
			
			jointlsit= this.notifyJadeMAS(joinrequest);

		} catch (Exception e) { 
			e.printStackTrace();
		}
		return jointlsit; 
	}


	private CompletableFuture<ResponsetoFrontDTO> notifyJadeMAS(JoinRequestDTO registration) throws Exception {

 
		JoinRequestDTO askingUser = registration;
		return CompletableFuture.supplyAsync(() ->{
			ResponsetoFrontDTO response = new ResponsetoFrontDTO();
			long startTime = System.currentTimeMillis();
			try (Socket socket = new Socket("localhost", 8070);
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					//BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
					) {
				//OutputStream output = socket.getOutputStream();
				Gson gson = new Gson();
				String jsonMessage = gson.toJson(registration);


				out.println(jsonMessage); 

				MasJoinList joindbresponse = new MasJoinList();
				List<LongDistanceSegment> longdistanceresponse = new ArrayList<>();
				
				
				while (response.getJoinList() == null || response.getJoinList().isEmpty()) {

					//if ((System.currentTimeMillis() - startTime)% CHECK_INTERVAL  == 0) {
					if(registration.tripType == 1) {
						return new ResponsetoFrontDTO();
					}
					else if(registration.tripType == 2) {
						
					joindbresponse = _maslistrepo.matchcall(askingUser.joinReqId);
					
					}else if(registration.tripType == 3) {
						
						longdistanceresponse = _longSegrepo.getsegsbyTripid(askingUser.joinReqId);
					}
					
					
					if(joindbresponse != null) {
						List<ResponsePassengerDTO> tempJoinList = new ArrayList<>();
						List<Passenger> res= new ArrayList<>();
				
							for(String ajoin: joindbresponse.getJoinlistReqid()) {

								res = _passengerepo.getPassforreqId(ajoin);
								for(Passenger user: res) {

								tempJoinList.add(new ResponsePassengerDTO(user.getUserid(),user.getFirstName(),user.getLastName(),user.getPhone(),user.getGender(),user.getTown(),ajoin));
								}
							}

						response.setJoinReqId(askingUser.joinReqId);
						response.setJoinList(tempJoinList);
						joinMsgTimer(askingUser.joinReqId,1);
						
						System.out.println("Received from MAS: " +CHECK_INTERVAL+ response);
					}
					
					if(!longdistanceresponse.isEmpty()) {
						response.setFarRouteSegs(longdistanceresponse);
					}

					//}

								     
					if (System.currentTimeMillis() - startTime > TIMEOUT_MILLIS) {
						System.out.println("Timeout waiting for response from MAS.");
						return  response ;
						//response ="Timeout waiting for response from MAS."; // Exit the loop if timeout occurs
					}

					// Sleep for the specified check interval
					
					Thread.sleep(CHECK_INTERVAL);
				}
				


			}catch (Exception e) {
				e.printStackTrace();
			}

			return  response ;
		});


	}

	private String generatepasId(String pasname) {

		long currentTimeMillis = System.currentTimeMillis();

		return pasname +currentTimeMillis;
	}
	
	private String generateTaxiReqId(String pasname, float des) {
		
		return pasname +"to"+des;
	}


	@Override 
	public String joinPassengerInform(String jsonfromMAS) {
		Gson gson = new Gson(); 
		MasJoinList masmsg  = new MasJoinList(); 
		JoinResponseListDTO joinPassengerListData = gson.fromJson(jsonfromMAS, JoinResponseListDTO.class);
		masmsg.setAskingReqid(joinPassengerListData.getCurrentPassenger().joinReqId);
		List<String> tempList = new ArrayList<String>();
		for(JoinRequestDTO joinreq: joinPassengerListData.getJoinPassengerList() ) {
			tempList.add(joinreq.joinReqId);
		}
		masmsg.setJoinlistReqid(tempList);


		System.out.println("Entering to saving the req");
		joinlistfromMAS(masmsg);
		return "Received";
	}


	@Override
	public Passenger createPassenger(Passenger passenger) {
		return _passengerepo.save(passenger);

	}


	@Override
	@Transactional
	public void joinlistfromMAS(MasJoinList msg) {
	 

		_maslistrepo.findByAskingReqid(msg.getAskingReqid()).map(jlist ->{
			jlist.setJoinlistReqid(msg.getJoinlistReqid());
			return _maslistrepo.save(msg);
		}).orElse( _maslistrepo.save(msg));

	}


//	@Override
//	public String farRouteSaveInform(String farRouteSegDataMas) {
//
//		
//		Gson gson = new Gson(); 
//		  
//		List<LongDistanceSegment> FarSegmentData = gson.fromJson(farRouteSegDataMas, LongDistanceSegment.class);
//		 
//
//		System.out.println("Entering to saving the segments");
//		farRouteSave(FarSegmentData);
//		return "Received";
//		 
//	}
//	
	
	@Override
	@Transactional
	public String farRouteSave(List<LongDistanceSegment> farRouteSegData) {

		for(LongDistanceSegment item: farRouteSegData) {
		_longSegrepo.save(item);
		}
		return "Segments revied";
	}


	@Override
	public String farRouteSaveInform(LongDistanceSegment farRouteSegData) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String joinMsgManager(JoinReqMsgDTO msgJoin) {
		 System.out.println(msgJoin.reqStatus+"   "+ msgJoin.receiverUserId);
		switch(msgJoin.reqStatus) {
		case Reqeust:
			_webSocketController.sendRideRequestToPassenger(msgJoin, msgJoin.receiverUserId);
			break;
		case Accept:
			_webSocketController.sendRideRequestToPassenger(msgJoin, msgJoin.senderUserId);
			
			//genreate taxi request
			
			joinMsgTimer(msgJoin.senderJoinReqId,0);
			joinMsgTimer(msgJoin.reveiverJoinReqId,0);
			break;
		case Decline:
			_webSocketController.sendRideRequestToPassenger(msgJoin, msgJoin.senderUserId);
			break;
		default:
			break;
		}
		return "msg processed";
	}
	
	@Transactional
	private String joinMsgTimer(String JoinTripReqId, int isNewMsg) {
		JoinReqMsgDTO joinmsgupdate = new JoinReqMsgDTO();
		
		if(isNewMsg == 0) {
		MasJoinList joindbresponse = _maslistrepo.matchcall(JoinTripReqId);
		
				
		if(!joindbresponse.getJoinlistReqid().isEmpty()) {
			
		
			joinmsgupdate.senderJoinReqId = JoinTripReqId;
			
			joinmsgupdate.reqStatus = JoinReqStatus.Accept;
			List<Passenger> res= new ArrayList<>();
					
			for(String ajoin: joindbresponse.getJoinlistReqid()) {
				
				 res = _passengerepo.getPassforreqId(ajoin);
				for(Passenger user: res) {
				_webSocketController.sendRideRequestToPassenger(joinmsgupdate, user.getUserid());
				}
			}
		}
		}
		
		return null;
	}


}
