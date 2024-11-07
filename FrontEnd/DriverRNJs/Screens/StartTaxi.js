import React, { useState } from "react";
import { View, Text, TouchableOpacity } from "react-native";
import { Checkbox, RadioButton } from "react-native-paper";
import tw from "twrnc";
import { useDispatch, useSelector } from "react-redux";
import { selectUserInfo, setCurrentLocation } from "../Slices/navSlice";
import { API_BASE_URL } from "@env";
import axios from "axios";

/**
 * @typedef {Object} Coordinate
 * @property {number} lat
 * @property {number} lng
 */

const StartTaxi = ({ navigation }) => {
  const [checked, setChecked] = useState(false);
  const [radioValue, setRadioValue] = useState("A");
  const driverinfo = useSelector(selectUserInfo);

  /** @type {Coordinate[]} */
  const coordinatesArray = [
    { lat: 6.927343251759639, lng: 79.84430490676255 },//OGF
    { lat:  6.899843493436574, lng:  79.85148027253149 },//Kollu
    { lat: 6.817345778234778, lng: 79.88976761288893 },
  ];
 
   
  const [locValue, setLocValue] = useState("0");

  const setCurrentLocationLatLng = (newValue) => {
    setRadioValue(newValue);
    switch (newValue) {
      case 'A':
        setLocValue(0);
        break;
      case 'B':
        setLocValue(1);
        break;
      case 'C':
        setLocValue(2);
        break;
      default:
        break;
    }
    
   
  };

  const handleSubmit = () => {
     
    sendRouteToBackend();
  };

  const sendRouteToBackend = async () => {
    try {
      navigation.navigate("TripRequestList");
      const response = await axios.post(
        `${API_BASE_URL}/driver/driverStatus`,
        {
          driverid: driverinfo.driverid,
          Drivername: driverinfo.drivername,
          currentLon: coordinatesArray[locValue].lng,
          currentLat: coordinatesArray[locValue].lat,
          headingLat: 0,
          headingLon: 0,
          vehicletype: driverinfo.vehicletype,
          taxiStatus: 1,
          onService: 1,
          phone: driverinfo.phone,
          nic: driverinfo.nic,
        }
      );

      console.log("Request successful:", response.data);
      navigation.navigate("TripRequestList");
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <View style={tw`flex-1 justify-center items-center p-5`}>
      <TouchableOpacity
        style={tw`bg-blue-500 rounded-full py-3 px-6`}
        onPress={handleSubmit}
      >
        <Text style={tw`text-white text-lg font-bold`}>Start Taxi</Text>
      </TouchableOpacity>

      <View style={tw`mt-5 flex-row items-center`}>
        <Checkbox
          status={checked ? "checked" : "unchecked"}
          onPress={() => setChecked(!checked)}
          color="#1D4ED8"
        />
        <Text style={tw`ml-2 text-base text-gray-800`}>Heading Home</Text>
      </View>

      <View style={tw`mt-7`}>
        <Text style={tw`text-lg font-semibold mb-2`}>Select an option:</Text>
        <RadioButton.Group
          onValueChange={(newValue) => setCurrentLocationLatLng(newValue)}
          value={radioValue}
        >
          <View style={tw`flex-row items-center mb-2`}>
            <RadioButton value="A" color="#1D4ED8" />
            <Text style={tw`ml-2 text-gray-800`}>Location A</Text>
          </View>
          <View style={tw`flex-row items-center mb-2`}>
            <RadioButton value="B" color="#1D4ED8" />
            <Text style={tw`ml-2 text-gray-800`}>Location B</Text>
          </View>
          <View style={tw`flex-row items-center mb-2`}>
            <RadioButton value="C" color="#1D4ED8" />
            <Text style={tw`ml-2 text-gray-800`}>Location C</Text>
          </View>
        </RadioButton.Group>
      </View>


      <TouchableOpacity
        style={tw`bg-red-500 rounded-full py-3 px-6 mt-5`}
        onPress={handleSubmit}
      >
        <Text style={tw`text-white text-lg font-bold`}>Stop Taxi</Text>
      </TouchableOpacity>
    </View>
  );
};

export default StartTaxi;
