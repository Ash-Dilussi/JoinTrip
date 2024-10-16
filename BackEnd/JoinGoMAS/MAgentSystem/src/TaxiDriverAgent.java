import java.io.Serializable;

import DTO.TaxiRequestDTO;
import DTO.TaxiStatusDTO;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class TaxiDriverAgent extends Agent{

	private TaxiStatusDTO driverData = new TaxiStatusDTO();
	private static int closeradius = 5;
	private static int homecloseradius = 10;


	@Override
	protected void setup() {


		registerWithDF();


		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			// Assuming the first argument is the PassengerDTO object
			driverData = (TaxiStatusDTO) args[0]; 
		} else {
			System.out.println("No driver data received.");
		}

		System.out.println("++Driver Created agent: " +getAID().getLocalName() + ": A Driver.");


		addBehaviour(new DriverStatisUpdates());

	}

	private void registerWithDF() {

		ServiceDescription sd = new ServiceDescription();
		sd.setType("taxidriver");
		sd.setName(getLocalName());
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	class DriverStatisUpdates extends CyclicBehaviour{

		@Override
		public void action() {



			MessageTemplate stausTemplate = MessageTemplate.MatchConversationId("taxistatus");
			ACLMessage taxistatusMsg = receive(stausTemplate);


			try {
				if(taxistatusMsg !=null) {


					TaxiStatusDTO driverStatUpdate =(TaxiStatusDTO) taxistatusMsg.getContentObject();
					var headdingchange = (driverData.getHeadingLat() != driverStatUpdate.getHeadingLat() || driverData.getHeadingLon() !=  driverStatUpdate.getHeadingLon());
					if(headdingchange) {
						driverData.setHeadingLat(driverStatUpdate.getHeadingLat());
						driverData.setHeadingLon(driverStatUpdate.getHeadingLon());
					}

					var currentlocationchange = (driverData.getCurrentLat() != driverStatUpdate.getCurrentLat() || driverData.getCurrentLon() !=  driverStatUpdate.getCurrentLon());
					if(currentlocationchange) {
						driverData.setCurrentLat(driverStatUpdate.getCurrentLat());
						driverData.setCurrentLon(driverStatUpdate.getCurrentLon());
					}

					var taxiStasuschange = (driverData.getTaxiStatus() != driverStatUpdate.getTaxiStatus());
					if(taxiStasuschange) {
						driverData.setTaxiStatus(driverStatUpdate.getTaxiStatus());
						if(driverData.getTaxiStatus() == 0) {

							DFService.deregister(myAgent);

						}else if(driverData.getTaxiStatus() ==1) {
							registerWithDF();
							//this.notify();
						}
					}
					var serviceavailabilitychange = (driverData.getOnService() != driverStatUpdate.getOnService());
					if(serviceavailabilitychange) {
						driverData.setOnService(driverStatUpdate.getOnService());
						if(driverData.getOnService() == 0) {
							System.out.println( " Terminating driver: " + getLocalName() );

							doDelete();
						}
					}

				}else {block();}

			}catch(Exception ex) {
				ex.printStackTrace();
			}

		}


	}



	class lookingfoTripCalls extends CyclicBehaviour{


		@Override
		public void action() {

			MessageTemplate tripCallTemplate = MessageTemplate.MatchConversationId("passengerTripCall");
			ACLMessage tripCallMsg = receive(tripCallTemplate);

			try {

				if(tripCallMsg !=null) {
					TaxiRequestDTO taxiCall = (TaxiRequestDTO) tripCallMsg.getContentObject();
					double distance = haversine(driverData.getCurrentLat(), driverData.getCurrentLon(), taxiCall.startLat, taxiCall.startLon);
					if(distance <= closeradius) {

						//A latitude of 0 and a longitude of 0 (0, 0) points to a location in the Gulf of Guinea, off the coast of West Africa, which is often considered "ocean" rather than land.
						if(driverData.getHeadingLat() == 0 && driverData.getHeadingLon() == 0) {
							ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);  

							msg.setConversationId("toJadetoSB");   
							msg.setContent("DriverAboutMatch");
							msg.setContentObject((Serializable) taxiCall); 
							msg.addReceiver(new AID("Jade2SBAgent", AID.ISLOCALNAME)); 
							send(msg);
						}else {
							double enddistance = haversine(driverData.getHeadingLat(), driverData.getHeadingLon(), taxiCall.destLat, taxiCall.destLon);
							if(enddistance <= homecloseradius) {
								ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);  

								msg.setConversationId("toJadetoSB");   
								msg.setContent("DriverAboutMatch");
								msg.setContentObject((Serializable) taxiCall); 
								msg.addReceiver(new AID("Jade2SBAgent", AID.ISLOCALNAME)); 
								send(msg);

							}
						}

					}
				}

			}
			catch(Exception ex) {
				ex.printStackTrace();
			}

		}






		public static final double EARTH_RADIUS = 6371.0; // Earth radius in kilometers

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