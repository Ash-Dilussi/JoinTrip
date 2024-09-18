import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

public class PassengerTCPListner extends Agent {

    @Override
    protected void setup() {
        ServiceDescription sd = new ServiceDescription();
        sd.setType("PassengerTCPListner");
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
                        addBehaviour(new PassengerTCPListner.WaitTCPListenBehaviour(message));
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

        private AtomicReference<String> message;

        public WaitTCPListenBehaviour(AtomicReference<String> message){
            this.message =message;
        }

        public void action(){
            if(message !=null){

                Gson gson = new Gson();
                passengerDTO passenderData = gson.fromJson(message.get(), passengerDTO.class);

                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("passenger"); // Type of agents to look for
                template.addServices(sd);

                try {
                    // Search DF for all agents providing the "passenger" service
                    DFAgentDescription[] result = DFService.search(this.getAgent(), template);
                    System.out.println("Found " + result.length + " passenger agents.");

                    // Send a message to a specific passenger agent
                    if (result.length > 0) {
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.addReceiver(result[0].getName()); // Send to first found agent (or choose a specific one)
                        msg.setContent(message.get()); // Task to activate
                        send(msg);

                        System.out.println("Sent activation message to " + result[0].getName().getLocalName());
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }



            }


        }












//        public void action() {
//            System.out.println("Listen Confirmed ");
//            DFAgentDescription template = new DFAgentDescription();
//            ServiceDescription sd = new ServiceDescription();
//            sd.setType("JadetoSB");
//            template.addServices(sd);
//            try {
//
//                DFAgentDescription[] result = DFService.search(this.getAgent(), template);
//                if (result.length > 0) {
//                    for (DFAgentDescription dfAgent : result) {
//                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
//                        msg.addReceiver(dfAgent.getName());
//                        msg.setContent("Received registration: " + "test1111");
//                        send(msg);
//                    }
//                }
//            } catch (FIPAException fe) {
//                fe.printStackTrace();
//            }
//
//        }

    }

}


