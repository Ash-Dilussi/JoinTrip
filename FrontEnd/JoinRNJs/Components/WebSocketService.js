import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { WEBSOCKET_URL } from "@env";
import { useDispatch, useSelector } from "react-redux";
import {} from "../slices/navSlice";
 

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
        debug: (str) => console.log("11",str, socket),
        onConnect: () => {
          console.log("Connected to WebSocket:",userInfo.primaryUserInfo.userid);
         

          this.stompClient.subscribe(
            `/topic/passenger/requests/${userInfo.primaryUserInfo.userid}`,
            (message) => {
              // const parsedMessage = JSON.parse(message.body);
              console.log("parsedMessage:" ,message.body);
            }
          );
        },
        onDisconnect: () => {
          console.log("Disconnected from WebSocket");
        },
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
