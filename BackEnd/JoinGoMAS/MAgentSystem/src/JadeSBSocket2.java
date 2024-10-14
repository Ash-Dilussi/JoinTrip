import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class JadeSBSocket2 extends Agent {

	@Override
	protected void setup() {
		System.out.println("JadeSBSocket");

		new Thread(() -> {

			try (ServerSocket serverSocket = new ServerSocket(8082)) {
				while (true) {
					try (Socket clientSocket = serverSocket.accept();
							BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
							PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

						String inputLine = in.readLine();
						System.out.println("Received: " + inputLine);
						if (inputLine != null && !inputLine.isEmpty()) {
							//System.out.println("in if call : "+inputLine); 
							addBehaviour(new WaitTCPListenBehaviour(inputLine));
							//System.out.println("Received registration to tcp: " + inputLine);

						}

						// Process the message here and prepare a response

						MessageTemplate passtoSBJoinTemplate = MessageTemplate.MatchConversationId("passtoSBJoin");
						ACLMessage passtoSBJoinmsg = receive(passtoSBJoinTemplate);

						System.out.println( getLocalName() + ": Waiting for msg to send..."); 


						if (passtoSBJoinmsg != null) {

							try {
								final Gson gson = new Gson();
								returnPasstoSBDTO messageOBjectContent = (returnPasstoSBDTO) passtoSBJoinmsg.getContentObject();
								//sendJoinlistToSpringBoot(messageOBjectContent);
								String jsonInputString = gson.toJson(messageOBjectContent);
								System.out.println("TO the SB: "+jsonInputString);
								out.println(jsonInputString);
							} catch (Exception e) {
								throw new RuntimeException(e);
							} 
						}

						// Send response back to Spring Boot

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}).start();


		//addBehaviour(new SocketServerBehaviour());

	}

	private class SocketServerBehaviour extends CyclicBehaviour {
		private ServerSocket serverSocket;

		public SocketServerBehaviour() {
			try {
				serverSocket = new ServerSocket(8082); // Same port as in Spring Boot
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void action() {


			try (Socket clientSocket = serverSocket.accept();
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

				String inputLine = in.readLine();
				System.out.println("brfore pass call : "+inputLine);

				if (inputLine != null && !inputLine.isEmpty()) {
					//System.out.println("in if call : "+inputLine); 
					addBehaviour(new WaitTCPListenBehaviour(inputLine));
					//System.out.println("Received registration to tcp: " + inputLine);

				}

				//System.out.println("after pass call : "+inputLine);

				MessageTemplate passtoSBJoinTemplate = MessageTemplate.MatchConversationId("passtoSBJoin");
				ACLMessage passtoSBJoinmsg = receive(passtoSBJoinTemplate);

				System.out.println(getAgent().getLocalName() + ": Waiting for msg to send..."); 


				if (passtoSBJoinmsg != null) {

					try {
						final Gson gson = new Gson();
						returnPasstoSBDTO messageOBjectContent = (returnPasstoSBDTO) passtoSBJoinmsg.getContentObject();
						//sendJoinlistToSpringBoot(messageOBjectContent);
						String jsonInputString = gson.toJson(messageOBjectContent);
						System.out.println("TO the SB: "+jsonInputString);
						out.println(jsonInputString);
					} catch (Exception e) {
						throw new RuntimeException(e);
					} 
				}

				in.close();
				out.close();
				clientSocket.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//		@Override
		//		public boolean done() {
		//			return false; // Keep the behavior running
		//		}
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
