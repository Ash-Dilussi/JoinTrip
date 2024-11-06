import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.Gson;

import DTO.JoinRequestDTO;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
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
		//addBehaviour(new WaitformsgBehaviour());

		// new thread to listen for incoming calls
		new Thread(() -> {
			try (ServerSocket serverSocket = new ServerSocket(8070)) {


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
						//System.out.println("Received registration to tcp: " + message);

					} 
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

				createPassengerAgent(passengerData);

			}else {
				System.out.println("Message is null or empty");
			}


		}

		private void createPassengerAgent(JoinRequestDTO passenger) {
			try {
				// Get the current container
				AgentContainer container = getContainerController();

				Object[] args = new Object[] { passenger };
				if(passenger.getTripType() == 2) {
					AgentController passengerAgent = container.createNewAgent(passenger.getJoinReqId(), PassengerAgent.class.getName(), args);
					passengerAgent.start();
				}else if(passenger.getTripType() == 4){
					AgentController schedulepassengerAgent = container.createNewAgent(passenger.getJoinReqId(), SchedulPasngerAgent.class.getName(), args);
					schedulepassengerAgent.start();
				}else if(passenger.getTripType() == 3) {
					AgentController longridepassengerAgent = container.createNewAgent(passenger.getJoinReqId(), LongRidePassengerAgent.class.getName(), args);
					longridepassengerAgent.start();
					
				}
				//System.out.println("Created agent: " +passenger.getJoinReqId());
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}

	}

}



//	class WaitformsgBehaviour extends CyclicBehaviour {
//		private final Gson gson = new Gson();
//
//		@Override
//		public void action() {
//
//			MessageTemplate passtoSBJoinTemplate = MessageTemplate.MatchConversationId("passtoSBJoin");
//			ACLMessage passtoSBJoinmsg = receive(passtoSBJoinTemplate);
//
//			System.out.println(getAgent().getLocalName() + ": Waiting for msg to send..."); 
//
//
//			if (passtoSBJoinmsg != null) {
//
//				try {
//					returnPasstoSBDTO messageOBjectContent = (returnPasstoSBDTO) passtoSBJoinmsg.getContentObject();
//					sendJoinlistToSpringBoot(messageOBjectContent);
//				} catch (Exception e) {
//					throw new RuntimeException(e);
//				}
//				//ACLMessage response = msg.createReply();
//				//response.setPerformative(ACLMessage.INFORM);
//				//response.setContent("Msg Sent");
//				//send(response);
//			} else {
//				block();	
//			}
//		}
//
//		private void sendJoinlistToSpringBoot(returnPasstoSBDTO messageContent) throws Exception {
//
//			URI uri = new URI("http://localhost:8080/api/v1/passenger/masReponseJoin");
//
//			// Convert URI to URL
//			URL url = uri.toURL();
//
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setRequestMethod("POST");
//			connection.setDoOutput(true);
//			connection.setRequestProperty("Content-Type", "application/json");
//
//
//			String jsonInputString = gson.toJson(messageContent);
//
//
//			try (OutputStream os = connection.getOutputStream()) {
//				byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
//				os.write(input, 0, input.length);
//			}
//
//			// Get the response
//			int responseCode = connection.getResponseCode();
//			if (responseCode == HttpURLConnection.HTTP_OK) {
//				System.out.println("Message sent to Spring Boot successfully!");
//			} else {
//				System.out.println("Failed to send message. Response code: " + responseCode);
//			}
//
//			connection.disconnect();
//		}
//	}
//
//}









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



