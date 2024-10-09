import jade.core.Agent;

public class Main extends Agent {
	public static void main(String[] args) {
		// Create and start the passenger and taxi agents
		jade.core.Runtime rt = jade.core.Runtime.instance();
		jade.wrapper.AgentContainer container = rt.createMainContainer(new jade.core.ProfileImpl());
		try {


			container.createNewAgent("Jade2SBAgent", JadetoSB.class.getName(), null).start();
			//container.createNewAgent("JadeSBSocket", "JadeSBSocket", null).start();
				container.createNewAgent("PortListenerAgent", "PassengerTCPListner", null).start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}