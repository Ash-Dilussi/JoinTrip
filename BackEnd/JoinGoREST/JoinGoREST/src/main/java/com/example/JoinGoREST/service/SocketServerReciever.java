package com.example.JoinGoREST.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.JoinGoREST.Model.Entity.MasJoinList;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class SocketServerReciever {

	private ServerSocket serverSocket;
	private final int PORT = 8082; // Use the same port as JADE

	@Autowired
	private Ipassenger _passenger;


	@PostConstruct
	public void startServer() {
		new Thread(() -> {
			try {
				serverSocket = new ServerSocket(PORT);
				System.out.println("Socket server started on port " + PORT);

				while (true) {
					Socket clientSocket = serverSocket.accept();
					System.out.println("Client connected: " + clientSocket);
					handleClientConnection(clientSocket);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	private void handleClientConnection(Socket clientSocket) {
		new Thread(() -> {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
				MasJoinList masmsg  = new MasJoinList();
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					System.out.println("Received from JADE: " + inputLine);
					// Process the inputLine as needed
					masmsg.setList(inputLine);
					_passenger.joinlistfromMAS(masmsg);
					// Optionally, send a response back to JADE
					//String response = "Echo: " + inputLine;
					//out.println(response);
					System.out.println("From MAS: "+ inputLine);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@PreDestroy
	public void stopServer() {
		try {
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
				System.out.println("Socket server stopped.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
