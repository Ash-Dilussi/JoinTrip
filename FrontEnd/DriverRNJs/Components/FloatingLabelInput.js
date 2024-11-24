import React, { useState, useRef, useEffect } from 'react';
import { View, TextInput, Animated, Text } from 'react-native';
import tw from 'twrnc';

const FloatingLabelInput = ({ label, value, onChangeText }) => {
  const [isFocused, setIsFocused] = useState(false);
   
  const animatedIsFocused = useRef(new Animated.Value(0)).current;

  const handleFocus = () => setIsFocused(true); 

  React.useEffect(() => {
    Animated.timing(animatedIsFocused, {
      toValue: isFocused || value ? 1 : 0,
      duration: 200,
      useNativeDriver: false,
    }).start();
  }, [isFocused, value]);

  const labelStyle = {
    position: 'absolute',
    left: 10,
    top: animatedIsFocused.interpolate({
      inputRange: [0, 1],
      outputRange: [18, -8],
    }),
    fontSize: animatedIsFocused.interpolate({
      inputRange: [0, 1],
      outputRange: [16, 12],
    }),
    color: animatedIsFocused.interpolate({
      inputRange: [0, 1],
      outputRange: ['#aaa', '#333'],
    }),
  };

  return (
    <View style={tw`mt-6`}>
      <Animated.Text style={[labelStyle, tw`text-gray-500`]}>
        {label}
      </Animated.Text>
      <TextInput
        value={value}
        onChangeText={onChangeText}
        onFocus={() => setIsFocused(true)}
        onBlur={() => setIsFocused(value ? true : false)}
        style={tw`border-b border-gray-400 py-2 text-lg`}
      />
    </View>
  );
};

export default FloatingLabelInput;
