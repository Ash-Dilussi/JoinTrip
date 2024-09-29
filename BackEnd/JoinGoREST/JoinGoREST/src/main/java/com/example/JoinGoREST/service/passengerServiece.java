package com.example.JoinGoREST.service;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.JoinGoREST.Model.DTO.PassengerDTO;
import com.example.JoinGoREST.Model.Entity.Passenger;
import com.example.JoinGoREST.repo.PassengerRepo;
import com.google.gson.Gson;

@Service
public class passengerServiece implements Ipassenger {
	
	
	private PassengerRepo _passengerepo;
	
	@Autowired
	public passengerServiece(PassengerRepo passengerepo) {
		this._passengerepo = passengerepo;
	}
	
	
	public  List<Passenger> getAllPassengers(){
		
		return _passengerepo.getPassengerAll();
		
	}
	
	public Passenger createPassenger(Passenger passenger) {
		passenger.setPasId(generatepasId(passenger.getPasName()));
		try {
			this.notifyJadeMAS(passenger);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return _passengerepo.save(passenger);
	}
	
	
	private void notifyJadeMAS(Passenger registration) throws Exception {
        // Send data to JADE's listener on port 12345 (example port)
        try (Socket socket = new Socket("localhost", 8082)) {
            OutputStream output = socket.getOutputStream();
            Gson gson = new Gson();
            String jsonMessage = gson.toJson(registration);

            // Send the JSON string over the socket
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(jsonMessage);  // Send JSON

            System.out.println("Sent JSON message: " + jsonMessage);
        }
    }
	
	private String generatepasId(String pasname) {
		
		long currentTimeMillis = System.currentTimeMillis();
		
		return pasname +currentTimeMillis;
	}
	

}
