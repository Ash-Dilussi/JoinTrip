import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import DTO.TaxiStatusDTO;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class JadeHTTPControllerAgent extends Agent{



	private static final int PORT = 8888;

	@Override
	protected void setup() {
		System.out.println("Controller Agent " + getLocalName() + " is ready.");
		startHttpServer();

		// Add JADE behaviour to receive ACL messages
		addBehaviour(new CyclicBehaviour(this) {
			@Override
			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					System.out.println("Message received from another JADE agent: " + msg.getContent());
				} else {
					block();
				}
			}
		});
	}

	private void startHttpServer() {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

			//  multiple contexts
			server.createContext("/taxiRequest", new taxiRequestHandler());
			server.createContext("/taxiStatusUpdate", new taxiStatusUpdateHandler());
			//server.createContext("/taxiMatchReply", new jsutgetHandler());

			server.setExecutor(null); // creates a default executor
			server.start();
			System.out.println("HTTP Server started on port " + PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Handler for processing data
	private class taxiStatusUpdateHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			if ("POST".equals(exchange.getRequestMethod())) {
				InputStream inputStream = exchange.getRequestBody();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				String data = reader.readLine();

				// Process the data
				try {
					Gson gson = new Gson();
					TaxiStatusDTO driver = gson.fromJson(data,TaxiStatusDTO.class);
					Object[] args = new Object[] { driver };

					if(isExistinMAS(driver.getDriverid()).length > 0) {
						DFAgentDescription[] existdriver = isExistinMAS(driver.getDriverid());

						for (DFAgentDescription agentDescription : existdriver) {
							ACLMessage message = new ACLMessage(ACLMessage.INFORM);
							message.setConversationId("taxistatus");
							message.setContentObject((Serializable) driver); 
							message.addReceiver(agentDescription.getName());

							send(message);
						}

					}else{
						AgentContainer container = getContainerController();
						AgentController schedulepassengerAgent = container.createNewAgent(driver.getDriverid(), TaxiDriverAgent.class.getName(), args);
						schedulepassengerAgent.start();
					}

				} catch(Exception ex) {ex.printStackTrace();}
				System.out.println("Data received via /processData: " + data);

				String response = "Data processed.";
				exchange.sendResponseHeaders(200, response.length());
				OutputStream os = exchange.getResponseBody();
				os.write(response.getBytes());
				os.close();
			}
		}

		private DFAgentDescription[] isExistinMAS(String driverName ) {
			DFAgentDescription[] results= null;
			try {
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setName(driverName);
				template.addServices(sd);

				results = DFService.search(JadeHTTPControllerAgent.this, template);

			}catch(Exception ex) {ex.printStackTrace();}
			//return results.length >0 ;
			return results;
		}
	}


	// Handler for sending messages
	private class taxiRequestHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			if ("POST".equals(exchange.getRequestMethod())) {
				InputStream inputStream = exchange.getRequestBody();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				String message = reader.readLine();

				try {
					System.out.println("Message received via /taxiRequest: " );

					DFAgentDescription template = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType("taxidriver");
					template.addServices(sd);

					DFAgentDescription[] result = DFService.search(JadeHTTPControllerAgent.this, template);
					if (result.length > 0) {
						for (DFAgentDescription dfAgent : result) {
							var agentlocal_name = dfAgent.getName().getLocalName();
							Agent daAgent = getAgent(agentlocal_name);
							ACLMessage passcallbrdcast = new ACLMessage(ACLMessage.REQUEST);  
							passcallbrdcast.addReceiver(dfAgent.getName());
							passcallbrdcast.setConversationId("passengerTripCall"); 
							passcallbrdcast.setContent(message);

							send(passcallbrdcast);

						}
					}

				}catch(Exception ex){	ex.printStackTrace(); }

				String response = "Message processed.";
				exchange.sendResponseHeaders(200, response.length());
				OutputStream os = exchange.getResponseBody();
				os.write(response.getBytes());
				os.close();
			}
		}

		private Agent getAgent(String localName) {


			return null;
		}
	}

	// Handler for getting status
	private class justgetHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			if ("GET".equals(exchange.getRequestMethod())) {
				// Respond with some status information
				String response = "Agent status: Active";
				exchange.sendResponseHeaders(200, response.length());
				OutputStream os = exchange.getResponseBody();
				os.write(response.getBytes());
				os.close();
			}
		}
	}



}