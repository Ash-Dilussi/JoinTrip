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

const RegScreen = () => {
  const [driverName, setDriverName] = useState("");
  const [phone, setPhone] = useState("");
  const [nic, setNic] = useState("");
  const [addressLine1, setAddressLine1] = useState("");
  const [addressLine2, setAddressLine2] = useState("");
  const [homeTown, setHomeTown] = useState("");
  const [vehicleType, setVehicleType] = useState("");

  const handleRegister = () => {
    // Handle registration logic here
    //  console.log({ driverName, phone, nic, addressLine1, addressLine2, homeTown, vehicleType });
  };

  return (
    <ScrollView contentContainerStyle={tw`flex-1 items-center p-5 bg-gray-100`}>
      <Text style={tw`text-2xl font-bold mb-5`}>Register</Text>

      <View style={tw`w-full`}>
        <FloatingLabelInput
          label="Driver Name"
          value={driverName}
          onChangeText={setDriverName}
          containerStyle={tw`border border-gray-300 rounded-md p-2 mb-4`} // Container styles for visibility
          inputStyles={tw`h-12`} // Set a height for the input
          labelStyles={tw`text-gray-600`} // Customize label styles if needed
        />

        <FloatingLabelInput
          label="Phone"
          value={phone}
          onChangeText={setPhone}
          containerStyle={tw`border border-gray-300 rounded-md p-2 mb-4`} // Container styles for visibility
          inputStyles={tw`h-12`} // Set a height for the input
          labelStyles={tw`text-gray-600`} // Customize label styles if needed
        />

        <FloatingLabelInput
          label="NIC"
          value={nic}
          onChangeText={setNic}
          containerStyle={tw`border border-gray-300 rounded-md p-2 mb-4`} // Container styles for visibility
          inputStyles={tw`h-12`} // Set a height for the input
          labelStyles={tw`text-gray-600`} // Customize label styles if needed
        />
        <FloatingLabelInput
          label="Address Line 1"
          value={addressLine1}
          onChangeText={setAddressLine1}
          containerStyle={tw`border border-gray-300 rounded-md p-2 mb-4`} // Container styles for visibility
          inputStyles={tw`h-12`} // Set a height for the input
          labelStyles={tw`text-gray-600`} // Customize label styles if needed
        />
        <FloatingLabelInput
          label="Address Line 2"
          value={addressLine2}
          onChangeText={setAddressLine2}
          containerStyle={tw`border border-gray-300 rounded-md p-2 mb-4`} // Container styles for visibility
          inputStyles={tw`h-12`} // Set a height for the input
          labelStyles={tw`text-gray-600`} // Customize label styles if needed
        />
        <FloatingLabelInput
          label="Home Town"
          value={homeTown}
          onChangeText={setHomeTown}
          containerStyle={tw`border border-gray-300 rounded-md p-2 mb-4`} // Container styles for visibility
          inputStyles={tw`h-12`} // Set a height for the input
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
            <RadioButton value="Car" />
            <Text>Car</Text>
          </View>
          <View style={tw`flex-row items-center mr-5`}>
            <RadioButton value="Van" />
            <Text>Van</Text>
          </View>
          <View style={tw`flex-row items-center`}>
            <RadioButton value="Bike" />
            <Text>Bike</Text>
          </View>
        </RadioButton.Group>
      </View>

      {/* <TextInput
        placeholder="Driver Name"
        value={driverName}
        onChangeText={setDriverName}
        style={tw`w-full p-3 bg-white rounded mb-3 border border-gray-300`}
      /> */}

      {/* <TextInput
        placeholder="Phone"
        value={phone}
        onChangeText={setPhone}
        style={tw`w-full p-3 bg-white rounded mb-3 border border-gray-300`}
        keyboardType="phone-pad"
      /> */}

      {/* <TextInput
        placeholder="NIC"
        value={nic}
        onChangeText={setNic}
        style={tw`w-full p-3 bg-white rounded mb-3 border border-gray-300`}
      /> */}

      {/* <TextInput
        placeholder="Address Line 1"
        value={addressLine1}
        onChangeText={setAddressLine1}
        style={tw`w-full p-3 bg-white rounded mb-3 border border-gray-300`}
      />

      <TextInput
        placeholder="Address Line 2"
        value={addressLine2}
        onChangeText={setAddressLine2}
        style={tw`w-full p-3 bg-white rounded mb-3 border border-gray-300`}
      />

      <TextInput
        placeholder="Home Town"
        value={homeTown}
        onChangeText={setHomeTown}
        style={tw`w-full p-3 bg-white rounded mb-3 border border-gray-300`}
      /> */}

      <TouchableOpacity
        onPress={handleRegister}
        style={tw`w-full p-3 bg-blue-500 rounded mt-5`}
      >
        <Text style={tw`text-white text-center font-bold`}>Register</Text>
      </TouchableOpacity>
    </ScrollView>
  );
};

export default RegScreen;
