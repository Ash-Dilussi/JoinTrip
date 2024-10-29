import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import {API_BASE_URL} from "@env";

 
const WEBSOCKET_URL = API_BASE_URL;  

class WebSocketService {
  constructor() {
    this.stompClient = null;
  }

  // Initialize WebSocket connection and subscribe to topics
  connect(onPassengerRequest ) {
    const socket = new SockJS(WEBSOCKET_URL);
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log(str),
      onConnect: () => {
        console.log('Connected to WebSocket');

        
        this.stompClient.subscribe('/topic/passenger/requests', (message) => {
          onPassengerRequest(JSON.parse(message.body));
        });

      
      },
      onDisconnect: () => {
        console.log('Disconnected from WebSocket');
      },
      reconnectDelay: 5000, // Reconnect every 5 seconds if disconnected
    });

    this.stompClient.activate();
  }

  
  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }
}

export default new WebSocketService();
