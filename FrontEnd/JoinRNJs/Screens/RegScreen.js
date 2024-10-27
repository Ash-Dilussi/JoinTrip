import React, { useState } from 'react';
import { Alert, View, Text, TextInput, TouchableOpacity, ScrollView } from 'react-native';
import { Ionicons } from '@expo/vector-icons'; // Using Ionicons for radio button icons
import tw from 'twrnc';
import { useDispatch, useSelector } from 'react-redux';
import { setUserInfo } from '../slices/navSlice';


const RegScreen = ({ navigation }) => {
  const [firstName, setFirstName] = useState('Diddy');
  const [lastName, setLastName] = useState('Bieber');
  const [addressline1, setAddressline1] = useState('road 1');
  const [addressline2, setAddressline2] = useState('main road');
  const [phoneNumber, setPhoneNumber] = useState('077874654');
  const [homeTown, setHomeTown] = useState('Cold Water');
  const [NIC, setNIC] = useState('1256121XXX');
  const [gender, setGender] = useState(null); 

  const dispatch = useDispatch(); 

  const handleRegister = () => {
    
    const currentTimeMillis = Date.now(); 
    const millisString = currentTimeMillis.toString();
    const startIndex = Math.floor(millisString.length / 2) - 2;
    const middleFourDigits = millisString.substring(startIndex, startIndex + 4);
  
    const userid = lastName + middleFourDigits;

    if (!NIC || !phoneNumber || !lastName|| !homeTown) {
      Alert.alert("Please Fill Essential Fields.");
      return;
    }
    const primaryUser={
id: 0,
  userid: userid,
  firstName:firstName,
  lastName:lastName,
  addressline1:addressline1,
  addressline2:addressline2,
  town:homeTown,
  gender:gender,
  nic:NIC,
  phone:phoneNumber
    }
dispatch(setUserInfo({
  primaryUserInfo: primaryUser
}))

    console.log('Registering user with details:', {
      userid,
      firstName,
      lastName,
      addressline1,
      addressline2,
      phoneNumber,
      homeTown,
      NIC,
      gender,  
    });

navigation.navigate("HomeScreen");
  };

  return (
    <ScrollView style={tw`flex-1 bg-gray-100`} contentContainerStyle={tw`justify-center items-center p-6`}>
      <Text style={tw`text-2xl font-bold mb-6`}>Register</Text>

      <TextInput
        style={tw`border border-gray-300 rounded-lg px-4 py-2 mb-4 w-80`}
        placeholder="First Name"
        value={firstName}
        onChangeText={setFirstName}
      />

      <TextInput
        style={tw`border border-gray-300 rounded-lg px-4 py-2 mb-4 w-80`}
        placeholder="Last Name"
        value={lastName}
        onChangeText={setLastName}
      />

      <TextInput
        style={tw`border border-gray-300 rounded-lg px-4 py-2 mb-4 w-80`}
        placeholder="Addressline 1"
        value={addressline1}
        onChangeText={setAddressline1}
      />

      <TextInput
        style={tw`border border-gray-300 rounded-lg px-4 py-2 mb-4 w-80`}
        placeholder="Addressline 2"
        value={addressline2}
        onChangeText={setAddressline2}
      />

      <TextInput
        style={tw`border border-gray-300 rounded-lg px-4 py-2 mb-4 w-80`}
        placeholder="Phone Number"
        keyboardType="phone-pad"
        value={phoneNumber}
        onChangeText={setPhoneNumber}
      />

      <TextInput
        style={tw`border border-gray-300 rounded-lg px-4 py-2 mb-4 w-80`}
        placeholder="Home Town"
        value={homeTown}
        onChangeText={setHomeTown}
      />

      <TextInput
        style={tw`border border-gray-300 rounded-lg px-4 py-2 mb-6 w-80`}
        placeholder="NIC"
        value={NIC}
        onChangeText={setNIC}
      />

       
      <View style={tw`w-80 mb-4`}>
        <Text style={tw`mb-2 text-lg`}>Gender</Text>

        <View style={tw`flex-row items-center mb-2`}>
          <TouchableOpacity onPress={() => setGender('M')} style={tw`flex-row items-center`}>
            <Ionicons
              name={gender === 'M' ? 'radio-button-on' : 'radio-button-off'}
              size={20}
              color={gender === 'M' ? 'blue' : 'gray'}
            />
            <Text style={tw`ml-2 text-gray-400`}>Male</Text>
          </TouchableOpacity>
        </View>

        <View style={tw`flex-row items-center mb-2`}>
          <TouchableOpacity onPress={() => setGender('F')} style={tw`flex-row items-center`}>
            <Ionicons
              name={gender === 'F' ? 'radio-button-on' : 'radio-button-off'}
              size={20}
              color={gender === 'F' ? 'blue' : 'gray'}
            />
            <Text style={tw`ml-2 text-gray-400`}>Female</Text>
          </TouchableOpacity>
        </View>
      </View>

      <TouchableOpacity
        style={tw`bg-blue-500 rounded-lg px-4 py-2 w-80 mb-4`}
        onPress={handleRegister}
      >
        <Text style={tw`text-white text-center`}>Register</Text>
      </TouchableOpacity>
    </ScrollView>
  );
};

export default RegScreen;
