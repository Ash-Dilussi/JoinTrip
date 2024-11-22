import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import DTO.JoinRequestDTO;
import DTO.ResJoinMachListDTO;
import jade.core.AID;
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
import jade.lang.acl.UnreadableException;

 
public class PassengerAgent extends Agent {

	private long startTime;
	private static final long TIME_LIMIT = 10000;
	private static int 	closeradius= 2;
	private  JoinRequestDTO passengerData = new JoinRequestDTO();
	//private JoinRequestDTO comparePassenger = new JoinRequestDTO();
	private List<String> destPlaceIdCheckednequlList = new ArrayList<>();
	private List<JoinRequestDTO> joinCompaitbleList = new ArrayList<>(); 
	private List<String> messagedAgents = new ArrayList<>();



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
			passengerData = (JoinRequestDTO) args[0]; 
		} else {
			System.out.println("No data received.");
		}

		destPlaceIdCheckednequlList.add(getLocalName());
		messagedAgents.add(getLocalName()); 

		System.out.println("++Created agent: " +getAID().getLocalName() + ": A Join Passenger agent created.");

		addBehaviour(new agentdeteTimer(this, TIME_LIMIT));
		addBehaviour(new sendorinfotoMatch());
		addBehaviour(new destinationBroadcast());

		addBehaviour(new firstnewreqplaceidone()); 
		addBehaviour(new broadcastNewarrival()); 




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

	class firstnewreqplaceidone extends OneShotBehaviour{

		@Override
		public void action() {

			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sdnew = new ServiceDescription();
			sdnew.setType("passenger");
			template.addServices(sdnew);

			try {
				DFAgentDescription[] result = DFService.search(this.getAgent(), template);
				if (result.length > 0) {
					for (DFAgentDescription dfAgent : result) {
						//AID agentAID = dfAgent.getName();
						if( !messagedAgents.contains(dfAgent.getName().getLocalName()) ) {  

							String mypalceId = passengerData.getDesplace_id();
							ACLMessage msgplaceinfo = new ACLMessage(ACLMessage.REQUEST); 
							msgplaceinfo.addReceiver(dfAgent.getName()); 
							msgplaceinfo.setConversationId("placeId");  
							msgplaceinfo.setContent(mypalceId);			
							send(msgplaceinfo);

						}

					}
				}
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}

		}

	}


	class broadcastNewarrival extends OneShotBehaviour{
		@Override
		public void action() {

			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sdnew = new ServiceDescription();
			sdnew.setType("passenger");
			template.addServices(sdnew);

			try {
				DFAgentDescription[] result = DFService.search(this.getAgent(), template);
				if (result.length > 0) {
					for (DFAgentDescription dfAgent : result) {

						if( !messagedAgents.contains(dfAgent.getName().getLocalName()) ) {  

							ACLMessage msgnewalert = new ACLMessage(ACLMessage.REQUEST); 
							msgnewalert.addReceiver(dfAgent.getName());
							msgnewalert.setConversationId("newReqNotice");  
							msgnewalert.setContent("new Request Came!!");

							send(msgnewalert);


						} 
					}
				}
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}


		}
	}

	class agentdeteTimer extends TickerBehaviour{


		public agentdeteTimer(Agent a, long period) {
			super(a, period);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onTick() {
			long elapsedTime = System.currentTimeMillis() - startTime; 
			if (elapsedTime >= TIME_LIMIT) { 

				System.out.println( "Done Broadcasting. Terminating agent : " + getLocalName() );
				if (!joinCompaitbleList.isEmpty()) {

					try { 


						ACLMessage msgtoSB = new ACLMessage(ACLMessage.REQUEST);  

						ResJoinMachListDTO listToSB = new ResJoinMachListDTO();
						listToSB.setCurrentPassenger(passengerData);
						listToSB.setJoinPassengerList(joinCompaitbleList);
						
						msgtoSB.setConversationId("fromPassengerJoinList");  
						msgtoSB.setContentObject((Serializable) listToSB); 
						msgtoSB.addReceiver(new AID("Jade2SBAgent", AID.ISLOCALNAME)); 
						send(msgtoSB);


						for (JoinRequestDTO passenger : joinCompaitbleList) {
							System.out.println(passenger.getJoinReqId() +" : " + passenger.getDesplace_id());
						}

					} catch (java.io.IOException e) {
						e.printStackTrace();
					}

				} 


				doDelete();

			}

		}

	}

	class destinationBroadcast extends  CyclicBehaviour{


		@Override
		public void action(){


			MessageTemplate newReqNoticeTemplate = MessageTemplate.MatchConversationId("newReqNotice");
			ACLMessage newReqNoticeMsg = receive(newReqNoticeTemplate);


			if (newReqNoticeMsg != null) {  
				String mypalceId = passengerData.getDesplace_id();

				ACLMessage response = newReqNoticeMsg.createReply();
				response.setPerformative(ACLMessage.INFORM);
				response.setConversationId("placeId"); 
				response.setContent(mypalceId);
				send(response);



			}else {
				block();
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
					JoinRequestDTO comparePassenger = (JoinRequestDTO) joinpassdataMsg.getContentObject();

					double distance = haversine(comparePassenger.getStartLat(), comparePassenger.getStartLon(), passengerData.getStartLat(), passengerData.getStartLon());
					if (distance <= closeradius) {
						System.out.println(getLocalName()+" : Join match to: " + joinpassdataMsg.getSender().getLocalName());

						joinCompaitbleList.add(comparePassenger);
						

							try { 
								ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);  

								ResJoinMachListDTO listToSB = new ResJoinMachListDTO();
								listToSB.setCurrentPassenger(passengerData);
								listToSB.setJoinPassengerList(joinCompaitbleList);
								
								msg.setConversationId("fromPassengerJoinList");   
								msg.setContentObject((Serializable) listToSB); 
								msg.addReceiver(new AID("Jade2SBAgent", AID.ISLOCALNAME)); 
								send(msg);

								joinCompaitbleList.clear();
							} catch (java.io.IOException e) {
								e.printStackTrace();
							}

						
					}

					

				} catch (UnreadableException e) { 
					e.printStackTrace();
				}

			}

			else {
				block();}


		}




		public  boolean isindestPlaceIdCheckednequlList(String joinpassname) {
			return destPlaceIdCheckednequlList.contains(joinpassname);

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




