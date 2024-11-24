import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import DTO.Coordinate;
import DTO.JoinRequestDTO;
import DTO.ResJoinMachListDTO;
import DTO.TaxiRequestDTO;
import DTO.longrouteSegmentDTO;
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

public class LongRidePassengerAgent extends Agent {

	private long startTime;
	private static final long TIME_LIMIT = 15000;
	private JoinRequestDTO passengerData = new JoinRequestDTO();
	private List<Coordinate> fullLongRoute;
	private int segmentdistance;
	private List<longrouteSegmentDTO> longRouteSegments= new ArrayList<>();;
	private List<TaxiRequestDTO> taxiRequestList =new ArrayList<>();;

	@Override
	protected void setup() {

		startTime = System.currentTimeMillis();

		ServiceDescription sd = new ServiceDescription();
		sd.setType("longtrippassenger");
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

		fullLongRoute = passengerData.getLongRoute();
		segmentdistance = passengerData.getSegmentDistance();

		System.out.println("++Created agent: " + getAID().getLocalName() + ": A Join LongRidePassenger agent created.");

		addBehaviour(new agentdeteTimer(this, TIME_LIMIT));
		addBehaviour(new segmentRouteBehave(fullLongRoute, segmentdistance));
		
		

		addBehaviour(new newtoDriverBroadcast());

	}

	class firsttoDriverBroadcast extends OneShotBehaviour {

		@Override
		public void action() {

			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription toDriver = new ServiceDescription();
			toDriver.setType("taxidriver");
			template.addServices(toDriver);

			try {
				DFAgentDescription[] result = DFService.search(this.getAgent(), template);
				if (result.length > 0) {
					TaxiRequestDTO toTaxi = new TaxiRequestDTO();
					toTaxi.vehicletype = passengerData.getReqVehicletype();
					toTaxi.JoinReqid = passengerData.getJoinReqId();

					for (DFAgentDescription dfAgent : result) {
						if (!taxiRequestList.isEmpty()) { 

							ACLMessage msgnewalert = new ACLMessage(ACLMessage.INFORM);
							msgnewalert.addReceiver(dfAgent.getName());
							msgnewalert.setConversationId("passengerTripCall");
							
							for (TaxiRequestDTO req : taxiRequestList) {
							msgnewalert.setContentObject((Serializable) req);

							send(msgnewalert);
							}
						}

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	class newtoDriverBroadcast extends CyclicBehaviour {

		@Override
		public void action() {

			MessageTemplate taxiBroadcastTemplate = MessageTemplate.MatchConversationId("newTaxiAvailable");
			ACLMessage taxiBroadcastMsg = receive(taxiBroadcastTemplate);

			if (taxiBroadcastMsg != null) {

				if (!taxiRequestList.isEmpty()) {
					try {

						ACLMessage response = taxiBroadcastMsg.createReply();
						response.setConversationId("passengerTripCall");
						response.setPerformative(ACLMessage.INFORM);

						for (TaxiRequestDTO req : taxiRequestList) {
							response.setContentObject((Serializable) req);
							send(response);
						}

					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			} else {
				block();
			}
		}
	}

	class segmentRouteBehave extends OneShotBehaviour {

		private List<Coordinate> fullroute;
		private int segdistance;

		public segmentRouteBehave(List<Coordinate> dafullroute, int distance) {
			this.fullroute = dafullroute;
			this.segdistance = distance*100;

		}

		@Override
		public void action() {
			
			
			System.out.println("no of coords long distance: "+ this.fullroute.size());

			List<longrouteSegmentDTO> segments = new ArrayList<>();

			if (this.fullroute == null || this.fullroute.size() < 2) {
				return;
			}

			Coordinate segmentStart = this.fullroute.get(0);
			double accumulatedDistance = 0.0;
			int count = 1;
			for (int i = 1; i < this.fullroute.size(); i++) {
				Coordinate currentPoint = this.fullroute.get(i);
				double distance = calculateDistance(segmentStart, currentPoint);
		 
				accumulatedDistance += distance;

				if (accumulatedDistance >= this.segdistance) {
					// If accumulated distance meets or exceeds segment length, create a segment
					segments.add(
							new longrouteSegmentDTO(passengerData.getJoinReqId(), count, segmentStart, currentPoint));

					System.out.println("accumulate dis for seg long distance: "+ accumulatedDistance);
					
					// Reset for next segment
					segmentStart = currentPoint;
					accumulatedDistance = 0.0;
					count++;
				}
			}
		
			// if any remaining coordinates for final segment
			if (accumulatedDistance > 0 && !segments.isEmpty()) {
				segments.add(new longrouteSegmentDTO(passengerData.getJoinReqId(), count, segmentStart,
						this.fullroute.get(this.fullroute.size() - 1)));
			}
			System.out.println("no of segs long distance: "+ count);
			System.out.println("accumulate dis after for seg long distance: "+ accumulatedDistance); 
			longRouteSegments = segments;

			if (!longRouteSegments.isEmpty()) {
				try {

					TaxiRequestDTO toTaxi = new TaxiRequestDTO();
					toTaxi.vehicletype = passengerData.getReqVehicletype();
					toTaxi.JoinReqid = passengerData.getJoinReqId();
					toTaxi.tripType = 3;

					for (longrouteSegmentDTO asegment : longRouteSegments) {
						toTaxi.taxiReqid = asegment.getTripReqId() + asegment.getSegNo();
						toTaxi.startLat = (float) asegment.getStart().getLatitude();
						toTaxi.startLon = (float) asegment.getStart().getLongitude();
						toTaxi.destLat = (float) asegment.getEnd().getLatitude();
						toTaxi.destLon = (float) asegment.getEnd().getLongitude();
						toTaxi.destinationName =String.valueOf(asegment.getEnd().getLatitude())+ ", "+String.valueOf(asegment.getEnd().getLongitude());

						taxiRequestList.add(toTaxi);

					}
					System.out.println("to taxi long distance: "+ taxiRequestList.get(0).taxiReqid);
					

					if (!taxiRequestList.isEmpty()) {

						ACLMessage msgtoSB = new ACLMessage(ACLMessage.REQUEST);

						msgtoSB.setConversationId("fromPassLongTaxiReq");
						msgtoSB.addReceiver(new AID("Jade2SBAgent", AID.ISLOCALNAME));
						for (TaxiRequestDTO req : taxiRequestList) {
							msgtoSB.setContentObject((Serializable) req);

							send(msgtoSB);
						}
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}

				addBehaviour(new firsttoDriverBroadcast());
			}

		}

		private double calculateDistance(Coordinate start, Coordinate end) {
			final int EARTH_RADIUS_KM = 6371;

			double latDistance = Math.toRadians(end.getLatitude() - start.getLatitude());
			double lngDistance = Math.toRadians(end.getLongitude() - start.getLongitude());

			double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
					+ Math.cos(Math.toRadians(start.getLatitude())) * Math.cos(Math.toRadians(end.getLatitude()))
							* Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

			return EARTH_RADIUS_KM * c;
		}
	}

	class agentdeteTimer extends TickerBehaviour {

		public agentdeteTimer(Agent a, long period) {
			super(a, period);

		}

		@Override
		protected void onTick() {
			long elapsedTime = System.currentTimeMillis() - startTime;
			if (elapsedTime >= TIME_LIMIT) {

				doDelete();

			}

		}

	}

}