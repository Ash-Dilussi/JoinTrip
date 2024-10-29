import React, { useEffect, useState } from 'react';
import { View, Text, Button } from 'react-native';
import WebSocketService from '../Components/WebSocketService';

const TripRequestList = () => { 
  const [driverUpdate, setDriverUpdate] = useState(null);

  useEffect(() => {
    // Connect to WebSocket and handle incoming messages
    WebSocketService.connect(
      (request) => setPassengerRequest(request),
      (notification) => setPassengerNotification(notification),
      (update) => setDriverUpdate(update)
    );

    
    return () => {
      WebSocketService.disconnect();
    };
  }, []);

  return (
    <View>
      <Text>Passenger Request: {JSON.stringify(passengerRequest)}</Text>
      <Text>Passenger Notification: {JSON.stringify(passengerNotification)}</Text>
      <Text>Driver Update: {JSON.stringify(driverUpdate)}</Text>
      <Button title="Reconnect" onPress={() => WebSocketService.connect()} />
    </View>
  );
};

export default TripRequestList;
