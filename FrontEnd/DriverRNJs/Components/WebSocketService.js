import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { API_BASE_URL } from "@env";
import { useDispatch, useSelector } from "react-redux";
import {  addTripreqList } from "../Slices/navSlice";
const WEBSOCKET_URL = API_BASE_URL;

class WebSocketService {
 
  constructor() {
    this.stompClient = null;
    
  }

  // Connect to the WebSocket and set up subscriptions
  connect(userInfo) {
    const socket = new SockJS(WEBSOCKET_URL);
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log(str),
      onConnect: () => {
        console.log('Connected to WebSocket');
        const dispatch = useDispatch(); 
        
        this.stompClient.subscribe(`/topic/driver/updates/${userInfo.driverid}`, (message) => {
       
          const parsedMessage = JSON.parse(message.body);
      
          dispatch(addTripreqList(parsedMessage)); 
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
