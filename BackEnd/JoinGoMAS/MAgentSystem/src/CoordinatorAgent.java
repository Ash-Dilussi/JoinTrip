
import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class CoordinatorAgent extends Agent {
    protected void setup() {
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                sendToAgentType("matching", "Message to driver");
                sendToAgentType("passenger", "Message to matching");
                sendToAgentType("taxi", "Message to passenger");
            }

            private void sendToAgentType(String agentType, String content) {
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType(agentType);
                template.addServices(sd);
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    if (result.length > 0) {
                        for (DFAgentDescription dfAgent : result) {
                            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                            msg.addReceiver(dfAgent.getName());
                            msg.setContent(content);
                            send(msg);
                        }
                    }
                } catch (FIPAException fe) {
                    fe.printStackTrace();
                }
            }
        });
    }
}
