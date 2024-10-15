import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;

import DTO.returnPasstoSBDTO;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class JadetoSB extends Agent {

	@Override
	protected void setup() {
		ServiceDescription sd = new ServiceDescription();
		sd.setType("JadetoSB");
		sd.setName(getLocalName());
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println(getAID().getLocalName()+" : JadetoSB agent created.");
		addBehaviour(new JadetoSB.WaitformsgBehaviour());
	}


	class WaitformsgBehaviour extends CyclicBehaviour {
		private final Gson gson = new Gson();

		@Override
		public void action() {

			MessageTemplate passtoSBJoinTemplate = MessageTemplate.MatchConversationId("toJadetoSB");
			ACLMessage passtoSBJoinmsg = receive(passtoSBJoinTemplate);

			//System.out.println(getAgent().getLocalName() + ": Waiting for msg to send..."); 


			if (passtoSBJoinmsg != null) {

				try {
					String reddUrl = "";

					if(passtoSBJoinmsg.getContent().equals("passengerAboutJoint")) { 
						reddUrl = "http://localhost:8080/api/v1/passenger/masReponseJoin";

					}else if(passtoSBJoinmsg.getContent().equals("DriverAboutMatch")) {
						reddUrl = "http://localhost:8080/api/v1/driver/masReponseJoin";
					}

					returnPasstoSBDTO messageOBjectContent = (returnPasstoSBDTO) passtoSBJoinmsg.getContentObject();
					sendJoinlistToSpringBoot(messageOBjectContent, reddUrl);
					LocalTime currentTime = LocalTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
					String formattedTime = currentTime.format(formatter);
					System.out.println("Sent to SB from :: "+formattedTime +" -->"+ passtoSBJoinmsg.getSender().getLocalName());
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
			} else {
				block();	
			}
		}

		private void sendJoinlistToSpringBoot(returnPasstoSBDTO messageContent, String redirectURL) throws Exception {





			URI uri = new URI(redirectURL);

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
