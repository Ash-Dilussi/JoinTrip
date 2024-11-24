import React, { useEffect, useState, useCallback } from "react";
import {  View } from 'react-native'
import { useSelector,useDispatch  } from "react-redux";
import { 
 
  selectUserInfo,
} from "../slices/navSlice";
import WebSocketService from '../Components/WebSocketService'
import tw from 'twrnc';
import Map from '../Components/Map';

const RidersViewScreen = ({navigation}) =>{
  const userInfo = useSelector(selectUserInfo); 
  const dispatch = useDispatch();

  useEffect(() => {
    
    WebSocketService.connect(userInfo,dispatch);


    return () => {
      WebSocketService.disconnect();
     //if (wsService.stompClient) wsService.stompClient.deactivate();
    };
  }, []);

  return (
    <View style={tw`h-2/3`}>
    <Map />
  </View>
  )
}

export default RidersViewScreen