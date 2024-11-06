import { TextDecoder, TextEncoder } from 'text-encoding';
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { WEBSOCKET_URL } from "@env";
import { useDispatch, useSelector } from "react-redux";
import {} from "../slices/navSlice";
 
// Polyfill for TextDecoder and TextEncoder in React Native
if (typeof global.TextDecoder === 'undefined') {
  global.TextDecoder = TextDecoder;
}

if (typeof global.TextEncoder === 'undefined') {
  global.TextEncoder = TextEncoder;
}

class WebSocketService {
  constructor() {
    this.stompClient = null;
    this.privateStompClient = null;

  }

  // Connect to the WebSocket and set up subscriptions
  connect(userInfo) {
    const socket = new SockJS(WEBSOCKET_URL);
    try {
      this.stompClient = new Client({
        webSocketFactory: () => socket,
        debug: (str) => console.log("11",str, socket.url),
        onConnect: () => {
         
          this.stompClient.subscribe(
            `/specific/passenger/requests/${userInfo.primaryUserInfo.userid}`,
            (message) => {
              // const parsedMessage = JSON.parse(message.body);
              console.log("parsedMessage:" ,message.body);
            }
          );
        },  
         onStompError: (error) => {
          console.error("STOMP error:", error.headers.message || error.body || error);
        },
        onDisconnect: () => {
          console.log("Disconnected from WebSocket");
        }, // Connection error handler
     
        reconnectDelay: 100, // Reconnect if disconnected: delay
      });

      this.stompClient.activate();
    } catch (error) {
      console.error("Error subscribong", error);
    }
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }
}

export default new WebSocketService();
