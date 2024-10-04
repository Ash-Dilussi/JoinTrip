package com.example.JoinGoREST.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.JoinGoREST.Model.DTO.JoinRequestDTO;
import com.example.JoinGoREST.Model.DTO.JoinResponseListDTO;
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

	private final int TIMEOUT_MILLIS = 10000; // Total timeout duration (20 seconds)
	private final int CHECK_INTERVAL = 2000; // Check every 5 seconds




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
		// Send data to JADE's listener on port 12345 (example port)


		return CompletableFuture.supplyAsync(() ->{

			String response = null;
			try (Socket socket = new Socket("localhost", 8082);
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
				//OutputStream output = socket.getOutputStream();
				Gson gson = new Gson();
				String jsonMessage = gson.toJson(registration);


				// Send the JSON string over the socket
				//PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
				out.println(jsonMessage); 


				long startTime = System.currentTimeMillis();
				while (response == null) {
					// Wait for a response from the MAS (blocking call)
					if (in.ready()) {
						response = in.readLine(); // This will block until a response is received
						System.out.println("Received from MAS: " + response);
					}

					if(System.currentTimeMillis() - startTime > 11000) {
						response = "value assigend after 3sec";
					}

					// Check if timeout has occurred
					if (System.currentTimeMillis() - startTime > TIMEOUT_MILLIS) {
						System.out.println("Timeout waiting for response from MAS.");
						break; // Exit the loop if timeout occurs
					}

					// Sleep for the specified check interval
					//Thread.sleep(CHECK_INTERVAL);
				}// Send JSON

				//response= in.readLine();
				//System.out.println("Received from MAS: " + response);

			}catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		});


	}

	private String generatepasId(String pasname) {

		long currentTimeMillis = System.currentTimeMillis();

		return pasname +currentTimeMillis;
	}


	@Override
	@Async
	public CompletableFuture<List<Passenger>> joinPassengerInform(JoinResponseListDTO joinPassengerListData) {

		List<Passenger> responsePassengerList = new ArrayList<>();

		for(JoinRequestDTO joinreq: joinPassengerListData.getJoinPassengerList() ) {
			System.out.println(joinreq.userId);
			Passenger responsePassenger = _passengerepo.getjoinReponseUserInfo(joinreq.userId);
			System.out.println(responsePassenger.getUsername());
			if (responsePassenger != null) {
				responsePassengerList.add(responsePassenger);
			}
		}

		for(Passenger response: responsePassengerList) {
			System.out.println(response.getUsername());
		}
		return CompletableFuture.completedFuture(responsePassengerList);
	}


	@Override
	public Passenger createPassenger(Passenger passenger) {
		return _passengerepo.save(passenger);

	}


	@Override
	public void joinlistfromMAS(MasJoinList msg) {
		// TODO Auto-generated method stub

		_maslistrepo.save(msg);

	}


}
