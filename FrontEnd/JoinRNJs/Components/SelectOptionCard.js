import React, { useState, useEffect  } from 'react';
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
  addJoinList,
  selectSegmentDistanceKm
} from "../slices/navSlice";
import carImage from "../assets/joinCar.jpg";
import tukImage from "../assets/joinTuk.jpg";
import bikeImage from "../assets/joinBike.jpg";
import onePersonImage from "../assets/onePerson.jpg";
import manyPeopleImage from "../assets/manyPeople.jpg";

const vehicleTypes = [
  {
    id: "1",
    title: "Join Bike",
    image: bikeImage, 
    vehicletype: "1",
    noPerson:1,
    pricekm:80,
  },
  {
    id: "2",
    title: "Join Tuk",
    image: tukImage, 
    vehicletype: "2",
    noPerson:3,
    pricekm:100,
  },
  {
    id: "3",
    title: "Join Car",
    image: carImage, 
    vehicletype: "3",
    noPerson:4,
    pricekm:140,
  },
];

const SelectOptionCard = ({ navigation }) => {
  const origin = useSelector(selectOrigin);
  const destination = useSelector(selectDestination);
  const farRoute = useSelector(selectFarRoute);
  const rideType = useSelector(selectRideType);
  const scheduleTime = useSelector(selectScheduleTime);
  const userType = useSelector(selectUserType);
  const userInfo = useSelector(selectUserInfo);
  const SegmentDistanceKm = useSelector(selectSegmentDistanceKm);

  const dispatch = useDispatch();

  const [selectedId, setSelectedId] = useState(2);
  const [vehicleType, setVehicleType] = useState(2);
  const [tripDistance, setTripDistance] = useState(2);
  const [priceperkm, setPriceperkm] = useState(2);
  const [tripPrice, setTripPrice] = useState(0);
   



  useEffect(() => {
     
    setTripDistance(haversineDistance(origin.location,destination.location).toFixed(1))
 
  }, [origin,destination])

  useEffect(() => {
     
    setTripPrice(()=>{
      return tripDistance*priceperkm;
    })
 
  }, [tripDistance,vehicleType])


  function haversineDistance(coords1, coords2) {
    const toRad = (value) => (value * Math.PI) / 180; // Convert degrees to radians
  
    const lat1 = coords1.lat;
    const lon1 = coords1.lng;
    const lat2 = coords2.lat;
    const lon2 = coords2.lng;
  
    const R = 6371; // Radius of the Earth in kilometers
    const dLat = toRad(lat2 - lat1);
    const dLon = toRad(lon2 - lon1);
  
    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2);
  
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  
    return R * c; // kilometers
  }

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
      const randomUser = "userTest" + randomFourDigitNumber;
      const useridapi = usertype == 2 ? userInfo.primaryUserInfo.userid : randomUser;

      //console.log(`${API_BASE_URL}/passenger/createRideRequest`);
     // console.log("userinfo:  ", userinfo);
      console.log("vehicleinfo:  ", useridapi);
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
          segmentDistance: SegmentDistanceKm,
          tripType: apiridetype,
          scheduleTime: scheduletime,
          userType: usertype,
          userInfo: userinfo,
        }
      );

      dispatch(clearJoinList());
      clearJoinList;

      if (response.data) {
        console.log("Got a Response:", response.data);

        if (apiridetype == 2) {
          const itemsArray = response.data.joinList;
          if (itemsArray) {
            itemsArray.forEach((item) => {
              dispatch(addJoinList(item));
            });
          }

          navigation.navigate("JoinListScreen");
        } else if (apiridetype == 3) {
          const itemsArray = response.data.farRouteSegs;
          console.log(itemsArray);
        } else if (apiridetype == 4) {
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
    <View style={tw`mt-4 p-1`}>
      <FlatList
        data={vehicleTypes}
        horizontal 
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <TouchableOpacity onPress={() => {

            setVehicleType(item.vehicletype);
            setSelectedId(item.id);
            setPriceperkm(item.pricekm);
        

          }} 
          //style={tw`m-2 w-40 pl-6 p-2`}
          
          style={[
            tw`p-3 pl-10 w-40 m-2 bg-white rounded-lg`,  
            item.id === selectedId
              ? tw`border-2 border-blue-500 shadow-lg` 
              : tw`border border-gray-300`, 
          ]}>
         
            <View>
              <Image
                style={{ width: 80, height: 80, resizeMode: "contain" }}
                source={item.image}
              />
            </View>
            <Text style={tw`mt-2 text-base font-semibold`}>{item.title}</Text>
            <View style={tw`flex-row items-center`}>
              <Image
                style={{ width: 20, height: 20, resizeMode: "contain" }}
                source={item.noPerson ==1 ? onePersonImage: manyPeopleImage}
              />
                <Text style={tw`ml-2`}>
              {item.noPerson}
            </Text>
            </View>

          </TouchableOpacity>
        )}
      />
      <View style={tw`m-2`}>
      <View style={tw`m-2`}> 
      <Text>Journey Distance :      {tripDistance} km</Text>
      <Text>Journey Price       :      {tripPrice} Rs. </Text>
      <Text style={tw `text-gray-500`}>(If join with another You can share the Cost)</Text>
      </View>
        <TouchableOpacity style={tw`mt-3 mx-30`} onPress={sendRouteToBackend}>
          <Text style={tw` text-lg font-semibold`}>Get a Ride</Text>
          <Icon
            style={tw`p-2 bg-black rounded-full w-10 mt-4`}
            name="arrowright"
            color="white"
            type="antdesign"
          />
        </TouchableOpacity>
      </View>
    </View>
  );
};

export default SelectOptionCard;
