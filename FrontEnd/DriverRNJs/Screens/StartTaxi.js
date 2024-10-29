import React, { useState } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { Checkbox, RadioButton } from 'react-native-paper';
import tw from 'twrnc';

const StartTaxi = ({navigation}) => {
  const [checked, setChecked] = useState(false);
  const [radioValue, setRadioValue] = useState('A');

  const handleSubmit = () => {
    // Handle the submit action here
    console.log('Form submitted:', { 
      headingHome: checked, 
      selectedLocation: radioValue 
    });
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
          status={checked ? 'checked' : 'unchecked'}
          onPress={() => setChecked(!checked)}
          color="#1D4ED8"
        />
        <Text style={tw`ml-2 text-base text-gray-800`}>Heading Home</Text>
      </View>

      <View style={tw`mt-7`}>
        <Text style={tw`text-lg font-semibold mb-2`}>Select an option:</Text>
        <RadioButton.Group onValueChange={newValue => setRadioValue(newValue)} value={radioValue}>
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
    </View>
  );
};

export default StartTaxi;
