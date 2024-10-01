import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

// Define a PassengerAgent class representing a passenger seeking a taxi ride
public class PassengerAgent extends Agent {

	private long startTime;
	private static final long TIME_LIMIT = 90000;
	private static int 	closeradius= 1;
	private  passengerDTO passengerData = new passengerDTO();
	private passengerDTO comparePassenger = new passengerDTO();
	private List<String> destPlaceIdCheckednequlList = new ArrayList<>();
	private List<passengerDTO> joinCompaitbleList = new ArrayList<>();



	@Override
	protected void setup() {

		startTime = System.currentTimeMillis();


		ServiceDescription sd = new ServiceDescription();
		sd.setType("passenger");
		sd.setName(getLocalName());
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			// Assuming the first argument is the PassengerDTO object
			passengerData = (passengerDTO) args[0]; 
		} else {
			System.out.println("No data received.");
		}
		destPlaceIdCheckednequlList.add(getLocalName());
		System.out.println(getAID().getLocalName() + ": A Join Passenger agent created.");
		// addBehaviour(new FindTaxiBehaviour());
		//addBehaviour(new callfromTCPListner());
		addBehaviour(new destinationBroadcast());
		addBehaviour(new sendorinfotoMatch());


	}


	class FindTaxiBehaviour extends OneShotBehaviour {


		@Override
		public void action() {
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("taxi");
			template.addServices(sd);
			try {
				System.out.println(getAgent().getLocalName() + ": Searching for available taxis...");
				DFAgentDescription[] result = DFService.search(this.getAgent(), template);
				if (result.length > 0) {
					for (DFAgentDescription dfAgent : result) {
						ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
						msg.addReceiver(dfAgent.getName());
						msg.setContent("Need a ride!");
						send(msg);

					}
				}
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}


		}
	}


	class callfromTCPListner  extends CyclicBehaviour{

		@Override
		public void action(){


			// Receive messages from PassengerAgents and respond to ride requests
			ACLMessage msg = receive();
			if (msg != null){ 
				System.out.println(getAgent().getLocalName() + ": Waiting for new passenger data kk...");
				try{
					//passengerDTO receivedData = (passengerDTO) msg.getContentObject();
					passengerData = (passengerDTO) msg.getContentObject();
					//setPassengerData(receivedData);

					System.out.println("Received passenger data:");
					System.out.println("ID: " + passengerData.getPasName());
					System.out.println("Name: " + passengerData.getPasName());


					System.out.println(getAgent().getLocalName() + ": Ride requested by " + msg.getSender().getLocalName() + " " + passengerData.getPasName());





				}catch(UnreadableException e) {   e.printStackTrace();}


			} else {
				block(); // Block until a message is received
			}

		}
	}

	class destinationBroadcast extends  CyclicBehaviour{



		@Override
		public void action(){

			long elapsedTime = System.currentTimeMillis() - startTime;
			if (elapsedTime < TIME_LIMIT) { 

				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("passenger");
				template.addServices(sd);
				try { 
					String mypalceId = passengerData.getDesplace_id();
					DFAgentDescription[] result = DFService.search(this.getAgent(), template);
					if (result.length > 0) {
						for (DFAgentDescription dfAgent : result) {
							ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
							msg.setConversationId("placeId");
							msg.addReceiver(dfAgent.getName());
							msg.setContent( mypalceId);
							send(msg);

							block(1000); // Block for 1 seconds to wait for responses
						}
					}
				} catch (FIPAException fe) {
					fe.printStackTrace(); 


				}
			}else {


				final Gson gson = new Gson();
				try { 
					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);  

					returnPasstoSBDTO listToSB = new returnPasstoSBDTO();
					listToSB.setCurrentPassenger(passengerData);
					listToSB.setJoinPassengerList(joinCompaitbleList);
					msg.setConversationId("passtoSBJoin");  
					String jsonString = gson.toJson(listToSB);
					msg.setContent(jsonString);
					msg.setContentObject(listToSB); 
					msg.addReceiver(new AID("Jade2SBAgent", AID.ISLOCALNAME)); 
					send(msg);

				} catch (java.io.IOException e) {
					e.printStackTrace();
				}
				System.out.println(getLocalName() + " :Done Broadcasting. Terminating agent");
				for (passengerDTO passenger : joinCompaitbleList) {
					System.out.println(passenger.getPasName() +" : " + passenger.getDesplace_id());
				}
				doDelete(); 

			}


		}



	}

	class sendorinfotoMatch extends CyclicBehaviour{


		@Override
		public void action(){ 


			MessageTemplate placeIdBroadcastTemplate = MessageTemplate.MatchConversationId("placeId");
			ACLMessage placebroadtMsg = receive(placeIdBroadcastTemplate);

			MessageTemplate joinpassinfoTemplate = MessageTemplate.MatchConversationId("joinpassenger");
			ACLMessage joinpassdataMsg = receive(joinpassinfoTemplate);



			if (placebroadtMsg != null) { 
				if( placebroadtMsg.getContent().equals(passengerData.getDesplace_id()) && !isindestPlaceIdCheckednequlList(placebroadtMsg.getSender().getLocalName())) {

					//System.out.println("Received request to ride from: " + placebroadtMsg.getSender().getLocalName());


					try {
						ACLMessage response = placebroadtMsg.createReply();
						response.setConversationId("joinpassenger");
						response.setPerformative(ACLMessage.INFORM);
						response.setContent("infrotoMatch"); 
						response.setContentObject(passengerData); 
						send(response);
						destPlaceIdCheckednequlList.add(placebroadtMsg.getSender().getLocalName());
					}catch (IOException e) {

						e.printStackTrace();
					} 

				}


			}

			if(joinpassdataMsg != null){


				try {
					comparePassenger = (passengerDTO) joinpassdataMsg.getContentObject();
					//System.out.println("Compare ID: " + comparePassenger.getPasId());
					double distance = haversine(comparePassenger.getStartLat(), comparePassenger.getStartLon(), passengerData.getStartLat(), passengerData.getStartLon());
					if (distance <= closeradius) {
						System.out.println(getLocalName()+" : Join match: " + placebroadtMsg.getSender().getLocalName());

						joinCompaitbleList.add(comparePassenger);
					}

				} catch (UnreadableException e) { 
					e.printStackTrace();
				}

			}

			else {
				block();}


		}




		private  boolean isindestPlaceIdCheckednequlList(String joinpassname) {
			return destPlaceIdCheckednequlList.contains(joinpassname);

		}


		private static final double EARTH_RADIUS = 6371.0; // Earth radius in kilometers

		public static double haversine(double lat1, double lon1, double lat2, double lon2) {
			double dLat = Math.toRadians(lat2 - lat1);
			double dLon = Math.toRadians(lon2 - lon1);

			double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
					Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
					Math.sin(dLon / 2) * Math.sin(dLon / 2);

			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

			return EARTH_RADIUS * c; // Distance in kilometers
		}

	}
}







//      System.out.println(getAgent().getLocalName() + ": Searching for available taxis...");
//// Send a message to all TaxiAgents to find an available taxi
//ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
//
//
//AID raid= new AID("arun", AID.ISLOCALNAME);
//            msg.addReceiver(raid);
//            msg.setContent("Need a ride!");
//
//send(msg);
//block(5000); // Block for 1 seconds to wait for responses
