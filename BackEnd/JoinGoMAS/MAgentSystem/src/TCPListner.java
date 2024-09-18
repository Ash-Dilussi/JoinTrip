import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.introspection.AddedBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

public class TCPListner extends Agent {

    @Override
    protected void setup() {
        ServiceDescription sd = new ServiceDescription();
        sd.setType("TCPListner");
        sd.setName(getLocalName());
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println( getLocalName() + " started. Listening for registrations...");
        AtomicReference<String> message = new AtomicReference<>("");
        // new thread to listen for incoming calls
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8082)) {


                while (true) {

                    Socket socket = serverSocket.accept();
                    //serverSocket.setSoTimeout(30000);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    message.set(reader.readLine());


                    if(message.get() != null && !message.get().isEmpty()) {
                        String Message= String.valueOf(message);
                        //TCPConfirmTask(Message);
                        addBehaviour(new TCPListner.WaitTCPListenBehaviour());
                        System.out.println("Received registration: " + message);

                    }
                    // Close the socket
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


    }


    class WaitTCPListenBehaviour extends OneShotBehaviour {
        public void action() {
            System.out.println("Listen Confirmed ");
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("JadetoSB");
            template.addServices(sd);
            try {

                DFAgentDescription[] result = DFService.search(this.getAgent(), template);
                if (result.length > 0) {
                    for (DFAgentDescription dfAgent : result) {
                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                        msg.addReceiver(dfAgent.getName());
                        msg.setContent("Received registration: " + "test1111");
                        send(msg);
                    }
                }
            } catch (FIPAException fe) {
                fe.printStackTrace();
            }

        }

    }

}


