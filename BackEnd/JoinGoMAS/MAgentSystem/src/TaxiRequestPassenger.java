import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import DTO.JoinRequestDTO;
import DTO.TaxiRequestDTO;
import DTO.longrouteSegmentDTO; 
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class TaxiRequestPassenger extends Agent{
	
	
	
	private long startTime;
	private static final long TIME_LIMIT = 10000;
 
	private  JoinRequestDTO passengerData = new JoinRequestDTO();
 
 

	@Override
	protected void setup() {
		
		startTime = System.currentTimeMillis();


		ServiceDescription sd = new ServiceDescription();
		sd.setType("taxiReqpassenger");
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
			passengerData = (JoinRequestDTO) args[0]; 
		} else {
			System.out.println("No data received.");
		}
		
		
		
		System.out.println("++Created agent: " +getAID().getLocalName() + ": A Taxi Req Passenger agent created.");
		
		addBehaviour(new agentdeteTimer(this, TIME_LIMIT));
		addBehaviour(new newtoDriverBroadcast());  
		addBehaviour(new firsttoDriverBroadcast());

	}
	

	class agentdeteTimer extends TickerBehaviour{


		public agentdeteTimer(Agent a, long period) {
			super(a, period);
	 
		}

		@Override
		protected void onTick() {
			long elapsedTime = System.currentTimeMillis() - startTime; 
			if (elapsedTime >= TIME_LIMIT) { 
System.out.println("taxi  req Agetn terminating");
				doDelete();

			}

		}
	}
		
	
class firsttoDriverBroadcast extends OneShotBehaviour{
		
		@Override
		public void action() {
			
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription toDriver = new ServiceDescription();
			toDriver.setType("taxidriver");
			template.addServices(toDriver);
			
			try {
				DFAgentDescription[] result = DFService.search(this.getAgent(), template);
				if (result.length > 0) {
					TaxiRequestDTO toTaxi= new TaxiRequestDTO();
					toTaxi.vehicletype = passengerData.getReqVehicletype();
					toTaxi.JoinReqid = passengerData.getJoinReqId();
					
					
					for (DFAgentDescription dfAgent : result) {

	  toTaxi.taxiReqid = passengerData.getTaxiReqid(); 
	  toTaxi.JoinReqid = passengerData.getJoinReqId(); 
	  toTaxi.startLat =  passengerData.getStartLat();
	  toTaxi.startLon =  passengerData.getStartLon();
	  toTaxi.destLat =  passengerData.getDestLat();
	  toTaxi.destLon =  passengerData.getDestLon();
	  toTaxi.destinationName = passengerData.getDesplace_id();
				  
							ACLMessage msgnewalert = new ACLMessage(ACLMessage.INFORM); 
							msgnewalert.addReceiver(dfAgent.getName());
							msgnewalert.setConversationId("passengerTripCall");  
							msgnewalert.setContentObject((Serializable) toTaxi); 


							send(msgnewalert);

 
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			 
		}
	}

	
	
class newtoDriverBroadcast extends CyclicBehaviour{
		
		
		@Override
		public void action() {
			

			MessageTemplate taxiBroadcastTemplate = MessageTemplate.MatchConversationId("newTaxiAvailable");
			ACLMessage taxiBroadcastMsg = receive(taxiBroadcastTemplate);

			if(taxiBroadcastMsg != null) {
				
				
				try {
					TaxiRequestDTO toTaxi= new TaxiRequestDTO();
					toTaxi.vehicletype = passengerData.getReqVehicletype();
					toTaxi.JoinReqid = passengerData.getJoinReqId();
					
					
					ACLMessage response = taxiBroadcastMsg.createReply();
					response.setConversationId("passengerTripCall");
					response.setPerformative(ACLMessage.INFORM);  
					
			
					  toTaxi.taxiReqid = passengerData.getTaxiReqid(); 
					  toTaxi.JoinReqid = passengerData.getJoinReqId(); 
						  toTaxi.startLat = passengerData.getStartLat();
						  toTaxi.startLon = passengerData.getStartLon();
						  toTaxi.destLat =  passengerData.getDestLat();
						  toTaxi.destLon =  passengerData.getDestLon();
						  toTaxi.destinationName = passengerData.getDesplace_id();
					response.setContentObject((Serializable) toTaxi); 
					send(response); 
					
					
				}catch (IOException e) {

					e.printStackTrace();
				} 

			}else {
				block();
			}
		}
	}
	
	
}