import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import {API_BASE_URL} from "@env";
import { useDispatch, useSelector } from "react-redux";
import { selectUserInfo } from "../Slices/navSlice";
 
const WEBSOCKET_URL = API_BASE_URL;  
const userInfo = useSelector(selectUserInfo);
const webSocketService = new WebSocketService(userInfo);

class WebSocketService {

   
  constructor() {
    this.stompClient = null;
    this.userInfo = userInfo;
  }

  // connection topics
  connect( onDriverUpdate) {
    const socket = new SockJS(WEBSOCKET_URL);
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log(str),
      onConnect: () => {
        console.log('Connected to WebSocket');

      
        this.stompClient.subscribe(`/topic/driver/updates/${this.userInfo.driverid}`, (message) => {
          onDriverUpdate(JSON.parse(message.body));
        });

      },
      onDisconnect: () => {
        console.log('Disconnected from WebSocket');
      },
      reconnectDelay: 100, // Reconnect if disconnected: delay
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
