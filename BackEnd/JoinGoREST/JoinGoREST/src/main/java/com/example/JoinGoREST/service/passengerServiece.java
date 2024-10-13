package com.example.JoinGoREST.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.JoinGoREST.Model.DTO.JoinPassengerInfo;
import com.example.JoinGoREST.Model.DTO.JoinRequestDTO;
import com.example.JoinGoREST.Model.DTO.JoinResponseListDTO;
import com.example.JoinGoREST.Model.DTO.ResponsePassengerDTO;
import com.example.JoinGoREST.Model.Entity.JoinRequest;
import com.example.JoinGoREST.Model.Entity.MasJoinList;
import com.example.JoinGoREST.Model.Entity.Passenger;
import com.example.JoinGoREST.repo.JoinRequestRepo;
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

	private final int TIMEOUT_MILLIS = 17000; // Total timeout duration (20 seconds)
	private final int CHECK_INTERVAL = 4000; // Check every 5 seconds




	@Override
	public  List<JoinRequest> getAllPassengers(){

		return _joinrequestrepo.getPassengerAll();

	}


	@Override
	@Transactional
	public CompletableFuture<String> createJoinRequest(JoinRequestDTO joinrequest) {
		CompletableFuture<String> jointlsit=null;
		try {
			joinrequest.setJoinReqId(generatepasId(joinrequest.userId));

			_joinrequestrepo.insertPassenger(joinrequest.joinReqId, joinrequest.userId, joinrequest.desplace_id, joinrequest.startLon, joinrequest.startLat, joinrequest.destLon, joinrequest.destLat);

			System.out.println("data saved");
			jointlsit= this.notifyJadeMAS(joinrequest);

		} catch (Exception e) { 
			e.printStackTrace();
		}
		return jointlsit;
		//return _passengerepo.save(passenger);
	}

	
	private CompletableFuture<String> notifyJadeMAS(JoinRequestDTO registration) throws Exception {
		 
	
		
		JoinRequestDTO askingUser = registration;
		return CompletableFuture.supplyAsync(() ->{
			String response = null;
			long startTime = System.currentTimeMillis();
			try (Socket socket = new Socket("localhost", 8082);
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					//BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
							) {
				//OutputStream output = socket.getOutputStream();
				Gson gson = new Gson();
				String jsonMessage = gson.toJson(registration);

    
				out.println(jsonMessage); 
 
				
				while (response == null) {
					 
					//if ((System.currentTimeMillis() - startTime)% CHECK_INTERVAL  == 0) {
			 
						List<MasJoinList> joindbresponse = _maslistrepo.matchcall(askingUser.joinReqId);
						
						if(!joindbresponse.isEmpty()) {// && joindbresponse.getAskingReqid().equals(re)
							List<ResponsePassengerDTO> tempJoinList = new ArrayList<>();
							
							for(MasJoinList item: joindbresponse) {
								for(String ajoin: item.getJoinlistReqid()) {
									
									Passenger res = _passengerepo.getResponsePassforreqId(ajoin);
							 
									tempJoinList.add(new ResponsePassengerDTO(res.getUserid(),res.getUsername(),res.getPhone(),res.getTown()));
							 
								}
									
							}
							response = "got a match "+ registration.joinReqId+ "  ::-> "+tempJoinList;
							System.out.println("Received from MAS: " +CHECK_INTERVAL+ response);
						}
						
					//}

//				     
					if (System.currentTimeMillis() - startTime > TIMEOUT_MILLIS) {
						System.out.println("Timeout waiting for response from MAS.");
						response ="Timeout waiting for response from MAS."; // Exit the loop if timeout occurs
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
		        
			return formattedTime +" ::> "+ response ;
		});


	}

	private String generatepasId(String pasname) {

		long currentTimeMillis = System.currentTimeMillis();

		return pasname +currentTimeMillis;
	}


	@Override
	//@Async
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


}
