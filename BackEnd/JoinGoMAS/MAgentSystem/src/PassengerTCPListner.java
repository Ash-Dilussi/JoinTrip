import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.Gson;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class PassengerTCPListner extends Agent {

	@Override
	protected void setup() {
		ServiceDescription sd = new ServiceDescription();
		sd.setType("PassengerTCPListner");
		sd.setName(getLocalName());
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println( getLocalName() + " started. Listening for registrations...");
		AtomicReference<String> message = new AtomicReference<>("");
		addBehaviour(new WaitformsgBehaviour());

		// new thread to listen for incoming calls
		new Thread(() -> {
			try (ServerSocket serverSocket = new ServerSocket(80823)) {


				while (true) {

					Socket socket = serverSocket.accept();
					//serverSocket.setSoTimeout(30000);
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));



					synchronized (message)  { 
						message.set(reader.readLine()); 
						if (message.get() != null && !message.get().isEmpty()) {
							// Add the behavior to handle the received message
							addBehaviour(new WaitTCPListenBehaviour(message));
						}
						System.out.println("Received registration to tcp: " + message);

					}
					// Close the socket
					socket.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();


	}


	class WaitTCPListenBehaviour extends OneShotBehaviour {

		private AtomicReference<String> message;

		public WaitTCPListenBehaviour(AtomicReference<String> message){
			this.message =message;
		}

		@Override
		public void action(){
			if(this.message != null && !this.message.get().isEmpty()){

				Gson gson = new Gson();


				JoinRequestDTO passengerData = gson.fromJson(this.message.get(), JoinRequestDTO.class);

				//					DFAgentDescription template = new DFAgentDescription();
				//					ServiceDescription sd = new ServiceDescription();
				//					sd.setType("passenger"); // Type of agents to look for
				//					template.addServices(sd);
				//					// Search DF for all agents providing the "passenger" service
				//					DFAgentDescription[] result = DFService.search(this.getAgent(), template);
				//					System.out.println("Found " + result.length + " passenger agents.");

				// Send a message to a specific passenger agent
				//					if (result.length > 0) {
				//						ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				//						msg.addReceiver(result[0].getName()); // Send to first found agent (or choose a specific one)
				//						msg.setContentObject(passengerData); // Task to activate
				//						send(msg);
				createPassengerAgent(passengerData);
				//						System.out.println("Sent activation message to " + result[0].getName().getLocalName());
				//					}



			}else {
				System.out.println("Message is null or empty");
			}


		}

		private void createPassengerAgent(JoinRequestDTO passenger) {
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

	class WaitformsgBehaviour extends CyclicBehaviour {
		private final Gson gson = new Gson();

		@Override
		public void action() {

			MessageTemplate passtoSBJoinTemplate = MessageTemplate.MatchConversationId("passtoSBJoin");
			ACLMessage passtoSBJoinmsg = receive(passtoSBJoinTemplate);

			System.out.println(getAgent().getLocalName() + ": Waiting for msg to send..."); 


			if (passtoSBJoinmsg != null) {

				try {
					returnPasstoSBDTO messageOBjectContent = (returnPasstoSBDTO) passtoSBJoinmsg.getContentObject();
					sendJoinlistToSpringBoot(messageOBjectContent);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				//ACLMessage response = msg.createReply();
				//response.setPerformative(ACLMessage.INFORM);
				//response.setContent("Msg Sent");
				//send(response);
			} else {
				block();	
			}
		}

		private void sendJoinlistToSpringBoot(returnPasstoSBDTO messageContent) throws Exception {

			URI uri = new URI("http://localhost:8080/api/v1/passenger/masReponseJoin");

			// Convert URI to URL
			URL url = uri.toURL();

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");


			String jsonInputString = gson.toJson(messageContent);


			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}

			// Get the response
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				System.out.println("Message sent to Spring Boot successfully!");
			} else {
				System.out.println("Failed to send message. Response code: " + responseCode);
			}

			connection.disconnect();
		}
	}

}









//        public void action() {
//            System.out.println("Listen Confirmed ");
//            DFAgentDescription template = new DFAgentDescription();
//            ServiceDescription sd = new ServiceDescription();
//            sd.setType("JadetoSB");
//            template.addServices(sd);
//            try {
//
//                DFAgentDescription[] result = DFService.search(this.getAgent(), template);
//                if (result.length > 0) {
//                    for (DFAgentDescription dfAgent : result) {
//                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
//                        msg.addReceiver(dfAgent.getName());
//                        msg.setContent("Received registration: " + "test1111");
//                        send(msg);
//                    }
//                }
//            } catch (FIPAException fe) {
//                fe.printStackTrace();
//            }
//
//        }



