import DTO.TaxiStatusDTO;
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

	@Override
	protected void setup() {

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


	class DriverStatisUpdates extends CyclicBehaviour{

		@Override
		public void action() {



			MessageTemplate stausTemplate = MessageTemplate.MatchConversationId("taxistatus");
			ACLMessage taxistatusMsg = receive(stausTemplate);


			MessageTemplate availabilityTemplate = MessageTemplate.MatchConversationId("availability");
			ACLMessage availabilityMsg = receive(availabilityTemplate);


			try {
				if(taxistatusMsg !=null) {
					//set is taxistatus
				}



				if(availabilityMsg != null) {
					//consider 'onsiervice' var
					// do delete
				}
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