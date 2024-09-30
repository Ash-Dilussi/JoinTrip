import jade.core.Agent;

public class Main extends Agent {
	public static void main(String[] args) {
		// Create and start the passenger and taxi agents
		jade.core.Runtime rt = jade.core.Runtime.instance();
		jade.wrapper.AgentContainer container = rt.createMainContainer(new jade.core.ProfileImpl());
		try {

			Object[] args3 =new Object[]{"arg1", "ar"};
			//  container.createNewAgent("Anura", helloworld.class.getName(), args3 ).start();

			//container.createNewAgent("Arun(Taxi)", TaxiAgent.class.getName(), null).start();
			//            container.createNewAgent("JoinPass1", PassengerAgent.class.getName(), null).start();
			//            container.createNewAgent("JoinPass2", PassengerAgent.class.getName(), null).start();
			//            container.createNewAgent("JoinPass3", PassengerAgent.class.getName(), null).start();
			//            container.createNewAgent("JoinPass4", PassengerAgent.class.getName(), null).start();
			//container.createNewAgent("JoinPass5", PassengerAgent.class.getName(), null).start();
			container.createNewAgent("Jade to SB Agent", JadetoSB.class.getName(), null).start();
			container.createNewAgent("PortListenerAgent", "PassengerTCPListner", null).start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}