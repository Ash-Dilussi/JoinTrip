import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class TaxiAgent extends Agent {
	@Override
	protected void setup() {
		// Register the taxi service in the yellow pages
		ServiceDescription sd = new ServiceDescription();
		sd.setType("taxi");
		sd.setName(getLocalName());
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println(getAID().getLocalName()+" : Taxi agent created.");
		addBehaviour(new WaitPassengerBehaviour());

	}

	// Behaviour for waiting for passenger requests
	class WaitPassengerBehaviour extends CyclicBehaviour {
		@Override
		public void action() {
			System.out.println(getAgent().getLocalName() + ": Waiting for passenger requests...");
			// Receive messages from PassengerAgents and respond to ride requests
			ACLMessage msg = receive();
			if (msg != null) {
				System.out.println(getAgent().getLocalName() + ": Ride requested by " + msg.getSender().getLocalName() + " " + msg.getContent()+ ".... Responding...");
				// Respond to the ride request
				ACLMessage response = msg.createReply();
				response.setPerformative(ACLMessage.INFORM);
				response.setContent("Taxi is on the way!");
				send(response);

			} else {
				block(); // Block until a message is received
			}
		}
	}

}