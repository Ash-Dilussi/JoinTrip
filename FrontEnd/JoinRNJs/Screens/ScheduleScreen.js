import React from 'react';
import { View, Text } from 'react-native';
import tw from 'twrnc';
import { useSelector } from "react-redux";
import {
   selectScheduleTime,
   selectDestination,
   selectOrigin
  } from "../slices/navSlice";


const ScheduleScreen = () => {

const taxiScheduleTime= useSelector(selectScheduleTime);
const destination = useSelector(selectDestination);
const origin = useSelector(selectOrigin);
const formattedScheduleTime =   taxiScheduleTime?.datetime   ? new Date(taxiScheduleTime.datetime).toLocaleString(): null ;
  return (
    <View style={tw`flex-1 justify-center items-center bg-gray-100`}>
     
      <Text style={tw`text-base`}>Tour Taxi Scheduled</Text>
      <Text style={tw`text-lg font-bold mt-4 mb-2`}>{formattedScheduleTime}</Text>
      <Text style={tw`text-base mt-4`}>from</Text>
      <Text style={tw`text-lg font-bold mb-2`}>{origin.description}</Text>
      <Text style={tw`text-base`}>to</Text>
      <Text style={tw`text-lg font-bold mb-2`}>{destination.description}</Text>
    </View>
  );
};

export default ScheduleScreen;
