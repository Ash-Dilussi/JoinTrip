import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

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
				System.out.println("Data received via /processData: " + data);

				String response = "Data processed.";
				exchange.sendResponseHeaders(200, response.length());
				OutputStream os = exchange.getResponseBody();
				os.write(response.getBytes());
				os.close();
			}
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

				// Process the message
				System.out.println("Message received via /taxiRequest: " );

				String response = "Message processed.";
				exchange.sendResponseHeaders(200, response.length());
				OutputStream os = exchange.getResponseBody();
				os.write(response.getBytes());
				os.close();
			}
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