package com.example.JoinGoREST.service;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.JoinGoREST.Model.DTO.JoinRequestDTO;
import com.example.JoinGoREST.Model.DTO.JoinResponseListDTO;
import com.example.JoinGoREST.Model.DTO.ResponsePassengerDTO;
import com.example.JoinGoREST.Model.DTO.ResponsetoFrontDTO;
import com.example.JoinGoREST.Model.Entity.JoinRequest;
import com.example.JoinGoREST.Model.Entity.LongDistanceSegment;
import com.example.JoinGoREST.Model.Entity.MasJoinList;
import com.example.JoinGoREST.Model.Entity.Passenger;
import com.example.JoinGoREST.controllers.WebSocketController;
import com.example.JoinGoREST.repo.JoinRequestRepo;
import com.example.JoinGoREST.repo.LongDistanceSegmentsRepo;
import com.example.JoinGoREST.repo.MasJoinListRepo;
import com.example.JoinGoREST.repo.PassengerRepo;
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
System.out.println("schedule time in date"+joinrequest.getScheduleTimeinDate());
			_joinrequestrepo.insertPasReq(joinrequest.joinReqId, joinrequest.userId, joinrequest.desplace_id, joinrequest.startLon, joinrequest.startLat, joinrequest.destLon, joinrequest.destLat, joinrequest.requestStatus, joinrequest.reqVehicletype, joinrequest.getScheduleTimeinDate(), joinrequest.tripType, joinrequest.SegmentDistance);

		
			
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

				List<MasJoinList> joindbresponse = new ArrayList<>();
				List<LongDistanceSegment> longdistanceresponse = new ArrayList<>();
				while (response.getJoinList() == null || response.getJoinList().isEmpty()) {

					//if ((System.currentTimeMillis() - startTime)% CHECK_INTERVAL  == 0) {
					if(registration.tripType == 2) {
						
					joindbresponse = _maslistrepo.matchcall(askingUser.joinReqId);
					
					}else if(registration.tripType == 3) {
						
						longdistanceresponse = _longSegrepo.getsegsbyTripid(askingUser.joinReqId);
					}
					
					
					if(!joindbresponse.isEmpty()) {// && joindbresponse.getAskingReqid().equals(re)
						List<ResponsePassengerDTO> tempJoinList = new ArrayList<>();

						for(MasJoinList item: joindbresponse) {
							for(String ajoin: item.getJoinlistReqid()) {

								Passenger res = _passengerepo.getResponsePassforreqId(ajoin);

								tempJoinList.add(new ResponsePassengerDTO(res.getUserid(),res.getFirstName(),res.getLastName(),res.getPhone(),res.getGender(),res.getTown()));

							}

						}
						response.setJoinList(tempJoinList);
						System.out.println("Received from MAS: " +CHECK_INTERVAL+ response);
					}
					
					if(!longdistanceresponse.isEmpty()) {
						response.setFarRouteSegs(longdistanceresponse);
					}

					//}

					//				     
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

			long currentTime = System.currentTimeMillis() - startTime ;
			long seconds = currentTime / 1000; // Total seconds
			long hours = seconds / 3600; // Total hours
			seconds %= 3600; // Remaining seconds after hours
			long minutes = seconds / 60; // Total minutes
			seconds %= 60; 
			// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

			return  response ;
		});


	}

	private String generatepasId(String pasname) {

		long currentTimeMillis = System.currentTimeMillis();

		return pasname +currentTimeMillis;
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
		// TODO Auto-generated method stub

		_maslistrepo.save(msg);

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


}
