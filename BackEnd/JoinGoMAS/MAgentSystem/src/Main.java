import jade.core.Agent;

public class Main extends Agent {
	public static void main(String[] args) {
 
		jade.core.Runtime rt = jade.core.Runtime.instance();
		jade.wrapper.AgentContainer container = rt.createMainContainer(new jade.core.ProfileImpl());
		try {


			container.createNewAgent("Jade2SBAgent", JadetoSB.class.getName(), null).start(); 
			container.createNewAgent("PortListenerAgent", "PassengerTCPListner", null).start();
			container.createNewAgent("HTTPControllerAgent", JadeHTTPControllerAgent.class.getName(), null).start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}