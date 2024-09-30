import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

// Define a PassengerAgent class representing a passenger seeking a taxi ride
public class PassengerAgent extends Agent {

	private long startTime;
	private static final long TIME_LIMIT = 7000;
	private static int 	closeradius= 1;
	private  passengerDTO passengerData = new passengerDTO();
	private passengerDTO comparePassenger = new passengerDTO();
	private List<passengerDTO> nearbyPassengerList = new ArrayList<>();

	//	public passengerDTO getPassengerData() {
	//		return passengerData;
	//	}
	//
	//	public void setPassengerData(passengerDTO passengerData) {
	//		this.passengerData = passengerData;
	//	}

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
			System.out.println("Passenger Agent created for: " + passengerData.getPasId());
		} else {
			System.out.println("No data received.");
		}

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
				//passengerDTO passenger= getPassengerData();

				System.out.println("broadcasting:");
				System.out.println("placeID: " + passengerData.getDesplace_id());
				System.out.println("Name: " + passengerData.getPasId());

				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setContent(passengerData.getDesplace_id()); 
				msg.addReceiver(new AID(PassengerAgent.class.getName(), AID.ISLOCALNAME)); // Change to your target agent's name
				send(msg);

			}

			else {
				//remove behavior
				System.out.println(getLocalName() + " :Done Broadcast. terminating agent");
				for (passengerDTO passenger : nearbyPassengerList) {
					System.out.println(passenger.getPasName() +" : " + passenger.getDesplace_id());
				}

				doDelete();   
			}

		}
	}

	class sendorinfotoMatch extends CyclicBehaviour{


		@Override
		public void action(){ 
			ACLMessage msg = receive();
			if(msg != null) { 
				try{	
					String content =msg.getContent();
					String mydes= passengerData.getDesplace_id();
					switch(content) {
					case "infrotoMatch":

						comparePassenger = (passengerDTO) msg.getContentObject();
						System.out.println("comapre passenger data:"+ passengerData.getPasName()); 
						System.out.println("Compare ID: " + passengerData.getPasId());


						double distance = haversine(comparePassenger.getStartLat(), comparePassenger.getStartLon(), passengerData.getStartLat(), passengerData.getStartLon());

						if (distance <= closeradius) {
							nearbyPassengerList.add(comparePassenger);
						}

						break;

					default:

						if(content == mydes) {

							ACLMessage response = msg.createReply();
							response.setPerformative(ACLMessage.INFORM);
							response.setContent("infrotoMatch");
							response.setContentObject(passengerData);
							send(response);

						}
						break;
					} 

					msg.setContentObject(passengerData);}
				catch( IOException  | UnreadableException e)   {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {block();}

		}
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
