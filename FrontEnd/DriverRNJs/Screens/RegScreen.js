import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  ScrollView,
} from "react-native";
import { RadioButton } from "react-native-paper";
import tw from "twrnc";
import FloatingLabelInput from "../Components/FloatingLabelInput";
import { useDispatch, useSelector } from "react-redux";
import { setDriverHomeTown, setUserInfo } from "../Slices/navSlice";
import { GooglePlacesAutocomplete } from "react-native-google-places-autocomplete";
import { KeyboardAwareScrollView } from "react-native-keyboard-aware-scroll-view";

import { GOOGLE_MAPS_APIKEY } from "@env";

const RegScreen = ({ navigation }) => {
  const [driverName, setDriverName] = useState("John Doily");
  const [phone, setPhone] = useState("01412542");
  const [nic, setNic] = useState("12145XXX");
  const [addressLine1, setAddressLine1] = useState("sdfss");
  const [addressLine2, setAddressLine2] = useState("dfddf");
  const [homeTown, setHomeTown] = useState("London");
  const [vehicleType, setVehicleType] = useState("2");

  const dispatch = useDispatch();

  const handleRegister = () => {
    const currentTimeMillis = Date.now();
    const millisString = currentTimeMillis.toString();
    const startIndex = Math.floor(millisString.length / 2) - 2;
    const middleFourDigits = millisString.substring(startIndex, startIndex + 4);

    const userid = driverName.trim() + middleFourDigits;

    dispatch(
      setUserInfo({
        id: 0,
        driverid: userid,
        drivername: driverName,
        addressline1: addressLine1,
        addressline2: addressLine2,
        town: homeTown,
        vehicletype: vehicleType,
        nic: nic,
        phone: phone,
      })
    );
    console.log(userid + " : " + vehicleType);
    navigation.navigate("StartTaxi");
  };

  return (
    <View
     // contentContainerStyle={tw`flex-1 items-center p-5 bg-gray-100`}
     style={tw`h-full m-4`}
    >
      <Text style={tw`text-2xl font-bold m-6`}>Register</Text>

      <View style={tw`w-full`}>
        <FloatingLabelInput
          label="Driver Name"
          value={driverName}
          onChangeText={setDriverName}
          containerStyle={tw`border border-gray-300 rounded-md p-2 mb-4`}
          inputStyles={tw`h-12`}
          labelStyles={tw`text-gray-600`} // Customize label styles if needed
        />

        <FloatingLabelInput
          label="Phone"
          value={phone}
          onChangeText={setPhone}
          containerStyle={tw`border border-gray-300 rounded-md p-2 mb-4`}
          inputStyles={tw`h-12`}
          labelStyles={tw`text-gray-600`} // Customize label styles if needed
        />

        <FloatingLabelInput
          label="NIC"
          value={nic}
          onChangeText={setNic}
          containerStyle={tw`border border-gray-300 rounded-md p-2 mb-4`}
          inputStyles={tw`h-12`}
          labelStyles={tw`text-gray-600`} // Customize label styles if needed
        />

<View style={tw`mt-4 mb-5`}>
          <GooglePlacesAutocomplete
            styles={{
              container: { flex: 0 ,   borderWidth: 1,  borderColor: '#ccc',    borderRadius: 8, },
              textInput: { fontSize: 18, backgroundColor: "#F5F5F5" },
              textInputContainer: { paddingHorizontal: 20, paddingBottom: 1 },
            }}
            fetchDetails={true}
            returnKeyType={"search"}
            query={{
              key: GOOGLE_MAPS_APIKEY,
              language: "en",
            }}
            onPress={(data, details = null) => {
              if (details) {
                setHomeTown(data.description);

                dispatch(setDriverHomeTown({
                  location: details.geometry.location,
                  description: data.description,
                }))
              }
              
            }}
            placeholder="Your Home Town"
            debounce={400}
          />
        </View>
        
        <FloatingLabelInput
          label="Address Line 1"
          value={addressLine1}
          onChangeText={setAddressLine1}
          containerStyle={tw`border border-gray-300 rounded-md p-2 mb-4`}
          inputStyles={tw`h-12`}
          labelStyles={tw`text-gray-600`} // Customize label styles if needed
        />
        <FloatingLabelInput
          label="Address Line 2"
          value={addressLine2}
          onChangeText={setAddressLine2}
          containerStyle={tw`border border-gray-300 rounded-md p-2 mb-4`}
          inputStyles={tw`h-12`}
          labelStyles={tw`text-gray-600`} // Customize label styles if needed
        />
        
      </View>

      <Text style={tw`text-lg mb-2`}>Vehicle Type:</Text>
      <View style={tw`flex-row w-full mb-3`}>
        <RadioButton.Group
          onValueChange={(newValue) => setVehicleType(newValue)}
          value={vehicleType}
        >
          <View style={tw`flex-row items-center mr-5`}>
            <RadioButton value="1" />
            <Text>Bike</Text>
          </View>
          <View style={tw`flex-row items-center mr-5`}>
            <RadioButton value="2" />
            <Text>Tuk</Text>
          </View>
          <View style={tw`flex-row items-center`}>
            <RadioButton value="3" />
            <Text>Car</Text>
          </View>
        </RadioButton.Group>
      </View>

      <TouchableOpacity
        onPress={handleRegister}
        style={tw`w-full p-3 bg-blue-500 rounded mt-5`}
      >
        <Text style={tw`text-white text-center font-bold`}>Register</Text>
      </TouchableOpacity>
    </View>
  );
};

export default RegScreen;
