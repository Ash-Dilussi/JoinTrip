import { TextDecoder, TextEncoder } from "text-encoding";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { WEBSOCKET_URL } from "@env";

import { addTripreqList } from "../Slices/navSlice";

// Polyfill for TextDecoder and TextEncoder in React Native
if (typeof global.TextDecoder === "undefined") {
  global.TextDecoder = TextDecoder;
}

if (typeof global.TextEncoder === "undefined") {
  global.TextEncoder = TextEncoder;
}

class WebSocketService {
  constructor() {
    this.stompClient = null;
  }

  // Connect to the WebSocket and set up subscriptions
  connect(userInfo, dispatch) {
    const socket = new SockJS(WEBSOCKET_URL);
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log(str),
      onConnect: () => {
        console.log("Connected to server at", socket.url);

        this.stompClient.subscribe(
          `/specific/driver/updates/${userInfo.driverid}`,
          (message) => {
            if (message && message.body) {
              const parsedMessage = JSON.parse(message.body);
              console.log("parsedmsg:", parsedMessage);
              dispatch(addTripreqList({parsedMessage}));
            }
          }
        );
      },
      onDisconnect: () => {
        console.log("Disconnected from WebSocket");
      },
      reconnectDelay: 500, // Reconnect if disconnected: delay
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
