import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.io.IOException;

// Define a PassengerAgent class representing a passenger seeking a taxi ride
public class PassengerAgent extends Agent {



    protected void setup() {
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

        System.out.println(getAID().getLocalName() + ": A Join Passenger agent created.");
        addBehaviour(new FindTaxiBehaviour());

    }


    class FindTaxiBehaviour extends OneShotBehaviour {
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
