import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class JadeSBSocket extends Agent {
	private List<Socket> clientSockets = new ArrayList<>();

	@Override
	protected void setup() {
		System.out.println("JadeSBSocket started");

		new Thread(() -> {
			try (ServerSocket serverSocket = new ServerSocket(8082)) {
				while (true) {
					try {
						Socket clientSocket = serverSocket.accept();
						clientSockets.add(clientSocket);
						System.out.println("Client connected: " + clientSocket);

						// Start a new thread to handle this client's connection
						new Thread(() -> handleClient(clientSocket)).start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();

		addBehaviour(new CyclicBehaviour(this) {
			@Override
			public void action() {
				// Check for messages from other agents (if needed)
				MessageTemplate passtoSBJoinTemplate = MessageTemplate.MatchConversationId("passtoSBJoin");
				ACLMessage passtoSBJoinmsg = receive(passtoSBJoinTemplate);

				if (passtoSBJoinmsg != null) {
					try {
						Gson gson = new Gson();
						returnPasstoSBDTO messageObjectContent = (returnPasstoSBDTO) passtoSBJoinmsg.getContentObject();
						String jsonInputString = gson.toJson(messageObjectContent);
						System.out.println("Sending to Spring Boot: " + jsonInputString);
						sendMessageToClients(jsonInputString);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					block(); // Block until a new message is received
				}
			}
		});
	}

	private void handleClient(Socket clientSocket) {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

			String inputLine = in.readLine();
			System.out.println("Received: " + inputLine);
			if (inputLine != null && !inputLine.isEmpty()) {
				//System.out.println("in if call : "+inputLine); 
				addBehaviour(new WaitTCPListenBehaviour(inputLine));
				//System.out.println("Received registration to tcp: " + inputLine);

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				clientSockets.remove(clientSocket);
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendMessageToClients(String message) {
		for (Socket clientSocket : clientSockets) {
			try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
				out.println(clientSocket+" : " + message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class WaitTCPListenBehaviour extends OneShotBehaviour {

		public String message;

		public WaitTCPListenBehaviour(String message){
			this.message =message;
		}

		@Override
		public void action(){
			if(this.message != null && !this.message.isEmpty()){

				Gson gson = new Gson();
				JoinRequestDTO passengerData = gson.fromJson(this.message, JoinRequestDTO.class);
				createPassengerAgent(passengerData); 

			}else {
				System.out.println("Message is null or empty");
			}


		}

		public void createPassengerAgent(JoinRequestDTO passenger) {
			try {
				// Get the current container
				AgentContainer container = getContainerController();

				Object[] args = new Object[] { passenger };
				// Create the agent
				AgentController passengerAgent = container.createNewAgent(passenger.getJoinReqId(), PassengerAgent.class.getName(), args);
				passengerAgent.start();
				System.out.println("Created agent: " +passenger.getJoinReqId());
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}



	}
}
