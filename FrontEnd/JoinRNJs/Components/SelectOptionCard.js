import React from "react";
import {
  FlatList,
  Image,
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import tw from "twrnc";
import { Icon } from "react-native-elements";
import { useSelector, useDispatch } from "react-redux";
import axios from "axios";
import { API_BASE_URL } from "@env";
import {
  selectDestination,
  selectFarRoute,
  selectOrigin,
  selectRideType,
  selectScheduleTime,
  selectUserType,
  selectUserInfo,
  clearJoinList,
  addJoinList
} from "../slices/navSlice";

const SelectOptionCard = ({ navigation }) => {
  const origin = useSelector(selectOrigin);
  const destination = useSelector(selectDestination);
  const farRoute = useSelector(selectFarRoute);
  const rideType = useSelector(selectRideType);
  const scheduleTime = useSelector(selectScheduleTime);
  const userType = useSelector(selectUserType);
  const userInfo = useSelector(selectUserInfo);


  const dispatch = useDispatch(); 

  const vehicleType = 0;
  const SegmentDistanceKm = 10;

  const sendRouteToBackend = async () => {
    try {
      const pickupLat = origin.location.lat;
      const pickupLng = origin.location.lng;
      const destLat = destination.location.lat;
      const destLng = destination.location.lng;
      //segmentDistanceKm= SegmentDistanceKm;
      const destinationName = destination.description;
      const apiridetype = rideType.ridetype;
      var scheduletime = apiridetype == 4 ? scheduleTime?.datetime : null;
      var routeCoordinates = apiridetype == 3 ? farRoute.route : null; 
      const usertype = userType.type;
      const userinfo = usertype == 2 ? userInfo.primaryUserInfo : null;

      const randomFourDigitNumber = Math.floor(1000 + Math.random() * 9000);
      const randomUser = "userTest"+randomFourDigitNumber;
      const useridapi = usertype == 2 ? userInfo.userid: randomUser ;

      //console.log(`${API_BASE_URL}/passenger/createRideRequest`);
      console.log("userinfo:  ",userinfo);
      //console.log("long rout:  ",routeCoordinates);

      
      const response = await axios.post(
        `${API_BASE_URL}/passenger/createRideRequest`,
        {
          userId: useridapi,
          startLat: pickupLat,
          startLon: pickupLng,
          destLat: destLat,
          destLon: destLng,
          desplace_id: destinationName,
          longRoute: routeCoordinates,
          reqVehicletype: vehicleType,
          SegmentDistance: SegmentDistanceKm,
          tripType: apiridetype,
          scheduleTime: scheduletime,
          userType: usertype,
          userInfo: userinfo
        }
      );
     
      dispatch(clearJoinList());
      clearJoinList

      if (response.data) {
        console.log("Got a Response:", response.data);
        
       if(apiridetype == 2 ){
       
        const itemsArray = response.data.joinList; 
        if(itemsArray){
          itemsArray.forEach((item) => {
          dispatch(addJoinList(item)); 
        })}
        
      
      navigation.navigate("JoinListScreen");
   
       }else if(apiridetype == 3){

        const itemsArray = response.data.farRouteSegs; 
        console.log(itemsArray);

       }else if(apiridetype == 4){

       }
 

      } else {
        console.log("Ride request unsuccessful:", response.data.message);
        // Optional handling for specific cases in the response
      }
    } catch (error) {
      console.error("Error sending route to backend:", error);
    }
  };

  return (
    <View style={tw`m-3`}>
      <TouchableOpacity style={tw`mt-30 mx-30`} onPress={sendRouteToBackend}>
        <Text style={tw` text-lg font-semibold`}>Get a Ride</Text>
        <Icon
          style={tw`p-2 bg-black rounded-full w-10 mt-4`}
          name="arrowright"
          color="white"
          type="antdesign"
        />
      </TouchableOpacity>
    </View>
  );
};

export default SelectOptionCard;
