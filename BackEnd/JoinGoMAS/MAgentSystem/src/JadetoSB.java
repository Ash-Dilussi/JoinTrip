import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.google.gson.Gson;

import DTO.ResDriverMatch;
import DTO.ResJoinMachListDTO;
import DTO.longrouteSegmentDTO;
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
		private final String BASE_API_URL = "http://localhost:8080/api/v1";

		@Override
		public void action() {

			
			
			MessageTemplate fromPassengerJoinListTemplate = MessageTemplate.MatchConversationId("fromPassengerJoinList");
			ACLMessage fromPassengerJoinListmsg = receive(fromPassengerJoinListTemplate);

			MessageTemplate fromPassengerFarRouteTemplate = MessageTemplate.MatchConversationId("fromPassengerLongroutSegs");
			ACLMessage fromPassengerFarRoutemsg = receive(fromPassengerFarRouteTemplate);
 
			MessageTemplate fromDriverRideMatchTemplate = MessageTemplate.MatchConversationId("fromDriverRideMatch");
			ACLMessage fromDriverRideMatchmsg = receive(fromDriverRideMatchTemplate);
 

			if (fromPassengerJoinListmsg != null) {

			//	System.out.println("Message content: '" + fromPassengerJoinListmsg.getContent() + "'");

				try {
					String reddUrl = BASE_API_URL +"/passenger/masReponseJoin";
	  
					 
					//	reddUrl = BASE_API_URL +"/driver/masReponseJoin";
					  
					ResJoinMachListDTO messageOBjectContent = (ResJoinMachListDTO) fromPassengerJoinListmsg.getContentObject();
					
					String jsonInputString = gson.toJson(messageOBjectContent);
					sendJoinlistToSpringBoot(jsonInputString, reddUrl);
					 
				 } catch (Exception e) {
					throw new RuntimeException(e);
				} 
			}
			
			else if(fromPassengerFarRoutemsg != null) {
				
				
				try {
					String reddUrl = BASE_API_URL +"/passenger/masReponseFarRoute";
	 
					 
					List<longrouteSegmentDTO> messageOBjectContent = (List<longrouteSegmentDTO>) fromPassengerJoinListmsg.getContentObject();
					
					String jsonInputString = gson.toJson(messageOBjectContent);
					sendJoinlistToSpringBoot(jsonInputString, reddUrl);
					 
				 } catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			else if(fromDriverRideMatchmsg != null) {
				
				
				try {
					String reddUrl = BASE_API_URL +"/driver/masReponseTaxiMatch";
	 
					 
					ResDriverMatch messageOBjectContent = (ResDriverMatch) fromDriverRideMatchmsg.getContentObject();
					
					String jsonInputString = gson.toJson(messageOBjectContent);
					sendJoinlistToSpringBoot(jsonInputString, reddUrl);
					 
				 } catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
				else {
				block();	
			}
			
			
			
			
			
		}

		private void sendJoinlistToSpringBoot(String messageContent, String redirectURL) throws Exception {


			System.out.println("Redirect URL: " + redirectURL);


			URI uri = new URI(redirectURL);

			// Convert URI to URL
			URL url = uri.toURL();

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");


			


			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = messageContent.getBytes(StandardCharsets.UTF_8);
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
