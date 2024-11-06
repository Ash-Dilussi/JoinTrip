import React, { useState, useRef } from 'react';
import { View, TextInput, Animated, Text } from 'react-native';
import tw from 'twrnc';

const FloatingLabelInput = ({ label }) => {
  const [isFocused, setIsFocused] = useState(false);
  const [inputValue, setInputValue] = useState('');
  const animatedIsFocused = useRef(new Animated.Value(0)).current;

  const handleFocus = () => setIsFocused(true);
  const handleBlur = () => {
    if (!inputValue) setIsFocused(false);
  };

  React.useEffect(() => {
    Animated.timing(animatedIsFocused, {
      toValue: isFocused || inputValue ? 1 : 0,
      duration: 200,
      useNativeDriver: false,
    }).start();
  }, [isFocused, inputValue]);

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
        value={inputValue}
        onChangeText={setInputValue}
        onFocus={handleFocus}
        onBlur={handleBlur}
        style={tw`border-b border-gray-400 py-2 text-lg`}
      />
    </View>
  );
};

export default FloatingLabelInput;
