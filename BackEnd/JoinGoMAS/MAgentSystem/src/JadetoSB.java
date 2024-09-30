import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

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
		@Override
		public void action() {
			System.out.println(getAgent().getLocalName() + ": Waiting for msg to send...");
			// Receive messages from PassengerAgents and respond to ride requests
			ACLMessage msg = receive();
			if (msg != null) {
				System.out.println(getAgent().getLocalName() + ": msg received from " + msg.getSender().getLocalName() + ": " + msg.getContent()+ "....Sending...");

				String msgtosb= String.valueOf(getAgent().getLocalName() + ": msg received from " + msg.getSender().getLocalName() + ": " + msg.getContent()+ "....Sending...");
				try {
					sendMessageToSpringBoot(msgtosb);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				ACLMessage response = msg.createReply();
				response.setPerformative(ACLMessage.INFORM);
				response.setContent("Msg Sent");
				send(response);
			} else {
				block();
			}
		}
		private void sendMessageToSpringBoot(String messageContent) throws Exception {

			URL url = new URL("http://localhost:8080/api/v1/passenger/fromMAS");


			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");


			String jsonInputString = "{\"content\": \"" + messageContent + "\"}";


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
